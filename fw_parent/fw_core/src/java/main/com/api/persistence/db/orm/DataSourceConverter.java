package com.api.persistence.db.orm;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.NotFoundException;
import com.RMT2Base;
import com.SystemException;
import com.api.DaoApi;
import com.api.DataSourceApi;
import com.api.config.AppPropertyPool;
import com.api.persistence.DatabaseException;
import com.api.persistence.db.orm.bean.DataSourceColumn;
import com.api.persistence.db.orm.bean.ObjectMapperAttrib;
import com.api.persistence.db.orm.bean.TableUsageBean;
import com.util.RMT2BeanUtility;
import com.util.RMT2Utility;

/**
 * Abstract class that provides methods for converting data from one source to
 * another. Various conversion combinations are avialble such as:
 * <ul>
 * <li>OrmBean to DataSourceApi</li>
 * <li>HttpServletRequest to DataSourcApi</li>
 * <li>OrmBean to DaoApi</li>
 * <li>HttpServletRequest to OrmBean</li>
 * <li>XML to OrmBean</li>
 * <li>OrmBean to XML</li>
 * </ul>
 * <p>
 * An example of marshalling an ORmBean instance to XML and unmarshalling XML to
 * OrmBean: <blockquote> UserApi api2 = UserFactory.createXmlApi(conBean);<br>
 * String data = null;<br>
 * data = (String) api2.findUserByLoginId(loginId);>br> java.util.List list =
 * DataSourceConverter.unMarshallOrmBean(data);<br>
 * data = DataSourceConverter.marshallOrmBean(conBean, list); </blockquote>
 * 
 * @author appdev
 * 
 */
public abstract class DataSourceConverter extends RMT2Base {
    private static Logger logger = Logger.getLogger("DataSourceConverter");

    private static final String DB_NULL = "null";

    private static final int DSO_TO_BEAN_THREAD_ELIGIBLITY_COUNT = 50;

    /**
     * Creates multiple {@link OrmBean} derived objects from data contained in
     * an instance of DataSourceApi.
     * <p>
     * If applicable, the end result will be List of OrmBean derived instances
     * where each List element represents a row from <i>dso</i>. Override this
     * method at the descendent level in order to provide custom packaging
     * logic.
     * 
     * @param dso
     *            an instance of {@link DataSourceApi} containing the data that
     *            is to be transfered.
     * @param beanClass
     *            the class name of the bean that will recieve the data
     *            transfer.
     * @return a List of {@link OrmBean} objects representing the data that was
     *         transferred from <i>dso</i>.
     * @throws NotFoundException
     * @throws SystemException
     * @throws DatabaseException
     */
    public static final List<OrmBean> packageBean(DataSourceApi dso,
            String beanClass) throws NotFoundException, SystemException,
            DatabaseException {
        if (dso == null) {
            throw new SystemException(
                    "Datasource is not valid or does not have a valid connection object");
        }
        if (beanClass == null || beanClass.length() <= 0) {
            throw new SystemException(
                    "API Bean object is not valid when attempting to find data.  Ensure that the base Bean Class Name has been set");
        }

        RMT2BeanUtility beanUtil = new RMT2BeanUtility();
        List<OrmBean> list = new ArrayList<OrmBean>();
        int rows = dso.getRowCount();

        while (dso.nextRow()) {
            OrmBean newBean = (OrmBean) beanUtil.createBean(beanClass);
            packageBean(dso, newBean);
            list.add(newBean);
        }

        // // Package data within the current thread since the data set is
        // // relatively small.
        // if (rows <= DataSourceConverter.DSO_TO_BEAN_THREAD_ELIGIBLITY_COUNT)
        // {
        // while (dso.nextRow()) {
        // OrmBean newBean = (OrmBean) beanUtil.createBean(beanClass);
        // packageBean(dso, newBean);
        // list.add(newBean);
        // }
        // }
        // else {
        // // Use separate thread to package a bean for each row that exist in
        // // the DSO to prevent latency
        // for (int ndx = 0; ndx < rows; ndx++) {
        // OrmBean ormBean = (OrmBean) beanUtil.createBean(beanClass);
        // list.add(ormBean);
        // }
        // DatasourceToBeanConverterThread t = new
        // DatasourceToBeanConverterThread(
        // dso, list);
        // t.start();
        // }

        // while (dso.nextRow()) {
        // OrmBean newBean = (OrmBean) beanUtil.createBean(beanClass);
        // packageBean(dso, newBean);
        // list.add(newBean);
        // }
        return list;
    }

