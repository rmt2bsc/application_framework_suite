/**
 * 
 */
package testcases.ldap.beans;

/**
 * @author rterrell
 * 
 */
public class TestLdapComputerWeb extends TestLdapCommonComputer {

    private String remote_services_app;

    private String remote_services_servlet;

    private String loadservices_host;

    private String loadservices_module;

    private String loadservices_id;

    private String defaultconnections;

    private String maxconnections;

    private String timeoutInterval;

    private String serial_drive;

    private String serial_path;

    private String serial_ext;

    private String SAXDriver;

    private String polling_page;

    private String web_app_mapping;

    private String rpt_xslt_path;

    private String rpt_file_ext;

    private String image_dir;

    private String ENCRYPT_CYCLE;

    private String mail_host_smtp;

    private String mail_host_pop3;

    private String mail_authentication;

    private String mail_userId;

    private String mail_password;

    private String mail_defaultcontent;

    private String mail_templatepath;

    private String mail_internal_smtp_domain;

    private String mail_resourcetype;

    private String jms_jndisource;

    private String jms_contextclass;

    private String jms_providerurl;

    private String jms_connectionfactory;

    private String jms_jaxb_defaultpackage;

    private String soaphost;

    private String soapNameSpaceAware;

    private String pagination_page_size;

    private String page_link_max;

    private String SERVER;

    private String DBMSVendor;

    private String webapps_drive;

    private String webapps_dir;

    private String defaultWebAppCtxRootDirName;

    private String systemConfig;

    private String appConfigPath;

    /**
     * 
     */
    public TestLdapComputerWeb() {
        super();
    }

    /**
     * @return the remote_services_app
     */
    public String getRemote_services_app() {
        return remote_services_app;
    }

    /**
     * @param remote_services_app
     *            the remote_services_app to set
     */
    public void setRemote_services_app(String remote_services_app) {
        this.remote_services_app = remote_services_app;
    }

    /**
     * @return the remote_services_servlet
     */
    public String getRemote_services_servlet() {
        return remote_services_servlet;
    }

    /**
     * @param remote_services_servlet
     *            the remote_services_servlet to set
     */
    public void setRemote_services_servlet(String remote_services_servlet) {
        this.remote_services_servlet = remote_services_servlet;
    }

    /**
     * @return the loadservices_host
     */
    public String getLoadservices_host() {
        return loadservices_host;
    }

    /**
     * @param loadservices_host
     *            the loadservices_host to set
     */
    public void setLoadservices_host(String loadservices_host) {
        this.loadservices_host = loadservices_host;
    }

    /**
     * @return the loadservices_module
     */
    public String getLoadservices_module() {
        return loadservices_module;
    }

    /**
     * @param loadservices_module
     *            the loadservices_module to set
     */
    public void setLoadservices_module(String loadservices_module) {
        this.loadservices_module = loadservices_module;
    }

    /**
     * @return the loadservices_id
     */
    public String getLoadservices_id() {
        return loadservices_id;
    }

    /**
     * @param loadservices_id
     *            the loadservices_id to set
     */
    public void setLoadservices_id(String loadservices_id) {
        this.loadservices_id = loadservices_id;
    }

    /**
     * @return the defaultconnections
     */
    public String getDefaultconnections() {
        return defaultconnections;
    }

    /**
     * @param defaultconnections
     *            the defaultconnections to set
     */
    public void setDefaultconnections(String defaultconnections) {
        this.defaultconnections = defaultconnections;
    }

    /**
     * @return the maxconnections
     */
    public String getMaxconnections() {
        return maxconnections;
    }

    /**
     * @param maxconnections
     *            the maxconnections to set
     */
    public void setMaxconnections(String maxconnections) {
        this.maxconnections = maxconnections;
    }

    /**
     * @return the timeoutInterval
     */
    public String getTimeoutInterval() {
        return timeoutInterval;
    }

    /**
     * @param timeoutInterval
     *            the timeoutInterval to set
     */
    public void setTimeoutInterval(String timeoutInterval) {
        this.timeoutInterval = timeoutInterval;
    }

    /**
     * @return the serial_drive
     */
    public String getSerial_drive() {
        return serial_drive;
    }

    /**
     * @param serial_drive
     *            the serial_drive to set
     */
    public void setSerial_drive(String serial_drive) {
        this.serial_drive = serial_drive;
    }

    /**
     * @return the serial_path
     */
    public String getSerial_path() {
        return serial_path;
    }

    /**
     * @param serial_path
     *            the serial_path to set
     */
    public void setSerial_path(String serial_path) {
        this.serial_path = serial_path;
    }

    /**
     * @return the serial_ext
     */
    public String getSerial_ext() {
        return serial_ext;
    }

