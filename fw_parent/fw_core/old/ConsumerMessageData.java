package com.api.messaging.jms.service;

import java.io.Serializable;

import com.RMT2Base;

/**
 * Model used for sending data to a JMS consumer message handler for processing a message 
 * at the business API level.
 * 
 * @author rterrell
 *
 */
public class ConsumerMessageData extends RMT2Base implements Serializable {

    private static final long serialVersionUID = -8055073158717964783L;

    private Object payload;
    
    private String command;
    
    
    
    /**
     * Create a ConsumerMessageData object
     */
    public ConsumerMessageData() {
	super();
	return;
    }


    /**
     * Create a ConsumerMessageData initialized with a known command and the payload data.
     * 
     * @param command
     *         the request command or message id
     * @param data
     *         the data that is to be processed by the receiving remote object.
     */
    public ConsumerMessageData(String command, Object data) {
	this();
	this.command = command;
	this.payload = data;
	return;
    }
    
    
    /**
     * Return the request data.
     * 
     * @return the payload
     */
    public Object getPayload() {
        return payload;
    }


    /**
     * Set the request data
     * 
     * @param payload the payload to set
     */
    public void setPayload(Object payload) {
        this.payload = payload;
    }


    /**
     * Return the handler command.
     * 
     * @return the command
     */
    public String getCommand() {
        return command;
    }


    /**
     * Set the handler command.
     * 
     * @param command the command to set
     */
    public void setCommand(String command) {
        this.command = command;
    }

}
