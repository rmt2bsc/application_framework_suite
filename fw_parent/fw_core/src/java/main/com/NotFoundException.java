package com;

public class NotFoundException extends RMT2RuntimeException {
    private static final long serialVersionUID = 1441957717014777177L;

    private int errorCode;

    /**
     * Default constructor that creates an NotFoundException object with a null
     * message.
     * 
     */
    public NotFoundException() {
        super();
    }

    /**
     * Creates an NotFoundException with a message.
     * 
     * @param msg
     *            The text message.
     */
    public NotFoundException(String msg) {
        super(msg);
    }

    /**
     * Creates an NotFoundException with a code and no message.
     * 
     * @param code
     *            The integer code.
     */
    public NotFoundException(int code) {
        this.errorCode = code;
    }

    /**
     * Creates an NotFoundException with a message and a code.
     * 
     * @param msg
     *            The text message.
     * @param code
     *            The integer code.
     */
    public NotFoundException(String msg, int code) {
        super(msg);
        this.errorCode = code;
    }

    /**
     * Creates an NotFoundException using an Exception.
     * 
     * @param e
     *            An Exception object.
     */
    public NotFoundException(Exception e) {
        super(e);
    }

    /**
     * Creates a new NotFoundException with a the specified message and the
     * causing throwable instance.
     * 
     * @param msg
     *            the message that explains the error.
     * @param cause
     *            the cause (which is saved for later retrieval by the
     *            Throwable.getCause() method). (A null value is permitted, and
     *            indicates that the cause is nonexistent or unknown.)
     * 
     */
    public NotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * @return the errorCode
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * Constructs an NotFoundException object using a connection object, message
     * id, and an ArrayList of messages. A template message is retrieved from an
     * external data source which is identified by its message id, _code. The
     * list of messages are used to populate any place holders in the template
     * message in the order which they exist in the collection.
     * 
     * @param _con
     *            Connection to an external data source.
     * @param _code
     *            The id of the message.
     * @param _args
     *            One or more messages used to populate the place holders in the
     *            template message.
     */
    // public NotFoundException(Object _con, int _code, ArrayList _args) {
    // super(_con, _code, _args);
    // }
}
