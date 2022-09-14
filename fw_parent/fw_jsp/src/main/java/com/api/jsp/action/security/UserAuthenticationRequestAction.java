package com.api.jsp.action.security;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.SystemException;
import com.api.constants.RMT2ServletConst;
import com.api.jsp.action.AbstractActionHandler;
import com.api.persistence.DatabaseException;
import com.api.security.authentication.web.AuthenticationConst;
import com.api.security.authentication.web.LoginException;
import com.api.security.authentication.web.UserCredentials;
import com.api.web.ActionCommandException;
import com.api.web.Context;
import com.api.web.ICommand;
import com.api.web.Request;
import com.api.web.Response;
import com.api.web.security.RMT2SecurityToken;
import com.api.web.security.RMT2SessionBean;
import com.api.web.security.SessionBeanManager;
import com.api.web.security.UserAuthenticationFactory;
import com.api.web.security.UserAuthenticationHelper;
import com.api.web.security.UserAuthenticator;

/**
 * This common action handler is called upon from a descendent of the
 * AbstractCommandController and is designed to drive the process of handling
 * user authentication requests. It instantiates and uses the appropriate
 * authenticator assigned to manage various security tasks such as user login,
 * user logout, resource authorization, and etc. This action handler possesses
 * the capability to dynamically determine and invoke the
 * {@link com.api.security.authentication.web.UserAuthenticator
 * UserAuthenticator} implementation based on the setting of the application
 * property, <i>authenticator</i>.
 * <p>
 * <b>Required Configuration</b><br>
 * A few configuration chores must be performed in order for this handler to
 * invoke the appropriate authenticator implementation for the UserSecurity
 * interface. <blockquote>
 * <ol>
 * <li>The property, <i>authenticator</i>, must be setup in the application's
 * configuration (web.xml or LDAP/Active Diretory), and its value should be the
 * class name of the authenticator implementation for the respective
 * application. The authenticator implementation is required to derived the
 * class,
 * {@link com.api.security.authentication.web.AbstractClientAuthentication
 * AbstractClientAuthentication}.</li>
 * <li>This class, <i>AuthenticationAction</i>, is required to be assigned as
 * the handler for the "login" and "logout" navigation rules in the
 * Security.properties file for each web applictaion.</li>
 * </ol>
 * </blockquote> <br>
 * <b>Authenticator Discovery</b><br>
 * The process of discovering the appropriate authenticator for a specifi
 * application is centered around the factory method,
 * {@link com.api.security.authentication.web.AuthenticationFactory#getAuthenticator(String)
 * AuthenticationFactory.getAuthenticator(String)}. In the init method of this
 * class, the AuthenticationFactory.getAuthenticator method is invoked with the
 * intent of creating the authenticator based on the class name assigned to the
 * <i>authenticator</i> property regarding the above configuration details. This
 * is accomplished by passing null as an input parameter to the getAuthenticator
 * method. Alternatively, the authenticator can be established by direct means,
 * hence, bypassing the authenticator configuration/discovery process by passing
 * a fully qualified class name of the authenticator to the getAuthenticator
 * method. Of course, you will need to derived this class and override the
 * init() method in order for this concept to work.
 * <p>
 * When handling the "login" request, each implementation is responsible for
 * obtaining a session bean token for the user so that it may be placed on the
 * application session. The association of the session bean with the application
 * session will signify that the user has successfully logged into the system.
 * 
 * @author RTerrell
 * 
 */
public class UserAuthenticationRequestAction extends AbstractActionHandler implements ICommand {

    private static final String COMMAND_LOGIN = "Security.Authentication.login";

    private static final String COMMAND_LOGOUT = "Security.Authentication.logoff";

    private static final String COMMAND_AUTHORIZE = "Security.Authentication.authorize";

    private static final String COMMAND_CHECK_USER_AUTH_STATUS = "Security.Authentication.verifyauthentication";

    private static Logger logger = Logger.getLogger(UserAuthenticationRequestAction.class);

    private UserAuthenticator api;

    private String userId;

    // private String loginId;
    //
    // private String appName;
    //
    // private String srcSessionId;

    /**
     * Default constructor which sets up the logger.
     * 
     */
    public UserAuthenticationRequestAction() {
        super();
    }

    /**
     * Creates a user security api instance based on the locale type of
     * authentication. The locale type can be found in the application,
     * AppParms.properties, file within the configuration area.
     * 
     * @param context
     *            the servlet context
     * @param request
     *            the http servlet request
     * @throws SystemException
     */
    protected void init(Context context, Request request) throws SystemException {
        this.userId = request.getParameter(AuthenticationConst.AUTH_PROP_USERID);
        try {
            this.api = UserAuthenticationFactory.getAuthenticator(null);
        } catch (LoginException e) {
            this.msg = "Initialization of common Authentication action hanlder failed";
            logger.error(this.msg);
            throw new SystemException(e);
        }
    }

