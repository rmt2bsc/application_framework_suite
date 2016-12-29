package com.api.persistence.db.orm.query.pagination;

import com.RMT2Exception;

/**
 * This exception is thrown by classes using the ORM Pagination api.
 * 
 * @author RTerrell
 * 
 */
public class PaginationException extends RMT2Exception {

    private static final long serialVersionUID = 2469045739436149290L;

    /**
     * 
     */
    public PaginationException() {
        super();
    }

    /**
     * 
     * @param msg
     */
    public PaginationException(String msg) {
        super(msg);
    }

    /**
     * 
     * @param cause
     */
    public PaginationException(Exception cause) {
        super(cause);
    }

    /**
     * 
     * @param message
     * @param cause
     */
    public PaginationException(String message, Throwable cause) {
        super(message);
    }
}
