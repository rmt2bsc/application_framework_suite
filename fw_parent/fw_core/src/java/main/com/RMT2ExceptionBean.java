package com;

/**
 * This class is designed to capture, house, and track data items that pertain
 * to Exception classes descending from RMT2Exception.
 * 
 * @author roy.terrell
 * 
 */
public class RMT2ExceptionBean extends RMT2BaseBean {
    private static final long serialVersionUID = 4248050507731851801L;

    protected String objName;

    // protected String methodName;
    protected int errorCode;

    protected String errorMsg;

    // protected String className;
    protected String resolution;

    /**
     * Default constructor.
     * 
     * @throws SystemException
     */
    public RMT2ExceptionBean() throws SystemException {
        super();
        this.initBean();
    }

    /**
     * No Action
     */
    public void initBean() throws SystemException {
        return;
    }

    /**
     * Get the error code.
     * 
     * @return error code
     */
    public int getErrorCode() {
        return this.errorCode;
    }

    /**
     * Set teh error code
     * 
     * @param value
     *            error code
     */
    public void setErrorCode(int value) {
        this.errorCode = value;
    }

    /**
     * Set the error message.
     * 
     * @param value
     *            message
     */
    public void setErrorMsg(String value) {
        this.errorMsg = value;
    }

    /**
     * Get the error message.
     * 
     * @return Message
     */
    public String getErrorMsg() {
        return this.errorMsg;
    }

    /**
     * Set the object name
     * 
     * @param value
     *            The name of the object.
     */
    public void setObjectName(String value) {
        this.objName = value;
    }

    /**
     * Get the name of the object.
     * 
     * @return object name
     */
    public String getObjectName() {
        return this.objName;
    }

    /**
     * Set the resolution
     * 
     * @param value
     *            resolution
     */
    public void setResolution(String value) {
        this.resolution = value;
    }

    /**
     * Get the resolution
     * 
     * @return Resolution
     */
    public String getResolution() {
        return this.resolution;
    }

}
