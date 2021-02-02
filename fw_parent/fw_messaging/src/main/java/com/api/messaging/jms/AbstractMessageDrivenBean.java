package com.api.messaging.jms;

import java.util.ResourceBundle;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import com.api.messaging.handler.MessageHandlerCommonReplyStatus;
import com.api.messaging.handler.MessageHandlerException;
import com.api.messaging.handler.MessageHandlerResults;

/**
 * Abstract Message-Driven Bean implementation class that implements the
 * template and command patterns for processing JMS messages that are retrieved
 * from either a queue or topic destinations.
 * <p>
 * This implementation dynamically identifies the designated message handler,
 * passes the message to the handler, and accepts the results of the message
 * handler. The message handler is required to be of type
 * {@link MessageHandlerCommand} and is identified by use of a command key.
 * <p>
 * The command key is a calculated value, and its properties can be found
 * embedded in the message. It is comprised of the following three properties:
 * <i>application</i>, <i>module</i>, and <i>transaction</i>. These three
 * properties are combined together as a String in the format of
 * <i>application.module.transaction</i>. In order to identify and instantiate
 * the MessageHandlerCommand instance, a transaction file, which contains the
 * mappings needed to identify the handler's class name that is associated with
 * the command key, must be loaded as a ResourceBundle object.
 * <p>
 * The ResouceBundle is identified by the <i>application</i> property of the
 * command key. The name of a typical messaging transaction ResourceBundle file
 * will be as such: <i>HandlerMappings_<application>.properties</i>. For
 * example, the mapping file for the accounting and address book applications
 * would be identified as <i>HandlerMappings_accounting.properties</i> and
 * <i>HandlerMappings_contacts.properties</i>, respectively.
 * <p>
 * A relpy is sent to the caller in the event the incoming message is associated
 * with a ReplyTo Destination.
 */
public abstract class AbstractMessageDrivenBean {
    private static Logger logger = Logger.getLogger(AbstractMessageDrivenBean.class);
    
    protected static final String BUS_SERVER_ERROR = "Business API Server Error was encountered.  Contact application support";

    protected ResourceBundle mappings;

    protected JmsClientManager jms;

    /**
     * Default constructor.
     */
    public AbstractMessageDrivenBean() {
        this.jms = null;
        return;
    }

    /**
     * Processes the request message and returns the results to the consumer.
     * 
     * @param message
     *            instance of {@link Message}
     * @return {@link MessageHandlerResults}
     */
    protected abstract MessageHandlerResults processMessage(Message message);

    /**
     * Build a repsonse to be sent bact to the call in the event an error occurs
     * during the period of processing the message prior to identifying the API
     * handler.
     * 
     * @param errorDetails
     *            an instance of {@link MessageHandlerCommonReplyStatus}
     * @return the error message marshalled as a String.
     */
    protected abstract String buildErrorResponse(MessageHandlerCommonReplyStatus errorDetails);
    
    /**
     * Provides common functionality for caputring a JMS message from its
     * respiective destinaition, processing the message, and, when applicable,
     * send a response message to a specified destination.
     * <p>
     * This implementation relies on the template pattern ffor proessing the
     * actual JMS message.
     * 
     * @param message
     *            an instance of {@link Message}
     * @see MessageListener#onMessage(Message)
     */
    public void onMessage(Message message) {
        logger.info("Entering onMessage() method");
        this.jms = JmsClientManager.getInstance();

        // Process the message by its appropriate handler
        MessageHandlerResults results = this.processMessage(message);

        // Send the handler's reply message, if requested
        try {
            if (message.getJMSReplyTo() != null) {
                TextMessage replyMsg = this.jms.createTextMessage();
                replyMsg.setText(results.getPayload().toString());
                this.jms.send(message.getJMSReplyTo(), replyMsg);
            }
        } catch (JMSException e) {
            throw new MessageHandlerException("Error occurred evaluating the JMS Reply To destination", e);
        } catch (Exception e) {
            String errMsg = "Error occurred sending the response message to its JMS ReplyTo destinationevaluating the JMS Reply To destination";
            throw new MessageHandlerException(errMsg, e);
        }
    }


}
