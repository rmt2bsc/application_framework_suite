package com.api.messaging.webservice;

/**
 * Constants class for maintaining web service resource related values.
 * 
 * @author rterrell
 * 
 */
public class WebServiceConstants {
    
    /**
     * Successful return code.
     */
    public static final int RETURN_CODE_SUCCESS = 1;
    
    /**
     * Failure return code.
     */
    public static final int RETURN_CODE_FAILURE = -1;

    /**
     * 
     */
    public static final String MSG_TRANSPORT_MODE_SYNC = "SYNC";

    /**
     * 
     */
    public static final String MSG_MODE_REQUEST = "REQUEST";

    /**
     * 
     */
    public static final String MSG_MODE_RESPONSE = "RESPONSE";

    /**
     * 
     */
    public static final String MSG_TRANSPORT_MODE_ASYNC = "ASYNC";

    /**
     * 
     */
    public static final String RETURN_STATUS_SUCCESS = "200";

    /**
     * 
     */
    public static final String RETURN_STATUS_BAD_REQUEST = "400";
    
    /**
     * 
     */
    public static final String RETURN_STATUS_SERVER_ERROR = "500";

    /**
     * 
     */
    public static final String SERVICE_REGISTRY_SRC_KEY_NAME = "ServiceRegistrySource";

    /**
     * The value of web service resource type.
     */
    public static final int WEBSERV_VAL = 3;

    /**
     * The value of the RMI resource sub type.
     */
    public static final int WEBSERV_TYPE_RMI = 5;

    /**
     * The value of the HTTP resource sub type.
     */
    public static final int WEBSERV_TYPE_HTTP = 6;

    /**
     * The value of the SOAP resource sub type.
     */
    public static final int WEBSERV_TYPE_SOAP = 7;

    /**
     * The value of the REST resource sub type.
     */
    public static final int WEBSERV_TYPE_REST = 9;

    /**
     * The database column name representing the resource type id.
     */
    public static final String RSRC_TYPE_DBNAME = "rsrcTypeId";

    /*
     * The database column name representing the resource sub type id.
     */
    public static final String RSRC_SUBTYPE_DBNAME = "rsrcSubtypeId";

    /**
     * Message router type name for Hyper Text Transfer Protocol type
     * transporter.
     */
    public static final String MSG_ROUTER_TYPE_HTTP = "HTTP";
    /**
     * Message router type name for RAW type transporter.
     */
    public static final String MSG_ROUTER_TYPE_RAW = "RAW";
    /**
     * Message router type name for Common Object Model type transporter.
     */
    public static final String MSG_ROUTER_TYPE_COM = "COM";
    /**
     * Message router type name for DISH type transporter.
     */
    public static final String MSG_ROUTER_TYPE_DISH = "DISH";
    /**
     * Message router type name for Remote Method Invocation type transporter.
     */
    public static final String MSG_ROUTER_TYPE_RMI = "RMI";
    /**
     * Message router type name for XML type transporter.
     */
    public static final String MSG_ROUTER_TYPE_XML = "XML";
    /**
     * Message router type name for Simple Object Access Protocol type
     * transporter.
     */
    public static final String MSG_ROUTER_TYPE_SOAP = "SOAP";
    /**
     * Message router type name for Socket type transporter.
     */
    public static final String MSG_ROUTER_TYPE_SOCK = "SOCK";
    /**
     * Message router type name for JMS type transporter.
     */
    public static final String MSG_ROUTER_TYPE_JMS = "JMS";
    
    public static final String APPLICATION = "application";

    public static final String MODULE = "module";

    public static final String TRANSACTION = "transaction";

    public static final String ROUTING = "routing";

}
