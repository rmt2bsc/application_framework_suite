package com.api.web.security;

import com.RMT2Base;
import com.SystemException;
import com.api.config.AppPropertyPool;
import com.api.config.ConfigConstants;
import com.api.config.old.ProviderConfig;
import com.api.security.authentication.web.AuthenticationException;
import com.api.security.authentication.web.LoginException;
import com.api.util.RMT2Utility;
import com.api.web.Request;
import com.api.web.Session;

/**
 * Factory that creates instances of classes that are related to authentication
 * and authorization.
 * 
 * @author RTerrell
 * 
 */
public class UserAuthenticationFactory extends RMT2Base {

    /**
     * Creates an instance of RMT2SessionBean which is not assigned to any
     * particular user.
     * 
     * @return {@link RMT2SessionBean} or null if instance could not be created.
     */
    private static RMT2SessionBean getSessionBeanInstance() {
        RMT2SessionBean obj;
        try {
            obj = new RMT2SessionBean();
            return obj;
        } catch (SystemException e) {
            return null;
        }
    }

    /**
     * Creates an instance of RMT2SessionBean which belongs to a user identified
     * as, <i>loginId</i>, of application, <i>appId</i>.
     * 
     * @param loginId
     *            The user's login id.
     * @param appId
     *            The id of the application which the user is associated with at
     *            login time.
     * @return {@link RMT2SessionBean} or null if instance could not be created.
     * @throws AuthenticationException
     *             <i>loginId</i> and/or <i>appId</i> is null or not present.
     */
    public static RMT2SessionBean getSessionBeanInstance(String loginId, String appId) throws AuthenticationException {
        if (loginId != null && loginId.length() <= 0) {
            throw new AuthenticationException("Session Bean token failure...User\'s login id must be present");
        }
        if (appId != null && appId.length() <= 0) {
            throw new AuthenticationException("Session Bean token failure...User\'s application id must be present");
        }
        RMT2SessionBean obj = UserAuthenticationFactory.getSessionBeanInstance();
        obj.setLoginId(loginId);
        obj.setOrigAppId(appId);
        return obj;
    }

    /**
     * Creates a new RMT2SessionBean object using the user's request and session
     * objects to initialize its properties.
     * 
     * @param request
     *            The user's request
     * @param session
     *            The user's session.
     * @return {@link RMT2SessionBean} or null if instance could not be created.
     * @throws AuthenticationException
     *             <i>request</i> and/or <i>session</i> is null.
     */
    public static RMT2SessionBean getSessionBeanInstance(Request request, Session session) throws AuthenticationException {
        if (request == null) {
            throw new AuthenticationException("Session Bean token failure...User\'s request object is invalid");
        }
        if (session == null) {
            throw new AuthenticationException("Session Bean token failure...User\'s web server session object is invalid");
        }
        RMT2SessionBean bean = null;
        try {
            bean = new RMT2SessionBean(request, session);
            return bean;
        } catch (SystemException e) {
            return null;
        }
    }

    /**
     * Creates a new RMT2SessionBean object using the user's login id,
     * application code, and the request and session instances to initialize its
     * properties.
     * 
     * @param loginId
     *            the user's login id
     * @param appCode
     *            the application code
     * @param request
     *            the user's request
     * @param session
     *            the user's session
     * @return {@link RMT2SessionBean} or null if instance could not be created.
     * @throws AuthenticationException
     */
    public static RMT2SessionBean getSessionBeanInstance(String loginId, String appCode, Request request, Session session)
            throws AuthenticationException {
        RMT2SessionBean bean = UserAuthenticationFactory.getSessionBeanInstance(request, session);
        bean.setLoginId(loginId);
        bean.setOrigAppId(appCode);
        return bean;
    }

    /**
     * Creates the user authenticator instance.
     * <p>
     * The authenticator is created dynamically by the input of the class name.
     * The class name can be obtained from the input parameter of this method,
     * <i>implClassName</i>, or it can be discovered via a http system property
     * configuration. Each application that uses this framework api is required
     * to create a class which implements the
     * {@link com.api.security.UserSecurity UserSecurity} interface and make the
     * class name available to the authentication process via the
     * {@link com.api.config.PropertyFileSystemResourceConfigImpl#PROPNAME_AUTHENTICATOR
     * PROPNAME_AUTHENTICATOR} property in the AppParms.properties or input
     * parameter, <i>implClassName</i>.
     * 
     * @param implClassName
     *            The name of the implementing class to instantiate. If null,
     *            then the application property, authenticator, is used.
     * @return {@link com.api.security.authentication.web.UserAuthenticator
     *         UserAuthenticator} The specific authenticator implementation.
     * @throws LoginException
     *             If <i>implClassName</i> renders a null class instance, or the
     *             instance does not implement the UserSecurity interface.
     */
    public static UserAuthenticator getAuthenticator(String implClassName) throws LoginException {
        // Obtain the class name from either the input parameter or the
        // application's property pool.
        String className = null;
        if (implClassName == null || implClassName.length() <= 0) {
            className = AppPropertyPool.getProperty(ConfigConstants.PROPNAME_AUTHENTICATOR);
        }
        else {
            className = implClassName;
        }
        if (className == null) {
            throw new LoginException("User Authenticator object class name is invalid or unknown");
        }

        try {
            // Dynamically create object based on input parameter, className
            Object obj = RMT2Utility.getClassInstance(className);

            // Check if object implements UserSecurity
            if (obj instanceof UserAuthenticator) {
                // Do nothing...object implements UserSecurity interface.
            }
            else {
                throw new LoginException("User Authenticator object is of the wrong type.  Must implement the interface, "
                                + UserAuthenticator.class.getName());
            }
            return (UserAuthenticator) obj;
        } catch (SystemException e) {
            throw new LoginException(e);
        }
    }

    /**
     * Creates the user authenticator instance with its own custom data
     * provider.
     * 
     * @param implClassName
     *            The name of the implementing class to instantiate. If null,
     *            then the application property, authenticator, is used.
     * @param provider
     *            The custom data source provider.
     * @return {@link com.api.security.authentication.web.UserAuthenticator
     *         UserAuthenticator} The specific authenticator implementation.
     * @throws LoginException
     *             If <i>implClassName</i> renders a null class instance, or the
     *             instance does not implement the UserSecurity interface.
     */
    public static UserAuthenticator getAuthenticator(String implClassName, ProviderConfig provider) throws LoginException {
        UserAuthenticator a = UserAuthenticationFactory.getAuthenticator(implClassName);
        a.setProvider(provider);
        return a;
    }

    /**
     * Creates the user authenticator and initializes it with the user's Request
     * and Response.
     * 
     * @param implClassName
     *            The name of the implementing class to instantiate. If null,
     *            then the application property, authenticator, is used.
     * @param request
     *            The user's request
     * @return {@link com.api.security.authentication.web.UserAuthenticator
     *         UserAuthenticator} The specific authenticator implementation.
     * @throws LoginException
     *             If <i>implClassName</i> renders a null class instance, or the
     *             instance does not implement the UserSecurity interface.
     * @see {@link com.api.security.authentication.web.AuthenticationFactory#getAuthenticator(String)
     *      getAuthenticator(String)}
     */
    public static UserAuthenticator getAuthenticator(String implClassName, Request request) throws LoginException {
        UserAuthenticator api = UserAuthenticationFactory.getAuthenticator(implClassName);
        api.setRequest(request);
        return api;
    }

}
