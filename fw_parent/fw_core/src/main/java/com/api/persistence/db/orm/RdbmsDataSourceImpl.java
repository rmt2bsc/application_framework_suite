package com.api.persistence.db.orm;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

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
import com.api.config.AppPropertyPool;
import com.api.config.ConfigConstants;
import com.api.constants.RMT2SystemExceptionConst;
import com.api.persistence.DatabaseException;
import com.api.persistence.db.AbstractDatabasePersistenceImpl;
import com.api.persistence.db.DatabaseConnectionBean;
import com.api.persistence.db.DbSqlConst;
import com.api.persistence.db.DynamicSqlApi;
import com.api.persistence.db.DynamicSqlFactory;
import com.api.persistence.db.orm.bean.DataSourceColumn;
import com.api.persistence.db.orm.bean.ObjectMapperAttrib;
import com.api.persistence.db.orm.bean.TableUsageBean;
import com.api.persistence.db.orm.query.OrmQueryBuilderFactory;
import com.api.security.RMT2TagQueryBean;
import com.api.util.RMT2Date;
import com.api.util.RMT2Money;
import com.api.util.RMT2String;
import com.api.xml.parsers.datasource.RMT2OrmDatasourceParser;
import com.api.xml.parsers.datasource.RMT2OrmDatasourceParserFactory;

/**
 * Implements the DataSourceApi interface which provides methods that interface
 * with a relational database via a DataSource View.
 * 
 * @author roy.terrell
 * 
 */
