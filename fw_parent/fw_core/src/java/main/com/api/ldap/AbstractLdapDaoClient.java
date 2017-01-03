package com.api.ldap;

import org.apache.log4j.Logger;

import com.RMT2Base;
import com.api.persistence.DaoClient;
import com.api.persistence.PersistenceClient;

/**
 * An abstract class for creating LDAP DAO clients.
 * 
 * @author Roy Terrell
 * 
 */
public abstract class AbstractLdapDaoClient extends RMT2Base implements
        DaoClient {

    private static final Logger logger = Logger
            .getLogger(AbstractLdapDaoClient.class);

    private String daoUser;

    /**
     * Base DN for this LDAP DAO
     */
    protected static String BASE_DN;

    /**
     * An instance of {@link LdapFactory}
     */
    protected LdapFactory factory;

    /**
     * An instnace of {@link LdapClient} identifying the user connection to the
     * LDAP server.
     */
    protected LdapClient ldap;

    /**
     * Constructs a LDAP DAO client using the default user included in the
     * default LDAP configuration file, <i>config.LdapAuthConfig</i>.
     */
    public AbstractLdapDaoClient() {
        this.factory = new LdapFactory();
        this.ldap = this.factory.createAttributeClient();
        this.ldap.connect(LdapClient.DEFAULT_CONFIG_RESOURCE);
        this.msg = "User was bounded to the LDAP server successfully using the default application user login via the DAO class, "
                + this.getClass().getSimpleName();
        logger.info(this.msg);
    }

    /**
     * Constructs a LDAP DAO client by binding the to the LDAP server using the
     * user's login credentials.
     * 
     * @param user
     *            the value of <i>loginId</i> property of the user's LDAP
     *            profile
     * @param password
     *            the user's password as it pertains to the user's LDAP profile
     */
    public AbstractLdapDaoClient(String user, String password) {
        this.factory = new LdapFactory();
        this.ldap = this.factory.createAttributeClient();
        this.ldap.connect(user, password, LdapClient.DEFAULT_CONFIG_RESOURCE);
        this.msg = "User, "
                + user
                + ", was bounded to the LDAP server successfully via the DAO class, "
                + this.getClass().getSimpleName() + ", using its own profile";
        logger.info(this.msg);
    }

    /**
     * Constructs a LDAP DAO client initialized with a valid instance of
     * LdapClient.
     * 
     * @param con
     *            an instance of {@link LdapClient}
     */
    public AbstractLdapDaoClient(LdapClient con) {
        this.ldap = con;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.persistence.DaoClient#close()
     */
    public void close() {
        this.ldap.close();
        this.ldap = null;
        this.factory = null;
    }

    /**
     * Always return null.
     */
    public String getDaoUser() {
        return this.daoUser;
    }

    /**
     * Stubbed
     */
    public void setDaoUser(String userName) {
        this.daoUser = userName;
    }

    /**
     * Method is not supported for LDAP operations
     */
    @Override
    public void beginTrans() {
        throw new UnsupportedOperationException(
                "This method is not supported for LDAP operations, beginTrans()");

    }

    /**
     * Method is not supported for LDAP operations
     */
    @Override
    public void commitTrans() {
        throw new UnsupportedOperationException(
                "This method is not supported for LDAP operations, commitTrans()");

    }

    /**
     * Method is not supported for LDAP operations
     */
    @Override
    public void rollbackTrans() {
        throw new UnsupportedOperationException(
                "This method is not supported for LDAP operations, rollbackTrans()");
    }

    /**
     * Always return null
     */
    @Override
    public PersistenceClient getClient() {
        return null;
    }

}
