package com.api.web.security;

import com.api.security.authentication.web.AuthenticationException;
import com.api.security.authentication.web.LogoutException;
import com.api.web.Request;

/**
 * This interface provides the basic methods needed for a web client to identify
 * and verify a user for the purpose of authenticity and security.
 * 
 * @author roy.terrell
 * 
 */
public interface ClientUserAuthenticator {

    /**
     * Authenticate user.
     * 
     * @param loginId
     *            The user's unique system identifier
     * @param password
     *            The user's password.
     * @return An instance of {@link RMT2SecurityToken}
     * @throws AuthenticationException
     *             For any case where the user's login attempt fails.
     */
    RMT2SecurityToken authenticate(String loginId, String password) throws AuthenticationException;

    /**
     * Authenticate user
     * 
     * @param req
     *            instance of {@link Request}
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
     */
    RMT2SecurityToken authenticate(Request request, String loginId, String password, String appCode, String sessionId)
            throws AuthenticationException;

    /**
     * Determines if a user is currently signed on to one or more applications
     * and verifies if he/she has the authority to access application,
     * <i>appCode</i>.
     * <p>
     * It uses the following attributes to create and return a
     * {@link RMT2SecurityToken} back to the client: user name, application
     * name, and session id .
     * 
     * @param req
     *            instance of {@link Request}
     * @param userName
     *            The user's user name
     * @param appCode
     *            The name of the application user is trying to access. This is
     *            optional and can be null.
     * @param sessionId
     *            The id of the source session.
     * @return An instance of {@link RMT2SecurityToken}
     * @throws AuthenticationException
     */
    RMT2SecurityToken checkAuthenticationStatus(Request request, String loginId, String appCode, String sessionId)
            throws AuthenticationException;

    /**
     * Logs the specified user, belonging to the session identified as
     * <i>sessionId</i>, out of the application.
     * 
     * @param loginId
     *            The user's unique system identifier
     * @param sessionId
     *            The id of the session the user is currently bound.
     * @return An instance of {@link RMT2SecurityToken}
     * @throws LogoutException
     */
    RMT2SecurityToken logout(String loginId, String sessionId) throws LogoutException;

}
