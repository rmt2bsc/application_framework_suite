package com.api.messaging.rmi.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.api.messaging.handler.MessageHandlerException;
import com.api.messaging.handler.MessageHandlerInput;
import com.api.messaging.handler.MessageHandlerResults;

/**
 * A common interface that provides a contract for receiving or consuming RMI
 * messages in the form of native java objects.
 * <p>
 * An example of a remote process would be SOAP, RMI, JMS, and REST type web
 * service technologies. These processes can be thought of as clients to
 * instances of RmiServiceCommand needing to forward a message to a business API
 * handler residing in a different JVM or machine.
 * <p>
 * The implementor is expected to employ the technique of the Command design
 * pattern.
 */
public interface RmiServiceCommand extends Remote {

    /**
     * Accepts the RMI message.
     * 
     * @param req
     *            the input data in the form of a request.
     * @return an instance of {@link MessageHandlerResults}
     * @throws MessageHandlerException
     * @throws RemoteException
     */
    MessageHandlerResults processMessage(MessageHandlerInput req)
            throws MessageHandlerException, RemoteException;

}
