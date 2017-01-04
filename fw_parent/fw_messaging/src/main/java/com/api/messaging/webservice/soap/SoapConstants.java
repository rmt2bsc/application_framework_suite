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
    public static final int RETURNCODE_SUCCESS = 1;

    /**
     * The return code representing a failed SOAP message invocation.
     */
    public static final int RETURNCODE_FAILURE = -1;

    /**
     * Create SoapConstants object
     */
    public SoapConstants() {
        return;
    }

}
