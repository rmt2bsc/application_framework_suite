package com.api.messaging.handler;

/**
 * Model for capturing the payload results of an arbitrary message handler
 * invocation.
 * 
 * @author rterrell
 * 
 */
public class MessageHandlerResults extends MessageHandlerCommonData {

    private static final long serialVersionUID = -8055073158717964783L;

    private int returnCode;

    private String errorMsg;

    /**
     * Create a MessageHandlerResults object
     */
    public MessageHandlerResults() {
        super();
        return;
    }

    /**
     * Return the return code of the remote method invocation.
     * 
     * @return 1 for success and -1 for error.
     */
    public int getReturnCode() {
        return returnCode;
    }

    /**
     * Set the return code of the remote method invocation.
     * 
     * @param returnCode
     *            the returnCode to set
     */
    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    /**
     * @return the errorMsg
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * @param errorMsg
     *            the errorMsg to set
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
