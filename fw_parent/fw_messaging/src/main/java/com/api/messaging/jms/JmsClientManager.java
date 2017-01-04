package com.api.messaging.jms;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TemporaryQueue;
import javax.jms.TemporaryTopic;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.Context;

import org.apache.log4j.Logger;

import com.RMT2RuntimeException;

/*
 * The JmsClientManager class is a facade in that it provides a very simple
 * interface for using JMS. 
 * <p>
 * This class depends on the JNDI context for looking up JMS resouces.
 * Whether a destination is a topic or queue, the implementation details 
 * are hidden to the user.
 *
 * @author rterrell
 */
public class JmsClientManager {
    private static Logger LOGGER = Logger.getLogger(JmsClientManager.class);

    /**
     * Connection to the JMS provider is set up once for all class instances
     */
    protected Connection connection = null;

    /**
     * JNDI related data, loaded once for all class instances
     */
    protected Context jndi = null;

    // Destinations maintained in this hashtable
    protected Map<String, JMSDestination> jmsDestinations;

    protected String errMsg;

    /**
     * Nested class encapsulates JMS Destination objects and their associated
     * producers and consumers
     * 
     * @author rterrell
     * 
     */
    class JMSDestination {
        Destination destination = null;

        Destination replyToDestination = null;

        Session session = null;

        MessageProducer producer = null;

        MessageConsumer consumer = null;

        public JMSDestination(Destination destination, Session session,
                MessageProducer producer, MessageConsumer consumer) {
            this.destination = destination;
            this.session = session;
            this.producer = producer;
            this.consumer = consumer;
        }
    }

    /**
     * 
     */
    private JmsClientManager() {
        this.jmsDestinations = new HashMap<String, JMSDestination>();
        return;
    }

    /**
     * Creates JmsClientManager object which is initialized with a JMS
     * connection and JNDI context.
     * 
     * @param connection
     *            a valid JMS connection
     * @param jndi
     *            a valid JNDI context
     * @throws JmsUtilException
     *             when <i>connection</i> is null or invalid. when <i>jndi</i>
     *             is null or invalid.
     */
    public JmsClientManager(Connection connection, Context jndi) {
        this();
        if (connection == null) {
            this.errMsg = "JmsClientManager intialization error: Must receive a valid JMS connection";
            LOGGER.fatal(this.errMsg);
            throw new JmsClientManagerException(this.errMsg);
        }
        if (jndi == null) {
            this.errMsg = "JmsClientManager intialization error: Must receive a valid JMS JNDI reference";
            LOGGER.fatal(this.errMsg);
            throw new JmsClientManagerException(this.errMsg);
        }
        this.connection = connection;
        this.jndi = jndi;
    }

    /**
     * Get a JmsClientManager based on the default messaging system.
     * <p>
     * The default messaging system can be identified by
     * {@link JmsConstants#DEFAUL_MSG_SYS}
     * 
     * @return
     */
    public static JmsClientManager getInstance() {
        return JmsClientManager.getInstance(JmsConstants.DEFAUL_MSG_SYS);
    }

    /**
     * Get a JmsClientManager based on the specified messaging system
     * 
     * @param systemId
     *            the id of the messaging system
     * @return
     */
    public static JmsClientManager getInstance(String systemId) {
        JmsConnectionManager jmsConMgr = JmsConnectionManager
                .getJmsConnectionManger();

        // Get the appropriate JMS connection based on the messaging
        // system specified in transaction's configuration.
        Connection con = jmsConMgr.getConnection(systemId);
        if (con == null) {
            throw new RMT2RuntimeException(
                    "Error obtaining a connection for JMS client instance");
        }

        // Associate JMS connection with the client
        return new JmsClientManager(con, jmsConMgr.getJndi());
    }

    /**
     * Uses JNDI to obtain JMS objects.
     * 
     * @param name
     *            the name of the object to lookup
     * @return
     */
    private Object lookupJndiObject(String name) {
        try {
            Object obj = this.jndi.lookup(name);
            return obj;
        } catch (Exception e) {
            this.errMsg = "Unable to locate JMS object, " + name
                    + ", via JNDI directory lookup";
            LOGGER.warn(this.errMsg, e);
            throw new RuntimeException(this.errMsg, e);
        }
    }

