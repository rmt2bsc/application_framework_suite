package com.api.messaging.jms;

import java.io.Serializable;

import com.RMT2Base;

/**
 * JMS message bean model that is used by the RMT2 JMS API to manage a JMS Send/Receive operation
 * 
 * @author rterrell
 *
 */
public class CommonMessageInfoBean extends RMT2Base implements Serializable {

    private static final long serialVersionUID = 5050538455662052141L;

    /**
     * The message id which is synonomous to message command.
     */
    protected String messageId;

    /**
     * The destination where the message is to be sent to or retrieve from.
     * <p>
     * Its value can be either the destination name, which is a String, or an instance 
     * of {@link javax.jms.Destination}
     */
    protected Object destination;
    

    /**
     * Create a CommonMessageInfoBean
     */
    public CommonMessageInfoBean() {
	super();
    }


    /**
     * Return the message id
     * 
     * @return the messageId
     */
    public String getMessageId() {
        return messageId;
    }


    /**
     * Set the message id
     * 
     * @param messageId 
     *          the messageId to set
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
    
    
    /**
     * @return the destination
     */
    public Object getDestination() {
        return destination;
    }

    /**
     * @param destination the destination to set
     */
    public void setDestination(Object destination) {
        this.destination = destination;
    }


}
