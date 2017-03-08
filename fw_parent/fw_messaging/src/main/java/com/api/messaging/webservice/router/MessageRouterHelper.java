package com.api.messaging.webservice.router;

import java.io.Serializable;

import javax.xml.soap.SOAPMessage;

import org.apache.log4j.Logger;

import com.RMT2Base;
import com.api.config.ConfigConstants;
import com.api.config.SystemConfigurator;
import com.api.messaging.MessageRoutingException;
import com.api.messaging.MessagingRouter;
import com.api.messaging.handler.MessageHandlerResults;
import com.api.messaging.webservice.soap.SoapMessageHelper;
import com.api.messaging.webservice.soap.client.SoapBuilderException;
import com.api.xml.jaxb.JaxbUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Helper class for routing various types of messages to their respective
 * destinations via either a RMI, HTTP, or JMS transport.
 * 
 * @author Roy Terrell
 * 
 */
public class MessageRouterHelper extends RMT2Base {

    private static final Logger logger = Logger.getLogger(MessageRouterHelper.class);

    /**
     * Create a MessageRouterHelper object
     */
    public MessageRouterHelper() {
        super();
    }

    /**
     * Route a SOAP message to its appropriate destination.
     * 
     * @param messageId
     *            The message id of the service that requires the SOAP message
     *            to be routed to its destination.
     * @param payload
     *            an instance of {@link SOAPMessage}
     * @return an instance of {@link SOAPMessage} as the reply
     * @throws MessageRoutingException
     */
    public SOAPMessage routeSoapMessage(String messageId, SOAPMessage payload) throws MessageRoutingException {
        MessagingRouter router = MessageRouterFactory.createSoapMessageRouter();
        try {
            SoapMessageHelper helper = new SoapMessageHelper();
            String soapStr = helper.toString(payload);
            logger.info("Routing SOAP Request: ");
            logger.info(soapStr);
            SOAPMessage results = (SOAPMessage) router.routeMessage(messageId, payload);
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
     * Route a JSON message to its appropriate destination.
     * 
     * @param messageId
     *            The message id of the service to route payload.
     * @param payload
     *            The payload is a Serializable object that can be
     *            marshalled/unmarshalled as JSON
     * @return Serializable object as the reply which is expected to be an
     *         object that can be marshalled/unmarshalled via JAXB.
     * @throws MessageRoutingException
     */
    public Object routeJsonMessage(String messageId, Serializable payload) throws MessageRoutingException {
        // Try to marshal request payload as JSON and dump contents to logger
        final Gson gson = new GsonBuilder().create();
        String jsonReq = gson.toJson(payload);
        logger.info("Routing Request: ");
        logger.info(jsonReq);

        // Route message to business server
        Serializable jaxPayload = this.sendPayload(messageId, payload);

        // Try to marshal response payload as JSON and dump contents to logger
        String jsonResp = gson.toJson(jaxPayload);
        logger.info("Routing Response: ");
        logger.info(jsonResp);

        return jaxPayload;
    }

    /**
     * Route a XML message object to its appropriate destination.
     * 
     * @param messageId
     *            The message id of the service to route payload.
     * @param payload
     *            The payload is a Serializable object that can be
     *            marshalled/unmarshalled as XML
     * @return Serializable object as the reply which is expected to be an
     *         object that can be marshalled/unmarshalled via JAXB.
     * @throws MessageRoutingException
     */
    public Object routeXmlMessage(String messageId, Serializable payload) throws MessageRoutingException {
        JaxbUtil util = SystemConfigurator.getJaxb(ConfigConstants.JAXB_CONTEXNAME_DEFAULT);
        String xml = null;
        // Try to marshal request payload as XML and dump contents to logger
        try {
            xml = util.marshalMessage(payload);
            logger.info("Routing Response: ");
            logger.info(xml);
        } catch (Exception e) {
            // Do nothing...not a JAXB object
        }

        // Route message to business server
        Serializable jaxPayload = this.sendPayload(messageId, payload);

        // Try marshall response payload as XML and dump contents to logger
        try {
            xml = util.marshalMessage(jaxPayload);
            logger.info("Routing Response: ");
            logger.info(xml);
        } catch (Exception e) {
            // Do nothing...not a JAXB object
        }
        return jaxPayload;
    }

    /**
     * Sends a generic message payload to it's appropriate destination.
     * <p>
     * The payload is expcected to be in the form of a serializable object that
     * can be marshalled and unmarshalled via JAXB implementation.
     * 
     * @param messageId
     *            The message id of the service to route payload.
     * @param payload
     *            The message payload
     * @return Serializable object as the reply
     * @throws MessageRoutingException
     */
    protected Serializable sendPayload(String messageId, Serializable payload) throws MessageRoutingException {
        MessagingRouter router = null;
        try {
            router = MessageRouterFactory.createJaxbMessageRouter();
        } catch (Exception e) {
            this.msg = "Error creating Messaging Router instance";
            logger.error(this.msg, e);
            throw new MessageRoutingException(this.msg, e);
        }
        // Route message to business server
        Serializable results = (Serializable) router.routeMessage(messageId, payload);
        Serializable jaxbPayload = null;
        if (results instanceof MessageHandlerResults) {
            jaxbPayload = ((MessageHandlerResults) results).getPayload();
        }
        return jaxbPayload;
    }
}
