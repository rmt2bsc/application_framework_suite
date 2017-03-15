package com.api.messaging.webservice.router;

import java.io.Serializable;
import java.util.List;

import javax.activation.DataHandler;

import org.apache.log4j.Logger;

import com.api.config.ConfigConstants;
import com.api.config.SystemConfigurator;
import com.api.messaging.MessageRoutingException;
import com.api.messaging.MessageRoutingInfo;
import com.api.messaging.handler.MessageHandlerInput;
import com.api.messaging.handler.MessageHandlerResults;
import com.api.xml.jaxb.JaxbUtil;

/**
 * A SOAP implementation of {@link MessagingRouter} providing common router
 * functionality for managing the service registry and routing a SOAP message to
 * an appropriate service handler.
 * 
 * @author Roy Terrell
 * 
 */
class SoapMessageRouterImpl extends AbstractMessageRouterImpl {
    private static final Logger logger = Logger.getLogger(SoapMessageRouterImpl.class);
    // get SOAP attachments, if applicable.
    private List<DataHandler> attachments;
    /**
     * Default constructor.
     */
    SoapMessageRouterImpl() {
        super();
        return;
    }

    /**
     * Routes an incoming web service message to its targeted destination.
     * <p>
     * This method will use <i>messageId</i> to obtain the message routing
     * information.
     * 
     * @param messageId
     *            the identifier of the message to route.
     * @param message
     *            an arbitrary message that is to be processed
     * @return an instance of {@link MessageHandlerResults}
     * @throws MessageRoutingException
     */
    public MessageHandlerResults routeMessage(String messageId, String soapXml, List<DataHandler> attachments)
            throws MessageRoutingException {
        this.attachments = attachments;
        return super.routeMessage(messageId, soapXml);
    }


    /*
     * (non-Javadoc)
     * 
     * @see
     * com.api.messaging.MessagingRouter#createHandlerRequestData(java.lang.
     * Object)
     */
    @Override
    protected MessageHandlerInput prepareMessageForTransport(MessageRoutingInfo srvc, Object inMessage)
            throws MessageRoutingException {
        // Get payload...required to be serializable
        String bodyXml = null;
        if (inMessage instanceof String) {
            bodyXml = (String) inMessage;
        }
        else {
            this.msg = "Unable to create messaging handler data object.  Incoming generic message data type must resolve to SOAPMessage";
            throw new MessageRoutingException(this.msg);
        }

        Object bodyObj = null;
        try {
            JaxbUtil jaxbUtil = SystemConfigurator.getJaxb(ConfigConstants.JAXB_CONTEXNAME_DEFAULT);
            bodyObj = jaxbUtil.unMarshalMessage(bodyXml);
        } catch (Exception e) {
            this.msg = "Error occurred trying to unmarshall the payload of the request SOAP envelope as a JAXB object";
            throw new MessageRoutingException(this.msg, e);
        }

        Serializable payload;
        if (bodyObj != null && bodyObj instanceof Serializable) {
            payload = (Serializable) bodyObj;
        }
        else {
            this.msg = "The payload of incoming message is required to be of Serializable type";
            throw new MessageRoutingException(this.msg);
        }
        // Create and initialize message handle input object
        MessageHandlerInput data = new MessageHandlerInput();
        data.setMessageId(srvc.getMessageId());
        data.setCommand(null);
        data.setPayload(payload);
        data.setAttachments(this.attachments);
        return data;
    }

    // /**
    // * Extended this method in order to build an instance of the SOAP response
    // * message envelope from the messaging handler's results, <i>results</i>.
    // * <p>
    // * This method undergoes the following process to create the SOAP message:
    // * <ol>
    // * <li>Extracts the response data from <i>results.</i></li>
    // * <li>Marshals the payload in the form of XML.</li>
    // * <li>Creates an instance of {@link SOAPMessage}.</li>
    // * <li>Returns the instance to the client.</li>
    // * </ol>
    // *
    // * @param serviceId
    // * The response service id.
    // * @param results
    // * An instance of {@link MessageHandlerResults} containing the
    // * results of the service handler processing.
    // * @return The response message as an XML String.
    // * @throws InvalidDataException
    // * @throws MessageRoutingException
    // */
    // @Override
    // protected Object getReceptorResults(MessageHandlerResults results)
    // throws InvalidDataException, MessageRoutingException {
    // SOAPMessage sm = null;
    // SoapMessageHelper helper = new SoapMessageHelper();
    //
    // // Build SOAP response instance.
    // try {
    // if (results.getReturnCode() == SoapConstants.RETURNCODE_SUCCESS) {
    // // The business API hanlder processed the request successfully.
    // String payload = (String) super.getReceptorResults(results);
    // String xml = helper.createResponse(results.getMessageId(), payload);
    // if (results.getAttachments() != null && results.getAttachments().size() >
    // 0) {
    // // Attachments were found...build SOAP message with
    // // attachments.
    // sm = helper.getSoapInstance(xml, results.getAttachments());
    // }
    // else {
    // // Build SOAP message without attachments.
    // sm = helper.getSoapInstance(xml);
    // }
    // }
    // else if (results.getReturnCode() == SoapConstants.RETURNCODE_FAILURE) {
    // // Create SOAP fault message since the business API handler
    // // returned an error.
    // sm =
    // helper.createSoapFault(String.valueOf(SoapConstants.RETURNCODE_FAILURE),
    // results.getErrorMsg(),
    // null, null);
    // }
    // else {
    // // The required return code was not set by the business API
    // // handler.
    // this.msg = "Unable to process the SOAP response. The business service API
    // handler must return an instance of MessageHandlerResults with the return
    // code property set to 1 or -1.";
    // throw new InvalidDataException(this.msg);
    // }
    // return sm;
    // } catch (SoapResponseException e) {
    // this.msg = "Error occurred creating XML String during the process of
    // packaging SOAP response via the SOAP router";
    // throw new MessageRoutingException(this.msg, e);
    // } catch (MessageException e) {
    // this.msg = "Error occurred creating SOAP envelope instance during the
    // process of packaging the SOAP response via the SOAP router";
    // throw new MessageRoutingException(this.msg, e);
    // }
    // }

}
