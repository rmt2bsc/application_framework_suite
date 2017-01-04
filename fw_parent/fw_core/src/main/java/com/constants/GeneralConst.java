package com.constants;

/**
 * This class offers common constants
 * 
 * @author appdev
 * 
 */
public class GeneralConst {
    /** Code that indicates user is logged into the system */
    public static final int USER_LOGGEDIN = 100;

    /** Code that indicates user is logged out of the system */
    public static final int USER_LOGGEDOUT = -100;

    /** Operation return code for success */
    public static final int RC_SUCCESS = 1;

    /** Operatio return code for failure */
    public static final int RC_FAILURE = -1;

    /** One character space constant */
    public static final String SPACE = " ";

    /**
     * Typical property name of the client input control used to store and track
     * the row number for a lists of data
     */
    public static final String CLIENTROW_PROPERTY = "selCbx";

    /**
     * Property that identifies the user's requested command as translated by
     * the server
     */
    public static final String REQ_COMMAND = "command";

    /**
     * Property that identifies the user's requested command as translated by
     * the client
     */
    public static final String REQ_CLIENTACTION = "clientAction";

    /** List action code */
    public static final String REQ_LIST = "list";

    /** Edit action code */
    public static final String REQ_EDIT = "edit";

    /** Edit action code */
    public static final String REQ_VIEW = "view";

    /** Add action code */
    public static final String REQ_ADD = "add";

    /** Delete action code */
    public static final String REQ_DELETE = "delete";

    /** Save action code */
    public static final String REQ_SAVE = "save";

    /** Go Back action code */
    public static final String REQ_BACK = "back";

    /** Next page action code */
    public static final String REQ_NEXT = "next";

    /** Get Details action code */
    public static final String REQ_DETAILS = "details";

    /** Search action code */
    public static final String REQ_SEARCH = "search";

    /** New Search action code */
    public static final String REQ_NEWSEARCH = "newsearch";

    /** Old Search action code */
    public static final String REQ_OLDSEARCH = "oldsearch";

    /** Cancel action code */
    public static final String REQ_CANCEL = "cancel";

    /** Print action code */
    public static final String REQ_PRINT = "print";

    /** Print action code */
    public static final String REQ_RESET = "reset";

    /** Service Id action code */
    public static final String REQ_SERVICEID = "serviceId";

    public static final String REQ_SOAPMESSAGE = "payload";

    /**
     * HttpServletRequest key attribute that is mappedg to the data used by
     * target JSP page
     */
    public static final String REQ_ATTRIB_DATA = "data";

    /** General constant to represent Search criteria arguments */
    public static final String CLIENT_DATA_CRITERIA = "criteriadata";

    /** General constant to represent list data */
    public static final String CLIENT_DATA_LIST = "listdata";

    /** General constant to represent a single record */
    public static final String CLIENT_DATA_RECORD = "record";

    /** General constant to represent Person data */
    public static final String CLIENT_DATA_PERSON = "person";

    /** General constant to represent Business data */
    public static final String CLIENT_DATA_BUSINESS = "business";

    /** General constant to represent Address data */
    public static final String CLIENT_DATA_ADDERSS = "address";

    /** General constant to represent Zipcode data */
    public static final String CLIENT_DATA_ZIP = "zip";

    /** Indicates whether or not SOAP XML should use pretty formatting */
    public static final boolean FORMAT_SOAP_XML = false;

    /**
     * Declares the application that is responsible for authenticating users for
     * the entire system by its application code.
     */
    public static final String SECURITY_APP_CODE = "authentication";

    public static String NAMESPACES_RESOURCES = "com.api.xml.RMT2NamespaceContext";

}