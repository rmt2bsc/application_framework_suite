package com;

import java.util.List;

import com.api.config.AppPropertyPool;
import com.api.config.ConfigConstants;
import com.util.RMT2String;
import com.util.RMT2Utility;

/**
 * The class RMT2Exception and its subclasses are a form of Exception that
 * indicates conditions that a reasonable application might want to catch.
 * 
 * @author roy.terrell
 * 
 */
public class RMT2Exception extends Exception {
    private static final long serialVersionUID = 4879374016844141550L;

    private static final String ENV = AppPropertyPool
            .getProperty(ConfigConstants.PROPNAME_ENVIRONMENT);

    // private DatabaseConnectionBean con;

    protected final int FAILURE = -1;

    protected final int SUCCESS = 1;

    protected final String DELIM = "%s";

    protected String objName;

    protected String methodName;

    protected int errorCode;

    protected String errorMsg;

    protected String className;

    protected List msgArgs;

    /**
     * Default constructor
     * 
     */
    public RMT2Exception() {
        this.className = this.getClass().getName();
    }

    /**
     * Constructor which initializes the error message
     * 
     * @param msg
     *            Error message
     */
    public RMT2Exception(String msg) {
        this();
        this.errorMsg = msg;
    }

    /**
     * Constructor which initializes the error code
     * 
     * @param code
     *            Error code
     */
    public RMT2Exception(int code) {
        this();
        this.errorCode = code;
    }

    // /**
    // * Constructor which obtains the error message from the Database using the
    // * error code. _con is expected to be of type DatabaseConnectionBean.
    // _args
    // * contains an array of String argument values that will replace "%s"
    // * placeholders in the message text retrieved from the database using
    // _code.
    // *
    // * @param _con
    // * A connection object to a data source provider.
    // * @param _code
    // * The error code
    // * @param _args
    // * A list of messages to substitute the place holders.
    // */
    // public RMT2Exception(Object _con, int _code, List _args) {
    // this(_code);
    // String msg;
    //
    // this.msgArgs = _args;
    // msg = this.getDBMessage(_con, _code);
    // if (msg != null) {
    // this.setErrorMessage(msg);
    // }
    // else {
    // this.setErrorMessage("An invalid RMT2DBConnection object was passed to RMT2Exception(Object _con, int _code, String _objName, String _methodName)");
    // }
    //
    // if (_args != null && _args.size() > 0) {
    // this.fillMsgArgs();
    // }
    // this.setErrorCode(_code);
    // }

    /**
     * Constructor which initializes the error message and error code,
     * 
     * @param _msg
     *            Error message
     * @param _code
     *            Error code
     */
    public RMT2Exception(String _msg, int _code) {
        this();
        this.setErrorMessage(_msg);
        this.setErrorCode(_code);
    }

    /**
     * Constructor which initializes the error message and error code object
     * name and method name.
     * 
     * @param _msg
     *            The error message
     * @param _code
     *            The error code
     * @param _objName
     *            The name of originating class.
     * @param _methodName
     *            The name of the orginating method.
     */
    public RMT2Exception(String _msg, int _code, String _objName,
            String _methodName) {
        this(_msg, _code);
        this.objName = (_objName == null ? "Unknown" : _objName);
        this.methodName = (_methodName == null ? "Unknown" : _methodName);
        this.setErrorMessage(_msg);
        this.setErrorCode(_code);
    }

    /**
     * Constructor which initializes the error message with the input of the
     * message value from the argument of type Exception. If the value of the
     * message is null set the error message to be class name of the Exception
     * "e".
     * 
     * @param e
     */
    public RMT2Exception(Exception e) {
        super(e);
        this.errorMsg = e.getMessage();
        if (this.errorMsg == null) {
            this.errorMsg = "Original Exception runtime type is unknown";
        }
        if (e instanceof RMT2Exception) {
            this.errorCode = ((RMT2Exception) e).getErrorCode();
        }
    }

    /**
     * 
     * @param e
     */
    public RMT2Exception(Throwable e) {
        super(e);
    }

