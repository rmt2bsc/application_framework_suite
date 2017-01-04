package com.api.messaging.webservice.router;

import java.io.Serializable;
import java.util.List;

import javax.activation.DataHandler;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.apache.log4j.Logger;

import com.InvalidDataException;
import com.NotFoundException;
import com.api.messaging.MessageException;
import com.api.messaging.MessageRoutingException;
import com.api.messaging.MessagingResourceFactory;
import com.api.messaging.handler.MessageHandlerInput;
import com.api.messaging.handler.MessageHandlerResults;
import com.api.messaging.webservice.soap.SoapConstants;
import com.api.messaging.webservice.soap.SoapMessageHelper;
import com.api.messaging.webservice.soap.SoapResponseException;
import com.api.messaging.webservice.soap.SoapServiceException;
import com.api.messaging.webservice.soap.client.SoapProductBuilder;

/**
 * A SOAP implementation of {@link MessagingRouter} providing common router
 * functionality for managing the service registry and routing a SOAP message to
 * an appropriate service handler.
 * 
 * @author Roy Terrell
 * 
 */
class MessageRouterSoapImpl extends AbstractMessageRouterImpl {
    private static final Logger logger = Logger
            .getLogger(MessageRouterSoapImpl.class);

    /**
     * Default constructor.
     */
    MessageRouterSoapImpl() {
        super();
        return;
    }

    // /**
    // * Load the service registry with data
    // * <p>
    // * This implementation is capable of utilizing two types of input sources
    // to load the data: a HTTP
    // * or a LDAP source. The descendent can override if the source should be
    // identified as something
    // * other than a HTTP service.
    // *
    // * @throws MessageRoutingException
    // */
    // public void intitalizeRegistry() throws MessageRoutingException {
    // this.intitalizeRegistryFromFileSource(WebServiceConstants.WEBSERV_TYPE_SOAP);
    //
    // //
    // this.intitalizeRegistryFromHttpSource(WebServiceConstants.WEBSERV_TYPE_SOAP);
    // //
    // this.intitalizeRegistryFromLdapSource(WebServiceConstants.WEBSERV_TYPE_SOAP);
    // }

    private Object bindPayload(SOAPMessage message) {
        Object payloadBinding;
        try {
            payloadBinding = this.getPayloadAsJaxbObject(message);
            return payloadBinding;
        } catch (MessageRoutingException e) {
            return null;
        }
    }

