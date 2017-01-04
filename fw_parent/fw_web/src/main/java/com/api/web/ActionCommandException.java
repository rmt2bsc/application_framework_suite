package com.api.web;

import com.RMT2Exception;

/**
 * An exception that is thrown by descendent classes of AbstractActionHandler.
 * 
 * @author roy.terrell
 * 
 */
public class ActionCommandException extends RMT2Exception {
    private static final long serialVersionUID = 8852424708037697306L;

    /**
     * Default constructor which its message is null.
     * 
     */
    public ActionCommandException() {
        super();
    }

    /**
     * Constructs an ActionHandlerException object with a message.
     * 
     * @param msg
     *            The exception's message.
     */
    public ActionCommandException(String msg) {
        super(msg);
    }

    /**
     * Constructs an ActionHandlerException object with an integer code and the
     * message is null.
     * 
     * @param code
     *            The exception code
     */
    public ActionCommandException(int code) {
        super(code);
    }

    /**
     * Constructs an ActionHandlerException object with a message and a code.
     * 
     * @param msg
     *            The exception message.
     * @param code
     *            The exception code.
     */
    public ActionCommandException(String msg, int code) {
        super(msg, code);
    }

    // /**
    // * Constructs an ActionHandlerException object using a connection object,
    // * message id, and an ArrayList of messages. A template message is
    // retrieved
    // * from an external data source which is identified by its message id,
    // * _code. The list of messages are used to populate any place holders in
    // the
    // * template message in the order which they exist in the collection.
    // *
    // * @param _con
    // * Connection to an external data source.
    // * @param _code
    // * The id of the message.
    // * @param _args
    // * One or more messages used to populate the place holders in the
    // * template message.
    // */
    // public ActionHandlerException(Object _con, int _code, ArrayList _args) {
    // super(_con, _code, _args);
    // }

    /**
     * Constructs an ActionHandlerException which the message will contain the
     * message text, message code, the names of the object and method which the
     * exception orginated.
     * 
     * @param msg
     *            The text of the message.
     * @param code
     *            A integer code representing the message.
     * @param objname
     *            The name of the object which message originated.
     * @param methodname
     *            The name of the method which them message orginated.
     */
    public ActionCommandException(String msg, int code, String objname,
            String methodname) {
        super(msg, code, objname, methodname);
    }

    /**
     * Constructs an ActionHandlerException using an Exception object.
     * 
     * @param e
     *            Exception
     */
    public ActionCommandException(Exception e) {
        super(e);
    }

    /**
     * 
     * @param msg
     * @param e
     */
    public ActionCommandException(String msg, Throwable e) {
        super(msg, e);
    }
}