    /**
     * Packages a JavaBean object from the current row of a <i>DataSourceApi</i>
     * instance.
     * 
     * @param dso
     *            An instance of {@link DataSourceApi} serving as the source of
     *            the data transfer of one row. The data source should be
     *            positioned on a row within the valid range of the first row
     *            and last row.
     * 
     * @param beanObj
     *            an instance of {@link OrmBean} serving as the destination of
     *            the data transfer.
     * @return Always return 1
     * @throws SystemException
     */
    public static final int packageBean(DataSourceApi dso, OrmBean beanObj)
            throws SystemException {
        String property = null;
        Object value = null;
        List<String> props = null;

        if (dso == null) {
            throw new SystemException(
                    "Datasource is not valid or does not have a valid connection object");
        }

        // Setup the Bean Utility for _bean
        RMT2BeanUtility srcBeanUtil = new RMT2BeanUtility(beanObj);

        // Retrieve all property names associated with _bean
        props = srcBeanUtil.getPropertyNames();
        try {
            // Obtain the value of each property name of the datasource
            // and assign that value to the respective property name of _bean.
            for (int ndx = 0; ndx < props.size(); ndx++) {
                try {
                    property = RMT2Utility.getBeanMethodName(props.get(ndx)
                            .toString());
                    Class clazz = srcBeanUtil.getPropertyType(property);
                    String name = null;
                    if (clazz != null) {
                        name = clazz.getName();
                    }
                    if (name != null && name.equals("java.io.InputStream")) {
                        value = dso.getColumnBinaryValue(property);
                    }
                    else {
                        value = dso.getColumnValue(property);
                    }
                    srcBeanUtil.setPropertyValue(property, value);
                    DataSourceConverter.logger.log(Level.DEBUG,
                            "packageBean Property: " + property);
                    DataSourceConverter.logger.log(Level.DEBUG,
                            "packageBean Value: " + value);

                } catch (NotFoundException e) {
                    // Go to next property if the property does not exist in the
                    // datasource
                    DataSourceConverter.logger.log(Level.DEBUG, e.getMessage()
                            + " - Property: " + property);
                    continue;
                }
            }
            return 1;
        } catch (DatabaseException e) {
            throw new SystemException(e);
        }
    }

    /**
     * Packages a JavaBean object from a Properties object.
     * 
     * @param prop
     *            A Properties object containing the data to be converted.
     * @param bean
     *            The destination bean.
     * @return Total number of columns converted.
     * @throws SystemException
     */
    public static final int packageBean(Properties prop, Object bean)
            throws SystemException {
        String property = null;
        Object value = null;
        Enumeration propNames = prop.propertyNames();
        int totColsFoundCount = 0;

        // Setup the Bean Utility for _bean
        RMT2BeanUtility beanUtil = new RMT2BeanUtility(bean);

        // Cycle through all request parameters
        while (propNames.hasMoreElements()) {
            try {

                // Obtain the parameter name and value
                property = propNames.nextElement().toString();
                value = prop.getProperty(property);

                // Uncomment for debugging
                DataSourceConverter.logger.log(Level.DEBUG, "Property: "
                        + property);
                DataSourceConverter.logger.log(Level.DEBUG, "Value: " + value);

                // Go to next parameter if the target parameter does not exits
                if (value == null) {
                    continue;
                }
                // Convert the property name to proper Bean specification casing
                property = RMT2Utility.getBeanMethodName(property);
                DataSourceConverter.logger.log(Level.DEBUG,
                        "Revised Property Name: " + property);

                // Assign the target bean property its value
                beanUtil.setPropertyValue(property, value);
                totColsFoundCount++;
            } catch (NotFoundException e) {
                DataSourceConverter.logger.log(Level.DEBUG, e.getMessage()
                        + " - Property: " + property);
                continue;
            }
        }
        return totColsFoundCount;
    }

    /**
     * Packages a JavaBean object from a Properties object.
     * 
     * @param prop
     *            A Properties object containing the data to be converted.
     * @param bean
     *            The destination bean.
     * @param row
     *            The row index to identify the property by.
     * @return Total number of columns converted.
     * @throws SystemException
     */
    public static final int packageBean(Properties prop, Object bean, int row)
            throws SystemException {
        String property = null;
        String propName = null;
        String rowNdx = null;
        Object value = null;
        List<String> propNames = new ArrayList<String>();
        int totColsFoundCount = 0;

        // Convert row index to string so that we can concatenate it with
        // each property name of the target row of the request object.
        rowNdx = (row < 0 ? "" : String.valueOf(row));

        // Setup the Bean Utility for _bean
        RMT2BeanUtility beanUtil = new RMT2BeanUtility(bean);
        propNames = beanUtil.getPropertyNames();

        // Cycle through all request parameters
        for (int ndx = 0; ndx < propNames.size(); ndx++) {
            try {

                // Obtain a property name
                property = (String) propNames.get(ndx);
                // Convert the property name to proper Bean specification casing
                propName = RMT2Utility.getBeanMethodName(property) + rowNdx;

                value = prop.getProperty(propName);

                // Uncomment for debugging
                DataSourceConverter.logger.log(Level.DEBUG,
                        "packageBean Property: " + propName);
                DataSourceConverter.logger.log(Level.DEBUG,
                        "packageBean Value: " + value);

                // Go to next parameter if the target parameter does not exits
                if (value == null) {
                    continue;
                }

                // Assign the target bean property its value
                beanUtil.setPropertyValue(property, value);
                totColsFoundCount++;
            } catch (NotFoundException e) {
                DataSourceConverter.logger.log(Level.DEBUG, e.getMessage()
                        + " - Property: " + property);
                continue;
            }
        }
        return totColsFoundCount;
    }

