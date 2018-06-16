package com.api.messaging.webservice.router;

import java.util.List;

import javax.activation.DataHandler;

import org.apache.log4j.Logger;

import com.api.messaging.handler.MessageHandlerInput;
import com.api.messaging.handler.MessageHandlerResults;

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
     * @param routeInfo
     *            An instance of {@link MessageRoutingInfo}
     * @param message
     *            an arbitrary message that is to be processed
     * @return an instance of {@link MessageHandlerResults}
     * @throws MessageRoutingException
     */
    public MessageHandlerResults routeMessage(MessageRoutingInfo routeInfo, String soapXml, List<DataHandler> attachments)
            throws MessageRoutingException {
        this.attachments = attachments;
        return super.routeMessage(routeInfo, soapXml);
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
        // Create and initialize message handle input object
        MessageHandlerInput data = new MessageHandlerInput();
        data.setMessageId(srvc.getMessageId());
        data.setCommand(null);
        data.setPayload(bodyXml);
        data.setAttachments(this.attachments);
        return data;
    }
}
