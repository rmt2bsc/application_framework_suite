package com.api.messaging.jms.service;

import com.api.messaging.handler.MessageHandlerException;
import com.api.messaging.handler.MessageHandlerResults;


/**
 * An interface providing a contract for invoking JMS handler objects once a message has 
 * arrived to its destination.
 * <p>
 * If it is required of the handler to return results, then it is mandatory that the results 
 * be of type {@link MessageHandlerResults}.  This class obtains the results of the 
 * invocation using the technique of the Command design pattern.
 */
public interface ConsumerCommand {
    
    /**
     * 
     * @param req
     * @return
     * @throws MessageHandlerException
     */
    MessageHandlerResults processRequest(ConsumerMessageData req) throws MessageHandlerException;

   
}
