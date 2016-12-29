package com.api;

import com.RMT2Exception;

/**
 * This exception is thrown when the construction or deconstruction of query
 * statements produces an error.
 * 
 * @author roy.terrell
 * 
 */
public class ProductBuilderException extends RMT2Exception {

    private static final long serialVersionUID = 9002985426996513157L;

    /**
     * Default constructor which its message is null.
     * 
     */
    public ProductBuilderException() {
        super();
    }

    /**
     * Constructs an ProductBuilderException object with a message.
     * 
     * @param msg
     *            The exception's message.
     */
    public ProductBuilderException(String msg) {
        super(msg);
    }

    /**
     * Constructs an ProductBuilderException object with an integer code and the
     * message is null.
     * 
     * @param code
     *            The exception code
     */
    public ProductBuilderException(int code) {
        super(code);
    }

    /**
     * Constructs an ProductBuilderException object with a message and a code.
     * 
     * @param msg
     *            The exception message.
     * @param code
     *            The exception code.
     */
    public ProductBuilderException(String msg, int code) {
        super(msg, code);
    }

    /**
     * Constructs an DatabaseException using an Exception object.
     * 
     * @param e
     *            Exception
     */
    public ProductBuilderException(Exception e) {
        super(e);
    }

}
