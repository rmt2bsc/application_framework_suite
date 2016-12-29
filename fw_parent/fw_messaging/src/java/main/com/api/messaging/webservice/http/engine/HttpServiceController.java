package com.api.messaging.webservice.http.engine;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.Properties;

import com.SystemException;
import com.api.config.AppPropertyPool;
import com.api.config.ConfigConstants;
import com.constants.GeneralConst;
import com.api.messaging.MessageRoutingInfo;
import com.api.messaging.webservice.ServiceRegistryFactory;
import com.api.messaging.webservice.ServiceRegistryFactoryImpl;
import com.api.messaging.webservice.ServiceRegistry;
import com.api.messaging.webservice.WebServiceConstants;
import com.api.messaging.webservice.http.HttpClient;
import com.api.security.authentication.web.AuthenticationConst;
import com.api.web.controller.AbstractServlet;
import com.api.web.controller.StatelessControllerProcessingException;

/**
 * Identifies, invokes and dispatches remote service requests using URL
 * connection communication methods via the HTTP protocol. This servlet acts as
 * a liason between and serves as a hub for the application of the requester and
 * the remote services.
 * <p>
 * To invoke a remote service, the user must build a URL that targets the
 * service such as: <br>
 * <code>
 *      <protocol>://<host computer><:port>/<ServiceDispatcher>/<remoteservices>/?<clientAction=xxxx&UID=xxxx&appcode=xxxxx>
 *   </code>.
 * <p>
 * This servlet is responsible for creating a list of avaiable services that can
 * be accessed by the public. This list will reside in memory for the life of
 * the applicaltin and serves as a lookup mechanism for validating service id's
 * and for obtaiing the associated URL of the service id. The following keys are
 * configured in the ServiceDispatcher's configuration file,
 * SystemParms.properties, to provide values for some of the sections of the
 * URL:
 * <ul>
 * <li>service_host - identifies the host computer, web application, and servlet
 * separated by "/".</li>
 * <li>service_module - identifies the command module configured in the
 * applications's AppCommandMappings.properties. The value of the command module
 * should be prefixed by "/".</li>
 * </ul>
 * 
 * @author appdev
 * 
 */
public class HttpServiceController extends AbstractServlet {
    private static final long serialVersionUID = 8295709423196681300L;

    /**
     * User Resource Access Verification service name
     */
    private static final String CHECK_SERVICE_ACCESS = "checkserviceaccess";

    private static Map<String, MessageRoutingInfo> SERVICES;

    private static Logger logger = Logger.getLogger("HttpServiceController");

    /**
     * Drives the process of initializing the HttpServiceController.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    /**
     * Initializes the HttpServiceController member variables.
     */
    public void initServlet() throws StatelessControllerProcessingException {
        // Load registry with HTTP URL query string based web services
        try {
            if (HttpServiceController.SERVICES == null) {
                ServiceRegistryFactory f = new ServiceRegistryFactoryImpl();
                ServiceRegistry regMgr = f.getHttpServiceRegistryManager();
                HttpServiceController.SERVICES = regMgr
                        .loadServices(WebServiceConstants.WEBSERV_TYPE_HTTP);
            }
        } catch (Exception e) {
            String msg = "Failed to load web services registry for HTTP Service controller";
            logger.fatal(msg, e);
            throw new StatelessControllerProcessingException(msg, e);
        }

    }

