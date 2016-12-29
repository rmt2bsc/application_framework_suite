package com.api.ldap;

import java.util.Hashtable;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.apache.log4j.Logger;

import com.InvalidDataException;
import com.SystemException;
import com.api.DaoApiStub;
import com.api.config.ConfigConstants;
import com.api.ldap.operation.LdapOperation;
import com.api.persistence.DatabaseException;
import com.util.RMT2File;

/**
 * Abstract class for providing basic functionality for a user to connect and/or
 * bind to a LDAP server as well as common validation tasks.
 * <p>
 * Connections to the LDAP server can be performed as anonymous or
 * authenticated. To connect to the server, a reference to an object that
 * implements the DirContext interface mus be obtained. This is done by using an
 * InitialDirContext object that takes a hashtable as an argument. The Hashtable
 * will contain various entries, such as the hostname, port, and JNDI service
 * provider class to use. Once connected, the client may need to authenticate
 * itself which is also known as "binding" in the world of LDAP.
 * 
 * @author rterrell
 * 
 */
public abstract class AbstractLdapAuthenticationImpl extends DaoApiStub {

    private static final Logger logger = Logger
            .getLogger(AbstractLdapAuthenticationImpl.class);

    private LdapContext dirContext;

    private String ctxFactory;

    private String ctxUrl;

    private String ctxAuthType;

    private String ctxUser;

    private String baseDn;

    private String peopleRdn;

    private String boundUser;

    private String defaultUserIdProperty;

    private boolean connected;

    /**
     * Creates a RMT2AbstractLdapImpl without having any assoication with the
     * LDAP server.
     */
    public AbstractLdapAuthenticationImpl() {
        super();
        dirContext = null;
        this.connected = false;
        return;
    }

    /**
     * 
     * @param rdn
     * @return
     */
    protected String buildDistinguishedName(String rdn) {
        String dn = null;
        if (this.getBaseDn() == null) {
            dn = rdn;
        }
        else {
            dn = rdn + "," + this.getBaseDn();
        }
        return dn;
    }

    /**
     * Establishes a connection to a LDAP server where the authentication
     * credentials are discovered and obtained from an instance of
     * ResourceBundle.
     * <p>
     * The user id and password that is used to connect to LDAP should be found
     * in the input data source so that authentication can be performed. If the
     * user credentials and/or authentication type indicator do not exist then
     * an exception should be thrown. The authencation flag should be set to one
     * of the JNDI LDAP authentication type values.
     * 
     * @param propFile
     *            A Properties file containing all of the LDAP configuration
     *            information needed to establish a connection. If found to be
     *            null, then the default LDAP configuration file,
     *            com.api.security
     *            .authentication.DefaultAuthenticatorLdapConfig.properties,
     *            will be used. This method expects the properties file to be
     *            included in the classpath.
     * @return A connection to the LDAP server which is of type LdapContext
     * @throws RMT2LdapException
     *             Application user and/or the application password is not
     *             found, the authentication type indicator is not set, or a
     *             general directory naming error occurs.
     */
    public LdapContext connect(Object propFile) throws RMT2LdapException {
        // Use the default LDAP Properties file in the event profile is null.
        String configFile = (propFile == null ? LdapClient.DEFAULT_CONFIG_RESOURCE
                : propFile.toString());
        ResourceBundle rb = this.openConfigFile(configFile);
        String userId = null;
        String pw = null;
        try {
            userId = rb.getString("appUser");
            pw = rb.getString("appPassword");
        } catch (SystemException e) {
            this.msg = "Unable to obtain LDAP general application user credentials from LDAP configuration";
            logger.error(this.msg);
            throw new RMT2LdapException(this.msg, e);
        }

        // Capture the user id
        this.ctxUser = userId;

        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, this.ctxFactory);
        env.put(Context.PROVIDER_URL, this.ctxUrl);

        // Determine if the connection will be anonymous or authenticated.
        if (this.ctxAuthType == null) {
            this.msg = "The authentication flag is required to be set for the general LDAP applcation user";
            logger.error(this.msg);
            throw new RMT2LdapException(this.msg);
        }

        // Build distinguished name for user login id
        this.boundUser = userId;
        env.put(Context.SECURITY_PRINCIPAL, userId);
        env.put(Context.SECURITY_CREDENTIALS, pw);