    // /**
    // * Packages a JavaBean object from a HttpServletRequest.
    // *
    // * @param request
    // * A HttpServletRequest object containing the data to be
    // * converted.
    // * @param bean
    // * The destination bean.
    // * @return Total number of columns converted.
    // * @throws SystemException
    // */
    // public static final int packageBean(Request request, Object bean)
    // throws SystemException {
    //
    // Properties prop = RMT2Utility.getRequestData(request);
    // return DataSourceConverter.packageBean(prop, bean);
    // }

    // /**
    // * Packages a JavaBean object from an indexed row of the
    // HttpServletRequest
    // * object.
    // *
    // * @param request
    // * A HttpServletRequest object containing the data to be
    // * converted.
    // * @param bean
    // * The destination bean.
    // * @param row
    // * The row index to identify the property by.
    // * @return Total number of columns converted.
    // * @throws SystemException
    // */
    // public static final int packageBean(Request request, Object bean, int
    // row)
    // throws SystemException {
    // Properties prop = RMT2Utility.getRequestData(request);
    // return DataSourceConverter.packageBean(prop, bean, row);
    // }

    /**
     * Packages a RMT2DataSourceApi object from a JavaBean
     * 
     * @param _dso
     * @param _bean
     * @return
     * @throws SystemException
     */
    public static final int packageDSO(DataSourceApi _dso, Object _bean)
            throws SystemException {
        String property = null;
        Object value = null;
        List<String> props = null;

        if (_dso == null) {
            throw new SystemException(
                    "Datasource is not valid or does not have a valid connection object");
        }

        // Setup the Bean Utility for _bean
        RMT2BeanUtility srcBeanUtil = new RMT2BeanUtility(_bean);

        // Retrieve all property names associated with _bean
        props = srcBeanUtil.getPropertyNames();

        try {
            for (int ndx = 0; ndx < props.size(); ndx++) {
                try {
                    // Get and convert the property name to proper Bean
                    // specification casing
                    property = RMT2Utility.getBeanMethodName(props.get(ndx)
                            .toString());

                    // Get the value that was assigned to the target property of
                    // _bean
                    value = srcBeanUtil.getPropertyValue(property);

                    // Uncomment for debugging
                    DataSourceConverter.logger.log(Level.DEBUG,
                            "packageDSO Property: " + property);
                    DataSourceConverter.logger.log(Level.DEBUG,
                            "packageDSO Value: " + value);

                    // If the property exist from within the datasource and is
                    // not
                    // part of the primarykey , attemtp the assign the
                    // datasource's target
                    // property with the corresponding _bean property.
                    if (_dso.isColumnValid(property)) {
                        Boolean isKey = (Boolean) _dso.getDataSourceAttib()
                                .getColumnAttribute(property, "primaryKey");
                        if (isKey.booleanValue()) {
                            continue;
                        }
                        _dso.setColumnValue(property, value);
                    }
                } // end try
                catch (NotFoundException e) {
                    DataSourceConverter.logger.log(Level.DEBUG, e.getMessage()
                            + " - Property: " + property);
                    continue;
                }
            } // end for
            return 1;
        } // end try
        catch (DatabaseException e) {
            throw new SystemException(e);
        }
    }

    /**
     * Synchronizes dao's datasource with the property values of bean.
     * 
     * @param dao
     *            The target Data Access Object
     * @param bean
     *            The ORM bean object that is the source of the copy.
     * @return The synchronized dao object.
     * @throws SystemException
     */
    public static final DaoApi packageDSO(DaoApi dao, OrmBean bean)
            throws SystemException {
        String property = null;
        String viewName = null;
        String origPropName = null;
        Object value = null;
        List<String> props = null;

        viewName = RMT2Utility.getBeanClassName(bean);
        if (viewName == null || viewName.equals("")) {
            return null;
        }
        viewName += "View";

        // Setup the Bean Utility for _bean
        RMT2BeanUtility srcBeanUtil = new RMT2BeanUtility(bean);

        // Retrieve all property names associated with _bean
        props = srcBeanUtil.getPropertyNames();

        try {
            for (int ndx = 0; ndx < props.size(); ndx++) {
                try {
                    // Get and convert the property name to proper Bean naming
                    // convention
                    origPropName = props.get(ndx).toString();
                    property = RMT2Utility.getBeanMethodName(origPropName);
                    // property =
                    // RMT2Utility.getBeanMethodName(props.get(ndx).toString());

                    // Get the value that was assigned to the target property of
                    // _bean
                    if (dao.getDataSourceAttib() == null) {
                        String msg = "DAO object is not properly initialized for data source, "
                                + viewName
                                + ".  Be sure that the System properties used for locating the SAX driver libraries and the ORM data source objects & views are set correctly during server start up.";
                        Logger.getLogger("DataSourceConverter").log(
                                Level.ERROR, msg);
                        throw new SystemException(msg);
                    }
                    DataSourceColumn dsc = dao.getDataSourceAttib()
                            .getDsoColumn(property);
                    if (dsc == null) {
                        continue;
                    }
                    value = srcBeanUtil.getPropertyValue(property);
                    if (bean.isNull(origPropName)) {
                        value = DataSourceConverter.DB_NULL;
                    }
                    dsc.setDataValue(value);
                    DataSourceColumn dsc2 = dao.getDataSourceAttib()
                            .getDsoColumn(property);
                    value = dsc2.getDataValue().toString();
                } // end try
                catch (NotFoundException e) {
                    DataSourceConverter.logger.log(Level.DEBUG, e.getMessage()
                            + " - Property: " + property);
                    continue;
                }
            } // end for
            return dao;
        } // end try
        catch (Exception e) {
            throw new SystemException("Unable to package DAO using ORMBean", e);
        }
    }

