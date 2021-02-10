package com.api.messaging.ftp;

import com.api.messaging.MessageException;

/**
 * This exception is thrown by classes using the FTP api.
 * 
 * @author RTerrell
 * 
 */
public class FtpException extends MessageException {
    private static final long serialVersionUID = 2469045739436149290L;

    /**
     * Default constructor
     */
    public FtpException() {
        super();
    }

    /**
     * Create exception with error message
     * 
     * @param msg
     */
    public FtpException(String msg) {
        super(msg);
    }

    /**
     * Create exception with Exception object that caused the error.
     * 
     * @param cause
     */
    public FtpException(Exception cause) {
        super(cause);
    }

    /**
     * Create exception with error message Exception object that caused the
     * error.
     * 
     * @param message
     * @param cause
     */
    public FtpException(String message, Throwable cause) {
        super(message);
    }
}
