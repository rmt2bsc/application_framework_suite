package com.api.messaging.jms;

import com.RMT2RuntimeException;

/**
 * Manages error conditions orginating from the JmsClientManager class.
 * 
 * @author rterrell
 *
 */
public class JmsClientManagerException extends RMT2RuntimeException {

    private static final long serialVersionUID = -9055848289033486730L;

    /**
	 * 
	 */
    public JmsClientManagerException() {
        super();
    }

    /**
     * @param msg
     */
    public JmsClientManagerException(String msg) {
        super(msg);
    }

    // /**
    // * @param msg
    // * @param code
    // */
    // public JmsClientManagerException(String msg, String code) {
    // super(msg, code);
    // }

    /**
     * @param e
     */
    public JmsClientManagerException(Throwable e) {
        super(e);
    }

    /**
     * @param msg
     * @param cause
     */
    public JmsClientManagerException(String msg, Throwable cause) {
        super(msg, cause);
    }

    // /**
    // * @param msg
    // * @param code
    // * @param cause
    // */
    // public JmsClientManagerException(String msg, String code, Throwable
    // cause) {
    // super(msg, code, cause);
    // }
}
