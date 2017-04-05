package com.api.messaging.webservice.router;

import java.io.Serializable;

import org.apache.log4j.Logger;

import com.api.messaging.handler.MessageHandlerInput;

/**
 * A basic implementation of {@link AbstractMessageRouterImpl} that uses an
 * instance of {@link MessageRoutingInfo} as the source of data.
 * 
 * @author Roy Terrell
 * 
 */
class BasicMessageRouterImpl extends AbstractMessageRouterImpl {

    private static final Logger logger = Logger.getLogger(BasicMessageRouterImpl.class);

    /**
     * Default constructor.
     */
    BasicMessageRouterImpl() {
        super();
        return;
    }

    /**
     * Create a MessageHandlerInput instance from an MessageRoutingInfo object.
     * 
     * @param srvc
     *            the routing information pertaining to the web service message
     * @param inMessage
     *            an arbitrary object disguised as an instance of
     *            {@link MessageRoutingInfo}.
     * @return an instance of {@link MessageHandlerInput}
     * @throws MessageRoutingException
     *             Routing information is unobtainable due to the occurrence of
     *             data access errors or etc.
     */
    @Override
    protected MessageHandlerInput prepareMessageForTransport(MessageRoutingInfo srvc, Object inMessage)
            throws MessageRoutingException {
        MessageHandlerInput handlerInputData = new MessageHandlerInput();
        handlerInputData.setMessageId(srvc.getMessageId());
        handlerInputData.setDeliveryMode(srvc.getDeliveryMode());
        handlerInputData.setCommand(null);
        if (inMessage instanceof Serializable) {
            handlerInputData.setPayload((Serializable) inMessage);
        }
        else {
            handlerInputData.setPayload(null);
        }
        handlerInputData.setAttachments(null);
        return handlerInputData;
    }
}
