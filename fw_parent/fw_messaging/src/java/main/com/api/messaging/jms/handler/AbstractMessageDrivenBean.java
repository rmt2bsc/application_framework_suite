package com.api.messaging.jms.handler;

import java.util.List;
import java.util.ResourceBundle;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.RMT2RuntimeException;
import com.api.messaging.handler.MessageHandlerException;
import com.api.messaging.handler.MessageHandlerResults;
import com.api.messaging.jms.JmsClientManager;
import com.api.messaging.jms.JmsConstants;
import com.api.xml.RMT2XmlUtility;
import com.util.RMT2File;
import com.util.RMT2Utility;

/**
 * Abstract Message-Driven Bean implementation class that implements the
 * template and command patterns for processing JMS messages that are retrieved
 * from either a queue or topic destinations.
 * <p>
 * A relpy is sent to the caller in the event the incoming message is associated
 * with a ReplyTo Destination.
 */
public abstract class AbstractMessageDrivenBean {
    private static Logger logger = LoggerFactory
            .getLogger(AbstractMessageDrivenBean.class);

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
        String msg = null;

        // Identify the Destination this message came from
        Destination dest;
        try {
            dest = message.getJMSDestination();
        } catch (JMSException e) {
            throw new MessageHandlerException(
                    "Error obttaining message destination object", e);
        }
        String destName;
        this.jms = JmsClientManager.getInstance();
        destName = this.jms.getInternalDestinationName(dest);
        logger.info("Recieved message from destination, " + destName);

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
            throw new MessageHandlerException(
                    "Error occurred evaluating the JMS Reply To destination", e);
        } catch (Exception e) {
            throw new MessageHandlerException(
                    "Error occurred sending the response message to its JMS ReplyTo destinationevaluating the JMS Reply To destination",
                    e);
        }
    }

    /**
     * Processes the request message and returns the results to the consumer.
     * <p>
     * This implementation uses the command pattern which basically identifies
     * the message handler designated to process the message, passes the message
     * to the handler, and accepts the results of the message handler. The
     * message handler is required to be of type {@link MessageHandlerCommand}
     * and is identified by use of a command key.
     * <p>
     * The command key is a calculated value and its properties can be found in
     * the message. It is comprised of the following three properties:
     * <i>application</i>, <i>module</i>, and <i>transaction</i>. These three
     * properties are combined together as a String in the format of
     * <i>application.module.transaction</i>. In order to create the
     * MessageHandlerCommand instance, a transaction file, which contains the
     * class name of handler that is associated witht he command key, must be
     * loaded as a ResourceBundle object. The ResouceBundle is identified by the
     * <i>application</i> property of the command key.
     * 
     * @param msg
     * @return an instance of {@link MessageHandlerResults}
     */
    protected MessageHandlerResults processMessage(Message message) {
        // For now, accept only TextMessage types
        String requestMsg = null;
        String responseMsg = null;
        if (message instanceof TextMessage) {
            try {
                requestMsg = ((TextMessage) message).getText();
            } catch (JMSException e) {
                throw new MessageHandlerException(
                        "Error occurred fetching JMS TextMessage content", e);
            }
        }
        logger.info("MDB request message: ");
        logger.info(requestMsg);

        // Get header data: application, module, and transaction
        String query = JmsConstants.HEADER_BASE_XPATH_QUERY;
        List<String> results = RMT2XmlUtility.getElementValue(
                JmsConstants.HEADER_NODE_APPLICATION, query, requestMsg);
        String app = results.get(0);
        results = RMT2XmlUtility.getElementValue(
                JmsConstants.HEADER_NODE_MODULE, query, requestMsg);
        String module = results.get(0);
        results = RMT2XmlUtility.getElementValue(
                JmsConstants.HEADER_NODE_TRANSACTION, query, requestMsg);
        String trans = results.get(0);

        // Create handler mapping key name
        String commandKey = this.createCommandKey(app, module, trans);

        // Load handler mappings based on application name.
        ResourceBundle rb = this.getApplicationMappings(app);

        // Instantiate Handler class
        MessageHandlerCommand handler = this.getCommandHandlerInstance(
                commandKey, rb);

        // Invoke handler.
        try {
            MessageHandlerResults response = handler.processMessage(
                    commandKey.toString(), requestMsg);
            if (response != null && response.getPayload() != null) {
                responseMsg = response.getPayload().toString();
                logger.info("MDB response message: ");
                logger.info(responseMsg);
            }
            return response;
        } catch (MessageHandlerCommandException e) {
            throw new RMT2RuntimeException(
                    "Error executing message handler for command, "
                            + commandKey.toString(), e);
        }
    }

    /**
     * Generates the command key which is used in dynamically identifying the
     * appropriate message handler at runtime.
     * <p>
     * The command key is a calculated value and its properties can be found in
     * the message. It is comprised of the following three properties:
     * <i>application</i>, <i>module</i>, and <i>transaction</i>. These three
     * properties are combined together as a String in the format of
     * <i>application.module.transaction</i> and returned to the caller.
     * 
     * @param app
     *            the name of the application
     * @param module
     *            the name of the application module
     * @param transaction
     *            the transaction
     * @return the command key in the format of
     *         <i>application.module.transaction</i>.
     */
    private String createCommandKey(String app, String module,
            String transaction) {
        StringBuilder commandKey = new StringBuilder();
        commandKey.append(app).append(".").append(module).append(".")
                .append(transaction);
        return commandKey.toString();
    }

    /**
     * Load handler mappings based on header data.
     * <p>
     * Example Properties file: org.rmt2.config.HandlerMappings_Accounting
     * 
     * @param app
     * @return
     */
    private ResourceBundle getApplicationMappings(String app) {
        String mappingConfig = System
                .getProperty(JmsConstants.MSG_HANDLER_MAPPINGS_KEY)
                + "."
                + JmsConstants.HANDLER_MAPPING_CONFIG + "_" + app;
        ResourceBundle rb = RMT2File.loadAppConfigProperties(mappingConfig);
        return rb;
    }

    /**
     * Identifies, instantiates, and returns the MessageHandlerCommand instance
     * to the caller based of the command key.
     * 
     * @param commandKey
     *            the command key used to identify the message handler to
     *            instantiate
     * @param config
     *            the data source containing the message configuration used to
     *            lookup the transaction details.
     * @return an instance of {@link MessageHandlerCommand}
     */
    private MessageHandlerCommand getCommandHandlerInstance(String commandKey,
            ResourceBundle config) {
        // Get handler class from mapping file
        String handlerClassName = config.getString(commandKey + ".handler");

        // Instantiate Handler class
        MessageHandlerCommand handler = (MessageHandlerCommand) RMT2Utility
                .getClassInstance(handlerClassName);
        return handler;
    }
}
