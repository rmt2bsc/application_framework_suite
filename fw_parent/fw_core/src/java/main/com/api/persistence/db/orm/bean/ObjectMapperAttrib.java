package com.api.persistence.db.orm.bean;

import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import com.NotFoundException;
import com.RMT2BaseBean;
import com.SystemException;
import com.api.persistence.db.DbSqlConst;
import com.constants.RMT2SystemExceptionConst;
import com.util.RMT2String;

/**
 * This class assists the DataSourceApi implementation to track and map database
 * equivalent properties such as database table names, where clauses, order by
 * clause, column definitions, and etc.
 * 
 * @author roy.terrell
 * 
 */
public class ObjectMapperAttrib extends RMT2BaseBean {
    private static final Logger logger = Logger
            .getLogger(ObjectMapperAttrib.class);

    private static final long serialVersionUID = 1L;

    private String name;

    private String type;

    private String whereClause;

    private String orderByClause;

    private Hashtable selectStatement;

    private Hashtable tables;

    private Hashtable columnDef;

    /**
     * Default constructor.
     * 
     * @throws SystemException
     */
    public ObjectMapperAttrib() throws SystemException {
        super();
        this.initBean();
    }

    /**
     * Initializes the properties selectStatement and columnDef.
     */
    public void initBean() throws SystemException {

        this.initColumnDef();
        this.initTables();
        this.initSelectStatement();
        return;
    }

    /**
     * Release resources created by this instance.
     * 
     */
    public void close() {
        this.columnDef.clear();
        this.columnDef = null;
        this.tables.clear();
        this.tables = null;
        this.selectStatement.clear();
        this.selectStatement = null;
        this.name = null;
        this.type = null;
        this.whereClause = null;
        this.orderByClause = null;
    }

    /**
     * Initializes the initColumnDef Hastable.
     * 
     * @return An Enumeration of the keys if any.
     * @throws SystemException
     */
    private Enumeration initColumnDef() throws SystemException {
        this.columnDef = null;
        this.columnDef = new Hashtable();
        return this.columnDef.keys();
    }

    /**
     * Initializes the initTables Hastable.
     * 
     * @return An Enumeration of the keys if any.
     * @throws SystemException
     */
    private Enumeration initTables() throws SystemException {
        this.tables = null;
        this.tables = new Hashtable();
        return this.tables.keys();
    }

    /**
     * Returns the column names of the datasource as an Enumeration object.
     * 
     * @return Enumeration
     */
    public Enumeration getColumns() {
        return this.columnDef.keys();
    }

    /**
     * Initializes the initSelectStatement Hastable.
     * 
     * @return An Enumeration of the keys if any.
     * @throws SystemException
     */
    private Enumeration initSelectStatement() throws SystemException {
        try {
            this.selectStatement = null;
            this.selectStatement = new Hashtable();
            return this.selectStatement.keys();
        } catch (NullPointerException e) {
            throw new SystemException(
                    "Null Pointer Exception occurred trying to setup select statement attribute");
        }
    }

    /**
     * Retrieves the value associated with the property, "atribute", of target
     * "column". The value returned to the caller is of type Object.
     * 
     * @param column
     *            name of column property.
     * @param attribute
     *            The attrubute name.
     * @return Object
     * @throws NotFoundException
     * @throws SystemException
     */
    public Object getColumnAttribute(String column, String attribute)
            throws NotFoundException, SystemException {
        logger.debug("Get attribute info for ORM column, " + column);
        if (column != null && attribute != null) {
        }
        else {
            throw new SystemException(
                    RMT2SystemExceptionConst.MSG_NULL_METHOD_ARGUMENT,
                    RMT2SystemExceptionConst.RC_NULL_METHOD_ARGUMENT);
        }

        if (this.columnDef == null) {
            logger.warn("Internal colmumn definition collection is null");
            return null;
        }
        if (!this.columnDef.containsKey(column)) {
            // throw new NotFoundException(
            // RMT2SystemExceptionConst.MSG_DATASOURCE_COLUMN_NOTFOUND
            // + ": " + column,
            // RMT2SystemExceptionConst.RC_DATASOURCE_COLUMN_NOTFOUND);
            return null;
        }

        DataSourceColumn values = (DataSourceColumn) this.columnDef.get(column);

        if (attribute.equalsIgnoreCase("name"))
            return values.getName();
        else if (attribute.equalsIgnoreCase("dbName"))
            return values.getDbName();
        else if (attribute.equalsIgnoreCase("sqlType"))
            return values.getSqlType();
        else if (attribute.equalsIgnoreCase("nullable"))
            return new Boolean(values.isNullable());
        else if (attribute.equalsIgnoreCase("javaType"))
            return new Integer(values.getJavaType());
        else if (attribute.equalsIgnoreCase("primaryKey"))
            return new Boolean(values.isPrimaryKey());
        else if (attribute.equalsIgnoreCase("tableName"))
            return values.getTableName();
        else if (attribute.equalsIgnoreCase("displayValue"))
            return values.getDisplayValue();
        else if (attribute.equalsIgnoreCase("updateable"))
            return new Boolean(values.isUpdateable());
        else if (attribute.equalsIgnoreCase("computed"))
            return new Boolean(values.isComputed());
        else if (attribute.equalsIgnoreCase("datavalue"))
            return (String) values.getDataValue();

        return null;
    }

