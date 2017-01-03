package com.constants;

//import java.util.Hashtable;

/**
 * Class that contains constants regarding error codes and their messages.
 */
public class RMT2SystemExceptionConst {

    // System Exception API Return Codes
    public static final int RC_CONNECTION_INVALID = -101;

    public static final int RC_INVALID_USERID = -102;

    public static final int RC_INVALID_SQL_STATEMENT = -103;

    public static final int RC_FAIL_TO_CONNECT = -104;

    public static final int RC_DB_DOWN = -105;

    public static final int RC_NULL_DB_DRIVER = -106;

    public static final int RC_NULL_DB_URL = -107;

    public static final int RC_NULL_DB_USERID = -108;

    public static final int RC_NULL_DB_PASSWORD = -109;

    public static final int RC_NULL_CONNECTION_COUNT = -110;

    public static final int RC_NULL_DATASOURCE_TYPE = -111;

    public static final int RC_DATASOURCE_INVALID = -112;

    public static final int RC_DATASOURCE_TYPE_INVALID = -113;

    public static final int RC_XML_DATASOURCE_INVALID = -114;

    public static final int RC_DATASOURCE_SQL_NOTEXIST = -115;

    public static final int RC_SYSTEM_DATA_NOTEXIST = -116;

    public static final int RC_NULL_METHOD_ARGUMENT = -117;

    public static final int RC_DATASOURCE_COLUMN_NOTFOUND = -119;

    public static final int RC_SQL_CLAUSE_NOTFOUND = -120;

    public static final int RC_NULL_DATASOURCE_VLAUE = -121;

    public static final int RC_INVALID_STRING_DATE_VALUE = -122;

    public static final int RC_INVALID_DATE_FORMAT = -123;

    public static final int RC_INVALID_NUMBER_FORMAT = -124;

    public static final int RC_INVALID_STRING_NUMBER_VALUE = -125;

    public static final int RC_LOGIN_PARAMETER_INVALID = -126;

    public static final int RC_LOGIN_USERID_INVALID = -127;

    public static final int RC_LOGIN_PASSWORD_INVALID = -128;

    public static final int RC_LOGIN_USERID_NOTEXIST = -129;

    public static final int RC_LOGIN_PASSWORD_NOTEXIST = -130;

    public static final int RC_DATASOURCE_UNOBTAINABLE = -131;

    public static final int RC_ITEM_NOT_SELECTED = -132;

    public static final int RC_INVALID_REQUEST = -133;

    public static final int RC_USER_NOT_LOGGED_IN = -134;

    public static final int RC_BAD_LOGIN_COLUMNNAME = -135; // SCR003 02182003

    public static final int RC_MAX_DBASSIGN_EXCEEDED = -136; // SCR004 02212003

    public static final int RC_CONNECTPOOL_INVALID = -137; // SCR004 02212003

    public static final int RC_CONNECTBEAN_INVALID = -138; // SCR004 02212003

    public static final int RC_DELETEROW_NOT_SELECTED = -140;

    public static final int RC_SQLSERVER_REF_INTEGRITY_CONFLICT = 547; // equates
                                                                       // to SQL
                                                                       // Server
                                                                       // Referential
                                                                       // Integrity
                                                                       // conflict
                                                                       // error

    public static final int RC_GRP_HAS_CHILDREN_CONFLICT = -141;

    public static final int RC_DATASOURCE_TABLE_NOTFOUND = -142;

    public static final int RC_API_DSO_INVALID = -143;

    public static final int RC_API_BEAN_INVALID = -144;

    public static final int RC_API_BEANNAME_NULL = -145;

    public static final int RC_USER_LOGGED_IN = -146;

    // System Exception API Messages
    public static final String MSG_INVALID_USERID = "The supplied user id must be valid in order to obtain a database connection object";

    public static final String MSG_CONNECTION_INVALID = "The database connection object is invalid";

    public static final String MSG_INVALID_SQL_STATEMENT = "SQL statement is invalid";

    public static final String MSG_FAIL_TO_CONNECT = "A database connection could not be established for the current connection object.  Possible causes: 1) database is down  2) Total allowable connections limit is exceeded.";

    public static final String MSG_DB_DOWN = "Database Server is down!";

    public static final String MSG_NULL_DB_DRIVER = "Database driver class was not specified.";

    public static final String MSG_NULL_DB_URL = "Database URL was not specified";

    public static final String MSG_NULL_DB_USERID = "Database user id was not specified";

    public static final String MSG_NULL_DB_PASSWORD = "Database Password was not specified";

