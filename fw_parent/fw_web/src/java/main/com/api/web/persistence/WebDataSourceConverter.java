package com.api.web.persistence;

import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.NotFoundException;
import com.SystemException;
import com.api.DataSourceApi;
import com.api.persistence.DatabaseException;
import com.api.persistence.db.DbSqlConst;
import com.api.persistence.db.orm.DataSourceConverter;
import com.api.persistence.db.orm.bean.DataSourceColumn;
import com.api.web.Request;
import com.api.web.util.RMT2WebUtility;
import com.util.RMT2BeanUtility;
import com.util.RMT2Date;
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
public abstract class WebDataSourceConverter extends DataSourceConverter {
    private static Logger logger = Logger
            .getLogger(WebDataSourceConverter.class);

    private static final String DB_NULL = "null";

    private static final int DSO_TO_BEAN_THREAD_ELIGIBLITY_COUNT = 30;

    /**
     * Packages a JavaBean object from a HttpServletRequest.
     * 
     * @param request
     *            A HttpServletRequest object containing the data to be
     *            converted.
     * @param bean
     *            The destination bean.
     * @return Total number of columns converted.
     * @throws SystemException
     */
    public static final int packageBean(Request request, Object bean)
            throws SystemException {

        Properties prop = RMT2WebUtility.getRequestData(request);
        return WebDataSourceConverter.packageBean(prop, bean);
    }

    /**
     * Packages a JavaBean object from an indexed row of the HttpServletRequest
     * object.
     * 
     * @param request
     *            A HttpServletRequest object containing the data to be
     *            converted.
     * @param bean
     *            The destination bean.
     * @param row
     *            The row index to identify the property by.
     * @return Total number of columns converted.
     * @throws SystemException
     */
    public static final int packageBean(Request request, Object bean, int row)
            throws SystemException {
        Properties prop = RMT2WebUtility.getRequestData(request);
        return WebDataSourceConverter.packageBean(prop, bean, row);
    }

    /**
     * Packages a RMT2DataSourceApi object from a HttpServletRequest object.
     * 
     * @param _dso
     * @param _request
     * @return
     * @throws SystemException
     */
    public static final int packageDSO(DataSourceApi _dso, Request _request)
            throws SystemException {
        String property = null;
        String value = null;

        if (_dso == null) {
            throw new SystemException(
                    "Datasource is not valid or does not have a valid connection object");
        }

        // Get request's parameter names (The request parameter names
        // should equal the column names of _dso including case).
        Enumeration reqParms = _request.getParameterNames();

        // Retrieve all property names associated with _request by
        // cycling through all request parameters.

        try {
            while (reqParms.hasMoreElements()) {
                try {
                    // Obtain the parameter name and value
                    property = reqParms.nextElement().toString();
                    // Get and convert the property name to proper Bean
                    // specification casing
                    property = RMT2Utility.getBeanMethodName(property);
                    value = _request.getParameter(property);

                    // Uncomment for debugging
                    WebDataSourceConverter.logger.log(Level.DEBUG,
                            "packageDSO Property: " + property);
                    WebDataSourceConverter.logger.log(Level.DEBUG,
                            "packageDSO Value: " + value);

                    // Exclude if property is part of the primary key
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
                    WebDataSourceConverter.logger.log(Level.DEBUG,
                            e.getMessage() + " - Property: " + property);
                    continue;
                }
            } // end while

            return 1;

        } // end try
        catch (DatabaseException e) {
            throw new SystemException(e);
        }
    }

