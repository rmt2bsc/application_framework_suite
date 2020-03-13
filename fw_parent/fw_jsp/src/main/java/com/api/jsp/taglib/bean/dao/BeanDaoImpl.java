package com.api.jsp.taglib.bean.dao;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.NotFoundException;
import com.RMT2Constants;
import com.SystemException;
import com.api.DaoApi;
import com.api.DaoApiStub;
import com.api.persistence.DatabaseException;
import com.api.util.RMT2BeanUtility;
import com.api.util.RMT2Utility;

/**
 * This class implements the DaoApi interface by abstracting the functioanlity
 * to manage a list of java beans of a single java bean as the source of data.
 * Below is an example on how to cycle through a list of java beans and extract
 * data values from each bean element of the list:
 * 
 * <pre>
 * <i>
 *    DaoApi beanApi = BeanDaoFactory.createApi();
 *    try {   
 *       // Input as an argument to the method, retrieve, a list of arbitrary objects or a single bean.
 *       beanApi.retrieve(this.creditors);    
 *       // Iterate each bean and access its property values.
 *       while (beanApi.nextRow()) {	
 *          String acctNo = beanApi.getColumnValue("AccountNo");	
 *          String name = beanApi.getColumnValue("Name");	
 *          String glId = beanApi.getColumnValue("GlAccountId");	
 *          String creditLim = beanApi.getColumnValue("CreditLimit");	
 *          continue;    
 *       }
 *    }
 *    catch (Exception e) {   
 *       e.printStackTrace();
 *    }
 * </i>
 * </pre>
 * 
 * @author RTerrell
 * 
 */

class BeanDaoImpl extends DaoApiStub implements BeanDao {
    private static final long serialVersionUID = 2620585880717472272L;

    private Logger logger;

    private List list;

    private Object bean;

    private int count;

    private int curRow;

    private int javaType;

    RMT2BeanUtility beanUtil;

