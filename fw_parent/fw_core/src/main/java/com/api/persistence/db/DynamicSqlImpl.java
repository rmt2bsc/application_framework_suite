package com.api.persistence.db;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;

import com.SystemException;
import com.api.persistence.DatabaseException;
import com.api.persistence.db.orm.bean.DynamSqlParmBean;
import com.api.util.RMT2String;

import java.util.Hashtable;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.sql.Blob;
import java.sql.Clob;

/**
 * Responsible for providing dynamic functionality for handling SQL
 * transactions. Mainly design for dynamically building and executing stored
 * procedures and DML. Can be instantiated directly or subclassed.
 * 
 * @author appdev
 * 
 */

public class DynamicSqlImpl extends AbstractDatabasePersistenceImpl implements
        DynamicSqlApi {

    private String sql;

    // private PreparedStatement prepStmt;
    private CallableStatement callStmt;

    private Hashtable parms;

    private Hashtable outParms;

    private Logger logger;

    /**
     * Default constructor. Initializes most member variables.
     * 
     * @throws DatabaseException
     * @throws SystemException
     */
    public DynamicSqlImpl() throws DatabaseException, SystemException {
        super();
    }

    /**
     * Constructor that sets the Connection object and the loginId after
     * invoking the default constructor.
     * 
     * @param _con
     * @param _loginId
     * @throws DatabaseException
     * @throws SystemException
     */
    public DynamicSqlImpl(Connection _con, String _loginId)
            throws DatabaseException, SystemException {
        super(_con, _loginId);
    }

    /**
     * Initializes all pertinent variables needed for this class.
     */
    public void init() {
        super.init();
        this.parms = new Hashtable();
        this.outParms = new Hashtable();
        // this.prepStmt = null;
        this.callStmt = null;
        logger = Logger.getLogger("DynamicSqlImpl");
    } // End init

    /**
     * Releases the resources belonging to the callable statement used to
     * interact with the stored proc.
     */
    public void close() throws DatabaseException {
        try {
            super.close();
            if (this.callStmt != null) {
                // this.callStmt.close();
                this.callStmt = null;
            }
        }

        catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    public void clearParms() {
        this.parms.clear();
        this.outParms.clear();
    }

    /**
     * {@inheritDoc}
     */
    public int addParm(String _dbColName, int _dataType, Object _value,
            boolean _outParm) throws SystemException {
        if (_dbColName == null || _dbColName.length() <= 0) {
            this.msg = "First parameter, " + _dbColName + ", cannot be null";
            throw new SystemException(this.msg);
        }

        DynamSqlParmBean parmBean = new DynamSqlParmBean();
        parmBean.setDbColName(_dbColName);
        parmBean.setDataType(_dataType);
        // parmBean.setParmValue( (_value == null ? "" : _value) );
        parmBean.setParmValue(_value);
        parmBean.setOutParm(_outParm);
        parmBean.setIndex(parms.size() + 1);
        parms.put(String.valueOf(parmBean.getIndex()), parmBean);
        return parms.size();
    }

    /**
     * {@inheritDoc}
     */
    public int addParm(String _dbColName, int _dataType, int _value,
            boolean _outParm) throws SystemException {
        Integer intValue = new Integer(_value);
        return this.addParm(_dbColName, _dataType, intValue.toString(),
                _outParm);
    }

    /**
     * {@inheritDoc}
     */
    public int addParm(String _dbColName, int _dataType, float _value,
            boolean _outParm) throws SystemException {
        Float floatValue = new Float(_value);
        return this.addParm(_dbColName, _dataType, floatValue, _outParm);
    }

    /**
     * {@inheritDoc}
     */
    public int addParm(String _dbColName, int _dataType, double _value,
            boolean _outParm) throws SystemException {
        Double doubleValue = new Double(_value);
        return this.addParm(_dbColName, _dataType, doubleValue, _outParm);
    }

    /**
     * {@inheritDoc}
     */
    public int addParm(String _dbColName, int _dataType, long _value,
            boolean _outParm) throws SystemException {
        Long longValue = new Long(_value);
        return this.addParm(_dbColName, _dataType, longValue, _outParm);
    }

    /**
     * {@inheritDoc}
     */
    public int addParm(String _dbColName, int _dataType, short _value,
            boolean _outParm) throws SystemException {
        Short shortValue = new Short(_value);
        return this.addParm(_dbColName, _dataType, shortValue, _outParm);
    }

    /**
     * 
     * @param _dbColName
     * @param _dataType
     * @param _value
     * @param _outParm
     * @return
     * @throws SystemException
     */
    public int addParm(String _dbColName, int _dataType, java.util.Date _value,
            boolean _outParm) throws SystemException {
        java.util.Date dateValue = new java.util.Date(_value.getTime());
        return this.addParm(_dbColName, _dataType, dateValue, _outParm);
    }

    /**
     * Sets the CallableStatment's parameters based on the list of
     * DynamSqlParmBean objects that exist in the Hashtable, parms'.
     * 
     * @return
     * @throws DatabaseException
     * @throws SystemException
     */
    protected int setParms() throws DatabaseException, SystemException {

        int keyNdx = 0;
        String key = null;
        Enumeration keys;
        DynamSqlParmBean bean;

        try {
            if (this.callStmt == null) {
                this.callStmt = this.getConnection().prepareCall(this.sql);
            }
            else {
                this.callStmt.clearParameters(); // Clear CallabeStatement's
                // parameters
            }

            keys = parms.keys();
            while (keys.hasMoreElements()) {
                key = keys.nextElement().toString();
                bean = (DynamSqlParmBean) parms.get(key);
                keyNdx = new Integer(key).intValue();
                this.setParms(bean);
            }

            return keyNdx; // return total number of parameters set.
        } // end try

        catch (NullPointerException e) {
            throw new SystemException(
                    "Attempted to obtain a Hashtable value which the key is null");
        } catch (NumberFormatException e) {
            throw new SystemException(
                    "Parm Key could not be coverted to a number");
        } catch (NoSuchElementException e) {
            throw new SystemException(
                    "Dynamic SQL Parameter element cannot be found");
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * Determines which setXXX method to call for a given CallableStatement's
     * parameter based on the parameter's data type. Once the data type is
     * determined, the parameter's value is converted to that of its data t ype.
     * The parameter is registered as an inout or out parameter provided that it
     * is declared as such. Returns true if the parameter is successfully set
     * and/or registered. Otherwise, it returns false.
     * 
     * @param _bean
     * @return
     * @throws DatabaseException
     * @throws SystemException
     */
    private boolean setParms(DynamSqlParmBean _bean) throws DatabaseException,
            SystemException {
        String strValue;
        try {
            switch (_bean.getDataType()) {
                case Types.INTEGER:
                case Types.TINYINT:
                case Types.SMALLINT:
                    int intValue;
                    intValue = new Integer(_bean.getParmValue().toString())
                            .intValue();
                    this.callStmt.setInt(_bean.getIndex(), intValue);
                    if (_bean.isOutParm()) {
                        callStmt.registerOutParameter(_bean.getIndex(),
                                Types.INTEGER);
                    }
                    break;

                case Types.BIGINT:
                    long longValue;
                    longValue = new Long(_bean.getParmValue().toString())
                            .longValue();
                    this.callStmt.setLong(_bean.getIndex(), longValue);
                    if (_bean.isOutParm()) {
                        callStmt.registerOutParameter(_bean.getIndex(),
                                Types.BIGINT);
                    }
                    break;

                case Types.CHAR:
                case Types.VARCHAR:
                case Types.LONGVARCHAR:
                    strValue = (String) _bean.getParmValue();
                    this.callStmt.setString(_bean.getIndex(), strValue);
                    if (_bean.isOutParm()) {
                        callStmt.registerOutParameter(_bean.getIndex(),
                                Types.VARCHAR);
                    }
                    break;

                case Types.DATE:
                    java.sql.Date dtValue = null;
                    Object dateObj = _bean.getParmValue();
                    if (dateObj instanceof java.util.Date) {
                        long milliSecs = ((java.util.Date) dateObj).getTime();
                        dtValue = new java.sql.Date(milliSecs);
                    }
                    else {
                        dtValue = (java.sql.Date) _bean.getParmValue();
                    }
                    this.callStmt.setDate(_bean.getIndex(), dtValue);
                    if (_bean.isOutParm()) {
                        callStmt.registerOutParameter(_bean.getIndex(),
                                Types.DATE);
                    }
                    break;

                case Types.TIME:
                    Time tValue = (Time) _bean.getParmValue();
                    this.callStmt.setTime(_bean.getIndex(), tValue);
                    if (_bean.isOutParm()) {
                        callStmt.registerOutParameter(_bean.getIndex(),
                                Types.TIME);
                    }
                    break;

                case Types.TIMESTAMP:
                    Object tsTemp = _bean.getParmValue();
                    if (tsTemp instanceof Timestamp) {
                        Timestamp tsValue = (Timestamp) _bean.getParmValue();
                        this.callStmt.setTimestamp(_bean.getIndex(), tsValue);
                    }
                    else {
                        this.callStmt.setTimestamp(_bean.getIndex(), null);
                    }
                    if (_bean.isOutParm()) {
                        callStmt.registerOutParameter(_bean.getIndex(),
                                Types.TIMESTAMP);
                    }
                    break;

                case Types.REAL:
                case Types.FLOAT:
                    float floatValue;
                    floatValue = new Float(_bean.getParmValue().toString())
                            .floatValue();
                    this.callStmt.setFloat(_bean.getIndex(), floatValue);
                    if (_bean.isOutParm()) {
                        callStmt.registerOutParameter(_bean.getIndex(),
                                Types.FLOAT);
                    }
                    break;

                case Types.NUMERIC:
                case Types.DOUBLE:
                case Types.DECIMAL:
                    double doubleValue;
                    doubleValue = new Double(_bean.getParmValue().toString())
                            .doubleValue();
                    this.callStmt.setDouble(_bean.getIndex(), doubleValue);
                    if (_bean.isOutParm()) {
                        callStmt.registerOutParameter(_bean.getIndex(),
                                Types.DOUBLE);
                    }
                    break;

                case Types.BLOB:
                    strValue = _bean.getParmValue().toString();
                    this.callStmt.setString(_bean.getIndex(), strValue);
                    if (_bean.isOutParm()) {
                        callStmt.registerOutParameter(_bean.getIndex(),
                                Types.BLOB);
                    }
                    break;

                case Types.CLOB:
                    strValue = _bean.getParmValue().toString();
                    this.callStmt.setString(_bean.getIndex(), strValue);
                    if (_bean.isOutParm()) {
                        callStmt.registerOutParameter(_bean.getIndex(),
                                Types.CLOB);
                    }
                    break;

                default:
                    return false;
            } // end switch

            // Register column and its parameter index with this object as an
            // output parameter for tracking purposes.
            if (_bean.isOutParm()) {
                this.outParms.put(_bean.getDbColName(),
                        String.valueOf(_bean.getIndex()));
            }

            return true;
        }

        catch (NumberFormatException e) {
            throw new SystemException(
                    "Stored procedure input parameter value could not be converted to a number");
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    public Object getOutParm(String _parmName) throws DatabaseException,
            SystemException {
        int parmNdx = 0;
        int ndx;
        int parmCount;
        // boolean parmFound = false;
        String temp;
        DynamSqlParmBean parmBean;

        // Determine if _parmName exists as an output parameter.
        if (this.outParms.containsKey(_parmName)) {
            // Get parametere Index of _parmName
            temp = this.outParms.get(_parmName).toString();
            if (temp != null) {
                parmNdx = new Integer(temp).intValue();
            }
        }
        else {
            return null;
        }

        parmBean = new DynamSqlParmBean();

        // Since we have obtained the parameter index of _parmName, we can
        // safely assume that _parmName exist as a parameter for the stored
        // procedure call and retrieve its datatype..
        parmCount = this.parms.size();
        for (ndx = 1; ndx <= parmCount; ndx++) {
            parmBean = (DynamSqlParmBean) this.parms.get(String.valueOf(ndx));
            if (parmBean.getDbColName().equalsIgnoreCase(_parmName)) {
                // parmFound = true;
                break;
            }
        }

        // Now that the datatype of _parmName has been obtained,
        // we can package the value into a java object.
        try {
            switch (parmBean.getDataType()) {
                case Types.INTEGER:
                case Types.TINYINT:
                case Types.SMALLINT:
                    int intValue;
                    intValue = this.callStmt.getInt(parmNdx);
                    Integer intObj = new Integer(intValue);
                    return intObj;

                case Types.BIGINT:
                    long longValue;
                    longValue = this.callStmt.getLong(parmNdx);
                    Long longObj = new Long(longValue);
                    return longObj;

                case Types.CHAR:
                case Types.VARCHAR:
                case Types.LONGVARCHAR:
                    String strValue = this.callStmt.getString(parmNdx);
                    return strValue;

                case Types.DATE:
                    java.sql.Date dtValue = this.callStmt.getDate(parmNdx);
                    return dtValue;

                case Types.TIME:
                    Time tmValue = this.callStmt.getTime(parmNdx);
                    return tmValue;

                case Types.TIMESTAMP:
                    Timestamp tsValue = this.callStmt.getTimestamp(parmNdx);
                    return tsValue;

                case Types.REAL:
                case Types.FLOAT:
                    float floatValue;
                    floatValue = this.callStmt.getFloat(parmNdx);
                    Float floatObj = new Float(floatValue);
                    return floatObj;

                case Types.NUMERIC:
                case Types.DOUBLE:
                case Types.DECIMAL:
                    double doubleValue;
                    doubleValue = this.callStmt.getDouble(parmNdx);
                    Double doubleObj = new Double(doubleValue);
                    return doubleObj;

                case Types.BLOB:
                    Blob blobObj = this.callStmt.getBlob(parmNdx);
                    return blobObj;

                case Types.CLOB:
                    Clob clobObj = this.callStmt.getClob(parmNdx);
                    return clobObj;
            } // end switch

            return null;
        }

        catch (NumberFormatException e) {
            throw new SystemException(
                    "Stored Procedure output parameter value could not be converted to a number");
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLExcepiotn: " + e.getMessage());
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    public ResultSet executeDirect(String _sql) throws DatabaseException,
            SystemException {

        this.sql = _sql;
        try {
            this.stmt = this.getConnection().createStatement();
            if (this.stmt.execute(this.sql)) {
                return this.stmt.getResultSet();
            }
            else {
                return null;
            }
        } catch (SQLException e) {
            throw new SystemException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    public ResultSet execute(String _sql) throws DatabaseException,
            SystemException {

        logger.log(Level.DEBUG, "SQL Statement to execute: " + _sql);
        logger.log(Level.DEBUG, "RMT2Utility's count of '?' in SQL Statement: "
                + RMT2String.countChar(_sql, '?'));
        logger.log(Level.DEBUG,
                "Total number of place holder elements included in this.parms collection: "
                        + this.parms.size());

        if (RMT2String.countChar(_sql, '?') != this.parms.size()) {
            String msg = "Total number of place holders in dynamic sql statement does equal the total number of parameters setup by caller";
            throw new SystemException(msg);
        }

        this.sql = _sql;

        try {
            this.callStmt = this.getConnection().prepareCall(this.sql);
            this.setParms();
            if (this.callStmt.execute()) {
                return this.callStmt.getResultSet();
            }
            else {
                // update dbChanageCount so that commitTrans method will work.
                this.dbChangeCount = this.callStmt.getUpdateCount();
                return null;
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    public ResultSet getRs() throws DatabaseException {
        try {
            if (this.callStmt == null) {
                return null;
            }
            if (!this.callStmt.getMoreResults()) {
                return null;
            }
            this.rs = this.callStmt.getResultSet();
            return this.rs;
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    public void createBatchMsg(String _batchLogId, String _msg) {
        try {
            this.clearParms();
            this.addParm("batch_Job_id", Types.VARCHAR, _batchLogId, false);
            this.addParm("msg", Types.VARCHAR, _msg, false);
            this.execute("exec usp_batch_msg ? ?");
        } catch (SystemException e) {
        } catch (DatabaseException e) {
        }
    }

    /**
     * {@inheritDoc}
     */
    public Statement getStmt() {
        return this.stmt;
    }

    /**
     * {@inheritDoc}
     */
    public CallableStatement getCallStmt() {
        return this.callStmt;
    }

    /**
     * 
     */
    public void setSql(String value) {
        this.sql = value;
    }

    /**
     * 
     */
    public String getSql() {
        return this.sql;
    }

    /**
     * 
     */
    public void setConnector(Object value) {
        super.setConnector(value);
    }

} // end class

