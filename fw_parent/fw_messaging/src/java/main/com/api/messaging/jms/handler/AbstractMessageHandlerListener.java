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
 * Abstract Message-Driven Bean implementation class for which mandates that the
 * implementor provide logic that appoints the approriate handler to process the
 * message.
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
public abstract class AbstractMessageHandlerListener {
    private static Logger logger = LoggerFactory
            .getLogger(AbstractMessageHandlerListener.class);

    protected ResourceBundle mappings;

    protected JmsClientManager jms;

    /**
     * Default constructor.
     */
    public AbstractMessageHandlerListener() {
        this.jms = null;
        return;
    }

    /**
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
     * This implementation basically uses the command pattern to process the
     * message and employs the following steps to process the message:
     * <ol>
     * <li>Accept the message (usually a text message)</li>
     * <li>Obtains the header data: application, module, and transaction</li>
     * <li>Calculate the command key from the handler's mappings</li>
     * <li>Load handler mappings based on application name</li>
     * <li>Instantiate the handler</li>
     * <li>Invoke the handler with the input message</li>
     * <li>Return the handler's response to client</li>
     * </ol>
     * 
     * @param msg
     *            the message to be processed
     * @return {@link MessageHandlerResults}
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

        // Create key name from the handler's mappings
        String commandKey = this.calculateCommandKey(app, module, trans);

        // Load handler mappings based on application name.
        ResourceBundle rb = this.loadHandlerMappings(app);

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
     * Builds the command key which will be used to identify the message
     * handler.
     * <p>
     * The command key is a calculated value that is comprised of the following
     * three properties: <i>application</i>, <i>module</i>, and
     * <i>transaction</i>. These three properties are combined together as a
     * String in the format of <i>application.module.transaction</i>.
     * 
     * @param app
     *            the application name
     * @param module
     *            the name of the application module
     * @param transaction
     *            the transaction id
     * @return String - the command key in the format of:
     *         application.module.transaction
     */
    protected String calculateCommandKey(String app, String module,
            String transaction) {
        StringBuilder commandKey = new StringBuilder();
        commandKey.append(app).append(".").append(module).append(".")
                .append(transaction);
        return commandKey.toString();
    }

    /**
     * Loads the application's message handler mappings as a ResourceBundle.
     * <p>
     * The message handler mappings are grouped in ResourceBundles and are based
     * on the application. The application name can be found in the header data
     * of the message. An example ResourceBundle file would like such:
     * <i>org.rmt2.config.HandlerMappings_accounting.properties</i>. This method
     * calculates the handler mapping ResourceBundle name.
     * <p>
     * The package name of the ResourceBundle is set as a System property
     * identified as <i>msg_handler_reg_pkg</i>. All names of the
     * ResouceBundles, which are dedicated to housing message handler mappings,
     * are prefixed with <i>HandlerMappings_</i>.
     * 
     * @param applicationName
     *            the application name
     * @return {@link ResourceBundle}
     */
    protected ResourceBundle loadHandlerMappings(String applicationName) {
        String mappingConfig = System
                .getProperty(JmsConstants.MSG_HANDLER_MAPPINGS_KEY)
                + "."
                + JmsConstants.HANDLER_MAPPING_CONFIG + "_" + applicationName;
        ResourceBundle rb = RMT2File.loadAppConfigProperties(mappingConfig);
        return rb;
    }

    /**
     * Returns a instance of the message handler.
     * <p>
     * This method uses the command key plus other key information to derive the
     * master key to lookup and instantiate the message handler. The master key
     * is calculated by combining the <i>commandKey</i> and the literal,
     * <i>.handler</i>.
     * <p>
     * For example, a key/value pair could look like this as a mapping:
     * accounting.accounts.getBusiness.handler=org.rmt2.handlers.accounting
     * .AccountingPayloadHandler,
     * 
     * @param commandKey
     *            the key that is used to lookup the message handler
     * @param config
     *            the ResouceBundle that contains all of the message handler
     *            mappings for an application.
     * @return The message handler which is an instance of
     *         {@link MessageHandlerCommand}
     */
    protected MessageHandlerCommand getCommandHandlerInstance(
            String commandKey, ResourceBundle config) {
        // Get handler class from mapping file
        String handlerClassName = config.getString(commandKey + ".handler");

        // Instantiate Handler class
        MessageHandlerCommand handler = (MessageHandlerCommand) RMT2Utility
                .getClassInstance(handlerClassName);
        return handler;
    }
}
