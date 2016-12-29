package com.api.config.old;

import java.util.Properties;

import com.api.config.ConfigException;

/**
 * This interface declares the methods that must be implemented in order to
 * provide a centralized facility for an application to identify and access
 * properties. When implementing the init(Object) method, its generic argument
 * can be used as an initialization source for the process of instantiation.
 * 
 * @author RTerrell
 * @deprecated No longer needed
 */
public interface ResourceConfigurator {

    // /** The name of the system ResourceBundle */
    // public static final String SYSTEM_CONFIG_PARMS = "SystemParms";
    //
    // /** Envirionment variable name */
    // public static String PROPNAME_ENVIRONMENT = "env";
    //
    // /** Configuration location variable name */
    // public static String PROPNAME_CFG_LOC = "config_location";
    //
    // /** Application configuration path location variable name */
    // public static String PROPNAME_APP_CONFIG_PATH = "app_config_path";
    //
    // /** Application's AppParms.properties file location variable name */
    // public static String PROPNAME_APPPARMS_LOCATION = "appParms_location";
    //
    // /** Application code name variable */
    // public static String PROPNAME_APP_NAME = "app_name";
    //
    // /** Web Server variable name */
    // public static String PROPNAME_SERVER = "server";
    //
    // /** DBMS Vendor code variable name */
    // public static String PROPNAME_DBMS_VENDOR = "DBMSVendor";
    //
    // /** Services application variable name */
    // public static String PROPNAME_SERVICE_APP = "services_app";
    //
    // /** Variable name used to identify the servlet designated for the
    // services application. */
    // public static String PROPNAME_SERVICE_SERVLET = "services_servlet";
    //
    // /** Variable name used to identify the host of the service application.
    // */
    // public static String PROPNAME_SERVICELOAD_HOST = "loadservices_host";
    //
    // /** Variable name used to identify the command to query all services. */
    // public static String PROPNAME_SERVICELOAD_COMMAND =
    // "loadservices_module";
    //
    // /** Variable name used to identify the service id to query all services.
    // */
    // public static String PROPNAME_SERVICELOAD_SERVICEID = "loadservices_id";
    //
    // /** Default database connections variable name */
    // public static String PROPNAME_DEF_CONN = "defaultconnections";
    //
    // /** Maximum number of database connections variable name */
    // public static String PROPNAME_MAX_CONN = "maxconnections";
    //
    // /** Session time out interval variable name */
    // public static String PROPNAME_SESSION_TIMEOUT = "timeoutInterval";
    //
    // /** Serializatin drive letter variable name */
    // public static String PROPNAME_SERIAL_DRIVE = "serial_drive";
    //
    // /** Serialization path variable name */
    // public static String PROPNAME_SERIAL_PATH = "serial_path";
    //
    // /** Serialization file name extension variable name */
    // public static String PROPNAME_SERIAL_EXT = "serial_ext";
    //
    // /** SAX driver variable name */
    // public static String PROPNAME_SAX_DRIVER = "SAXDriver";
    //
    // /** XML Document class variable name */
    // public static String PROPNAME_DOC_CLASS = "docClass";
    //
    // /** JSP Polling page variable name */
    // public static String PROPNAME_POLL_PAGE = "polling_page";
    //
    // /** Web application mapping configuration variable name */
    // public static String PROPNAME_APP_MAPPING = "web_app_mapping";
    //
    // /** Web application mapping configuration path variable name */
    // public static String PROPNAME_APP_MAPPING_PATH = "web_app_mapping_path";
    //
    // /** Report XSLT output path variable name */
    // public static String PROPNAME_XSLT_PATH = "rpt_xslt_path";
    //
    // /** Report output file extension variable name */
    // public static String PROPNAME_RPT_FILE_EXT = "rpt_file_ext";
    //
    // /** Images directory variable name */
    // public static String PROPNAME_IMG_DIR = "image_dir";
    //
    // /** Encryption cycle count variable name */
    // public static String PROPNAME_ENCRYPT_CYCLE = "ENCRYPT_CYCLE";
    //
    // /** SMTP mail authentication variable name */
    // public static String PROPNAME_MAIL_AUTH = "mail.authentication";
    //
    // /** SMTP password variable name */
    // public static String PROPNAME_MAIL_PW = "mail.password";
    //
    // /** SMTP user id variable name */
    // public static String PROPNAME_MAIL_UID = "mail.userId";
    //
    // /** POP server variable name */
    // public static String PROPNAME_POP_SERVER = "mail.host.pop3";
    //
    // /** SMTP Server variable name */
    // public static String PROPNAME_SMTP_SERVER = "mail.host.smtp";
    //
    // /** INternal SMTP Domain variable name */
    // public static String PROPNAME_INTERNAL_SMTP_DOMAIN =
    // "mail.internal_smtp_domain";
    //
    // /** The name that points to the datasource directory */
    // public static String PROPNAME_DATASOURCE_DIR = "datasource_dir";
    //
    // /** Web drive */
    // public static String PROPNAME_WEB_DRIVE = "webapps_drive";
    //
    // /** web directory */
    // public static String PROPNAME_WEB_DIR = "webapps_dir";
    //
    // /** The name of the attribute that indicates whether user atuhentication
    // is performed locally or remotely. */
    // public static String PROPNAME_AUTHLOCALE = "user_auth_locale";
    //
    // /** The name of the attribute that indicates the SOAP Host. */
    // public static String SOAP_HOST = "soaphost";
    //
    // /** The name of the attribute that indicates whether or not the SOAP
    // parser is namespace aware. */
    // public static String SOAP_NAMESPACE_AWARE = "soapNameSpaceAware";
    //
    // /**
    // * The name of the authentication source for logging users in to the
    // system.
    // *
    // */
    // public static String PROPNAME_LOGINSRC = "user_login_source";
    //
    // /**
    // * The name of the authentication source for logging users out of the
    // system.
    // *
    // */
    // public static String PROPNAME_LOGOUTSRC = "user_logout_source";
    //
    // /** The name of the ORM pagination page size indicator */
    // public static String PROPNAME_ORM_PAGE_SIZE = "pagination_page_size";
    //
    // /** The name of the ORM maximum pagination links total indicator */
    // public static String PROPNAME_ORM_PAGE_LINK_TOTAL = "page_link_max";
    //
    // /** The name of the authentication source for logging users in/out of the
    // system. */
    // public static String PROPNAME_AUTHENTICATOR = "authenticator";
    //
    //
    // /** Company text description */
    // public static String OWNER_TEXT = "owner.companyTxt";
    //
    // public static String OWNER_NAME = "owner.Name";
    //
    // public static String OWNER_CONTACT = "owner.Contact";
    //
    // public static String OWNER_ADDR1 = "owner.Address1";
    //
    // public static String OWNER_ADDR2 = "owner.Address2";
    //
    // public static String OWNER_ADDR3 = "owner.Address3";
    //
    // public static String OWNER_ADDR4 = "owner.Address4";
    //
    // public static String OWNER_CITY = "owner.City";
    //
    // public static String OWNER_STATE = "owner.State";
    //
    // public static String OWNER_ZIP = "owner.Zip";
    //
    // public static String OWNER_PHONE = "owner.Phone";
    //
    // public static String OWNER_FAX = "owner.Fax";
    //
    // public static String OWNER_EMAIL = "owner.Email";
    //
    // public static String OWNER_WEBSITE = "owner.Website";
    //
    // public static String DBMSTYPE_ASA = "ASA";
    //
    // public static String DBMSTYPE_ASE = "ASE";
    //
    // public static String DBMSTYPE_ORACLE = "ORACLE";
    //
    // public static String DBMSTYPE_SQLSERVER = "SQLSRVR";
    //
    // public static String DBMSTYPE_DB2 = "DB2";

    /**
     * Commences the initialization of a ResourceConfigurator implementation.
     * 
     * @param initCtx
     *            An arbitrary object representing some form of data source.
     * @throws ConfigException
     */
    void init(Object initCtx) throws ConfigException;

    /**
     * Setup the properties to their respective entities.
     * 
     * @throws ConfigException
     * @deprecated No longer needed
     */
    void doSetup() throws ConfigException;

    /**
     * Perform post setup logic.
     * 
     * @param ctx
     *            An arbitrary object representing some form of data source.
     * @throws ConfigException
     * @deprecated No longer needed
     */
    void doPostSetup(Object ctx) throws ConfigException;

    /**
     * Get the value of the property named <i>key</i>.
     * 
     * @param key
     *            The property name.
     * @return String.
     */
    String getProperty(String key);

    /**
     * Get the environment indicator.
     * 
     * @return String.
     */
    String getEnv();

    /**
     * Gets the Application Properties object.
     * 
     * @return Properties.
     */
    Properties getAppProps();

    /**
     * Perform any clean up routines when deallocating an ResourceConfigurator
     * implementation.
     * 
     * @throws ConfigException
     */
    void destroy() throws ConfigException;
}