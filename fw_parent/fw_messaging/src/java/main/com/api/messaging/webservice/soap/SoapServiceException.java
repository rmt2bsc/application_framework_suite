package com.api.messaging.webservice.soap;

import com.api.messaging.MessageException;

/**
 * @author appdev
 * 
 */
public class SoapServiceException extends MessageException {

    private static final long serialVersionUID = 9181149461315422237L;

    /**
     * 
     */
    public SoapServiceException() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param msg
     */
    public SoapServiceException(String msg) {
        super(msg);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param _msg
     * @param _code
     */
    public SoapServiceException(String _msg, int _code) {
        super(_msg, _code);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param e
     */
    public SoapServiceException(Exception e) {
        super(e);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param msg
     * @param cause
     */
    public SoapServiceException(String msg, Throwable cause) {
        super(msg, cause);
        // TODO Auto-generated constructor stub
    }

}
