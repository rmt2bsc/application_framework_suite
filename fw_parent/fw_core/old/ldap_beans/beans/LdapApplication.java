package com.api.ldap.beans;

/**
 * @author rterrell
 * 
 */
public class LdapApplication extends LdapCommonEntity {

    private String appCode;

    private String active;

    private String appDir;

    private String appTitle;

    private String APP_ROOT_DIR_NAME;

    private String appLogConfig;

    private String appNavRulesPath;

    private String appOutPath;

    private String DB_CONNECTION_FACTORY;

    private String dbOwner;

    private String ormBeanPkg;

    private String ormBeanPkgPrefix;

    private String ormGenOutput;

    private String ormXmlDir;

    private String userAuthLocale;

    private String userLoginSrc;

    private String userLogoutSrc;

    /**
     * 
     */
    public LdapApplication() {
        super();
    }

    /**
     * @return the appCode
     */
    public String getAppCode() {
        return appCode;
    }

    /**
     * @param appCode
     *            the appCode to set
     */
    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    /**
     * @return the active
     */
    public String getActive() {
        return active;
    }

    /**
     * @param active
     *            the active to set
     */
    public void setActive(String active) {
        this.active = active;
    }

    /**
     * @return the appDir
     */
    public String getAppDir() {
        return appDir;
    }

    /**
     * @param appDir
     *            the appDir to set
     */
    public void setAppDir(String appDir) {
        this.appDir = appDir;
    }

    /**
     * @return the appTitle
     */
    public String getAppTitle() {
        return appTitle;
    }

    /**
     * @param appTitle
     *            the appTitle to set
     */
    public void setAppTitle(String appTitle) {
        this.appTitle = appTitle;
    }

    /**
     * @return the aPP_ROOT_DIR_NAME
     */
    public String getAPP_ROOT_DIR_NAME() {
        return APP_ROOT_DIR_NAME;
    }

    /**
     * @param aPP_ROOT_DIR_NAME
     *            the aPP_ROOT_DIR_NAME to set
     */
    public void setAPP_ROOT_DIR_NAME(String aPP_ROOT_DIR_NAME) {
        APP_ROOT_DIR_NAME = aPP_ROOT_DIR_NAME;
    }

    /**
     * @return the appLogConfig
     */
    public String getAppLogConfig() {
        return appLogConfig;
    }

    /**
     * @param appLogConfig
     *            the appLogConfig to set
     */
    public void setAppLogConfig(String appLogConfig) {
        this.appLogConfig = appLogConfig;
    }

    /**
     * @return the appNavRulesPath
     */
    public String getAppNavRulesPath() {
        return appNavRulesPath;
    }

    /**
     * @param appNavRulesPath
     *            the appNavRulesPath to set
     */
    public void setAppNavRulesPath(String appNavRulesPath) {
        this.appNavRulesPath = appNavRulesPath;
    }

    /**
     * @return the appOutPath
     */
    public String getAppOutPath() {
        return appOutPath;
    }

    /**
     * @param appOutPath
     *            the appOutPath to set
     */
    public void setAppOutPath(String appOutPath) {
        this.appOutPath = appOutPath;
    }

    /**
     * @return the dB_CONNECTION_FACTORY
     */
    public String getDB_CONNECTION_FACTORY() {
        return DB_CONNECTION_FACTORY;
    }

    /**
     * @param dB_CONNECTION_FACTORY
     *            the dB_CONNECTION_FACTORY to set
     */
    public void setDB_CONNECTION_FACTORY(String dB_CONNECTION_FACTORY) {
        DB_CONNECTION_FACTORY = dB_CONNECTION_FACTORY;
    }

    /**
     * @return the dbOwner
     */
    public String getDbOwner() {
        return dbOwner;
    }

    /**
     * @param dbOwner
     *            the dbOwner to set
     */
    public void setDbOwner(String dbOwner) {
        this.dbOwner = dbOwner;
    }

    /**
     * @return the ormBeanPkg
     */
    public String getOrmBeanPkg() {
        return ormBeanPkg;
    }

    /**
     * @param ormBeanPkg
     *            the ormBeanPkg to set
     */
    public void setOrmBeanPkg(String ormBeanPkg) {
        this.ormBeanPkg = ormBeanPkg;
    }

    /**
     * @return the ormBeanPkgPrefix
     */
    public String getOrmBeanPkgPrefix() {
        return ormBeanPkgPrefix;
    }

    /**
     * @param ormBeanPkgPrefix
     *            the ormBeanPkgPrefix to set
     */
    public void setOrmBeanPkgPrefix(String ormBeanPkgPrefix) {
        this.ormBeanPkgPrefix = ormBeanPkgPrefix;
    }

    /**
     * @return the ormGenOutput
     */
    public String getOrmGenOutput() {
        return ormGenOutput;
    }

    /**
     * @param ormGenOutput
     *            the ormGenOutput to set
     */
    public void setOrmGenOutput(String ormGenOutput) {
        this.ormGenOutput = ormGenOutput;
    }

    /**
     * @return the ormXmlDir
     */
    public String getOrmXmlDir() {
        return ormXmlDir;
    }

    /**
     * @param ormXmlDir
     *            the ormXmlDir to set
     */
    public void setOrmXmlDir(String ormXmlDir) {
        this.ormXmlDir = ormXmlDir;
    }

    /**
     * @return the userAuthLocale
     */
    public String getUserAuthLocale() {
        return userAuthLocale;
    }

    /**
     * @param userAuthLocale
     *            the userAuthLocale to set
     */
    public void setUserAuthLocale(String userAuthLocale) {
        this.userAuthLocale = userAuthLocale;
    }

    /**
     * @return the userLoginSrc
     */
    public String getUserLoginSrc() {
        return userLoginSrc;
    }

    /**
     * @param userLoginSrc
     *            the userLoginSrc to set
     */
    public void setUserLoginSrc(String userLoginSrc) {
        this.userLoginSrc = userLoginSrc;
    }

    /**
     * @return the userLogoutSrc
     */
    public String getUserLogoutSrc() {
        return userLogoutSrc;
    }

    /**
     * @param userLogoutSrc
     *            the userLogoutSrc to set
     */
    public void setUserLogoutSrc(String userLogoutSrc) {
        this.userLogoutSrc = userLogoutSrc;
    }

}