    /**
     * Construct a BeanDaoImpl object which initializes the logger.
     * 
     * @throws SystemException
     */
    public BeanDaoImpl() throws SystemException {
        super();
        this.logger = Logger.getLogger("BeanDaoImpl");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#deleteRow(java.lang.Object)
     */
    public int deleteRow(Object obj) throws DatabaseException {
        if (obj == null) {
            return -1;
        }
        int deleteCount = 0;
        while (this.list.remove(obj)) {
            this.count--;
            deleteCount++;
        }
        return deleteCount;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#findData(java.lang.Object)
     */
    public Object[] findData(Object obj) throws DatabaseException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#firstRow()
     */
    public boolean firstRow() throws DatabaseException, SystemException {
        boolean rowFound = false;
        if (this.count > 0) {
            this.bean = this.list.get(0);
            this.beanUtil = new RMT2BeanUtility(this.bean);
            rowFound = true;
        }
        return rowFound;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#getColumnValue(java.lang.String)
     */
    public String getColumnValue(String property) throws DatabaseException,
            NotFoundException, SystemException {
        if (this.curRow == DaoApi.BOF || this.curRow == DaoApi.EOF) {
            throw new DatabaseException(
                    "Bean cursor needs to be positioned using one of the navigator methods: nextRow, previousRow, firstRow, or lastRow");
        }

        if (this.beanUtil == null) {
            return null;
        }
        Object tempValue = this.beanUtil.getPropertyValue(property);
        String value = tempValue == null ? "" : tempValue.toString();
        if (value == null) {
            value = "";
        }
        return value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#setColumnValue(java.lang.String, java.lang.Object)
     */
    public void setColumnValue(String property, Object value)
            throws SystemException, NotFoundException, DatabaseException {
        if (this.curRow == DaoApi.BOF || this.curRow == DaoApi.EOF) {
            throw new DatabaseException(
                    "Bean cursor needs to be positioned using one of the navigator methods: nextRow, previousRow, firstRow, or lastRow");
        }

        if (this.beanUtil == null) {
            return;
        }
        RMT2BeanUtility beanUtil = new RMT2BeanUtility(this.bean);
        beanUtil.setPropertyValue(property, value);
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#getDataSourceName()
     */
    public String getDataSourceName() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#insertRow(java.lang.Object, boolean)
     */
    public int insertRow(Object obj, boolean autoKey) throws DatabaseException {
        if (obj == null) {
            return -1;
        }
        this.list.add(obj);
        this.count++;
        return this.count;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#lastRow()
     */
    public boolean lastRow() throws DatabaseException, SystemException {
        boolean rowFound = false;
        if (this.count > 0) {
            this.bean = this.list.get(this.count - 1);
            this.beanUtil = new RMT2BeanUtility(this.bean);
            rowFound = true;
        }
        return rowFound;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#nextRow()
     */
    public boolean nextRow() throws DatabaseException, SystemException {
        boolean rowFound = false;

        // We cannot navigate to the next node when the end-of-file point has
        // been reached.
        if ((this.curRow) == DaoApi.EOF) {
            return false;
        }
        // The current cursor position cannot be greater than the total number
        // of nodes.
        if ((this.curRow + 1) == this.count || this.count == 0) {
            // Position cursor to the end of file mark
            this.curRow = DaoApi.EOF;
            return false;
        }

        // In the event the cursor is pointing to BOF, set cursor position to a
        // point
        // where we can navigate to the first record.
        if (this.curRow == DaoApi.BOF) {
            this.curRow = -1;
        }

        // At this point it is okay to move to the next row
        this.curRow++;
        this.bean = this.list.get(curRow);
        this.beanUtil = new RMT2BeanUtility(this.bean);
        rowFound = true;

        return rowFound;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#previousRow()
     */
    public boolean previousRow() throws DatabaseException, SystemException {
        boolean rowFound = false;

        // We cannot navigate to the previous node when the begin-of-file point
        // has been reached.
        if ((this.curRow) == DaoApi.BOF) {
            return false;
        }
        // The current cursor position cannot be the first element.
        if ((this.curRow - 1) == -1 || this.count == 0) {
            // Position the cursor to begin-of-file mark
            this.curRow = DaoApi.BOF;
            return false;
        }

        // In the event the cursor is pointing to EOF, set cursor position to a
        // point
        // where we can navigate to the last record.
        if (this.curRow == DaoApi.EOF) {
            this.curRow = this.count;
        }

        // At this point it is okay to move to the previous row
        this.curRow--;
        this.bean = this.list.get(curRow);
        this.beanUtil = new RMT2BeanUtility(this.bean);
        rowFound = true;

        return rowFound;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#retrieve(java.lang.Object)
     */
    public Object[] retrieve(Object obj) throws DatabaseException {
        if (obj == null) {
            this.msg = "An invalid bean argument was discovered";
            this.logger.log(Level.ERROR, this.msg);
            throw new DatabaseException(this.msg);
        }

        this.setConnector(obj);
        Object results[] = new Object[1];
        results[0] = new Integer(this.count);
        return results;
    }

    /**
     * 
     * @param property
     * @return
     */
    public int getJavaType(String property) {
        String propertyType = beanUtil.getPropertyType(property).getName();
        int javaType = RMT2Utility.getJavaType(propertyType);
        return javaType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#getConnector()
     */
    public Object getConnector() {
        return this.list;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#setConnector(java.lang.Object)
     */
    public void setConnector(Object obj) {

        if (obj instanceof List) {
            // Set list member variable to obj provided obj is of type List
            this.list = (List) obj;
        }
        else {
            // obj must be a single bean, so create a list and add obj to list
            this.bean = obj;
            this.list = new ArrayList();
            this.list.add(obj);
        }
        this.count = this.list.size();
        this.curRow = DaoApi.BOF;
        this.beanUtil = null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#setDataSourceName(java.lang.String)
     */
    public void setDataSourceName(String dsn) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#updateRow(java.lang.Object)
     */
    public int updateRow(Object obj) throws DatabaseException {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#getColumnBinaryValue(java.lang.String)
     */
    public InputStream getColumnBinaryValue(String property)
            throws DatabaseException, NotFoundException, SystemException {
        throw new UnsupportedOperationException(
                RMT2Constants.MSG_METHOD_NOT_SUPPORTED);
    }

}
