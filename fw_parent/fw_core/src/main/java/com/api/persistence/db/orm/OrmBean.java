package com.api.persistence.db.orm;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.NotFoundException;
import com.RMT2Base;
import com.SystemException;
import com.api.config.AppPropertyPool;
import com.api.config.ConfigConstants;
import com.api.config.old.AbstractSystemConfigImpl;
import com.util.RMT2Date;
import com.util.RMT2File;
import com.util.RMT2String;
import com.util.RMT2Utility;

/**
 * Abstract class used for Object Relational Mapping and database persistence.
 * This class is the root of the ORM POJO class hierarchy.
 * 
 * @author appdev
 * 
 */
public abstract class OrmBean extends RMT2Base implements Serializable {

    private static final long serialVersionUID = -7789790084286173356L;

    /** A String literal representing the database null value */
    public static final String DB_NULL = "null";

    /** A String literal representing the database not null value */
    public static final String DB_NOTNULL = "not null";

    /** A String literal representing the database equality */
    public static final String DB_EQUAL = "eq";

    /**
     * Collection that stores the names of ORM bean properties that are to be
     * flagged as null when time to be persisted to the database. This is
     * especially useful for native type properties.
     */
    private Hashtable nullValues;

    /**
     * SQL Select order by clause ascending literal
     */
    public static final String ORDERBY_ASCENDING = "asc";

    /**
     * SQL Select order by clause descending literal
     */
    public static final String ORDERBY_DESCENDING = "desc";

    /** Value that indicates the "begin with" SQL like condition */
    public static final int LIKE_BEGIN = 1;

    /** Value that indicates the "contains" SQL like condition */
    public static final int LIKE_CONTAINS = 2;

    /** Value that indicates the "end with" SQL like condition */
    public static final int LIKE_END = 3;

    /**
     * Tabular result set type code
     */
    public static final int RESULTSET_TYPE_TABULAR = 100;

    /**
     * XML result set type code
     */
    public static final int RESULTSET_TYPE_XML = 200;

    /**
     * The name of the application key that points to the package location of
     * the RMT2 OrmBean peer classes.
     */
    public static final String PACKAGE_PREFIX = "orm_bean_package_prefix";

    /** Indicates the format of the data result set of an OrmBean */
    private int resultsetType;

    /** A collection of SQL equality criteria. */
    private Map criteria;

    /** A collection of custom SQL predicates. */
    private Set customCriteria;

    /** A collection SQL "IN" clause criteria. */
    private Map inClause;

    /** A collection of SQL "Begin With" Like condition criteria. */
    private Map likeClauseBegin;

    /** A collection of SQL "Contains" Like condition criteria. */
    private Map likeClauseContain;

    /** A collection of SQL "Ends With" Like condition criteria. */
    private Map likeClauseEnd;

    /** A collection SQL ordering criteria. */
    private Map orderBy;

    /** A collection SQL Group By criteria. */
    private Set groupBy;

    /** A collection of SQL HAVING criteria. */
    private Set having;

    /** limiting the number of rows returned by a query */
    private String rowLimitClause;

    /** Indicates if XML result set should persisted as a file */
    private boolean serializeXml;

    private String fileName;

    // /** Holds the java package prefix to the location of all ORM bean
    // classes. */
    // private String packagePrefix;

    /**
     * Creates a OrmBean instance and initializes the key data members needed to
     * build SQL statements.
     * 
     * @throws SystemException
     */
    public OrmBean() throws SystemException {
        super();
        this.initBean();
        this.resetNullValues();
        this.resetCriteria();
        this.resetCustomCriteria();
        this.resetInClause();
        this.resetOrderBy();
        this.resetLikeClause(OrmBean.LIKE_BEGIN);
        this.resetLikeClause(OrmBean.LIKE_CONTAINS);
        this.resetLikeClause(OrmBean.LIKE_END);
        this.resetGroupBy();
        this.resetHaving();
        this.serializeXml = false;

        // // Try to get bean package from application propertey pool.
        // this.packagePrefix =
        // AppPropertyPool.getProperty(OrmBean.PACKAGE_PREFIX);
        // if (this.packagePrefix == null) {
        // // Try to get bean package from System properties.
        // this.packagePrefix = System.getProperty(OrmBean.PACKAGE_PREFIX);
        // // Default bean class package prefix to bean.
        // if (this.packagePrefix == null) {
        // this.packagePrefix = "bean.";
        // }
        // }
    }

