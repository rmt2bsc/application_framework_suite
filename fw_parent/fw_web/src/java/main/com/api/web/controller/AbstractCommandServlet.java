package com.api.web.controller;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.SystemException;
import com.api.config.AppPropertyPool;
import com.api.config.ConfigConstants;
import com.api.security.authentication.web.AuthenticationConst;
import com.api.security.authentication.web.AuthenticationException;
import com.api.web.ActionCommandException;
import com.api.web.ICommand;
import com.api.web.Request;
import com.api.web.Response;
import com.api.web.Session;
import com.api.web.controller.scope.HttpVariableScopeFactory;
import com.api.web.security.RMT2SessionBean;
import com.api.web.security.SessionBeanManager;
import com.api.web.security.UserAuthenticationFactory;
import com.api.web.util.RMT2WebUtility;
import com.constants.RMT2ServletConst;
import com.util.RMT2File;
import com.util.RMT2String;
import com.util.RMT2Utility;

/**
 * This class represents the bulk of a generic framework for handling arbitrary
 * requests based on navigation rules in a typical web application. Its
 * implemented by using the Command, Template and factory patterns. In esence it
 * works as follows:
 * <ol>
 * <li>
 * The browser makes a request for a particular action called commands in the
 * form of a URL request like:
 * http://www.mysite.com/ServletProcessor/MyCommand?param1=value1.</li>
 * <li>
 * This servlet will parse the request and identify the requester command, by
 * using a properties file containing the target navigation rule, the class name
 * that implements the command is instantiated, the process method on it is
 * invoked and then, using again the properties file, the resulting URL is
 * determined and the request gets forwarded there.</li>
 * <li>
 * In general, a command has a success URL and an error URL. It's assumed that
 * any exception thrown by the command's processes method signals an error. To
 * standarized error processing, the servlet instantiates a properties object
 * that the command can use to store a message with the appropriate error, this
 * way the caller can display an error message to the user.</li>
 * </ol>
 * <p>
 * The implementor of the Command interface may choose to send messages back to
 * the calling servlet (which is usually this servlet) by setting the "info"
 * attribute in the request. In turn this servlet will extract the contents of
 * the "info" attribute into the MESSAGE object, and sets the MESSAGE object in
 * the request for the client to read. The client is expected to identify the
 * MESSAGE object as the attribute, "messages".
 * 
 * This class is declared abstract so that the actual web app can extend it, the
 * only required method to define is getMappingFileName() wich returns the name
 * of the properties file, which contains the navigation rules, describing the
 * command and the success and error URI.
 * 
 * The format of this file is: commandName.command=foo.bar.MyClass ( fully
 * qualified name of the implementation class) commandName.sucess=mypage.jsp
 * (URI to use when forwarding a successfull request, this can be a JSP page or
 * another command. commandName.error=error.jsp (URI to use when forwarding an
 * unsuccessfull request, this can be a JSP page or another command.
 * <p>
 * <b><u>Navigation Rules</u></b><br>
 * 
 * <pre>
 * A Navigation rule provides information to the command-related servlet of a request-response paradigm
 * on how to process a request regarding forms, reporst,and remote services.   Four things can be
 *  identified by a command that instructs how the appliation should navigate based on certain conditions:
 * <ol>
 *   <li>the action handler that is to be executed.</li>
 *   <li>the URI that is to be executed upon success of the action handler.</li>
 *   <li>the URI that is to be executed upon failure of the action handler.</li>
 *   <li>the resource redirecting method to use.  Set to yes, y, or true.</li>
 * </ol>
 * 
 *  Configuring a navigation rule can be accomplished using the following format:
 *  <quoteblock>
 *       <i>[Entity Name or Functionality Name].[Action Name].[Command Type]</i>
 *  </quoteblock>
 * 
 *  The format for invoking a navigation rule goes as follows:
 *  <quoteblock>
 *     <i>[Properties file name].[Entity Name or Functionality Name].[Action Name].[Command Type]</i>
 *  </quoteblock>
 *  Where <i>Properties file name</i> is the name of the properties file (minus the 
 *  file extenion) that is the source of the rules.
 * 
 *  An example of invoking a edit navigation rule from a form on the User Search JSP in 
 *  which the rule exist in UserSearch.properties:
 *    &lt;form name="form1" method="POST" action="UserSearch.Search?clientAction=edit"&gt;
 * 
 *  "UserSearh" - the properties file.
 *  "Search" - The entity name of the rule.
 *  "clientAction=edit" - As a requirement processing command servlet, all Action Names must 
 *                        always be mapped to an URL argument named, "clientAction".
 * 
 *  To dynamically handle the "success" URI, leave success blank and pass the URI back to the
 *  command servlet via the request object using RMT2ServletConst.RESPONSE_HREF 
 *  as the attribute key.
 * 
 *  A report command is thought to be in the format of:
 *  [Application Module].[Report file name].[Action Name].[Command Type]
 * 
 *  Three things can be identified by a report command that instructs how the appliation should 
 *  navigate based on certain conditions:
 * <ol>
 *   <li>the action handler that is to be executed.</li>
 *   <li>the URI that is to be executed upon success of the action handler.</li>
 *   <li>the URI that is to be executed upon failure of the action handler.</li>
 * </ol>
 * 
 *  For example, this is a common transaction action handler:
 *    Project.EmployeeTimesheet.prepare.handler=com.action.report.ProjectTimesheetEditAction
 *    Project.EmployeeTimesheet.prepare.success=
 *    Project.EmployeeTimesheet.prepare.failure=/forms/sales/ErrorPage.jsp
 * 
 *  Since most reports display the polling JSP page as a response to the prepare command, the 
 *  "success"  URI should be left blank so that
 *    1) the action handler can set the polling URI for the command servlet via the request object
 *       using RMT2ServletConst.RESPONSE_HREF as the attribute key.
 *    2) or the report is streamed to the client via the HttpServletResponse object.
 * 
 *  A remote service command exist in the format of: [Properties file name].[Service Id].[Command Type].
 *  The service category exist as the portion of the servie URL that followss the servlet name.  
 *  The Service Id can be thouht of as the name used by an application to identify the service that 
 *  is to be invoked. Command Type represents the portion of the command that can be identified as 
 *  handler, success or error.
 * </pre>
 * 
 * @deprecated moved to com.api.jsp.controller package as
 *             AbstractCommandController. Will be removed in future versions
 */
