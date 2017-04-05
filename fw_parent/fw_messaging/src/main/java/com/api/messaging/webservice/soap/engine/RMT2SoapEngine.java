package com.api.messaging.webservice.soap.engine;

import java.util.Date;
import java.util.List;

import javax.activation.DataHandler;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.SOAPMessage;

import org.apache.log4j.Logger;

import com.NotFoundException;
import com.SystemException;
import com.api.messaging.MessageException;
import com.api.messaging.webservice.router.MessageRouterHelper;
import com.api.messaging.webservice.router.MessageRoutingException;
import com.api.messaging.webservice.router.MessageRoutingInfo;
import com.api.messaging.webservice.soap.SoapMessageHelper;
import com.api.messaging.webservice.soap.SoapResponseException;
import com.api.web.Request;
import com.api.web.Response;
import com.api.web.controller.AbstractServlet;
import com.api.web.controller.StatelessControllerProcessingException;
import com.api.web.controller.scope.HttpVariableScopeFactory;
import com.api.xml.RMT2XmlUtility;
import com.util.RMT2Date;

/**
 * A servlet for accepting SOAP based web service requests, dispatching the web
 * service request to its intended destination, packaging the SOAP response, and
 * sending the SOAP response to the intended caller.
 * <p>
 * <b><u>Configuration notes</u></b><br>
 * <ul>
 * <li>It is highly important that the concrete class implements method,
 * {@link RMT2SoapEngine#getMessageRoutingInfo(SOAPMessage, Object)
 * getMessageRoutingInfo} so that web service routing information is properly
 * obtained for the SOAP message.</li>
 * <li>It is <b>no longer</b> a requirement for an application to identify the
 * class name of the SOAP messaging router in the AppParms.properties. </u>
 * 
 * 
 * @author Roy TErrell
 * 
 */
public class RMT2SoapEngine extends AbstractServlet {

    private static final long serialVersionUID = 5433010431085175539L;

    private static Logger logger = Logger.getLogger(RMT2SoapEngine.class);

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
    public void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws StatelessControllerProcessingException {
        super.processRequest(request, response);
        String msg = null;
        SOAPMessage results;
        String serviceId = null;

        Request genericRequest = HttpVariableScopeFactory.createHttpRequest(request);
        Response genericResponse = HttpVariableScopeFactory.createHttpResponse(response);

        SoapMessageHelper helper = new SoapMessageHelper();

        SOAPMessage sm = null;
        try {
            // Get SOAP message instance from the Object Input Stream of the
            // request
            sm = helper.getSoapInstance(genericRequest);
            String soapXml = helper.toString(sm);

            // Extract SOAP Body
            String payloadXml = helper.getBody(sm);

            // get SOAP attachments, if applicable.
            List<DataHandler> attachments = helper.extractAttachments(sm);

            // Obtain the transaction id from the SOAP message header.
            serviceId = RMT2XmlUtility.getElementValue("transaction", soapXml);

            // Invoke the service. The consumer is required to send the response
            // as a valid SOAP String.
            results = this.invokeService(serviceId, payloadXml, attachments);

            // Return the results of the service invocation to the requestor.
            this.sendResponse(genericResponse, results);
        } catch (MessageException e) {
            msg = "Error occurred processing incoming SOAP message. " + e.getMessage();
            this.sendErrorResponse(response, msg);
            throw new StatelessControllerProcessingException(msg, e);
        }
        catch (NotFoundException e) {
            msg = "Service was not found in service registry by message id, " + serviceId + ". " + e.getMessage();
            this.sendErrorResponse(response, msg);
            throw new StatelessControllerProcessingException(msg, e);
        }
    }

    /**
     * Performs the invocation of a remote service and returns the results to
     * the caller.
     * 
     * @param messageId
     *            The message Id of the service to invoke
     * @param payload
     *            The payload as an XML String
     * @throws MessageException
     *             The routing of the message errored
     * @throws SoapEngineException
     *             The SOAP engine router is invalid or has not been
     *             initialized.
     */
    private SOAPMessage invokeService(String messageId, String payload, List<DataHandler> attachments)
            throws SoapEngineException, MessageException {
        String msg = null;
        MessageRouterHelper helper = new MessageRouterHelper();
        try {
            MessageRoutingInfo routeInfo = helper.getRoutingInfo(messageId);
            String modifiedPayload = RMT2XmlUtility.setElementValue("routing", routeInfo.getRouterType() + ": "
                    + routeInfo.getDestination(), payload);
            modifiedPayload = RMT2XmlUtility.setElementValue("delivery_mode", routeInfo.getDeliveryMode(),
                    modifiedPayload);
            modifiedPayload = RMT2XmlUtility.setElementValue("message_mode", "REQUEST", modifiedPayload);
            modifiedPayload = RMT2XmlUtility.setElementValue("delivery_date",
                    RMT2Date.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), modifiedPayload);
            return helper.routeSoapMessage(routeInfo, modifiedPayload, attachments);
        } catch (MessageRoutingException e) {
            msg = "Error occurred routing SOAP message to its designated handler";
            logger.error(msg);
            throw new MessageException(e);
        }
    }

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
    protected void sendResponse(Response response, SOAPMessage soapObj) throws SoapResponseException {
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
    protected void sendErrorResponse(HttpServletResponse response, String errorMessage) throws SystemException {
        Response genericResponse = HttpVariableScopeFactory.createHttpResponse(response);
        SoapMessageHelper helper = new SoapMessageHelper();
        errorMessage = "SOAP Engine Failure.   " + errorMessage + ".   Please contact Technical Support!";
        SOAPMessage err = helper.createSoapFault("Server", errorMessage, null, null);
        try {
            this.sendResponse(genericResponse, err);
        } catch (SoapResponseException e) {
            throw new SystemException(e);
        }
        return;
    }
}