    /**
     * Abstract method to perform custom object initialization.
     * 
     * @throws SystemException
     */
    public abstract void initBean() throws SystemException;

    /**
     * Indicates whether or not if _property is flagged as null in the database.
     * This method is generally used to set corresponding database columns of
     * _property to null..
     * 
     * @param _property
     *            One of the properties of the class that is mapped to a
     *            database column.
     * @return
     */
    public boolean isNull(String _property) {
        return this.nullValues.containsKey(_property);
    }

    /**
     * Flags _property with a null indicator. When _property is set to null, a
     * null value will be assigned to its corresponding database column.
     * 
     * @param _property
     *            One of the properties of the class that is mapped to a
     *            database column.
     */
    public void setNull(String _property) {
        this.nullValues.put(_property, OrmBean.DB_NULL);
    }

    /**
     * Removes the null value flag from_property so that its corresponding
     * database column will not be set to null.
     * 
     * @param _property
     *            One of the properties of the class that is mapped to a
     *            database column.
     */
    public void removeNull(String _property) {
        this.nullValues.remove(_property);
    }

    /**
     * Removes the null flag indicator for all properties that may have been
     * previously set.
     * 
     */
    public void resetNullValues() {
        this.nullValues = null;
        this.nullValues = new Hashtable();
    }

    /**
     * Initializes the equality selection criteria collection which will contain
     * no elements.
     * 
     */
    public void resetCriteria() {
        this.criteria = null;
        this.criteria = new Hashtable();
    }

    /**
     * Initializes the equality selection criteria collection which will contain
     * no elements.
     * 
     */
    public void resetCustomCriteria() {
        this.customCriteria = null;
        this.customCriteria = new HashSet();
    }

    /**
     * Initializes the in cluase criteria collection which will contain no
     * elements.
     * 
     */
    public void resetInClause() {
        this.inClause = null;
        this.inClause = new Hashtable();
    }

    /**
     * Determines if criteria exist.
     * 
     * @return true if exist. Otherwise, false is returned.
     */
    public boolean isCriteriaAvailable() {
        return this.criteria.size() > 0;
    }

    /**
     * Determines if custom criteria exist.
     * 
     * @return true if exist. Otherwise, false is returned.
     */
    public boolean isCustomCriteriaAvailable() {
        return this.customCriteria.size() > 0;
    }

    /**
     * Determines if In Clause criteria exist.
     * 
     * @return true if exist. Otherwise, false is returned.
     */
    public boolean isInClauseAvailable() {
        return this.inClause.size() > 0;
    }

    /**
     * Determines if one of the three Like Clause criteria collections contains
     * conditions to be processed.
     * 
     * @param conditionType
     *            One of the following Like condtion types to check:
     *            {@link OrmBean.LIKE_BEGIN}, {@link OrmBean.LIKE_CONTAINS} or
     *            {@link OrmBean.LIKE_END}
     * @return true when target condition type has one or more entries to
     *         process. False is returned when the target condition type
     *         contains zero entries or an invalid condtion type is specified.
     */
    public boolean isLikeClauseAvailable(int conditionType) {
        switch (conditionType) {
            case OrmBean.LIKE_BEGIN:
                return this.likeClauseBegin.size() > 0;
            case OrmBean.LIKE_CONTAINS:
                return this.likeClauseContain.size() > 0;
            case OrmBean.LIKE_END:
                return this.likeClauseEnd.size() > 0;
        }
        return false;
    }

