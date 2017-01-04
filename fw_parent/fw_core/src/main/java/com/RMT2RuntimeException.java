package com;

import com.api.config.AppPropertyPool;
import com.api.config.ConfigConstants;
import com.util.RMT2Utility;

/**
 * This class is an extension of RuntimeException that does not require the
 * declaration of a throws clause or try/catch blocks for methods that throw
 * this kind of exception.
 * 
 * @author roy.terrell
 * 
 */
public class RMT2RuntimeException extends RuntimeException {
    private static final long serialVersionUID = 4879374016844141550L;

    private static final String ENV = AppPropertyPool
            .getProperty(ConfigConstants.PROPNAME_ENVIRONMENT);

    protected final int FAILURE = -1;

    protected final int SUCCESS = 1;

    protected int errorCode;

    /**
     * Constructs a new RMT2RuntimeException with null as its detail message.
     * 
     */
    public RMT2RuntimeException() {
        super();
    }

    /**
     * Constructs a new RMT2RuntimeException exception with the specified detail
     * message.
     * 
     * @param msg
     *            Error message
     */
    public RMT2RuntimeException(String msg) {
        super(msg);
    }

    public RMT2RuntimeException(String msg, int errorCode) {
        this(msg);
        this.errorCode = errorCode;
    }

    /**
     * Constructs a new RMT2RuntimeException exception with the specified detail
     * message and a throwable object.
     * 
     * @param e
     */
    public RMT2RuntimeException(String msg, Throwable e) {
        super(msg, e);
    }

    /**
     * Constructs a new RMT2RuntimeException exception with the specified cause.
     * 
     * @param e
     */
    public RMT2RuntimeException(Throwable e) {
        super(e);
    }

    /**
     * Obtains the environment value.
     * 
     * @return String
     */
    public static final String getEnvironment() {
        return RMT2RuntimeException.ENV;
    }

    /**
     * Constructs the error message by concatenating the member variables:
     * className, objName, methodName, and errorCode provided errorCode is less
     * than zero.
     * 
     * @return Formatted error message
     */
    public String getFormatedMsg() {
        StringBuffer msg = new StringBuffer(500);
        msg.append("  Message: ");
        msg.append(this.getMessage());
        return msg.toString();
    }

    /**
     * Formats the error message as HTML.
     * 
     * @return HTML Message.
     */
    public String getHtmlMessage() {

        StringBuffer msg = new StringBuffer(500);
        msg.append("Exception Type: ");
        msg.append(this.getClassName());
        msg.append("<br>");
        msg.append("Error Message: ");
        msg.append(this.getMessage() == null ? "Unkown Message Description"
                : this.getMessage());
        return msg.toString();
    }

    /**
     * Extracts the actual class name from the fully qualified class name value,
     * this.className.
     * 
     * @return The name of the class in error.
     */
    public String getClassName() {
        int ndx = 0;
        String value;

        try {
            ndx = this.getClass().getName().lastIndexOf(".");
            if (ndx == this.FAILURE) {
                if (this.getClass().getName().length() > 0) {
                    return this.getClass().getName();
                }
                return "Class Unknown";
            }
            value = this.getClass().getName().substring(++ndx);
            return value;
        } catch (NullPointerException e) {
            return "Class is Null or Invalid";
        } catch (IndexOutOfBoundsException e) {
            return "Class Not Parsed";
        }
    }

    /**
     * Obtains the stack trace and converts and concatenates each
     * StackTraceElement into a String.
     * 
     * @return String derived from combined StackTraceElements
     */
    public String getErrorStack() {
        return RMT2Utility.getStackTrace(this);
    }

    /**
     * Returns exception as XML containing error code and error message. The
     * message format goes as follows:
     * <p>
     * 
     * <pre>
     * &lt;error&gt;
     *    &lt;message&gt;message text&lt;/message&gt;
     * &lt;/error&gt;
     * </pre>
     * 
     * @return
     */
    public String getMessageAsXml() {
        StringBuffer msg = new StringBuffer(100);
        msg.append("<error>");
        msg.append("<message>");
        msg.append(this.getMessage());
        msg.append("</message>");
        msg.append("</error>");
        return msg.toString();
    }

    /**
     * @return the errorCode
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * @param errorCode
     *            the errorCode to set
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

}
