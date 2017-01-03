package com.api.messaging.jms.client;

import org.apache.log4j.Logger;

import com.RMT2Base;
import com.api.messaging.MessageException;
import com.api.messaging.MessageRoutingException;
import com.api.messaging.MessageRoutingInfo;
import com.api.messaging.handler.MessageHandlerInput;
import com.api.messaging.handler.MessageHandlerResults;

/**
 * A wrapper class for providing an easier approach to creating JMS clients for
 * producing (sending) messages.
 * <p>
 * <b><u>How to Use the JmsProducerClient to send a message</u></b>
 * 
 * <pre>
 * // Setup producer
 * MessageRoutingInfo info = new MessageRoutingInfo();
 * MessageHandlerInput data = new MessageHandlerInput();
 * info.setDestination(&quot;queue1&quot;);
 * info.setHandler(&quot;testcases.messaging.jms.TestCommandHandler&quot;);
 * data.setPayload(&quot;This is asynchronous message&quot;);
 * data.setMessageId(&quot;TestAsynchronousMessage_1&quot;);
 * data.setCommand(&quot;edit_contact&quot;);
 * 
 * // Send message to its destination
 * try {
 *     JmsProducerClient client = new JmsProducerClient();
 *     client.callProducer(info, data);
 *     return;
 * } catch (Exception e) {
 *     e.printStackTrace();
 * }
 * </pre>
 * <p>
 * 
 * @author rterrell
 * 
 */
public class JmsProducerClient extends RMT2Base {

    private static Logger logger;

    // private String consumerApiReceptor;

    // private String destination;

    /**
     * Create a JmsProducerClient equipped with a logger.
     */
    public JmsProducerClient() {
        logger = Logger.getLogger(JmsProducerClient.class);
        logger.info("logger initialized for JmsProducerClient");
        return;
    }

    // /**
    // * Create a JmsProducerClient initialized with message routing data such
    // as the class
    // * name of the consumer handler and the name of the destination.
    // *
    // * @param routeInfo
    // * an instance of {@link MessageRoutingInfo}
    // */
    // public JmsProducerClient(MessageRoutingInfo routeInfo) {
    // this();
    // this.consumerApiReceptor = routeInfo.getHandler();
    // this.destination = routeInfo.getDestination();
    // return;
    // }

    /**
     * Send a JMS message using a combination of message routing information and
     * message handler input.
     * <p>
     * This method is best utilized under the management of the message router
     * and service registry for sending messages.
     * 
     * @param routeInfo
     *            an instance of {@link MessageRoutingInfo}
     * @param msgData
     *            an instance of {@link MessageHandlerInput}
     * @return an instance of {@link MessageHandlerResults}
     * @throws MessageRoutingException
     */
    public MessageHandlerResults callProducer(MessageRoutingInfo routeInfo,
            MessageHandlerInput msgData) throws MessageRoutingException {
        JmsProducerMsgBean msg = new JmsProducerMsgBean();
        msg.setPayload(msgData.getPayload());
        msg.setDestination(routeInfo.getDestination());
        msg.setMessageId(msgData.getMessageId());
        msg.setCommand(msgData.getCommand() == null ? msgData.getMessageId()
                : msgData.getCommand());
        msg.setConsumerApiHandler(routeInfo.getHandler());
        msg.setReplyDestination(routeInfo.getReplyMessageId());
        return this.callProducer(msg);
    }

    /**
     * Send a JMS message using basic infomation contained in <i>jmsMsgBean</i>.
     * <p>
     * This method is best used to facilitate the need to manually send messages
     * directly to the JMS destination outside message router. A practical
     * example would be sending message to a particular destination and waiting
     * for the reply.
     * 
     * @param jmsMsgBean
     *            an instance of {@link JmsProducerMsgBean} where the following
     *            properties are usually used: payload, destination, command,
     *            and replyDestination.
     * @return an instance of {@link MessageHandlerResults}
     * @throws MessageRoutingException
     */
    public MessageHandlerResults callProducer(JmsProducerMsgBean jmsMsgBean)
            throws MessageRoutingException {
        JmsResourceFactory f = new JmsResourceFactory();

        // Setup producer
        JmsMsgProducer prod;
        try {
            prod = f.getProducerInstance();
        } catch (JmsResourceCreationException e) {
            throw new MessageRoutingException(e);
        }
        try {
            prod.sendMessage(jmsMsgBean);
            return null;
        } catch (MessageException e) {
            this.msg = "A problem occurred trying to send JMS message to destination, "
                    + jmsMsgBean.getDestination();
            throw new MessageRoutingException(this.msg, e);
        } finally {
            prod.close();
            prod = null;
        }
    }

    // /**
    // * @param consumerApiReceptor the consumerApiReceptor to set
    // */
    // public void setConsumerApiReceptor(String consumerHandlerClassName) {
    // this.consumerApiReceptor = consumerHandlerClassName;
    // }
    //
    //
    // /**
    // * @param destination the destination to set
    // */
    // public void setDestination(String destination) {
    // this.destination = destination;
    // }
}
