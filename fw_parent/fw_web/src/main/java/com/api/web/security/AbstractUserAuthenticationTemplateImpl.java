package com.api.web.security;

import java.util.List;

import org.apache.log4j.Logger;

import com.SystemException;
import com.api.config.old.ProviderConfig;
import com.api.persistence.DatabaseException;
import com.api.security.TargetAppException;
import com.api.security.authentication.web.AuthenticationException;
import com.api.security.authentication.web.AuthorizationException;
import com.api.security.authentication.web.LoginIdException;
import com.api.security.authentication.web.LogoutException;
import com.api.security.authentication.web.MissingLoginCredentialsException;
import com.api.security.authentication.web.PasswordException;
import com.api.security.authentication.web.UserCredentials;
import com.api.web.AbstractWebRequestManager;
import com.constants.GeneralConst;

/**
 * An abstract class definition that utilizes the template pattern for
 * developing concrete user authentication, authorization, and logoff scenarios.
 * 
 * @author rterrell
 * 
 */
public abstract class AbstractUserAuthenticationTemplateImpl extends
        AbstractWebRequestManager {

    private static Logger logger = Logger
            .getLogger("AbstractUserAuthenticationTemplateImpl");

    private UserCredentials credentials;

    private boolean error;

    protected Object sessionToken;

    protected RMT2SessionBean sessionBean;

    protected ProviderConfig provider;

    /**
     * Constructs a AbstractUserAuthenticationTemplateImpl object and
     * initializes the logger.
     */
    protected AbstractUserAuthenticationTemplateImpl() {
        super();
        logger.info("Logger initialized");
        return;
    }

    /**
     * Stub implementation.
     * 
     * @throws DatabaseException
     * @throws SystemException
     */
    protected void initApi() throws DatabaseException, SystemException {
        this.credentials = new UserCredentials();
        return;
    }

    /**
     * Drives the authentication process by initializing the environment,
     * verifies the user's credentials, and performs a clean operation.
     * 
     * @param loginId
     * @param password
     * @param appCode
     * @param sessionId
     * @return
     * @throws AuthenticationException
     * @throws AuthorizationException
     */
    protected Object process(String loginId, String password, String appCode,
            String sessionId) throws AuthenticationException,
            AuthorizationException {
        // Setup credentials Hash
        this.credentials.setUserId(loginId);
        this.credentials.setPassword(password);
        this.credentials.setAppCode(appCode);
        this.credentials.setCurrentSessionId(sessionId);

        // Validate input credentials
        try {
            this.verifyCredentials();
        } catch (MissingLoginCredentialsException e) {
            throw new AuthenticationException(
                    "Unable to authenticate user due to invalid input credentials",
                    e);
        }

        // Perform implementation specific intialization
        this.initialize();
        // Perform actual user authentication
        Object results = null;
        try {
            error = false;
            results = this.verifyUser(loginId, password, appCode, sessionId);
        } finally {
            // Perform any clean up routines
            this.cleanUp();
        }

        // Assign the results of the authentication as security token
        this.sessionToken = results;
        return results;
    }

    /**
     * The implementor of this method is required to provide functionality that
     * will initialize any resources needed for the authentication process. For
     * example, the initialization of a database connection.
     * 
     * @throws AuthenticationException
     */
    protected abstract void initialize() throws AuthenticationException;

    /**
     * The implementor of this method is required to provide functionality that
     * will verify the user's creditials against some form of a data source.
     * 
     * @param loginId
     * @param password
     * @param appCode
     * @param sessionId
     * @return
     * @throws AuthenticationException
     * @throws AuthorizationException
     */
    protected abstract RMT2SecurityToken verifyUser(String loginId,
            String password, String appCode, String sessionId)
            throws AuthenticationException, AuthorizationException;

    /**
     * This method serves as the end point of the user authentication process.
     * <p>
     * This is a good place to provide functionality to package the results of
     * the authenticating the user and release any resources that may have been
     * allocated.
     * 
     * @throws AuthenticationException
     */
    protected abstract void cleanUp() throws AuthenticationException;

    /**
     * Invalidates the user's session.
     * 
     * @return {@link com.api.security.authentication.web.RMT2SessionBean
     *         RMT2SessionBean} An instance of the session bean that was removed
     *         from the user's session.
     * @throws LogoutException
     */
    protected RMT2SessionBean removeSession() throws LogoutException {
        UserAuthenticationHelper helper = new UserAuthenticationHelper();
        return helper.removeSession(this.request);
    }

    /**
     * Validates the existence of login credentials, and individual attributes
     * <i>login id</i>, <i>password</i> and the <i>target applicatin name</i>.
     * 
     * @throws LoginException
     *             When credentials is invalid, the login id is null, password
     *             is null, or the user's request object is invalid.
     */
    private void verifyCredentials() throws MissingLoginCredentialsException {
        if (this.credentials == null) {
            throw new MissingLoginCredentialsException(
                    "Login credential collection has not been initialized",
                    -150);
        }

        String temp = null;
        temp = this.credentials.getUserId();
        if (temp == null || temp.equals("")) {
            throw new LoginIdException("Login id is required", -151);
        }
        temp = this.credentials.getPassword();
        if (temp == null || temp.equals("")) {
            throw new PasswordException("Password is required", -152);
        }
        temp = this.credentials.getAppCode();
        if (temp == null || temp.equals("")) {
            throw new TargetAppException("Target application name is required",
                    -153);
        }
    }

    /**
     * Validates whether the login id is not null..
     * 
     * @param userName
     *            the login id of the user to query.
     * @return boolean returns true when userName is not null. Otherwise, false
     *         is returned.
     * @throws AuthenticationException
     */
    protected boolean isAuthenticated(String loginId)
            throws AuthenticationException {
        return (loginId == null ? false : true);
    }

    /**
     * Fetches the security session token which is usually utilized by the
     * client.
     * <p>
     * The descendent class is responsible for accurately determining the data
     * type of the value returned.
     * 
     * @return an abitrary object representing the session token.
     */
    public Object getSessionToken() {
        return this.sessionToken;
    }

    /**
     * 
     * @return
     */
    public String getSessionId() {
        if (this.credentials == null) {
            return null;
        }
        return this.credentials.getCurrentSessionId();
    }

    /**
     * Determines if the user is being authenticated locally or remotely.
     * <p>
     * The <i>target applicatin code</i> is used to distinguish between local
     * and remote authentication for a user. Local authentication occurs when
     * the incoming application code is equal to
     * {@link com.constants.GeneralConst#SECURITY_APP_CODE SECURITY_APP_CODE}.
     * Otherwise, the user is being authenticated remotely.
     * 
     * @return
     */
    protected boolean isLocalLogin() {
        if (this.credentials == null || this.credentials.getAppCode() == null) {
            return true;
        }
        return this.credentials.getAppCode().equalsIgnoreCase(
                GeneralConst.SECURITY_APP_CODE);
    }

    /**
     * @return the error
     */
    public boolean isError() {
        return error;
    }

    /**
     * Sets the data source provider
     * 
     * @param provider
     */
    public void setProvider(ProviderConfig provider) {
        this.provider = provider;
    }

    /**
     * Determines if the user possesses one or more of the required roles. The
     * implementation must, first, determine if the user has been authenticated,
     * and ,secondly, verify the user possesses one or more of the the required
     * roles.
     * 
     * @param loginId
     *            The user's login id.
     * @param roles
     *            A List of Strings representing the required roles.
     * @param token
     *            An instance of
     *            {@link com.api.security.authentication.web.RMT2SecurityToken
     *            RMT2SecurityToken}
     * @throws AuthorizationException
     *             <ul>
     *             <li>When token is null</li>
     *             <li>When token is invalid or of the incorrect type</li>
     *             <li>When any of the user's roles fail to match at least one
     *             of the required roles (The user is not authorized</li>
     *             <li>General authorization process errors</li>
     *             </ul>
     * @throws AuthenticationException
     *             When token contains a null session bean
     */
    public void authorize(String loginId, List roles, Object token)
            throws AuthorizationException, AuthenticationException {
        if (token == null) {
            this.msg = "Security Token cannot be null when authorizing a user for system resource access";
            logger.error(this.msg);
            throw new AuthorizationException(this.msg);
        }

        boolean validToken = (token instanceof RMT2SecurityToken);
        if (!validToken) {
            this.msg = "An invalid security token was discovered during user authorization";
            logger.error(this.msg);
            throw new AuthorizationException(this.msg);
        }

        RMT2SessionBean session = ((RMT2SecurityToken) token).getSession();
        if (session == null) {
            this.msg = "A null session bean was discovered to be associated with the user's security token.  Possibly, the user may not be authenticated.";
            logger.error(this.msg);
            throw new AuthenticationException(this.msg);
        }

        List<String> userRoles = session.getRoles();

        UserAuthenticationHelper helper = new UserAuthenticationHelper();
        boolean authorized = helper.isAuthorized(loginId, roles, userRoles);
        if (authorized) {
            return;
        }
        else {
            this.msg = "User does not have the proper authorization based on the required roles";
            logger.error(this.msg);
            throw new AuthorizationException(this.msg);
        }
    }

}
