package com.ui.event;

import com.RMT2Constants;

/**
 * A notification source for indicating that a component's changes have been
 * saved.
 * 
 * @author rterrell
 *
 */
public class ComponentUpdateCompletedEvent extends BaseEvent {

    private static final long serialVersionUID = -4118435768509924170L;

    private RMT2Constants.OperationResultCode opCode;

    private String message;

    /**
     * Create a ComponentUpdateCompletedEvent initialized with the source of the
     * event, the operation code, and a text message describing the results of
     * the operation.
     * 
     * @param source
     *            the object that triggered the event
     * @param opCode
     *            the operation code which represent the status of the update
     *            operation.
     * @param message
     *            text describing the results of the update operation.
     */
    public ComponentUpdateCompletedEvent(Object source,
            RMT2Constants.OperationResultCode opCode, String message) {
        super(source, 0);
        this.opCode = opCode;
        this.message = message;
    }

    /**
     * @return the opCode
     */
    public RMT2Constants.OperationResultCode getOpCode() {
        return opCode;
    }

    /**
     * @param opCode
     *            the opCode to set
     */
    public void setOpCode(RMT2Constants.OperationResultCode opCode) {
        this.opCode = opCode;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message
     *            the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
