package com.nv.security;

import java.util.Date;

import org.apache.log4j.Logger;

import com.api.security.User;

/**
 * Employs the Singleton design pattern to manage an instance of the
 * {@link GuiSecurityToken}.
 * <p>
 * The security token represents an authenticated user and is used to gain
 * access to various resources pertaining to the application.
 * 
 * @author rterrell
 *
 */
public class UserSecurityManager {

    private static GuiSecurityToken SECURITY_TOKEN;

    private static final Logger logger = Logger
            .getLogger(UserSecurityManager.class);

    /**
     * Create a SecurityUtil object containing an uninitialized user security
     * token.
     */
    public UserSecurityManager() {
        return;
    }

    /**
     * Return the user's security token.
     * 
     * @return an instance of {@link GuiSecurityToken}. Null is returned if the
     *         GUI environment has not bee intialized.
     */
    public static final GuiSecurityToken getSecurityToken() {
        return UserSecurityManager.SECURITY_TOKEN;
    }

    /**
     * The purpose of this method is to setup the security token that is
     * associated with the logged on user.
     * <p>
     * The security token is not linked with a user at this point. Once the
     * token is successfully created, the token is populated with pertinent user
     * information and made available to the entire application via the security
     * token.
     * 
     * @throws UserSecurityException
     *             user is not accessible
     */
    public void initUserSecurity() throws UserSecurityException {
        try {
            // Get database connection
            // SqlMapClient ibatisClient = IbaistEnvConfig.getSqlMap();
            GuiSecurityToken token = new GuiSecurityToken();
            // Add connection to security token
            // token.setDbCon(ibatisClient);
            UserSecurityManager.SECURITY_TOKEN = token;
        } catch (Exception e) {
            String msg = "Unable to setup security token for user";
            logger.error(msg);
            throw new UserSecurityException(msg, e);
        }
    }

    /**
     * Updates the security token with the valid user with administrative access
     * rights
     * 
     * @param user
     */
    public void updateSecurityToken(User user) {
        if (SECURITY_TOKEN == null) {
            return;
        }
        SECURITY_TOKEN.setSecurityLevel(1);
        SECURITY_TOKEN.setUserId(user.getLoginId());
        Date dt = new Date();
        SECURITY_TOKEN.setSignOn(dt);
        SECURITY_TOKEN.setLastActivity(dt);
    }

    /**
     * Removes the user from the security token signaling that there is no one
     * logged into the system.
     */
    public void resetSecurityToken() {
        if (SECURITY_TOKEN == null) {
            return;
        }
        SECURITY_TOKEN.setSecurityLevel(0);
        SECURITY_TOKEN.setUserId(null);
        SECURITY_TOKEN.setSignOn(null);
        SECURITY_TOKEN.setLastActivity(null);
    }

}
