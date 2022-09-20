package com.api.jsp.action;

import com.RMT2Exception;



/**
 * An exception that is thrown by descendent classes of AbstractActionHandler.
 * 
 * @author roy.terrell
 * 
 */
public class ActionHandlerException extends RMT2Exception {
    private static final long serialVersionUID = 8852424708037697306L;

    /**
     * Default constructor which its message is null.
     * 
     */
    public ActionHandlerException() {
        super();
    }

    /**
     * Constructs an ActionHandlerException object with a message.
     * 
     * @param msg
     *            The exception's message.
     */
    public ActionHandlerException(String msg) {
        super(msg);
    }

    /**
     * Constructs an ActionHandlerException object with an integer code and the
     * message is null.
     * 
     * @param code
     *            The exception code
     */
    public ActionHandlerException(int code) {
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
    public ActionHandlerException(String msg, int code) {
        super(msg, code);
    }



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
    public ActionHandlerException(String msg, int code, String objname, String methodname) {
        super(msg, code, objname, methodname);
    }

    /**
     * Constructs an ActionHandlerException using an Exception object.
     * 
     * @param e
     *            Exception
     */
    public ActionHandlerException(Exception e) {
        super(e);
    }

    /**
     * 
     * @param msg
     * @param e
     */
    public ActionHandlerException(String msg, Throwable e) {
        super(msg, e);
    }
}
