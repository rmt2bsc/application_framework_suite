package com.api.ldap.beans;

/**
 * ORM bean for mapping computer data as it relates to web servers coming from a
 * LDAP server.
 * 
 * @author Roy Terrell
 * 
 */
public class LdapComputerWeb extends LdapCommonComputer {

    private String remoteSrvcApp;

    private String remoteSrvcServlet;

    private String loadServicesHost;

    private String loadServicesModule;

    private String loadServicesId;

    private String defaultconnections;

    private String maxconnections;

    private String timeoutInterval;

    private String serialDrive;

    private String serialPath;

    private String serialExt;

    private String SaxDriver;

    private String pollingPage;

    private String webAppMapping;

    private String rptXsltPath;

    private String rptFileExt;

    private String imageDir;

    private String encryptCycle;

    private String mailHostSmtp;

    private String mailHostPop3;

    private String mailAuthentication;

    private String mailUserId;

    private String mailPassword;

    private String mailDefaultcontent;

    private String mailTemplatepath;

    private String mailInternalSmtpDomain;

    private String mailResourcetype;

    private String jmsJndisource;

    private String jmsContextclass;

    private String jmsProviderurl;

    private String jmsConnectionfactory;

    private String jmsJaxbDefaultpackage;

    private String soaphost;

    private String soapNameSpaceAware;

    private String paginationPageSize;

    private String pageLinkMax;

    private String server;

    private String dbmsVendor;

    private String webAppsDrive;

    private String webAppsDir;

    private String defaultWebAppCtxRootDirName;

    private String systemConfig;

    private String appConfigPath;

    /**
     * Creates a LdapComputerWeb object withou intializing any of its
     * properties.
     */
    public LdapComputerWeb() {
        super();
    }

    /**
     * Get the remote services web application context
     * 
     * @return String
     */
    public String getRemoteSrvcApp() {
        return remoteSrvcApp;
    }

    /**
     * Set the remote services web application context
     * 
     * @param app
     *            String
     */
    public void setRemoteSrvcApp(String app) {
        this.remoteSrvcApp = app;
    }

    /**
     * Get the name of the servlet used for remote services.
     * 
     * @return String
     */
    public String getRemoteSrvcServlet() {
        return remoteSrvcServlet;
    }

    /**
     * Set the name of the servlet used for remote services.
     * 
     * @param servlet
     *            String
     */
    public void setRemoteSrvcServlet(String servlet) {
        this.remoteSrvcServlet = servlet;
    }

    /**
     * Get the load services host name
     * 
     * @return the load services host
     */
    public String getLoadServicesHost() {
        return loadServicesHost;
    }

    /**
     * Set the load services host name
     * 
     * @param host
     *            the loadservices_host to set
     */
    public void setLoadServicesHost(String host) {
        this.loadServicesHost = host;
    }

    /**
     * Get the services module name
     * 
     * @return the load services module name
     */
    public String getLoadServicesModule() {
        return loadServicesModule;
    }

    /**
     * Set the services module name
     * 
     * @param module
     *            the load services module to set
     */
    public void setLoadServicesModule(String module) {
        this.loadServicesModule = module;
    }

    /**
     * Get the id of the load services.
     * 
     * @return the load services id
     */
    public String getLoadServicesId() {
        return loadServicesId;
    }

    /**
     * Set the id of the load services
     * 
     * @param id
     *            the load services id to set
     */
    public void setLoadServicesId(String id) {
        this.loadServicesId = id;
    }

    /**
     * Get the default number of database connections to add to the connection
     * pool
     * 
     * @return the total number of default connections
     */
    public String getDefaultconnections() {
        return defaultconnections;
    }

    /**
     * Set the default number of database connections to add to the connection
     * pool
     * 
     * @param defaultconnections
     *            the total number of default connections
     */
    public void setDefaultconnections(String defaultconnections) {
        this.defaultconnections = defaultconnections;
    }