    // /**
    // * Packages a RMT2DataSourceApi object from a HttpServletRequest object.
    // *
    // * @param _dso
    // * @param _request
    // * @return
    // * @throws SystemException
    // */
    // public static final int packageDSO(DataSourceApi _dso, Request _request)
    // throws SystemException {
    // String property = null;
    // String value = null;
    //
    // if (_dso == null) {
    // throw new SystemException(
    // "Datasource is not valid or does not have a valid connection object");
    // }
    //
    // // Get request's parameter names (The request parameter names
    // // should equal the column names of _dso including case).
    // Enumeration reqParms = _request.getParameterNames();
    //
    // // Retrieve all property names associated with _request by
    // // cycling through all request parameters.
    //
    // try {
    // while (reqParms.hasMoreElements()) {
    // try {
    // // Obtain the parameter name and value
    // property = reqParms.nextElement().toString();
    // // Get and convert the property name to proper Bean
    // // specification casing
    // property = RMT2Utility.getBeanMethodName(property);
    // value = _request.getParameter(property);
    //
    // // Uncomment for debugging
    // DataSourceConverter.logger.log(Level.DEBUG,
    // "packageDSO Property: " + property);
    // DataSourceConverter.logger.log(Level.DEBUG,
    // "packageDSO Value: " + value);
    //
    // // Exclude if property is part of the primary key
    // if (_dso.isColumnValid(property)) {
    // Boolean isKey = (Boolean) _dso.getDataSourceAttib()
    // .getColumnAttribute(property, "primaryKey");
    // if (isKey.booleanValue()) {
    // continue;
    // }
    // _dso.setColumnValue(property, value);
    // }
    // } // end try
    // catch (NotFoundException e) {
    // DataSourceConverter.logger.log(Level.DEBUG, e.getMessage()
    // + " - Property: " + property);
    // continue;
    // }
    // } // end while
    //
    // return 1;
    //
    // } // end try
    // catch (DatabaseException e) {
    // throw new SystemException(e);
    // }
    // }

    // /**
    // * Packages a RMT2DataSourceApi object from an indexed row of the
    // * HttpServletRequest object.
    // *
    // * @param _dso
    // * @param _request
    // * @param _row
    // * @return
    // * @throws SystemException
    // */
    // public static final int packageDSO(DataSourceApi _dso, Request _request,
    // int _row) throws SystemException {
    // String property = null;
    // String value = null;
    // String rowNdx = null;
    //
    // if (_dso == null) {
    // throw new SystemException(
    // "Datasource is not valid or does not have a valid connection object");
    // }
    //
    // // Convert row index to string so that we can concatenate it with
    // // each property name of the target row of the request object.
    // rowNdx = (_row <= 0 ? "" : String.valueOf(_row));
    //
    // // Get request's parameter names (The request parameter names
    // // should equal the column names of _dso including case).
    // Enumeration reqParms = _request.getParameterNames();
    //
    // // Retrieve all property names associated with _request by
    // // cycling through all request parameters.
    //
    // try {
    // while (reqParms.hasMoreElements()) {
    // try {
    // // Obtain the parameter name and value
    // property = reqParms.nextElement().toString();
    // // Get and convert the property name to proper Bean
    // // specification casing
    // property = RMT2Utility.getBeanMethodName(property);
    // value = _request.getParameter(property + rowNdx);
    //
    // // Uncomment for debugging
    // DataSourceConverter.logger.log(Level.DEBUG,
    // "packageDSO Property: " + property);
    // DataSourceConverter.logger.log(Level.DEBUG,
    // "packageDSO Value: " + value);
    //
    // // Exclude if property is part of the primary key
    // if (_dso.isColumnValid(property)) {
    // Boolean isKey = (Boolean) _dso.getDataSourceAttib()
    // .getColumnAttribute(property, "primaryKey");
    // if (isKey.booleanValue()) {
    // continue;
    // }
    // _dso.setColumnValue(property, value);
    // }
    // } // end try
    // catch (NotFoundException e) {
    // DataSourceConverter.logger.log(Level.DEBUG, e.getMessage()
    // + " - Property: " + property);
    // continue;
    // }
    // } // end while
    //
    // return 1;
    //
    // } // end try
    // catch (DatabaseException e) {
    // throw new SystemException(e);
    // }
    // }

