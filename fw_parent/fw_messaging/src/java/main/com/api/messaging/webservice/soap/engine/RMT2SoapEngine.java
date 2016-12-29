package com.api.messaging.webservice.soap.engine;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.apache.log4j.Logger;










//import com.InvalidDataException;
import com.NotFoundException;
import com.SystemException;
import com.api.messaging.MessageException;
import com.api.messaging.MessageRoutingException;
//import com.api.messaging.MessageRoutingInfo;
//import com.api.messaging.MessagingRouter;

import com.api.messaging.webservice.router.MessageRouterHelper;
//import com.api.messaging.webservice.router.MessageRouterSoapImpl;
import com.api.messaging.webservice.soap.SoapConstants;
import com.api.messaging.webservice.soap.SoapMessageHelper;
import com.api.messaging.webservice.soap.SoapResponseException;
import com.api.messaging.webservice.soap.client.SoapProductBuilder;
import com.api.web.Request;
import com.api.web.Response;
import com.api.web.controller.AbstractServlet;
import com.api.web.controller.StatelessControllerProcessingException;
import com.api.web.controller.scope.HttpVariableScopeFactory;

/**
 * A servlet for accepting SOAP based web service requests, dispatching the web
 * service request to its intended destination, packaging the SOAP response, and
 * sending the SOAP response to the intended caller.
 * <p>
 * <b><u>Configuration notes</u></b><br>
 * <ul>
 * <li>
 * It is highly important that the concrete class implements method,
 * {@link RMT2SoapEngine#getMessageRoutingInfo(SOAPMessage, Object)
 * getMessageRoutingInfo} so that web service routing information is properly
 * obtained for the SOAP message.</li>
 * <li>
 * It is <b>no longer</b> a requirement for an application to identify the class
 * name of the SOAP messaging router in the AppParms.properties. </u>
 * 
 * 
 * @author appdev
 * 
 */
public class RMT2SoapEngine extends AbstractServlet {

    private static final long serialVersionUID = 5433010431085175539L;

    private static Logger logger = Logger.getLogger(RMT2SoapEngine.class);

    // /**
    // * The message router that is used to contact the appropriate service
    // handler for a
    // * given web service.
    // * <p>
    // * An instance of {@link MessageRouterSoapImpl} is used as the
    // implementation and
    // * usually instantiated in the <i>initServlet</i> method..
    // */
    // protected MessagingRouter router;

    /**
     * 
     */
    public RMT2SoapEngine() {
        return;
    }