    /**
     * Adds equality-only selection criteria as key/value pairs to a collection.
     * The collection of key/value pairs represent equality-only selection
     * criteria which uses the equal sign to compare two operands within a SQL
     * predicate. This method expects to apply the key/values pairs as simple
     * equality statements like: (a = b). The key should be the name of a
     * property that can be found in an ORM peer XML documnet, and the value is
     * expecting one of the java primitive wrapper type objects.
     * 
     * @param _property
     *            Property name of a datasource that corresponds to a database
     *            column which is case sensitive. This property must have a
     *            value and cannot be null.
     * @param _criteria
     *            String value crireria to be appended to the SQL where clause.
     *            This property must have a value and cannot be null.
     */
    public void addCriteria(String _property, String _criteria) {
        if (_property == null) {
            return;
        }
        if (_criteria == null) {
            return;
        }

        // Take care of escaping any occurrences of single quote literals
        String val = RMT2String.replaceAll2(_criteria, "''", "'");
        this.criteria.put(_property, val);
    }

    /**
     * Adds selection criteria as an integer value.
     * 
     * @param _property
     *            Property name of a datasource that corresponds to a database
     *            column which is case sensitive
     * @param _criteria
     *            integer value crireria to be appended to the SQL where clause.
     */
    public void addCriteria(String _property, int _criteria) {
        String temp = String.valueOf(_criteria);
        this.addCriteria(_property, temp);
    }

    /**
     * Adds selection criteria as a boolean value.
     * 
     * @param _property
     *            Property name of a datasource that corresponds to a database
     *            column which is case sensitive
     * @param _criteria
     *            boolean value crireria to be appended to the SQL where clause.
     */
    public void addCriteria(String _property, boolean _criteria) {
        String temp = String.valueOf(_criteria);
        this.addCriteria(_property, temp);
    }

    /**
     * Adds selection criteria as a double value.
     * 
     * @param _property
     *            Property name of a datasource that corresponds to a database
     *            column which is case sensitive
     * @param _criteria
     *            double value crireria to be appended to the SQL where clause.
     */
    public void addCriteria(String _property, double _criteria) {
        String temp = String.valueOf(_criteria);
        this.addCriteria(_property, temp);
    }

    /**
     * Adds selection criteria as a java.util.Date value
     * 
     * @param property
     *            Property name of a datasource that corresponds to a database
     *            column which is case sensitive
     * @param criteria
     *            Date value crireria to be appended to the SQL where clause.
     */
    public void addCriteria(String property, java.util.Date criteria) {
        try {
            String dbmsId = AppPropertyPool
                    .getProperty(ConfigConstants.PROPNAME_DBMS_VENDOR);
            // Be able to obtain dbms id in web and stand alone application
            // contexts
            if (dbmsId == null) {
                // PropertyFileSystemResourceConfigImpl config = new
                // PropertyFileSystemResourceConfigImpl();
                dbmsId = RMT2File
                        .getAppParmProperty(ConfigConstants.PROPNAME_DBMS_VENDOR);
                if (dbmsId == null) {
                    throw new NotFoundException(
                            "Date property, "
                                    + property
                                    + ", for RMT2 ORM object could not be manipulated due to the database vendor is not configured for this application");
                }
            }
            String dbmsVal = AbstractSystemConfigImpl.getDbmsType(dbmsId);
            String temp = null;
            if (dbmsVal.equals(ConfigConstants.DBMSTYPE_SQLSERVER)) {
                temp = RMT2Date.formatDate(criteria, "MM/dd/yyyy HH:mm:ss");
            }
            else if (dbmsVal.equals(ConfigConstants.DBMSTYPE_ASA)) {
                temp = RMT2Date.formatDate(criteria, "yyyy/MM/dd HH:mm:ss");
            }
            this.addCriteria(property, temp);
        } catch (SystemException e) {

        }
    }

    /**
     * Adds an instruction to explicitly limit the number of rows returned by a
     * query. You can use the FIRST or TOP keywords to limit the number of rows
     * included in the result set of a query.
     * 
     * @param instruction
     */
    public void addRowLimitClause(String instruction) {
        this.rowLimitClause = instruction;
    }

