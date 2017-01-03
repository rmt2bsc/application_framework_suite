package com.api.messaging.handler;

import java.rmi.RemoteException;

import com.RMT2Base;

/**
 * @author rterrell
 *
 */
public abstract class AbstractApiMessageHandlerCommandImpl extends RMT2Base implements RemoteApiHandlerCommand {
    
    protected String command;
    
    

    /**
     * 
     */
    protected AbstractApiMessageHandlerCommandImpl() {
	return;
    }

    /* (non-Javadoc)
     * @see com.api.messaging.handler.RemoteApiHandlerCommand#processRequest(com.api.messaging.handler.MessageHandlerInput)
     */
    @Override
    public MessageHandlerResults processMessage(MessageHandlerInput req) throws MessageHandlerException, RemoteException {
	this.command = req.getCommand();
	return this.callApiOperation(req);
    }

    /**
     * 
     * @param req
     * @return
     * @throws MessageHandlerException
     */
    abstract protected MessageHandlerResults callApiOperation(MessageHandlerInput req) throws MessageHandlerException;

    
    /**
     * @return the command
     */
    protected String getCommand() {
        return command;
    }
    
    
    


}
