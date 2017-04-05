package com.api.messaging.webservice.router;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import com.InvalidDataException;
import com.RMT2Base;
import com.RMT2RuntimeException;
import com.api.config.ConfigConstants;
import com.api.config.old.ProviderConfig;
import com.api.config.old.ProviderConnectionException;
import com.api.messaging.MessageException;
import com.api.messaging.handler.MessageHandlerInput;
import com.api.messaging.handler.MessageHandlerResults;
import com.api.messaging.jms.JmsClientManager;
import com.api.messaging.jms.JmsConnectionManager;
import com.api.messaging.jms.JmsConstants;
import com.api.messaging.rmi.client.RmiClientSession;
import com.api.messaging.webservice.WebServiceConstants;
import com.api.messaging.webservice.http.client.HttpClientResourceFactory;
import com.api.messaging.webservice.http.client.HttpMessageSender;
import com.util.RMT2String2;

/**
 * An abstract implementation of {@link MessagingRouter} providing common router
 * functionality for managing the service registry and routing a web service
 * message to an appropriate service handler.
 * 
 * @author Roy Terrell
 * 
 */
public abstract class AbstractMessageRouterImpl extends RMT2Base implements MessagingRouter {

    private static final Logger logger = Logger.getLogger(AbstractMessageRouterImpl.class);

    // protected ServiceRegistry register;

    /**
     * Create a AbstractMessageRouterImpl with an initialized web service
     * registry.
     * 
     * @throws RMT2RuntimeException
     *             Web service registry could not be created.
     */
    public AbstractMessageRouterImpl() {
        return;
    }

    /**
     * Create the input data for a specific message handler using the incoming
     * generic message.
     * 
     * @param srvc
     *            the routing information pertaining to the web service message
     * @param inMessage
     *            an arbitrary object representing the incoming message data.
     * @return an instance of {@link MessageHandlerInput}
     * @throws MessageRoutingException
     *             Routing information is unobtainable due to the occurrence of
     *             data access errors or etc.
     */
    protected abstract MessageHandlerInput prepareMessageForTransport(MessageRoutingInfo srvc, Object message)
            throws MessageRoutingException;

    /**
     * Routes an incoming web service message to its targeted destination.
     * <p>
     * 
     * @param srvc
     *            the routing information pertaining to the web service message
     * @param inMessage
     *            an arbitrary message that is to be processed
     * @return an instance of {@linkMessageHandlerResults}
     * @throws MessageRoutingException
     */
    @Override
    public MessageHandlerResults routeMessage(MessageRoutingInfo srvc, Object inMessage)
            throws MessageRoutingException {
        MessageHandlerInput handlerMessage = this.prepareMessageForTransport(srvc, inMessage);

        if (handlerMessage == null) {
            throw new InvalidDataException("Unable to create an instance of MessageHandlerInput");
        }

        MessageHandlerResults results = null;
        if (srvc.getRouterType().equalsIgnoreCase(WebServiceConstants.MSG_ROUTER_TYPE_HTTP)) {
            results = this.routeMessageToHttpHandler(srvc, handlerMessage);
        }
        else if (srvc.getRouterType().equalsIgnoreCase(WebServiceConstants.MSG_ROUTER_TYPE_RMI)) {
            results = this.routeMessageToRmiHandler(srvc, handlerMessage);
        }
        else if (srvc.getRouterType().equalsIgnoreCase(WebServiceConstants.MSG_ROUTER_TYPE_JMS)) {
            results = this.routeMessageToJmsHandler(srvc, handlerMessage);
        }

        // Need to obtain the message id for the response message instead of
        // sending null.
        try {
            // If reply message id is not available, then set it to the request
            // message id with the String, "_RESPONSE", appended to it.
            if (srvc.getReplyMessageId() == null) {
                results.setMessageId(srvc.getMessageId() + "_RESPONSE");
            }
            else {
                results.setMessageId(srvc.getReplyMessageId());
            }
        } catch (InvalidDataException e) {
            throw new MessageRoutingException(e);
        }
        return results;
    }

    /**
     * Uses a RMI to route a message to a business API handler for processing.
     * 
     * @param srvc
     * @param message
     * @return
     * @throws MessageRoutingException
     */
    protected MessageHandlerResults routeMessageToRmiHandler(MessageRoutingInfo srvc, MessageHandlerInput message)
            throws MessageRoutingException {
        String remoteObj = srvc.getDestination();
        // Get host server where the intended web service implementation can be
        // found.
        String server = srvc.getHost();
        if (server == null) {
            // The web service's implementation was not specified in resource
            // configuration...assume implementation lives on the same server as
            // the SOAP router.
            server = System.getProperty(ConfigConstants.PROPNAME_SERVER);
        }

        // Make RMI call
        RmiClientSession client = new RmiClientSession();
        MessageHandlerResults results = client.callRmi(remoteObj, message.getMessageId(), message.getCommand(),
                message.getPayload());
        client.close();
        return results;
    }