    /**
     * Packages a RMT2DataSourceApi object from an indexed row of the
     * HttpServletRequest object.
     * 
     * @param _dso
     * @param _request
     * @param _row
     * @return
     * @throws SystemException
     */
    public static final int packageDSO(DataSourceApi _dso, Request _request,
            int _row) throws SystemException {
        String property = null;
        String value = null;
        String rowNdx = null;

        if (_dso == null) {
            throw new SystemException(
                    "Datasource is not valid or does not have a valid connection object");
        }

        // Convert row index to string so that we can concatenate it with
        // each property name of the target row of the request object.
        rowNdx = (_row <= 0 ? "" : String.valueOf(_row));

        // Get request's parameter names (The request parameter names
        // should equal the column names of _dso including case).
        Enumeration reqParms = _request.getParameterNames();

        // Retrieve all property names associated with _request by
        // cycling through all request parameters.

        try {
            while (reqParms.hasMoreElements()) {
                try {
                    // Obtain the parameter name and value
                    property = reqParms.nextElement().toString();
                    // Get and convert the property name to proper Bean
                    // specification casing
                    property = RMT2Utility.getBeanMethodName(property);
                    value = _request.getParameter(property + rowNdx);

                    // Uncomment for debugging
                    WebDataSourceConverter.logger.log(Level.DEBUG,
                            "packageDSO Property: " + property);
                    WebDataSourceConverter.logger.log(Level.DEBUG,
                            "packageDSO Value: " + value);

                    // Exclude if property is part of the primary key
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
                    WebDataSourceConverter.logger.log(Level.DEBUG,
                            e.getMessage() + " - Property: " + property);
                    continue;
                }
            } // end while

            return 1;

        } // end try
        catch (DatabaseException e) {
            throw new SystemException(e);
        }
    }

    /**
     * Packages a RMT2DataSourceApi object from a HttpServletRequest object that
     * contains more than one row of data. The parameter names should be
     * uniquely named by appending a unique number to all column names per row.
     * The value of the unique number will genereally be row number. Example:
     * the column names of row 1 will be named col1 col2, col3, colx.
     * <p>
     * Note: This will only work on single updateable table datasources which
     * contains a single column primary key named, Id.
     * <p>
     * The naming convention of the input controls of the Client JSP document
     * should match the spelling and case of the column names that belong to the
     * corresponding Data Source View.
     * 
     * @param _dso
     * @param _request
     * @param _bean
     * @return
     * @throws DatabaseException
     * @throws SystemException
     */
    public static final int packageDSO(DataSourceApi _dso, Request _request,
            Object _bean) throws DatabaseException, SystemException {
        String property = null;
        String reqProp = null;
        String value = null;
        List<String> props = null;
        int reqRows = 0;
        boolean isUpdate = false;
        boolean isInsert = false;

        if (_dso == null) {
            throw new SystemException(
                    "Datasource is not valid or does not have a valid connection object");
        }

        // Setup the Bean Utility for _bean
        if (_bean == null) {
            throw new SystemException("Bean Name cannot be null or blank");
        }

        // Dynamically create bean
        // RMT2BeanUtility beanUtil = new RMT2BeanUtility();
        // bean = beanUtil.createBean(_bean);
        RMT2BeanUtility beanUtil = new RMT2BeanUtility(_bean);

        // Get total row count of the HttpServletRequest object
        reqRows = getRequestRowCount(_request, beanUtil);
        // Retrieve all property names associated with _bean
        props = beanUtil.getPropertyNames();

        try {
            // Traverse HttpServletRequest rows
            for (int rowNdx = 0; rowNdx < reqRows; rowNdx++) {

                // Goto next row if no updates exist for current row
                String rowStat = _request.getParameter("rowStatus" + rowNdx);
                if (rowStat.equalsIgnoreCase("U")) {
                    continue;
                }

                // Determine if _dso is to perform an insert or an update
                // by using the primary key column, Id.
                isInsert = false;
                isUpdate = false;
                String colName = null;

                // Get Column object of type DataSourceColumn
                DataSourceColumn colObj = _dso.getDataSourceAttib()
                        .getDsoPrimaryKey();

                if (colObj == null) {
                    // Default primary key name to "id" if primary key was not
                    // found
                    colName = "id";
                }
                else {
                    // Set colName to primary key name retrieved from datasource
                    colName = colObj.getName();
                }

                String keyValue = _request.getParameter(colName + rowNdx);
                // Determine the SQL DML to execute.
                // Perform insert if primary key value is null or blank
                if (keyValue == null || keyValue.trim().length() == 0) {
                    // Insert row
                    _dso.setDatasourceSql(DbSqlConst.WHERE_KEY, null);
                    _dso.setDatasourceSql(DbSqlConst.WHERE_KEY, "id = -1");
                    _dso.executeQuery(true, true);
                    _dso.createRow();
                    isInsert = true;
                }
                // Perform update if primary key has a value
                else {
                    // Update row
                    _dso.setDatasourceSql(DbSqlConst.WHERE_KEY, null);
                    _dso.setDatasourceSql(DbSqlConst.WHERE_KEY, "id = "
                            + keyValue);
                    _dso.executeQuery(true, true);

                    // TODO: code stronger logic for row not found
                    if (!_dso.nextRow()) {
                        continue;
                    }
                    isUpdate = true;
                }

                // Get property names and their data values by
                // traversing all columns of the current HttpServletRequest
                // row
                for (int colNdx = 0; colNdx < props.size(); colNdx++) {

                    // Get next column to process
                    property = (String) props.get(colNdx);
                    try {
                        // Get and convert the property name to proper Bean
                        // specification casing
                        property = RMT2Utility.getBeanMethodName(property);

                        // Get unique column value from HttpServletRequest
                        // object
                        reqProp = property + rowNdx;
                        value = _request.getParameter(reqProp);

                        // Skip updating property(reqProp) if it does not exist
                        // as an object of the client's HttpServeltRequest
                        // object.
                        if (value == null || value.equals("null")) {
                            continue; //
                        }

                        // Uncomment for debugging
                        WebDataSourceConverter.logger.log(Level.DEBUG,
                                "packageDSO Property: " + property);
                        WebDataSourceConverter.logger.log(Level.DEBUG,
                                "packageDSO Unique Property: " + reqProp);
                        WebDataSourceConverter.logger.log(Level.DEBUG,
                                "packageDSO Value: " + value);

                        // Exclude if property is part of the primary key
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
                        WebDataSourceConverter.logger.log(Level.DEBUG,
                                e.getMessage() + " - Property: " + property);
                        continue;
                    }
                } // end for colNdx

                // After all columns have been processed, update database with
                // current row
                if (_request != null) {
                    RMT2Date.doRowTimeStamp("fake_user_id", _dso,
                            (isInsert ? true : false));
                }
                if (isUpdate) {
                    // TODO: Include a more meaningful message in framework
                    // that will indicate a row must be current
                    // before performing an updateRow operation.

                    _dso.updateRow();
                }
                if (isInsert) {
                    _dso.insertRow();
                }

                // process next row

            } // end for rowNdx

            return 1;

        } // end try

        catch (DatabaseException e) {
            throw e;
        } catch (NotFoundException e) {
            throw new SystemException(e);
        }
    }