    /**
     * Creates a new RMT2Exception with a the specified message and the causing
     * throwable instance.
     * 
     * @param msg
     *            the message that explains the error.
     * @param cause
     *            the cause (which is saved for later retrieval by the
     *            Throwable.getCause() method). (A null value is permitted, and
     *            indicates that the cause is nonexistent or unknown.)
     * 
     */
    public RMT2Exception(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Obtains the environment value.
     * 
     * @return String
     */
    public static final String getEnvironment() {
        return RMT2Exception.ENV;
    }

    // /**
    // * Retrieves the error message associated with the error code, _code, from
    // * the database and returns the message to the caller. If the message is
    // not
    // * found, then "Unknown Error" is returned to the caller. If an an
    // exception
    // * occurs notify caller that message is not obtainable due to system
    // * exception. _con must be a valid DatabaseConnectionBean. Otherwise,
    // return
    // * null to the caller signaling that the connectgion object is invalid.
    // *
    // * @param _con
    // * The connection object to a data source provider.
    // * @param _code
    // * The error code.
    // * @return the error message
    // */
    // private String getDBMessage(Object _con, int _code) {
    //
    // String msg = null;
    //
    // // Validate _con
    // if (_con instanceof DatabaseConnectionBean) {
    // this.con = (DatabaseConnectionBean) _con;
    // }
    // else {
    // return null;
    // }
    //
    // try {
    // DataSourceApi api = DataSourceFactory.create(this.con,
    // "SystemMessageDetailView");
    // api.setDatasourceSql(DbSqlConst.WHERE_KEY, null);
    // api.setDatasourceSql(DbSqlConst.WHERE_KEY, "id = " + _code);
    // api.executeQuery();
    //
    // while (api.nextRow()) {
    // msg = api.getColumnValue("Description");
    // }
    //
    // return (msg == null ? "Unknown Error" : msg);
    // } catch (Exception e) {
    // return e.getMessage();
    // }
    // }

    /**
     * Constructs the error message by concatenating the member variables:
     * className, objName, methodName, and errorCode provided errorCode is less
     * than zero.
     * 
     * @return Formatted error message
     */
    public String getFormatedMsg() {
        StringBuffer msg = new StringBuffer(500);
        msg.append("Exception Type: ");
        msg.append(this.getClassName());
        msg.append("  Class: ");
        msg.append(this.objName);
        msg.append("  Method: ");
        msg.append(this.methodName);

        if (this.errorCode < 0) {
            msg.append("  Error Code: ");
            msg.append(this.errorCode);
        }

        msg.append("  Message: ");
        msg.append(this.errorMsg);

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
        msg.append(this.getClass().getName());
        msg.append("<br>");
        msg.append("Error Message: ");
        msg.append(this.errorMsg == null ? "Unkown Message Description"
                : this.errorMsg);
        msg.append("<br>");
        msg.append("Error Code: ");
        msg.append(this.errorCode == 0 ? "Error Code Unknown" : this.errorCode);
        return msg.toString();
    }

    /**
     * Packages the RMT2ExceptionBean with error message, error code and class
     * Name.
     * 
     * @return {@link RMT2ExceptionBean}
     */
    public RMT2ExceptionBean getExceptionBean() {

        String exName = this.getClassName();
        try {
            RMT2ExceptionBean e = new RMT2ExceptionBean();
            e.setErrorMsg(this.errorMsg);
            e.setErrorCode(this.errorCode);
            // e.setClassName(exName);
            if (exName.equalsIgnoreCase("SystemException")) {
                e.setResolution("Consult System Administrator or Developer");
            }
            if (exName.equalsIgnoreCase("DatabaseException")) {
                e.setResolution("Go back and adjust your actions based on the error details shown in the \"What Happened\" section.   If error persist consult System Administrator or Developer");
            }
            if (exName.equalsIgnoreCase("NotFoundException")) {
                e.setResolution("Go back and refine search criteria");
            }
            if (exName.equalsIgnoreCase("BusinessException")) {
                e.setResolution("Go back and correct the error.");
            }
            if (exName.equalsIgnoreCase("LoginException")) {
                e.setResolution("Go back and enter the correct user id and/or password.");
            }
            return e;
        } catch (SystemException e) {
            return null;
        }
    }

    /**
     * Obtains the exception data from the database based on "_errorId. Packages
     * the RMT2ExceptionBean with exception data.
     * 
     * @param _errorId
     *            THe error code
     * @return {@link RMT2ExceptionBean}
     */
    public RMT2ExceptionBean getExceptionBean(int _errorId) {

        try {
            // TODO: Create RMT2ExceptionBeanView.xml document.
            // Retrieve exception data from the database via xml document
            // Populate exception bean with data retrieved from the database
            // Return exception bean.
            RMT2ExceptionBean e = new RMT2ExceptionBean();
            return e;
        } catch (SystemException e) {
            return null;
        }

    }

    /**
     * Packages the RMT2ExceptionBean with error message, error code and class
     * Name.
     * 
     * @param _className
     *            The name of the class to associate with the exception bean.
     * @return {@link RMT2ExceptionBean}
     */
    public RMT2ExceptionBean getExceptionBean(String _className) {
        this.className = _className;
        return this.getExceptionBean();
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
            ndx = this.className.lastIndexOf(".");
            if (ndx == this.FAILURE) {
                if (this.className.length() > 0) {
                    return this.className;
                }
                return "Class Unknown";
            }
            value = this.className.substring(++ndx);
            return value;
        } catch (NullPointerException e) {
            return "Class is Null or Invalid";
        } catch (IndexOutOfBoundsException e) {
            return "Class Not Parsed";
        }
    }

