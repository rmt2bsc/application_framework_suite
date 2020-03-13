package com.api.messaging.webservice.router;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.activation.DataHandler;
import javax.xml.soap.SOAPMessage;

import org.apache.log4j.Logger;

import com.InvalidDataException;
import com.NotFoundException;
import com.RMT2Base;
import com.api.config.ConfigConstants;
import com.api.config.SystemConfigurator;
import com.api.messaging.MessageException;
import com.api.messaging.handler.MessageHandlerResults;
import com.api.messaging.webservice.ServiceRegistry;
import com.api.messaging.webservice.ServiceRegistryFactoryImpl;
import com.api.messaging.webservice.soap.SoapConstants;
import com.api.messaging.webservice.soap.SoapMessageHelper;
import com.api.messaging.webservice.soap.SoapResponseException;
import com.api.messaging.webservice.soap.client.SoapBuilderException;
import com.api.util.RMT2Date;
import com.api.xml.RMT2XmlUtility;
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

    protected ServiceRegistry register;

    /**
     * Create a MessageRouterHelper object which initializes the Service
     * Registry.
     */
    public MessageRouterHelper() {
        super();
        this.register = null;
        try {
            this.intitalizeRegistry();
        } catch (MessageRoutingException e) {
            this.msg = "Unable to initialize web service router due to error creating SERVICES registry";
            logger.error(this.msg, e);
        }
        return;
    }

    /**
     * Load the service registry with data by dynamically determining the
     * <i>ServiceRegistry</i> implementation to use based on its declaration
     * found in <i>AppParms.properties</i>.
     * <p>
     * This implementation is capable of utilizing different types of input
     * sources to load the data such as HTTP or a LDAP source. The descendent
     * can override if the source should be identified as something other than a
     * HTTP service.
     * 
     * @throws MessageRoutingException
     */
    protected void intitalizeRegistry() throws MessageRoutingException {
        // Get SystemConfigurator Service Registry implementation
        ServiceRegistryFactoryImpl f = new ServiceRegistryFactoryImpl();
        this.register = f.getSystemConfiguratorServiceRegistryManager();

        // Load the all service configurations
        this.register.loadServices();
    }

    /**
     * Obtain routing information for the target message id.
     * 
     * @param messageId
     *            the message id to use to obtain message routing information.
     * @return an instance of {@link com.api.messaging.MessageRoutingInfo
     *         MessageRoutingInfo}
     * @throws MessageRoutingException
     *             Routing information is unobtainable due to the occurrence of
     *             data access errors or etc.
     * @throws InvalidDataException
     *             <i>messageId</i> is null or the routing information obtained
     *             does not contain a URL.
     * @throws NotFoundException
     *             Routing information is not found in the service registry
     *             using the supplied key, <i>messageId</i>.
     */
    public MessageRoutingInfo getRoutingInfo(String messageId)
            throws InvalidDataException, NotFoundException, MessageRoutingException {
        if (this.register == null) {
            msg = "Unable to get message routing information due to the web service registry is not initialize";
            logger.error(msg);
            throw new MessageRoutingException(msg);
        }
        // Validate the existence of Service Id
        if (messageId == null) {
            msg = "Unable to get message routing information due to required message id is null";
            logger.error(msg);
            throw new InvalidDataException(msg);
        }
        MessageRoutingInfo srvc = this.register.getEntry(messageId);
        if (srvc == null) {
            msg = "Routing information was not found in the web service registry for message id,  " + messageId;
            logger.error(msg);
            throw new NotFoundException(msg);
        }
        if (!messageId.equalsIgnoreCase(srvc.getMessageId())) {
            msg = "A naming conflict exist between the requested message id [" + messageId
                    + "] and the service name associated with the web service registry entry that was found";
            logger.error(msg);
            throw new NotFoundException(msg);
        }
        if (srvc.getDestination() == null) {
            msg = "The required URL property of the matching message routing entry found in the web service registry for message id, "
                    + messageId + ", is null";
            logger.error(msg);
            throw new InvalidDataException(msg);
        }
        return srvc;
    }

    /**
     * Route a SOAP message to its appropriate destination.
     * 
     * @param messageId
     *            the unique identification of the SOAP message
     * @param payload
     *            the data to be sent
     * @param attachments
     *            SOAP attachments
     * @return instance of {@link SOAPMessage}
     * @throws MessageRoutingException
     *             Unable to access routing information or problems routing the
     *             message to its destination.
     * @throws InvalidDataException
     *             messageId is null or the routing information obtained does
     *             not contain a URL.
     * @throws NotFoundException
     *             Routing information is not found in the service registry
     *             using the supplied key,
     */
    public SOAPMessage routeSoapMessage(String messageId, String payload, List<DataHandler> attachments)
            throws MessageRoutingException {
        
        // Get routing information
        MessageRoutingInfo routeInfo = this.getRoutingInfo(messageId);

        // Route the message
        return this.routeSoapMessage(routeInfo, payload, attachments);
    }
    
    
    
    /**
     * Route a SOAP message to its appropriate destination.
     * 
     * @param routeInfo
     *            An instance of {@link MessageRoutingInfo}
     * @param payload
     *            XML payload
     * @param attachments
     *            the SOAP attachments
     * @return an instance of {@link SOAPMessage} as the reply
     * @throws MessageRoutingException
     */
    private SOAPMessage routeSoapMessage(MessageRoutingInfo routeInfo, String payload, List<DataHandler> attachments)
            throws MessageRoutingException {
        MessagingRouter router = MessageRouterFactory.createSoapMessageRouter();
        
        // Setup header informtation in the payload
        String modifiedPayload = this.setPayloadHeaderValues(routeInfo, payload);
        
        try {
            // Output request to logger
            logger.info("Routing SOAP Request: ");
            logger.info(modifiedPayload);
            MessageHandlerResults results = ((SoapMessageRouterImpl) router).routeMessage(routeInfo, modifiedPayload, attachments);
            // Convert results to SOAP Message
            SOAPMessage soapResponse = this.createSoapResponse(results);
            SoapMessageHelper helper = new SoapMessageHelper();

            // Output response to logger
            String resultsSoapStr = helper.toString(soapResponse);
            logger.info("Received SOAP Response: ");
            logger.info(resultsSoapStr);
            return soapResponse;
        } catch (SoapBuilderException e) {
            this.msg = "An error occurred translating SOAP instance to String";
            throw new MessageRoutingException(this.msg, e);
        }
    }



    /**
     * Route a JSON message to its appropriate destination.
     * 
     * @param messageId
     *            the unique identification of the JSON message
     * @param payload
     *            The payload is a Serializable object that can be
     *            marshalled/unmarshalled as JSON
     * @return Serializable object as the reply which is expected to be an
     *         object that can be marshalled/unmarshalled via JAXB.
     * @throws MessageRoutingException
     *             Error converting <i>Payload</i> to JSON or the inability to
     *             route the message to its destination.
     */
    public Object routeJsonMessage(String messageId, Serializable payload) throws MessageRoutingException {
        // Get routing information
        MessageRoutingInfo routeInfo = this.getRoutingInfo(messageId);
        
        // Try to marshal request payload as JSON and dump contents to logger
        final Gson gson = new GsonBuilder().create();
        String jsonReq = gson.toJson(payload);
        logger.info("Routing Request: ");
        logger.info(jsonReq);

        // Convert JSON to XML which is to be routed to the destination
        String xml = this.convertPayloadToXml(payload);
        
        // Setup header informtation in the payload
        String modifiedPayload = this.setPayloadHeaderValues(routeInfo, xml);

        // Route message to business server
        Serializable jaxPayload = this.routeMessage(routeInfo, modifiedPayload);

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
     *            the unique identification of the XML message
     * @param payload
     *            The payload is a Serializable object that can be
     *            marshalled/unmarshalled as XML
     * @return Serializable object as the reply which is expected to be an
     *         object that can be marshalled/unmarshalled via JAXB.
     * @throws MessageRoutingException
     */
    public Object routeXmlMessage(String messageId, Serializable payload) throws MessageRoutingException {
        // Get routing information
        MessageRoutingInfo routeInfo = this.getRoutingInfo(messageId);
        
        String xml = this.convertPayloadToXml(payload);
        logger.info("Routing Request: ");
        logger.info(xml);
        
        // Setup header informtation in the payload
        String modifiedPayload = this.setPayloadHeaderValues(routeInfo, xml);

        // Route message to business server
        Serializable jaxPayload = this.routeMessage(routeInfo, modifiedPayload);

        // Try marshall response payload as XML and dump contents to logger
        try {
            xml = this.convertPayloadToXml(jaxPayload);
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
     * @param routeInfo
     *            An instance of {@link MessageRoutingInfo}
     * @param payload
     *            The message payload
     * @return Serializable object as the reply
     * @throws MessageRoutingException
     */
    protected Serializable routeMessage(MessageRoutingInfo routeInfo, Serializable payload)
            throws MessageRoutingException {
        MessagingRouter router = null;
        try {
            router = MessageRouterFactory.createBasicMessageRouter();
        } catch (Exception e) {
            this.msg = "Error creating Messaging Router instance";
            logger.error(this.msg, e);
            throw new MessageRoutingException(this.msg, e);
        }
        // Route message to business server
        Serializable results = (Serializable) router.routeMessage(routeInfo, payload);
        Serializable jaxbPayload = null;
        if (results instanceof MessageHandlerResults) {
            jaxbPayload = ((MessageHandlerResults) results).getPayload();
        }
        return jaxbPayload;
    }

    private String setPayloadHeaderValues(MessageRoutingInfo routeInfo, String payload) {
        // Setup header informtation in the payload
        String modifiedPayload = RMT2XmlUtility.setElementValue("routing", routeInfo.getRouterType() + ": "
                + routeInfo.getDestination(), payload);
        modifiedPayload = RMT2XmlUtility.setElementValue("delivery_mode", routeInfo.getDeliveryMode(),
                modifiedPayload);
        modifiedPayload = RMT2XmlUtility.setElementValue("message_mode", "REQUEST", modifiedPayload);
        modifiedPayload = RMT2XmlUtility.setElementValue("delivery_date",
                RMT2Date.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), modifiedPayload);
        return modifiedPayload;
    }
    
    private String convertPayloadToXml(Serializable payload) {
        // Convert JSON to XML which is to be routed to the destination
        try {
            JaxbUtil util = SystemConfigurator.getJaxb(ConfigConstants.JAXB_CONTEXNAME_DEFAULT);
            String xml = util.marshalMessage(payload);
            return xml;
        } catch (Exception e) {
            throw new MessageRoutingException("Unable to convert payload to XML String");
        }
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
    private SOAPMessage createSoapResponse(MessageHandlerResults results)
            throws InvalidDataException, MessageRoutingException {
        SOAPMessage sm = null;
        SoapMessageHelper helper = new SoapMessageHelper();

        // Marshall payload
        String bodyXml = null;
        try {
            JaxbUtil jaxbUtil = SystemConfigurator.getJaxb(ConfigConstants.JAXB_CONTEXNAME_DEFAULT);
            bodyXml = jaxbUtil.marshalMessage(results.getPayload());
        } catch (Exception e) {
            this.msg = "Error occurred trying to marshall the payload of the SOAP response";
            throw new MessageRoutingException(this.msg, e);
        }

        // Build SOAP response instance.
        try {
            if (results.getReturnCode() == SoapConstants.RETURNCODE_SUCCESS) {
                // The business API hanlder processed the request successfully.
                String soapXml = helper.createResponse(results.getMessageId(), bodyXml);
                
                // Add attachments if available
                if (results.getAttachments() != null && results.getAttachments().size() > 0) {
                    // Attachments were found...build SOAP message with
                    // attachments.
                    sm = helper.getSoapInstance(soapXml, results.getAttachments());
                }
                else {
                    // Build SOAP message without attachments.
                    sm = helper.getSoapInstance(soapXml);
                }
            }
            else if (results.getReturnCode() == SoapConstants.RETURNCODE_FAILURE) {
                // Create SOAP fault message since the business API handler
                // returned an error.
                sm = helper.createSoapFault(String.valueOf(SoapConstants.RETURNCODE_FAILURE), results.getErrorMsg(),
                        null, null);
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