    /**
     * Uses JMS to route the message to its destination.
     * <p>
     * The routing of JMS messages can be performed synchronously or
     * asynchronously depending on whether or not <i>replyDestination</i>
     * property of <i>srvc</i> is specified. When specified, a the method will
     * block until the conumer of the message sends the reply. The
     * <i>replyDestination</i> property is used to create a temporary
     * non-persistent destination for handling message replies.
     * 
     * @param srvc
     *            The routing information pertaining to the message
     * @param message
     *            The message to send.
     * @return an instance of {@link MessageHandlerResults} when the message is
     *         routed with the expectation of receiving a reply from the
     *         consumer or null for asynchronous submissions.
     * @throws MessageRoutingException
     *             General errors pertaining to sending a message.
     */
    protected MessageHandlerResults routeMessageToJmsHandler(MessageRoutingInfo srvc, MessageHandlerInput message)
            throws MessageRoutingException {

        JmsConnectionManager jmsConMgr = JmsConnectionManager.getJmsConnectionManger();

        // Get the appropriate JMS connection based on the messaging
        // system specified in transaction's configuration.
        Connection con = jmsConMgr.getConnection(JmsConstants.DEFAUL_MSG_SYS);
        if (con == null) {
            throw new RMT2RuntimeException(
                    "Unable to route message to JMS systems due to an error obtaining a connection for JMS client");
        }

        // Associate JMS connection with the client
        JmsClientManager jms = new JmsClientManager(con, jmsConMgr.getJndi());

        // Create message
        Message response = null;
        try {
            TextMessage msg = jms.createTextMessage(srvc.getDestination());
            msg.setText(message.getPayload().toString());
            Destination replyToDest = null;
            if (!RMT2String2.isEmpty(srvc.getReplyMessageId())) {
                replyToDest = jms.createReplyToDestination(srvc.getDestination(), msg);
            }

            jms.send(srvc.getDestination(), msg);
            if (msg.getJMSReplyTo() != null) {
                response = jms.listen(replyToDest, 10000);
                MessageHandlerResults reply = new MessageHandlerResults();
                if (response instanceof TextMessage) {
                    reply.setPayload(((TextMessage) response).getText());
                    return reply;
                }
            }
            return null;
        } catch (Exception e) {
            throw new RMT2RuntimeException(e);
        } finally {
            jms.stop(srvc.getDestination());
        }
    }

    /**
     * Uses a HTTP to route a message to the appropriate business API handler
     * for processing.
     * 
     * @param srvc
     * @param message
     * @return
     * @throws MessageRoutingException
     */
    protected MessageHandlerResults routeMessageToHttpHandler(MessageRoutingInfo srvc, MessageHandlerInput message)
            throws MessageRoutingException {
        // Get host server where the intended web service implementation can be
        // found.
        String server = srvc.getHost();
        if (server == null) {
            // The web service's implementation was not specified in resource
            // configuration...assume implementation lives on the same server as
            // the SOAP router.
            server = System.getProperty(ConfigConstants.PROPNAME_SERVER);
        }
        // Ensure that the protocol is prefixed to host name
        if (server.indexOf("http://") < 0) {
            server = "http://" + server;
        }

        // Tack on the client action to the URL since we are posting a
        // SOAP message to the request and not Properties collection.
        String url = server + "/" + srvc.getDestination() + "?clientAction=" + srvc.getMessageId();

        // TODO: Potential problem using HttpMessageSender since it exclusively
        // manages messages of a name/value pair nature. Will need to provide
        // some enhancements to the implemetnation of HttpMessagSender to handle
        // arbitary serializable Object types.
        HttpMessageSender client = HttpClientResourceFactory.getHttpInstance();
        ProviderConfig config;
        try {
            config = HttpClientResourceFactory.getHttpConfigInstance();
            config.setHost(url);
            client.connect(config);
            client.sendMessage(message);
            return (MessageHandlerResults) client.getMessage();
        } catch (MessageException e) {
            msg = "Problem routing web service HTTP request to its destination.  The URL in error: " + url;
            throw new MessageRoutingException(e);
        } catch (ProviderConnectionException e) {
            msg = "Problem routing web service HTTP request due to the inability to establish a connection to the HTTP resource via an erroneous ProviderConfig instance.  The URL in error: "
                    + url;
            throw new MessageRoutingException(e);
        }
    }
}