    // /**
    // * Packages a RMT2DataSourceApi object from a HttpServletRequest object
    // that
    // * contains more than one row of data. The parameter names should be
    // * uniquely named by appending a unique number to all column names per
    // row.
    // * The value of the unique number will genereally be row number. Example:
    // * the column names of row 1 will be named col1 col2, col3, colx.
    // * <p>
    // * Note: This will only work on single updateable table datasources which
    // * contains a single column primary key named, Id.
    // * <p>
    // * The naming convention of the input controls of the Client JSP document
    // * should match the spelling and case of the column names that belong to
    // the
    // * corresponding Data Source View.
    // *
    // * @param _dso
    // * @param _request
    // * @param _bean
    // * @return
    // * @throws DatabaseException
    // * @throws SystemException
    // */
    // public static final int packageDSO(DataSourceApi _dso, Request _request,
    // Object _bean) throws DatabaseException, SystemException {
    // String property = null;
    // String reqProp = null;
    // String value = null;
    // List<String> props = null;
    // int reqRows = 0;
    // boolean isUpdate = false;
    // boolean isInsert = false;
    //
    // if (_dso == null) {
    // throw new SystemException(
    // "Datasource is not valid or does not have a valid connection object");
    // }
    //
    // // Setup the Bean Utility for _bean
    // if (_bean == null) {
    // throw new SystemException("Bean Name cannot be null or blank");
    // }
    //
    // // Dynamically create bean
    // // RMT2BeanUtility beanUtil = new RMT2BeanUtility();
    // // bean = beanUtil.createBean(_bean);
    // RMT2BeanUtility beanUtil = new RMT2BeanUtility(_bean);
    //
    // // Get total row count of the HttpServletRequest object
    // reqRows = getRequestRowCount(_request, beanUtil);
    // // Retrieve all property names associated with _bean
    // props = beanUtil.getPropertyNames();
    //
    // try {
    // // Traverse HttpServletRequest rows
    // for (int rowNdx = 0; rowNdx < reqRows; rowNdx++) {
    //
    // // Goto next row if no updates exist for current row
    // String rowStat = _request.getParameter("rowStatus" + rowNdx);
    // if (rowStat.equalsIgnoreCase("U")) {
    // continue;
    // }
    //
    // // Determine if _dso is to perform an insert or an update
    // // by using the primary key column, Id.
    // isInsert = false;
    // isUpdate = false;
    // String colName = null;
    //
    // // Get Column object of type DataSourceColumn
    // DataSourceColumn colObj = _dso.getDataSourceAttib()
    // .getDsoPrimaryKey();
    //
    // if (colObj == null) {
    // // Default primary key name to "id" if primary key was not
    // // found
    // colName = "id";
    // }
    // else {
    // // Set colName to primary key name retrieved from datasource
    // colName = colObj.getName();
    // }
    //
    // String keyValue = _request.getParameter(colName + rowNdx);
    // // Determine the SQL DML to execute.
    // // Perform insert if primary key value is null or blank
    // if (keyValue == null || keyValue.trim().length() == 0) {
    // // Insert row
    // _dso.setDatasourceSql(DbSqlConst.WHERE_KEY, null);
    // _dso.setDatasourceSql(DbSqlConst.WHERE_KEY, "id = -1");
    // _dso.executeQuery(true, true);
    // _dso.createRow();
    // isInsert = true;
    // }
    // // Perform update if primary key has a value
    // else {
    // // Update row
    // _dso.setDatasourceSql(DbSqlConst.WHERE_KEY, null);
    // _dso.setDatasourceSql(DbSqlConst.WHERE_KEY, "id = "
    // + keyValue);
    // _dso.executeQuery(true, true);
    //
    // // TODO: code stronger logic for row not found
    // if (!_dso.nextRow()) {
    // continue;
    // }
    // isUpdate = true;
    // }
    //
    // // Get property names and their data values by
    // // traversing all columns of the current HttpServletRequest
    // // row
    // for (int colNdx = 0; colNdx < props.size(); colNdx++) {
    //
    // // Get next column to process
    // property = (String) props.get(colNdx);
    // try {
    // // Get and convert the property name to proper Bean
    // // specification casing
    // property = RMT2Utility.getBeanMethodName(property);
    //
    // // Get unique column value from HttpServletRequest
    // // object
    // reqProp = property + rowNdx;
    // value = _request.getParameter(reqProp);
    //
    // // Skip updating property(reqProp) if it does not exist
    // // as an object of the client's HttpServeltRequest
    // // object.
    // if (value == null || value.equals("null")) {
    // continue; //
    // }
    //
    // // Uncomment for debugging
    // DataSourceConverter.logger.log(Level.DEBUG,
    // "packageDSO Property: " + property);
    // DataSourceConverter.logger.log(Level.DEBUG,
    // "packageDSO Unique Property: " + reqProp);
    // DataSourceConverter.logger.log(Level.DEBUG,
    // "packageDSO Value: " + value);
    //
    // // Exclude if property is part of the primary key
    // if (_dso.isColumnValid(property)) {
    // Boolean isKey = (Boolean) _dso.getDataSourceAttib()
    // .getColumnAttribute(property, "primaryKey");
    // if (isKey.booleanValue()) {
    // continue;
    // }
    // _dso.setColumnValue(property, value);
    // }
    // } // end try
    // catch (NotFoundException e) {
    // DataSourceConverter.logger.log(Level.DEBUG,
    // e.getMessage() + " - Property: " + property);
    // continue;
    // }
    // } // end for colNdx
    //
    // // After all columns have been processed, update database with
    // // current row
    // if (_request != null) {
    // RMT2Date.doRowTimeStamp("fake_user_id", _dso,
    // (isInsert ? true : false));
    // }
    // if (isUpdate) {
    // // TODO: Include a more meaningful message in framework
    // // that will indicate a row must be current
    // // before performing an updateRow operation.
    //
    // _dso.updateRow();
    // }
    // if (isInsert) {
    // _dso.insertRow();
    // }
    //
    // // process next row
    //
    // } // end for rowNdx
    //
    // return 1;
    //
    // } // end try
    //
    // catch (DatabaseException e) {
    // throw e;
    // } catch (NotFoundException e) {
    // throw new SystemException(e);
    // }
    // }
    //
    // /**
    // * Calculates the total number of rows the HttpServleRequest object (_req)
    // * contains.
    // *
    // * @param _request
    // * @param _beanUtil
    // * @return
    // */
    // protected static final int getRequestRowCount(Request _request,
    // RMT2BeanUtility _beanUtil) {
    //
    // String prop = null;
    // String value = null;
    // boolean isPropFound = false;
    // List<String> props = _beanUtil.getPropertyNames();
    //
    // // Since this method relies on a valid HttpServletRequest
    // // (this.request), return an error code of -1 if this.request
    // // is not valid.
    // if (_request == null) {
    // return -1;
    // }
    //
    // // Locate a parameter in the request object that can be used
    // // to determine the request object's row count
    // for (int ndx = 0; ndx < props.size(); ndx++) {
    // prop = (String) props.get(ndx);
    // prop = RMT2Utility.getBeanMethodName(prop);
    // if (_request.getParameter(prop + "0") != null) {
    // isPropFound = true;
    // break;
    // }
    // }
    //
    // // If bean property could not be match with a
    // // parameter of the HttpServletRequest object
    // // then return 0 as the total count.
    // if (!isPropFound) {
    // return 0;
    // }
    //
    // // Calculate how many times "prop" uniquely occurs within
    // // the HttpServletRequest object which render a total
    // // row count.
    // int ndx = 0;
    // value = _request.getParameter(prop + ndx);
    // while (value != null) {
    // ndx++;
    // value = _request.getParameter(prop + ndx);
    // }
    //
    // return ndx;
    // }

