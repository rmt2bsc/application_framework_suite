package com.api.messaging.rmi.client;

import java.io.Serializable;

import com.api.messaging.MessageException;
import com.api.messaging.MessageManager;
import com.api.messaging.handler.MessageHandlerResults;

/**
 * Interface used by RMI clients for invoking remote objects.
 * <p>
 * 
 * @author rterrell
 * 
 */
public interface RmiClient extends MessageManager {

    /**
     * Invokes a remote object named, <i>objName</i>, along with the data that
     * is to be processed.
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
    MessageHandlerResults sendMessage(String objName, String messageId,
            String command, Serializable payload) throws MessageException;
}
