package com.api.security.authentication.web;

/**
 * Authentication module constants.
 * 
 * @author roy.terrell
 * 
 */
public class AuthenticationConst {
    /** The tag name associated with the user's login id */
    public static final String AUTH_PROP_USERID = "UID";

    /** The tag name associated with the user's password */
    public static final String AUTH_PROP_PASSWORD = "PW";

    /** The tag name associated with the user's session id */
    public static final String AUTH_PROP_SESSIONID = "sessionid";

    /**
     * The tag name associated with the name of the application the user used to
     * signed on to the system. This code can be found in SystemParms.properties
     */
    public static final String AUTH_PROP_MAINAPP = "appcode";

    /** Used to identify a generic or fake user */
    public static final String FAKE_USER = "!@#$%^&*";

    /** Default session time out value */
    public static final int DEFAULT_TIMEOUT = 3600;

    /** Minimum session time out value */
    public static final int MIN_TIMEOUT = 600;

}