    /**
     * Converts the properties and values of one OrmBean object into a XML
     * document. The OrmBean instance is wrapped in a RMT2BeanUtility which is
     * used to discover the properties and methods of the bean. The bean is a
     * direct descendent of OrmBean.
     * 
     * @param dbo
     *            A valid {@link DatabaseConnectionBean} object. Cannot be null.
     * @param bean
     *            The OrmBean that is to be marshalled.
     * @return A String XML document.
     * @throws SystemException
     *             When bean is null or invalid.
     */
    public static final String marshallOrmBean(Object dbo, OrmBean bean)
            throws SystemException {
        if (bean == null) {
            throw new SystemException(
                    "Cannot marshall individual OrmBean.  Argument, bean, is invalid.");
        }
        List beans = new ArrayList();
        beans.add(bean);
        return DataSourceConverter.marshallOrmBean(dbo, beans);
    }

    /**
     * Converts the properties and values of a List of OrmBean objects into a
     * XML an document. Each OrmBean instance is wrapped in a RMT2BeanUtility
     * which is used to discover the properties and methods of the bean. The
     * bean is a direct descendent of OrmBean.
     * 
     * @param dbo
     *            A valid {@link DatabaseConnectionBean} object. Cannot be null.
     * @param beans
     *            A List of OrmBeans that are to be marshalled.
     * @return A String XML document.
     * @throws SystemException
     *             When the List containing the OrmBeans is invalid or when the
     *             DataSource view name is unobtainable.
     */
    public static final String marshallOrmBean(Object dbo, List beans)
            throws SystemException {
        if (beans == null || beans.size() <= 0) {
            throw new SystemException(
                    "There are no OrmBean objects to marshall.");
        }

        // Get data source view name using the first element in bean list.
        OrmBean bean = (OrmBean) beans.get(0);
        String viewName = bean.getDataSourceName();
        if (viewName == null) {
            throw new SystemException(
                    "ORM Bean cannot be marshalled.  DataSource view name could not be determined");
        }
        // Setup root element
        Element root = new Element(viewName);

        // Build an XML element for each bean
        Iterator iter = beans.iterator();
        while (iter.hasNext()) {
            bean = (OrmBean) iter.next();
            Element beanXml = DataSourceConverter.convertOrmToXml(dbo, bean);
            root.addContent(beanXml);
        }

        // Output document
        Document doc = new Document(root);
        XMLOutputter outputter = new XMLOutputter(Format.getCompactFormat()
                .setOmitDeclaration(true));
        String xml = outputter.outputString(doc);
        return xml;
    }

