package com.api.persistence.db.orm.query.pagination;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.NotFoundException;
import com.SystemException;
import com.api.persistence.DatabaseException;
import com.api.persistence.PersistenceClient;
import com.api.persistence.db.DatabaseConnectionBean;
import com.api.persistence.db.orm.DataSourceFactory;
import com.api.persistence.db.orm.OrmBean;
import com.api.persistence.db.orm.Rmt2OrmClientFactory;
import com.api.persistence.db.orm.RdbmsDaoImpl;
import com.api.persistence.db.orm.query.AbstractQueryBuilder;

/**
 * @author appdev
 * 
 */
class PaginationImpl extends RdbmsDaoImpl implements PaginationApi {

    private static final Logger logger = Logger.getLogger("PaginationImpl");

    private int pageNo;

    private int rowCount;

    /**
     * @throws DatabaseException
     * @throws SystemException
     */
    public PaginationImpl() throws DatabaseException, SystemException {
        return;
    }

    /**
     * @param dbConn
     * @throws DatabaseException
     * @throws SystemException
     */
    public PaginationImpl(DatabaseConnectionBean dbConn)
            throws DatabaseException, SystemException {
        super(dbConn);
        return;
    }

    // /**
    // * @param dbConn
    // * @param request
    // * @throws DatabaseException
    // * @throws SystemException
    // */
    // public PaginationImpl(DatabaseConnectionBean dbConn, Request request)
    // throws DatabaseException, SystemException {
    // super(dbConn, request);
    // return;
    // }

    /**
     * @param dbConn
     * @param dsn
     * @throws DatabaseException
     * @throws SystemException
     */
    public PaginationImpl(DatabaseConnectionBean dbConn, String dsn)
            throws DatabaseException, SystemException {
        super(dbConn, dsn);
        return;
    }

    /**
     * 
     * @param obj
     * @param pageNo
     * @return
     * @throws DatabaseException
     * @thows PaginationException
     */
    public PaginationQueryResults retrieveList(OrmBean obj, int pageNo)
            throws DatabaseException, PaginationException {
        if (obj == null) {
            this.msg = "A valid ORM instance is required when performing a pagination query";
            logger.error(this.msg);
            throw new PaginationException(this.msg);
        }

        // pageNo is required. It is an error if page number is less than or
        // equal to zero.
        if (pageNo <= 0) {
            this.msg = "The page number to fetch must be specified when performing a pagination query";
            logger.error(this.msg);
            throw new PaginationException(this.msg);
        }

        // PersistenceClient daoHelper =
        // Rmt2OrmClientFactory.createInstance(this.connector);
        PersistenceClient daoHelper = Rmt2OrmClientFactory
                .createOrmClientInstance();
        this.pageNo = pageNo;
        PaginationQueryResults pageObj = new PaginationQueryResults();
        pageObj.setPageNo(pageNo);
        try {
            // Get complete dataset
            this.returnRowCount = false;
            Object data[] = this.retrieve(obj);
            if (data != null && data.length > 0) {
                pageObj.setResults(java.util.Arrays.asList(data));
                pageObj.setPageRowCount(data.length);
                pageObj.setQuery(this.getSelectSql());
            }
            else {
                return null;
            }
            // Get the total row count of the query that would be expected if
            // pagination was not used.
            Long count = this.retrieveCount(obj);
            pageObj.setTotalRowCount(count);

            // Get total page count
            PageCalculator calc = new PageCalculator(count);
            double totalPages = calc.getTotalPages();
            pageObj.setTotalPageCount(totalPages);

            return pageObj;
        } catch (DatabaseException e) {
            throw new DatabaseException(
                    "Problem retrieving ORMBean List data for page #" + pageNo,
                    e);
        }
    }

    public long retrieveCount(OrmBean obj) throws DatabaseException {
        if (obj == null) {
            return -2;
        }
        try {
            // Get row count
            this.returnRowCount = true;
            this.pageNo = 0;
            Object data[] = this.retrieve(obj);
            if (data != null && data.length > 0) {
                Long count = (Long) data[0];
                return count;
            }
            return -1;
        } catch (DatabaseException e) {
            throw new DatabaseException(
                    "Problem retrieving database count of ORMBean criteria", e);
        }
    }

    /**
     * Obtains data from the database via RMT2DataSourceApi using selection
     * crtiteria from the input parameter, _criteria. The developer is
     * responsible for developing the logic for packaging a bean by extending
     * the method, packageBean(RMT2DataSourceApi _dso) to facilitate capturing
     * multiple rows of data from dso.
     * 
     * @param _whereClause
     *            Criteria for the datasource's where clause
     * @param _orderByClause
     *            Criteria for the datasource's order by clasue
     * @return Always returns a valid List
     * @throws SystemException
     */
    protected List find(String _whereClause, String _orderByClause)
            throws SystemException {
        this.executeDso(_whereClause, _orderByClause);
        try {
            // Initialize ArrayList with data
            List list = null;
            if (this.isReturnRowCount()) {
                // Try to row count result set
                try {
                    ResultSet rs = this.getRs();
                    if (rs != null) {
                        while (rs.next()) {
                            long result = rs
                                    .getLong(AbstractQueryBuilder.QUERY_ROWCOUNT);
                            list = new ArrayList();
                            list.add(result);
                        }
                    }
                } catch (SQLException e) {
                    throw new SystemException(e);
                }
                return list;
            }
            else {
                // Try to get complete result set
                list = DataSourceFactory.packageBean(this, this.baseBeanClass);
            }
            return list;
        } catch (DatabaseException e) {
            throw new SystemException(e);
        } catch (NotFoundException e) {
            throw new SystemException(e);
        }
    }

    /**
     * Get the current page number which the query is executing.
     * 
     * @return int
     */
    public int getPageNo() {
        return pageNo;
    }

    /**
     * SGet the current page number for query is execution.
     * 
     * @param pageNo
     *            int the page number
     */
    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    /**
     * Get the total number of rows which query returned.
     * 
     * @return int
     */
    public int getRowCount() {
        return rowCount;
    }

    /**
     * Set the total number of rows which query returned.
     * 
     * @param rowCount
     */
    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

}
