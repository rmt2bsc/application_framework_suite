package com.api.persistence.db.orm;

import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.SystemException;
import com.api.config.AppPropertyPool;
import com.api.config.ConfigConstants;
import com.util.RMT2File;

/**
 * Helper class for managing the configuration of ORM Datasources.
 * 
 * @author Roy Terrell
 * 
 */
public class OrmConfigHelper {

    /** The name of the application ResourceBundle */
    public static final String APP_PROP_CONFIG_RESOURCE = "config.AppParms";

    /**
     * Obtains system properties related to the directory path where the ORM
     * Datasource configuration files can be found. The various properties
     * should be configured in the AppParms.properties and/or the application's
     * web.xml.
     * 
     * @throws SystemException
     * @throws NotFoundException
     * 
     *             Refactor this method to new class
     *             com.api.db.orm.OrmConfigHelper
     */
    public static String getOrmDatasourceFullDirPath(String docName)
            throws SystemException {
        String dsDir;
        String msg;
        StringBuffer temp = new StringBuffer(50);
        Logger logger = Logger.getLogger(OrmConfigHelper.class);
        try {
            String ext = RMT2File.getFileExt(docName);
            if (ext == null || (ext != null && !ext.equalsIgnoreCase(".xml"))) {
                docName += ".xml";
            }

            dsDir = AppPropertyPool
                    .getProperty(ConfigConstants.PROPNAME_DATASOURCE_DIR);
            if (dsDir == null) {
                dsDir = System
                        .getProperty(ConfigConstants.PROPNAME_DATASOURCE_DIR);
            }
            // Take this approach for stand alone applications where the
            // application configuration can be found in the classpath as
            // "config.AppParms.properties".
            if (dsDir == null) {
                ResourceBundle rb = RMT2File
                        .loadAppConfigProperties(OrmConfigHelper.APP_PROP_CONFIG_RESOURCE);
                dsDir = rb.getString(ConfigConstants.PROPNAME_DATASOURCE_DIR);
            }

            if (dsDir == null) {
                msg = "ORM data source configuration for " + docName
                        + " was not found for optional environment variable, "
                        + ConfigConstants.PROPNAME_DATASOURCE_DIR;
                logger.warn(msg);
            }

            // Build full file path of XML document
            temp.append(dsDir == null ? "" : dsDir);
            docName = temp.toString() + docName;
            return docName;
        } catch (NullPointerException e) {
            msg = "One of the XML Datasource config properties in SystemParms.properties are null";
            throw new SystemException(msg, e);
        }
    }

}
