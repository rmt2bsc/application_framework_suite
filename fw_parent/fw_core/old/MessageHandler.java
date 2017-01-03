package com.api.messaging;

import java.util.Properties;


/**
 * Interface for service message handlers.  
 * <p>
 * Service message handlers are classes that serve as the model for receiving and 
 * processing a specific request of a messaging system.
 *  
 * @author rterrell
 *
 */
public interface MessageHandler {
    
    static final String MSG_USERID_TAG = "userId";
    
    static final String MSG_REQUESTID_TAG = "serviceId";
    
    static final String MSG_RESPONSEID_TAG = "responseMsgId";
    
    /**
     * Processes the service request based on parameters included in <i>parms</i>.   Minimally, 
     * <i>parms</i> should include the key/value pairs for "userId" and "requestMsgId", which 
     * are the user id of the service requester and the service id of the request, respectively.
     * 
     * @param parms
     *           Properties instance containing the request parameters
     * @return
     *           The results as an arbitrary Object
     * @throws MessageHandlerException
     *           When <i>parms</i> is either null or does not contain key-value pairs for "userId" 
     *           and "requestMsgId".
     */
    Object execute(Properties parms) throws MessageHandlerException;
    
    /**
     * Returns the service id representing the response message.
     * 
     * @return String
     *          A String containing the name of the service id to be assinged as the name 
     *          of the response message.
     */
    String getResponseServiceId();
    
    /**
     * Assigns the service id of the response message.
     * 
     * @param id
     *          A String containing the name of the service id to be assinged as the name 
     *          of the response message.
     */
    void setResponseServiceId(String id);
}