    /**
     * Get internally managed JMSDestination object based on destination name.
     * 
     * @param name
     *            the name of the destination
     * @return {@link JMSDestination}
     * @throws Exception
     */
    protected JMSDestination getJMSDestination(String name) throws Exception {
        // Look for an existing Destination for the given name
        JMSDestination jmsDest = jmsDestinations.get(name);

        // If not found, by default, create Queue destination now
        if (jmsDest == null) {
            this.createDestination(name, Queue.class);
            jmsDest = jmsDestinations.get(name);
        }
        return jmsDest;
    }

    /**
     * 
     * @param jmsDest
     * @throws Exception
     */
    protected void setupProducer(JMSDestination jmsDest) throws Exception {
        if (jmsDest.producer != null)
            return;
        jmsDest.producer = jmsDest.session.createProducer(jmsDest.destination);
    }

    /**
     * Setup an asynchronous consumer.
     * <p>
     * In cases where <i>jmsDest</i> may not have a consumer object, then a
     * consumer object is created.
     * 
     * @param jmsDest
     *            an instance of {@link JMSDestination} containing the consumer
     * @param callback
     *            An instance of an implementaton of {@link MessageListener}.
     * @throws Exception
     */
    protected void setupAsynchConsumer(JMSDestination jmsDest,
            MessageListener callback) throws Exception {
        if (jmsDest.consumer == null) {
            jmsDest.consumer = jmsDest.session
                    .createConsumer(jmsDest.destination);
        }
        jmsDest.consumer.setMessageListener(callback);
    }

    /**
     * Setup a synchronous consumer.
     * 
     * @param jmsDest
     *            an instance of {@link JMSDestination} containing the consumer
     * @param timeout
     *            the tiem in milliseconds to block waitning to receive the
     *            message.
     * @return an instance of {@link Message}
     * @throws Exception
     */
    protected Message setupSynchConsumer(JMSDestination jmsDest, int timeout)
            throws Exception {
        if (jmsDest.consumer == null) {
            jmsDest.consumer = jmsDest.session
                    .createConsumer(jmsDest.destination);
        }
        if (timeout > 0)
            return jmsDest.consumer.receive(timeout);
        else
            return jmsDest.consumer.receive();
    }

    /**
     * Creates a JMS ReplyTo destination and associtates it with the JMS
     * Message.
     * <p>
     * This method uses <i>requestMsgDestName</i> to determine the appropriate
     * temporary destination class type to use as the JMS reply to destination.
     * The type of destination chosen for the ReplyToDestination is based on the
     * destination type of <i>dest<i>. For example, if <i>dest</i> is of type
     * Queue, then the ReplyTo Destination will be of type,
     * <i>javax.jms.TemporaryQueue</i>. It is imperative that the appropriate
     * temporary destination type is paired with the request message's type.
     *
     * @param requestMsgDestName
     *            The destination name of the request message which will be
     *            associated with the JMSReplyTo destination.
     * @param msg
     *            The JMS message that will be associated with the ReplyTo
     *            destination.
     * @return the JMS replyTo destination that was created.
     * @throws Exception
     */
    public Destination createReplyToDestination(String requestMsgDestName,
            Message msg) throws Exception {
        Class tempDestClass = null;
        JMSDestination jmsDest = getJMSDestination(requestMsgDestName);
        if (jmsDest.destination instanceof Queue) {
            tempDestClass = TemporaryQueue.class;
        }
        else {
            tempDestClass = TemporaryTopic.class;
        }

        // Create the actual temporary destination.
        Destination tempDest = this
                .createReplyToDestination(msg, tempDestClass);

        // Pair temporary destination with the JMSDestinaiton entry so we can
        // easily track and delete during JMS resource cleanup.
        jmsDest.replyToDestination = tempDest;
        return tempDest;
    }