    /**
     * Sets the value associated with the property, DataValue. This is typically
     * used when performing database updates using the DataSource object.
     * 
     * @param column
     *            THe name of the column property.
     * @param value
     *            THe value to be assigned.
     * @throws SystemException
     * @throws NotFoundException
     */
    public void setColumnAttribute(String column, Object value)
            throws SystemException, NotFoundException {
        if (column == null) {
            throw new SystemException(
                    RMT2SystemExceptionConst.MSG_NULL_METHOD_ARGUMENT
                            + ".  Argument: String column",
                    RMT2SystemExceptionConst.RC_NULL_METHOD_ARGUMENT);
        }

        if (!this.columnDef.containsKey(column)) {
            throw new NotFoundException(
                    RMT2SystemExceptionConst.MSG_DATASOURCE_COLUMN_NOTFOUND
                            + ": " + column,
                    RMT2SystemExceptionConst.RC_DATASOURCE_COLUMN_NOTFOUND);
        }

        DataSourceColumn values = (DataSourceColumn) this.columnDef.get(column);
        values.setDataValue(value);
    }

    /**
     * Retrieves the value of an SQL Select Statement clause stored in the
     * property, selectStatement. The returned value is cast as type String
     * since the Hashtable that houses all of its values as of type Object.
     * 
     * @param sqlKey
     *            The id of the SQL Statement component
     * @return The SQL Statement clause
     * @throws NotFoundException
     * @throws SystemException
     */
    public String getSqlAttribute(int sqlKey) throws NotFoundException,
            SystemException {
        if (!this.selectStatement.containsKey(new Integer(sqlKey))) {
            throw new NotFoundException(
                    RMT2SystemExceptionConst.MSG_SQL_CLAUSE_NOTFOUND + ": "
                            + sqlKey,
                    RMT2SystemExceptionConst.RC_SQL_CLAUSE_NOTFOUND);
        }

        return (String) this.selectStatement.get(new Integer(sqlKey));
    }

    /**
     * Sets or appends "value" to an SQL select statement clause. If value is
     * null, clause is reset to "". If valud is equal to "", view will be
     * executed as is.
     * 
     * @param sqlKey
     *            The id of the SQL Statement component
     * @param value
     *            The cluase to be assigned.
     * @throws NotFoundException
     * @throws SystemException
     */
    public void setSqlAttribute(int sqlKey, String value)
            throws NotFoundException, SystemException {
        String clause;
        if (value == null) {
            // if value is null, reset clause
            this.selectStatement.put(new Integer(sqlKey), "");
            return;
        }

        if (value.equals("") || value.equals(RMT2String.spaces(value.length()))) {
            // Do not change the view's properties if value = spaces
            return;
        }

        // validate key by attempting to retrieve its value
        clause = this.getSqlAttribute(sqlKey);

        if (clause.length() > 1 && !clause.equals("")) {
            switch (sqlKey) {
                case DbSqlConst.SELECT_KEY:
                case DbSqlConst.FROM_KEY:
                case DbSqlConst.GROUPBY_KEY:
                case DbSqlConst.ORDERBY_KEY:
                    clause += ", " + value;
                    break;

                case DbSqlConst.WHERE_KEY:
                case DbSqlConst.HAVING_KEY:
                    clause += " and " + value;
                    break;
            }
        }
        if (clause.equals("")) {
            clause = value;
        }
        this.selectStatement.put(new Integer(sqlKey), clause);
    }

    /**
     * Retrieves the value associated with the property, "atribute", of target
     * "table". The value returned to the caller is of type Object.
     * 
     * @param property
     *            The property name
     * @param attribute
     *            THe attribute name.
     * @return Object
     * @throws NotFoundException
     * @throws SystemException
     */
    public Object getTableAttribute(String property, String attribute)
            throws NotFoundException, SystemException {
        if (property != null && attribute != null) {
            // Do nothing
        }
        else {
            throw new SystemException(
                    RMT2SystemExceptionConst.MSG_NULL_METHOD_ARGUMENT,
                    RMT2SystemExceptionConst.RC_NULL_METHOD_ARGUMENT);
        }

        TableUsageBean tableObj = this.findTable(property);
        if (tableObj == null) {
            throw new NotFoundException(
                    RMT2SystemExceptionConst.MSG_DATASOURCE_TABLE_NOTFOUND
                            + ": " + property,
                    RMT2SystemExceptionConst.RC_DATASOURCE_TABLE_NOTFOUND);
        }

        if (attribute.equalsIgnoreCase("name"))
            return tableObj.getName();
        else if (attribute.equalsIgnoreCase("dbName"))
            return tableObj.getDbName();
        else if (attribute.equalsIgnoreCase("updateable"))
            return new Boolean(tableObj.isUpdateable());

        return null;
    }

