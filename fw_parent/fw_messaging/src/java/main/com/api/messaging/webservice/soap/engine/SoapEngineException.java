package com.api.messaging.webservice.soap.engine;

import com.api.messaging.MessageException;

/**
 * Exception class for handling SOAP engine errors.
 * 
 * @author appdev
 * 
 */
public class SoapEngineException extends MessageException {

    private static final long serialVersionUID = 9181149461315422237L;

    /**
     * 
     */
    public SoapEngineException() {
        super();
    }

    /**
     * @param msg
     */
    public SoapEngineException(String msg) {
        super(msg);
    }

    /**
     * @param e
     */
    public SoapEngineException(Exception e) {
        super(e);
    }

    /**
     * @param msg
     * @param cause
     */
    public SoapEngineException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
