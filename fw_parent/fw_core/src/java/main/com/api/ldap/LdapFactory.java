package com.api.ldap;

import com.RMT2Base;

/**
 * Factory class for creating LDAP related objects.
 * 
 * @author rterrell
 * 
 */
public class LdapFactory extends RMT2Base {

    /**
     * Create a clean LdapFactory.
     */
    public LdapFactory() {
        super();
        return;
    }

    /**
     * Creates an attribute managable LdapClient that is not connected to the
     * LDAP server.
     * 
     * @return and instance of {@link LdapClient}
     */
    public LdapClient createAttributeClient() {
        LdapClient api = new StoredAttributeClientImpl();
        return api;
    }

    /**
     * Creates an attribute managable <i>LdapClient</i> that is bound to the
     * LDAP server under the user id/password credentials decalred in
     * .properties file, <i>configPropFile</i>.
     * <p>
     * This method sort of mimics the approach of anonymous binding since no
     * user id and password is provided. In order for this method invocation to
     * work, the configuration properties file must contain the following hash
     * values:
     * <ul>
     * <li>baseDN - The base distinguished name node in which all user queries
     * stem from.</li>
     * <li>peopleRDN - The relative base dinstinguished name used to locate
     * users.</li>
     * <li>ldapFactory - The fully qualified java class name representing the
     * LDAP server factory.</li>
     * <li>ldapUrl - The URL for the production LDAP server.</li>
     * <li>ldapUrl.dev - The URL for the development LDAP server.</li>
     * <li>ldapAuth - The method of authentication used.</li>
     * <li>defaultUserIdProperty - The name of the login id property belonging
     * to the person targeted for authentication.</li>
     * <li>appUser - The default user id used for binding to the LDAP server.
     * <li>appPassword - The password of the default user id.
     * </ul>
     * 
     * @param configPropFile
     *            The Properties file containing all of the LDAP configuration
     *            information needed to establish a connection. If found to be
     *            null, then the default LDAP configuration file,
     *            {@link LdapClient#DEFAULT_CONFIG_RESOURCE
     *            DEFAULT_CONFIG_RESOURCE}, will be used.
     * @return and instance of {@link LdapClient}
     */
    public LdapClient createAttributeClient(String configPropFile) {
        LdapClient api = this.createAttributeClient();
        api.connect(configPropFile);
        return api;
    }

    /**
     * Creates an attribute managable <i>LdapClient</i> instance and binds the
     * user to the server.
     * <p>
     * In order for this method invocation to work, the configuration properties
     * file must contain the following hash values:
     * <ul>
     * <li>baseDN - The base distinguished name node in which all user queries
     * stem from.</li>
     * <li>peopleRDN - The relative base dinstinguished name used to locate
     * users.</li>
     * <li>ldapFactory - The fully qualified java class name representing the
     * LDAP server factory.</li>
     * <li>ldapUrl - The URL for the production LDAP server.</li>
     * <li>ldapUrl.dev - The URL for the development LDAP server.</li>
     * <li>ldapAuth - The method of authentication used.</li>
     * <li>defaultUserIdProperty - The name of the login id property belonging
     * to the person targeted for authentication.</li>
     * </ul>
     * 
     * @param userId
     *            The user id to appended to the base DN. Can be null for
     *            anonymous bindigs.
     * @param password
     *            The password to authenticate the user. Can be null for
     *            anonymous bindigs.
     * @param configPropFile
     *            The Properties file containing all of the LDAP configuration
     *            information needed to establish a connection. When null, then
     *            the default LDAP configuration file,
     *            {@link LdapClient#DEFAULT_CONFIG_RESOURCE
     *            DEFAULT_CONFIG_RESOURCE}, will be used.
     * @return and instance of {@link LdapClient}
     */
    public LdapClient createAttributeClient(String userId, String password,
            String configPropFile) {
        LdapClient api = this.createAttributeClient();
        api.connect(userId, password, configPropFile);
        return api;
    }

    /**
     * Creates a <i.LdapClient</i> instance capable of looking up objects within
     * a LDAP server.
     * 
     * @return and instance of {@link LdapClient}
     */
    public LdapClient createObjectClient() {
        LdapClient api = new StoredObjectClientImpl();
        return api;
    }
}