    /**
     * @param serial_ext
     *            the serial_ext to set
     */
    public void setSerial_ext(String serial_ext) {
        this.serial_ext = serial_ext;
    }

    /**
     * @return the sAXDriver
     */
    public String getSAXDriver() {
        return SAXDriver;
    }

    /**
     * @param sAXDriver
     *            the sAXDriver to set
     */
    public void setSAXDriver(String sAXDriver) {
        SAXDriver = sAXDriver;
    }

    /**
     * @return the polling_page
     */
    public String getPolling_page() {
        return polling_page;
    }

    /**
     * @param polling_page
     *            the polling_page to set
     */
    public void setPolling_page(String polling_page) {
        this.polling_page = polling_page;
    }

    /**
     * @return the web_app_mapping
     */
    public String getWeb_app_mapping() {
        return web_app_mapping;
    }

    /**
     * @param web_app_mapping
     *            the web_app_mapping to set
     */
    public void setWeb_app_mapping(String web_app_mapping) {
        this.web_app_mapping = web_app_mapping;
    }

    /**
     * @return the rpt_xslt_path
     */
    public String getRpt_xslt_path() {
        return rpt_xslt_path;
    }

    /**
     * @param rpt_xslt_path
     *            the rpt_xslt_path to set
     */
    public void setRpt_xslt_path(String rpt_xslt_path) {
        this.rpt_xslt_path = rpt_xslt_path;
    }

    /**
     * @return the rpt_file_ext
     */
    public String getRpt_file_ext() {
        return rpt_file_ext;
    }

    /**
     * @param rpt_file_ext
     *            the rpt_file_ext to set
     */
    public void setRpt_file_ext(String rpt_file_ext) {
        this.rpt_file_ext = rpt_file_ext;
    }

    /**
     * @return the image_dir
     */
    public String getImage_dir() {
        return image_dir;
    }

    /**
     * @param image_dir
     *            the image_dir to set
     */
    public void setImage_dir(String image_dir) {
        this.image_dir = image_dir;
    }

    /**
     * @return the eNCRYPT_CYCLE
     */
    public String getENCRYPT_CYCLE() {
        return ENCRYPT_CYCLE;
    }

    /**
     * @param eNCRYPT_CYCLE
     *            the eNCRYPT_CYCLE to set
     */
    public void setENCRYPT_CYCLE(String eNCRYPT_CYCLE) {
        ENCRYPT_CYCLE = eNCRYPT_CYCLE;
    }

    /**
     * @return the mail_host_smtp
     */
    public String getMail_host_smtp() {
        return mail_host_smtp;
    }

    /**
     * @param mail_host_smtp
     *            the mail_host_smtp to set
     */
    public void setMail_host_smtp(String mail_host_smtp) {
        this.mail_host_smtp = mail_host_smtp;
    }

    /**
     * @return the mail_host_pop3
     */
    public String getMail_host_pop3() {
        return mail_host_pop3;
    }

    /**
     * @param mail_host_pop3
     *            the mail_host_pop3 to set
     */
    public void setMail_host_pop3(String mail_host_pop3) {
        this.mail_host_pop3 = mail_host_pop3;
    }

    /**
     * @return the mail_authentication
     */
    public String getMail_authentication() {
        return mail_authentication;
    }

    /**
     * @param mail_authentication
     *            the mail_authentication to set
     */
    public void setMail_authentication(String mail_authentication) {
        this.mail_authentication = mail_authentication;
    }

    /**
     * @return the mail_userId
     */
    public String getMail_userId() {
        return mail_userId;
    }

    /**
     * @param mail_userId
     *            the mail_userId to set
     */
    public void setMail_userId(String mail_userId) {
        this.mail_userId = mail_userId;
    }

    /**
     * @return the mail_password
     */
    public String getMail_password() {
        return mail_password;
    }

    /**
     * @param mail_password
     *            the mail_password to set
     */
    public void setMail_password(String mail_password) {
        this.mail_password = mail_password;
    }

    /**
     * @return the mail_defaultcontent
     */
    public String getMail_defaultcontent() {
        return mail_defaultcontent;
    }

    /**
     * @param mail_defaultcontent
     *            the mail_defaultcontent to set
     */
    public void setMail_defaultcontent(String mail_defaultcontent) {
        this.mail_defaultcontent = mail_defaultcontent;
    }

    /**
     * @return the mail_templatepath
     */
    public String getMail_templatepath() {
        return mail_templatepath;
    }

    /**
     * @param mail_templatepath
     *            the mail_templatepath to set
     */
    public void setMail_templatepath(String mail_templatepath) {
        this.mail_templatepath = mail_templatepath;
    }

    /**
     * @return the mail_internal_smtp_domain
     */
    public String getMail_internal_smtp_domain() {
        return mail_internal_smtp_domain;
    }