    /**
     * Get the maximum number of database connections to add to the connection
     * pool
     * 
     * @return the maxconnections the maximum number of default connections
     */
    public String getMaxconnections() {
        return maxconnections;
    }

    /**
     * Set the maximum number of database connections to add to the connection
     * pool
     * 
     * @param maxconnections
     *            the maximum number of default connections
     */
    public void setMaxconnections(String maxconnections) {
        this.maxconnections = maxconnections;
    }

    /**
     * Get the time out interval in seconds which the user session should
     * terminate
     * 
     * @return String
     */
    public String getTimeoutInterval() {
        return timeoutInterval;
    }

    /**
     * Set the time out interval in seconds which the user session should
     * terminate
     * 
     * @param timeoutInterval
     *            String
     */
    public void setTimeoutInterval(String timeoutInterval) {
        this.timeoutInterval = timeoutInterval;
    }

    /**
     * Get the letter of the hard drive where object serialization should occur
     * 
     * @return the serial drive
     */
    public String getSerialDrive() {
        return serialDrive;
    }

    /**
     * Set the letter of the hard drive where object serialization should occur
     * 
     * @param serialDrive
     *            the serial_drive to set
     */
    public void setSerialDrive(String serialDrive) {
        this.serialDrive = serialDrive;
    }

    /**
     * Get file serialization directory path
     * 
     * @return the serial path
     */
    public String getSerialPath() {
        return serialPath;
    }

    /**
     * Set file serialization directory path
     * 
     * @param serialPath
     *            the serial path to set
     */
    public void setSerialPath(String serialPath) {
        this.serialPath = serialPath;
    }

    /**
     * Get the extention of the file that will containt the results of object
     * serialization
     * 
     * @return the serial ext
     */
    public String getSerial_ext() {
        return serialExt;
    }

    /**
     * Set the extention of the file that will containt the results of object
     * serialization
     * 
     * @param serialExt
     *            the serial_ext to set
     */
    public void setSerial_ext(String serialExt) {
        this.serialExt = serialExt;
    }

    /**
     * Get the fully qualified class name of the XML SAX driver
     * 
     * @return the SAX Driver
     */
    public String getSaxDriver() {
        return SaxDriver;
    }

    /**
     * Set the fully qualified class name of the XML SAX driver
     * 
     * @param saxDriver
     *            the SAX Driver to set
     */
    public void setSaxDriver(String saxDriver) {
        SaxDriver = saxDriver;
    }

    /**
     * Get the URL resource for the \"Wait Please...\" JSP page used with
     * polling service
     * 
     * @return the polling page
     */
    public String getPollingPage() {
        return pollingPage;
    }

    /**
     * Set the URL resource for the \"Wait Please...\" JSP page used with
     * polling service
     * 
     * @param pollingPage
     *            the polling page to set
     */
    public void setPollingPage(String pollingPage) {
        this.pollingPage = pollingPage;
    }

    /**
     * Get the file name of the application command mappings properties file
     * 
     * @return the web app mapping
     */
    public String getWeb_app_mapping() {
        return webAppMapping;
    }

    /**
     * Set the file name of the application command mappings properties file
     * 
     * @param webAppMapping
     *            the web app mapping to set
     */
    public void setWeb_app_mapping(String webAppMapping) {
        this.webAppMapping = webAppMapping;
    }

    /**
     * Get the path where to find the XSLT files for reporting
     * 
     * @return the rpt_xslt_path
     */
    public String getRptXsltPath() {
        return rptXsltPath;
    }

    /**
     * Set the path where to find the XSLT files for reporting
     * 
     * @param path
     *            the rpt_xslt_path to set
     */
    public void setRptXsltPath(String path) {
        this.rptXsltPath = path;
    }

    /**
     * Get the file extention of the XSLT files for reporting
     * 
     * @return the rpt_file_ext
     */
    public String getRptFileExt() {
        return rptFileExt;
    }

