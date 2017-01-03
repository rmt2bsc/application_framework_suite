package com.api;

import java.sql.Connection;
import java.sql.ResultSet;

import com.NotFoundException;
import com.RMT2Base;
import com.RMT2Constants;
import com.SystemException;
import com.api.persistence.DatabaseException;
import com.api.persistence.db.orm.bean.ObjectMapperAttrib;

/**
 * This class provides numerous stub methods which statisfies the minimum
 * implemention requirements of the DaoApi interface.
 * 
 * @author roy.terrell
 * 
 */
public class DaoApiStub extends RMT2Base {

    private boolean returnRowCount;

    /*
     * (non-Javadoc)
     * 
     * @see com.api.db.DatabaseTransApi#commitUOW()
     */
    public int commitUOW() {
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.db.DatabaseTransApi#rollbackUOW()
     */
    public int rollbackUOW() {
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.db.DatabaseTransApi#close()
     */
    public void close() throws DatabaseException {
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.db.DatabaseTransApi#close(boolean)
     */
    public void close(boolean replenishPool) throws DatabaseException {
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.db.DatabaseTransApi#getConnection()
     */
    public Connection getConnection() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#setConnector(java.lang.Object)
     */
    public void setConnector(Object value) {
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#getConnector()
     */
    public Object getConnector() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#setDataSourceName(java.lang.String)
     */
    public void setDataSourceName(String dsn) {
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#getDataSourceName()
     */
    public String getDataSourceName() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#setBaseView(java.lang.String)
     */
    public String setBaseView(String value) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#getBaseView()
     */
    public String getBaseView() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#setBaseClass(java.lang.String)
     */
    public String setBaseClass(String value) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#getBaseClass()
     */
    public String getBaseClass() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#getDataSourceAttib()
     */
    public ObjectMapperAttrib getDataSourceAttib() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#setResultType(int)
     */
    public void setResultType(int value) {
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#getResultType()
     */
    public int getResultType() {
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#setColumnValue(java.lang.String, java.lang.Object)
     */
    public void setColumnValue(String _property, Object value)
            throws SystemException, NotFoundException, DatabaseException {
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#getColumnValue(java.lang.String)
     */
    public String getColumnValue(String _property) throws DatabaseException,
            NotFoundException, SystemException {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#getColumnBinaryValue(java.lang.String)
     */
    public Object getColumnBinaryValue(String property)
            throws DatabaseException, NotFoundException, SystemException {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.db.DatabaseTransApi#connect(java.lang.String,
     * java.lang.String, java.lang.Object)
     */
    public Object connect(String user, String password, Object dataSource)
            throws DatabaseException {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#executeXmlQuery(java.lang.String)
     */
    public String executeXmlQuery(String sql) throws DatabaseException,
            SystemException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @return the returnRowCount
     */
    public boolean isReturnRowCount() {
        return returnRowCount;
    }

    /**
     * @param returnRowCount
     *            the returnRowCount to set
     */
    public void setReturnRowCount(boolean returnRowCount) {
        this.returnRowCount = returnRowCount;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#executeQuery(java.lang.String)
     */
    public ResultSet executeQuery(String sql) throws DatabaseException,
            SystemException {
        throw new UnsupportedOperationException(
                RMT2Constants.MSG_METHOD_NOT_SUPPORTED);
    }

    public int executeUpdate(String sql) throws DatabaseException {
        throw new UnsupportedOperationException(
                RMT2Constants.MSG_METHOD_NOT_SUPPORTED);
    }
} // end class

