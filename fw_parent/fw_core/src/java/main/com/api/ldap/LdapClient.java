package com.api.ldap;

import java.util.List;

import com.SystemException;
import com.api.DaoApi;
import com.api.persistence.DatabaseException;

/**
 * An interface that acts as a client to an LDAP connection. Callers interact
 * with this interface at an LDAP operation level. That is, the caller invokes a
 * method to do a SEARCH, COMPARE, ADD, UPDATE, DELETE, BIND or UNDBIND
 * operation and get back a result.
 * <p>
 * The implementation should contain the approriate constructor to an instance
 * of this interface. It then needs to use one of the connect() methods to
 * perform an LDAP BIND.
 * 
 * @author rterrell
 * 
 */
public interface LdapClient extends DaoApi {

    /**
     * The full package name of the properties file containing information
     * needed to establish a connection to the LDAP server by an identified
     * user.
     * <p>
     * By default, the value is set to "config.LdapAuthConfig" which is required
     * to be discovered in the classpath.
     */
    static final String DEFAULT_CONFIG_RESOURCE = "config.LdapAuthConfig";

    /**
     * The full package name of the properties file containing information
     * needed to establish a connection to the LDAP server by an unknown user.
     * <p>
     * By default, the value is set to "config.LdapAnonymousConfig" which is
     * required to be discovered in the classpath.
     */
    static final String DEFAULT_ANONYONUS_CONFIG_RESOURCE = "config.LdapAnonymousConfig";

    /**
     * Constant for indicating that an attribute will not change during a
     * modification operation.
     * <p>
     * This value is used as the default for Add operations.
     */
    static final int MOD_OPERATION_NONE = 0;

    /**
     * Constant for adding an attribute to an entry during a modification
     * operation.
     */
    static final int MOD_OPERATION_ADD = 1;

    /**
     * Constant for replacing or updating an attribute to an entry during a
     * modification operation.
     */
    static final int MOD_OPERATION_REPLACE = 2;

    /**
     * Constant for removing an attribute to an entry during a modification
     * operation.
     */
    static final int MOD_OPERATION_REMOVE = 3;

    /**
     * Establishes a connection to a LDAP server where the authentication
     * credentials are discovered and obtained from <i>dataSource</i>.
     * <p>
     * The user id and password that is used to connect to LDAP should be found
     * in the input data source so that authentication can be performed. If the
     * user credentials and/or authentication type indicator do not exist then
     * an exception should be thrown. The authencation flag should be set to one
     * of the JNDI LDAP authentication type values.
     * 
     * @param dataSource
     * @return
     * @throws DatabaseException
     */
    Object connect(Object dataSource) throws DatabaseException;

    /**
     * Creates an internal connection to a LDAP server based on user name, user
     * password, and other connection related data supplied by an arbitrary
     * object.
     * 
     * @param user
     *            The user id
     * @param password
     *            The password
     * @param dataSource
     *            An arbitrary object containing the information needed to
     *            establish a valid connection.
     * @return An arbitrary object acting as the connection to the data source.
     * @throws SystemException
     */
    Object connect(String user, String password, Object dataSource)
            throws SystemException;

    /**
     * Indicates if a connection to the LDAP server has been established.
     * 
     * @return true when the connection is live. Otherwise, false is returned.
     */
    boolean isConnected();

    /**
     * Extracts the results of the LDAP query.
     * 
     * @param results
     *            Object array which the first element should be a List
     *            instance. Optionally, can be null.
     * @return An instance of List or null if <i>resluts</i> is null
     * @throws DatabaseException
     */
    List extractLdapResults(Object results[]) throws DatabaseException;

}