    /**
     * Fills in the place holders in this.errorMsg with values contained in the
     * Message Argument ArrayList variable, msgArgs.
     * 
     */
    protected void fillMsgArgs() {
        String arg = null;
        String temp;
        int count = this.msgArgs.size();

        try {
            temp = this.errorMsg;
            for (int ndx = 0; ndx < count; ndx++) {
                arg = (String) this.msgArgs.get(ndx);
                temp = RMT2String.replace(temp, arg, this.DELIM);
            }
            this.errorMsg = temp;
            this.msgArgs.clear();
        } catch (IndexOutOfBoundsException e) {
            this.errorMsg = "Failure reading Message Argument structure - exceeded array bounds";
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
     * Get message.
     * 
     * @return error message
     */
    public String getMessage() {
        return this.errorMsg;
    }

    /**
     * Returns exception as XML containing error code and error message. The
     * message format goes as follows:
     * <p>
     * 
     * <pre>
     * &lt;error&gt;
     *    &lt;code&gt;numeric value&lt;/code&gt;
     *    &lt;message&gt;message text&lt;/message&gt;
     * &lt;/error&gt;
     * </pre>
     * 
     * @return
     */
    public String getMessageAsXml() {
        StringBuffer msg = new StringBuffer(100);
        msg.append("<error>");
        msg.append("<code>");
        msg.append(this.errorCode);
        msg.append("</code>");
        msg.append("<message>");
        msg.append(this.errorMsg);
        msg.append("</message>");
        msg.append("</error>");
        return msg.toString();
    }

    /**
     * Get error code
     * 
     * @return THe error code
     */
    public int getErrorCode() {
        return this.errorCode;
    }

    /**
     * Set error message.
     * 
     * @param value
     *            message
     */
    public void setErrorMessage(String value) {
        this.errorMsg = value;
    }

    /**
     * Set error code.
     * 
     * @param value
     *            error code
     */
    public void setErrorCode(int value) {
        this.errorCode = value;
    }

    /**
     * Get the fully gualified name of class that is in error.
     * 
     * @return class name
     */
    public String getFullClassName() {
        return this.className;
    }

    /**
     * Set exception object name.
     * 
     * @return The name of the object.
     */
    public String setObjName() {
        return this.objName;
    }

    /**
     * Set the name of the method in error
     * 
     * @return method name
     */
    public String setMethodName() {
        return this.methodName;
    }

    /**
     * Set one or more messages to this exception.
     * 
     * @param value
     *            ArrayList of String messages.
     */
    public void setMsgArgs(List value) {
        this.msgArgs = value;
    }
}
