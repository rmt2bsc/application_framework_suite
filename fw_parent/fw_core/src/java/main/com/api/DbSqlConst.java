package com.api;

import java.util.Hashtable;

/**
 * Class containing static constants and method that are related to SQL
 * transaction operations.
 * 
 * @author roy.terrell
 * 
 */
public class DbSqlConst {

    /** Not found code */
    public static final int RC_ROW_NOTFOUND = -201;

    /** Not found message */
    public static final String MSG_ROW_NOTFOUND = "Row was not found";

    /** SQL Row Not Found code */
    public static final int RC_ROWNOTFOUND = -100;

    /** User Id key name */
    public static final String USERID_KEY = "loginId";

    /** Password key name */
    public static final String PASSWORD_KEY = "password";

    /** SQL insert statement code */
    public static final int SQL_INSERT = 1;

    /** SQL update statement code */
    public static final int SQL_UPDATE = 2;

    /** SQL delete statement code */
    public static final int SQL_DELETE = 3;

    /** SQL SELECT list code */
    public static final int SELECT_KEY = 0;

    /** SQL FROM clause code */
    public static final int FROM_KEY = 1;

    /** SQL WHERE clause code */
    public static final int WHERE_KEY = 2;

    /** SQL GROUP BY clause code */
    public static final int GROUPBY_KEY = 3;

    /** SQL HAVING clause code */
    public static final int HAVING_KEY = 4;

    /** SQL ORDER BY clause code */
    public static final int ORDERBY_KEY = 5;

    /** SQL select statement literals */
    private static final String SELECT_VALUE = "select";

    private static final String FROM_VALUE = "from";

    private static final String WHERE_VALUE = "where";

    private static final String GROUPBY_VALUE = "group by";

    private static final String HAVING_VALUE = "having";

    private static final String ORDERBY_VALUE = "order by";

    /** Hashtable containing key/value SQL Select statement component groupings */
    public static final Hashtable SELECTOBJECT = new Hashtable();
    static {
        SELECTOBJECT.put(new Integer(SELECT_KEY), SELECT_VALUE);
        SELECTOBJECT.put(new Integer(FROM_KEY), FROM_VALUE);
        SELECTOBJECT.put(new Integer(WHERE_KEY), WHERE_VALUE);
        SELECTOBJECT.put(new Integer(GROUPBY_KEY), GROUPBY_VALUE);
        SELECTOBJECT.put(new Integer(HAVING_KEY), HAVING_VALUE);
        SELECTOBJECT.put(new Integer(ORDERBY_KEY), ORDERBY_VALUE);
    }

    /**
     * Obtains the indexed position of the SQL Select clause component in the
     * {@link SELECTOBJECT} Hashtable. For instance, if we want to locate the
     * position of the select list clause in the SELECTOBJECT Hash, value would
     * equal "select"; for the order by cluase, value would equal "order by".
     * When locating the position of the order by or group by clauses, the value
     * can equal "order by", "orderby", "group by", or "groupby", respectively.
     * 
     * @param value
     *            The value of the statement clause to query which is not case
     *            sensitive.
     * @return int as the indexed position. -1 if value does not exist.
     */
    public static final int getSelectObjectKey(String value) {
        String clause = null;
        for (int ndx = SELECT_KEY; ndx <= ORDERBY_KEY; ndx++) {
            clause = (String) SELECTOBJECT.get(new Integer(ndx));
            if (value.equalsIgnoreCase(clause)) {
                return ndx;
            }
            if (value.equalsIgnoreCase("GROUPBY")
                    && clause.equals(GROUPBY_VALUE)) {
                return ndx;
            }
            if (value.equalsIgnoreCase("ORDERBY")
                    && clause.equals(ORDERBY_VALUE)) {
                return ndx;
            }
        }
        return -1; // clause was not found
    }

    /** Document type for XML DataSource */
    public static final int DOCTYPE_DATASOURCE = 1;

    /** Document type for SQL DataSource */
    public static final int DOCTYPE_SQL = 2;

    /** Document type for Property DataSource */
    public static final int DOCTYPE_PROPERTY = 3;

    /** Hashtable of supported document types */
    public static final Hashtable DOCTYPES = new Hashtable();
    static {
        DOCTYPES.put(new Integer(DOCTYPE_DATASOURCE), "datasource");
        DOCTYPES.put(new Integer(DOCTYPE_SQL), "sql");
        DOCTYPES.put(new Integer(DOCTYPE_PROPERTY), "property");
    }

    /** A Hashtable that list the types of DataSources */
    public static final Hashtable DATASOURCE_TYPES = new Hashtable();
    static {
        DATASOURCE_TYPES.put(new Integer(1), "xml");
        DATASOURCE_TYPES.put(new Integer(2), "sql");
    }

    /** Sybase Adaptive Server Anywhere RDBMS Id */
    public static final int DBMS_SYBASE_ASA_KEY = 1;

    /** Sybase Adaptive Server Enterprise RDBMS Id */
    public static final int DBMS_SYBASE_ASE_KEY = 2;

    /** Oracle RDBMS Id */
    public static final int DBMS_ORACLE_KEY = 3;

    /** Microsoft SQL Server RDBMS Id */
    public static final int DBMS_SQLSERVER_KEY = 4;

    /** IBM DB2 RDBMS Id */
    public static final int DBMS_DB2_KEY = 5;

    /** RDBMS name constants */
    private static final String DBMS_SYBASE_ASA_VALUE = "Sybase Adaptive Server Anywhere";

    private static final String DBMS_SYBASE_ASE_VALUE = "Sybase Adaptive Server Enterprise";

    private static final String DBMS_ORACLE_VALUE = "Oracle";

    private static final String DBMS_SQLSERVER_VALUE = "MS SQL Server";

    private static final String DBMS_DB2_VALUE = "IBM DB2";

    /** Hashtable containing the key/value pairs of supported RDBMS systems */
    public static final Hashtable DBMS_VENDORS = new Hashtable();
    static {
        DBMS_VENDORS.put(new Integer(DBMS_SYBASE_ASA_KEY),
                DBMS_SYBASE_ASA_VALUE);
        DBMS_VENDORS.put(new Integer(DBMS_SYBASE_ASE_KEY),
                DBMS_SYBASE_ASE_VALUE);
        DBMS_VENDORS.put(new Integer(DBMS_ORACLE_KEY), DBMS_ORACLE_VALUE);
        DBMS_VENDORS.put(new Integer(DBMS_SQLSERVER_KEY), DBMS_SQLSERVER_VALUE);
        DBMS_VENDORS.put(new Integer(DBMS_DB2_KEY), DBMS_DB2_VALUE);
    }

}