/**
 * 
 */
package com.api.config;

/**
 * Conatins system configuarion related constants class variables.
 * 
 * @author Roy Terrell
 * 
 */
public class ConfigConstants {
    public static final String JAXB_CONFIG_PKG = "com.api.config.jaxb";
    public static String JAXB_CONTEXNAME_CONFIG = "CONFIG";
    public static String JAXB_CONTEXNAME_RMT2 = "RMT2";
    public static String JAXB_CONTEXNAME_DEFAULT = "RMT2";

    /** Environment value for Development */
    public static String ENVTYPE_DEV = "DEV";

    /** Environment value for Test */
    public static String ENVTYPE_TEST = "TEST";

    /** Environment value for Staging */
    public static String ENVTYPE_STAGE = "STAGE";

    /** Environment value for Production */
    public static String ENVTYPE_PROD = "PROD";

    /** The name of the system ResourceBundle */
    public static final String SYSTEM_CONFIG_PARMS = "SystemConfigParms";
    /** The name of the application ResourceBundle */
    public static final String CONFIG_APP = "config.AppParms";
    /** The key name that identifies the path of an api's logger configuration */
    public static final String API_LOGGER_CONFIG_PATH_KEY = "logger_config_path";
    public static final String API_APP_TITLE_KEY = "apptitle";
    public static final String API_APP_CODE_KEY = "appcode";
    public static final String API_APP_MODULE_VALUE = "unknown";

    /** Envirionment variable name */
    public static String PROPNAME_ENVIRONMENT = "ENVIRONMENT";
    /** Envirionment variable name abreviation */
    public static String PROPNAME_ENV = "env";
    /** Envirionment variable name */
    public static String PROPNAME_DEFAULT_APP_CTX_NAME = "defaultWebAppCtxRootDirName";

