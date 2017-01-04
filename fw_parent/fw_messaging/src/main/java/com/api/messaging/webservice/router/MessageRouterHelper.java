package com.api.messaging.webservice.router;

import java.io.Serializable;

import javax.xml.soap.SOAPMessage;

import org.apache.log4j.Logger;

import com.RMT2Base;
import com.api.messaging.MessageRoutingException;
import com.api.messaging.MessagingResourceFactory;
import com.api.messaging.MessagingRouter;
import com.api.messaging.handler.MessageHandlerResults;
import com.api.messaging.webservice.soap.SoapMessageHelper;
import com.api.messaging.webservice.soap.client.SoapBuilderException;

/**
 * Helper class for routing various types of messages from one API to another.
 * 
 * @author Roy Terrell
 * 
 */
public class MessageRouterHelper extends RMT2Base {

    private static final Logger logger = Logger
            .getLogger(MessageRouterHelper.class);

    /**
     * Create a MessageRouterHelper object
     */
    public MessageRouterHelper() {
        super();
    }

    /**
     * Route a SOAP message to its appropriate destination via either a RMI,
     * HTTP, or JMS transport.
     * 
     * @param messageId
     *            The message id of the service that requires the SOAP message
     *            to be routed to its destination.
     * @param payload
     *            an instance of {@link SOAPMessage}
     * @return an instance of {@link SOAPMessage} as the reply
     * @throws MessageRoutingException
     */
    public SOAPMessage routeSoapMessage(String messageId, SOAPMessage payload)
            throws MessageRoutingException {
        MessagingRouter router = MessageRouterFactory.createSoapMessageRouter();
        try {
            SoapMessageHelper helper = new SoapMessageHelper();
            String soapStr = helper.toString(payload);
            logger.info("Routing SOAP Request: ");
            logger.info(soapStr);
            SOAPMessage results = (SOAPMessage) router.routeMessage(messageId,
                    payload);
            soapStr = helper.toString(results);
            logger.info("Received SOAP Response: ");
            logger.info(soapStr);
            return results;
        } catch (SoapBuilderException e) {
            this.msg = "An error occurred translating SOAP instance to String";
            throw new MessageRoutingException(this.msg, e);
        }
    }

    /**
     * Route a general message object to its appropriate destination via either
     * a RMI, HTTP, or JMS transport.
     * 
     * @param messageId
     *            The message id of the service to route payload.
     * @param payload
     *            The payload is a Serializable object
     * @return Serializable object as the reply
     * @throws MessageRoutingException
     */
    public Object routeSerialMessage(String messageId, Serializable payload)
            throws MessageRoutingException {
        MessagingRouter router = MessageRouterFactory.createJaxbMessageRouter();
        String xml = null;
        // // Try to marshal request payload as XML and dump contents to logger
        // // in the event it is a JAXB object.
        // try {
        // xml = MessagingResourceFactory.getJaxbMessageBinder(payload)
        // .marshalMessage(payload);
        // logger.info("Routing Request: ");
        // logger.info(xml);
        // } catch (Exception e) {
        // // Do nothing...not a JAXB object
        // }
        Serializable results = (Serializable) router.routeMessage(messageId,
                payload);
        Serializable jaxPayload = null;
        if (results instanceof MessageHandlerResults) {
            jaxPayload = ((MessageHandlerResults) results).getPayload();
        }
        // Try to marshal response payload as XML and dump contents to logger
        // in the event it is a JAXB object.
        try {
            xml = MessagingResourceFactory.getJaxbMessageBinder()
                    .marshalMessage(jaxPayload);
            logger.info("Routing Response: ");
            logger.info(xml);
        } catch (Exception e) {
            // Do nothing...not a JAXB object
        }
        return jaxPayload;
    }

}