    /**
     * Gets the criteria for a particular property.
     * 
     * @param _property
     *            The property to target criteria. This property must have a
     *            value and cannot be null.
     * @return criteria
     */
    public String getCriteria(String _property) {
        Object value = this.criteria.get(_property);
        if (value != null) {
            return value.toString();
        }
        return null;
    }

    /**
     * Removes the selection criteria pertaining to _property.
     * 
     * @param _property
     *            The property name of the datasource that targets the database
     *            column which is case sensitive. This property must have a
     *            value and cannot be null.
     */
    public void removeCriteria(String _property) {
        if (_property == null) {
            return;
        }
        this.criteria.remove(_property);
    }

    /**
     * Adds "IN" clause criteria which is used for building SQL predicates. This
     * method manages a collection of key/value pairs that represent one or more
     * "IN" clause criteria that are to be used as part of the SQL where clause.
     * The key should be the name of a property that can be found in an ORM peer
     * XML documnet, and the value is expecting an array of String which
     * contains one or more valuesrespective to the key. The user should not be
     * concern whether or not to encapsulate each value in the String array with
     * single quotes since the framework determines that automatically.
     * 
     * @param _property
     *            ORM column property name.
     * @param _values
     *            A String array of values.
     */
    public void addInClause(String _property, String _values[]) {
        if (_values == null) {
            return;
        }
        if (_property == null || _property.equals("")) {
            return;
        }
        this.inClause.put(_property, _values);
    }

    /**
     * Adds "IN" clause criteria which is used for building SQL predicates from
     * an array of int.
     * 
     * @param property
     *            ORM column property name.
     * @param values
     *            An int array.
     */
    public void addInClause(String property, Integer values[]) {
        if (values == null) {
            return;
        }
        if (values.length <= 0) {
            return;
        }
        String strValues[] = new String[values.length];
        for (int ndx = 0; ndx < values.length; ndx++) {
            strValues[ndx] = String.valueOf(values[ndx]);
        }
        this.addInClause(property, strValues);
    }

    /**
     * Gets the In clause criteria for a particular property.
     * 
     * @param _property
     *            The property to target criteria. This property must have a
     *            value and cannot be null.
     * @return An array String values.
     */
    public String[] getInClause(String _property) {
        String values[];
        Object temp = (String[]) this.inClause.get(_property);
        if (temp != null && temp instanceof String[]) {
            values = (String[]) temp;
            return values;
        }
        return null;
    }

    /**
     * Removes the In cluase selection criteria pertaining to _property.
     * 
     * @param _property
     *            The property name of the datasource that targets the database
     *            column which is case sensitive. This property must have a
     *            value and cannot be null.
     */
    public void removeInClause(String _property) {
        if (_property == null) {
            return;
        }
        this.inClause.remove(_property);
    }

    /**
     * Initializes the order by collection to contain no elements.
     * 
     */
    public void resetOrderBy() {
        this.orderBy = null;
        this.orderBy = new LinkedHashMap();
    }

    /**
     * Initializes the group by collection to contain no elements.
     * 
     */
    public void resetGroupBy() {
        this.groupBy = null;
        this.groupBy = new LinkedHashSet();
    }

    /**
     * Initializes the having by collection to contain no elements.
     * 
     */
    public void resetHaving() {
        this.having = null;
        this.having = new HashSet();
    }

    /**
     * Determines if order by data exist.
     * 
     * @return true if exist. Otherwise, false is returned.
     */
    public boolean isOrderByAvailable() {
        return this.orderBy.size() > 0;
    }

    /**
     * Adds ordering criteria which is used to build SQL order by clause. The
     * criteria is managed as a collection of key/value pairs that specify the
     * column names and ordering flags needed to build the order by clasue of a
     * SQL query. The criteria is managed on a FIFO basis and duplicate entries
     * are not allowed. The key should contain the name of the database column
     * that should be ordered and the value should contain a flag that indicates
     * how the column should be ordered ("asc" or "desc").
     * 
     * @param columnName
     *            The database columnh name of a datasource that corresponds to
     *            a database column which is case sensitive. This property must
     *            have a value and cannot be null.
     * @param direction
     *            The Order By value that instructs the SQL statement on how to
     *            order data: Ascending (asc) or Descending (desc). This
     *            property must have a value and cannot be null.
     */
    public void addOrderBy(String columnName, String direction) {
        if (columnName == null) {
            return;
        }
        this.orderBy.put(columnName, direction);
    }

