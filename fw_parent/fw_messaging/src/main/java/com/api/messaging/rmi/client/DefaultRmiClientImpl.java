package com.api.messaging.rmi.client;

import java.io.Serializable;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.RMT2Base;
import com.SystemException;
import com.api.config.old.ProviderConfig;
import com.api.config.old.ProviderConnectionException;
import com.api.messaging.MessageException;
import com.api.messaging.handler.MessageHandlerException;
import com.api.messaging.handler.MessageHandlerResults;
import com.api.messaging.rmi.RmiMessageHandlerInput;
import com.api.messaging.rmi.engine.RmiEngine;
import com.api.messaging.rmi.service.RmiServiceCommand;

/**
 * An implementation of {@link RmiClient} which focuses on establishing a
 * connection to the RMI server, invoking a remote object on the RMI server, and
 * receiving the results of the RMI invocation.
 * 
 * @author Roy Terrell
 * 
 */
class DefaultRmiClientImpl extends RMT2Base implements RmiClient {

    private Registry registry;

    private String host;

    private int port;

    private String serverPolicyFile;

    private MessageHandlerResults results;

    /**
     * Create an DefaultRmiClientImpl object.
     */
    public DefaultRmiClientImpl() {
        super();
    }

    /**
     * Triggers the process responsible for initializing the RMI registry.
     * 
     * @throws RMT2RuntimeException
     *             Error invoking registry intialization process.
     */
    @Override
    public void init() {
        super.init();
        this.registry = null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.api.messaging.MessageManager#connect(com.api.messaging.ProviderConfig
     * )
     */
    @Override
    public Object connect(ProviderConfig config)
            throws ProviderConnectionException {
        if (config == null) {
            this.msg = "RMT client connection failed due an invalid ProviderConfig instance";
            throw new ProviderConnectionException(this.msg);
        }

        // Load security manager if needed.
        this.loadSecurityManager(config.getPolicyFile());

        // Convert port to integer
        String portStr = config.getPort();
        try {
            this.port = Integer.parseInt(portStr);
        } catch (NumberFormatException e) {
            this.port = RmiEngine.DEFAULT_RMI_PORT;
        }
        this.host = config.getHost();

        // Connect the target RMI registry
        try {
            this.registry = LocateRegistry.getRegistry(this.host, this.port);
            return this.registry;
        } catch (RemoteException e) {
            this.msg = "Unable to connect to RMI server " + config.getHost()
                    + " at port " + this.port;
            throw new ProviderConnectionException(this.msg, e);
        }
    }

    private void loadSecurityManager(String policyFile) {
        if (System.getSecurityManager() == null) {
            System.setProperty("java.security.policy", policyFile);
            System.setSecurityManager(new RMISecurityManager());
        }
        return;
    }

    /**
     * Invokes a remote object named, <i>objName</i>, with the data that is to
     * be processed.
     * <p>
     * All of the input parameters are used to create an instance of
     * {@link RmiMessageHandlerInput} which is used as the actual message when
     * contacting the target remote object.
     * 
     * @param objName
     *            The name of the remote object to invoke.
     * @param messageId
     *            The message id. This parameter is optional.
     * @param command
     *            The associated command. This parameter is optional.
     * @param payload
     *            The payload. This parameter is optional.
     * @return An instance of {@link MessageHandlerResults}
     * @throws MessageException
     */
    public MessageHandlerResults sendMessage(String objName, String messageId,
            String command, Serializable payload) throws MessageException {
        if (this.registry == null) {
            this.msg = "Unable to invoke remote object, " + objName
                    + ", due to the RMI registry has not been initialized";
            throw new RmiClientException(this.msg);
        }
        RmiMessageHandlerInput req = new RmiMessageHandlerInput(objName,
                messageId, command, payload);
        MessageHandlerResults results = (MessageHandlerResults) this
                .sendMessage(req);
        this.results = results;
        return results;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.messaging.MessageManager#sendMessage(java.io.Serializable)
     */
    public MessageHandlerResults sendMessage(Serializable request)
            throws MessageException {
        if (request == null) {
            this.msg = "Unable to send message via remote object due to the RmiMessageHandlerInput object is null or invalid";
            throw new RmiClientException(this.msg);
        }
        RmiMessageHandlerInput req = null;
        if (request instanceof RmiMessageHandlerInput) {
            req = (RmiMessageHandlerInput) request;
        }

        // Lookup Remote object
        RmiServiceCommand ro = null;
        try {
            ro = (RmiServiceCommand) this.registry.lookup(req
                    .getRemoteObjectName());
        } catch (AccessException e) {
            this.msg = "The registry has denied access to the current RMI client";
            throw new RmiClientException(this.msg, e);
        } catch (RemoteException e) {
            this.msg = "The RMI client has undergone a communication error with RMI registry occurred.";
            throw new RmiClientException(this.msg, e);
        } catch (NotBoundException e) {
            this.msg = "The RMI client was not able to process the request due to the remote object was not found in the registry by its name, "
                    + req.getRemoteObjectName();
            throw new RmiClientException(this.msg, e);
        }

        // Process the request with the remote object.
        try {
            MessageHandlerResults results = ro.processMessage(req);
            return results;
        } catch (MessageHandlerException e) {
            this.msg = "The RMI client failed due to encountering an error pertaining business logic/rules";
            throw new RmiClientException(this.msg, e);
        } catch (RemoteException e) {
            this.msg = "The RMI client failed due to a general remote call error";
            throw new RmiClientException(this.msg, e);
        }

    }

    /**
     * Returns the RMI registry instance.
     * 
     * @return the registry
     */
    public Registry getRegistry() {
        return registry;
    }

    /**
     * Sets the RMI registry.
     * 
     * @param registry
     *            the registry to set
     */
    protected void setRegistry(Registry registry) {
        this.registry = registry;
    }

    /**
     * Returns the RMI host server name.
     * 
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * Sets the RMI host server name.
     * 
     * @param host
     *            the host to set
     */
    protected void setHost(String host) {
        this.host = host;
    }

    /**
     * Returns the RMI's port number
     * 
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * Sets the RMI port number.
     * 
     * @param port
     *            the port to set
     */
    protected void setPort(int port) {
        this.port = port;
    }

    /**
     * Returns the policy file used by the RMI client.
     * 
     * @return the serverPolicyFile
     */
    public String getServerPolicyFile() {
        return serverPolicyFile;
    }

    /**
     * Sets the the policy file to be used by the RMI cleint.
     * 
     * @param serverPolicyFile
     *            the serverPolicyFile to set
     */
    protected void setServerPolicyFile(String serverPolicyFile) {
        this.serverPolicyFile = serverPolicyFile;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.messaging.MessageManager#getConfig()
     */
    @Override
    public ProviderConfig getConfig() throws MessageException {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.messaging.MessageManager#close()
     */
    @Override
    public void close() throws SystemException {
        this.registry = null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.messaging.MessageManager#getMessage()
     */
    @Override
    public Serializable getMessage() throws MessageException {
        return this.results;
    }

    /**
     * Always returns false.
     */
    @Override
    public boolean isAuthRequired() {
        return false;
    }

}
