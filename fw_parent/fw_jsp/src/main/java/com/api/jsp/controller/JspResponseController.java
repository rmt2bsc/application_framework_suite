package com.api.jsp.controller;

import java.io.IOException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.api.constants.RMT2ServletConst;
import com.api.web.ActionCommandException;
import com.api.web.util.RMT2WebUtility;

/**
 * Concrete implementation of the AbstractCommandController which is designed to
 * process the client's HTTP request and return the response to the client as a
 * JSP resource using one or more native Java objects stored onto the Request
 * and Session contexts as data for its presentation. In most cases the client
 * will find most of the reply to exist on the Request instance.
 * 
 * @author appdev
 * 
 */
public class JspResponseController extends AbstractCommandController {
    private static final long serialVersionUID = 348875533953752618L;

    protected Logger logger;

    /**
     * Performs general initialization.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.logger = Logger.getLogger("JspResponseController");
        this.logger.log(Level.INFO, "Native Java Response Servlet initialized");
    }

    protected void doPreSendResponse(HttpServletRequest request, HttpServletResponse response, String url, Object data) {
        return;
    }

    /**
     * Sends the response to the JSP client, which is capable of interpreting
     * and managing the Java objects stored on the Request or Session contexts
     * as data for its presentation. The JSP resource, <i>nextURL</i>, is sent
     * either as a forward or as a sendRedirect.
     * 
     * @param request
     *            The user's request containing one or more java objects that
     *            are to be used by the client.
     * @param response
     *            The user's response
     * @param nextURL
     *            The target response URL which is usually a JSP resource.
     * @param requestedCommand
     *            The root of the requested command
     * @param error
     *            boolean indicating whether or not an error occurred. Will be
     *            set to <i>true</i> when an error exists and set to
     *            <i>false</i> when no errors exists
     * @throws ServletException
     */
    protected void sendResponse(HttpServletRequest request, HttpServletResponse response, String nextURL,
            String requestedCommand,
            ResourceBundle mappings, boolean error) throws ServletException {
        // Get the protocol of the response url
        int protocolType = this.getResponseProtocolType(request);
        logger.log(Level.DEBUG, "next url protocol:  " + protocolType);

        // Optionally, the commands might not need to redirect to a JSP source.
        // If true, skip attempt to redirect JSP to the client.
        if (nextURL == null || nextURL.length() == 0) {
            // Check to see if the next URL was dynamically set during the
            // execution
            // of the action handler completed.
            nextURL = (String) request.getAttribute(RMT2ServletConst.RESPONSE_HREF);
            if (nextURL == null) {
                return;
            }
        }

        // Encode the resulting URL
        nextURL = response.encodeURL(nextURL);
        if (nextURL.charAt(0) != '/' && protocolType <= RMT2ServletConst.RESPONSE_PROTOCOL_NORMAL) {
            nextURL = "/" + nextURL;
        }
        else {
            // TODO: Apply the proper protocol based on the value of
            // "protocolType".
        }

        String msg = null;
        // Set cookies
        String clientAction = this.getAction(request);
        Object obj = null;
        try {
            if (clientAction != null && clientAction.equalsIgnoreCase(RMT2ServletConst.LOGIN_ACTION)) {
                obj = request.getSession(false).getAttribute(RMT2ServletConst.SESSION_BEAN);
                this.createCookies(response, obj);
            }
        } catch (Exception e) {
            msg = "Failure to create cookies based on current session instance: " + obj.getClass().getSimpleName();
            throw new ServletException(msg, e);
        }

        // Redirect request.
        logger.log(Level.DEBUG, "Redirecting to: " + nextURL);

        // Get cache mode setting to determine if reply is to be sent via a
        // RequestDispatch forward or a Response sendRedirect invocation.
        boolean cacheRequest;
        try {
            cacheRequest = this.cacheData(mappings, requestedCommand);
        } catch (ActionCommandException e) {
            msg = "Failure to obtain cache data in order to send response";
            logger.error(msg);
            e.printStackTrace();
            throw new ServletException(msg, e);
        }

        if (cacheRequest || error) {
            // Navigate to next resource using forward.
            this.doPreSendResponse(request, response, nextURL, null);
            RequestDispatcher dispatcher = request.getRequestDispatcher(nextURL);
            if (dispatcher == null) {
                msg = "RequestDispatcher is NULL for: " + nextURL;
                logger.error(msg);
                throw new ServletException(msg);
            }
            try {
                dispatcher.forward(request, response);
            } catch (ServletException e) {
                msg = "A ServletException occured when trying to forward response to  " + nextURL
                        + (e.getMessage() != null ? ".  " + "  Other messages: " + e.getMessage() : "");
                logger.log(Level.ERROR, msg);
                e.printStackTrace();
                throw new ServletException(msg, e);
            } catch (IOException e) {
                msg = "An IOException occured when trying to forward response to  "
                        + nextURL + (e.getMessage() != null ? ".  " + "  Other messages: " + e.getMessage() : "");
                logger.log(Level.ERROR, msg);
                e.printStackTrace();
                throw new ServletException(msg, e);
            } catch (IllegalStateException e) {
                msg = "An IllegalStateException occured when trying to forward response to  "
                        + nextURL + ".   Probable cause is response was already committed.  "
                        + (e.getMessage() != null ? "  Other messages: " + e.getMessage() : "");
                logger.log(Level.ERROR, msg);
                e.printStackTrace();
                throw new ServletException(msg, e);
            }
        }
        else {
            // Redirect resource outside of context.
            try {
                String root = RMT2WebUtility.getWebAppContext(request);
                nextURL = root + nextURL;
                this.doPreSendResponse(request, response, nextURL, null);
                response.sendRedirect(nextURL);
            } catch (IOException e) {
                msg = "An IOException occured when trying to redirect response to  "
                        + nextURL + (e.getMessage() != null ? ".  " + "  Other messages: " + e.getMessage() : "");
                logger.log(Level.ERROR, msg);
                e.printStackTrace();
                throw new ServletException(msg, e);
            } catch (IllegalStateException e) {
                msg = "An IllegalStateException occured when trying to redirect response to  "
                        + nextURL
                        + ".   Probable cause is response was already committed or a partial URL is given and cannot be converted into a valid URL.  "
                        + (e.getMessage() != null ? "  Other messages: "
                                + e.getMessage() : "");
                logger.log(Level.ERROR, msg);
                e.printStackTrace();
                throw new ServletException(msg, e);
            }
        }
    }

