package com.api.messaging.webservice.soap.client;

import com.api.messaging.MessageException;

/**
 * @author appdev
 * 
 */
public class SoapBuilderException extends MessageException {

    private static final long serialVersionUID = 9181149461315422237L;

    /**
     * 
     */
    public SoapBuilderException() {
        super();
    }

    /**
     * @param msg
     */
    public SoapBuilderException(String msg) {
        super(msg);
    }

    /**
     * @param _msg
     * @param _code
     */
    public SoapBuilderException(String _msg, int _code) {
        super(_msg, _code);
    }

    /**
     * @param e
     */
    public SoapBuilderException(Exception e) {
        super(e);
    }

    public SoapBuilderException(String msg, Exception cause) {
        super(msg, cause);
    }

    /**
     * @param msg
     * @param cause
     */
    public SoapBuilderException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
