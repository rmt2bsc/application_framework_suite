package com.api.persistence.db.orm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.NotFoundException;
import com.SystemException;
import com.api.DaoApi;
import com.api.DataSourceApi;
import com.api.Product;
import com.api.ProductBuilder;
import com.api.ProductBuilderException;
import com.api.ProductDirector;
import com.api.persistence.DatabaseException;
import com.api.persistence.db.DatabaseConnectionBean;
import com.api.persistence.db.DbSqlConst;
import com.api.persistence.db.orm.bean.ObjectMapperAttrib;
import com.api.persistence.db.orm.bean.TableUsageBean;
import com.api.persistence.db.orm.query.OrmQueryBuilderFactory;
import com.api.security.RMT2TagQueryBean;
import com.constants.RMT2SystemExceptionConst;
import com.util.RMT2File;
import com.util.RMT2Utility;

/**
 * This class contains methods responsible for the storage and retrieval of data
 * from one or more relational database providers.
 * <p>
 * The implemented methods provides functionality for recognizing and handling
 * SQL related statements that are used to communicate transactions to a
 * relational database.
 * 
 * @author roy.terrell
 * 
 */
public class RdbmsDaoImpl extends RdbmsDataSourceImpl implements DaoApi,
        DataSourceApi {

    private int resultType;

    private Logger logger; // = Logger.getLogger(RdbmsDaoImpl.class);

    private String dsn;

    private OrmBean ormBean;

    /**
     * Constructs a DAO that is not assoicated with a data provider.
     * 
     * @throws DatabaseException
     * @throws SystemException
     */
    protected RdbmsDaoImpl() throws DatabaseException, SystemException {
        super();
        // logger = Logger.getLogger("RdbmsDaoImpl");
        this.resultType = DaoApi.RESULTSET_TYPE_TABULAR;
    }

    /**
     * Constructs a DAO that is assoicated with database.
     * 
     * @param dbConn
     *            The database connection that is to serve as the data provider
     *            for this DAO.
     * @throws DatabaseException
     * @throws SystemException
     */
    public RdbmsDaoImpl(DatabaseConnectionBean dbConn)
            throws DatabaseException, SystemException {
        super(dbConn);
        logger = Logger.getLogger("RdbmsDaoImpl");
        return;
    }

    // public RdbmsDaoImpl(DatabaseConnectionBean dbConn, Request request)
    // throws DatabaseException, SystemException {
    // super(dbConn, request);
    // logger = Logger.getLogger("RdbmsDaoImpl");
    // return;
    // }

    /**
     * Constructs a DAO object by setting is database connection and its data
     * source.
     * 
     * @param dbConn
     *            The database connection lthat is to serve as the data provider
     *            for this DAO>
     * @param dsn
     *            The name of the data source
     * @throws DatabaseException
     * @throws SystemException
     */
    public RdbmsDaoImpl(DatabaseConnectionBean dbConn, String dsn)
            throws DatabaseException, SystemException {
        super(dbConn);
        this.setDataSourceName(dsn);
        logger = Logger.getLogger("RdbmsDaoImpl");
        return;
    }

    /**
     * Initializes a RdbmsDaoImpl object with a Connection object, a loginId, a
     * RMT2TagQueryBean, and a ResourceBundle. Subsequently determines the type
     * of and initializes the datasource that will be used.
     * 
     * @param con
     * @param loginId
     * @param queryData
     * @param sysData
     * @throws NotFoundException
     * @throws DatabaseException
     * @throws SystemException
     */
    public RdbmsDaoImpl(Connection con, String loginId,
            RMT2TagQueryBean queryData) throws NotFoundException,
            DatabaseException, SystemException {
        super(con, loginId, queryData);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.db.DatabaseTransImpl#close()
     */
    @Override
    public void close() throws DatabaseException {
        this.dsn = null;
        logger = null;
        super.close();
    }

    /**
     * Initializes the connection object and the statement object that is to be
     * used with this class.
     */
    protected void initApi() throws DatabaseException, SystemException {
        super.initApi();
        logger = Logger.getLogger("RdbmsDaoImpl");
    } // End init

    /**
     * Sets the data source name for this DAO. In turn a data source object is
     * created using dsn which will make it current. Upon successful completion,
     * the current data source for this DAO can be otained through the method
     * call, getDatasource(), and the identification of this DAO can be obtained
     * by calling getDataSourcename().
     * 
     * @param dsn
     *            The name of the data source
     */
    public void setDataSourceName(String dsn) {
        try {
            this.parseXmlDataSource(dsn);
            this.dsn = dsn;
        } catch (Exception e) {
            throw new SystemException(
                    "Problem initializing the name of the ORM Datasource Configuration",
                    e);
        }
    }

    /**
     * Gets the identity of this DAO by is data source name.
     * 
     * @return Data source name.
     */
    public String getDataSourceName() {
        return this.dsn;
    }

    /**
     * Determines the DataSource view and class from an OrmBean object type and
     * sets the DataSource view and class properties.
     * 
     * @param bean
     *            The ORM bean to evaluate.
     */
    protected void discoverDataSource(OrmBean bean) {
        String viewName = bean.getDataSourceName();
        String className = bean.getDataSourceClassName();
        this.setBaseClass(className);
        this.setBaseView(viewName);
        return;
    }

    /**
     * Releases the database and JDBC resources immediately instead of waiting
     * for them to be garbage collectd. Extend this method at the decendent
     * level if any other resources are needed to be released.
     * 
     * @throws DatabaseException
     * @throws SystemException
     */
    protected void releaseDBResources() throws DatabaseException,
            SystemException {
        super.releaseDBResources();
    }

    /**
     * Executes the SQL statement, _sql, and instructs the JDBC Driver whether
     * or not auto-generated keys are produced. It is assumed that user is
     * expecting only one key to be auto-generated which is a single column
     * primary key value.
     * 
     * @param _sql
     *            An Insert, Update, or Delete statement or an SQL statement
     *            that returns nothing.
     * @param _generateKeys
     *            A flag indicating whether keys are automatically generated by
     *            the database and made available to the caller for retrival.
     * @return The total count of rows effect by the DML statement when
     *         _generatedKeys equal false. When _generateKeys is true, returns
     *         the auto-generated key which is a value greater than zero or -1
     *         when the key cannot be obtained from the auto-generated key
     *         ResultSet.
     * @throws DatabaseException
     * @throws SystemException
     */
    protected int executeUpdate(String _sql, boolean _generateKeys)
            throws DatabaseException, SystemException {
        String temp = null;
        int autoKeyInd = 0;
        int rc = 0;
        ResultSet rs = null;
        Statement statement = null;

        if (_sql.length() <= 0) {
            this.msg = RMT2SystemExceptionConst.MSG_INVALID_SQL_STATEMENT
                    + " for user: " + this.loginId;
            throw new SystemException(msg,
                    RMT2SystemExceptionConst.RC_INVALID_SQL_STATEMENT);
        }

        if (_generateKeys) {
            autoKeyInd = Statement.RETURN_GENERATED_KEYS;
        }
        else {
            autoKeyInd = Statement.NO_GENERATED_KEYS;
        }

        try {
            this.rsScrollable = false;
            this.rsUpdatable = true;
            statement = this.getJDBCStatementObject();
            rc = statement.executeUpdate(_sql, autoKeyInd);
            if (_generateKeys) {
                rs = statement.getGeneratedKeys();
                if (rs.next()) {
                    temp = rs.getString(1);
                    rc = Integer.parseInt(temp);
                }
                else {
                    rc = -1;
                }
            }
            return rc;
        } catch (SQLException e) {
            msg = "JDBC SQL  Error occurred for ORM Insert execution.  SQL: "
                    + _sql;
            logger.error(msg);
            throw new DatabaseException(msg, e);
        } catch (Throwable e) {
            msg = "General SQL database error occurred for ORM Insert execution.  SQL: "
                    + _sql;
            throw new DatabaseException(msg, e);
        }
    }

    /**
     * Executes a DML SQL Statement such as an Insert, Delete or Update.
     * 
     * @param _sql
     *            An Insert, Update, or Delete statement or an SQL statement
     *            that returns nothing.
     * @return A count of the number of rows effected by the DML statement or
     *         zero if nothing was returned from the SQL Statement.
     * @throws DatabaseException
     * @throws SystemException
     */
    public int executeUpdate(String _sql) throws DatabaseException,
            SystemException {
        Statement statement = null;

        if (_sql.length() <= 0) {
            this.msg = RMT2SystemExceptionConst.MSG_INVALID_SQL_STATEMENT
                    + " for user: " + this.loginId;
            throw new SystemException(msg,
                    RMT2SystemExceptionConst.RC_INVALID_SQL_STATEMENT);
        }

        try {
            this.rsScrollable = false;
            this.rsUpdatable = true;
            statement = this.getJDBCStatementObject();
            return statement.executeUpdate(_sql);
        } catch (SQLException e) {
            msg = "JDBC SQL Error occurred for ORM Insert, Update, or Delete execution.  SQL: "
                    + _sql;
            throw new DatabaseException(msg, e);
        }
    }

    /**
     * Resets the DataSource metadata provided that the expected result type is
     * tabular.
     * 
     * @return Total number of column definitions processed. Returns zero if
     *         result set type is something other thatn "tabular".
     * @throws DatabaseException
     * @throws NotFoundException
     * @throws SystemException
     * @see {@link RMT2DataSourceApiImpl#refreshDataSource()}
     */
    protected int refreshDataSource() throws DatabaseException,
            NotFoundException, SystemException {
        if (this.resultType == DaoApi.RESULTSET_TYPE_TABULAR) {
            return super.refreshDataSource();
        }
        return 0;
    }

    /**
     * Determines if the target SQL statement should return its result set in
     * the form of XML instead of tabular.
     * 
     * @return SQL statement with the <i>for xml</i> clause appended.
     * @throws SystemException
     */
    protected String assembleQuery() throws SystemException {
        String sql = null;

        // Get select statement object and apply row limiting clauses, if
        // applicable
        ObjectMapperAttrib dsAttr = this.getDataSourceAttib();
        Hashtable selectStmt = dsAttr.getSelectStatement();

        // Test the validity of "ormBean". Could be null if the source is JSP
        // taglib.
        if (this.ormBean != null) {
            if (this.ormBean.getRowLimitClause() != null) {
                Integer ndx = new Integer(DbSqlConst.SELECT_KEY);
                String selectList = (String) selectStmt.get(ndx);
                selectList = this.ormBean.getRowLimitClause() + "  "
                        + selectList;
                selectStmt.put(ndx, selectList);
            }
        }

        // TODO: In the future, replace call by moving super class logic here...
        sql = super.assembleQuery();

        if (this.resultType == DaoApi.RESULTSET_TYPE_XML) {
            sql += " for xml auto, elements ";
        }
        return sql;
    }

    /**
     * Retrieves pertaining to the datasource that ormBean is associated with.
     * User is expected to setup the appropriate selection and ordering criteria
     * from within ormBean.
     * 
     * @param ormBean
     *            The ORM bean that is used to obtain data from the database.
     * @return One or more ormBean objects.
     * @throws DatabaseException
     *             A database access error occurs.
     */
    public Object[] findData(Object ormBean) throws DatabaseException {
        Object list[] = this.retrieve(ormBean);
        return list;
    }

    /**
     * Determines the location of the ORM's assoicated datasource XML file and
     * returns the findings to the caller.
     * <p>
     * The datasource file is expected to reside in the same package or
     * directory as the ORM bean class. The functionality of this method works
     * for datasources that are encapsulated within a jar or for those that are
     * a part of the top level application. This methos is used for retrieve,
     * insert, update, and delete API operations.
     * 
     * @param ormBean
     *            An instance of {@link OrmBean}
     * @return The path of the associated datasource file.
     */
    private String getDataSourcePath(OrmBean ormBean) {
        String viewName = ormBean.getDataSourceName();
        String className = ormBean.getDataSourceClassName();
        String pakageName = ormBean.getDataSourcePackage();
        String dataSourceView = pakageName + "." + viewName;
        dataSourceView = dataSourceView.replaceAll("\\.", "/") + ".xml";
        // Get view and class names and set to this object
        this.setBaseClass(className);
        this.setBaseView(viewName);

        return dataSourceView;
    }

    /**
     * Retrieves data from the database using _obj as the source using a ORM
     * approach.
     * 
     * @throws DatabaseException
     * @see com.api.RMT2DaoApi#retrieve(RMT2BaseBean)
     */
    public Object[] retrieve(Object obj) throws DatabaseException {
        if (obj == null) {
            return null;
        }
        OrmBean ormBean = null;
        if (obj instanceof OrmBean) {
            ormBean = (OrmBean) obj;
        }
        else {
            this.msg = "Method argument must be of type RMT2BaseBean";
            logger.error(this.msg);
            throw new DatabaseException(this.msg);
        }

        this.ormBean = ormBean;
        Object results[] = null;
        String where = null;
        String orderBy = null;
        // String viewName = ormBean.getDataSourceName();
        // String className = ormBean.getDataSourceClassName();
        // String pakageName = ormBean.getDataSourcePackage();

        // System.out.println(ormBean.getClass().getName()
        // + " is loaded from "
        // + ormBean.getClass().getProtectionDomain().getCodeSource()
        // .getLocation());

        // Ensure that datasource is set

        try {
            // String dataSourceView = pakageName + "." + viewName;
            // dataSourceView = dataSourceView.replaceAll("\\.", "/") + ".xml";
            String dataSourceView = this.getDataSourcePath(ormBean);
            this.setDataSourceName(dataSourceView);
            DaoApi dao = this;
            // Synchronize DAO with the ORM bean
            DataSourceConverter.packageDSO(dao, ormBean);
            // // Get view and class names and set to this object
            // this.setBaseClass(className);
            // this.setBaseView(viewName);

            // Get selection and order criteria
            try {
                // Build where clause
                ProductBuilder selectionBuilder = OrmQueryBuilderFactory
                        .getCriteriaQuery(this, ormBean);
                Product sqlObj = ProductDirector.construct(selectionBuilder);
                where = sqlObj.toString();

                // Build order by clause
                ProductBuilder orderBuilder = OrmQueryBuilderFactory
                        .getOrderQuery(this, ormBean);
                sqlObj = ProductDirector.construct(orderBuilder);
                orderBy = sqlObj.toString();

            } catch (ProductBuilderException e) {
                throw new SystemException(e);
            }

            // Call find method.
            if (ormBean.getResultsetType() == OrmBean.RESULTSET_TYPE_XML) {
                List list = this.findData(where, orderBy);
                String xml = DataSourceConverter.marshallOrmBean(
                        this.connector, list);

                // Determine if instructed to serialize XML to file. If file
                // name turns
                // out to be null then seiralization will not take place.
                String filename = null;
                String beanXmlFilename = ormBean.getDataSourceRoot() + ".xml";
                if (ormBean.isSerializeXml()) {
                    filename = (ormBean.getFileName() == null ? beanXmlFilename
                            : ormBean.getFileName());
                    RMT2File.serializeText(xml, filename);
                }
                results = new Object[1];
                results[0] = xml;
            }
            else {
                this.baseView = dataSourceView;
                List list = this.findData(where, orderBy);
                // Convert results to Object[].
                results = list.toArray();
            }
            return results;
        } catch (SystemException e) {
            throw new DatabaseException(
                    "Problem executing an  ORM bean fetch operation", e);
        } finally {
            // Close cursor once the results are obtained so to prevent the
            // syabse error,
            // "Resource Governor for prepared statements exceeded."
            try {
                if (this.rs != null) {
                    this.rs.close();
                }
                if (this.stmt != null) {
                    this.stmt.close();
                }
            } catch (SQLException e) {
                throw new DatabaseException(
                        "Unable to close result set and/or statement objects",
                        e);
            }
        }
    }

    /**
     * Adds data to the database using _obj as the source. Primary keys can be
     * automatically generated depending on the value of _autoKey. Currently,
     * this method only supports those situations where there is a one to one
     * mapping of a POJO and database table.
     * 
     * @param obj
     *            A POJO, which maps to some table in the database, and is
     *            required to have an associated DataSource View document.
     * @param autoKey
     *            A flag indicating whether or not primary keys are generated
     *            and made available for retrieval.
     * @return ResultSet containing the generated keys when _autoKey is equal
     *         true. Otherwise, null is returned.
     * @throws DatabaseException
     */
    public int insertRow(Object obj, boolean autoKey) throws DatabaseException {
        OrmBean ormBean = null;
        if (obj instanceof OrmBean) {
            ormBean = (OrmBean) obj;
        }
        else {
            throw new DatabaseException(
                    "Method argument must be of type RMT2BaseBean");
        }

        String sql = null;
        DaoApi dso = this;
        int rc = 0;

        try {
            // String viewName = ormBean.getDataSourceName();
            String dataSourceView = this.getDataSourcePath(ormBean);
            this.setDataSourceName(dataSourceView);
            DataSourceConverter.packageDSO(dso, ormBean);

            // Build insert statement.
            try {
                ProductBuilder builder = OrmQueryBuilderFactory.getInsertQuery(
                        this, ormBean, autoKey);
                Product sqlObj = ProductDirector.construct(builder);
                sql = sqlObj.toString();
            } catch (ProductBuilderException e) {
                throw new DatabaseException(e);
            }

            logger.log(Level.INFO, "Exceuting Insert SQL: " + sql);
            rc = this.executeUpdate(sql, autoKey);
            return rc;
        } catch (SystemException e) {
            throw new DatabaseException(
                    "Problem executing an ORM bean Insert operation", e);
        }
    }

    /**
     * Modifies one record contained in a table residing in the database based
     * on the values contained in _obj. Currently, this method only supports
     * those situations where there is a one to one mapping of a POJO and
     * database table.
     * 
     * @param _obj
     *            A POJO, which maps to some table in the database, and is
     *            required to have an associated DataSource View document.
     * @return A count of the number of rows effected by this operation when
     *         _autoKey equals false. Otherwise, returns the auto-generated id.
     * @throws DatabaseException
     */
    public int updateRow(Object _obj) throws DatabaseException {
        OrmBean ormBean = null;
        if (_obj instanceof OrmBean) {
            ormBean = (OrmBean) _obj;
        }
        else {
            throw new DatabaseException(
                    "Method argument must be of type RMT2BaseBean");
        }

        String sql = null;
        DaoApi dso = this;
        int rc = 0;

        try {
            // String viewName = ormBean.getDataSourceName();
            // this.setDataSourceName(viewName);
            String dataSourceView = this.getDataSourcePath(ormBean);
            this.setDataSourceName(dataSourceView);
            DataSourceConverter.packageDSO(dso, ormBean);

            // Build update statement
            try {
                ProductBuilder builder = OrmQueryBuilderFactory.getUpdateQuery(
                        this, ormBean);
                Product sqlObj = ProductDirector.construct(builder);
                sql = sqlObj.toString();
            } catch (ProductBuilderException e) {
                throw new DatabaseException(e);
            }

            logger.log(Level.INFO, "Exceuting Update SQL: " + sql);
            rc = this.executeUpdate(sql);
            return rc;
        } catch (SystemException e) {
            throw new DatabaseException(
                    "Problem executing an ORM bean Update operation", e);
        }
    }

    /**
     * Deleted one record contained in a table residing in the database based on
     * the values contained in _obj. Currently, this method only supports those
     * situations where there is a one to one mapping of a POJO and database
     * table.
     * 
     * @param _obj
     *            A POJO, which maps to some table in the database, and is
     *            required to have an associated DataSource View document.
     * @return A count of the number of rows effected by this operation.
     * @throws DatabaseException
     */
    public int deleteRow(Object _obj) throws DatabaseException {
        OrmBean ormBean = null;
        if (_obj instanceof OrmBean) {
            ormBean = (OrmBean) _obj;
        }
        else {
            this.msg = "Method argument must be of type RMT2BaseBean";
            logger.error(this.msg);
            throw new DatabaseException(this.msg);
        }

        String sql = null;
        DaoApi dso = this;
        int rc = 0;

        try {
            // String viewName = ormBean.getDataSourceName();
            // this.setDataSourceName(viewName);
            String dataSourceView = this.getDataSourcePath(ormBean);
            this.setDataSourceName(dataSourceView);
            DataSourceConverter.packageDSO(dso, ormBean);

            // Build delete statement
            try {
                ProductBuilder builder = OrmQueryBuilderFactory.getDeleteQuery(
                        this, ormBean);
                Product sqlObj = ProductDirector.construct(builder);
                sql = sqlObj.toString();
            } catch (ProductBuilderException e) {
                throw new DatabaseException(e);
            }

            logger.log(Level.INFO, "Exceuting SQL: " + sql);
            rc = this.executeUpdate(sql);
            return rc;
        } catch (SystemException e) {
            throw new DatabaseException(
                    "Problem executing an ORM bean Delete operation", e);
        }
    }

    /**
     * Creates and returns to the caller a DataSource object to be used as an
     * Object Relational Mapping manager.
     * 
     * @return {@link RMT2DataSourceApi}
     */
    protected DaoApi getOrmDataSource() {
        DaoApi dso = null;
        if (this.connector == null) {
            return null;
        }
        dso = DataSourceFactory.createDao(this.connector);
        return dso;
    }

    /**
     * Retrieves data from the database as xml using custom selection criteria.
     * <p>
     * When _xmlFileName is not null, the xml data is persisted to disk as
     * _xmlFileName, and the location of the output is determined by the
     * serialization properties in the SystemParms. properties file. Otherwise,
     * the persistence operation is skipped.
     * 
     * @param _whereClause
     *            SQL where clasue to provide a condition used to restrict the
     *            result set of data.
     * @param _orderByClause
     *            SQL order by clasue. Additionally, the order by clause may
     *            have an appended xml clause that instructs the database
     *            management system to output results as XML.
     * @param _xmlFileName
     *            The output file name without the path.
     * @return String of XML data.
     * @throws SystemException
     */
    protected final String findXmlData(String _whereClause,
            String _orderByClause, String _xmlFileName) throws SystemException {
        StringBuilder xml = new StringBuilder(1000);
        String data = null;
        this.resultType = DaoApi.RESULTSET_TYPE_XML;
        this.executeDso(_whereClause, _orderByClause);

        // Build XML Root Tags. Try to create tags with datasources assoicated
        // table name.
        // Otherwise, use default tag value, "dataitem".
        String rootTagStart = DataSourceConst.XML_TAG_START;
        String rootTagEnd = DataSourceConst.XML_TAG_END;
        ObjectMapperAttrib attrib = this.getDataSourceAttib();
        Hashtable tables = attrib.getTables();
        if (tables != null) {
            try {
                TableUsageBean tableBean = (TableUsageBean) tables.elements()
                        .nextElement();
                String viewname = tableBean.getDbName();
                viewname = RMT2Utility.formatDsName(viewname);
                rootTagStart = "<" + viewname + ">";
                rootTagEnd = "</" + viewname + ">";
            } catch (NoSuchElementException e) {
                logger.log(Level.WARN,
                        "Table name could not be matched with ORM Bean, "
                                + this.getBaseClass());
            }
        }

        try {
            while (this.nextRow()) {
                xml.append(this.getRs().getString(1));
            }
            data = xml.toString();
            data = rootTagStart + data + rootTagEnd;
            if (_xmlFileName != null) {
                RMT2File.serializeText(data, _xmlFileName);
            }
            return data;
        } catch (Exception e) {

            throw new SystemException(
                    "Problem retrieving a XML resultset directly from the database via the ORM bean",
                    e);
        }
    }

    /**
     * Sets the result set type.
     * 
     * @param value
     */
    public void setResultType(int value) {
        this.resultType = value;
    }

    /**
     * Gets the result set type.
     * 
     * @return int
     */
    public int getResultType() {
        return this.resultType;
    }

    /**
     * There is no concreted use for this method. Relies totally on the internal
     * functionality of the ancestor.
     * 
     * @return true if the row is valid and false if there are no more rows
     * @throws DatabaseException
     * @throws SystemException
     */
    public boolean nextRow() throws DatabaseException, SystemException {
        return super.nextRow();
    }

    /**
     * There is no concreted use for this method. Relies totally on the internal
     * functionality of the ancestor.
     * 
     * @return true if the row is valid and false if there are no more rows
     * @throws DatabaseException
     * @throws SystemException
     */
    public boolean previousRow() throws DatabaseException, SystemException {
        return super.previousRow();
    }

    /**
     * There is no concreted use for this method. Relies totally on the internal
     * functionality of the ancestor.
     * 
     * @return true if the row is valid and false if there are no rows in the
     *         dataset
     * @throws DatabaseException
     * @throws SystemException
     */
    public boolean firstRow() throws DatabaseException, SystemException {
        return super.firstRow();
    }

    /**
     * There is no concreted use for this method. Relies totally on the internal
     * functionality of the ancestor.
     * 
     * @return true if the row is valid and false if there are no rows in the
     *         dataset
     * @throws DatabaseException
     * @throws SystemException
     */
    public boolean lastRow() throws DatabaseException, SystemException {
        return super.lastRow();
    }

} // end class

