package com.api.messaging.webservice;

import com.RMT2BaseBean;
import com.SystemException;

/**
 * Return code class for external remote service calls.
 * 
 * @author appdev
 * 
 */
public class ServiceReturnCode extends RMT2BaseBean {
    private static final long serialVersionUID = 2388664161270797390L;

    private int code;

    private String message;

    private String data;

    /**
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @param code
     *            the code to set
     */
    public void setCode(int code) {
        this.code = code;
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

    /**
     * @throws SystemException
     */
    public ServiceReturnCode() throws SystemException {
        // TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see bean.RMT2BaseBean#initBean()
     */
    @Override
    public void initBean() throws SystemException {
        // TODO Auto-generated method stub

    }
}
