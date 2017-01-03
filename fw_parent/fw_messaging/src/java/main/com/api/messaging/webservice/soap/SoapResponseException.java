package com.api.messaging.webservice.soap;

import com.api.messaging.MessageException;

/**
 * @author appdev
 * 
 */
public class SoapResponseException extends MessageException {

    private static final long serialVersionUID = 9181149461315422237L;

    /**
     * 
     */
    public SoapResponseException() {
        super();
    }

    /**
     * @param msg
     */
    public SoapResponseException(String msg) {
        super(msg);
    }

    /**
     * @param _msg
     * @param _code
     */
    public SoapResponseException(String _msg, int _code) {
        super(_msg, _code);
    }

    /**
     * @param e
     */
    public SoapResponseException(Exception e) {
        super(e);
    }

    /**
     * @param msg
     * @param cause
     */
    public SoapResponseException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
