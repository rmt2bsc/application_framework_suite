package com.api.messaging.webservice.soap;

import com.api.messaging.MessageException;

/**
 * @author appdev
 * 
 */
public class SoapRequestException extends MessageException {

    private static final long serialVersionUID = 9181149461315422237L;

    /**
     * 
     */
    public SoapRequestException() {
        super();
    }

    /**
     * @param msg
     */
    public SoapRequestException(String msg) {
        super(msg);
    }

    /**
     * @param _msg
     * @param _code
     */
    public SoapRequestException(String _msg, int _code) {
        super(_msg, _code);
    }

    /**
     * @param e
     */
    public SoapRequestException(Exception e) {
        super(e);
    }

    /**
     * @param msg
     * @param cause
     */
    public SoapRequestException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
