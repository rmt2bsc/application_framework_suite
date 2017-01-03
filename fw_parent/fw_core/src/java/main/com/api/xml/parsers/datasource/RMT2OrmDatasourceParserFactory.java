package com.api.xml.parsers.datasource;

import com.api.security.RMT2TagQueryBean;

/**
 * Factory for creating objects related to ORM Datasource configuration parsing.
 * 
 * @author appdev
 * 
 */
public class RMT2OrmDatasourceParserFactory {

    private RMT2OrmDatasourceParserFactory() {
        return;
    }

    /**
     * Returns a new instance of RMT2OrmDatasourceParserFactory
     * 
     * @return {@link com.api.xml.parsers.datasource.RMT2OrmDatasourceParserFactory
     *         RMT2OrmDatasourceParserFactory}
     */
    public static RMT2OrmDatasourceParserFactory getNewInstance() {
        return new RMT2OrmDatasourceParserFactory();
    }

    /**
     * Creates an instance of RMT2OrmDatasourceParser using RMT2TagQueryBean.
     * 
     * @param queryData
     *            an instance of {@link bean.RMT2TagQueryBean RMT2TagQueryBean}
     * @return {@link com.api.xml.parsers.datasource.RMT2OrmDatasourceParser
     *         RMT2OrmDatasourceParser}
     */
    public RMT2OrmDatasourceParser getSax1OrmDatasourceParser(
            RMT2TagQueryBean queryData) {
        RMT2OrmDatasourceParser api = new RMT2Sax1OrmDatasourceParserImpl(
                queryData);
        return api;
    }

    /**
     * Creates an instance of RMT2OrmDatasourceParser using the name of the ORM
     * Datasource configuration.
     * 
     * @param dsName
     *            the ORM datasource name.
     * @return {@link com.api.xml.parsers.datasource.RMT2OrmDatasourceParser
     *         RMT2OrmDatasourceParser}
     */
    public RMT2OrmDatasourceParser getSax1OrmDatasourceParser(String dsName) {
        RMT2OrmDatasourceParser api = new RMT2Sax1OrmDatasourceParserImpl(
                dsName);
        return api;
    }

    /**
     * 
     * @param xmlDoc
     * @return
     */
    public RMT2OrmDatasourceParser getSax2OrmDatasourceParser(String xmlDoc) {
        RMT2OrmDatasourceParser api = new RMT2Sax2OrmThridPartyDriverImpl(
                xmlDoc);
        return api;
    }
}
