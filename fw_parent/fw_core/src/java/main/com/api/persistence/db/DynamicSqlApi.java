package com.api.persistence.db;

import com.SystemException;
import com.api.persistence.DatabaseException;

import java.sql.ResultSet;
import java.sql.CallableStatement;
import java.sql.Statement;

/**
 * Interface that provides functionality for executing dynamic SQL and database
 * stored procedures and functions.
 * 
 * @author roy.terrell
 * 
 */
public interface DynamicSqlApi {

    /**
     * Release database resources.
     */
    void close();

    /**
     * Remove all keys from the parms collection property.
     * 
     */
    void clearParms();

    /**
     * Creates and populates a DynamSqlParmBean in which the value of the
     * parameter is of type Object. The DynamSqlParmBean object is added to the
     * parameter collection and returns the index of where the DynamSqlParmBean
     * parameter is positioned in parameter collection.
     * 
     * @param _dbColName
     *            The name of the targeted database column.
     * @param _dataType
     *            The data type of the _dbColName.
     * @param _value
     *            The Object value of the parameter.
     * @param _outParm
     *            boolean parameter which determines if parameter is to be
     *            treated as a output parameter.
     * @return The total number of parameters collected thus far.
     * @throws SystemException
     */
    int addParm(String _dbColName, int _dataType, Object _value,
            boolean _outParm) throws SystemException;

    /**
     * Creates and populates a DynamSqlParmBean in which the value of the
     * parameter is of type int. The DynamSqlParmBean object is added to the
     * parameter collection and returns the index of where the DynamSqlParmBean
     * parameter is positioned in parameter collection.
     * 
     * @param _dbColName
     *            The name of the targeted database column.
     * @param _dataType
     *            The data type of the _dbColName.
     * @param _value
     *            The int value of the parameter.
     * @param _outParm
     *            boolean parameter which determines if parameter is to be
     *            treated as a output parameter.
     * @return The total number of parameters collected thus far.
     * @throws SystemException
     */
    int addParm(String _dbColName, int _dataType, int _value, boolean _outParm)
            throws SystemException;

    /**
     * Creates and populates a DynamSqlParmBean in which the value of the
     * parameter is of type float. The DynamSqlParmBean object is added to the
     * parameter collection and returns the index of where the DynamSqlParmBean
     * parameter is positioned in parameter collection.
     * 
     * @param _dbColName
     *            The name of the targeted database column.
     * @param _dataType
     *            The data type of the _dbColName.
     * @param _value
     *            The float value of the parameter.
     * @param _outParm
     *            boolean parameter which determines if parameter is to be
     *            treated as a output parameter.
     * @return The total number of parameters collected thus far.
     * @throws SystemException
     */
    int addParm(String _dbColName, int _dataType, float _value, boolean _outParm)
            throws SystemException;

    /**
     * Creates and populates a DynamSqlParmBean in which the value of the
     * parameter is of type double. The DynamSqlParmBean object is added to the
     * parameter collection and returns the index of where the DynamSqlParmBean
     * parameter is positioned in parameter collection.
     * 
     * @param _dbColName
     *            The name of the targeted database column.
     * @param _dataType
     *            The data type of the _dbColName.
     * @param _value
     *            The double value of the parameter.
     * @param _outParm
     *            boolean parameter which determines if parameter is to be
     *            treated as a output parameter.
     * @return The total number of parameters collected thus far.
     * @throws SystemException
     */
    int addParm(String _dbColName, int _dataType, double _value,
            boolean _outParm) throws SystemException;

    /**
     * Creates and populates a DynamSqlParmBean in which the value of the
     * parameter is of type long. The DynamSqlParmBean object is added to the
     * parameter collection and returns the index of where the DynamSqlParmBean
     * parameter is positioned in parameter collection.
     * 
     * @param _dbColName
     *            The name of the targeted database column.
     * @param _dataType
     *            The data type of the _dbColName.
     * @param _value
     *            The long value of the parameter.
     * @param _outParm
     *            boolean parameter which determines if parameter is to be
     *            treated as a output parameter.
     * @return The total number of parameters collected thus far.
     * @throws SystemException
     */
    int addParm(String _dbColName, int _dataType, long _value, boolean _outParm)
            throws SystemException;

    /**
     * Creates and populates a DynamSqlParmBean in which the value of the
     * parameter is of type short. The DynamSqlParmBean object is added to the
     * parameter collection and returns the index of where the DynamSqlParmBean
     * parameter is positioned in parameter collection.
     * 
     * @param _dbColName
     *            The name of the targeted database column.
     * @param _dataType
     *            The data type of the _dbColName.
     * @param _value
     *            The short value of the parameter.
     * @param _outParm
     *            boolean parameter which determines if parameter is to be
     *            treated as a output parameter.
     * @return The total number of parameters collected thus far.
     * @throws SystemException
     */
    int addParm(String _dbColName, int _dataType, short _value, boolean _outParm)
            throws SystemException;

    /**
     * This method will retrieve the requested output paramerter, _parmName. It
     * will first determine if the parameter is associated with the
     * CallableStatement object as an output parameter. Provided that the output
     * parameter exists, the parameter's index is obtained so that the value
     * returned from the stored procedure can be retrieved. The results are sent
     * back to the caller as a data type of java.lang.Object.
     * 
     * @param _parmName
     *            The name of the parameter to retrieve value for.
     * @return The value generically disguised as an Object.
     * @throws DatabaseException
     * @throws SystemException
     */
    Object getOutParm(String _parmName) throws DatabaseException,
            SystemException;

    /**
     * Executes _sql dynamically and directly and "as is" without preparing.
     * Possibly will return multiple result sets. Returns true if the next
     * result is a ResultSet object; false if it is an update count or there are
     * no more results. Use only if expecting zero or one result sets.
     * 
     * @param _sql
     *            The SQL statement to execute.
     * @return The JDBC result set.
     * @throws DatabaseException
     * @throws SystemException
     */
    ResultSet executeDirect(String _sql) throws DatabaseException,
            SystemException;

    /**
     * Creates a CallableStatement object using _sql, dynamically sets the
     * callable statement's parameters, and execute the sql statement associated
     * with the CallableStatement. _sql can be any kind of sql statement.
     * 
     * @param _sql
     *            The SQL statement to execute.
     * @return The result set.
     * @throws DatabaseException
     * @throws SystemException
     */
    ResultSet execute(String _sql) throws DatabaseException, SystemException;

    /**
     * Creates a batch Job message by invoking stored procedure, usp_batch_msg.
     * 
     * @param _batchLogId
     *            The batch log id
     * @param _msg
     *            The message to log
     */
    void createBatchMsg(String _batchLogId, String _msg);

    /**
     * Sets the value of the SQL statement that is to be executed.
     * 
     * @param value
     *            The SQL statement
     */
    void setSql(String value);

    /**
     * Gets the value fot he current SQL statement.
     * 
     * @return The SQL statement.
     */
    String getSql();

    /**
     * Gets the JDBC Statement object.
     * 
     * @return Statement.
     */
    Statement getStmt();

    /**
     * Gets the JDBC CallableStatement.
     * 
     * @return CallableStatement
     */
    CallableStatement getCallStmt();

    /**
     * Gets the JDBC ResultSet object.
     * 
     * @return ResultSet
     * @throws DatabaseException
     */
    ResultSet getRs() throws DatabaseException;

    /**
     * Assoicates a DatabaseConnectionBean with this object.
     * 
     * @param value
     *            {@link DatabaseConnectionBean}
     */
    void setConnector(Object value);

}
