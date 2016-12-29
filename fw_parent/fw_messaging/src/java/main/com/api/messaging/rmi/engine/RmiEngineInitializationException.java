package com.api.messaging.rmi.engine;

import com.RMT2RuntimeException;

/**
 * An exception that indicates the sudden failure of initializing the RMI Server
 * 
 * @author rterrell
 * 
 */
public class RmiEngineInitializationException extends RMT2RuntimeException {

    private static final long serialVersionUID = -8831114014369797515L;

    /**
     * 
     */
    public RmiEngineInitializationException() {
        super();
    }

    /**
     * @param msg
     */
    public RmiEngineInitializationException(String msg) {
        super(msg);
    }

    /**
     * @param e
     */
    public RmiEngineInitializationException(Exception e) {
        super(e);
    }

    /**
     * @param msg
     * @param cause
     */
    public RmiEngineInitializationException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
