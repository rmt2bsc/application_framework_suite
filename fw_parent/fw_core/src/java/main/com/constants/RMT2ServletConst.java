package com.constants;

/**
 * Class that contains servlet constants.
 * 
 * @author roy.terrell
 * 
 */
public class RMT2ServletConst {
    /**
     * The application mapping file key that is declared in SysParams.properties
     */
    public static final String CONFIG_FILE_KEY = "ConfigFileKey";

    /** Login action handler constant */
    public static final String LOGIN_ACTION = "Login";

    /** Remote Login action handler constant */
    public static final String REMOTE_LOGIN_ACTION = "remotelogin";

    /** Remote Services collection */
    public static final String REMOTE_SERVICES = "REMOTESERVICES";

    /** Remote Services Type collection */
    public static final String REMOTE_SERVICES_TYPE = "REMOTESERVICESTYPE";

    /** Database connection pool identifier */
    public static final String DB_CONNECTIONS = "CONNECTIONS";

    /** Database connection bean identifier */
    public static final String DB_CONNECTION_BEAN = "DB_CONNECTION_BEAN";

    /** Database connection count identifier */
    public static final String DBCOUNT = "DBCOUNT";

    /** User's session bean identifier */
    public static final String SESSION_BEAN = "SESSION_BEAN";

    /** User's query bean identifier */
    public static final String QUERY_BEAN = "QUERY_BEAN";

    /** User's report bean identifier */
    public static final String REPORT_BEAN = "REPORT_BEAN";

    /** User defined application properties identifier. */
    public static final String APP_PROPERTIES = "APP_PROPERTIES";

    /** Idnetifier for "User not logged in" message */
    public static final String USER_NOT_LOGGED_IN = "User is not logged into the system";

    /** Search criteria identifier */
    public static final String SEARCH_CRITERIA = "SearchCriteria";

    /** Code for "user not logged in" */
    public static final int RC_USER_NOT_LOGGED_IN = -20;

    /** Menu type "Top" code */
    public static final int MENUTYPE_TOP = 1;

    /** Menu type "Side" code */
    public static final int MENUTYPE_SIDE = 2;

    /** JSP Transasctin origin code */
    public static final String ORIGIN_TRANSACT = "xact";

    /** JSP Accounting origin code */
    public static final String ORIGIN_ACCOUNTING = "acct";

    /** Code used as an indicator to hide wait page */
    public static final int WAITPAGE_HIDE = 1;

    /** Code used as an indicator to show wait page */
    public static final int WAITPAGE_SHOW = 0;

    /** New search mode code */
    public static final int SEARCH_MODE_NEW = 1;

    /** Old search mode code */
    public static final int SEARCH_MODE_OLD = 2;

    /**
     * HEADER property which is used as the key to the key/value pair of a
     * Property object that manages messages for the
     * {@link AbstractCommandController}
     */
    public static final String PROPERTY_MSG_HEADER = "HEADER";

    /**
     * INFO property which is used as the key to the key/value pair of a
     * Property object that manages messages for the
     * {@link AbstractCommandController}
     */
    public static final String PROPERTY_MSG_INFO = "INFO";

    /**
     * ERROR property which is used as the key to the key/value pair of a
     * Property object that manages messages for the
     * {@link AbstractCommandController}
     */
    public static final String PROPERTY_MSG_ERROR = "ERROR";

    /**
     * info attribute which is used as an attribute to the key/value pair of the
     * HttpServletRequest object that manages messages for the
     * {@link AbstractCommandController}
     */
    public static final String REQUEST_MSG_INFO = "info";

    /**
     * messages attribute which is used as an attribute to the key/value pair of
     * the HttpServletRequest object that manages messages for the
     * {@link AbstractCommandController}
     */
    public static final String REQUEST_MSG_MESSAGES = "messages";

    /**
     * Code that is associated with a response URL that is to be redirected
     * after an action handler has completed.
     */
    public static final String RESPONSE_HREF = "delayedresponseurl";

    /**
     * Serves as an Indicator that the URL is absolute and prefixed with a
     * protocol
     */
    public static final String RESPONSE_PROTOCOL = "responseprotocol";

    /**
     * The data that is to be streamed directly to the client via the user's
     * Response instance
     */
    public static final String RESPONSE_NONJSP_DATA = "nonjspdata";

    /** Code indicating that the response URL is governed by the http */
    public static final int RESPONSE_PROTOCOL_HTTP = 1000;

    /** Code indicating that the response URL is governed by the ftp */
    public static final int RESPONSE_PROTOCOL_FTP = 1010;

    /** Code indicating that the response URL is governed by the mailto */
    public static final int RESPONSE_PROTOCOL_MAILTO = 1020;

    /**
     * Code indicating that the response URL is relative and the protocol is not
     * indicated
     */
    public static final int RESPONSE_PROTOCOL_NORMAL = 0;

    /** GLobal User id attribute name */
    public static final String GLOBAL_USER = "globaluserid";

    /** GLobal Session id attribute name */
    public static final String GLOBAL_SESSION = "globalsessionid";

}