    /**
     * Low level logic that the OrmBean to a XML document using a
     * DatabaseConnectionBean and the OrmBean.
     * <p>
     * The java bean instance is considered to be the crux of this operation.
     * <b><u>Processing Sequence</u></b><br>
     * <ol>
     * <li>Verify that dbo is a valid DatabaseConnectionBean object and that
     * bean is capable of rendering a valid DataSource view name. If both do not
     * meet the requirements, throw an exception.</li>
     * <li>Create a DaoApi object with dbo and the DataSource view name.</li>
     * <li>Create RMT2BeanUtility with bean.</li>
     * <li>Obtain an ObjectMapperAttrib object from the DaoApi created above.</li>
     * <li>Invoike routine that converts the java bean to a XML document.</li>
     * <li>Return String XML document to caller.</li>
     * </ol>
     * 
     * @param dbo
     *            A valid {@link DatabaseConnectionBean} object. Cannot be null.
     * @param bean
     *            An {@link OrmBean} instance whose data will be marshalled.
     * @return String XML document.
     * @throws SystemException
     *             <ul>
     *             <li>When bean is null.</li> <li>When bean's DataSource name
     *             cannot be determined.</li> <li>When DaoApi object cannot be
     *             instantiated using dbo and the DataSource view name.</li> 
     *             <li>When a RMT2BeanUtility cannot be created using bean. 
     *             </li>
     *             </ul>
     */
    private static final Element convertOrmToXml(Object dbo, OrmBean bean)
            throws SystemException {
        if (dbo == null) {
            throw new SystemException(
                    "ORM Bean could not be marshalled.  Datasource object is invalid or null");
        }
        if (bean == null) {
            throw new SystemException(
                    "ORM Bean could not be marshalled.  Bean is not valid");
        }

        // Get data source view name.
        String dsn = bean.getDataSourceName();
        if (dsn == null) {
            throw new SystemException(
                    "ORM Bean could not be marshalled.  DataSource view name could not be determined");
        }
        // Get DAO object.
        DaoApi dao = DataSourceFactory.createDao(dbo, dsn);
        if (dao == null) {
            throw new SystemException(
                    "ORM Bean could not be marshalled.  Data Access Object could not be created");
        }
        // Setup bean utility so that we may discover the
        // properties and methods of the parameter, bean.
        RMT2BeanUtility beanUtil = new RMT2BeanUtility(bean);
        // Get DataSource attribute object.
        ObjectMapperAttrib attr = dao.getDataSourceAttib();

        Element tableElement = DataSourceConverter.convertOrmToXml(beanUtil,
                attr);
        return tableElement;
    }

    /**
     * Uses the attributes of an OrmBean's DataSource to create a XML Document
     * element object.
     * 
     * @param util
     *            Bean Utility used to discover the properties and values of the
     *            OrmBean.
     * @param attr
     *            The DataSource view attributes.
     * @return XML data as a String.
     */
    private static Element convertOrmToXml(RMT2BeanUtility util,
            ObjectMapperAttrib attr) {
        Hashtable tables = attr.getTables();
        Enumeration iter = tables.elements();
        String tableName = null;
        if (iter.hasMoreElements()) {
            TableUsageBean tableBean = (TableUsageBean) iter.nextElement();
            tableName = tableBean.getDbName();
        }
        else {
            tableName = "dataitem";
        }

        // Create root XML element.
        Element tableRoot = new Element(tableName);

        // Build XML document by cycling through properties
        // and obtaining their respective text values.
        iter = attr.getColumnDef().keys();
        while (iter.hasMoreElements()) {
            String prop = (String) iter.nextElement();
            String dbProp = null;
            Object value = null;
            try {
                DataSourceColumn dsc = (DataSourceColumn) attr.getColumnDef()
                        .get(prop);
                dbProp = dsc.getDbName();
                value = util.getPropertyValue(prop);
            } catch (Exception e) {
                continue;
            }
            Element element = new Element(dbProp);
            element.setText(value.toString());
            tableRoot.addContent(element);
        }
        return tableRoot;
    }