    /**
     * Creates a JMS ReplyTo destination and associtates it with the JMS
     * Message.
     * <p>
     * The JMS ReplyTo destination must be of type
     * <i>javax.jms.TemporaryTopic</i> or <i>javax.jms.TemporaryQueue</i>.
     *
     * @param requestMsgDest
     *            The JMS message that will be associated with the ReplyTo
     *            destination.
     * @param destType
     *            Must be of type <i>javax.jms.TemporaryTopic</i> or
     *            <i>javax.jms.TemporaryQueue</i>
     * @return the JMS replyTo destination that was created.
     * @throws Exception
     *             <i>destType</i> is null or its Class type is something other
     *             than <i>javax.jms.TemporaryTopic</i> or
     *             <i>javax.jms.TemporaryQueue</i>.
     */
    protected Destination createReplyToDestination(Message requestMsg,
            Class destType) throws Exception {
        if (destType == null) {
            throw new RuntimeException(
                    "Reply To Destination class type cannot be null");
        }
        Session session = this.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Create a temporary topic or temporary queue as specified
        Destination replyToDest = null;
        if (destType.getName().equals("javax.jms.TemporaryQueue")) {
            LOGGER.info("createReplyToDestination() - creating Temporary Queue");
            replyToDest = session.createTemporaryQueue();
        }
        else if (destType.getName().equals("javax.jms.TemporaryTopic")) {
            LOGGER.info("createReplyToDestination() - creating Temporary Topic");
            replyToDest = session.createTemporaryTopic();
        }
        else {
            // force destination to be either TemporaryTopic or TemporaryQueue
            throw new RuntimeException(
                    "Reply To Destination class type must be of type TemporaryQueue or TemporaryTopic");
        }
        this.createReplyToDestination(requestMsg, replyToDest);

        String destName = null;
        try {
            destName = this.getInternalDestinationName(replyToDest);
        } catch (Exception e) {
            destName = "[Unknown]";
        }
        LOGGER.info("Created temporary JMS destination named, " + destName);
        return replyToDest;
    }