    /**
     * Gets the order by value for a particular property.
     * 
     * @param _property
     *            The property to target order by which is case sensitive
     * @return order by clause for _property
     */
    public String getOrderBy(String _property) {
        Object value = this.orderBy.get(_property);
        if (value != null) {
            return value.toString();
        }
        return null;
    }

    /**
     * Removes the order by clause pertaining to _property.
     * 
     * @param _property
     *            The property name of the datasource that targets the database
     *            column which is case sensitive. This property must have a
     *            value and cannot be null.
     */
    public void removeOrderBy(String _property) {
        if (_property == null) {
            return;
        }
        this.orderBy.remove(_property);
    }

    /**
     * Gets the criteria collecion.
     * 
     * @return Map using Hashtable implementation where key=data source property
     *         name and value=the value to equate.
     */
    public Map getCriteria() {
        return this.criteria;
    }

    /**
     * Gets the "In" Clause criteria collecion.
     * 
     * @return Map using Hashtable implementation where key=data source property
     *         name and value=the value to equate.
     */
    public Map getInClause() {
        return this.inClause;
    }

    /**
     * Gets the order by collection.
     * 
     * @return Map using LinkedHashMap implementation where key=data source
     *         property name and value=the value to equate.
     */
    public Map getOrderBy() {
        return this.orderBy;
    }

    /**
     * Get the SQL group by collection.
     * 
     * @return Set
     */
    public Set getGroupBy() {
        return groupBy;
    }

    /**
     * Adds Group By criteria which is used to build a SQL group by clasue. The
     * criteria is managed as a collection of column names which serves as
     * criteria for creating a SQL group by clause. The criteria is managed on a
     * FIFO basis and should not contain any duplicate entries.
     * 
     * @param columnName
     *            The database column name to include in the group by column
     *            list.
     */
    public void addGroupByCriteria(String columnName) {
        if (columnName == null) {
            return;
        }
        this.groupBy.add(columnName);
    }

    /**
     * Set the SQL group by collection.
     * 
     * @param groupBy
     *            a Set
     */
    public void setGroupBy(Set groupBy) {
        this.groupBy = groupBy;
    }

    /**
     * Get the SQL having collection.
     * 
     * @return Set
     */
    public Set getHaving() {
        return having;
    }

    /**
     * Set the SQL having collection.
     * 
     * @param having
     *            a Set
     */
    public void setHaving(Set having) {
        this.having = having;
    }

    /**
     * Adds complete predicate statements to the SQl havaing collection. Manages
     * a collection of selection criteria which are used to build SQL having
     * clause. Duplicate entries are not allowed and there is no requirement for
     * special ordering.
     * 
     * @param criteria
     *            Crtieria for SQL Having clause.
     */
    public void addHaving(String criteria) {
        if (criteria == null) {
            return;
        }
        this.having.add(criteria);

    }

    /**
     * Get custom selection criteria
     * 
     * @return Set
     */
    public Set getCustomCriteria() {
        return customCriteria;
    }

    /**
     * Set custom selection criteria.
     * 
     * @param customCriteria
     *            Set
     */
    public void setCustomCriteria(Set customCriteria) {
        this.customCriteria = customCriteria;
    }

    /**
     * Adds custom SQL predicates to the custom criteria collection. The
     * collection manages custom SQL predicates in completed form for building a
     * SQL where clause. Duplicate entries are not allowed and the order in
     * which elements are obtained are not guanranteed.
     * 
     * 
     * @param criteria
     *            String value crireria to be appended to the SQL where clause.
     *            This property must have a value and cannot be null.
     */
    public void addCustomCriteria(String criteria) {
        if (criteria == null) {
            return;
        }
        this.customCriteria.add(criteria);
    }

