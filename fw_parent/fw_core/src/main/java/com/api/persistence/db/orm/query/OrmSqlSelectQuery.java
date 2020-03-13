package com.api.persistence.db.orm.query;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.NoSuchElementException;

import com.SystemException;
import com.api.DaoApi;
import com.api.ProductBuilder;
import com.api.ProductBuilderException;
import com.api.constants.RMT2SystemExceptionConst;
//import com.api.config.PropertyFileSystemResourceConfigImpl;
import com.api.persistence.db.DbSqlConst;
import com.api.persistence.db.orm.bean.DataSourceColumn;
import com.api.persistence.db.orm.query.pagination.PageCalculator;
import com.api.persistence.db.orm.query.pagination.PaginationApi;
import com.api.util.RMT2String;

/**
 * This class provides complex processes to construct and deconstruct SQL select
 * statements as {@link com.api.Product Product}.
 * 
 * @author RTerrell
 * 
 */
class OrmSqlSelectQuery extends AbstractOrmQuery implements ProductBuilder {
    private static final long serialVersionUID = 186555399825202878L;

    private static Logger logger = Logger.getLogger(OrmSqlSelectQuery.class);

    /**
     * Constructs a OrmSqlSelectQuery object by initializing it with an
     * arbitrary object that has a runtime type of either DaoApi or String. This
     * constructor is for either assembling or disassembling a query.
     * 
     * @param src
     *            an arbitrary object represting a runtime value of
     *            {@link com.api.DaoApi DaoApi}.
     * @throws SystemException
     *             <i>src</i> is null or invalid.
     */
    public OrmSqlSelectQuery(Object src) throws SystemException {
        super(src);
        logger = Logger.getLogger("OrmSqlSelectQuery");
    }

    /**
     * Build a SQL Select statement. Capable of determining whether or not the
     * query shoulb be based on pagination.
     * 
     * @throws ProductBuilderException
     */
    public void assemble() throws ProductBuilderException {
        logger.log(Level.DEBUG, "Assembling ORM Select Statement");
        int pageNo = 0;
        if (this.getSrc() instanceof PaginationApi) {
            pageNo = ((PaginationApi) this.getSrc()).getPageNo();
        }
        if (pageNo > 0) {
            this.assemblePaginatedQuery(pageNo);
        }
        else {
            this.assembleQuery();
        }
    }

    /**
     * Builds a SQL Select statement based on the values stored in target
     * datasource view document. This method is flexible enough to determine if
     * the query is supposed to return a row count or a complete detail dataset
     * based on the datasource's returnRowCount value set by the caller.
     * 
     * @throws ProductBuilderException
     */

    private void assembleQuery() throws ProductBuilderException {
        StringBuffer sql = new StringBuffer(100);
        String sqlKeyword = null;
        String sqlClauseValue = null;

        Hashtable obj = this.validateAssemblySource(this.getSrc());

        boolean returnRowCount = false;
        // if (this.getSrc() instanceof PaginationApi) {
        // returnRowCount = ((PaginationApi) this.getSrc()).isReturnRowCount();
        // }

        returnRowCount = ((DaoApi) this.getSrc()).isReturnRowCount();

        // Merge select statement components
        try {
            // Evaluate the components of the Select statement in the order of:
            // select, from, where, group by, having and order by
            for (int ndx = DbSqlConst.SELECT_KEY; ndx <= DbSqlConst.ORDERBY_KEY; ndx++) {
                Integer sqlClauseKey = new Integer(ndx);
                // stored as 0,1,2,3,4,5
                sqlClauseValue = (String) obj.get(sqlClauseKey)
                        + RMT2String.spaces(3);
                // Get actual sql clause without the reserve word
                // Do not proces value if it is equal (" ", "null ")
                if (sqlClauseValue.trim().length() > 1
                        && !sqlClauseValue.trim().equalsIgnoreCase("null")) {
                    if (ndx == DbSqlConst.ORDERBY_KEY && returnRowCount) {
                        // Ignore order by clause if we are supposed to build a
                        // row count query.
                        continue;
                    }
                    sqlKeyword = DbSqlConst.SELECTOBJECT.get(sqlClauseKey)
                            + RMT2String.spaces(1);
                    sql.append(sqlKeyword);

                    // Check if a row count is request instead of a complete
                    // dataset
                    if (ndx == DbSqlConst.SELECT_KEY) {
                        if (returnRowCount) {
                            sqlClauseValue = " count(*) "
                                    + AbstractQueryBuilder.QUERY_ROWCOUNT
                                    + RMT2String.spaces(3);
                        }
                    }
                    sql.append(sqlClauseValue);
                }
            }

            if (sql.length() <= 0) {
                logger.log(Level.ERROR,
                        RMT2SystemExceptionConst.MSG_INVALID_SQL_STATEMENT);
                throw new ProductBuilderException(
                        RMT2SystemExceptionConst.MSG_INVALID_SQL_STATEMENT);
            }

            this.setQueryString(sql.toString());
            this.setQueryComp(obj);
        }

        catch (NoSuchElementException e) {
            msg = "The number of sql clauses to retrieve have been exceeded";
            throw new ProductBuilderException(this.msg);
        }
    }

