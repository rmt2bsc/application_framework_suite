package com.api.messaging.handler;

import java.rmi.Remote;
import java.rmi.RemoteException;



/**
 * A common interface that provides a contract for receiving or consuming messages from 
 * remote processes in the form of native java objects.
 * <p>
 * An example of a remote process would be SOAP, RMI, JMS, and REST type web service 
 * technologies.  These processes can be thought of as clients to instances of 
 * RemoteApiHandlerCommand needing to forward a message to a business API handler 
 * residing in a different JVM or machine.
 * <p>
 * The implementor is expected to employ the technique of the Command design pattern.
 */
public interface RemoteApiHandlerCommand extends Remote {
    
    /**
     * 
     * @param req
     * @return
     * @throws MessageHandlerException
     * @throws RemoteException
     */
    MessageHandlerResults processMessage(MessageHandlerInput req) throws MessageHandlerException, RemoteException;

   
}