    /**
     * Converts a XML document into a List of OrmBean objects. The OrmBean
     * instance is dynamically created based on the name of the root tag element
     * of the XML document. Certain data values within the XML document are used
     * to determine the actual data type and location of the java bean that is
     * to be instantiated.
     * <p>
     * The XML document is considered to be the crux of this operation and must
     * satisfy the following requirements: <blockquote>
     * <ul>
     * <li>Must contain a root element.</li>
     * <li>The root element's name must indicate the name of the database table
     * that is to be mapped.</li>
     * <li>The document must represent one and only one table (one to one
     * relationship).</li>
     * </ul>
     * </blockquote>
     * 
     * <b><u>Processing Sequence</u></b><br>
     * <ol>
     * <li>Obtain the root element of the XML document. If root does not exist,
     * return null to caller.</li>
     * <li>With the root element name, build OrmBean class package and
     * instantiate class.</li>
     * <li>Call routine that maps data to OrmBean.</li>
     * <li>Return List of OrmBean objects to caller.</li>
     * </ol>
     * 
     * @param xmlDoc
     *            A raw XML document containing the data that is to be mapped to
     *            the OrmBean.
     * @return List of {@link OrmBean} objects or null if XML document does not
     *         contain a root element.
     * @throws NotFoundException
     *             The proposed OrmBean is not found in the class path.
     * @throws SystemException
     *             When xmlDoc parameter is null or empty, error determining
     *             OrmBean class, or OrmBean class is not found in classpath.
     */
    public static final List unMarshallOrmBean(String xmlDoc)
            throws SystemException {
        if (xmlDoc == null) {
            throw new SystemException(
                    "ORM Bean cannot be unmarshalled.  XML document is not valid");
        }
        if (xmlDoc.length() <= 0) {
            throw new SystemException(
                    "ORM Bean cannot be unmarshalled.  XML document is empty");
        }

        ByteArrayInputStream bais = new ByteArrayInputStream(xmlDoc.getBytes());
        Element root;
        List childElements;
        try {
            SAXBuilder builder = new SAXBuilder();
            Document doc;
            doc = builder.build(bais);
            if (doc.hasRootElement()) {
                root = doc.getRootElement();
                // Get all child elements where each element corresponds to a
                // table row.
                childElements = root.getChildren();
            }
            else {
                return null;
            }

            // Build one or more beans from each child element
            List beanList = new ArrayList();
            for (int ndx = 0; ndx < childElements.size(); ndx++) {
                // Get element to process
                Element element = (Element) childElements.get(ndx);
                // Use the root element to instantiate the next
                // OrmBean to be unmarshalled.
                OrmBean bean = DataSourceConverter.getOrmBeanClass(element);
                // Wrap bean within bean utility.
                RMT2BeanUtility beanUtil = new RMT2BeanUtility(bean);
                // Convert element to bean
                DataSourceConverter.convertXmlToOrm(beanUtil, element);
                beanList.add(bean);
            }
            return beanList;
        } catch (JDOMException e) {
            throw new SystemException(e.getMessage());
        } catch (IOException e) {
            throw new SystemException(e.getMessage());
        }
    }

    /**
     * Cycles through the elements of an XML document and assigns the element
     * values to their respective OrmBean properties.
     * 
     * @param bean
     *            {@link RMT2BeanUtility} used to discover the properties and
     *            methods of the OrmBean object.
     * @param element
     *            The XMl element to bind to a particular OrmBean property.
     * @throws SystemException
     *             Problem assigning the value to OrmBean property.
     */
    private static void convertXmlToOrm(RMT2BeanUtility bean, Element element)
            throws SystemException {
        // Get value of element and add to bean
        String property = element.getName();
        String value = element.getTextTrim();
        if (property != null) {
            try {
                // Check for the existence of underscores and remove.
                property = RMT2Utility.formatDsName(property);
                bean.setPropertyValue(property, value);
            } catch (NotFoundException e) {
                // It is okay for a property not to found in this situation.
                Logger.getLogger("DataSourceConverter").log(Level.DEBUG,
                        e.getMessage());
            }
        }

        // Use recursion to process children of the current element, if
        // available
        Iterator children = element.getChildren().iterator();
        while (children.hasNext()) {
            Element nextElement = (Element) children.next();
            DataSourceConverter.convertXmlToOrm(bean, nextElement);
        }
        return;
    }

    /**
     * Uses the name of the root element of an XML document to determine which
     * class is to be instantiated as an OrmBean descendent during the
     * marshalling process.
     * 
     * @param root
     *            XML Element.
     * @return A descendent of OrmBean.
     * @throws SystemException
     *             If the class name is improperly computed or class cannot be
     *             instantiated.
     */
    private static OrmBean getOrmBeanClass(Element root) throws SystemException {
        OrmBean bean;
        String className;
        String tableName = root.getName();

        // Check for the existence of underscores and remove.
        tableName = RMT2Utility.formatDsName(tableName);
        String packagePrefix = AppPropertyPool
                .getProperty(OrmBean.PACKAGE_PREFIX);
        className = packagePrefix + tableName;

        // With the class name, instantiate OrmBean object
        bean = (OrmBean) RMT2Utility.getClassInstance(className);
        return bean;
    }

}