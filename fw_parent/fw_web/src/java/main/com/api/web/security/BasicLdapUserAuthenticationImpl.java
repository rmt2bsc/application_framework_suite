package com.api.web.security;

import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.LdapContext;

import org.apache.log4j.Logger;

import com.api.ldap.LdapClient;
import com.api.ldap.LdapFactory;
import com.api.ldap.beans.LdapUser;
import com.api.ldap.operation.LdapModifyOperation;
import com.api.ldap.operation.LdapSearchOperation;
import com.api.security.authentication.web.AuthenticationException;
import com.api.security.authentication.web.AuthorizationException;
import com.api.security.authentication.web.LogoutException;

/**
 * Light Weight Directory Access Protocol (LDAP) implementation of interface,
 * {@link com.api.security.authentication.web.UserAuthenticator
 * UserAuthenticator}, which uses directories to authenticate clients.
 * <p>
 * 
 * @author rterrell
 * 
 */
public class BasicLdapUserAuthenticationImpl extends
        AbstractUserAuthenticationTemplateImpl implements UserAuthenticator {

    private static Logger logger = Logger
            .getLogger(BasicLdapUserAuthenticationImpl.class);

    private String ldapConfig;

    private LdapClient ldap;

    private LdapContext ctx;

    /**
     * 
     */
    public BasicLdapUserAuthenticationImpl() {
        this.ldap = null;
        this.ctx = null;
        this.ldapConfig = null;
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.authentication.web.UserAuthenticator#authenticate()
     */
    public RMT2SecurityToken authenticate() throws AuthenticationException {
        return null;
    }

    /**
     * A LDAP implementation to authenticate a user using basic login
     * credentials, <i>loginId</i> and <i>password</i>.
     * <p>
     * Authentication is performed by binding the user, <i>loginid</i>, to the
     * LDAP server. If the the user fails the binding process, an
     * AuthenticationExpeption is thrown. Once successfully authenticated, the
     * user's record is fetched from the LDAP repository using the current
     * user's distinguished name and a {@link RMT2SessionBean} instance is
     * created containing basic user information which excludeds Request related
     * data. Lastly, the user's LDAP context is terminated.
     * 
     * @param loginId
     *            The user's unique system identifier
     * @param password
     *            The user's password.
     * @return An instance of {@link RMT2SecurityToken}
     * @throws AuthenticationException
     *             For any case where the user's login attempt fails.
     */
    public RMT2SecurityToken authenticate(String loginId, String password)
            throws AuthenticationException {
        LdapUser user = null;
        RMT2SessionBean session = null;
        RMT2SecurityToken token = null;

        // Ensure that the LDAP client is initialized
        if (this.ldap == null) {
            this.initialize();
        }

        try {
            // Attempt to authenticate user by binding to LDAP server
            String dn = "loginid=" + loginId;
            this.ctx = (LdapContext) this.ldap.connect(dn, password,
                    this.ldapConfig);

            // OBtain the User's LDAP entry.
            LdapSearchOperation op = new LdapSearchOperation();
            op.setDn("ou=People");
            op.setScope(SearchControls.SUBTREE_SCOPE);
            op.getSearchFilterArgs().put("loginid", "{0}");
            op.getSearchFilterPlaceholders().add(loginId);
            // op.setSearchFilter("loginid={0}");
            // op.getSearchFilterArgs().add(loginId);
            op.setUseSearchFilterExpression(true);
            op.setMappingBeanName("com.api.ldap.beans.LdapUser");
            Object data[] = this.ldap.retrieve(op);
            List<LdapUser> list = (List<LdapUser>) data[0];
            if (list.size() > 0) {
                user = (LdapUser) list.get(0);
                session = this.createSessionBean(user);
                token = new RMT2SecurityToken();
                token.setSession(session);
                token.setToken(user);
            }
            return token;
        } catch (Exception e) {
            this.msg = "Authentication for user, " + loginId + ", failed";
            logger.error(this.msg);
            throw new AuthenticationException(this.msg, e);
        } finally {
            // Try to close LDAP connection for target user.
            try {
                this.ctx.close();
            } catch (NamingException e) {
                this.msg = "User, "
                        + loginId
                        + ", does not have authority to close LDAP connection.  "
                        + e.getMessage();
                logger.warn(this.msg);
            }
            this.ctx = null;
        }
    }

    /**
     * A LDAP implementation to authenticate a user using <i>loginId</i>,
     * <i>password</i>, <i>appCode</i>, <i>sessionId</i>.
     * <p>
     * This implementation provides a means to manage and keep track of the
     * application and the current session as it relates to the user's active
     * presence.
     * 
     * @param loginId
     *            The user's unique system identifier
     * @param password
     *            The user's password.
     * @param appCode
     *            The code name for the application
     * @param sessionId
     *            The id of the session the user is currently bound.
     * @return An instance of {@link RMT2SecurityToken}
     * @throws AuthenticationException
     * @throws AuthorizationException
     */
    public RMT2SecurityToken authenticate(String loginId, String password,
            String appCode, String sessionId) throws AuthenticationException,
            AuthorizationException {
        RMT2SecurityToken token = (RMT2SecurityToken) this.process(loginId,
                password, appCode, sessionId);
        return token;
    }

    /**
     * An adapter that creates a RMT2SessionBean object using a valid LdapUser
     * instance as the source of data.
     * 
     * @param user
     *            An instance of {@link com.api.ldap.beans.LdapUser LdapUser}
     * @return An instnace of
     *         {@link ccom.api.security.authentication.RMT2SessionBean
     *         RMT2SessionBean} containing basic information obtained from the
     *         LDAP object, <i>user</i>. Request related data is excluded and is
     *         not captured at this point.
     */
    private RMT2SessionBean createSessionBean(LdapUser user) {
        if (user == null) {
            return null;
        }
        RMT2SessionBean s = new RMT2SessionBean();
        s.setLoginId(user.getLoginid());
        s.setFirstName(user.getFn());
        s.setLastName(user.getSn() != null & user.getSn().size() > 0 ? user
                .getSn().get(0) : null);

        // Add roles
        for (String role : user.getAr()) {
            s.addUserRole(role);
        }
        return s;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.api.security.authentication.web.AbstractUserAuthenticationTemplateImpl
     * #initialize()
     */
    @Override
    protected void initialize() throws AuthenticationException {
        this.ldapConfig = LdapClient.DEFAULT_CONFIG_RESOURCE;
        LdapFactory f = new LdapFactory();
        this.ldap = f.createAttributeClient();
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.api.security.authentication.web.AbstractUserAuthenticationTemplateImpl
     * #verifyUser(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)
     */
    @Override
    protected RMT2SecurityToken verifyUser(String loginId, String password,
            String appCode, String sessionId) throws AuthenticationException {
        // Perform basic authentication
        RMT2SecurityToken token = this.authenticate(loginId, password);

        // Continue to update the RMT2SessionBean with pertinent information at
        // this level
        RMT2SessionBean session = token.getSession();
        session.setOrigAppId(appCode);
        session.setSessionId(sessionId);

        // Prepare to update the user's record in LDAP as a successful login
        LdapUser user = (LdapUser) token.getToken();
        int loginCount = (user.getLc() == null || user.getLc().equals("") ? 0
                : Integer.parseInt(user.getLc()));
        loginCount++;

        // Do the update using the root user
        this.ctx = (LdapContext) this.ldap.connect(this.ldapConfig);
        LdapModifyOperation op = new LdapModifyOperation();
        String rdn = "loginid=" + loginId;
        op.setDn(rdn + ",ou=People");
        op.addListAttribute("as", sessionId, LdapClient.MOD_OPERATION_ADD);
        op.addAttribute("lc", String.valueOf(loginCount),
                LdapClient.MOD_OPERATION_REPLACE);
        // TODO: Add line to add application code to signed on application list
        this.ldap.updateRow(op);

        return token;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.api.security.authentication.web.AbstractUserAuthenticationTemplateImpl
     * #cleanUp()
     */
    @Override
    protected void cleanUp() throws AuthenticationException {
        this.ldap.close();
        this.ldap = null;
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.api.security.authentication.web.UserAuthenticator#logout(java.lang
     * .String , java.lang.String)
     */
    public int logout(String loginId, String sessionId) throws LogoutException {
        this.removeSession();
        return 0;
    }

    /**
     * Determines if a user is currently signed on to one or more applications.
     * <p>
     * It uses the following attributes to create and return a
     * {@link RMT2SecurityToken} back to the client: user name, application
     * name, and session id .
     * 
     * @param userName
     *            The user's user name
     * @param appCode
     *            The name of the application user is trying to access. This is
     *            optional and can be null.
     * @param sessionId
     *            The id of the source session.
     * @return {@link com.api.security.authentication.web.RMT2SecurityToken
     *         RMT2SecurityToken}
     * @throws AuthenticationException
     */
    public RMT2SecurityToken checkAuthenticationStatus(String loginId,
            String appCode, String sessionId) throws AuthenticationException {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.AbstractJspApiImpl#close()
     */
    @Override
    public void close() {
        // TODO Auto-generated method stub
        super.close();
    }

}
