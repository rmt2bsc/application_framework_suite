package com.api.messaging.rmi.service;

import java.rmi.RemoteException;

import com.RMT2Base;
import com.api.messaging.handler.MessageHandlerException;
import com.api.messaging.handler.MessageHandlerInput;
import com.api.messaging.handler.MessageHandlerResults;

/**
 * Abstract service receptor class which functions as a template for accepting
 * and processing RMI messages.
 * 
 * @author Roy Terrell
 * 
 */
public abstract class AbstractRmiServiceCommandImpl extends RMT2Base implements
        RmiServiceCommand {

    protected String command;

    protected MessageHandlerInput data;

    /**
     * Create a AbstractRmiServiceCommandImpl without initializing the command
     * string or request data object.
     */
    protected AbstractRmiServiceCommandImpl() {
        return;
    }

    /**
     * Accepts the RMI message and invokes the appropriate API handler to
     * process the message.
     * 
     * @param req
     *            the input data in the form of a request.
     * @return an instance of {@link MessageHandlerResults}
     * @throws MessageHandlerException
     * @throws RemoteException
     */
    @Override
    public MessageHandlerResults processMessage(MessageHandlerInput req)
            throws MessageHandlerException, RemoteException {
        this.command = req.getCommand();
        this.data = req;
        return this.callApiOperation();
    }

    /**
     * Implement this method in order to invoke the appropriate handler for
     * processing the RMI message.
     * 
     * @return an instance of {@link MessageHandlerResults}
     * @throws MessageHandlerException
     */
    abstract protected MessageHandlerResults callApiOperation()
            throws MessageHandlerException;

    /**
     * @return the command
     */
    protected String getCommand() {
        return command;
    }

}
