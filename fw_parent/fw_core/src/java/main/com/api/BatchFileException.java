package com.api;

import com.RMT2Exception;

/**
 * @author appdev
 * 
 */
public class BatchFileException extends RMT2Exception {

    private static final long serialVersionUID = -2249933179263928838L;

    /**
     * 
     */
    public BatchFileException() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param msg
     */
    public BatchFileException(String msg) {
        super(msg);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param code
     */
    public BatchFileException(int code) {
        super(code);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param e
     */
    public BatchFileException(Exception e) {
        super(e);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param msg
     * @param cause
     */
    public BatchFileException(String msg, Throwable cause) {
        super(msg, cause);
        // TODO Auto-generated constructor stub
    }

}
