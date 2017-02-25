package com.api.messaging.webservice.router;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import com.InvalidDataException;
import com.NotFoundException;
import com.RMT2Base;
import com.RMT2RuntimeException;
import com.api.config.ConfigConstants;
import com.api.config.old.ProviderConfig;
import com.api.config.old.ProviderConnectionException;
import com.api.messaging.MessageException;
import com.api.messaging.MessageRoutingException;
import com.api.messaging.MessageRoutingInfo;
import com.api.messaging.MessagingResourceFactory;
import com.api.messaging.MessagingRouter;
import com.api.messaging.handler.MessageHandlerInput;
import com.api.messaging.handler.MessageHandlerResults;
import com.api.messaging.jms.JmsClientManager;
import com.api.messaging.jms.JmsConnectionManager;
import com.api.messaging.jms.JmsConstants;
import com.api.messaging.rmi.client.RmiClientSession;
import com.api.messaging.webservice.ServiceRegistry;
import com.api.messaging.webservice.ServiceRegistryFactoryImpl;
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
public abstract class AbstractMessageRouterImpl extends RMT2Base implements
        MessagingRouter {

    private static final Logger logger = Logger
            .getLogger(AbstractMessageRouterImpl.class);

    // private static Map<String, MessageRoutingInfo> SERVICES;

    protected ServiceRegistry register;

    // protected int webServiceType;

    /**
     * Create a AbstractMessageRouterImpl with an initialized web service
     * registry.
     * 
     * @throws RMT2RuntimeException
     *             Web service registry could not be created.
     */
    public AbstractMessageRouterImpl() {
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

        // this.intitalizeLdapRegistry();

        // try {
        // RMT2BeanUtility beanUtil = new RMT2BeanUtility();
        // String srvcRegClass = RMT2File.getPropertyValue(
        // RMT2Utility.CONFIG_APP,
        // WebServiceConstants.SERVICE_REGISTRY_SRC_KEY_NAME);
        // this.register = (ServiceRegistry) beanUtil.createBean(srvcRegClass);
        // } catch (Exception e) {
        // String msg = "Unable to initialize service register Api";
        // logger.fatal(msg, e);
        // throw new MessageRoutingException(msg, e);
        // }
        // // Load the all service configurations
        // this.register.loadServices();

        // this.intitalizeRegistryFromFileSource(WebServiceConstants.WEBSERV_TYPE_SOAP);
        // this.intitalizeRegistryFromHttpSource(WebServiceConstants.WEBSERV_TYPE_SOAP);
        // this.intitalizeRegistryFromLdapSource(WebServiceConstants.WEBSERV_TYPE_SOAP);
    }

    // /**
    // * Setup the service registry using LDAP implementation of
    // * {@link ServiceRegistry}.
    // * <p>
    // * Registry data should of already been loaded at server start up time
    // from
    // * {@link com.api.config.SystemConfiguratorController}
    // *
    // * @throws MessageRoutingException
    // */
    // protected void intitalizeLdapRegistry() throws MessageRoutingException {
    // ServiceRegistryFactoryImpl f = new ServiceRegistryFactoryImpl();
    // this.register = f.getLdapServiceRegistryManager(null, null);
    // }

    /**
     * Create the input data for a specific message handler using the incoming
     * generic message.
     * 
     * @param inMessage
     *            an arbitrary object representing the incoming message data.
     * @return an instance of {@link MessageHandlerInput}
     * @throws MessageRoutingException
     *             Routing information is unobtainable due to the occurrence of
     *             data access errors or etc.
     */
    protected abstract MessageHandlerInput createReceptorInputData(
            Object inMessage) throws MessageRoutingException;

    // /**
    // * Setup the service registry with data obtained from a HTTP resource
    // *
    // * @param webServiceType
    // * An integer representing the type of web service configuration to load
    // into
    // * the registry. Web service types are like SOAP, RMI, HTTP, REST, and
    // etc.
    // * @throws MessageRoutingException
    // */
    // protected void intitalizeRegistryFromHttpSource(int webServiceType)
    // throws MessageRoutingException {
    // this.webServiceType = webServiceType;
    // try {
    // // Verifiy if service registry is loaded with SOAP based web SERVICES. If
    // not, then load
    // if (AbstractMessageRouterImpl.SERVICES == null) {
    // ServiceRegistryFactoryImpl f = new ServiceRegistryFactoryImpl();
    // ServiceRegistry regMgr = f.getHttpServiceRegistryManager();
    // AbstractMessageRouterImpl.SERVICES = regMgr.loadServices(webServiceType);
    // }
    // }
    // catch (Exception e) {
    // String msg = "Failed to load web service registry using a HTTP source";
    // throw new MessageRoutingException(msg, e);
    // }
    // }
    //
    // /**
    // * Setup the service registry with data obtained from a LDAP server.
    // *
    // * @param webServiceType
    // * An integer representing the type of web service configuration to load
    // into
    // * the registry. Web service types are like SOAP, RMI, HTTP, REST, and
    // etc.
    // * @throws MessageRoutingException
    // */
    // protected void intitalizeRegistryFromLdapSource(int webServiceType)
    // throws MessageRoutingException {
    // this.webServiceType = webServiceType;
    // try {
    // // Verifiy if service registry is loaded with SOAP based web SERVICES. If
    // not, then load
    // if (AbstractMessageRouterImpl.getServices() == null) {
    // ServiceRegistryFactoryImpl f = new ServiceRegistryFactoryImpl();
    // ServiceRegistry regMgr = f.getLdapServiceRegistryManager();
    // Map<String, MessageRoutingInfo> s = regMgr.loadServices(webServiceType);
    // AbstractMessageRouterImpl.setServices(s);
    // }
    // }
    // catch (Exception e) {
    // String msg = "Failed to load web service registry using LDAP source";
    // throw new MessageRoutingException(msg, e);
    // }
    // }
    //
    // protected void intitalizeRegistryFromFileSource(int webServiceType)
    // throws MessageRoutingException {
    // this.webServiceType = webServiceType;
    // try {
    // // Verifiy if service registry is loaded with SOAP based web SERVICES. If
    // not, then load
    // if (AbstractMessageRouterImpl.getServices() == null) {
    // ServiceRegistryFactoryImpl f = new ServiceRegistryFactoryImpl();
    // ServiceRegistry regMgr = f.getFileServiceRegistryManager(null);
    // Map<String, MessageRoutingInfo> s = regMgr.loadServices(webServiceType);
    // AbstractMessageRouterImpl.setServices(s);
    // }
    // }
    // catch (Exception e) {
    // String msg = "Failed to load web service registry using XML file source";
    // throw new MessageRoutingException(msg, e);
    // }
    // }

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
            throws InvalidDataException, NotFoundException,
            MessageRoutingException {
        if (this.register == null) {
            msg = "Unable to get message routing information due to the web service registry is not initialize";
            throw new MessageRoutingException(msg);
        }
        // Validate the existence of Service Id
        if (messageId == null) {
            msg = "Unable to get message routing information due to required message id is null";
            throw new InvalidDataException(msg);
        }
        MessageRoutingInfo srvc = this.register.getEntry(messageId);
        if (srvc == null) {
            msg = "Routing information was not found in the web service registry for message id,  "
                    + messageId;
            throw new NotFoundException(msg);
        }
        if (!messageId.equalsIgnoreCase(srvc.getMessageId())) {
            msg = "A naming conflict exist between the requested message id ["
                    + messageId
                    + "] and the service name associated with the web service registry entry that was found";
            throw new NotFoundException(msg);
        }
        if (srvc.getDestination() == null) {
            msg = "The required URL property of the matching message routing entry found in the web service registry for message id, "
                    + messageId + ", is null";
            throw new InvalidDataException(msg);
        }
        return srvc;
    }

    /**
     * Routes an incoming web service message to its targeted destination.
     * <p>
     * This method will use <i>messageId</i> to obtain the message routing
     * information.
     * 
     * @param messageId
     *            the identifier of the message to route.
     * @param message
     *            an arbitrary message that is to be processed
     * @return a generic repsonse appropriate for the descendent implementation.
     * @throws MessageRoutingException
     */
    @Override
    public Object routeMessage(String messageId, Object message)
            throws MessageRoutingException {
        MessageRoutingInfo routingInfo = null;
        try {
            routingInfo = this.getRoutingInfo(messageId);
            return this.routeMessage(routingInfo, message);
        } catch (MessageRoutingException e) {
            throw e;
        } catch (Exception e) {
            throw new MessageRoutingException(e);
        }
    }

    /**
     * Routes an incoming web service message to its targeted destination.
     * <p>
     * 
     * @param srvc
     *            the routing information pertaining to the web service message
     * @param message
     *            an arbitrary message that is to be processed
     * @return a generic repsonse appropriate for the descendent implementation.
     * @throws MessageRoutingException
     */
    @Override
    public Object routeMessage(MessageRoutingInfo srvc, Object message)
            throws MessageRoutingException {
        MessageHandlerInput handlerMessage = this
                .createReceptorInputData(message);
        MessageHandlerResults results = null;

        if (srvc.getRouterType().equalsIgnoreCase(
                WebServiceConstants.MSG_ROUTER_TYPE_HTTP)) {
            results = this.routeMessageToHttpHandler(srvc, handlerMessage);
        }
        else if (srvc.getRouterType().equalsIgnoreCase(
                WebServiceConstants.MSG_ROUTER_TYPE_RMI)) {
            results = this.routeMessageToRmiHandler(srvc, handlerMessage);
        }
        else if (srvc.getRouterType().equalsIgnoreCase(
                WebServiceConstants.MSG_ROUTER_TYPE_JMS)) {
            results = this.routeMessageToJmsHandler(srvc, handlerMessage);
        }

        // Need to obtain the message id for the response message instead of
        // sending null.
        // String responseMessageId = "RS" + srvc.getMessageId().substring(2);
        try {
            // If reply message id is not available, then set it to the request
            // message
            // id with the String, "_RESPONSE", appended to it.
            if (srvc.getReplyMessageId() == null) {
                results.setMessageId(srvc.getMessageId() + "_RESPONSE");
            }
            else {
                results.setMessageId(srvc.getReplyMessageId());
            }
            return this.getReceptorResults(results);
        } catch (InvalidDataException e) {
            throw new MessageRoutingException(e);
        }
    }

    /**
     * Uses a RMI to route a message to a business API handler for processing.
     * 
     * @param srvc
     * @param message
     * @return
     * @throws MessageRoutingException
     */
    protected MessageHandlerResults routeMessageToRmiHandler(
            MessageRoutingInfo srvc, MessageHandlerInput message)
            throws MessageRoutingException {
        String remoteObj = srvc.getDestination();
        // Get host server where the intended web service implementation can be
        // found.
        String server = srvc.getHost();
        if (server == null) {
            // The web service's implementation was not specified in resource
            // configuration...
            // assume implementation lives on the same server as the SOAP
            // router.
            server = System.getProperty(ConfigConstants.PROPNAME_SERVER);
        }

        // Make RMI call
        RmiClientSession client = new RmiClientSession();
        MessageHandlerResults results = client.callRmi(remoteObj,
                message.getMessageId(), message.getCommand(),
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
    protected MessageHandlerResults routeMessageToJmsHandler(
            MessageRoutingInfo srvc, MessageHandlerInput message)
            throws MessageRoutingException {

        JmsConnectionManager jmsConMgr = JmsConnectionManager
                .getJmsConnectionManger();

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
                replyToDest = jms.createReplyToDestination(
                        srvc.getDestination(), msg);
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

        // logger.info(AbstractMessageDrivenBean.class.getName()
        // + " message driven bean is instantiated!!!!!");
        //
        // JmsResourceFactory f = new JmsResourceFactory();
        // ProducerMsgBean jmsMsgBean = null;
        // MessageManager prod = null;
        // // Setup message bean and producer
        // try {
        // jmsMsgBean = f.createMessage(srvc, message);
        // prod = f.getProducerClientInstance();
        // } catch (JmsResourceCreationException e) {
        // throw new MessageRoutingException(e);
        // }
        // // Send message to its destination
        // try {
        // Message submittedMsg = (Message) prod.sendMessage(jmsMsgBean);
        // // If submittedMsg has a reply destination and the
        // // MessageHandlerInput object indicates that its delivery mode is
        // // synchronous ("SYNC"), then add logic to receive the reply from
        // // that destination and return the results to the caller as a
        // // MessageHandlerResults object. Otherwise, return null.
        // try {
        // if (submittedMsg.getJMSReplyTo() != null
        // && message.getDeliveryMode().equalsIgnoreCase(
        // WebServiceConstants.MSG_TRANSPORT_MODE_SYNC)) {
        // return this.waitForJmsReply(jmsMsgBean
        // .getReplyDestination());
        // }
        // } catch (JMSException e) {
        // this.msg =
        // "A problem occurred trying to interrogate the existence of the reply destination of the submitted JMS message";
        // throw new MessageRoutingException(this.msg, e);
        // }
        // return null;
        // } catch (MessageException e) {
        // this.msg =
        // "A problem occurred trying to send JMS message to destination, "
        // + jmsMsgBean.getDestination();
        // throw new MessageRoutingException(this.msg, e);
        // } finally {
        // prod.close();
        // prod = null;
        // }

        // JmsProducerClient client = new JmsProducerClient();
        // return client.callProducer(srvc, message);

        // JmsResourceFactory f = new JmsResourceFactory();
        //
        // // Setup producer
        // ProducerClient prod;
        // try {
        // prod = f.getProducerInstance();
        // }
        // catch (JmsResourceCreationException e) {
        // throw new MessageRoutingException(e);
        // }
        // ProducerMsgBean producerData = new ProducerMsgBean();
        // producerData.setData(message.getPayload());
        // producerData.setDestination(srvc.getDestination());
        // producerData.setMessageId(message.getCommand());
        // producerData.setMsgHandlerClass(srvc.getHandler());
        //
        // try {
        // prod.sendMessage(producerData);
        // return null;
        // }
        // catch (MessageException e) {
        // this.msg =
        // "A problem occurred trying to send JMS message to destination, " +
        // srvc.getDestination();
        // throw new MessageRoutingException(this.msg, e);
        // }
        // finally {
        // prod.close();
        // prod = null;
        // }
    }

    // private MessageHandlerResults waitForJmsReply(String destination)
    // throws MessageException {
    // JmsResourceFactory f = new JmsResourceFactory();
    // ConsumerClient replyConsumer;
    // try {
    // replyConsumer = f.getConsumerClientInstance(destination);
    // ConsumerMsgBean reply = (ConsumerMsgBean) replyConsumer
    // .getMessage();
    // MessageHandlerResults results = reply.getReplyPayload();
    // return results;
    // } catch (Exception e) {
    // this.msg =
    // "A problem occurred setting up and/or blocking synchronous consumer targeting tempoary JMS destination,  "
    // + destination;
    // logger.error(this.msg, e);
    // throw new MessageException(this.msg, e);
    // }
    // }

    /**
     * Uses a HTTP to route a message to the appropriate business API handler
     * for processing.
     * 
     * @param srvc
     * @param message
     * @return
     * @throws MessageRoutingException
     */
    protected MessageHandlerResults routeMessageToHttpHandler(
            MessageRoutingInfo srvc, MessageHandlerInput message)
            throws MessageRoutingException {
        // Get host server where the intended web service implementation can be
        // found.
        String server = srvc.getHost();
        if (server == null) {
            // The web service's implementation was not specified in resource
            // configuration...
            // assume implementation lives on the same server as the SOAP
            // router.
            server = System.getProperty(ConfigConstants.PROPNAME_SERVER);
        }
        // Ensure that the protocol is prefixed to host name
        if (server.indexOf("http://") < 0) {
            server = "http://" + server;
        }

        // Tack on the client action to the URL since we are posting a
        // SOAP message to the request and not Properties collection.
        String url = server + "/" + srvc.getDestination() + "?clientAction="
                + srvc.getMessageId();

        // TODO: Potential problem using HttpMessageSender since it exclusively
        // manages messages
        // of a name/value pair nature. Will need to provide some enhancements
        // to the
        // implemetnation of HttpMessagSender to handle arbitary serializable
        // Object types.
        HttpMessageSender client = HttpClientResourceFactory.getHttpInstance();
        ProviderConfig config;
        try {
            config = HttpClientResourceFactory.getHttpConfigInstance();
            config.setHost(url);
            client.connect(config);
            client.sendMessage(message);
            return (MessageHandlerResults) client.getMessage();
        } catch (MessageException e) {
            msg = "Problem routing web service HTTP request to its destination.  The URL in error: "
                    + url;
            throw new MessageRoutingException(e);
        } catch (ProviderConnectionException e) {
            msg = "Problem routing web service HTTP request due to the inability to establish a connection to the HTTP resource via an erroneous ProviderConfig instance.  The URL in error: "
                    + url;
            throw new MessageRoutingException(e);
        }

        // try {
        // HttpClient client = new HttpClient(url);
        // InputStream is = client.sendPostMessage(serialMessage);
        // client.close();
        //
        // MessageHandlerResults response = null;
        // return response;
        // }
        // catch (Exception e) {
        // msg =
        // "Problem routing SOAP request to its destination.  The URL in error: "
        // + url;
        // throw new MessageRoutingException(msg, e);
        // }
    }

    /**
     * Extracts the payload from the business API handler's response, marshals
     * it in the form of XML, and returns the XML to the client.
     * <p>
     * The payload is expected to be an object of JAXB nature. If <i>results</i>
     * is not a valid JAXB object, then this method will fail.
     * 
     * @param results
     *            An instance of {@link MessageHandlerResults} containing the
     *            response payload of the business API handler, which is
     *            expected to be a JAXB object.
     * @return The response message as an XML String.
     * @throws InvalidDataException
     */
    protected Object getReceptorResults(MessageHandlerResults results)
            throws InvalidDataException, MessageRoutingException {
        // logic to convert RMI results to SOAP response.
        String xml;
        try {
            xml = MessagingResourceFactory.getJaxbMessageBinder()
                    .marshalMessage(results.getPayload(),
                            results.getMessageId(), true);
            return xml;
        } catch (Exception e) {
            this.msg = "Unable to marshal messaging handler results into a XML String due to an invalid JAXB object reference: "
                    + results.getPayload().getClass().getName();
            logger.error(this.msg, e);
            throw new InvalidDataException(e);
        }
    }

    // /* (non-Javadoc)
    // * @see com.api.messaging.MessagingRouter#getServiceCount()
    // */
    // public Integer getServiceCount() {
    // return (this.register == null ? null : this.register.getServiceCount());
    // }

    // /**
    // * @return the SERVICES
    // */
    // protected static Map<String, MessageRoutingInfo> getServices() {
    // return SERVICES;
    // }
    //
    // /**
    // * @param SERVICES the SERVICES to set
    // */
    // protected static void setServices(Map<String, MessageRoutingInfo>
    // services) {
    // AbstractMessageRouterImpl.SERVICES = services;
    // }

    // /**
    // * Stub method.
    // *
    // * @param request
    // * This paramter is not assigned to anything.
    // */
    // @Override
    // public void setUserRequest(HttpServletRequest request) {
    // return;
    // }

}
