package com.api.messaging.rmi.client;

import com.api.messaging.MessageException;

/**
 * An exception for handling RMI client errors.
 * 
 * @author rterrell
 * 
 */
public class RmiClientException extends MessageException {

    private static final long serialVersionUID = -8831114014369797515L;

    /**
     * 
     */
    public RmiClientException() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param msg
     */
    public RmiClientException(String msg) {
        super(msg);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param e
     */
    public RmiClientException(Exception e) {
        super(e);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param msg
     * @param cause
     */
    public RmiClientException(String msg, Throwable cause) {
        super(msg, cause);
        // TODO Auto-generated constructor stub
    }

}