public abstract class AbstractCommandServlet extends AbstractServlet {
    private static final long serialVersionUID = 2249635693626658698L;

    private static Logger logger = Logger.getLogger("AbstractCommandServlet");

    protected static final int SESSION_LOGGED_IN = 1;

    protected static final int SESSION_NOT_LOGGED_IN = -1;

    protected static final int SESSION_VALID = 100;

    protected static final int SESSION_INVALID = -100;

    protected static final String DEFAULT_LOGIN_URI = "/index.jsp";

    /**
     * Sets up logger and load application controller-action mappings.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    /**
     * Drives the process of interpreting and executing the user's request.
     * 
     * @param request
     *            HttpServletRequest
     * @param response
     *            HttpServletResponse
     * @throws StatelessControllerProcessingException
     */
    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response)
            throws StatelessControllerProcessingException {
        super.processRequest(request, response);
        Properties messages = new Properties();
        ResourceBundle mappings = null;
        String nextURL = null;
        String requestedCommand = null;
        String msg = null;
        boolean error = false;

        try {
            // Validate client action
            String clientAction = this.getAction(request);
            if (clientAction == null) {
                msg = "Client action was not specified...request failed";
                logger.error(msg);
                throw new SystemException(msg);
            }

            // Get user's command.
            requestedCommand = this.buildCommand(request);

            // Get Application command/action mappings
            try {
                mappings = this.loadMappings(requestedCommand);
            } catch (SystemException e) {
                msg = "Unable to obtain command/mapping for request";
                logger.error(msg);
                throw new ServletException(
                        "Unable to obtain command/mapping for request", e);
            }

            // Pre Execute Command routine, if available.
            int sessionState = this.doSessionCheck(request);

            // In the event we are dealing with a non-java based client in the
            // Development/Test environment and
            // the browser's session management functionality behaves a bit
            // peculiar rendering the session to be
            // invalid or the user's login is not evident...
            if (sessionState != AbstractCommandServlet.SESSION_LOGGED_IN
                    && !clientAction.equalsIgnoreCase("login")
                    && !clientAction.equalsIgnoreCase("verifyauthentication")) {
                sessionState = this.doSessionCheckForExternalRequest(request,
                        requestedCommand, sessionState);
            }

            // Determine if request should be continued based on the state of
            // the session.
            switch (sessionState) {
                case AbstractCommandServlet.SESSION_NOT_LOGGED_IN:
                    if (!clientAction.equalsIgnoreCase("login")) {
                        msg = "Unable to service HTTP request due to user is not logged into system";
                        logger.error(msg);
                        this.sendResponse(request, response, "/index.jsp");
                        return;
                    }
                    break;

                case AbstractCommandServlet.SESSION_INVALID:
                    List<String> commandParts = RMT2String.getTokens(
                            requestedCommand, ".");
                    if (commandParts != null) {
                        String firstPart = (String) commandParts.get(0);
                        // Display index page if not a Remote Service request
                        if (!firstPart.equalsIgnoreCase("Services")
                                && !firstPart.equalsIgnoreCase("Security")) {
                            msg = "Unable to service HTTP request due to user's session is invalid";
                            logger.error(msg);
                            this.sendResponse(request, response, "/index.jsp");
                            return;
                        }
                    }
                    break;

                default:
                    break;
            }

            // Execute the user' command.
            this.executeCommand(requestedCommand, request, response, mappings);

            // Check if command sent any messages for the client from its
            // execution.
            msg = (String) request
                    .getAttribute(RMT2ServletConst.REQUEST_MSG_INFO);
            if (msg != null) {
                messages.setProperty(RMT2ServletConst.PROPERTY_MSG_INFO, msg);
                request.removeAttribute(RMT2ServletConst.REQUEST_MSG_INFO);
            }
            nextURL = getSucessURL(mappings, requestedCommand).trim();
            logger.log(Level.DEBUG, "next url is success");
        } catch (Exception e) {
            if (requestedCommand != null && mappings != null) {
                nextURL = getErrorURL(mappings, requestedCommand);
            }
            else {
                nextURL = "error.jsp";
                error = true;
                requestedCommand = "error";
                // this.sendResponse(request, response, nextURL,
                // requestedCommand, mappings, error);
                return;
            }
            logger.log(Level.DEBUG, "next url is error");
            msg = RMT2Utility.getRootErrorMessage(e);
            messages.setProperty(RMT2ServletConst.REQUEST_MSG_INFO, msg);
            error = true;
            logger.error(e);
        } finally {
            // If messages exist from the command transaction (whether error or
            // not),
            // store the message object in the request.
            if (messages.size() > 0) {
                request.setAttribute(RMT2ServletConst.REQUEST_MSG_MESSAGES,
                        messages);
            }
        }

        // Redirect request.
        try {
            this.sendResponse(request, response, nextURL, requestedCommand,
                    mappings, error);
        } catch (ServletException e) {
            msg = "Error occurred processing servlet response";
            throw new StatelessControllerProcessingException(msg, e);
        }
    }

    /**
     * Verifies the validity of the current session and whether or not the user
     * is logged on to the current application.
     * 
     * @param request
     *            An instance of HttpServletRequest containing the session that
     *            is to be validated.
     * @return int 1 for user is logged into the application, -1 for user is not
     *         logged into application, or -100 for invalid or null session.
     * @throws SystemException
     */
    protected int doSessionCheck(HttpServletRequest request)
            throws SystemException {
        int rc = AbstractCommandServlet.SESSION_LOGGED_IN;
        HttpSession session = request.getSession(false);
        if (session == null) {
            rc = AbstractCommandServlet.SESSION_INVALID;
        }
        else {
            RMT2SessionBean sessionBean = (RMT2SessionBean) session
                    .getAttribute(RMT2ServletConst.SESSION_BEAN);
            if (sessionBean == null || sessionBean.getLoginId() == null) {
                rc = AbstractCommandServlet.SESSION_NOT_LOGGED_IN;
            }
            else {
                request.setAttribute(AuthenticationConst.AUTH_PROP_USERID,
                        sessionBean.getLoginId());
                request.setAttribute(AuthenticationConst.AUTH_PROP_MAINAPP,
                        sessionBean.getOrigAppId());
            }
        }
        return rc;
    }

    /**
     * Tries to obtain a temporary session for HTTP requests that point to
     * invalid session handles despite the fact that the requestor is designed
     * to be internal to the current web application context. These requests
     * generally exist within the current web application context and are not
     * web service calls. The nomenclature of a web service call exists in the
     * format of "Services.RQ_". Once a session is created, it will be
     * associated with the login id of <i>TempUser</i>
     * <p>
     * This method targets requests that potentially are associated with an
     * invalid session handle when 1) being tested in development mode and 2)
     * are invoked outside of the normal web application context. An invalid
     * session handle is usually detected via a call to
     * HttpServletRequest.getSession(false).
     * <p>
     * This is a special situation where the usage of multiple browsers
     * targeting the same application on a single machine or multiple tabs of a
     * single browser targeting the same application may not manage sessions in
     * a peculiar way depnending on the browser type. This is especially true in
     * testing environments which the client is invoked outside of the current
     * web application context and the containing browser does not manage
     * sessions appropriately.
     * 
     * @param request
     *            An instance of HttpServletRequest which points to an invalid
     *            session.
     * @param command
     *            The fully qualified extra path information of the request URL.
     *            The extra path information follows the servlet path but
     *            precedes the query string and will start with the character,
     *            "/". For example, <i>Services.RQ_business_contact_search</i>
     *            or <i>Security.Authentication.login</i>.
     * @param currentSessionStateCode
     *            The session state code before before evaluating <i>request</i>
     * @return int 1 for user is logged into the application, -1 for user is not
     *         logged into application, or -100 for invalid or null session.
     */
    protected int doSessionCheckForExternalRequest(HttpServletRequest request,
            String command, int currentSessionStateCode) {
        String tempLoginId = "TempUser";
        if (currentSessionStateCode != AbstractCommandServlet.SESSION_INVALID) {
            return currentSessionStateCode;
        }
        int rc = currentSessionStateCode;

        String env = System.getProperty(ConfigConstants.PROPNAME_ENVIRONMENT);
        String appCode = AppPropertyPool
                .getProperty(ConfigConstants.PROPNAME_APP_NAME);
        boolean webServiceCall = command.contains("Services.RQ_");
        boolean devMode = env.equals(ConfigConstants.ENVTYPE_DEV);

        // Only get session for those requests that are executed in development
        // environment and are not web service calls.
        if (devMode && !webServiceCall) {
            RMT2SessionBean bean = null;
            HttpSession session = request.getSession();
            bean = this.createTempSession(request, session, tempLoginId,
                    appCode);
            if (session == null) {
                rc = AbstractCommandServlet.SESSION_INVALID;
            }
            else if (bean == null) {
                rc = AbstractCommandServlet.SESSION_NOT_LOGGED_IN;
            }
            else {
                request.setAttribute(AuthenticationConst.AUTH_PROP_USERID,
                        bean.getLoginId());
                request.setAttribute(AuthenticationConst.AUTH_PROP_MAINAPP,
                        bean.getOrigAppId());
                rc = AbstractCommandServlet.SESSION_LOGGED_IN;
            }
        }
        return rc;
    }

    /**
     * Creates a temporary session instance and assoicates it with
     * <i>request</i>.
     * 
     * @param request
     * @param session
     * @param userId
     * @param appCode
     * @return
     */
    private RMT2SessionBean createTempSession(HttpServletRequest request,
            HttpSession session, String userId, String appCode) {
        if (request != null && session != null) {
            // Continue on...
        }
        else {
            return null;
        }
        Session s = HttpVariableScopeFactory.createHttpSession(session);
        Request r = HttpVariableScopeFactory.createHttpRequest(request);
        RMT2SessionBean sessionBean;
        try {
            sessionBean = UserAuthenticationFactory
                    .getSessionBeanInstance(r, s);
        } catch (AuthenticationException e) {
            throw new SystemException(e);
        }
        sessionBean.setLoginId(userId);
        sessionBean.setOrigAppId(appCode);

        SessionBeanManager sbm = SessionBeanManager.getInstance(r);
        sbm.addSessionBean(sessionBean);
        return sessionBean;
    }

    /**
     * Perform the mapping and execution of the targeted command found in the
     * user's request. The steps to execute a command invlove: <blockquote>
     * <ol>
     * <li>Identify the command's action handler.</li>
     * <li>Instantiate the action handler.</li>
     * <li>Process the command at the level of the action handler by invoking
     * the action handler's method, processRequest(HttpServletRequest,
     * HttpServletResponse, String).</li>
     * <li>Depending on wheter the process was successfull or not, forward the
     * response to the client.</li>
     * </ol>
     * </blockquote>
     * 
     * @param requestedCommand
     * @param request
     * @param response
     * @param mappings
     * @throws ActionHandlerException
     * @throws SystemException
     */
    private void executeCommand(String requestedCommand,
            HttpServletRequest request, HttpServletResponse response,
            ResourceBundle mappings) throws ActionCommandException,
            SystemException {
        String commandClassName;
        ICommand command;

        // gets the name of the handler class that implements the command
        commandClassName = this.getHandlerClassName(mappings, requestedCommand);

        logger.log(Level.DEBUG, "Requested Command Name=" + requestedCommand);
        logger.log(Level.DEBUG, "Requested Command Class Name="
                + commandClassName);

        // instantiates the command handler class, all command handler classes
        // must implement the Command interface.
        command = this.instantiateCommandHandler(commandClassName);

        logger.log(Level.INFO, "Executing client command: " + requestedCommand);
        Request genericRequest = HttpVariableScopeFactory
                .createHttpRequest(request);
        Response genericResponse = HttpVariableScopeFactory
                .createHttpResponse(response);
        try {
            command.processRequest(genericRequest, genericResponse,
                    requestedCommand);
        } catch (Exception e) {
            String msg = "Client command, " + requestedCommand + ", failed";
            logger.error(msg);
            throw new ActionCommandException(msg, e);
        } finally {
            logger.log(Level.INFO, "Client command, " + requestedCommand
                    + ", executed");
            command.close();
            logger.log(Level.INFO, "Client command, " + requestedCommand
                    + ", disposed");
        }
    }

    /**
     * Builds a fully qualified command from properties that exist in the user's
     * request.
     * 
     * @param request
     *            HttpServletRequest
     * @return The name of the request as a fully qualified name.
     * @throws ServletException
     *             If the client action was not supplied in the request.
     */
    private String buildCommand(HttpServletRequest request)
            throws ServletException {
        String requestedCommand;
        String requestedResource;
        String clientAction = this.getAction(request);
        String msg = null;

        if (clientAction == null) {
            msg = "Client action must be supplied in order to build the request command String";
            logger.error(msg);
            throw new ServletException(msg);
        }
        // Get the name of the command appropriate for this request
        requestedResource = this.parseCommandName(request.getPathInfo());
        requestedCommand = requestedResource + "." + clientAction;
        return requestedCommand;
    }

    /**
     * Parses the extra path information associated with the URL of the client's
     * request by removing the leading character, '\', from the command.
     * 
     * @param _clientAction
     * @return The actual name of the command.
     */
    private String parseCommandName(String _clientAction) {
        String requestedCommand = "";

        if (_clientAction != null) {
            requestedCommand = _clientAction.substring(1);
        }
        return requestedCommand;
    }

    /**
     * Instantiates an command handler class based on the command name. All
     * command classes must implement the Command interface and have the default
     * constructor.
     * 
     * @param _handlerClass
     *            The name of the command handler class that will be
     *            instantiated.
     * @return {@link ICommand}
     * @throws SystemException
     */
    protected ICommand instantiateCommandHandler(String _handlerClass)
            throws SystemException {
        ICommand command;

        command = (ICommand) RMT2Utility.getClassInstance(_handlerClass);
        return command;
    }

    /**
     * Gets the command handler class name for particular command name.
     * 
     * @param ResoourceBundle
     *            of mappings
     * @param mapping
     *            Represents the root command.
     * @return the specific command class name
     * @throws ActionHandlerException
     */
    protected String getHandlerClassName(ResourceBundle mappings, String mapping)
            throws ActionCommandException {
        String value = null;
        String newCommand = null;

        List<String> commandParts = RMT2String.getTokens(mapping, ".");
        try {
            newCommand = (String) commandParts.get(1) + "."
                    + (String) commandParts.get(2);
            value = (String) mappings.getString(newCommand + ".handler");
            return value;
        } catch (Exception e) {
            // Intentionally do nothing
        }

        try {
            value = (String) mappings.getString(mapping + ".handler");
            return value;
        } catch (Exception e) {
            String msg = "Failure to obtain/locate command handler class name for request";
            logger.error(msg);
            throw new ActionCommandException(e.getMessage(), e);
        }
    }

    /**
     * Gets the success URI for a particular command name
     * 
     * @param ResoourceBundle
     *            of mappings
     * @param mapping
     *            Represents the root command.
     * @return the specific command success URI.
     * @throws ActionHandlerException
     */
    private String getSucessURL(ResourceBundle mappings, String mapping) {
        String value = null;
        String newCommand = null;

        List<String> commandParts = RMT2String.getTokens(mapping, ".");
        try {
            newCommand = (String) commandParts.get(1) + "."
                    + (String) commandParts.get(2);
            value = (String) mappings.getString(newCommand + ".success");
            return value;
        } catch (Exception e) {
            // Intentionally do nothing
        }

        try {
            value = (String) mappings.getString(mapping + ".success");
            return value;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Gets the error URI for a particular command name
     * 
     * @param ResoourceBundle
     *            of mappings
     * @param mapping
     *            Represents the root command.
     * @return the specific command error URI.
     * @throws ActionHandlerException
     */
    private String getErrorURL(ResourceBundle mappings, String mapping) {
        String value = null;
        String newCommand = null;

        List<String> commandParts = RMT2String.getTokens(mapping, ".");
        try {
            newCommand = (String) commandParts.get(1) + "."
                    + (String) commandParts.get(2);
            value = (String) mappings.getString(newCommand + ".error");
            return value;
        } catch (Exception e) {
            // Intentionally do nothing
        }

        try {
            value = (String) mappings.getString(mapping + ".error");
            return value;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Gets the login page URI for a particular command name
     * 
     * @param ResoourceBundle
     *            pf mappings
     * @param mapping
     * @return
     */
    protected String getLoginURL(ResourceBundle mappings, String mapping) {
        return (String) mappings.getString(mapping + ".handler");
    }

    /**
     * Loads the navigation rules for the a given request so to determine what
     * URL the client will experience next. It is required that the navigation
     * rules reside in properties files in the classpath. The methods that are
     * used to locate and load the mappings will be performed in one of two ways
     * in the following order: 1) by calculating the name of the properties file
     * from the first section of the dot-separated command, which acts as part
     * of the request's URL, to load mappings or 2) by using the name of the
     * properties file specified in the SystemParms.properties which should
     * exist at the top of applications class path (WEB-INF/classes).
     * <p>
     * 
     * <pre>
     * An example of case #1 would be a HTML form's 
     * action set as a call to a servelt:
     *   <i>&lt;form name="form1" method="POST" action="UserSearch.Search?clientAction=edit"&gt;</i>
     *   where UserSearch will be interpretted as the name of the properies file to load
     *   the mappings associated with the request.
     * </pre>
     * 
     * @param command
     *            The requested command in full form.
     * @return ResourceBundle A resource bundle containing the application
     *         mappings.
     * @throws SystemException
     *             When application mappings cannot be loaded.
     */
    protected ResourceBundle loadMappings(String command)
            throws SystemException {
        String bundleName1 = null;
        String bundleName2 = null;
        ResourceBundle mappings = null;
        List<String> commandParts = RMT2String.getTokens(command, ".");

        // Attempt to load mappings using the first part of the requested
        // command as a reference to the properties file on the classpath.
        if (commandParts.size() > 0) {
            bundleName1 = (String) commandParts.get(0);
            String bundlePath = AppPropertyPool
                    .getProperty(ConfigConstants.PROPNAME_APP_MAPPING_PATH);
            bundlePath += "." + bundleName1;
            try {
                mappings = RMT2File.loadAppConfigProperties(bundlePath);
            } catch (Exception e) {
                mappings = null;
                logger.error(
                        "Request mapping could not be loaded from dedicated request bundle specified as, "
                                + bundlePath
                                + ", for application, "
                                + this.appCtx, e);
            }
        }

        // Attempt to load mappings using a resource specified in a properties
        // file.
        if (mappings == null) {
            try {
                bundleName2 = AppPropertyPool
                        .getProperty(ConfigConstants.PROPNAME_APP_MAPPING);
                mappings = RMT2File.loadAppConfigProperties(bundleName2);
            } catch (Exception e) {
                mappings = null;
                String msg = "Request property mappings load failure.  Property mappings for "
                        + this.appCtx
                        + " application request could not be loaded from bundle(s) "
                        + bundleName1 + " or " + bundleName2;
                logger.error(msg);
                throw new SystemException(msg, e);
            }
        }
        return mappings;
    }

    /**
     * Implement this method to send the response to the client.
     * 
     * @param request
     *            The processed request.
     * @param response
     *            The response
     * @param nextURL
     *            The target response URL.
     * @param requestedCommand
     *            The root of the requested command
     * @param error
     *            boolean indicating whether or not an error occurred.
     * @throws ServletException
     */
    protected abstract void sendResponse(HttpServletRequest request,
            HttpServletResponse response, String nextURL,
            String requestedCommand, ResourceBundle mappings, boolean error)
            throws ServletException;

    /**
     * 
     * @param request
     * @param response
     * @param url
     * @throws ServletException
     */
    private void sendResponse(HttpServletRequest request,
            HttpServletResponse response, String url) throws ServletException {
        String msg = null;
        // Redirect resource outside of context.
        try {
            String root = RMT2WebUtility.getWebAppContext(request);
            url = root + url;
            this.doPreSendResponse(request, response, url, null);
            response.sendRedirect(url);
        } catch (IOException e) {
            msg = "An IOException occured when trying to redirect response to  "
                    + url
                    + (e.getMessage() != null ? ".  " + "  Other messages: "
                            + e.getMessage() : "");
            logger.log(Level.ERROR, msg);
            e.printStackTrace();
            throw new ServletException(msg, e);
        } catch (IllegalStateException e) {
            msg = "An IllegalStateException occured when trying to redirect response to  "
                    + url
                    + ".   Probable cause is response was already committed or a partial URL is given and cannot be converted into a valid URL.  "
                    + (e.getMessage() != null ? "  Other messages: "
                            + e.getMessage() : "");
            logger.log(Level.ERROR, msg);
            e.printStackTrace();
            throw new ServletException(msg, e);
        }
    }

    /**
     * Implement this method to perform some logic just before the request is
     * forwarded to the client.
     * 
     * @param request
     *            The request object
     * @param response
     *            The response object
     * @param url
     *            The URL to forward.
     * @param data
     *            An arbitrary object to hold extra data for processing.
     */
    protected abstract void doPreSendResponse(HttpServletRequest request,
            HttpServletResponse response, String url, Object data);
}