    /**
     * Set the file extention of the XSLT files for reporting
     * 
     * @param ext
     *            the rpt_file_ext to set
     */
    public void setRptFileExt(String ext) {
        this.rptFileExt = ext;
    }

    /**
     * Get the path where image files can be recognized by the XSLT files used
     * for reporting
     * 
     * @return the image_dir
     */
    public String getImageDir() {
        return imageDir;
    }

    /**
     * Set the path where image files can be recognized by the XSLT files used
     * for reporting
     * 
     * @param imageDir
     *            the image_dir to set
     */
    public void setImageDir(String imageDir) {
        this.imageDir = imageDir;
    }

    /**
     * Get The number of cycles used to encrypt passwords
     * 
     * @return the encrypt cycle number
     */
    public String getEncryptCycle() {
        return encryptCycle;
    }

    /**
     * Set The number of cycles used to encrypt passwords
     * 
     * @param encryptCycle
     *            the encrypt cycle number
     */
    public void setEncryptCycle(String encryptCycle) {
        this.encryptCycle = encryptCycle;
    }

    /**
     * Get the server name of the SMTP host for the mail server
     * 
     * @return the mail_host_smtp
     */
    public String getMailHostSmtp() {
        return mailHostSmtp;
    }

    /**
     * Set the server name of the SMTP host for the mail server
     * 
     * @param host
     *            the mail_host_smtp to set
     */
    public void setMailHostSmtp(String host) {
        this.mailHostSmtp = host;
    }

    /**
     * Get The server name of the POP3 host for the mail server
     * 
     * @return the mail_host_pop3
     */
    public String getMailHostPop3() {
        return mailHostPop3;
    }

    /**
     * Set the server name of the POP3 host for the mail server
     * 
     * @param host
     *            the mail_host_pop3 to set
     */
    public void setMailHostPop3(String host) {
        this.mailHostPop3 = host;
    }

    /**
     * Get the flag that indicates if the user should be authenticated when
     * connecting to the mail server.
     * 
     * @return the mail authentication
     */
    public String getMailAuthentication() {
        return mailAuthentication;
    }

    /**
     * Set the flag that indicates if the user should be authenticated when
     * connecting to the mail server.
     * <p>
     * Set to "true" when authentication is required. Otherwise, set to "false".
     * 
     * @param authentication
     *            the mail authentication to set
     */
    public void setMailAuthentication(String authentication) {
        this.mailAuthentication = authentication;
    }

    /**
     * Get The login id of the user connecting to the mail server
     * 
     * @return the mail userId
     */
    public String getMailUserId() {
        return mailUserId;
    }

    /**
     * Set The login id of the user connecting to the mail server
     * 
     * @param userId
     *            the mail userId to set
     */
    public void setMailUserId(String userId) {
        this.mailUserId = userId;
    }

    /**
     * Get the password of the user connecting to the mail server
     * 
     * @return the mail password
     */
    public String getMailPassword() {
        return mailPassword;
    }

    /**
     * Set the password of the user connecting to the mail server
     * 
     * @param password
     *            the mail password to set
     */
    public void setMailPassword(String password) {
        this.mailPassword = password;
    }

    /**
     * Get he default content type of the incoming/outgoing mail messages
     * 
     * @return the mail defaultcontent
     */
    public String getMailDefaultcontent() {
        return mailDefaultcontent;
    }

    /**
     * Set he default content type of the incoming/outgoing mail messages
     * 
     * @param defaultcontent
     *            the mail defaultcontent to set
     */
    public void setMailDefaultcontent(String defaultcontent) {
        this.mailDefaultcontent = defaultcontent;
    }

    /**
     * Get the path where mail templates can be found
     * 
     * @return the mail template path
     */
    public String getMailTemplatepath() {
        return mailTemplatepath;
    }

    /**
     * Set the path where mail templates can be found
     * 
     * @param path
     *            the mail template path to set
     */
    public void setMailTemplatepath(String path) {
        this.mailTemplatepath = path;
    }