    /**
     * Adds criteria to build "Begin With" Like conditions for SQL predicate.
     * 
     * @param property
     *            ORM column property name.
     * @param value
     *            The condition to assign.
     * @return 1 when value is mapped to the property successfully, 0 when
     *         conditionType is of the incorrect type, -1 when value is null and
     *         -2 when property is null or invalid.
     */
    public int addLikeClause(String property, String value) {
        return this.addLikeClause(property, value, OrmBean.LIKE_BEGIN);
    }

    /**
     * Adds criteria as one of the three types of "LIKE" conditions (begin with,
     * contains or end with) for a SQL predicate. The conditionType parameter
     * determines the which of the three conditions to use. The criteria is
     * managed as a collection of key/value pairs that represent a mixture or
     * "Begin With", "Contains" or "End With" Like conditions. Like conditions
     * are designed to allow a developer to use wild cards in where clause of a
     * SQL statement for String types.
     * 
     * @param property
     *            ORM column property name.
     * @param value
     *            The condition to assign.
     * @param conditionType
     *            One of the following Like condtion types:
     *            {@link OrmBean.LIKE_BEGIN}, {@link OrmBean.LIKE_CONTAINS} or
     *            {@link OrmBean.LIKE_END}
     * @return 1 when value is mapped to the property successfully, 0 when
     *         conditionType is of the incorrect type, -1 when value is null and
     *         -2 when property is null or invalid.
     */
    public int addLikeClause(String property, String value, int conditionType) {
        if (value == null) {
            return -1;
        }
        if (property == null || property.equals("")) {
            return -2;
        }

        // Take care of escaping any occurrences of single quote literals
        String modValue = RMT2String.replaceAll2(value, "''", "'");

        switch (conditionType) {
            case OrmBean.LIKE_BEGIN:
                this.likeClauseBegin.put(property, modValue);
                return 1;
            case OrmBean.LIKE_CONTAINS:
                this.likeClauseContain.put(property, modValue);
                return 1;
            case OrmBean.LIKE_END:
                this.likeClauseEnd.put(property, modValue);
                return 1;
        }
        return 0;
    }

    /**
     * Gets the "Like" Clause criteria collecion.
     * 
     * @param conditionType
     *            One of the following Like condtion types:
     *            {@link OrmBean.LIKE_BEGIN}, {@link OrmBean.LIKE_CONTAINS} or
     *            {@link OrmBean.LIKE_END}
     * 
     * @return Map using Hashtable implementation
     */
    public Map getLikeClause(int conditionType) {
        switch (conditionType) {
            case OrmBean.LIKE_BEGIN:
                return this.likeClauseBegin;
            case OrmBean.LIKE_CONTAINS:
                return this.likeClauseContain;
            case OrmBean.LIKE_END:
                return this.likeClauseEnd;
            default:
                return null;
        }

    }

    /**
     * Gets the like clause criteria for a particular property.
     * 
     * @param _property
     *            The property to target criteria. This property must have a
     *            value and cannot be null.
     * @param conditionType
     *            One of the following Like condtion types:
     *            {@link OrmBean.LIKE_BEGIN}, {@link OrmBean.LIKE_CONTAINS} or
     *            {@link OrmBean.LIKE_END}
     * @return The like condtion criteria. Returns null when an invalid
     *         condition type is specified or when _property does not exist.
     */
    public String getLikeClause(String _property, int conditionType) {
        Object value = null;

        Map likeType = this.getLikeClause(conditionType);
        if (likeType == null) {
            return null;
        }
        value = likeType.get(_property);

        if (value != null) {
            return value.toString();
        }
        return null;
    }

