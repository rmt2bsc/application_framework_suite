package com.api.util;

import com.RMT2RuntimeException;

/**
 * @author rterrell
 * 
 */
public class RMT2OsFileConverterException extends RMT2RuntimeException {
    private static final long serialVersionUID = -1667377962143517874L;

    /**
     * 
     */
    public RMT2OsFileConverterException() {
        super();
    }

    /**
     * @param message
     */
    public RMT2OsFileConverterException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public RMT2OsFileConverterException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public RMT2OsFileConverterException(String message, Throwable cause) {
        super(message, cause);
    }

}
