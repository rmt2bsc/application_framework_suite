package com.api.persistence;

import com.RMT2RuntimeException;
import com.constants.RMT2SystemExceptionConst;

import com.util.RMT2String;

import java.util.List;

/**
 * This exception is thrown by classes that interact with the database whether
 * DML or QML type statements.
 * 
 * @author roy.terrell
 * 
 */
public class DatabaseException extends RMT2RuntimeException {
    private static final long serialVersionUID = -6755175657939760814L;

    private String altMessage;

    /**
     * Default constructor which its message is null.
     * 
     */
    public DatabaseException() {
        super();
    }

    /**
     * Constructs an DatabaseException object with a message.
     * 
     * @param msg
     *            The exception's message.
     */
    public DatabaseException(String msg) {
        super(msg);
        this.extractMessage(msg, "]");
    }

    /**
     * Constructs an DatabaseException object with an integer code and the
     * message is null.
     * 
     * @param code
     *            The exception code
     */
    // public DatabaseException(int code) {
    // super(code);
    // }
    /**
     * Constructs an DatabaseException object with a message and a code.
     * 
     * @param msg
     *            The exception message.
     * @param code
     *            The exception code.
     */
    public DatabaseException(String msg, int code) {
        super(msg, code);
        this.extractMessage(msg, "]");
    }

    /**
     * Constructs an DatabaseException object using a connection object, message
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
    // public DatabaseException(Object _con, int _code, ArrayList _args) {
    // super(_con, _code, _args);
    // }
    /**
     * Constructs an DatabaseException which the message will contain the
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
    // public DatabaseException(String msg, int code, String objname,
    // String methodname) {
    // super(msg, code, objname, methodname);
    // this.extractMessage(msg, "]");
    // }
    /**
     * Constructs an DatabaseException using an Exception object.
     * 
     * @param e
     *            Exception
     */
    public DatabaseException(Exception e) {
        super(e);
    }

    public DatabaseException(Throwable e) {
        super(e);
    }

    /**
     * Creates a new DatabaseException with a the specified message and the
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
    public DatabaseException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Try to identify a more user-friendly exception message regarding SQL
     * Server database errors.
     * 
     * @param e
     *            A DatabaseException
     * @return
     */
    public boolean checkForSQLServerError(DatabaseException e) {

        // SQL Server Referential integrity conflict
        if (e.getErrorCode() == RMT2SystemExceptionConst.RC_SQLSERVER_REF_INTEGRITY_CONFLICT) {

            // Attempting to delete a Code Details that exist in dependent
            // tables throughout the database
            e.setAltMessage(RMT2SystemExceptionConst.MSG_SQLSERVER_REF_INTEGRITY_CONFLICT);
            e.setErrorCode(RMT2SystemExceptionConst.RC_SQLSERVER_REF_INTEGRITY_CONFLICT);
            return true;
        }
        return false;
    }

    /**
     * Attempts to parse and format SQL Server proprietary error messages into a
     * more user friendly style. Most JDBC Drivers produced error messages
     * prefixes with formated like:
     * <p>
     * "[JDBC Mfg] [Connect for JDBC Driver] message" or "[JDBC Mfg] [Connect
     * for JDBC Driver] [Database] message".
     * <p>
     * <p>
     * This method will extract the primary message "message" and assign the
     * results to this.errorMsg.
     * 
     * @param msg
     * @param delim
     * @return 1 if message is extracted and assigned successfully; returns 0 if
     *         there was not a message to process; and returns -1 if an error
     *         occurs.
     */
    private int extractMessage(String msg, String delim) {
        List<String> list = RMT2String.getTokens(msg, delim);
        String temp;

        if (list != null && list.size() > 0) {
            try {
                // Primary message part should be positioned at the end of the
                // entire message.
                temp = (String) list.get(list.size() - 1);
                if (temp != null) {
                    this.altMessage = temp;
                    return 1;
                }
            } catch (IndexOutOfBoundsException e) {
                return -1;
            }
        }

        return 0;
    }

    /**
     * @return the altMessage
     */
    public String getAltMessage() {
        return altMessage;
    }

    /**
     * @param altMessage
     *            the altMessage to set
     */
    public void setAltMessage(String altMessage) {
        this.altMessage = altMessage;
    }
}