        // Connect to LDAP
        try {
            dirContext = new InitialLdapContext(env, null);
            // Get dummy attribte definition at connection time. This will make
            // subsequent LDAP queries that use this connection to perform more
            // efficiently when having to obtain the definitions of a single
            // attribute.
            Attributes attrs = dirContext.getAttributes("");
            Attribute attr = attrs.get("objectclass");
            attr.getAttributeDefinition();
            this.connected = true;
            return dirContext;
        } catch (NamingException e) {
            this.msg = "Fail to establish a LDAP connection for user, "
                    + this.ctxUser;
            logger.error(this.msg);
            throw new RMT2LdapException(this.msg, e);
        }
    }

    /**
     * Establishes a connection to the LDAP server and optionally binds the
     * specified user to the server.
     * <p>
     * The connection can be anonymous or authenticated provided the specific
     * values are supplied for different environment variables in the Context
     * interface. An authenticated connection will potentially created when the
     * <i>user</i> and <i>password</i> parameters are supplied with non-blank
     * non-null values. Otherwise, the conenction is anonymous.
     * 
     * @param user
     *            The user id of the client that is to be bound to the server.
     *            This value should contain only the user id in its raw form.
     *            (not as an equation). For example, the user, admin, should be
     *            <i>admin</i> instead of <i>loginid=admin</i>.
     * @param password
     *            The password of the client that is to be bound to the server
     * @param propFile
     *            The Properties file containing all of the LDAP configuration
     *            information needed to establish a connection. If found to be
     *            null, then the default LDAP configuration file,
     *            com.api.security
     *            .authentication.DefaultAuthenticatorLdapConfig.properties,
     *            will be used.
     * @return A connection to the LDAP server which is of type LdapContext
     * @throws RMT2LdapException
     */
    public LdapContext connect(String user, String password, Object propFile)
            throws RMT2LdapException {
        // Use the default LDAP Properties file in the event profile is null.
        String configFile = (propFile == null ? LdapClient.DEFAULT_CONFIG_RESOURCE
                : propFile.toString());
        this.openConfigFile(configFile);

        // Capture the user id
        this.ctxUser = user;

        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, this.ctxFactory);
        env.put(Context.PROVIDER_URL, this.ctxUrl);

        // Determine if the connection will be anonymous or authenticated.
        if (this.ctxAuthType != null) {
            logger.info("The user is required to be authenticated when obtaining a LDAP connection.  The security type used is: "
                    + this.ctxAuthType);
            env.put(Context.SECURITY_AUTHENTICATION, this.ctxAuthType);
            if (user == null) {
                this.msg = "Login Id is required in order to bind the user to the LDAP server when obtaining a connection";
                logger.error(this.msg);
                throw new RMT2LdapException(this.msg);
            }
            if (password == null) {
                this.msg = "Password is required in order to bind the user to the LDAP server when obtaining a connection";
                logger.error(this.msg);
                throw new RMT2LdapException(this.msg);
            }
            if (this.defaultUserIdProperty == null) {
                this.msg = "The default user id property must be indentified in "
                        + LdapClient.DEFAULT_CONFIG_RESOURCE;
                logger.error(this.msg);
                throw new RMT2LdapException(this.msg);
            }

            // Build distinguished name for user login id
            this.boundUser = this.defaultUserIdProperty + "=" + user + ","
                    + this.peopleRdn + "," + this.baseDn;
            // String dn = user;
            env.put(Context.SECURITY_PRINCIPAL, this.boundUser);
            env.put(Context.SECURITY_CREDENTIALS, password);
        }

        // Connect to LDAP
        try {
            dirContext = new InitialLdapContext(env, null);
            // Get dummy attribte definition at connection time. This will make
            // subsequent LDAP queries that use this connection to perform more
            // efficiently when having to obtain the definitions of a single
            // attribute.
            Attributes attrs = dirContext.getAttributes("");
            Attribute attr = attrs.get("objectclass");
            attr.getAttributeDefinition();
            this.connected = true;
            return dirContext;
        } catch (NamingException e) {
            this.msg = "Fail to establish a LDAP connection for user, "
                    + this.ctxUser;
            logger.error(this.msg);
            throw new RMT2LdapException(this.msg, e);
        }
    }

    /**
     * Loads the LDAP connection information from the specified LDAP
     * configuration data file, <i>configFile</i>.
     * <p>
     * The LDAP configuration file is required to be found on the classpath of
     * the JVM. The following properties are expected to exist in the LDAP
     * configuration: <br>
     * <ul>
     * <li><b>ldapFactory</b> - the fully qualified class name to instantiate
     * the LDAP factory.</li>
     * <li><b>ldapUrl</b> - the URL of the LDAP server</li>
     * <li><b>ldapAuth</b> - the type of authentication to use. Valid values
     * are: <i>sasl_mech</i>, <i>none</i>, or <i>simple</i>.</li>
     * </ul>
     * 
     * @param configFile
     * @throws RMT2LdapException
     */
    private ResourceBundle openConfigFile(String configFile)
            throws RMT2LdapException {
        if (configFile == null) {
            this.msg = "An invalid or null LDAP configuration file name was detected";
            logger.error(this.msg);
            throw new RMT2LdapException(this.msg);
        }

        // Determine the environment we are working from.
        String env = System.getProperty(ConfigConstants.PROPNAME_ENV);

        // Load the LDAP configuration data into memory.
        ResourceBundle rb = null;
        try {
            rb = RMT2File.loadAppConfigProperties(configFile);
            this.baseDn = rb.getString("baseDN");
            this.peopleRdn = rb.getString("peopleRDN");
            this.ctxFactory = rb.getString("ldapFactory");

            if (env == null
                    || env.equalsIgnoreCase(ConfigConstants.ENVTYPE_PROD)) {
                this.ctxUrl = rb.getString("ldapUrl");
            }
            else if (env.equalsIgnoreCase(ConfigConstants.ENVTYPE_DEV)) {
                this.ctxUrl = rb.getString("ldapUrl.dev");
            }
            else if (env.equalsIgnoreCase(ConfigConstants.ENVTYPE_TEST)) {
                this.ctxUrl = rb.getString("ldapUrl.test");
            }
            else if (env.equalsIgnoreCase(ConfigConstants.ENVTYPE_STAGE)) {
                this.ctxUrl = rb.getString("ldapUrl.stage");
            }

            this.ctxAuthType = rb.getString("ldapAuth");
            if (this.ctxAuthType.equals("")) {
                this.ctxAuthType = null;
            }
            try {
                this.defaultUserIdProperty = rb
                        .getString("defaultUserIdProperty");
            } catch (MissingResourceException e) {
                this.msg = "\"defaultUserIdProperty\" property was not found...";
                logger.warn(this.msg);
            }
        } catch (Exception e) {
            this.msg = "Unable to load LDAP configuration. " + e.getMessage();
            logger.error(this.msg);
            throw new RMT2LdapException(this.msg);
        }
        return rb;
    }

    /**
     * @return the dirContext
     */
    public LdapContext getDirContext() {
        return dirContext;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApiStub#close()
     */
    @Override
    public void close() throws DatabaseException {
        super.close();
        try {
            if (dirContext != null) {
                dirContext.close();
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Validates the common attributes of a LDAP operation.
     * 
     * @param opParms
     * @throws InvalidDataException
     */
    protected void validateOperation(Object opParms)
            throws InvalidDataException {
        if (opParms == null) {
            this.msg = "LDAP operation parameter object cannot be null";
            logger.error(this.msg);
            throw new InvalidDataException(this.msg);
        }

        // A distintquished name is required for all operations.
        if (opParms instanceof LdapOperation) {
            LdapOperation commonOp = (LdapOperation) opParms;
            if (commonOp.getDn() == null) {
                this.msg = "LDAP operation requires a distinguished name";
                logger.error(this.msg);
                throw new InvalidDataException(this.msg);
            }
        }
    }

    /**
     * Locates and returns the actual results of the LDAP query.
     * 
     * @param results
     *            Object array which the first element should be a List
     *            instance. Optionally, can be null.
     * @return An instance of List or null if <i>resluts</i> is null
     * @throws DatabaseException
     */
    public List extractLdapResults(Object results[]) throws DatabaseException {
        if (results == null) {
            return null;
        }
        List list;
        if (results[0] instanceof List) {
            list = (List) results[0];
            return list;
        }
        else {
            this.msg = "It is required that a LDAP query returns an instance of List";
            throw new DatabaseException(this.msg);
        }
    }

    /**
     * @return the baseDn
     */
    public String getBaseDn() {
        return baseDn;
    }

    /**
     * @param baseDn
     *            the baseDn to set
     */
    public void setBaseDn(String baseDn) {
        this.baseDn = baseDn;
    }

    public boolean isConnected() {
        return this.connected;
    }
}
