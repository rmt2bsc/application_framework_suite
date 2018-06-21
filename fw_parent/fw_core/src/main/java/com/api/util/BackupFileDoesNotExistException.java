package com.api.util;

import com.api.BatchFileException;

/**
 * @author royterrell
 * 
 */
public class BackupFileDoesNotExistException extends BatchFileException {

    /**
     * 
     */
    private static final long serialVersionUID = -2229698804845567036L;

    /**
     * 
     */
    public BackupFileDoesNotExistException() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param msg
     */
    public BackupFileDoesNotExistException(String msg) {
        super(msg);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param code
     */
    public BackupFileDoesNotExistException(int code) {
        super(code);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param e
     */
    public BackupFileDoesNotExistException(Exception e) {
        super(e);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param msg
     * @param cause
     */
    public BackupFileDoesNotExistException(String msg, Throwable cause) {
        super(msg, cause);
        // TODO Auto-generated constructor stub
    }

}