    /**
     * @param mail_internal_smtp_domain
     *            the mail_internal_smtp_domain to set
     */
    public void setMail_internal_smtp_domain(String mail_internal_smtp_domain) {
        this.mail_internal_smtp_domain = mail_internal_smtp_domain;
    }

    /**
     * @return the mail_resourcetype
     */
    public String getMail_resourcetype() {
        return mail_resourcetype;
    }

    /**
     * @param mail_resourcetype
     *            the mail_resourcetype to set
     */
    public void setMail_resourcetype(String mail_resourcetype) {
        this.mail_resourcetype = mail_resourcetype;
    }

    /**
     * @return the jms_jndisource
     */
    public String getJms_jndisource() {
        return jms_jndisource;
    }

    /**
     * @param jms_jndisource
     *            the jms_jndisource to set
     */
    public void setJms_jndisource(String jms_jndisource) {
        this.jms_jndisource = jms_jndisource;
    }

    /**
     * @return the jms_contextclass
     */
    public String getJms_contextclass() {
        return jms_contextclass;
    }

    /**
     * @param jms_contextclass
     *            the jms_contextclass to set
     */
    public void setJms_contextclass(String jms_contextclass) {
        this.jms_contextclass = jms_contextclass;
    }

    /**
     * @return the jms_providerurl
     */
    public String getJms_providerurl() {
        return jms_providerurl;
    }

    /**
     * @param jms_providerurl
     *            the jms_providerurl to set
     */
    public void setJms_providerurl(String jms_providerurl) {
        this.jms_providerurl = jms_providerurl;
    }

    /**
     * @return the jms_connectionfactory
     */
    public String getJms_connectionfactory() {
        return jms_connectionfactory;
    }

    /**
     * @param jms_connectionfactory
     *            the jms_connectionfactory to set
     */
    public void setJms_connectionfactory(String jms_connectionfactory) {
        this.jms_connectionfactory = jms_connectionfactory;
    }

    /**
     * @return the jms_jaxb_defaultpackage
     */
    public String getJms_jaxb_defaultpackage() {
        return jms_jaxb_defaultpackage;
    }

    /**
     * @param jms_jaxb_defaultpackage
     *            the jms_jaxb_defaultpackage to set
     */
    public void setJms_jaxb_defaultpackage(String jms_jaxb_defaultpackage) {
        this.jms_jaxb_defaultpackage = jms_jaxb_defaultpackage;
    }

    /**
     * @return the soaphost
     */
    public String getSoaphost() {
        return soaphost;
    }

    /**
     * @param soaphost
     *            the soaphost to set
     */
    public void setSoaphost(String soaphost) {
        this.soaphost = soaphost;
    }

    /**
     * @return the soapNameSpaceAware
     */
    public String getSoapNameSpaceAware() {
        return soapNameSpaceAware;
    }

    /**
     * @param soapNameSpaceAware
     *            the soapNameSpaceAware to set
     */
    public void setSoapNameSpaceAware(String soapNameSpaceAware) {
        this.soapNameSpaceAware = soapNameSpaceAware;
    }

    /**
     * @return the pagination_page_size
     */
    public String getPagination_page_size() {
        return pagination_page_size;
    }

    /**
     * @param pagination_page_size
     *            the pagination_page_size to set
     */
    public void setPagination_page_size(String pagination_page_size) {
        this.pagination_page_size = pagination_page_size;
    }

    /**
     * @return the page_link_max
     */
    public String getPage_link_max() {
        return page_link_max;
    }

    /**
     * @param page_link_max
     *            the page_link_max to set
     */
    public void setPage_link_max(String page_link_max) {
        this.page_link_max = page_link_max;
    }

    /**
     * @return the sERVER
     */
    public String getSERVER() {
        return SERVER;
    }

    /**
     * @param sERVER
     *            the sERVER to set
     */
    public void setSERVER(String sERVER) {
        SERVER = sERVER;
    }

    /**
     * @return the dBMSVendor
     */
    public String getDBMSVendor() {
        return DBMSVendor;
    }

    /**
     * @param dBMSVendor
     *            the dBMSVendor to set
     */
    public void setDBMSVendor(String dBMSVendor) {
        DBMSVendor = dBMSVendor;
    }

    /**
     * @return the webapps_drive
     */
    public String getWebapps_drive() {
        return webapps_drive;
    }

    /**
     * @param webapps_drive
     *            the webapps_drive to set
     */
    public void setWebapps_drive(String webapps_drive) {
        this.webapps_drive = webapps_drive;
    }

    /**
     * @return the webapps_dir
     */
    public String getWebapps_dir() {
        return webapps_dir;
    }

    /**
     * @param webapps_dir
     *            the webapps_dir to set
     */
    public void setWebapps_dir(String webapps_dir) {
        this.webapps_dir = webapps_dir;
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