    /**
     * Processes one of the the requested commands: user login, user logout or
     * authorize user.
     * 
     * @param request
     *            The HttpRequest object
     * @param response
     *            The HttpResponse object
     * @param command
     *            Command issued by the client.
     * @Throws SystemException when an error needs to be reported.
     */
    public void processRequest(Request request, Response response, String command) throws ActionCommandException {
        super.processRequest(request, response, command);
        logger.log(Level.INFO, "Processing authentication command, " + command);

        // Test if authenticator instance has been retrieved successfully
        if (this.api == null) {
            this.msg = "Unable to authenticate user...Authenticator is not initialized";
            logger.log(Level.ERROR, this.msg);
            throw new ActionCommandException(this.msg);
        }
        this.api.setRequest(request);
        RMT2SessionBean sessionBean = null;

        // Obtain the user credentials from the Request instance as input
        UserAuthenticationHelper helper = new UserAuthenticationHelper();
        UserCredentials creds = helper.getInputCredentiials(request);

        // Begin to handle the user's authentication request
        RMT2SecurityToken token = null;
        try {
            if (command.equalsIgnoreCase(UserAuthenticationRequestAction.COMMAND_LOGIN)) {
                token = (RMT2SecurityToken) this.api.authenticate(creds.getUserId(), creds.getPassword(), creds.getAppCode(),
                        creds.getCurrentSessionId());
                sessionBean = token.getSession();
                this.assignSessionBean(sessionBean);
            }
            else if (command.equalsIgnoreCase(UserAuthenticationRequestAction.COMMAND_LOGOUT)) {
                this.api.logout(creds.getUserId(), creds.getCurrentSessionId());
            }
            else if (command.equalsIgnoreCase(UserAuthenticationRequestAction.COMMAND_AUTHORIZE)) {
                return;
            }
            else if (command.equalsIgnoreCase(UserAuthenticationRequestAction.COMMAND_CHECK_USER_AUTH_STATUS)) {
                token = (RMT2SecurityToken) this.api.checkAuthenticationStatus(creds.getUserId(), creds.getAppCode(),
                        creds.getCurrentSessionId());
                if (token != null && token.getSession() != null) {
                    this.assignSessionBean(token.getSession());
                }
            }
        } catch (Exception e) {
            this.msg = "Service command failed: " + command;
            logger.error(this.msg);
            throw new ActionCommandException(this.msg, e);
        } finally {
            // Send results as XML to non-java based clients regardless of
            // success or failure
            this.sendClientData();
            this.api = null;
            this.request.setAttribute(AuthenticationConst.AUTH_PROP_USERID, this.userId);
        }
    }

    /**
     * Assigns a RMT2SessionBean instance to the user's application session. If
     * the session bean is found to be invalid then an exception is produced.
     * 
     * @param sessionBean
     *            A valid {@link RMT2SessionBean} instance
     * @throws SystemException
     *             If <i>sessionBean</i> is invalid.
     */
    private void assignSessionBean(RMT2SessionBean sessionBean) throws SystemException {
        // TODO: delete commented code once this method has been tested.
        // UI-4: commented due to added logic in RMT2CheckUserLoggedInTag to
        // capture application code.
        // String appId =
        // AppPropertyPool.getProperty(AuthenticationConst.AUTH_PROP_MAINAPP);
        // sessionBean.setOrigAppId(appId);
        SessionBeanManager sbm = SessionBeanManager.getInstance(this.request);
        sbm.addSessionBean(sessionBean);
        logger.log(Level.INFO, "Session ID from Target App, " + sessionBean.getOrigAppId() + ": " + sessionBean.getSessionId());
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.action.AbstractActionHandler#receiveClientData()
     */
    @Override
    protected void receiveClientData() throws ActionCommandException {
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.action.AbstractActionHandler#sendClientData()
     */
    @Override
    protected void sendClientData() throws ActionCommandException {
        Object data = null;
        Object token = this.api.getSessionToken();
        if (token instanceof RMT2SecurityToken) {
            data = ((RMT2SecurityToken) token).getToken();
        }
        this.request.setAttribute(RMT2ServletConst.RESPONSE_NONJSP_DATA, data);
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.action.ICommonAction#add()
     */
    public void add() throws ActionCommandException {
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.action.ICommonAction#delete()
     */
    public void delete() throws ActionCommandException, DatabaseException {
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.action.ICommonAction#edit()
     */
    public void edit() throws ActionCommandException {
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.action.ICommonAction#save()
     */
    public void save() throws ActionCommandException, DatabaseException {
        return;
    }

}
