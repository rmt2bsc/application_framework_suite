package testcases;

import com.RMT2Exception;

/**
 * Handles System Configuration errors.
 * 
 * @author Roy TErrell
 * 
 */
public class ApiTestException extends RMT2Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -3464583273800872788L;

    /**
     * 
     */
    public ApiTestException() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param msg
     */
    public ApiTestException(String msg) {
        super(msg);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param e
     */
    public ApiTestException(Throwable e) {
        super(e);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param msg
     * @param cause
     */
    public ApiTestException(String msg, Throwable cause) {
        super(msg, cause);
        // TODO Auto-generated constructor stub
    }

}