    /**
     * Processes the request to invoke a particular remote service. The
     * following sequence of events occurs when a remote service is requested:
     * <ol>
     * <li>Load list of services into memory during the first time of servlet
     * invocation.</li>
     * <li>Collect request parameters into a Properties collection.</li>
     * <li>Validate the request parameters.</li>
     * <li>Validate the service id by obtaining its related URL.</li>
     * <li>Invoke the service.</li>
     * </ol>
     * 
     * @param request
     *            The request object
     * @param response
     *            The response object
     * @throws ServletException
     *             When a problem arises loading services list, validating input
     *             parameters, or invoking the service.
     */
    public void processRequest(HttpServletRequest request,
            HttpServletResponse response)
            throws StatelessControllerProcessingException {
        super.processRequest(request, response);
        String msg;

        // Gather request parameters into a Properties collection
        Properties prop = this.getRequestParms(request);

        try {
            // Verify that requrestor sent required parameters.
            this.validateRequestParameters(prop);

            // Validate Service id by attempting to obtain its assoicated URL
            String serviceId = request.getParameter(GeneralConst.REQ_SERVICEID);
            MessageRoutingInfo srvc = (MessageRoutingInfo) HttpServiceController.SERVICES
                    .get(serviceId);
            if (srvc == null) {
                msg = "Requested HTTP web service does not exist or is invalid: "
                        + serviceId;
                logger.log(Level.ERROR, msg);
                throw new SystemException(msg);
            }
            if (!serviceId.equalsIgnoreCase(srvc.getMessageId())) {
                msg = "A naming conflict exist between the HTTP web service id input ["
                        + serviceId
                        + "] and the HTTP web service registry data ["
                        + srvc.getMessageId() + "]";
                logger.log(Level.ERROR, msg);
                throw new SystemException(msg);
            }
            if (srvc.getDestination() == null) {
                msg = "The URL property of the requested HTTP web service, "
                        + serviceId + ", does not exist or is invalid: ";
                logger.log(Level.ERROR, msg);
                throw new SystemException(msg);
            }

            // Invoke the service
            Object results = this.invokeService(response, srvc, prop);

            // Return the results of the service invocation to the requestor.
            this.sendResponse(response, results);
        } catch (SystemException e) {
            this.sendResponse(response, e);
        }
    }

    /**
     * Verifies that the requestor of a service has supplied the minimum data
     * items needed to invoke a remote service. The required data items are:
     * service id.
     * 
     * @param parms
     *            Properties collection of the request parameters.
     * @throws SystemException
     *             If one of the parameters is invalid or null.
     */
    protected void validateRequestParameters(Properties parms)
            throws SystemException {
        String temp;
        String msg;

        // Service Id is required.
        temp = parms.getProperty(GeneralConst.REQ_SERVICEID);
        if (temp == null) {
            msg = "Remote service cannot be invoked without a Service Id input parameter";
            logger.log(Level.ERROR, msg);
            throw new SystemException(msg);
        }
        // User login id is required
        temp = parms.getProperty(AuthenticationConst.AUTH_PROP_USERID);
        if (temp == null) {
            msg = "Remote service cannot be invoked without the user\'s Login Id input parameter";
            logger.log(Level.ERROR, msg);
            throw new SystemException(msg);
        }
        // User's application id is required.
        temp = parms.getProperty(AuthenticationConst.AUTH_PROP_MAINAPP);
        if (temp == null) {
            msg = "Remote service cannot be invoked without the user\'s Application Id input parameter";
            logger.log(Level.ERROR, msg);
            throw new SystemException(msg);
        }
    }

