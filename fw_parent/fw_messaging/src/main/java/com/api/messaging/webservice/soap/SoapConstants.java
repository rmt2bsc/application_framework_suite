package com.api.messaging.webservice.soap;


/**
 * Constants to be used for SOAP message API.
 * 
 * @author rterrell
 * 
 */
public class SoapConstants {

    /**
     * The id of the SOAP message which its value is usually <i>serviceId</i>.
     */
    public static final String SERVICEID_NAME = "serviceId";

    /**
     * The name of command assoicated with a SOAP message which its value is
     * usually <i>target_action</i>.
     */
    public static final String COMMAND_NAME = "target_action";

    /**
     * The return code representing a successful SOAP message invocation.
     */
    public static final int RETURNCODE_SUCCESS = 0;

    /**
     * The return code representing a failed SOAP message invocation.
     */
    public static final int RETURNCODE_FAILURE = -1;

    // IS-70: Added HTTP status codes.
    /**
     * 
     */
    public static final String RETURN_STATUS_SUCCESS = "200";

    /**
     * 
     */
    public static final String RETURN_STATUS_BAD_REQUEST = "400";

    // IS-70: Created SOAP fault key constant values.
    public static final String SOAP_FAULT_KEY_VERMISMATCH = "VersionMisMatch";
    public static final String SOAP_FAULT_KEY_MUSTUNDERSTAND = "MustUnderstand";
    public static final String SOAP_FAULT_KEY_CLIENT = "Client";
    public static final String SOAP_FAULT_KEY_SERVER = "Server";

    /**
     * Create SoapConstants object
     */
    public SoapConstants() {
        return;
    }

}