    /** Configuration location variable name */
    public static String PROPNAME_CFG_LOC = "appConfigPath";
    /** Application configuration path location variable name */
    public static String PROPNAME_CONFIG_APP = "CONFIG_APP";
    /** Application's AppParms.properties file location variable name */
    public static String PROPNAME_APPPARMS_LOCATION = "appParms_location";
    /** Application code name variable */
    public static String PROPNAME_APP_NAME = "APP_NAME";
    /** Web Server variable name */
    public static String PROPNAME_SERVER = "server";
    /** DBMS Vendor code variable name */
    public static String PROPNAME_DBMS_VENDOR = "dbmsVendor";
    /** Services application variable name */
    public static String PROPNAME_SERVICE_APP = "services_app";
    /**
     * Variable name used to identify the servlet designated for the services
     * application.
     */
    public static String PROPNAME_SERVICE_SERVLET = "services_servlet";
    /** Variable name used to identify the host of the service application. */
    public static String PROPNAME_SERVICELOAD_HOST = "loadServicesHost";
    /** Variable name used to identify the command to query all services. */
    public static String PROPNAME_SERVICELOAD_COMMAND = "loadServicesModule";
    /** Variable name used to identify the service id to query all services. */
    public static String PROPNAME_SERVICELOAD_SERVICEID = "loadServicesId";
    /** Default database connections variable name */
    public static String PROPNAME_DEF_CONN = "defaultconnections";
    /** Maximum number of database connections variable name */
    public static String PROPNAME_MAX_CONN = "maxconnections";
    /** Session time out interval variable name */
    public static String PROPNAME_SESSION_TIMEOUT = "timeoutInterval";
    /** Serializatin drive letter variable name */
    public static String PROPNAME_SERIAL_DRIVE = "serialDrive";
    /** Serialization path variable name */
    public static String PROPNAME_SERIAL_PATH = "serialPath";
    /** Serialization file name extension variable name */
    public static String PROPNAME_SERIAL_EXT = "serialExt";
    /** SAX driver variable name */
    public static String PROPNAME_SAX_DRIVER = "SaxDriver";
    /** XML Document class variable name */
    public static String PROPNAME_DOC_CLASS = "docClass";
    /** JSP Polling page variable name */
    public static String PROPNAME_POLL_PAGE = "pollingPage";
    /** Web application mapping configuration variable name */
    public static String PROPNAME_APP_MAPPING = "webAppMapping";
    /** Web application mapping configuration path variable name */
    public static String PROPNAME_APP_MAPPING_PATH = "web_app_mapping_path";
    /** Report XSLT output path variable name */
    public static String PROPNAME_XSLT_PATH = "rpt_xslt_path";
    /** Report output file extension variable name */
    public static String PROPNAME_RPT_FILE_EXT = "rpt_file_ext";
    /** Images directory variable name */
    public static String PROPNAME_IMG_DIR = "imageDir";
    /** Encryption cycle count variable name */
    public static String PROPNAME_ENCRYPT_CYCLE = "encryptCycle";
    /** SMTP mail authentication variable name */
    public static String PROPNAME_MAIL_AUTH = "mailAuthentication";
    /** SMTP password variable name */
    public static String PROPNAME_MAIL_PW = "mailPassword";
    /** SMTP user id variable name */
    public static String PROPNAME_MAIL_UID = "mailUserId";
    /** POP server variable name */
    public static String PROPNAME_POP_SERVER = "mailHostPop3";
    /** SMTP Server variable name */
    public static String PROPNAME_SMTP_SERVER = "mailHostSmtp";
    /** INternal SMTP Domain variable name */
    public static String PROPNAME_INTERNAL_SMTP_DOMAIN = "mailInternalSmtpDomain";
    /** The name that points to the datasource directory */
    public static String PROPNAME_DATASOURCE_DIR = "datasource_dir";
    /** Web drive */
    public static String PROPNAME_WEB_DRIVE = "webAppsDrive";
    /** web directory */
    public static String PROPNAME_WEB_DIR = "webAppsDir";
    /**
     * The name of the attribute that indicates whether user atuhentication is
     * performed locally or remotely.
     */
    public static String PROPNAME_AUTHLOCALE = "user_auth_locale";
    /** The name of the attribute that indicates the SOAP Host. */
    public static String SOAP_HOST = "soaphost";
    /**
     * The name of the attribute that indicates whether or not the SOAP parser
     * is namespace aware.
     */
    public static String SOAP_NAMESPACE_AWARE = "soapNameSpaceAware";
    /**
     * The name of the authentication source for logging users in to the system.
     * 
     */
    public static String PROPNAME_LOGINSRC = "user_login_source";
    /**
     * The name of the authentication source for logging users out of the
     * system.
     * 
     */
    public static String PROPNAME_LOGOUTSRC = "user_logout_source";
    /** The name of the ORM pagination page size indicator */
    public static String PROPNAME_ORM_PAGE_SIZE = "pagination_page_size";
    /** The name of the ORM maximum pagination links total indicator */
    public static String PROPNAME_ORM_PAGE_LINK_TOTAL = "page_link_max";
    /**
     * The name of the authentication source for logging users in/out of the
     * system.
     */
    public static String PROPNAME_AUTHENTICATOR = "AUTHENTICATOR";

    public static String PROPNAME_SERVICE_REGISTRY = "serviceRegistry";

    public static String PROPNAME_SOAP_HOST = "soaphost";

    public static String PROPNAME_SOAP_NAMESPACE_AWARE = "soapNameSpaceAware";

    /** Company text description */
    public static String OWNER_TEXT = "owner.companyTxt";
    public static String OWNER_NAME = "owner.Name";
    public static String OWNER_CONTACT = "owner.Contact";
    public static String OWNER_ADDR1 = "owner.Address1";
    public static String OWNER_ADDR2 = "owner.Address2";
    public static String OWNER_ADDR3 = "owner.Address3";
    public static String OWNER_ADDR4 = "owner.Address4";
    public static String OWNER_CITY = "owner.City";
    public static String OWNER_STATE = "owner.State";
    public static String OWNER_ZIP = "owner.Zip";
    public static String OWNER_PHONE = "owner.Phone";
    public static String OWNER_FAX = "owner.Fax";
    public static String OWNER_EMAIL = "owner.Email";
    public static String OWNER_WEBSITE = "owner.Website";
    public static String DBMSTYPE_ASA = "ASA";
    public static String DBMSTYPE_ASE = "ASE";
    public static String DBMSTYPE_ORACLE = "ORACLE";
    public static String DBMSTYPE_SQLSERVER = "SQLSRVR";
    public static String DBMSTYPE_DB2 = "DB2";
    public static String CONFIG_ROOT = "config";
    public static String CONFIG_APP_ROOT = "app";

}