    /**
     * Calculates the total number of rows the HttpServleRequest object (_req)
     * contains.
     * 
     * @param _request
     * @param _beanUtil
     * @return
     */
    protected static final int getRequestRowCount(Request _request,
            RMT2BeanUtility _beanUtil) {

        String prop = null;
        String value = null;
        boolean isPropFound = false;
        List<String> props = _beanUtil.getPropertyNames();

        // Since this method relies on a valid HttpServletRequest
        // (this.request), return an error code of -1 if this.request
        // is not valid.
        if (_request == null) {
            return -1;
        }

        // Locate a parameter in the request object that can be used
        // to determine the request object's row count
        for (int ndx = 0; ndx < props.size(); ndx++) {
            prop = (String) props.get(ndx);
            prop = RMT2Utility.getBeanMethodName(prop);
            if (_request.getParameter(prop + "0") != null) {
                isPropFound = true;
                break;
            }
        }

        // If bean property could not be match with a
        // parameter of the HttpServletRequest object
        // then return 0 as the total count.
        if (!isPropFound) {
            return 0;
        }

        // Calculate how many times "prop" uniquely occurs within
        // the HttpServletRequest object which render a total
        // row count.
        int ndx = 0;
        value = _request.getParameter(prop + ndx);
        while (value != null) {
            ndx++;
            value = _request.getParameter(prop + ndx);
        }

        return ndx;
    }

}