    /**
     * 
     * @param sm
     * @return
     * @throws MessageRoutingException
     */
    private Object getPayloadAsJaxbObject(SOAPMessage sm)
            throws MessageRoutingException {
        SoapMessageHelper helper = new SoapMessageHelper();
        String bodyXml;
        Object bodyObj;
        try {
            bodyXml = helper.getBody(sm);
            // TODO: might need to change this invocation to create binder with
            // one class instead of the entire package in order to be
            // performant.
            bodyObj = MessagingResourceFactory.getJaxbMessageBinder()
                    .unMarshalMessage(bodyXml);
            return bodyObj;
        } catch (SoapServiceException e) {
            this.msg = "Unable to obtain payload as a String from the SOAP envelope instance";
            throw new MessageRoutingException(this.msg, e);
        } catch (Exception e) {
            this.msg = "Error occurred trying to unmarshall the payload of the request SOAP envelope as a JAXB object";
            throw new MessageRoutingException(this.msg, e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.api.messaging.MessagingRouter#createHandlerRequestData(java.lang.
     * Object)
     */
    @Override
    protected MessageHandlerInput createReceptorInputData(Object inMessage)
            throws MessageRoutingException {
        SOAPMessage sm = null;
        if (inMessage instanceof SOAPMessage) {
            sm = (SOAPMessage) inMessage;
        }
        else {
            this.msg = "Unable to create messaging handler data object.  Incoming generic message data type must resolve to SOAPMessage";
            throw new RuntimeException(this.msg);
        }

        SoapMessageHelper helper = new SoapMessageHelper();

        // Get message id and command name
        String msgId = null;
        String command = null;
        String hdrElement = null;
        try {
            hdrElement = SoapProductBuilder.HEADER_NS + ":"
                    + SoapConstants.SERVICEID_NAME;
            msgId = helper.getHeaderValue(hdrElement, sm);
        } catch (SOAPException e) {
            this.msg = "A problem occurred accessing SOAP header element, "
                    + hdrElement;
            throw new MessageRoutingException(this.msg, e);
        } catch (NotFoundException e) {
            this.msg = "Required SOAP header element, " + hdrElement
                    + " was not found as part of the SOAP header element";
            throw new MessageRoutingException(this.msg, e);
        }

        try {
            hdrElement = SoapProductBuilder.HEADER_NS + ":"
                    + SoapConstants.COMMAND_NAME;
            command = helper.getHeaderValue(hdrElement, sm);
        } catch (SOAPException e) {
            this.msg = "A problem occurred accessing SOAP header element, "
                    + hdrElement;
            throw new MessageRoutingException(this.msg, e);
        } catch (NotFoundException e) {
            this.msg = "Optional SOAP header element, " + hdrElement
                    + " was not found as part of the SOAP header element";
            logger.warn(this.msg);
        }

        // Get payload...required to be serializable
        Object boundData = this.bindPayload(sm);
        Serializable payload;
        if (boundData != null && boundData instanceof Serializable) {
            payload = (Serializable) boundData;
        }
        else {
            this.msg = "The payload of incoming message is required to be of Serializable type";
            throw new MessageRoutingException(this.msg);
        }

        // get SOAP attachments, if applicable.
        List<DataHandler> attachments = helper.extractAttachments(sm);

        // Create and initialize message handle input object
        MessageHandlerInput data = new MessageHandlerInput();
        data.setMessageId(msgId);
        data.setCommand(command);
        data.setPayload(payload);
        data.setAttachments(attachments);
        return data;
    }

    /**
     * Extended this method in order to build an instance of the SOAP response
     * message envelope from the messaging handler's results, <i>results</i>.
     * <p>
     * This method undergoes the following process to create the SOAP message:
     * <ol>
     * <li>Extracts the response data from <i>results.</i></li>
     * <li>Marshals the payload in the form of XML.</li>
     * <li>Creates an instance of {@link SOAPMessage}.</li>
     * <li>Returns the instance to the client.</li>
     * </ol>
     * 
     * @param serviceId
     *            The response service id.
     * @param results
     *            An instance of {@link MessageHandlerResults} containing the
     *            results of the service handler processing.
     * @return The response message as an XML String.
     * @throws InvalidDataException
     * @throws MessageRoutingException
     */
    @Override
    protected Object getReceptorResults(MessageHandlerResults results)
            throws InvalidDataException, MessageRoutingException {
        SOAPMessage sm = null;
        SoapMessageHelper helper = new SoapMessageHelper();

        // Build SOAP response instance.
        try {
            if (results.getReturnCode() == SoapConstants.RETURNCODE_SUCCESS) {
                // The business API hanlder processed the request successfully.
                String payload = (String) super.getReceptorResults(results);
                String xml = helper.createResponse(results.getMessageId(),
                        payload);
                if (results.getAttachments() != null
                        && results.getAttachments().size() > 0) {
                    // Attachments were found...build SOAP message with
                    // attachments.
                    sm = helper.getSoapInstance(xml, results.getAttachments());
                }
                else {
                    // Build SOAP message without attachments.
                    sm = helper.getSoapInstance(xml);
                }
            }
            else if (results.getReturnCode() == SoapConstants.RETURNCODE_FAILURE) {
                // Create SOAP fault message since the business API handler
                // returned an error.
                sm = helper.createSoapFault(
                        String.valueOf(SoapConstants.RETURNCODE_FAILURE),
                        results.getErrorMsg(), null, null);
            }
            else {
                // The required return code was not set by the business API
                // handler.
                this.msg = "Unable to process the SOAP response.  The business service API handler must return an instance of MessageHandlerResults with the return code property set to 1 or -1.";
                throw new InvalidDataException(this.msg);
            }
            return sm;
        } catch (SoapResponseException e) {
            this.msg = "Error occurred creating XML String during the process of packaging SOAP response via the SOAP router";
            throw new MessageRoutingException(this.msg, e);
        } catch (MessageException e) {
            this.msg = "Error occurred creating SOAP envelope instance during the process of packaging the SOAP response via the SOAP router";
            throw new MessageRoutingException(this.msg, e);
        }
    }

}