    public static final String MSG_NULL_CONNECTION_COUNT = "The total number of database connections was not specified";

    public static final String MSG_NULL_DATASOURCE_TYPE = "Datasource Type cannot be null";

    public static final String MSG_LOGIN_USERID_NOTEXIST = "Login Id must be available in order to use an object that extends AbstractActionHandler";

    public static final String MSG_DATASOURCE_INVALID = "Data Source is invalid";

    public static final String MSG_DATASOURCE_TYPE_INVALID = "Data source type for RMT2DataSourceApiImpl is invalid";

    public static final String MSG_XML_DATASOURCE_INVALID = "Expected XML Data source, found found null instead";

    public static final String MSG_DATASOURCE_SQL_NOTEXIST = "SQL select statement does not exist for the current datasource";

    public static final String MSG_SYSTEM_DATA_NOTEXIST = "System data file, SystemParms.properties, does not exist";

    public static final String MSG_NULL_METHOD_ARGUMENT = "Method cannot contain any null arguments";

    public static final String MSG_DATASOURCE_COLUMN_NOTFOUND = "DataSource does not contain target column";

    public static final String MSG_SQL_CLAUSE_NOTFOUND = "DataSource does not contain the Select Statement attribute: ";

    public static final String MSG_NULL_DATASOURCE_VLAUE = "Cannot add or associate a null value with an attribute for a DataSource object.  This operation triggered a java NullPointerException";

    public static final String MSG_INVALID_STRING_DATE_VALUE = "The date value entered is invalid and could not be converted to java.util.Date or java.sql.Date";

    public static final String MSG_INVALID_DATE_FORMAT = "Invalid Date Format";

    public static final String MSG_INVALID_NUMBER_FORMAT = "Invalid Number Format";

    public static final String MSG_INVALID_STRING_NUMBER_VALUE = "String value could not be converted to java.lang.Number";

    public static final String MSG_LOGIN_PARAMETER_INVALID = "Login Hashtable is invalid";

    public static final String MSG_LOGIN_USERID_INVALID = "The User Id is invalid and cannot be null";

    public static final String MSG_LOGIN_PASSWORD_INVALID = "The Password is invalid and cannot be null";

    public static final String MSG_LOGIN_PASSWORD_NOTEXIST = "The Password entered is incorrect";

    public static final String MSG_DATASOURCE_UNOBTAINABLE = "A valid Datasource Object could not be obtained from the Database Connection object";

    public static final String MSG_ITEM_NOT_SELECTED = "The following operation cannot be performed without selecting an item";

    public static final String MSG_INVALID_REQUEST = "Request object is invalid";

    public static final String MSG_USER_NOT_LOGGED_IN = "User is not logged into the system";

    public static final String MSG_BAD_LOGIN_COLUMNNAME = "Unable to obtain column name metadata for login Id column."; // SCR003
                                                                                                                        // 02182003

    public static final String MSG_MAX_DBASSIGN_EXCEEDED = "The maximum number of database conntections has been reached.   Consult Database Administrator to increase to total number of concurrent database connections"; // SCR004
                                                                                                                                                                                                                            // 02212003

    public static final String MSG_CONNECTPOOL_INVALID = "Unable to obtain the connection pool."; // SCR004
                                                                                                  // 02212003

    public static final String MSG_CONNECTBEAN_INVALID = "Unable to obtain the connection bean for the current process."; // SCR004
                                                                                                                          // 02212003

    public static final String MSG_DELETEROW_NOT_SELECTED = "A row must be selected before performing a delete";

    public static final String MSG_SQLSERVER_REF_INTEGRITY_CONFLICT = "The item cannot be deleted, because it is associated with detail data in other areas of the database.";

    public static final String MSG_GRP_HAS_CHILDREN_CONFLICT = "The target Code Group cannot be deleted, because it is associated with one or more code details.   The associated code details must be removed from the system prior to deleting target code group.";

    public static final String MSG_DATASOURCE_TABLE_NOTFOUND = "DataSource does not contain target table name";

    public static final String MSG_API_DSO_INVALID = "API Datasource object is not valid when attempting to find data.  Ensure that the base Datasource View name has been set";

    public static final String MSG_API_BEAN_INVALID = "API Bean object is not valid when attempting to find data.  Ensure that the base Bean Class Name has been set";

    public static final String MSG_API_BEANNAME_NULL = "Bean Name cannot be null or blank";

    public static final String MSG_USER_LOGGED_IN = "User is already logged into the system";

}