    /**
     * Removes the "Like" clause selection criteria pertaining to _property.
     * 
     * @param _property
     *            The property name of the datasource that targets the database
     *            column which is case sensitive. This property must have a
     *            value and cannot be null.
     * @param conditionType
     *            One of the following Like condtion types:
     *            {@link OrmBean.LIKE_BEGIN}, {@link OrmBean.LIKE_CONTAINS} or
     *            {@link OrmBean.LIKE_END}
     */
    public void removeLikeClause(String _property, int conditionType) {
        if (_property == null) {
            return;
        }

        switch (conditionType) {
            case OrmBean.LIKE_BEGIN:
                this.likeClauseBegin.remove(_property);
                break;
            case OrmBean.LIKE_CONTAINS:
                this.likeClauseContain.remove(_property);
                break;
            case OrmBean.LIKE_END:
                this.likeClauseEnd.remove(_property);
                break;
        }
    }

    /**
     * Initializes one of the three Like condition clause collections.
     * 
     * @param conditionType
     *            One of the following Like condtion types:
     *            {@link OrmBean.LIKE_BEGIN}, {@link OrmBean.LIKE_CONTAINS} or
     *            {@link OrmBean.LIKE_END}
     */
    public void resetLikeClause(int conditionType) {
        switch (conditionType) {
            case OrmBean.LIKE_BEGIN:
                this.likeClauseBegin = null;
                this.likeClauseBegin = new Hashtable();
                break;
            case OrmBean.LIKE_CONTAINS:
                this.likeClauseContain = null;
                this.likeClauseContain = new Hashtable();
                break;
            case OrmBean.LIKE_END:
                this.likeClauseEnd = null;
                this.likeClauseEnd = new Hashtable();
                break;
        }
    }

    /**
     * Sets the result set type.
     * 
     * @param value
     */
    public void setResultsetType(int value) {
        this.resultsetType = value;
    }

    /**
     * Gets the result set type.
     * 
     * @return int
     */
    public int getResultsetType() {
        return this.resultsetType;
    }

    /**
     * Gets the serialize Xml switch.
     * 
     * @return true to indicate XML will be searialized and false to inidicate
     *         otherwise.
     */
    public boolean isSerializeXml() {
        return this.serializeXml;
    }

    /**
     * Sets the serializ Xml switch
     * 
     * @param value
     *            boolean where true activates the swicth and false deactivates
     *            the switch.
     */
    public void setSerializeXml(boolean value) {
        this.serializeXml = value;
    }

    /**
     * Extracts the unqualified class name of an instance descending from
     * OrmBean.
     * 
     * @return The class name of the ORmBean instance.
     */
    public final String getDataSourceRoot() {
        String root = RMT2Utility.getBeanClassName(this);
        if (root == null || root.equals("")) {
            return null;
        }
        return root;
    }

    /**
     * Determines the data source view name of an instance descending from
     * OrmBean.
     * 
     * @return The datasource view name or null when the OrmBean instance's
     *         datasource view cannot be located.
     */
    public final String getDataSourceName() {
        String viewName = this.getDataSourceRoot();
        return (viewName == null ? viewName : (viewName += "View"));
    }

    /**
     * Determines the fully qualified class name of an instance descending from
     * OrmBean.
     * 
     * @return Fully qualified bean name.
     */
    public final String getDataSourceClassName() {
        String className = this.getClass().getName();
        return className;

        // Removed old logic that depending on packagePrefix setting from
        // AppParms.properties
        // String className = this.getDataSourceRoot();
        // return (className == null ? className : (this.packagePrefix +
        // className));
    }

    /**
     * Returns the package name of this OrmBean.
     * 
     * @return The package name excluding the resource name.
     */
    public final String getDataSourcePackage() {
        String pkgName = this.getClass().getPackage().getName();
        return pkgName;
    }

    /**
     * @return the rowLimitClause
     */
    public String getRowLimitClause() {
        return rowLimitClause;
    }

    /**
     * Gets the filename where object serialization output is stored.
     * 
     * @return Filename
     */
    public String getFileName() {
        return this.fileName;
    }

    /**
     * Sets the filename where object serialzation output is stored.
     * 
     * @param value
     */
    public void setFileName(String value) {
        this.fileName = value;
    }
}