    /**
     * Performs the invocation of a remote service and returns the results to
     * the caller.
     * 
     * @param response
     *            The HttpServletResponse object used to send the results of the
     *            service call back to the requestor. The results are passed
     *            back as an Object via the ObjectOutputStream.
     * @param url
     *            The URL of the remote service to invoke.
     * @param parms
     *            The parameters that are required to be assoicated with the
     *            remote service URL.
     * @throws SystemException
     *             When login id cannot be determined for secured services, http
     *             client errors, general I/O errors, or a class not found error
     *             pertaining to the object returned by the remote service.
     */
    protected Object invokeService(HttpServletResponse response,
            MessageRoutingInfo srvc, Properties parms) throws SystemException {
        String msg;
        String loginId;

        // Determine if user is authorized to access this service provided that
        // this is a secured service.
        if (srvc.isSecured()) {
            Properties srvcCheckProps = new Properties();
            srvcCheckProps.setProperty("ResrcName", srvc.getMessageId());

            // Determine if the user's login id was supplied
            loginId = parms.getProperty(AuthenticationConst.AUTH_PROP_USERID);
            if (loginId == null) {
                msg = "Service invocation failed: Service, "
                        + srvc.getMessageId()
                        + ", requires a user login id in order to determine authorization";
                logger.log(Level.ERROR, msg);
                throw new SystemException(msg);
            }
            srvcCheckProps.setProperty("UserLoginId", loginId);
            MessageRoutingInfo authSrvc = new MessageRoutingInfo();
            authSrvc.setMessageId(HttpServiceController.CHECK_SERVICE_ACCESS);
            this.authorizeUserAccess(authSrvc, srvcCheckProps);
        }

        parms.setProperty(GeneralConst.REQ_CLIENTACTION, srvc.getMessageId());

        // Complete the format of the service's URL
        String server = System.getProperty(ConfigConstants.PROPNAME_SERVER);
        String url = server + "/" + srvc.getDestination();

        try {
            // Prepare to invoke the service.
            HttpClient http = new HttpClient(url);
            InputStream in = (InputStream) http.sendPostMessage(parms);
            // Obtain the results of the service invocation.
            ObjectInputStream ois = new ObjectInputStream(in);
            Object data = null;
            if (ois != null) {
                data = ois.readObject();
            }
            http.close();
            return data;
        } catch (EOFException e) {
            msg = "EOF I/O error: End of input stream reached unexpectedly or input URL, "
                    + url + ", does not exist";
            logger.log(Level.ERROR, msg);
            throw new SystemException(msg);
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }

    /**
     * Determines whether or not the user has access to a particular service.
     * 
     * @param srvc
     *            {@link com.HttpServiceController.ServiceController.ServiceRoutingInfo
     *            ServiceFields}
     * @param prop
     *            A Hash collection containing key/value pairs for DataSource
     *            view properties "ResrcName" and "UserLoginId".
     * @throws SystemException
     *             When user fails authorization or for general communication
     *             errors.
     */
    protected void authorizeUserAccess(MessageRoutingInfo srvc, Properties prop)
            throws SystemException {
        String msg = null;

        // Complete the format of the service's URL
        String server = AppPropertyPool
                .getProperty(ConfigConstants.PROPNAME_ENVIRONMENT);
        String url = server + srvc.getDestination();

        try {
            // Prepare to invoke the service.
            HttpClient http = new HttpClient(url);
            InputStream in = (InputStream) http.sendPostMessage(prop);
            // Obtain the results of the service invocation.
            ObjectInputStream ois = new ObjectInputStream(in);
            Object data = null;
            if (ois != null) {
                data = ois.readObject();
            }
            // User is considered authorized if data is valid
            if (data == null || data instanceof Exception) {
                msg = "User, "
                        + prop.getProperty(AuthenticationConst.AUTH_PROP_USERID)
                        + ", is not authorized to access service, "
                        + srvc.getMessageId();
                logger.log(Level.ERROR, msg);
                throw new SystemException(msg);
            }
            http.close();
            return;
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }

    /**
     * Returns the data back to the client as a response to a service call. The
     * results will either be some arbitrary object or an Exception which should
     * contain message text explaining the cause of the error.
     * 
     * @param response
     *            The servlet response object used for transmitting the data
     *            back to the client.
     * @param data
     *            An arbitrary object representing the data to be transmitted.
     * @throws SystemException
     *             For general I/O errors with the output stream. IOException
     */
    private void sendResponse(HttpServletResponse response, Object data)
            throws SystemException {
        // Return the exception to the requestor.
        String content = "application/x-java-serialized-object";
        response.setContentType(content);

        // Write the serialized object as post data
        ObjectOutputStream os;
        try {
            os = new ObjectOutputStream(response.getOutputStream());
            os.writeObject(data);
            os.flush();
            os.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new SystemException(e);
        } finally {
            os = null;
        }

        return;
    }

} // End Class