    /**
     * Get the domain name of the mail server
     * 
     * @return the mail internal smtp domain
     */
    public String getMailIinternalSmtpDomain() {
        return mailInternalSmtpDomain;
    }

    /**
     * Set the domain name of the mail server
     * 
     * @param domain
     *            the mail internal smtp domain to set
     */
    public void setMailIinternalSmtpDomain(String domain) {
        this.mailInternalSmtpDomain = domain;
    }

    /**
     * Get the mail resource type.
     * 
     * @return the mail resource type
     */
    public String getMailResourcetype() {
        return mailResourcetype;
    }

    /**
     * Set the mail resource type.
     * 
     * @param resourcetype
     *            the mail resource type to set
     */
    public void setMailResourcetype(String resourcetype) {
        this.mailResourcetype = resourcetype;
    }

    /**
     * Get the JMS JNDI repository resource type
     * 
     * @return the jms jndi source
     */
    public String getJmsJndisource() {
        return jmsJndisource;
    }

    /**
     * Set the JMS JNDI repository resource type
     * 
     * @param jndisource
     *            the jms jndi source to set
     */
    public void setJmsJndisource(String jndisource) {
        this.jmsJndisource = jndisource;
    }

    /**
     * Get the fully qualified name of the JMS factory class
     * 
     * @return the jms contextclass
     */
    public String getJmsContextclass() {
        return jmsContextclass;
    }

    /**
     * Set the fully qualified name of the JMS factory class
     * 
     * @param contextclass
     *            the jms contextclass to set
     */
    public void setJmsContextclass(String contextclass) {
        this.jmsContextclass = contextclass;
    }

    /**
     * Get the URL of the JMS Server
     * 
     * @return the jms provider url
     */
    public String getJmsProviderurl() {
        return jmsProviderurl;
    }

    /**
     * Set the URL of the JMS Server
     * 
     * @param providerurl
     *            the jms provider url to set
     */
    public void setJmsProviderurl(String providerurl) {
        this.jmsProviderurl = providerurl;
    }

    /**
     * Get the lookup name for the ConnectionFactory when using the queue
     * manager as a JNDI repository
     * 
     * @return the jms connection factory
     */
    public String getJmsConnectionfactory() {
        return jmsConnectionfactory;
    }

    /**
     * Set the lookup name for the ConnectionFactory when using the queue
     * manager as a JNDI repository
     * 
     * @param connectionfactory
     *            the jms connection factory to set
     */
    public void setJmsConnectionfactory(String connectionfactory) {
        this.jmsConnectionfactory = connectionfactory;
    }

    /**
     * Get the default location where JMS bind and package XML related messages
     * via JAXB
     * 
     * @return the jms jaxb default package
     */
    public String getJmsJaxbDefaultpackage() {
        return jmsJaxbDefaultpackage;
    }

    /**
     * Set the default location where JMS bind and package XML related messages
     * via JAXB
     * 
     * @param defaultpackage
     *            the jms jaxb default package to set
     */
    public void setJmsJaxbDefaultpackage(String defaultpackage) {
        this.jmsJaxbDefaultpackage = defaultpackage;
    }

    /**
     * Get SOAP host
     * 
     * @return the soap host
     */
    public String getSoaphost() {
        return soaphost;
    }

    /**
     * Set the SOAP host
     * 
     * @param soaphost
     *            the soap host to set
     */
    public void setSoaphost(String soaphost) {
        this.soaphost = soaphost;
    }

    /**
     * Get SOAP name space aware flag
     * 
     * @return "true" when namespace aware and "false" when not namespace aware
     */
    public String getSoapNameSpaceAware() {
        return soapNameSpaceAware;
    }

    /**
     * Set SOAP name space aware flag
     * 
     * @param flag
     *            the soap namespace aware flag value to set. Set to "true" for
     *            namespace aware and "false" for not namespace aware
     */
    public void setSoapNameSpaceAware(String flag) {
        this.soapNameSpaceAware = flag;
    }

