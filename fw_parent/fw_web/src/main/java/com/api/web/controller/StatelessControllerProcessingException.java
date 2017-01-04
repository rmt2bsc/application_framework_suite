package com.api.web.controller;

import com.RMT2Exception;

/**
 * Handles errors that occur in stateless controller types (servlets).
 * 
 * @author rterrell
 * 
 */
public class StatelessControllerProcessingException extends RMT2Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -5406968309555287094L;

    /**
     * 
     */
    public StatelessControllerProcessingException() {
    }

    /**
     * @param msg
     */
    public StatelessControllerProcessingException(String msg) {
        super(msg);
    }

    /**
     * @param e
     */
    public StatelessControllerProcessingException(Exception e) {
        super(e);
    }

    /**
     * @param msg
     * @param cause
     */
    public StatelessControllerProcessingException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