    /**
     * Determines the protocol in which the servlet is to send its response.
     * Valid protocols are HTTP, FTP, or MAILTO.
     * 
     * @param request
     *            The user's request which serves as the input source of the
     *            specified protocol. Defaults to the protocol in which the
     *            request was governed by if the protocol is discovered to be
     *            invalid, not specified, or null.
     * @return The protocol type.
     */
    private int getResponseProtocolType(HttpServletRequest request) {
        int type = 0;
        String temp;
        temp = (String) request
                .getAttribute(RMT2ServletConst.RESPONSE_PROTOCOL);
        if (temp == null) {
            // Protocol was not indicated, so default to normal
            return RMT2ServletConst.RESPONSE_PROTOCOL_NORMAL;
        }
        try {
            type = Integer.parseInt(temp);
            // Validate protocol type's value
            switch (type) {
                case RMT2ServletConst.RESPONSE_PROTOCOL_HTTP:
                case RMT2ServletConst.RESPONSE_PROTOCOL_FTP:
                case RMT2ServletConst.RESPONSE_PROTOCOL_MAILTO:
                    break;

                default:
                    // default to normal if type value is not a valid value
                    type = RMT2ServletConst.RESPONSE_PROTOCOL_NORMAL;
            }
        } catch (NumberFormatException e) {
            type = RMT2ServletConst.RESPONSE_PROTOCOL_NORMAL;
        }
        return type;
    }

    /**
     * Determines the cache setting specified in the navigation rules for the
     * target servlet command. The cache navigation setting determines the mode
     * in which the response is sent to the JSP client. When true, the a
     * response is sent via a RequestDispather forward operation. When false,
     * the response is sent using the Response sendRedirect method.
     * 
     * @param mappings
     *            ResoourceBundle of mappings
     * @param mapping
     *            Represents the root command.
     * @return boolean Returns true when cache property equals "yes", "y", or
     *         "true". Otherwise, false is returned. By default, the cache
     *         setting is "yes".
     */
    protected boolean cacheData(ResourceBundle mappings, String mapping)
            throws ActionCommandException {
        String value = null;
        String key = mapping + ".cache";
        String msg = null;
        try {
            value = (String) mappings.getString(key);
            return value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("y") || value.equalsIgnoreCase("true");
        } catch (NullPointerException e) {
            msg = "Key, cache, was not specified.";
            logger.error(msg);
            throw new ActionCommandException(msg);
        } catch (MissingResourceException e) {
            return true;
        } catch (ClassCastException e) {
            msg = "The value found for key, " + key + ", must be a String";
            logger.error(msg);
            throw new ActionCommandException(msg);
        }
    }
}