    /**
     * Drives the initialization process when the SoapMessagingController is
     * first loaded.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    /**
     * Creates the appropriate message router object using the implementaion
     * specified by the client.
     * 
     * @throws StatelessControllerProcessingException
     */
    public void initServlet() throws StatelessControllerProcessingException {
        super.initServlet();
        // this.router = new MessageRouterSoapImpl();
        // try {
        // RMT2BeanUtility beanUtil = new RMT2BeanUtility();
        // String routerClass =
        // AppPropertyPool.getProperty(WebServiceConstants.MSG_ROUTER_KEY_SOAP);
        // this.router = (MessagingRouter) beanUtil.createBean(routerClass);
        // }
        // catch (Exception e) {
        // String msg = "Unable to instantiate SOAP Message Router Api";
        // logger.fatal(msg, e);
        // e.printStackTrace();
        // throw new StatelessControllerProcessingException(msg, e);
        // }
        return;
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
     * <li>Invoke the service. It is expected of the service to send its reply
     * as a valid SOAP String.</li>
     * </ol>
     * 
     * @param request
     *            The request object
     * @param response
     *            The response object
     * @throws StatelessControllerProcessingException
     *             When a problem arises loading services list, validating input
     *             parameters, or invoking the service.
     */
    public void processRequest(HttpServletRequest request,
            HttpServletResponse response)
            throws StatelessControllerProcessingException {
        super.processRequest(request, response);
        String msg = null;
        SOAPMessage results;
        String serviceId = null;
        // MessageRoutingInfo routingInfo = null;

        Request genericRequest = HttpVariableScopeFactory
                .createHttpRequest(request);
        Response genericResponse = HttpVariableScopeFactory
                .createHttpResponse(response);

        SoapMessageHelper helper = new SoapMessageHelper();

        SOAPMessage sm = null;
        try {
            // Get SOAP message instance from the Object Input Stream of the
            // request
            sm = helper.getSoapInstance(genericRequest);

            // Obtain the message id from the SOAP message header.
            try {
                serviceId = helper.getHeaderValue(SoapProductBuilder.HEADER_NS
                        + ":" + SoapConstants.SERVICEID_NAME, sm);
            } catch (SOAPException e) {
                msg = "Error occurred attempting to obtain the Service/Message Id from the SOAP header of the request message";
                throw new StatelessControllerProcessingException(msg, e);
            } catch (NotFoundException e) {
                msg = "Service/Message Id is a required element in the SOAP header but was not found";
                throw new StatelessControllerProcessingException(msg, e);
            }

            // // Validate SOAP message and get routing information for message
            // if (this.router == null) {
            // msg = "The SOAP engine router object is invalid or null";
            // logger.error(msg);
            // throw new StatelessControllerProcessingException(msg);
            // }
            // routingInfo = this.router.getRoutingInfo(serviceId);

            // Invoke the service. The consumer is required to send the response
            // as a valid SOAP String.
            // results = this.invokeService(routingInfo, sm, request);
            results = this.invokeService(serviceId, sm);

            // Return the results of the service invocation to the requestor.
            this.sendResponse(genericResponse, results);
        } catch (MessageException e) {
            msg = "Error occurred processing incoming SOAP message. "
                    + e.getMessage();
            this.sendErrorResponse(response, msg);
            throw new StatelessControllerProcessingException(msg, e);
        }
        // catch (MessageRoutingException e) {
        // msg =
        // "General error occurred obtaining SOAP message routing information. "
        // + e.getMessage();
        // this.sendErrorResponse(response, msg);
        // throw new StatelessControllerProcessingException(msg, e);
        // }
        catch (NotFoundException e) {
            msg = "Service was not found in service registry by message id, "
                    + serviceId + ". " + e.getMessage();
            this.sendErrorResponse(response, msg);
            throw new StatelessControllerProcessingException(msg, e);
        }
        // catch (InvalidDataException e) {
        // msg =
        // "Invalid data was discovered while obtaining SOAP message routing information. "
        // + e.getMessage();
        // this.sendErrorResponse(response, msg);
        // throw new StatelessControllerProcessingException(msg, e);
        // }
    }

    /**
     * Performs the invocation of a remote service and returns the results to
     * the caller.
     * 
     * @param messageId
     *            The message Id of the service to invoke
     * @param soapObj
     *            The payload as an instance of {@link SOAPMessage}
     * @throws MessageException
     *             The routing of the message errored
     * @throws SoapEngineException
     *             The SOAP engine router is invalid or has not been
     *             initialized.
     */
    private SOAPMessage invokeService(String messageId, SOAPMessage soapObj)
            throws SoapEngineException, MessageException {
        String msg = null;
        MessageRouterHelper helper = new MessageRouterHelper();
        try {
            return helper.routeSoapMessage(messageId, soapObj);
        } catch (MessageRoutingException e) {
            msg = "Error occurred routing SOAP message to its designated handler";
            logger.error(msg);
            throw new MessageException(e);
        }
    }

    // /**
    // * Performs the invocation of a remote service and returns the results to
    // the caller.
    // *
    // * @param url
    // * The URL of the remote service to invoke.
    // * @param parms
    // * The parameters that are required to be assoicated with the remote
    // service
    // * URL.
    // * @throws MessageException
    // * The routing of the message errored
    // * @throws SoapEngineException
    // * The SOAP engine router is invalid or has not been initialized.
    // */
    // private SOAPMessage invokeService(MessageRoutingInfo srvc, SOAPMessage
    // soapObj, HttpServletRequest request) throws SoapEngineException,
    // MessageException {
    // SOAPMessage reslults = null;
    // String msg = null;
    // try {
    // SoapMessageHelper helper = new SoapMessageHelper();
    // String soapStr = helper.toString(soapObj);
    // logger.info("SOAP Request: ");
    // logger.info(soapStr);
    // reslults = (SOAPMessage) this.router.routeMessage(srvc, soapObj);
    // soapStr = helper.toString(reslults);
    // logger.info("SOAP Response: ");
    // logger.info(soapStr);
    // return reslults;
    // }
    // catch (MessageRoutingException e) {
    // msg = "Error occurred routing SOAP message to its designated handler";
    // logger.error(msg);
    // throw new MessageException(e);
    // }
    // }

    /**
     * Sends a response to the client indicating that web service invocation was
     * a success.
     * <p>
     * The results will either be a SOAP message with a normal payload or one
     * with a Fault body derived from text of some Exception. Both types of
     * errors should be recognized as business errors as it relates to the web
     * service invoked.
     * 
     * @param response
     *            The servlet response object used for transmitting the data
     *            back to the client.
     * @param soapObj
     *            An arbitrary object representing the data to be transmitted.
     * @throws IOException
     *             For general I/O errors with the output stream.
     */
    protected void sendResponse(Response response, SOAPMessage soapObj)
            throws SoapResponseException {
        SoapMessageHelper helper = new SoapMessageHelper();
        helper.sendSoapInstance(response, soapObj);
        return;
    }

    /**
     * Sends a response to the client indicating that the web service invocation
     * failed.
     * <p>
     * Generally this type of error occurs within the context of the SOAP engine
     * and are typically considered to be system failures.
     * 
     * @param response
     * @param errorMessage
     * @throws SoapResponseException
     */
    protected void sendErrorResponse(HttpServletResponse response,
            String errorMessage) throws SystemException {
        Response genericResponse = HttpVariableScopeFactory
                .createHttpResponse(response);
        SoapMessageHelper helper = new SoapMessageHelper();
        errorMessage = "SOAP Engine Failure.   " + errorMessage
                + ".   Please contact Technical Support!";
        SOAPMessage err = helper.createSoapFault("Server", errorMessage, null,
                null);
        try {
            this.sendResponse(genericResponse, err);
        } catch (SoapResponseException e) {
            throw new SystemException(e);
        }
        return;
    }

}