    /**
     * Builds a SQL query that is appropriate for pagination. This method uses
     * the SQL ranking function, ROW_NUMBER, along with the OVER clause to
     * achieve paginated results. As of to date, this logic is compatible with
     * Oracle, Sybase, Sybase SQL AnyWhere, and SQL Server 2005.
     * 
     * @param pageNo
     *            the page index to base query on.
     * @throws ProductBuilderException
     */
    private void assemblePaginatedQuery(int pageNo)
            throws ProductBuilderException {
        // Determine what the page size and the starting and ending boudaries of
        // the result set should be.
        int pageSize = PageCalculator.getPageSize();
        int endNo = pageNo * pageSize;
        int startNo = endNo - pageSize + 1;

        // Begin to build Select statement.
        StringBuffer sql = new StringBuffer(100);
        StringBuffer outerSql = new StringBuffer(100);
        String sqlKeyword = null;
        String sqlClauseValue = null;

        Hashtable obj = this.validateAssemblySource(this.getSrc());

        // Merge select statement components
        try {
            // Evaluate the components of the Select statement in the order of:
            // select, from, where, group by, and having clauses. The order by
            // clause is intentionally skipped and will be deffered to later
            // step.
            for (int ndx = DbSqlConst.SELECT_KEY; ndx <= DbSqlConst.HAVING_KEY; ndx++) {
                Integer sqlClauseKey = new Integer(ndx);
                // stored as 0,1,2,3,4,5
                sqlClauseValue = (String) obj.get(sqlClauseKey)
                        + RMT2String.spaces(3);
                // Get actual sql clause without the reserve word
                // Do not proces value if it is equal (" ", "null ")
                if (sqlClauseValue.trim().length() > 1
                        && !sqlClauseValue.trim().equalsIgnoreCase("null")) {
                    sqlKeyword = DbSqlConst.SELECTOBJECT.get(sqlClauseKey)
                            + RMT2String.spaces(1);
                    sql.append(sqlKeyword);
                    // Add ranking function to query if we are constructing the
                    // select list
                    if (ndx == DbSqlConst.SELECT_KEY) {
                        sql.append(this.buildRankingFunction(obj));
                        sql.append(", ");

                        // Setup select list for outer Query.
                        outerSql.append(sqlKeyword);
                        outerSql.append(sqlClauseValue);
                        outerSql.append(" FROM (");
                    }
                    sql.append(sqlClauseValue);
                }
            }

            if (sql.length() <= 0) {
                this.msg = "Paginatiion SQL statement is invalid";
                logger.log(Level.ERROR, this.msg);
                throw new ProductBuilderException(this.msg);
            }

            // Finish building outer SQL Query
            outerSql.append(sql.toString());
            outerSql.append(") as pageResults ");
            outerSql.append(" where rownumber between ");
            outerSql.append(startNo);
            outerSql.append(" and ");
            outerSql.append(endNo);

            this.setQueryString(outerSql.toString());
            this.setQueryComp(obj);
        }

        catch (NoSuchElementException e) {
            msg = "The number of sql clauses to retrieve have been exceeded";
            throw new ProductBuilderException(this.msg);
        }
    }

    /**
     * Builds ranking functionality to be used as part of the SQL Select list.
     * This should work well for pagination using the ranking function,
     * ROW_NUMBER. The designated ROW_NUBER column is aliased as "rownumber" so
     * that it may be accessed by name in the event the results of this method
     * is part of a derived table which will act as a VIEW. It is required that
     * the datasource used to build the query contains a primary key.
     * 
     * @param selectMap
     * @return String SQL column list containg the ROW_NUMBER ranking function
     *         along with its OVER clause.
     * @throws ProductBuilderException
     *             When the order by clause is invalid or is unobtainable via
     *             the datasource
     */
    private String buildRankingFunction(Hashtable selectMap)
            throws ProductBuilderException {
        String orderByList = (String) selectMap.get(DbSqlConst.ORDERBY_KEY)
                + RMT2String.spaces(3);
        String sqlKeyword = DbSqlConst.SELECTOBJECT.get(DbSqlConst.ORDERBY_KEY)
                + RMT2String.spaces(1);
        StringBuffer sql = new StringBuffer();
        if (orderByList == null || orderByList.trim().length() <= 0) {
            // Add logic to discover primary key database column name
            // to use in the OVER clause
            DataSourceColumn dsc = this.dsAttr.getDsoPrimaryKey();
            if (dsc == null) {
                this.msg = "Datasource must have a primary key in order to build a paginated SQL Select statement";
                logger.log(Level.ERROR, this.msg);
                throw new ProductBuilderException(this.msg);
            }
            orderByList = dsc.getDbName();
        }
        sql.append("ROW_NUMBER() OVER (");
        sql.append(sqlKeyword);
        sql.append(orderByList);
        sql.append(") as rownumber");
        return sql.toString();
    }

