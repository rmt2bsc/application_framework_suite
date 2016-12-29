package com.api.messaging.jms;

import com.RMT2RuntimeException;

/**
 * Manages error conditions orginating from the JmsConnectiontManager class.
 * 
 * @author rterrell
 *
 */
public class JmsConnectiontManagerException extends RMT2RuntimeException {

    private static final long serialVersionUID = -9055848289033486730L;

    /**
	 * 
	 */
    public JmsConnectiontManagerException() {
        super();
    }

    /**
     * @param msg
     */
    public JmsConnectiontManagerException(String msg) {
        super(msg);
    }

    /**
     * @param e
     */
    public JmsConnectiontManagerException(Throwable e) {
        super(e);
    }

    /**
     * @param msg
     * @param cause
     */
    public JmsConnectiontManagerException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