public class RdbmsDataSourceImpl extends AbstractDatabasePersistenceImpl
        implements DataSourceApi {
    private List sql;

    private String selectSql;

    private String updateSql;

    private String insertSql;

    private String deleteSql;

    private RMT2TagQueryBean queryData;

    private ObjectMapperAttrib datasource;

    private boolean rsScrollSensitive;

    protected boolean returnRowCount;

    /** The name of the DataSource view */
    protected String baseView;

    /** The name of the DataSource class */
    protected String baseBeanClass;

    private static Logger logger = Logger.getLogger("RdbmsDataSourceImpl");

    /**
     * Scrollable indicator that is to be used with the internal result set
     * object
     */
    protected boolean rsScrollable;

    /**
     * Upateable indicator that is to be used with the internal result set
     * object
     */
    protected boolean rsUpdatable;

    protected DynamicSqlApi dynaApi;

    /**
     * Default constructor.
     * 
     * @throws DatabaseException
     * @throws SystemException
     */
    public RdbmsDataSourceImpl() throws DatabaseException, SystemException {

        super();
        this.sql = null;
        this.selectSql = null;
        this.updateSql = null;
        this.insertSql = null;
        this.deleteSql = null;
        this.stmt = null;
        this.rsScrollable = false;
        this.rsUpdatable = false;
        this.queryData = null;
    }

    /**
     * Initializes a RdbmsDataSourceImpl object using a
     * {@link DatabaseConnectionBean} object.
     * 
     * @param dbConn
     * @throws DatabaseException
     * @throws SystemException
     */
    public RdbmsDataSourceImpl(DatabaseConnectionBean dbConn)
            throws DatabaseException, SystemException {
        super(dbConn);
        this.dynaApi = DynamicSqlFactory.create(dbConn);
        return;
    }

    /**
     * Initializes a RdbmsDataSourceImpl object using a JDBC Connection object
     * and a loginId.
     * 
     * @param _con
     * @param _loginId
     * @throws DatabaseException
     * @throws SystemException
     */
    public RdbmsDataSourceImpl(Connection _con, String _loginId)
            throws DatabaseException, SystemException {
        super(_con, _loginId);
        this.initApi();
    } // end RMT2SQLAPIImpl

    /**
     * Initializes a RdbmsDataSourceImpl object with a Connection object, a
     * loginId, a RMT2TagQueryBean, and a ResourceBundle. Subsequently
     * determines the type of and initializes the datasource that will be used.
     * 
     * @param _con
     * @param _loginId
     * @param _queryData
     * @param _sysData
     * @throws NotFoundException
     * @throws DatabaseException
     * @throws SystemException
     */
    public RdbmsDataSourceImpl(Connection _con, String _loginId,
            RMT2TagQueryBean _queryData) throws NotFoundException,
            DatabaseException, SystemException {
        this(_con, _loginId);
        if (_queryData.getQuerySource() == null) {
            msg = RMT2SystemExceptionConst.MSG_DATASOURCE_INVALID
                    + " for user: " + this.loginId;
            logger.log(Level.ERROR, msg);
            throw new SystemException(this.msg);
        }
        if (_queryData.getQueryType() == null) {
            msg = RMT2SystemExceptionConst.MSG_NULL_DATASOURCE_TYPE
                    + " for user: " + this.loginId;
            logger.log(Level.ERROR, msg);
            throw new SystemException(this.msg);
        }

        if (!DbSqlConst.DATASOURCE_TYPES.containsValue(_queryData
                .getQueryType())) {
            msg = RMT2SystemExceptionConst.MSG_DATASOURCE_TYPE_INVALID
                    + " for user: " + this.loginId;
            logger.log(Level.ERROR, msg);
            throw new SystemException(this.msg);
        }

        this.queryData = _queryData;

        // get XML DataSource
        if (_queryData.getQueryType().equalsIgnoreCase("xml")) {
            this.parseXmlDataSource(_queryData);
            this.baseView = this.queryData.getQuerySource();
        }
        // execute SQL DataSource
        if (_queryData.getQueryType().equalsIgnoreCase("sql")) {
            this.parseSqlDataSource(_queryData);
        }

    } // end RMT2SQLAPIImpl

    // /**
    // * Initializes a RdbmsDataSourceImpl object using a
    // * DatabaseConnectionBean object and a HttpServletRequest object.
    // *
    // * @param dbConn
    // * @param req
    // * @throws DatabaseException
    // * @throws SystemException
    // */
    // public RdbmsDataSourceImpl(DatabaseConnectionBean dbConn, Request req)
    // throws DatabaseException, SystemException {
    // super(dbConn, req);
    // return;
    // }

    /**
     * Perform post initialization of a this api which defaults the internal
     * ResultSet to be scroll sensitive.
     * 
     * @throws DatabaseException
     * @throws SystemException
     */
    protected void initApi() throws DatabaseException, SystemException {
        super.initApi();
        this.rsScrollSensitive = false;
    } // End init

    /*
     * (non-Javadoc)
     * 
     * @see com.api.db.DatabaseTransImpl#close()
     */
    @Override
    public void close() throws DatabaseException {
        if (this.datasource != null) {
            this.datasource.close();
        }
        if (this.dynaApi != null) {
            this.dynaApi.close();
        }
        this.datasource = null;
        this.sql = null;
        this.selectSql = null;
        this.updateSql = null;
        this.insertSql = null;
        this.deleteSql = null;
        this.stmt = null;
        this.rsScrollable = false;
        this.rsUpdatable = false;
        this.queryData = null;
        super.close();
    }

    /**
     * Parses the document that is references as a filename within the
     * RMT2TagQueryBean. The system resource information is contained in
     * _sysData which is used to setup and initialize the XML SAX Parser. Upon
     * successfully parsing the document, the member variable, datasource, is
     * initialized with the results of the parsed document.
     * 
     * @param _queryData
     * @param _sysData
     * @return 1
     * @throws NotFoundException
     * @throws DatabaseException
     * @throws SystemException
     */
    protected int parseXmlDataSource(RMT2TagQueryBean _queryData)
            throws NotFoundException, DatabaseException, SystemException {
        logger.log(Level.DEBUG, "Begin to parse XML document");
        RMT2OrmDatasourceParserFactory f = RMT2OrmDatasourceParserFactory
                .getNewInstance();
        RMT2OrmDatasourceParser api = f.getSax1OrmDatasourceParser(_queryData);
        this.datasource = api.parseDocument();
        return 1;
    }

    /**
     * Parses the DataSource document that is references as _dataSourceName
     * 
     * @param dataSourceName
     * @return 1
     * @throws NotFoundException
     * @throws DatabaseException
     * @throws SystemException
     */
    protected int parseXmlDataSource(String dataSourceName)
            throws NotFoundException, DatabaseException, SystemException {
        RMT2OrmDatasourceParserFactory f = RMT2OrmDatasourceParserFactory
                .getNewInstance();
        RMT2OrmDatasourceParser api = f
                .getSax2OrmDatasourceParser(dataSourceName);
        this.datasource = api.parseDocument();

        this.baseView = dataSourceName;
        return 1;
    }

    /**
     * Accepts and executes an sql select statement contained in
     * RMT2TagQueryData. The datasource is constructed using the RestulSet's
     * metadata from the query.
     * 
     * @param _queryData
     * @param _sysData
     * @return The total number of column definitions captured.
     * @throws DatabaseException
     * @throws SystemException
     */
    protected int parseSqlDataSource(RMT2TagQueryBean _queryData)
            throws DatabaseException, SystemException {
        try {
            this.executeQuery(_queryData.getQuerySource());
            ResultSetMetaData rsmd = this.rs.getMetaData();
            int colCount = rsmd.getColumnCount();
            if (colCount <= 0) {
                return colCount;
            }

            if (this.selectSql != null) {
                this.disassembleQuery(this.selectSql);
            }
            Hashtable columnDef = new Hashtable();
            for (int ndx = 1; ndx <= colCount; ndx++) {
                String colName = rsmd.getColumnName(ndx);
                int colJavaType = rsmd.getColumnType(ndx);
                String colSqlType = rsmd.getColumnTypeName(ndx);
                int colIsNullable = rsmd.isNullable(ndx);
                DataSourceColumn colObj = new DataSourceColumn();
                colObj.setName(colName);
                colObj.setDbName(colName);
                colObj.setJavaType(colJavaType);
                colObj.setSqlType(colSqlType);
                switch (colIsNullable) {
                    case ResultSetMetaData.columnNoNulls:
                        colObj.setNullable(false);
                        break;
                    case ResultSetMetaData.columnNullable:
                    case ResultSetMetaData.columnNullableUnknown:
                        colObj.setNullable(true);
                        break;
                }
                // Add column object to Hashtable
                columnDef.put(colName, colObj);
            }

            this.datasource.setColumnDef(columnDef);

            return columnDef.size();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Examine a Sql Select statement and parses it into ObjectMapperAttrib
     * object.
     * 
     * @param sql
     * @throws SystemException
     */
    private void disassembleQuery(String sql) throws SystemException {
        try {
            ProductBuilder builder = OrmQueryBuilderFactory.getSelectQuery(sql);
            Product sqlObj = ProductDirector.deConstruct(builder);
            Hashtable obj = (Hashtable) sqlObj.toObject();
            this.datasource.setSelectStatement(obj);
        } catch (ProductBuilderException e) {
            throw new SystemException(e);
        }
    }

    /**
     * Build a Sql Select statement based on the values stored in target
     * datasource view document.
     * 
     * @return SQL statement
     * @throws SystemException
     * @deprecated Will be removed in future releases. Logic will be moved to
     *             the assembleQuery method of
     *             {@link com.api.db.orm.RdbmsDaoImpl RdbmsDaoImpl} once all
     *             applications have be refactored to rely on this class via
     *             RdbmsDaoImpl for database interaction.
     */
    protected String assembleQuery() throws SystemException {
        try {
            ProductBuilder builder = OrmQueryBuilderFactory
                    .getSelectQuery(this);
            Product sqlObj = ProductDirector.construct(builder);
            String sql = sqlObj.toString();
            return sql;
        } catch (ProductBuilderException e) {
            throw new SystemException(e);
        }
    } // End close

    /**
     * Releases the database and JDBC resources immediately instead of waiting
     * for them to be garbae collectd. Extend this method at the decendent level
     * if any other resources are needed to be released.
     * 
     * @throws DatabaseException
     * @throws SystemException
     */
    protected void releaseDBResources() throws DatabaseException,
            SystemException {
        this.selectSql = null;
        this.insertSql = null;
        this.updateSql = null;
        this.deleteSql = null;
    }

    /**
     * Generates the next unique primary key for an associated table.
     * 
     * @param tableName
     * @param keyName
     * @return int > 0 Generated value, -1 Sequence name was not provided, -100
     *         SQL Exception, and -200 General Exception.
     * @throws DatabaseException
     * @throws SystemException
     */
    protected int getNextSequence(String tableName, String keyName)
            throws DatabaseException, SystemException {

        StringBuffer sql = new StringBuffer(100);
        int key = 0;

        if (tableName.length() <= 0) {
            return -1;
        }

        try {
            sql.append("select max(");
            sql.append(keyName);
            sql.append(") as \"key\"");
            sql.append(" from ");
            sql.append(tableName);

            Statement s = this.getConnection().createStatement();
            ResultSet r = s.executeQuery(sql.toString());
            // this.executeQuery(sql.toString());
            if (r.next()) {
                key = r.getInt("key");
                key++;
            }

            s.close();

            return key;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        } catch (Exception e) {
            throw new SystemException(
                    "Unable to generate next sequence number for primary key",
                    e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public ResultSet executeQuery() throws DatabaseException, SystemException {
        try {
            this.selectSql = this.assembleQuery();

            this.rsScrollable = false;
            this.stmt = this.getJDBCStatementObject();
            // this.stmt = con.createStatement((this.rsScrollable ?
            // ResultSet.TYPE_SCROLL_INSENSITIVE : ResultSet.TYPE_FORWARD_ONLY),
            // (this.rsUpdatable ? ResultSet.CONCUR_UPDATABLE :
            // ResultSet.CONCUR_READ_ONLY));

            logger.log(Level.INFO, "Executing Query: " + this.baseView);
            logger.log(Level.INFO, "Preparing to execute SQL Statement: "
                    + this.selectSql);
            this.stmt.getResultSetConcurrency();
            this.stmt.getResultSetType();

            this.rs = this.stmt.executeQuery(this.selectSql);
            return this.rs;
        } catch (Exception e) {
            msg = e.getMessage() + " - SQL: " + this.selectSql;
            if (this.baseView != null && !this.baseView.equals("")) {
                msg += " View: " + this.baseView;
            }
            logger.log(Level.ERROR, this.msg);
            throw new DatabaseException(
                    "Problem executing query which the SQL is supplied internally by the ORM Datasource configuration",
                    e);
        }
    } // End executeQuery

    /**
     * {@inheritDoc}
     */
    public ResultSet executeQuery(boolean isScrollable, boolean isUpdatable)
            throws DatabaseException, SystemException {
        Integer dbmsCode = null;
        try {
            String dbmsValue = AppPropertyPool
                    .getProperty(ConfigConstants.PROPNAME_DBMS_VENDOR);
            dbmsCode = new Integer(dbmsValue.trim());
        } catch (NumberFormatException e) {
            this.msg = "Number Format Exception: Invalid numeric string passed as an argument to Integer(String)";
            throw new SystemException(this.msg);
        }

        switch (dbmsCode.intValue()) {
            case DbSqlConst.DBMS_SYBASE_ASA_KEY:
                // For ASA, jConnect 5x does not support ResultSets
                // that are scrollable and updatable. Updatable result sets must
                // have a forward only cursor type.
                if (isScrollable && isUpdatable) {
                    // Change to Non-Scrollable and Read-Only
                    isScrollable = false;
                    isUpdatable = true;
                }
                break;

            case DbSqlConst.DBMS_SYBASE_ASE_KEY:
            case DbSqlConst.DBMS_ORACLE_KEY:
            case DbSqlConst.DBMS_SQLSERVER_KEY:
            case DbSqlConst.DBMS_DB2_KEY:
                break;

            default:
                isScrollable = false;
                isUpdatable = false;
                break;
        }
        this.rsScrollable = isScrollable;
        this.rsUpdatable = isUpdatable;
        return this.executeQuery();
    } // End executeQuery

    /**
     * {@inheritDoc}
     */
    public ResultSet executeQuery(boolean isScrollable,
            boolean isScrollSensitive, boolean isUpdatable)
            throws DatabaseException, SystemException {
        this.rsScrollSensitive = isScrollSensitive;
        return this.executeQuery(isScrollable, isUpdatable);
    } // End executeQuery

    /**
     * {@inheritDoc}
     */
    public ResultSet executeQuery(String _sql) throws DatabaseException,
            SystemException {
        if (_sql.length() <= 0) {
            this.msg = RMT2SystemExceptionConst.MSG_INVALID_SQL_STATEMENT
                    + " for user: " + this.loginId;
            throw new SystemException(msg,
                    RMT2SystemExceptionConst.RC_INVALID_SQL_STATEMENT);
        }

        try {
            this.rsScrollable = false;
            this.rsUpdatable = false;
            this.stmt = this.getJDBCStatementObject();

            this.rs = this.stmt.executeQuery(_sql);
            return this.rs;
        } catch (SQLException e) {
            this.msg = " - SQL: " + this.selectSql;
            throw new DatabaseException(
                    "Problem executing Datasource query using supplied SQL String"
                            + this.msg, e);
        }
    } // End executeQuery

    /**
     * {@inheritDoc}
     */
    public String executeXmlQuery(String sql) throws DatabaseException,
            SystemException {
        if (sql == null) {
            return null;
        }
        sql += " for xml auto, elements";
        ResultSet results = this.executeQuery(sql);
        String xml = "";
        try {
            while (results.next()) {
                xml += results.getString(1);
            }
            results.close();
            results = null;
            return xml;
        } catch (SQLException e) {
            msg = e.getMessage();
            throw new DatabaseException(
                    "Problem executing Datasource query that is supposed to yield a XML resultset using supplied SQL String",
                    e);
        }
    } // End executeQuery

    /**
     * Creates a JDBC Statement object based on the internal values of the JDBC
     * result set type and the concurrency type member variables.
     * 
     * @return JDBC Statement
     * @throws DatabaseException
     */
    protected Statement getJDBCStatementObject() throws DatabaseException {
        int rsConcurrency = 0;
        int rsType = 0;
        Statement statement = null;

        // Determine result set type
        if (this.rsScrollable) {
            if (this.rsScrollSensitive) {
                rsType = ResultSet.TYPE_SCROLL_SENSITIVE;
            }
            else {
                rsType = ResultSet.TYPE_SCROLL_INSENSITIVE;
            }
        }
        else {
            rsType = ResultSet.TYPE_FORWARD_ONLY;
        }

        // Determine concurrency
        if (this.rsUpdatable) {
            rsConcurrency = ResultSet.CONCUR_UPDATABLE;
        }
        else {
            rsConcurrency = ResultSet.CONCUR_READ_ONLY;
        }

        // Get statement
        try {
            statement = getConnection().createStatement(rsType, rsConcurrency);
            return statement;
        } catch (SQLException e) {
            this.msg = "Problem obtaining JDBC Statement object";
            logger.log(Level.ERROR, this.msg);
            throw new DatabaseException(this.msg, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public int createRow() throws DatabaseException {
        String sql = null;
        DataSourceColumn colAtt = null;

        try {
            if (this.rs == null) {
                // Try to build a condition that will initialize the ResultSet
                // object with an empty resultset so that we can create a new
                // row.
                colAtt = this.datasource.getDsoPrimaryKey();
                sql = colAtt.getDbName() + " = ";
                if (colAtt.getSqlType().equalsIgnoreCase("int")
                        || colAtt.getSqlType().equalsIgnoreCase("decimal")
                        || colAtt.getSqlType().equalsIgnoreCase("numeric")) {
                    sql += "-1";
                }
                else {
                    sql += " \'\' ";
                }
                this.setDatasourceSql(DbSqlConst.WHERE_KEY, null);
                this.setDatasourceSql(DbSqlConst.WHERE_KEY, sql);
                try {
                    this.executeQuery(true, true, true);
                } catch (SystemException e) {
                    throw new SQLException(e.getMessage());
                }
            }

            // At this point we should have a valid ResultSet object to
            // operation on. Move to insert row of the ResultSet
            this.rs.moveToInsertRow();
            return 1;
        } catch (SQLException e) {
            throw new DatabaseException(
                    "Unable to create a row in internal ORM Datasource ResultSet instance",
                    e);
        }
    } // End createRow

    /**
     * {@inheritDoc}
     */
    public int insertRow() throws DatabaseException {
        try {
            this.rs.insertRow();
            this.rs.moveToCurrentRow();
            this.dbChangeCount++;
            return 1;
        } catch (SQLException e) {
            throw new DatabaseException(
                    "Unable to insert row into internal ORM Datasource ResultSet instance",
                    e);
        }

    } // End insertRow

    /**
     * {@inheritDoc}
     */
    public int updateRow() throws DatabaseException, SystemException,
            NotFoundException {
        this.buildUpdateStatement();
        try {
            this.rs.updateRow();
            this.dbChangeCount++;
            return 1;
        } catch (SQLException e) {
            throw new DatabaseException(this.msg);
        }
    } // End updateRow

    /**
     * {@inheritDoc}
     */
    public int deleteRow() throws DatabaseException {
        this.msg = "Possible cause: record's primary key may be linked to another table as a foreign key.";
        try {
            this.rs.deleteRow();
            this.dbChangeCount++;
            return 1;
        } catch (SQLException e) {
            this.msg = "Unable to delete row from the internal ORM Datasource ResultSet instance";
            throw new DatabaseException(this.msg, e);
        }
    } // End delete

    /**
     * Describes the one of the attributes of a ResultSet object.
     * 
     * @param _rsTypeId
     *            ONe of the ResultSet Type constants.
     * @return Srting Description of the result set type
     */
    protected String getResultSetAttribute(int _rsTypeId) {
        switch (_rsTypeId) {
            case ResultSet.CONCUR_READ_ONLY:
                return "Concurrency Read Only";
            case ResultSet.CONCUR_UPDATABLE:
                return "Concurrency Updatable";
            case ResultSet.TYPE_SCROLL_INSENSITIVE:
                return "Type Scroll Insensitive";
            case ResultSet.TYPE_FORWARD_ONLY:
                return "Type Forward Only";
            case ResultSet.TYPE_SCROLL_SENSITIVE:
                return "Type Scroll Sensitive";
            default:
                return "N/A";
        } // end switch
    }

    /**
     * Constructs update statement(s) based on the datasource's columnDef
     * values. Returns the update statement that was executed.
     * 
     * @return The size of the update SQL statemnt.
     * @throws SystemException
     * @throws NotFoundException
     */
    private int buildUpdateStatement() throws SystemException,
            NotFoundException {

        Hashtable colList = this.datasource.getColumnDef();
        Hashtable tableList = this.datasource.getTables();
        TableUsageBean table = null;
        String tableDbName = null;

        this.sql = new ArrayList();
        // For each updateable table
        for (int tableNdx = 1; tableNdx <= tableList.size(); tableNdx++) {
            table = (TableUsageBean) tableList.get(new Integer(tableNdx));
            tableDbName = table.getDbName();

            // if table is not updateable, then process next table
            if (!table.isUpdateable()) {
                continue;
            }

            StringBuffer updateStatement = new StringBuffer(100);
            updateStatement.append("update " + tableDbName + " set ");

            // Cycle through all columns that are associated
            // with the current table and build update statement.
            DataSourceColumn colBean = null;
            Enumeration colKeys = colList.keys();
            String colName = null;
            StringBuffer whereClause = new StringBuffer();
            int colCount = 0;

            while (colKeys.hasMoreElements()) {
                colName = (String) colKeys.nextElement();
                colBean = (DataSourceColumn) colList.get(colName);
                if (colBean.getTableName().equalsIgnoreCase(tableDbName)
                        && colBean.isUpdateable()) {
                    colCount += this.getUpdateDmlAssignment(colBean, colCount,
                            updateStatement);
                }
                if (colBean.isPrimaryKey()) {
                    this.getUpdateDmlWhereClause(colBean, whereClause);
                }
            }
            if (whereClause.length() > 0) {
                updateStatement.append(whereClause.toString());
            }
            this.sql.add(updateStatement.toString());
            updateStatement = null;
            whereClause = null;

        }
        return this.sql.size();
    }

    /**
     * Constructs the set statement for the update Statement based on the value
     * contained in the datasource object.
     * 
     * @param _colBean
     * @param _colCount
     * @param _sql
     * @return 1 for success.
     */
    private int getUpdateDmlAssignment(DataSourceColumn _colBean,
            int _colCount, StringBuffer _sql) {
        String colDbName = _colBean.getDbName();
        Object colValue = _colBean.getDataValue();
        String prefix = null;

        if (_colCount > 0) {
            prefix = ", ";
        }
        else {
            prefix = " ";
        }

        // if column value is null
        if (colValue == null) {
            if (_colBean.isNullable()) {
                _sql.append(prefix);
                _sql.append(colDbName);
                _sql.append(" = null  ");
                return 1;
            }
            return 0;
        }

        return this.buildDml(_colBean, _sql, prefix, "set");
    }

    /**
     * Sets the value of a SQL Select Statement clause contained in the
     * datasource object.
     * 
     * @param _colBean
     * @param _where
     * @return always zero
     */
    private int getUpdateDmlWhereClause(DataSourceColumn _colBean,
            StringBuffer _where) {

        String prefix = null;

        if (_where.toString().toLowerCase().indexOf("where ") == -1) {
            prefix = " where ";
        }
        else {
            prefix = " and ";
        }
        this.buildDml(_colBean, _where, prefix, "where");

        return 0;
    }

    /**
     * Constructs the set clause or the where clause of an update statement
     * based on the values contained in DataSourceColumn for the current column.
     * 
     * @param _colBean
     * @param _sql
     * @param _prefix
     * @param _type
     * @return 1 for success
     */
    private int buildDml(DataSourceColumn _colBean, StringBuffer _sql,
            String _prefix, String _type) {

        String colDbName = _colBean.getDbName();
        Object colValue = _colBean.getDataValue();
        int colType = _colBean.getJavaType();

        switch (colType) {
            case Types.INTEGER:
            case Types.FLOAT:
            case Types.DOUBLE:
                _sql.append(_prefix);
                _sql.append(colDbName);
                _sql.append(" = ");
                _sql.append(colValue.toString());
                break;

            case Types.DATE:
                _sql.append(_prefix);
                if (_type.equalsIgnoreCase("where")) {
                    _sql.append("date(");
                }
                _sql.append(colDbName);
                if (_type.equalsIgnoreCase("where")) {
                    _sql.append(")");
                }
                _sql.append(" = ");
                _sql.append("date(");
                _sql.append("\'");
                _sql.append(colValue.toString());
                _sql.append("\'");
                _sql.append(")");
                break;

            case Types.TIME:
            case Types.TIMESTAMP:
                _sql.append(_prefix);
                if (_type.equalsIgnoreCase("where")) {
                    _sql.append("datetime(");
                }
                _sql.append(colDbName);
                if (_type.equalsIgnoreCase("where")) {
                    _sql.append(")");
                }
                _sql.append(" = ");
                _sql.append("datetime(");
                _sql.append("\'");
                _sql.append(colValue.toString());
                _sql.append("\'");
                _sql.append(")");
                break;

            case Types.VARCHAR:
                _sql.append(_prefix);
                _sql.append(colDbName);
                _sql.append(" = ");
                _sql.append("\'");
                _sql.append(colValue.toString());
                _sql.append("\'");
                break;
        } // end switch

        return 1;

    }

    /**
     * {@inheritDoc}
     */
    public String getDatasourceSql(int sqlKey) {
        try {
            return this.datasource.getSqlAttribute(sqlKey);
        } catch (NotFoundException e) {
            return null;
        } catch (SystemException e) {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void setDatasourceSql(int sqlKey, String value) {
        try {
            this.datasource.setSqlAttribute(sqlKey, value);
        } catch (NotFoundException e) {
        } catch (SystemException e) {
        }
    }

    /**
     * Determines the data type of the expression, _type. Returns
     * java.sql.Types.
     * 
     * @param _type
     * @return int as the java type.
     */
    protected final int getJavaType(String _type) {

        if (_type.equalsIgnoreCase("java.lang.Integer")
                || _type.equalsIgnoreCase("java.lang.Long")
                || _type.equalsIgnoreCase("java.sql.Types.INTEGER")
                || _type.equalsIgnoreCase("java.sql.Types.BIGINT")
                || _type.equalsIgnoreCase("java.sql.Types.SMALLINT")
                || _type.equalsIgnoreCase("java.sql.Types.TINYINT")
                || _type.equalsIgnoreCase("integer")
                || _type.equalsIgnoreCase("int"))
            return Types.INTEGER;

        else if (_type.equalsIgnoreCase("java.lang.String")
                || _type.equalsIgnoreCase("java.sql.Types.CHAR")
                || _type.equalsIgnoreCase("java.sql.Types.VARCHAR")
                || _type.equalsIgnoreCase("java.sql.Types.LONGVARCHAR")
                || _type.equalsIgnoreCase("varchar")
                || _type.equalsIgnoreCase("char"))
            return Types.VARCHAR;

        else if (_type.equalsIgnoreCase("java.lang.Float")
                || _type.equalsIgnoreCase("java.sql.Types.FLOAT")
                || _type.equalsIgnoreCase("java.sql.Types.REAL")
                || _type.equalsIgnoreCase("float")
                || _type.equalsIgnoreCase("real"))
            return Types.FLOAT;

        else if (_type.equalsIgnoreCase("java.lang.Double")
                || _type.equalsIgnoreCase("java.sql.Types.DOUBLE")
                || _type.equalsIgnoreCase("java.sql.Types.DECIMAL")
                || _type.equalsIgnoreCase("java.sql.Types.NUMERIC")
                || _type.equalsIgnoreCase("double")
                || _type.equalsIgnoreCase("decimal")
                || _type.equalsIgnoreCase("numeric")
                || _type.equalsIgnoreCase("number"))
            return Types.DOUBLE;

        else if (_type.equalsIgnoreCase("java.lang.Date")
                || _type.equalsIgnoreCase("java.sql.Types.DATE")
                || _type.equalsIgnoreCase("date"))
            return Types.DATE;

        else if (_type.equalsIgnoreCase("java.sql.Types.TIME")
                || _type.equalsIgnoreCase("time"))
            return Types.TIME;

        else if (_type.equalsIgnoreCase("java.sql.Types.Timestamp")
                || _type.equalsIgnoreCase("timestamp"))
            return Types.TIMESTAMP;

        else
            return -1;

    }

    /**
     * {@inheritDoc}
     */
    public boolean firstRow() throws DatabaseException, SystemException {
        if (this.rs == null) {
            return false;
        }
        try {
            if (this.rs.first()) {
                this.refreshDataSource();
            }
            else {
                return false;
            }
            return true;
        } catch (Exception e) {
            throw new DatabaseException(
                    "Problem occured when attempting to position the internal ResultSet cursor of the ORM Datasource to the first row",
                    e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean lastRow() throws DatabaseException, SystemException {
        if (this.rs == null) {
            return false;
        }
        try {
            if (this.rs.last()) {
                this.refreshDataSource();
            }
            else {
                return false;
            }
            return true;
        } catch (Exception e) {
            throw new DatabaseException(
                    "Problem occured when attempting to position the internal ResultSet cursor of the ORM Datasource to the last row",
                    e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean nextRow() throws DatabaseException, SystemException {
        if (this.rs == null) {
            return false;
        }
        try {
            if (this.rs.next()) {
                this.refreshDataSource();
            }
            else {
                return false;
            }
            return true;
        } catch (Exception e) {
            throw new DatabaseException(
                    "Problem occured when attempting to position the internal ResultSet cursor of the ORM Datasource to the next row",
                    e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean previousRow() throws DatabaseException, SystemException {
        if (this.rs == null) {
            return false;
        }
        try {
            if (this.rs.previous()) {
                this.refreshDataSource();
            }
            else {
                return false;
            }
            return true;
        } catch (SQLException e) {
            throw new DatabaseException(
                    "Problem occured when attempting to position the internal ResultSet cursor of the ORM Datasource to the previous row",
                    e);
        }
    }

    /**
     * Resets the DataSource by assigning the datasource new primary key values
     * from the current row of the result set. This is typically performed after
     * a call from: firstRow(), lastRow(), nextRow(), and previousRow(). Returns
     * a count of all the datasource's primary keys that were updated.
     * 
     * @return Total number of column definitions processed
     * @throws DatabaseException
     * @throws NotFoundException
     * @throws SystemException
     */
    protected int refreshDataSource() throws DatabaseException,
            NotFoundException, SystemException {

        // Exit on the possibility that this API was created without a
        // datasource
        if (this.datasource == null) {
            return 0;
        }

        Hashtable colList = this.datasource.getColumnDef();
        Hashtable tableList = this.datasource.getTables();
        TableUsageBean table = null;
        String tableDbName = null;
        int keyCount = 0;

        this.sql = new ArrayList();

        // For each updateable table
        for (int tableNdx = 1; tableNdx <= tableList.size(); tableNdx++) {
            table = (TableUsageBean) tableList.get(new Integer(tableNdx));
            tableDbName = table.getName();

            // if table is not updateable, then process next table
            if (!table.isUpdateable()) {
                continue;
            }

            // Cycle through all columns that are associated
            // with the current table and build update statement.
            DataSourceColumn colBean = null;
            Enumeration colKeys = colList.keys();
            String colName = null;
            Object colValue = null;

            while (colKeys.hasMoreElements()) {
                colName = (String) colKeys.nextElement();
                colBean = (DataSourceColumn) colList.get(colName);
                if (colBean.getTableName().equalsIgnoreCase(tableDbName)) {
                    if (colBean.isPrimaryKey()) {
                        colValue = this.getColumnValue(colName);
                        colBean.setDataValue(colValue);
                        colList.put(colName, colBean);
                        keyCount++;
                    } // end if
                } // end if
            } // end while
        } // end for

        this.datasource.setColumnDef(colList);
        return keyCount;
    } // end refreshDataSource

    /**
     * {@inheritDoc}
     */
    public final String getColumnValue(String _property)
            throws DatabaseException, NotFoundException, SystemException {
        String value = null;
        Integer dataType = null;
        String dbName = null;

        if (_property == null) {
            throw new DatabaseException(
                    "Input parameter, _property, is required and cannot be null");
        }

        if (this.datasource == null) {
            logger.warn("The DOS's internal datasource object is null");
            return null;
        }
        try {
            dataType = (Integer) this.datasource.getColumnAttribute(_property,
                    "javaType");
            dbName = (String) this.datasource.getColumnAttribute(_property,
                    "dbName");
            if (dbName == null) {
                this.msg = " ORM column, " + _property
                        + ", is not apart of the ORM datasource configuration";
                throw new NotFoundException(this.msg);
            }
        } catch (NotFoundException e) {
            this.msg = this.baseView + " failure. " + e.getMessage();
            logger.log(Level.DEBUG, this.msg);
            throw new NotFoundException(
                    "ORM Datasource referenced property name or type was not found",
                    e);
        }

        try {
            switch (dataType.intValue()) {
                case Types.INTEGER:
                case Types.TINYINT:
                case Types.SMALLINT:
                    value = new Integer(this.rs.getInt(dbName)).toString();
                    break;

                case Types.BIGINT:
                    value = new Long(this.rs.getLong(dbName)).toString();
                    break;

                case Types.CHAR:
                case Types.VARCHAR:
                case Types.LONGVARCHAR:
                    value = this.rs.getString(dbName);
                    break;

                case Types.REAL:
                case Types.FLOAT:
                    value = new Float(this.rs.getFloat(dbName)).toString();
                    break;

                case Types.NUMERIC:
                case Types.DOUBLE:
                case Types.DECIMAL:
                    value = new Double(this.rs.getDouble(dbName)).toString();
                    break;

                case Types.DATE:
                    // Inerpret DATE type as TIMESTAMP
                    java.sql.Timestamp dateValue = this.rs.getTimestamp(dbName);
                    value = (dateValue == null ? "" : dateValue.toString());
                    break;

                case Types.TIME:
                    java.sql.Time timeValue = this.rs.getTime(dbName);
                    value = (timeValue == null ? null : timeValue.toString());
                    break;

                case Types.TIMESTAMP:
                    java.sql.Timestamp timestampValue = this.rs
                            .getTimestamp(dbName);
                    value = (timestampValue == null ? "" : timestampValue
                            .toString());
                    break;

            } // end switch

            return (value == null ? null : value.trim());

        } // end try

        catch (SQLException e) {
            String extraMsg = "JDBC error occurred acessing ORM Datasource column data - Target Column: "
                    + _property
                    + " DBName: "
                    + dbName
                    + "  SQL Message: "
                    + e.getMessage();
            throw new DatabaseException(extraMsg, e);
        } catch (NullPointerException e) {
            value = "";
            return value;
        }
    }

    /**
     * {@inheritDoc}
     */
    public final String getColumnValue(String _property, String _format)
            throws DatabaseException, NotFoundException, SystemException {
        Integer dataType = null;
        String dbName = null;
        String value = null;

        try {
            dataType = (Integer) this.datasource.getColumnAttribute(_property,
                    "javaType");
            dbName = (String) this.datasource.getColumnAttribute(_property,
                    "dbName");
            value = this.getColumnValue(_property);
        } catch (NotFoundException e) {
            this.msg = this.baseView + " failure. " + e.getMessage();
            logger.log(Level.DEBUG, this.msg);
            throw new NotFoundException(
                    "ORM Datasource referenced property name, db name, or java type was not found",
                    e);
        }

        if (value == null) {
            return null;
        }
        if (value.equals("")) {
            return null;
        }

        // If formatting is not required, then return value to caller
        if (_format == null) {
            return value;
        }

        // Try to format value and return to caller
        try {
            switch (dataType.intValue()) {
                case Types.INTEGER:
                case Types.TINYINT:
                case Types.SMALLINT:
                case Types.BIGINT:
                case Types.REAL:
                case Types.FLOAT:
                case Types.NUMERIC:
                case Types.DOUBLE:
                case Types.DECIMAL:
                    value = RMT2Money.formatNumber(new Double(value), _format);
                    break;

                case Types.DATE:
                    value = RMT2Date.formatDate(this.rs.getDate(dbName),
                            _format);
                    break;

                case Types.TIME:
                    break;

                case Types.TIMESTAMP:
                    break;
            } // end switch

            return value;
        } // end try
        catch (SQLException e) {
            throw new DatabaseException(
                    "Formatting error occurred for referenced ORM Datasource property"
                            + _property + ".  Format: " + _format, e);
        }
    }

    public final Object getColumnBinaryValue(String property)
            throws DatabaseException, NotFoundException, SystemException {
        InputStream is = null;
        Integer dataType = null;
        String dbName = null;

        try {
            dataType = (Integer) this.datasource.getColumnAttribute(property,
                    "javaType");
            dbName = (String) this.datasource.getColumnAttribute(property,
                    "dbName");
        } catch (NotFoundException e) {
            this.msg = this.baseView + " failure. " + e.getMessage();
            logger.log(Level.DEBUG, this.msg);
            throw new NotFoundException(
                    "ORM Datasource referenced property name, db name, or java type was not found when trying to obtain binary value",
                    e);
        }

        try {
            return this.rs.getObject(dbName);
        } // end try

        catch (SQLException e) {
            String extraMsg = "JDBC error occurred acessing ORM Datasource column binary data - Target Column: "
                    + property
                    + " DBName: "
                    + dbName
                    + "  SQL Message: "
                    + e.getMessage();
            throw new DatabaseException(extraMsg);
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public final void setColumnValue(String _property, Object value)
            throws SystemException, NotFoundException, DatabaseException {
        int sqlConcurrency = 0;

        try {
            sqlConcurrency = this.rs.getConcurrency();
        } catch (SQLException e) {
        }

        Integer dataType = (Integer) this.datasource.getColumnAttribute(
                _property, "javaType");
        String dbName = (String) this.datasource.getColumnAttribute(_property,
                "dbName");

        if (_property == null) {
            throw new SystemException(
                    "The property to obtain mapped value is invalid");
        }
        this.datasource.setColumnAttribute(_property, value);

        if (sqlConcurrency == ResultSet.CONCUR_READ_ONLY) {
            return;
        }

        try {
            if (value == null || value.toString().length() == 0) {
                this.rs.updateNull(dbName);
                return;
            }

            switch (dataType.intValue()) {
                case Types.INTEGER:
                case Types.TINYINT:
                case Types.SMALLINT:
                    // int iValue = new Integer(value.toString()).intValue();
                    int iValue = RMT2Money.stringToNumber(value.toString())
                            .intValue();
                    this.rs.updateInt(dbName, iValue);
                    break;

                case Types.BIGINT:
                    // long lValue = new Long(value.toString()).longValue();
                    long lValue = RMT2Money.stringToNumber(value.toString())
                            .longValue();
                    this.rs.updateLong(dbName, lValue);
                    break;

                case Types.CHAR:
                case Types.VARCHAR:
                case Types.LONGVARCHAR:
                    this.rs.updateString(dbName, value.toString());
                    break;

                case Types.LONGVARBINARY:
                    // This should be an instance of InputStream class
                    // if (value instanceof InputStream) {
                    // InputStream is = (InputStream) value;
                    // String strValue = RMT2File.getStreamStringData(is);
                    // int size = strValue.length();
                    // this.rs.updateBinaryStream(dbName, is, size);
                    // }
                    this.rs.updateObject(dbName, value);
                    break;

                case Types.REAL:
                case Types.FLOAT:
                    // float fValue = new Float(value.toString()).floatValue();
                    float fValue = RMT2Money.stringToNumber(value.toString())
                            .floatValue();
                    this.rs.updateFloat(dbName, fValue);
                    break;

                case Types.NUMERIC:
                case Types.DOUBLE:
                case Types.DECIMAL:
                    // double dValue = new
                    // Double(value.toString()).doubleValue();
                    double dValue = RMT2Money.stringToNumber(value.toString())
                            .doubleValue();
                    this.rs.updateDouble(dbName, dValue);
                    break;

                case Types.DATE:
                    java.util.Date date = null;
                    if (value instanceof String) {
                        try {
                            date = RMT2Date.stringToDate(value.toString());
                        } catch (SystemException e) {
                            throw e;
                        }
                    }
                    else {
                        if (value instanceof java.util.Date) {
                            date = (java.util.Date) value;
                        }
                        else {
                            throw new SystemException(
                                    "Invalid value passed for a date");
                        }
                    }

                    // this.rs.updateDate(dbName, new
                    // java.sql.Date(date.getTime()));
                    Timestamp dts = new Timestamp(date.getTime());
                    this.rs.updateTimestamp(dbName, dts);
                    break;

                case Types.TIME:
                    this.rs.updateTime(dbName, (java.sql.Time) value);
                    break;

                case Types.TIMESTAMP:
                    java.util.Date datetime = null;
                    if (value instanceof String) {
                        try {
                            datetime = RMT2Date.stringToDate(value.toString());
                        } catch (SystemException e) {
                            this.msg = "Unable to set DAO Column value for property,  "
                                    + _property;
                            throw new SystemException(this.msg, e);
                        }
                    }
                    else {
                        if (value instanceof java.util.Date) {
                            datetime = (java.util.Date) value;
                        }
                        else {
                            throw new SystemException(
                                    "Invalid value passed for a datetime");
                        }
                    }

                    Timestamp ts = new Timestamp(datetime.getTime());
                    this.rs.updateTimestamp(dbName, ts);
                    break;

                default:
                    return;
            } // end switch

            this.dbChangeCount++;
        } // end try

        catch (SQLException e) {
            this.msg = "Problem occured trying to set ORM Datasource property value - Column in error: "
                    + _property;
            throw new DatabaseException(this.msg, e);
        } catch (ClassCastException e) {
            this.msg = "Error casing object when trying to set ORM Datasource property value - Column in error: "
                    + _property;
            throw new DatabaseException(this.msg, e);
        }
    }

    /**
     * Formats data values originating from a datasource provider in order to
     * conform and be applied to a SQL statement using colBean.
     * 
     * @param colBean
     *            {@link bean.db.DataSourceColumn DataSourceColumn}
     * @return Properly formatted value.
     */
    public String getSqlColumnValue(DataSourceColumn colBean) {
        String value = this.getSqlColumnValue(colBean.getSqlType(),
                colBean.getDataValue());
        return value;
    }

    /**
     * Formats _value according to _dataType so that the value can be properly
     * applied to a SQL statement for processing.
     * 
     * @param _dataType
     *            The data type of the value to be applied to the SQL statement.
     * @param _value
     *            Teh value to be formatted.
     * @return Properly formatted value.
     */
    public String getSqlColumnValue(String _dataType, Object _value) {
        StringBuffer value = new StringBuffer(100);

        if (_value == null || _value.equals("")
                || _value.equals(DaoApi.DB_NULL)) {
            value.append(DaoApi.DB_NULL);
        }
        else {
            if (_dataType.equalsIgnoreCase("String")
                    || _dataType.equalsIgnoreCase("CHAR")
                    || _dataType.equalsIgnoreCase("VARCHAR")
                    || _dataType.equalsIgnoreCase("varchar2")
                    || _dataType.equalsIgnoreCase("date")
                    || _dataType.equalsIgnoreCase("time")
                    || _dataType.equalsIgnoreCase("timestamp")
                    || _dataType.equalsIgnoreCase("datetime")) {

                // Convert java date into a string.
                if (_dataType.equalsIgnoreCase("date")
                        || _dataType.equalsIgnoreCase("time")
                        || _dataType.equalsIgnoreCase("timestamp")
                        || _dataType.equalsIgnoreCase("datetime")) {
                    try {
                        String dt = null;
                        // Be able to handle values of Date and String
                        if (_value instanceof Date) {
                            dt = RMT2Date.formatDate((java.util.Date) _value,
                                    "MM/dd/yyyy HH:mm:ss");
                        }
                        else if (_value instanceof String) {
                            dt = _value.toString();
                        }
                        value.append("\'");
                        value.append(dt);
                        value.append("\'");
                    } catch (SystemException e) {
                        value.append(DaoApi.DB_NULL);
                    } catch (Exception e) {
                        logger.log(Level.ERROR,
                                "date value is invalid...defaulting to null");
                        value.append(DaoApi.DB_NULL);
                    }
                }
                else {
                    // Apply data as a string.
                    value.append("\'");
                    String strVal = RMT2String.replaceAll(_value.toString(),
                            "''", "'");
                    value.append(strVal);
                    value.append("\'");
                }
            }
            else {
                // Apply data as-is
                value.append(_value.toString());
            }
        }
        return value.toString();
    }

    /**
     * Executes target datasource and returns the datasource to the caller.
     * 
     * @param _wherClause
     * @param _orderByClause
     * @throws SystemException
     */
    protected final void executeDso(String _wherClause, String _orderByClause)
            throws SystemException {
        String method = "executeDso";
        if (this.baseView == null || this.baseView.length() <= 0) {
            throw new SystemException(method
                    + " failes because its base datasource view is invalid");
        }

        try {
            this.parseXmlDataSource(this.baseView);
            this.setWhereClause(_wherClause);
            this.setOrderByClause(_orderByClause);
            // Retrieve data
            this.executeQuery();
            return;
        } catch (Exception e) {
            throw new SystemException(
                    "Problem executing target ORM Datasource", e);
        }
    }

    /**
     * Sets the SQL where clause for this DataSource
     * 
     * @param _criteria
     */
    private final void setWhereClause(String _criteria) {
        String temp = null;
        temp = this.getDatasourceSql(DbSqlConst.WHERE_KEY);
        if (temp != null && temp.length() > 0) {
            // Do nothing...Prepare to append "_criteria" to where clause
        }
        else {
            this.setDatasourceSql(DbSqlConst.WHERE_KEY, null);
        }

        // Initialize where clause to the input criteria
        this.setDatasourceSql(DbSqlConst.WHERE_KEY, _criteria);
        return;
    }

    /**
     * Sets the SQL order by clause for this DataSource
     * 
     * @param _criteria
     */
    private final void setOrderByClause(String _criteria) {
        String temp = null;
        temp = this.getDatasourceSql(DbSqlConst.ORDERBY_KEY);
        if (temp != null && temp.length() > 0) {
            // Do nothing...Prepare to append "_criteria" to order by clause
        }
        else {
            this.setDatasourceSql(DbSqlConst.ORDERBY_KEY, null);
        }

        // Initialize order by clause to the input criteria
        this.setDatasourceSql(DbSqlConst.ORDERBY_KEY, _criteria);
        return;
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
     * @return
     * @throws SystemException
     */
    protected final List find(String _whereClause) throws SystemException {

        this.executeDso(_whereClause, null);
        try {
            // Initialize ArrayList with data
            List list = DataSourceFactory.packageBean(this, this.baseBeanClass);
            return list;
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public List findData(String _whereClause, String _orderByClause)
            throws SystemException {
        try {
            List list = this.find(_whereClause, _orderByClause);
            return list;
        } catch (SystemException e) {
            throw new SystemException(e);
        }
    }

    /**
     * Obtains all data rows that may exist for this DataSource based on the
     * assigned DataSource View.
     * 
     * @return An List of objects corresponding to the the DataSource View.
     * @throws NotFoundException
     * @throws SystemException
     * @throws DatabaseException
     */
    protected final List findAll() throws NotFoundException, SystemException,
            DatabaseException {
        return this.find(null);
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
            List list = DataSourceFactory.packageBean(this, this.baseBeanClass);
            return list;
        } catch (DatabaseException e) {
            throw new SystemException(e);
        } catch (NotFoundException e) {
            throw new SystemException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean isColumnValid(String _value) {
        return this.datasource.isColumnValid(_value);
    }

    /**
     * {@inheritDoc}
     */
    public ResultSet getRs() {
        return this.rs;
    }

    /**
     * Gets the SQL Select statement that is currently associated with this api
     * object.
     * 
     * @return String
     */
    protected String getSelectSql() {
        return this.selectSql;
    }

    /**
     * Sets the SQL select statement for this api object.
     * 
     * @param value
     *            SQL select statement
     */
    protected void setSelectSql(String value) {
        this.selectSql = value;
    }

    /**
     * Gets the SQL insert statement assoicated with this api object.
     * 
     * @return String
     */
    protected String getInsertSql() {
        return this.insertSql;
    }

    /**
     * Sets the SQL insert statement for this api object.
     * 
     * @param value
     *            SQL insert statement
     */
    protected void setInsertSql(String value) {
        this.insertSql = value;
    }

    /**
     * Gets the SQL delete statement assoicated with this api object.
     * 
     * @return String
     */
    protected String getDeleteSql() {
        return this.deleteSql;
    }

    /**
     * Sets the SQL delete statement for this api object.
     * 
     * @param value
     */
    protected void setDeleteSql(String value) {
        this.deleteSql = value;
    }

    /**
     * Gets the SQL update statement assoicated with this api object.
     * 
     * @return String
     */
    protected String getUpdateSql() {
        return this.updateSql;
    }

    /**
     * Sets the SQL update statement for this api object.
     * 
     * @param value
     *            String
     */
    protected void setUpdateSql(String value) {
        this.updateSql = value;
    }

    /**
     * Gets the user id.
     * 
     * @return String
     */
    protected String getUserId() {
        return this.loginId;
    }

    /**
     * Sets the User id.
     * 
     * @param value
     *            String
     */
    protected void setUserId(String value) {
        this.loginId = value;
    }

    /**
     * {@inheritDoc}
     */
    public ObjectMapperAttrib getDataSourceAttib() {
        return this.datasource;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isRsScrollable() {
        return this.rsScrollable;
    }

    /**
     * {@inheritDoc}
     */
    public void setRsScrollable(boolean value) {
        this.rsScrollable = value;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isRsUpdatable() {
        return this.rsUpdatable;
    }

    /**
     * {@inheritDoc}
     */
    public void setRsUpdatable(boolean value) {
        this.rsUpdatable = value;
    }

    /**
     * {@inheritDoc}
     */
    public void setQueryData(RMT2TagQueryBean value) {
        this.queryData = value;
    }

    /**
     * {@inheritDoc}
     */
    public RMT2TagQueryBean getQueryData() {
        return this.queryData;
    }

    /**
     * Set the base DataSource view name.
     * 
     * @param viewName
     *            Name of Datasource view.
     * @return The previous datasource view name.
     */
    public String setBaseView(String viewName) {
        String prevBaseView = this.baseView;
        this.baseView = viewName;
        return prevBaseView;
    }

    /**
     * Set the base DataSource class name.
     * 
     * @param className
     *            Name of Datasource class.
     * @return The previous datasource class name.
     */
    public String setBaseClass(String className) {
        String prevBaseClass = this.baseBeanClass;
        this.baseBeanClass = className;
        return prevBaseClass;
    }

    /**
     * Get current DataSource base view name.
     * 
     * @return String
     */
    public String getBaseView() {
        return this.baseView;
    }

    /**
     * Get current DataSource base class name.
     * 
     * @return String
     */
    public String getBaseClass() {
        return this.baseBeanClass;
    }

    public void setReturnRowCount(boolean flag) {
        this.returnRowCount = flag;
    }

    public boolean isReturnRowCount() {
        return this.returnRowCount;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DataSourceApi#getRowCount()
     */
    @Override
    public int getRowCount() {
        this.setReturnRowCount(true);
        String sql = null;
        Statement locStat = null;
        ResultSet locRs = null;
        try {
            if (this.selectSql == null) {
                return 0;
            }
            sql = "select count(*) rowcount from (" + this.selectSql
                    + ") as tempTable";
            this.rsScrollable = false;
            locStat = this.getJDBCStatementObject();

            logger.log(Level.INFO, "Executing Count Query: " + this.baseView);
            logger.log(Level.INFO, "Preparing to execute Count SQL Statement: "
                    + sql);
            locStat.getResultSetConcurrency();
            locStat.getResultSetType();

            locRs = locStat.executeQuery(sql);
            int rows = 0;
            while (locRs.next()) {
                rows = locRs.getInt("rowcount");
            }
            return rows;
        } catch (SQLException e) {
            msg = e.getMessage() + " - SQL: " + sql;
            if (this.baseView != null && !this.baseView.equals("")) {
                msg += " View: " + this.baseView;
            }
            logger.log(Level.ERROR, this.msg);
            throw new DatabaseException(
                    "Problem executing count query which the SQL is supplied internally by the ORM Datasource configuration",
                    e);
        } finally {
            this.setReturnRowCount(false);
            try {
                locRs.close();
                locStat.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DatabaseException(e);
            } finally {
                locRs = null;
                locStat = null;
            }
        }
    }

} // end class

