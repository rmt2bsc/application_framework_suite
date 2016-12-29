package com.api.messaging.rmi;

import java.io.Serializable;

import com.api.messaging.handler.MessageHandlerInput;

/**
 * Model used for sending data to a remote object involved in the remote method
 * invocation.
 * 
 * @author rterrell
 * 
 */
public class RmiMessageHandlerInput extends MessageHandlerInput {

    private static final long serialVersionUID = -8055073158717964783L;

    private String remoteObjectName;

    /**
     * Create a RmiMessageHandlerInput object
     */
    public RmiMessageHandlerInput() {
        super();
        return;
    }

    /**
     * Create a RmiMessageHandlerInput initialized with a known remote object
     * name, message id, command, and payload.
     * 
     * @param rmiObjName
     *            The name of the remote object.
     * @param messageId
     *            The message id. This parameter is optional.
     * @param command
     *            The associated command. This parameter is optional.
     * @param payload
     *            The payload. If provided, this parameter must be of type
     *            Seraializable. This parameter is optional.
     * @return An instance of {@link MessageHandlerResults}
     * @throws MessageException
     */
    public RmiMessageHandlerInput(String rmiObjName, String messageId,
            String command, Serializable payload) {
        this();
        this.remoteObjectName = rmiObjName;
        this.messageId = messageId;
        this.command = command;
        this.payload = payload;
        return;
    }

    /**
     * Return the name of the Remote object.
     * 
     * @return the remoteObjectName
     */
    public String getRemoteObjectName() {
        return remoteObjectName;
    }

    /**
     * Set the name of the remote object.
     * 
     * @param remoteObjectName
     *            the remoteObjectName to set
     */
    public void setRemoteObjectName(String remoteObjectName) {
        this.remoteObjectName = remoteObjectName;
    }

}
