package com.api.web.security;

import java.util.List;

import com.api.config.old.ProviderConfig;
import com.api.security.authentication.web.AuthenticationException;
import com.api.security.authentication.web.AuthorizationException;
import com.api.security.authentication.web.LogoutException;
import com.api.web.Request;

/**
 * This interface provides the basic methods needed to authenticate and
 * authorize users. This contract provides a way to develop an application
 * independent of the underlying authentication technology.
 * 
 * @author roy.terrell
 * 
 */
public interface UserAuthenticator {

    /**
     * Release any allocated resources.
     */
    void close();

    /**
     * Assign a custom datasource provider for this interface
     * 
     * @param provider
     */
    void setProvider(ProviderConfig provider);

    /**
     * Authenticates a user via anonymous means. The user's credentials are
     * obtained through some stat source that is intrinsic to the impelentation.
     * 
     * @return An arbitrary object that represents the authentication results.
     * @throws AuthenticationException
     *             For any case where the user's login attempt fails.
     */
    Object authenticate() throws AuthenticationException;

    /**
     * Authenticate user using basic login credentials, <i>loginId</i> and
     * <i>password</i>.
     * 
     * @param loginId
     *            The user's unique system identifier
     * @param password
     *            The user's password.
     * @return An arbitrary object that represents the authentication results.
     * @throws AuthenticationException
     *             For any case where the user's login attempt fails.
     */
    Object authenticate(String loginId, String password) throws AuthenticationException;

    /**
     * Authenticate user using <i>loginId</i>, <i>password</i>, <i>appCode</i>,
     * <i>sessionId</i>.
     * <p>
     * The implementation of this method is expected to fulfill single sign on
     * functionality in cases where the user is required to login to each
     * application invoked in a multi-application environment. The single sign
     * on principal should only apply towards the usage of two or more
     * applications bound to the same session.
     * 
     * @param loginId
     *            The user's unique system identifier
     * @param password
     *            The user's password.
     * @param appCode
     *            The code name for the application
     * @param sessionId
     *            The id of the session the user is currently bound.
     * @return An arbitrary object that represents the authentication results.
     * @throws AuthenticationException
     * @throws AuthorizationException
     */
    Object authenticate(String loginId, String password, String appCode, String sessionId)
            throws AuthenticationException, AuthorizationException;

    /**
     * Determines if a user is currently signed on to one or more applications
     * and verifies if he/she has the authority to access application,
     * <i>appCode</i>.
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
     * @throws AuthorizationException
     */
    RMT2SecurityToken checkAuthenticationStatus(String loginId, String appCode, String sessionId)
            throws AuthenticationException, AuthorizationException;

    /**
     * Logs the specified user, belonging to the session identified as
     * <i>sessionId</i>, out of the application.
     * 
     * @param loginId
     *            The user's unique system identifier
     * @param sessionId
     *            The id of the session the user is currently bound.
     * @return The number of times the user was logged out of the system based
     *         on the session id.
     * @throws LogoutException
     */
    int logout(String loginId, String sessionId) throws LogoutException;

    /**
     * Determines if the user possesses one or more of the required roles. The
     * implementation must, first, determine if the user has been authenticated,
     * and ,secondly, verify the user possesses one or more of the the required
     * roles.
     * 
     * @param loginId
     *            The user's login id.
     * @param roles
     *            A List of arbitrary objects representing the roles required by
     *            the target resource the user wishes to access.
     * @param token
     *            An arbitrary object that represents the authentication status
     *            of a given user.
     * @throws AuthorizationException
     * @throws AuthenticationException
     */
    void authorize(String loginId, List roles, Object token) throws AuthorizationException, AuthenticationException;

    /**
     * Assigns a valid instance of Request which is used to track the session
     * status of a given user.
     * 
     * @param request
     *            {@link com.controller.Request Request}
     */
    void setRequest(Request request);

    /**
     * Fetches the security session token which is usually utilized by the
     * client.
     * 
     * @return an arbitrary object representing the session token.
     */
    Object getSessionToken();

}