    /**
     * Locates a particular TableUsageBean from the Hastable, tables.
     * 
     * @param table
     *            THe table name
     * @return
     */
    private TableUsageBean findTable(String table) {

        TableUsageBean tableObj = null;
        Hashtable tableList = this.getTables();
        for (int tableNdx = 1; tableNdx <= tableList.size(); tableNdx++) {
            tableObj = (TableUsageBean) tableList.get(new Integer(tableNdx));
            if (tableObj.getName().equalsIgnoreCase(table)) {
                return tableObj;
            }
        }

        return null;
    }

    /**
     * Retrieves the total count of tables involved for a particular datasource
     * view.
     * 
     * @return int
     */
    public int getTableCount() {
        return this.tables.size();
    }

    /**
     * Retrieves the total count of columns involved for a particular datasource
     * view.
     * 
     * @return int
     */
    public int getColumnCount() {
        return this.columnDef.size();
    }

    /**
     * Retrieves and returns to the caller the DataSourceColumn object stored in
     * the column definition object, columnDef, based on the key value,
     * "column".
     * 
     * @param column
     *            The name of the column.
     * @return {@link DataSourceColumn}
     */
    public DataSourceColumn getDsoColumn(String column) {
        return (DataSourceColumn) this.columnDef.get(column);
    }

    /**
     * Retrieves and returns to the caller the DataSourceColumn object that
     * represents the primary key column of this datasource. If datasource does
     * not have a primary key then null is returned.
     * 
     * @return {@link DataSourceColumn}
     */
    public DataSourceColumn getDsoPrimaryKey() {

        DataSourceColumn colObj = null;
        String colName = null;

        Enumeration cols = this.getColumns();
        while (cols.hasMoreElements()) {
            colName = (String) cols.nextElement();
            colObj = (DataSourceColumn) this.columnDef.get(colName);
            if (colObj == null) {
                continue;
            }

            if (new Boolean(colObj.isPrimaryKey()).booleanValue()) {
                return colObj;
            }
        }

        return null;
    }

    /**
     * Evaluates if a column (_value) exist. Returns true if the column is found
     * to exist. Otherwise, false is returned.
     * 
     * @param colName
     *            The column name
     * @return true if valid and false for not valid.
     */
    public boolean isColumnValid(String colName) {
        if (!this.columnDef.containsKey(colName)) {
            return false;
        }
        return true;
    }

    /**
     * Gets the column name.
     * 
     * @return String
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the column name.
     * 
     * @param value
     *            STring.
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the column type.
     * 
     * @return String
     */
    public String getType() {
        return this.type;
    }

    /**
     * Sets the column type.
     * 
     * @param value
     *            String.
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the where clause.
     * 
     * @return String.
     */
    public String getWhereClause() {
        return this.whereClause;
    }

    /**
     * Sets the where clause.
     * 
     * @param value
     *            String.
     */
    public void setWhereClause(String value) {
        this.whereClause = value;
    }

    /**
     * Gets the order by clause.
     * 
     * @return String.
     */
    public String getOrderByClause() {
        return this.orderByClause;
    }

    /**
     * Sets the order by clause.
     * 
     * @param value
     *            String.
     */
    public void setOrderByClause(String value) {
        this.orderByClause = value;
    }

    /**
     * Gets the select statement hash
     * 
     * @return Hashtable
     */
    public Hashtable getSelectStatement() {
        return this.selectStatement;
    }

    /**
     * Sets the selecte statement hash.
     * 
     * @param value
     *            Hashtable.
     */
    public void setSelectStatement(Hashtable value) {
        this.selectStatement = value;
    }

    /**
     * Gets the column definition hash.
     * 
     * @return Hashtable.
     */
    public Hashtable getColumnDef() {
        return this.columnDef;
    }

    /**
     * Sets the column definition hash.
     * 
     * @param value
     *            Hashtable.
     */
    public void setColumnDef(Hashtable value) {
        this.columnDef = value;
    }

    /**
     * Gets the table hash
     * 
     * @return Hashtable.
     */
    public Hashtable getTables() {
        return this.tables;
    }

    /**
     * Sets the table hash.
     * 
     * @param value
     *            Hashtable.
     */
    public void setTables(Hashtable value) {
        this.tables = value;
    }
}
