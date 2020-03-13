package com.api.messaging;

import com.RMT2RuntimeException;

/**
 * @author appdev
 *
 */
public class InvalidRequestException extends RMT2RuntimeException {

    private static final long serialVersionUID = 5215472098798504260L;

    /**
     * 
     */
    public InvalidRequestException() {
        super();
    }

    /**
     * @param msg
     */
    public InvalidRequestException(String msg) {
        super(msg);
    }

    /**
     * @param msg
     * @param e
     */
    public InvalidRequestException(String msg, Throwable e) {
        super(msg, e);
    }

    /**
     * @param e
     */
    public InvalidRequestException(Throwable e) {
        super(e);
    }

}