    /**
     * Associates a known JMS ReplyTo destination with a JMS Message.
     * <p>
     * The JMS ReplyTo destination must be a derivative of
     * <i>javax.jms.Destination</i>.
     * 
     * @param msg
     *            The JMS message that will be associated with the ReplyTo
     *            destination.
     * @param replyToDest
     *            An instance of either <i>javax.jms.Destination</i>,
     *            <i>javax.jms.Queue</i>, <i>javax.jms.Topic</i>,
     *            <i>javax.jms.TemporaryQueue</i>, or
     *            <i>javax.jms.TemporaryTopic</i>.
     * @throws Exception
     *             General JMS error.
     */
    protected void createReplyToDestination(Message msg, Destination replyToDest)
            throws Exception {
        msg.setJMSReplyTo(replyToDest);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Public API wrapper methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create a Destination using a session that is non-transacted and auto
     * acknowledged.
     * 
     * @param name
     *            the name of the destination
     * @param type
     *            the class type of the destination, which is usually
     *            <i>javax.jms.Queue</i> or <i>javax.jms.Topic</i>.
     * @throws Exception
     */
    public void createDestination(String name, Class type) throws Exception {
        this.createDestination(name, type, false, Session.AUTO_ACKNOWLEDGE);
    }

    /**
     * Create a Destination and specify the session's transacted inidcator and
     * acknowledgment mode.
     * 
     * @param name
     *            the name of the destination
     * @param type
     *            the class type of the destination, which is usually
     *            <i>javax.jms.Queue</i> or <i>javax.jms.Topic</i>.
     * @param transacted
     *            a boolean indicating whether or not the session is transacted.
     *            Set to <i>true</i> when the desire to use a local transaction
     *            which may be subsequently committed and rollback manually. Set
     *            to <i>false</i> to make the session non-transacted.
     * @param ackMode
     *            When <i>transacted</i> is false, indicates how messages
     *            received by the session are acknowledged. Otherwise, this
     *            parameter is ignored.
     * @throws Exception
     */
    public void createDestination(String name, Class type, boolean transacted,
            int ackMode) throws Exception {

        // If the destination already exists, just return
        if (jmsDestinations.get(name) != null)
            return;

        // Create the new destination and store it
        Session session = connection.createSession(transacted, ackMode);
        LOGGER.info("JMS session created for destination, " + name);

        // Look up the destination otherwise create it
        Destination destination;
        if (this.jndi != null) {
            try {
                LOGGER.info("Begin JNDI lookup of destination, " + name + "...");
                destination = (Destination) lookupJndiObject(name);
                String jmsDestName = this
                        .getInternalDestinationName(destination);
                LOGGER.info("JNDI lookup of destination, " + name
                        + ", was successful!!");
                LOGGER.info("JNDI destination name is mapped to JMS destination, "
                        + jmsDestName);
            } catch (Exception e) {
                LOGGER.warn("Unable to lookup JMS destination via JNDI: "
                        + name);
                destination = null;
            }
        }
        else {
            destination = null;
        }

        if (destination == null) {
            LOGGER.info("JMS destination, " + name
                    + ", will be created directly.");
            // Create a topic or queue as specified
            if (type.getName().equals("javax.jms.Queue")) {
                LOGGER.info("setupDestination() - creating Queue");
                destination = session.createQueue(name);
            }
            else {
                LOGGER.info("setupDestination() - creating Topic");
                destination = session.createTopic(name);
            }
        }

        JMSDestination jmsDest = new JMSDestination(destination, session, null,
                null);
        jmsDestinations.put(name, jmsDest);
    }

    /**
     * This is an asynchronous listener with a callback.
     * <p>
     * The caller provides a JMS callback interface reference, and any messages
     * received for the given destination are provided through the onMessage()
     * callback method
     * 
     * @param destName
     * @param callback
     * @throws Exception
     */
    public void listen(String destName, MessageListener callback)
            throws Exception {
        LOGGER.info("JmsClientManager.listen(" + destName + ", " + callback
                + ")");

        JMSDestination jmsDest = getJMSDestination(destName);

        // Set the caller as a topic subcriber or queue receiver as appropriate
        setupAsynchConsumer(jmsDest, callback);
        LOGGER.info("listen() - Asynchronous listen on destination " + destName);
    }

    /**
     * This is a synchronous listen. The caller will block until a message is
     * received for the given destination
     * 
     * @param destName
     * @return
     * @throws Exception
     */
    public Message listen(String destName) throws Exception {
        LOGGER.info("listen() - Synchronous listen on destination " + destName);

        JMSDestination jmsDest = getJMSDestination(destName);

        // Setup the consumer and block until a
        // message arrives for this destination
        return setupSynchConsumer(jmsDest, 0);
    }

    /**
     * This is a synchronous listen. The caller will block until a message is
     * received for the given destination OR the timeout value (in milliseconds)
     * has been reached
     * 
     * @param destName
     *            the name of the destination
     * @param timeout
     *            the total time to block in milliseconds
     * @return
     * @throws Exception
     */
    public Message listen(String destName, int timeout) throws Exception {
        LOGGER.info("listen() - Synchronous listen on destination " + destName);

        JMSDestination jmsDest = getJMSDestination(destName);

        // Setup the consumer and block until a
        // message arrives for this destination
        return setupSynchConsumer(jmsDest, timeout);
    }

    /**
     * 
     * @param dest
     * @param callback
     * @throws Exception
     */
    public void listen(Destination dest, MessageListener callback)
            throws Exception {
        Session s = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageConsumer c = s.createConsumer(dest);
        c.setMessageListener(callback);
    }

    /**
     * 
     * @param dest
     * @return
     * @throws Exception
     */
    public Message listen(Destination dest) throws Exception {
        Session s = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageConsumer c = s.createConsumer(dest);
        Message msg = c.receive();
        s.close();
        return msg;
    }

    /**
     * 
     * @param dest
     * @param timeout
     * @return
     * @throws Exception
     */
    public Message listen(Destination dest, int timeout) throws Exception {
        Session s = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageConsumer c = s.createConsumer(dest);
        Message msg = c.receive(timeout);
        s.close();
        return msg;
    }

    /**
     * The following allows the caller to send a Message object to a destination
     * 
     * @param destName
     * @param msg
     * @throws Exception
     */
    public void send(String destName, Message msg) throws Exception {
        JMSDestination jmsDest = getJMSDestination(destName);

        // Make sure we have a message producer created for this destination
        setupProducer(jmsDest);

        // Send the message for this destination
        jmsDest.producer.send(msg);
        LOGGER.info("send() - message sent");
    }

    /**
     * Send message to a particular JMS Destination.
     * <p>
     * The destination can be of type Queue, Topic, TemporaryQueue, or
     * TemporaryTopic. For temporary destinations, a producer is created without
     * any association to a destination and is non persistent.
     * 
     * @param dest
     *            an instance of {@link Queue}, {@link Topic},
     *            {@link TemporaryQueue}, or {@link TemporaryTopic}
     * @param msg
     *            the message that is to be sent to <i>dest</i>.
     * @throws Exception
     */
    public void send(Destination dest, Message msg) throws Exception {
        Session s = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageProducer p = null;
        if (dest instanceof TemporaryQueue || dest instanceof TemporaryTopic) {
            p = s.createProducer(null);
            p.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            p.send(dest, msg);
        }
        else {
            p = s.createProducer(dest);
            p.send(msg);
        }
        String destName = null;
        try {
            destName = this.getInternalDestinationName(dest);
        } catch (Exception e) {
            destName = "[Unknown]";
        }
        LOGGER.info("JMS message was sent to destination, " + destName);
        s.close();
    }

    /**
     * The following allows the caller to send a Serializable object to a
     * destination
     * 
     * @param destName
     * @param obj
     * @throws Exception
     */
    public void send(String destName, Serializable obj) throws Exception {
        JMSDestination jmsDest = getJMSDestination(destName);
        // Make sure we have a message producer created for this destination
        setupProducer(jmsDest);

        // Send the message for this destination
        Message msg = createJMSMessage(obj, jmsDest.session);
        jmsDest.producer.send(msg);
        LOGGER.info("send() - message sent");
    }

    /**
     * The following allows the caller to send text to a destination
     * 
     * @param destName
     * @param messageText
     * @throws Exception
     */
    public void send(String destName, String messageText) throws Exception {
        this.send(destName, (Serializable) messageText);
    }

    /**
     * Closes out the producer, consumer, and/or session that is reltated to a
     * given destination.
     * 
     * @param destName
     *            the name of the destination to shutdown
     */
    public void stop(String destName) {
        try {
            // Look for an existing destination for the given destination
            JMSDestination jmsDest = jmsDestinations.get(destName);
            if (jmsDest != null) {
                // Remove temporary destination, if avaialble
                if (jmsDest.replyToDestination != null) {
                    String tempDestName = null;
                    try {
                        tempDestName = this
                                .getInternalDestinationName(jmsDest.replyToDestination);
                    } catch (Exception e) {
                        tempDestName = "[Unknown]";
                    }
                    try {
                        if (jmsDest.replyToDestination instanceof TemporaryTopic) {
                            ((TemporaryTopic) jmsDest.replyToDestination)
                                    .delete();
                        }
                        if (jmsDest.replyToDestination instanceof TemporaryQueue) {
                            ((TemporaryQueue) jmsDest.replyToDestination)
                                    .delete();
                        }
                        LOGGER.info("Temporary destination was deleted: "
                                + tempDestName);
                    } catch (Exception e) {
                        LOGGER.error("Problem closing temporary JMS destination: "
                                + e.getMessage());
                        e.printStackTrace();
                    }
                }

                // Close out all JMS related state
                if (jmsDest.producer != null) {
                    jmsDest.producer.close();
                    LOGGER.info("JMS producer closed for destination, "
                            + destName);
                }
                if (jmsDest.consumer != null) {
                    jmsDest.consumer.close();
                    LOGGER.info("JMS consumer closed for destination, "
                            + destName);
                }
                if (jmsDest.session != null) {
                    jmsDest.session.close();
                    LOGGER.info("JMS session closed for destination, "
                            + destName);
                }

                jmsDest.destination = null;
                jmsDest.replyToDestination = null;
                jmsDest.session = null;
                jmsDest.producer = null;
                jmsDest.consumer = null;

                // Remove the JMS client entry
                jmsDestinations.remove(destName);
                jmsDest = null;
            }
        } catch (Exception e) {

        }
    }

    /**
     * Returns the name of the JMS destination object.
     * 
     * @param dest
     * @return
     */
    public String getInternalDestinationName(Destination dest) {
        if (dest != null) {
            try {
                String destName = null;
                if (dest instanceof Topic) {
                    destName = ((Topic) dest).getTopicName();
                }
                if (dest instanceof Queue) {
                    destName = ((Queue) dest).getQueueName();
                }
                return destName;
            } catch (JMSException e) {
                this.errMsg = "Problem identifying JMS destination name";
                LOGGER.error(this.errMsg);
                e.printStackTrace();
                throw new JmsClientManagerException(this.errMsg, e);
            }
        }
        return null;
    }

    /**
     * Returns the actual name of the JMS destination that is mapped to the JNDI
     * name, <i>jndiName</i>.
     * 
     * @param jndiName
     * @return the name of the destination
     */
    public String getInternalDestinationName(String jndiName) {
        Destination destination;
        String destName;
        if (this.jndi != null) {
            try {
                destination = (Destination) lookupJndiObject(jndiName);
                destName = this.getInternalDestinationName(destination);
            } catch (Exception e) {
                this.errMsg = "Unable to lookup JMS destination via JNDI name: "
                        + jndiName;
                LOGGER.warn(this.errMsg);
                throw new JmsClientManagerException(this.errMsg, e);
            }
        }
        else {
            this.errMsg = "Can't lookup JMS destination because JNDI environment is not initialized";
            LOGGER.warn(this.errMsg);
            throw new JmsClientManagerException(this.errMsg);
        }
        return destName;
    }

    /**
     * Creates a JMS Message of type TextMessage or ObjectMessage.
     * 
     * @param obj
     *            The actual message content. Will be of type String or Object.
     * @param session
     *            The JMS session
     * @return an instance of {@link TextMessage} when msg is of type String.
     *         Otherwise, an instance of {@link ObjectMessage} is returned.
     * @throws Exception
     */
    protected Message createJMSMessage(Serializable obj, Session session)
            throws Exception {
        if (obj instanceof String) {
            TextMessage textMsg = session.createTextMessage();
            textMsg.setText((String) obj);
            return textMsg;
        }
        else {
            ObjectMessage objMsg = session.createObjectMessage();
            objMsg.setObject(obj);
            return objMsg;
        }
    }

    /**
     * 
     * @param destName
     * @return
     * @throws Exception
     */
    public MapMessage createMapMessage(String destName) throws Exception {
        JMSDestination jmsDest = getJMSDestination(destName);
        return jmsDest.session.createMapMessage();
    }

    /**
     * Creates a TextMessage without associating with a destination.
     * <p>
     * This is useful when associating a TextMessage with a JMS ReplyTo
     * Destination object.
     * 
     * @return {@link TextMessage}
     * @throws Exception
     */
    public TextMessage createTextMessage() throws Exception {
        Session session = this.createSession(false, Session.AUTO_ACKNOWLEDGE);
        return session.createTextMessage();
    }

    /**
     * Creates a TextMessage that is associated with a particular destination.
     * 
     * @param destName
     *            the Destination name
     * @return {@link TextMessage}
     * @throws Exception
     */
    public TextMessage createTextMessage(String destName) throws Exception {
        JMSDestination jmsDest = getJMSDestination(destName);
        return jmsDest.session.createTextMessage();
    }

    /**
     * 
     * @param destName
     * @return
     * @throws Exception
     */
    public ObjectMessage createObjectMessage(String destName) throws Exception {
        JMSDestination jmsDest = getJMSDestination(destName);
        return jmsDest.session.createObjectMessage();
    }

    /**
     * 
     * @param destName
     * @return
     * @throws Exception
     */
    public BytesMessage createBytesMessage(String destName) throws Exception {
        JMSDestination jmsDest = getJMSDestination(destName);
        return jmsDest.session.createBytesMessage();
    }

    public Session createSession(boolean transacted, int ackMode)
            throws Exception {
        Session session = connection.createSession(transacted, ackMode);
        return session;
    }

    /**
     * 
     * @param destName
     * @return
     * @throws Exception
     */
    public Session getSession(String destName) throws Exception {
        JMSDestination jmsDest = getJMSDestination(destName);
        return jmsDest.session;
    }

    /**
     * 
     * @param destName
     * @return
     * @throws Exception
     */
    public Destination getDestination(String destName) throws Exception {
        JMSDestination jmsDest = getJMSDestination(destName);
        return jmsDest.destination;
    }

    /**
     * 
     * @param destName
     * @return
     * @throws Exception
     */
    public MessageProducer getProducer(String destName) throws Exception {
        JMSDestination jmsDest = getJMSDestination(destName);
        return jmsDest.producer;
    }

    /**
     * 
     * @param destName
     * @return
     * @throws Exception
     */
    public MessageConsumer getConsumer(String destName) throws Exception {
        JMSDestination jmsDest = getJMSDestination(destName);
        return jmsDest.consumer;
    }

}
