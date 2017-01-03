package com.api.persistence.db.orm.query.pagination;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.api.persistence.db.DatabaseConnectionBean;
import com.api.persistence.db.orm.DataSourceConverter;

/**
 * Factory designed to create new instances of DataSourceApi and DaoApi objects.
 * 
 * @author roy.terrell
 * 
 */
public class PaginationFactory extends DataSourceConverter {
    private static Logger logger = Logger.getLogger(PaginationFactory.class);

    /**
     * Creates a PaginationApi object using a DatabaseConnectionBean.
     * 
     * @param dbo
     *            {@link DatabaseConnectionBean}
     * @return {@link PaginationApi}
     */
    public static PaginationApi createDao(DatabaseConnectionBean dbo) {
        try {
            PaginationApi api = new PaginationImpl(dbo);
            logger.log(Level.INFO,
                    "PaginationApi created with DatabaseConnectionBean");
            return api;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Creates a PaginationApi object using a DatabaseConnectionBean and the
     * data source name.
     * 
     * @param dbo
     *            A valid Database connection bean and cannot be null.
     * @param dsn
     *            The name of the data source.
     * @return {@link DaoApi}
     */
    public static PaginationApi createDao(DatabaseConnectionBean dbo, String dsn) {
        try {
            PaginationApi api = new PaginationImpl(dbo, dsn);
            logger.log(Level.INFO, "PaginationApi created with DSN");
            return api;
        } catch (Exception e) {
            return null;
        }
    }

    // /**
    // * Creates a PaginationApi object using a DatabaseConnectionBean and the
    // user's request
    // *
    // * @param dbo {@link DatabaseConnectionBean}
    // * @param request Object containing the user's request
    // * @return {@link PaginationApi}
    // */
    // public static PaginationApi createDao(DatabaseConnectionBean dbo, Request
    // request) {
    // try {
    // PaginationApi api = new PaginationImpl(dbo, request);
    // logger.log(Level.INFO,
    // "PaginationApi created with DatabaseConnectionBean and Use's Request");
    // return api;
    // }
    // catch (Exception e) {
    // return null;
    // }
    // }
}