    /**
     * Get the pagination property value for governing how many rows are
     * displayed per page
     * 
     * @return the pagination page size
     */
    public String getPaginationPageSize() {
        return paginationPageSize;
    }

    /**
     * Set the pagination property value for governing how many rows are
     * displayed per page
     * 
     * @param pageSize
     *            the pagination page size to set
     */
    public void setPaginationPageSize(String pageSize) {
        this.paginationPageSize = pageSize;
    }

    /**
     * Get the pagination proerty value for governing how many page numbers are
     * displayed in pagination rule of the display
     * 
     * @return the page link max
     */
    public String getPageLinkMax() {
        return pageLinkMax;
    }

    /**
     * Set the pagination proerty value for governing how many page numbers are
     * displayed in pagination rule of the display
     * 
     * @param pageLinkMax
     *            the page link max to set
     */
    public void setPageLinkMax(String pageLinkMax) {
        this.pageLinkMax = pageLinkMax;
    }

    /**
     * Get the identity of the server including its port number if applicable.
     * 
     * @return the server name and possible the port number
     */
    public String getServer() {
        return this.server;
    }

    /**
     * Set the identity of the server including its port number if applicable.
     * 
     * @param server
     *            the server name and possible the port number
     */
    public void setServer(String server) {
        this.server = server;
    }

    /**
     * Get A numeric value representing the the manufacture of the RDBMS used by
     * the application.
     * 
     * @return the DBMS Vendor
     */
    public String getDBMSVendor() {
        return dbmsVendor;
    }

    /**
     * Set A numeric value representing the the manufacture of the RDBMS used by
     * the application.
     * 
     * @param dbmsVendor
     *            The DBMS vendor to set. Valid values are: 1 = Sybase Sql
     *            Anywhere, Adaptive Server Anywhere, 2 = Sybase Adaptive Server
     *            Enterprise, 3 = Oracle, 4 = MS SQL Server, and 5 = DB2
     */
    public void setDBMSVendor(String dbmsVendor) {
        this.dbmsVendor = dbmsVendor;
    }

    /**
     * Get the drive letter where the web application is installed.
     * <p>
     * This should include the colon suffix
     * 
     * @return the web apps drive
     */
    public String getWebapps_drive() {
        return webAppsDrive;
    }

    /**
     * Set the drive letter where the web application is installed.
     * 
     * @param drive
     *            the web apps drive to set which should include the colon
     *            suffix
     */
    public void setWebapps_drive(String drive) {
        this.webAppsDrive = drive;
    }

    /**
     * Get the fully qualified file system directory path to the web application
     * context
     * 
     * @return the web apps dir
     */
    public String getWebapps_dir() {
        return webAppsDir;
    }

    /**
     * Set the fully qualified file system directory path to the web application
     * context
     * 
     * @param dir
     *            the web apps dir to set
     */
    public void setWebapps_dir(String dir) {
        this.webAppsDir = dir;
    }

    /**
     * @return the defaultWebAppCtxRootDirName
     */
    public String getDefaultWebAppCtxRootDirName() {
        return defaultWebAppCtxRootDirName;
    }

    /**
     * @param defaultWebAppCtxRootDirName
     *            the defaultWebAppCtxRootDirName to set
     */
    public void setDefaultWebAppCtxRootDirName(
            String defaultWebAppCtxRootDirName) {
        this.defaultWebAppCtxRootDirName = defaultWebAppCtxRootDirName;
    }

    /**
     * @return the systemConfig
     */
    public String getSystemConfig() {
        return systemConfig;
    }

    /**
     * @param systemConfig
     *            the systemConfig to set
     */
    public void setSystemConfig(String systemConfig) {
        this.systemConfig = systemConfig;
    }

    /**
     * @return the appConfigPath
     */
    public String getAppConfigPath() {
        return appConfigPath;
    }

    /**
     * @param appConfigPath
     *            the appConfigPath to set
     */
    public void setAppConfigPath(String appConfigPath) {
        this.appConfigPath = appConfigPath;
    }

}
