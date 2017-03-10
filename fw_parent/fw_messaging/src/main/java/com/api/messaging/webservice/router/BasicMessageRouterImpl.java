package com.api.messaging.webservice.router;

import org.apache.log4j.Logger;

import com.InvalidDataException;
import com.api.messaging.MessageRoutingException;
import com.api.messaging.MessageRoutingInfo;
import com.api.messaging.handler.MessageHandlerInput;
import com.api.messaging.handler.MessageHandlerResults;

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
     * @param data
     *            an arbitrary object disguised as an instance of
     *            {@link MessageRoutingInfo}.
     * @return an instance of {@link MessageHandlerInput}
     * @throws MessageRoutingException
     *             Routing information is unobtainable due to the occurrence of
     *             data access errors or etc.
     */
    @Override
    protected MessageHandlerInput createReceptorInputData(Object data) throws MessageRoutingException {
        // Create and initialize message handle input object
        MessageRoutingInfo routeInfo = null;
        if (data != null && data instanceof MessageRoutingInfo) {
            routeInfo = (MessageRoutingInfo) data;
        }
        else {
            return null;
        }
        MessageHandlerInput handlerInputData = new MessageHandlerInput();
        handlerInputData.setMessageId(routeInfo.getMessageId());
        handlerInputData.setDeliveryMode(routeInfo.getDeliveryMode());
        handlerInputData.setCommand(null);
        handlerInputData.setPayload(null);
        handlerInputData.setAttachments(null);
        return handlerInputData;

        // Serializable serialObj = null;
        // if (inMessage instanceof Serializable) {
        // serialObj = (Serializable) inMessage;
        // }
        // // Get message id
        // String msgId = null;
        // String deliveryMode = null;
        // String xml;
        // try {
        // JaxbUtil jaxb =
        // SystemConfigurator.getJaxb(ConfigConstants.JAXB_CONTEXNAME_DEFAULT);
        // xml = jaxb.marshalMessage(serialObj);
        //
        // DaoApi api = XmlApiFactory.createXmlDao(xml);
        // String query = "//header";
        // api.retrieve(query);
        // while (api.nextRow()) {
        // try {
        // msgId = api.getColumnValue("transaction");
        // } catch (NotFoundException e) {
        // this.msg = "Cannot find required header element, message_id, in XML
        // payload...invalid XML structure";
        // throw new InvalidDataException(this.msg, e);
        // }
        // try {
        // deliveryMode = api.getColumnValue("delivery_mode");
        // } catch (NotFoundException e) {
        // this.msg = "Cannot find header element, delivery_mode. Will default
        // to ASYNCHRONOUS.";
        // logger.warn(this.msg);
        // deliveryMode = WebServiceConstants.MSG_TRANSPORT_MODE_SYNC;
        // }
        // }
        // } catch (Exception e) {
        // this.msg = "Unable to route message to its destination";
        // throw new MessageRoutingException(this.msg, e);
        // }
        //
        // logger.info("Routing Request: ");
        // logger.info(xml);

        // // Create and initialize message handle input object
        // MessageHandlerInput data = new MessageHandlerInput();
        // data.setMessageId(msgId);
        // data.setDeliveryMode(deliveryMode);
        // data.setCommand(null);
        // data.setPayload(serialObj);
        // data.setAttachments(null);
        // return data;
    }

    /**
     * Verifies that the payload contained within <i>results</i> is a valid JAXB
     * object.
     * 
     * @param results
     *            An instance of {@link MessageHandlerResults} containing the
     *            results of the service handler processing.
     * @return Pending validation, <i>results</i> is returned.
     * @throws InvalidDataException
     * @throws MessageRoutingException
     */
    @Override
    protected Object getReceptorResults(MessageHandlerResults results)
            throws InvalidDataException, MessageRoutingException {
        // Verify that results' payload is a valid JAXB object by trying to
        // marshal it to an XML document.
        super.getReceptorResults(results);
        return results;
    }

}
