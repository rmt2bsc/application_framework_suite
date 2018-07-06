package com.api.messaging.handler;

import java.io.Serializable;

import com.RMT2Base;

/**
 * A common model for managing the replys status details of a message handler.
 * 
 * @author rterrell
 * 
 */
public class MessageHandlerCommonReplyStatus extends RMT2Base implements Serializable {

    private static final long serialVersionUID = -8055073158717964783L;

    private String message;
    
    private String extMessage;

    private String returnStatus;

    private int returnCode;

   

    /**
     * Create a MessageHandlerResults object
     */
    public MessageHandlerCommonReplyStatus() {
        super();
        return;
    }



    /**
     * @return the messageText
     */
    public String getMessage() {
        return message;
    }



    /**
     * @param messageText the messageText to set
     */
    public void setMessage(String messageText) {
        this.message = messageText;
    }



    /**
     * @return the additionalMessageText
     */
    public String getExtMessage() {
        return extMessage;
    }



    /**
     * @param additionalMessageText the additionalMessageText to set
     */
    public void setExtMessage(String additionalMessageText) {
        this.extMessage = additionalMessageText;
    }



    /**
     * @return the statusCode
     */
    public String getReturnStatus() {
        return returnStatus;
    }



    /**
     * @param statusCode the statusCode to set
     */
    public void setReturnStatus(String statusCode) {
        this.returnStatus = statusCode;
    }



    /**
     * @return the returnCode
     */
    public int getReturnCode() {
        return returnCode;
    }



    /**
     * @param returnCode the returnCode to set
     */
    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }
}