    /**
     * Validate the data source used for the assembly of SQL select statement.
     * 
     * @param src
     *            Required to be an instance of {@link com.api.DaoApi DaoApi}.
     * @return A Hashtable containg key/value pairs representing SQL Select
     *         statement claues.
     * @throws ProductBuilderException
     *             <i>src</i> is not of type ObjectMapperAttrib or is null or is
     *             empty.
     */
    private Hashtable validateAssemblySource(Object src)
            throws ProductBuilderException {
        // Get select statement object
        Hashtable obj = this.dsAttr.getSelectStatement();

        // Attribute object must be valid and have at least one entry.
        if (obj == null || obj.size() <= 0) {
            this.msg = "The select statement\'s ObjectMapperAttrib property has not been initialized or is empty";
            logger.log(Level.ERROR, this.msg);
            throw new ProductBuilderException(this.msg);
        }

        // Everything is valid
        return obj;
    }

    /**
     * Separate a Sql Select statement down to its individual claues and add the
     * individual clauses as key/value pairs to a Hashtable. The Hashtable
     * instance is subsequently assigned to the ObjectMapperAttrib as a result
     * of separating the select statement clauses.
     * 
     * @throws ProductBuilderException
     */
    public void disAssemble() throws ProductBuilderException {
        String sql = this.validateDisassemblySource(this.getSrc());

        int endpos;
        int startpos;
        int nextpos;
        String temp;
        List keyList;
        Hashtable selectStatement;

        keyList = new ArrayList();
        try {
            keyList.add(DbSqlConst.SELECTOBJECT.get(new Integer(
                    DbSqlConst.SELECT_KEY)));
            keyList.add(DbSqlConst.SELECTOBJECT.get(new Integer(
                    DbSqlConst.FROM_KEY)));
            keyList.add(DbSqlConst.SELECTOBJECT.get(new Integer(
                    DbSqlConst.WHERE_KEY)));
            keyList.add(DbSqlConst.SELECTOBJECT.get(new Integer(
                    DbSqlConst.GROUPBY_KEY)));
            keyList.add(DbSqlConst.SELECTOBJECT.get(new Integer(
                    DbSqlConst.HAVING_KEY)));
            keyList.add(DbSqlConst.SELECTOBJECT.get(new Integer(
                    DbSqlConst.ORDERBY_KEY)));

            selectStatement = new Hashtable();
            for (int ndx = 0; ndx < keyList.size(); ndx++) {
                // Get the current clause type index of the SQL statement.
                nextpos = ndx;

                // Get Sql Clause to process. example: "select", "from",
                // "where", ect.
                String key = (String) keyList.get(ndx);

                // Locate the position of clause in the SQL statment.
                startpos = sql.indexOf(key + RMT2String.spaces(1));
                if (startpos >= 0) {
                    // Reset the end position
                    endpos = 0;

                    // Determine the end position relative to the next clause in
                    // the statement provided that we are at the beginning or in
                    // the middle of the statement.
                    try {
                        while (keyList.get(++nextpos) != null) {
                            endpos = sql.indexOf((String) keyList.get(nextpos)
                                    + RMT2String.spaces(1));
                            // if the next clause exist, save the position of
                            // the next clause
                            if (endpos >= 0)
                                break;
                        }
                    } catch (IndexOutOfBoundsException e) {
                        endpos = -1; // Clause was not found
                    }
                    // If the clause is not found, or clause is the first and
                    // only, or clause is positioned as the last clause...
                    if (endpos < 0) {
                        endpos = sql.length();
                    }

                    // Capture the value of the sql clause relative to the value
                    // of "key". Extract the value without the reserve word.
                    temp = sql.substring(startpos + key.length(), endpos);
                    selectStatement.put(new Integer(ndx), temp);
                } // end if
            } // end for

            this.setQueryComp(selectStatement);
            this.setQueryString(sql);
            return;
        } catch (NullPointerException e) {
            throw new ProductBuilderException(
                    "A null pointer exceptin occrred when disasembling a SQL statement");
        } catch (IndexOutOfBoundsException e) {
            throw new ProductBuilderException(
                    "The bounds of the SQL statement object has been exceeded during SQL statement disassembly");
        }
    }

    /**
     * Validates the data source used to disassemble a SQL select statement.
     * 
     * @param src
     *            Required to be String.
     * @return SQL select statement as a String.
     * @throws ProductBuilderException
     *             When <i>src</i> is not of type String.
     */
    private String validateDisassemblySource(Object src)
            throws ProductBuilderException {
        boolean validSrcType = (src instanceof String);
        if (!validSrcType) {
            this.msg = "Query data source must be of type String in order to decompose SQL Select statement";
            logger.log(Level.ERROR, this.msg);
            throw new ProductBuilderException(this.msg);
        }
        return src.toString();
    }
}
