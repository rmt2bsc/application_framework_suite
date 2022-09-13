package com.api.jsp.action;

import java.sql.Types;
import java.util.Enumeration;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.NotFoundException;
import com.RMT2Base;
import com.SystemException;
import com.api.DataSourceApi;
import com.api.constants.GeneralConst;
import com.api.constants.RMT2ServletConst;
import com.api.constants.RMT2SystemExceptionConst;
import com.api.persistence.DatabaseException;
import com.api.persistence.db.DatabaseConnectionBean;
import com.api.persistence.db.DbSqlConst;
import com.api.persistence.db.orm.DataSourceFactory;
import com.api.pool.DatabaseConnectionPool;
import com.api.security.RMT2TagQueryBean;
import com.api.security.authentication.web.AuthenticationConst;
import com.api.util.RMT2String;
import com.api.util.RMT2Utility;
import com.api.web.ActionCommandException;
import com.api.web.Context;
import com.api.web.Request;
import com.api.web.Response;
import com.api.web.Session;
import com.api.web.security.RMT2SessionBean;

/**
 * Abstract base class for handling client actions triggered from the controller
 * servlet. This class extends RMT2Base and implements the ICommonAction
 * interface.
 * 
 * @author rterrell
 * 
 */
public abstract class AbstractActionHandler extends RMT2Base implements
        ICommonAction {
    /** String column type code */
    protected static final int COLTYPE_STR = 1;

    /** Integer column type code */
    protected static final int COLTYPE_INT = 2;

    /** Decimal column type code */
    protected static final int COLTYPE_DEC = 3;

    /** Date column type code */
    protected static final int COLTYPE_DATE = 4;

    /** Constant that represents when an index code does not exist. */
    protected static final int INDEX_CODE_NONE = -1;

    /** Constant that represents when an index code is invalid. */
    protected static final int INDEX_CODE_INVALID = -2;

    /** required prefix value for all selection criteria UI input fields */
    protected static final String QRY_PREFIX = "qry_";

    /**
     * required prefix value for all selection criteria UI input fields that
     * represent relational operators
     */
    protected static final String QRY_PREFIX_RELOP = "qryRelOp_";

    /**
     * required suffix value for all selection criteria UI input fields that use
     * the advance search options widget.
     */
    public static final String FIELD_NAME_SUFFIX = "_ADVSRCHOPTS";

    /** Row/column modified status */
    protected static final String ROW_STAT_MODIFY = "M";

    /** Row/column unchanged status */
    protected static final String ROW_STAT_UNCHANGE = "U";

    /** Row/column deleted status */
    protected static final String ROW_STAT_DELETE = "D";

    /** Item selector control name */
    protected final static String CLIENT_ITEM_SELECTOR = "selCbx";

    /** The id of the rowId HTML hidden field */
    protected final static String CLIENT_ITEM_ROWID = "rowId";

    public static String ADV_SRCH_BEGIN = "begin";

    public static String ADV_SRCH_END = "end";

    public static String ADV_SRCH_CONTATIN = "contain";

    public static String ADV_SRCH_EXACT = "exact";

    /** Tracks the page title of response JSP page */
    protected String pageTitle;

    /** The action hander's command */
    protected String command;

    /** The action handler's servlet context object */
    protected Context context;

    /** The user's request object */
    protected Request request;

    /** The user's response object */
    protected Response response;

    /** The user's session */
    protected Session session;

    /** The data source api */
    protected DataSourceApi dso;

    /** The action triggered by the client */
    protected String clientAction;

    /** The name of the data source view */
    protected String baseView;

    /** The name of the data source class. */
    protected String baseClass;

    /** The query object associated with the session object */
    protected RMT2TagQueryBean query;

    /**
     * Contains the UI row id of the row that is currently selected by the user.
     */
    protected int selectedRow;

    protected String loginId;

    private boolean firstTime;

    private boolean error;

    private Logger logger;

    // protected Map<String, String> advSrchFields;

    /**
     * Default constructor.
     */
    public AbstractActionHandler() {
        super();
        this.setError(false);
    }

    /**
     * Constructor responsible for initializing this object's servlet context
     * and Http Request objects
     * 
     * @param _context
     *            The servlet context passed by the servlet
     * @param _request
     *            The Http Servle Request object passed by the servlet
     * @throws SystemException
     */
    public AbstractActionHandler(Context _context, Request _request)
            throws SystemException {
        this();
        this.init(_context, _request);
    }

    /**
     * Initializes this object using _conext and _request. This is needed in the
     * event this object is instantiated using the default constructor which the
     * invocation of this method should be from within the descendant.
     * 
     * @param _context
     *            the servet context
     * @param _request
     *            the http servlet request
     * @throws SystemException
     */
    protected void init(Context _context, Request _request) throws SystemException {
        this.context = _context;
        this.request = _request;

        logger = Logger.getLogger("AbstractActionHandler");
        if (this.request == null) {
            return;
        }

        // Get selected row, if available.
        try {
            this.selectedRow = this.getSelectedRow(GeneralConst.CLIENTROW_PROPERTY);
        } catch (SystemException e) {
            this.selectedRow = -1; // no row selected.
        }

        // Get servlet context in the event it is not initialized.
        if (this.context == null) {
            this.context = this.request.getSession().getContext();
        }

        this.clientAction = this.request.getParameter("clientAction");
        this.initSession();

        // Get session query object
        if (this.session != null) {
            this.query = (RMT2TagQueryBean) this.session.getAttribute(RMT2ServletConst.QUERY_BEAN);
        }
        logger = Logger.getLogger(AbstractActionHandler.class);
    }

    /**
     * Assigns the request, context, and command member variables the values set
     * by the descendant implementation. Also, invokes
     * {@link bean.RMT2Base#init() init()} method for additional object
     * initialization.
     * 
     * @param request
     *            The user's request
     * @param response
     *            The user's response
     * @param command
     *            The user's command
     * @throws ActionHandlerException
     *             When any of the init methods fail.
     */
    public void processRequest(Request request, Response response, String command) throws ActionCommandException {
        if (this.request == null) {
            this.request = request;
        }
        this.response = response;
        this.command = command;
        try {
            this.init();
            this.init(null, request);
        } catch (SystemException e) {
            throw new ActionCommandException(e);
        }
    }

    /**
     * Attempts to the identify the current user of the request. The login id s
     * obtain from one of three sources: SessionBean and from the user request
     * as either {@link AuthenticationConst.AUTH_PROP_USERID} or
     * {@link RMT2ServletConst.LOGIN_ACTION)}.
     * 
     * @return User's login id.
     * @throws SystemException
     *             When login is unobtainable.
     */
    private String getLoginId() throws SystemException {
        RMT2SessionBean sessionBean = (RMT2SessionBean) this.request
                .getSession().getAttribute(RMT2ServletConst.SESSION_BEAN);
        String results;
        if (sessionBean == null) {
            results = this.request
                    .getParameter(AuthenticationConst.AUTH_PROP_USERID);
            if (results == null) {
                results = (String) this.request
                        .getAttribute(AuthenticationConst.AUTH_PROP_USERID);
            }
            if (results == null) {
                results = this.request
                        .getParameter(RMT2ServletConst.LOGIN_ACTION);
            }
        }
        else {
            results = sessionBean.getLoginId();
        }

        if (results == null) {
            logger.log(Level.WARN,
                    RMT2SystemExceptionConst.MSG_LOGIN_USERID_NOTEXIST
                            + ".  Error Code: "
                            + RMT2SystemExceptionConst.RC_LOGIN_USERID_NOTEXIST);
            throw new SystemException(
                    RMT2SystemExceptionConst.MSG_LOGIN_USERID_NOTEXIST,
                    RMT2SystemExceptionConst.RC_LOGIN_USERID_NOTEXIST);
        }
        return results;
    }

    /**
     * Initializes a session and a database connection without the assoiciation
     * of a valid user. This is ideal for a special cases when the user is not
     * required to be logged onto the system, but needs access to system
     * resources via the Session or database.
     * 
     * @param userTagId
     *            The id of a user. Serves to simply identify a user without
     *            validation and cannot be null or blank. In order to satisfy
     *            the development requirements of a RMT2 action handler, this
     *            parameter will serve as the user's login id for the life of
     *            this action handler.
     * @throws SystemException
     *             if userTagId is null or empty.
     */
    protected void initSession(String userTagId) throws SystemException {
        if (userTagId == null || userTagId.length() <= 0) {
            throw new SystemException(
                    "A non-user session could not be initialized. User Tag Id cannot be null or empty.");
        }
        this.loginId = userTagId;
        this.initSession();
    }

    /**
     * Retrieves the user's session object. Throws an exception if the session
     * object is null, meaning that the user has not logged in or the user's
     * session has timed out.
     * 
     * @throws SystemException
     */
    private void initSession() throws SystemException {
        this.session = this.request.getSession(false);
        if (session == null) {
            throw new SystemException(
                    RMT2SystemExceptionConst.MSG_USER_NOT_LOGGED_IN);
        }
    }

    /**
     * Invokes buildColumnCriteria(StringBuffer _sql, String _colName, int
     * _colType, String _value, boolean _exactDate) and defaults date value
     * comparison (if applicable) to 24 hour period.
     * 
     * @param _sql
     *            existing SQL selection criteria.
     * @param _colName
     *            Name of target column.
     * @param _colType
     *            Column type value.
     * @param _value
     *            The value that is to be associated with _colName.
     * @return String - Modified selection criteria.
     * @throws SystemException
     */
    protected String buildColumnCriteria(StringBuffer _sql, String _colName,
            int _colType, String _value) throws SystemException {
        return this
                .buildColumnCriteria(_sql, _colName, _colType, _value, false);
    }

    /**
     * Builds the selection criteria for a particular column, "_colName", and
     * appends the column criteria to "_sql".
     * 
     * @param _sql
     *            existing SQL selection criteria.
     * @param _colName
     *            Name of target database column.
     * @param _colType
     *            Column type value.
     * @param _value
     *            The value that is to be associated with _colName.
     * @param _exactDate
     *            Indicates if date should be compared exactly or be compared to
     *            exist between a 24 hour period.
     * @return String - Modified selection criteria.
     * @throws SystemException
     */
    protected String buildColumnCriteria(StringBuffer _sql, String _colName,
            int _colType, String _value, boolean _exactDate)
            throws SystemException {
        StringBuffer colCriteria = new StringBuffer(100);

        // Instantiate _sql if invalid
        if (_sql == null) {
            _sql = new StringBuffer(100);
        }

        // Return current String value of _sql if column name and/or column
        // value is not supplied
        if ((_colName != null && _colName.length() > 0)
                && (_value != null && _value.length() > 0)) {
            // Continue
        }
        else {
            return _sql.toString();
        }

        // Determine the type of column we are trying to build selection
        // criteria for.
        switch (_colType) {
            case AbstractActionHandler.COLTYPE_STR:
                if (_value != null && _value.length() > 0) {
                    colCriteria.append(_colName);
                    colCriteria.append(" like \'");
                    colCriteria.append(_value);
                    colCriteria.append("%\'");
                }
                break;

            case AbstractActionHandler.COLTYPE_INT:
            case AbstractActionHandler.COLTYPE_DEC:
                if (_value != null && _value.length() > 0) {
                    colCriteria.append(_colName);
                    colCriteria.append(" = ");
                    colCriteria.append(_value);
                }
                break;

            case AbstractActionHandler.COLTYPE_DATE:
                if (_exactDate) {
                    colCriteria.append(_colName);
                    colCriteria.append(" = \'");
                    colCriteria.append(_value);
                    colCriteria.append("\'");
                }
                else {
                    colCriteria.append(_colName);
                    colCriteria.append(" >= \'");
                    colCriteria.append(_value);
                    colCriteria.append("\' and ");
                    colCriteria.append(_colName);
                    colCriteria.append(" <= \'");
                    colCriteria.append(_value);
                    colCriteria.append(" 23:59:59\'");
                }
        } // end switch

        // "And" current SQL criteria and column criteria if _sql contains
        // exisiting criteria
        if (colCriteria.length() > 0) {
            if (_sql.length() > 0) {
                _sql.append(" and ");
            }
            _sql.append(colCriteria.toString());
        }

        return _sql.toString();
    }

    /**
     * Retrieves the client data from a HTML or JSP form via the
     * HttpServletRequest object. A valid base view is required.
     * 
     * @return int - 1 for success and -1 for failure.
     * @throws ActionHandlerException
     */
    protected int getSimpleFormData() throws ActionCommandException {
        // Get Data from Client.
        // DatabaseTransApi tx = DatabaseTransFactory.create();
        // DataSourceApi dso = DataSourceFactory.create((DatabaseConnectionBean)
        // tx.getConnector(), this.baseView);
        // try {
        // return this.getSimpleFormData(dso);
        // }
        // catch (Exception e) {
        // throw new ActionHandlerException(e);
        // }
        // finally {
        // dso.close();
        // tx.close();
        // dso = null;
        // tx = null;
        // }
        return 0;
    }

    /**
     * Retrieves the data from an HTML or JSP form and stores each data item
     * along with its associated property in dso. The input control names from
     * the HTML or JSP form should match the exact case and spelling of the
     * property names of the datasource view. User is responsible for passing a
     * valid dso object, and this method will not be repsonsible for its
     * creation.
     * 
     * @param dso
     *            DataSource API
     * @return int - The total number of properties recognized and stored in
     *         dso.
     * @throws ActionHandlerException
     */
    protected int getSimpleFormData(DataSourceApi _dso)
            throws ActionCommandException {
        String method = "getSimpleFormData(RMT2DataSourceApi dso)";
        int processCount = 0;

        if (_dso == null) {
            msg = "DataSource object is invalid.   Cannot obtain client data from HTML or JSP page.";
            logger.log(Level.ERROR, "SystemException: " + msg);
            throw new ActionCommandException(msg);
        }

        try {
            Enumeration parmNames = this.request.getParameterNames();
            String elementName = null;
            String elementValue = null;
            while (parmNames.hasMoreElements()) {
                elementName = (String) parmNames.nextElement();
                elementValue = this.request.getParameter(elementName);
                try {
                    _dso.getDataSourceAttib().setColumnAttribute(elementName,
                            elementValue);
                    processCount++;
                } catch (NotFoundException e) {
                    logger.log(Level.DEBUG, e.getMessage() + " - Property: "
                            + elementName);
                    continue;
                }
            } // end while

            return processCount;

        } // end try
        catch (Exception e) {
            throw new ActionCommandException(
                    "Unable to obtain simple form data", e);
        }
    }

    /**
     * Drives the process of builing the selection criteria. The selection
     * criteria can be used to allow server-side processes to retrieve and send
     * data to client packaged as a bean or send the selection criteria to the
     * client to be utilized accordingly. For the latter, the selection criteria
     * is stored in the session object as
     * {@link com.constants.RMT2ServletConst#QUERY_BEAN QUERY_BEAN} and can be
     * obtained using the following constant,
     * <b>RMT2ServletConst}.QUERY_BEAN</b>. The user's selection criteria input
     * can also be stored in {@link bean.RMT2TagQueryBean RMT2TagQueryBean} via
     * the custom object property which the client is responsible for prooperly
     * casting.
     * <p>
     * The developer is responsible for passing any required data to the client
     * via the request and session objects
     * 
     * @throws ActionHandlerException
     */
    public void buildSearchCriteria() throws ActionCommandException {
        String sql = null;
        this.firstTime = false;
        Object customObj = this.doCustomInitialization();
        RMT2TagQueryBean query = (RMT2TagQueryBean) this.getSession()
                .getAttribute(RMT2ServletConst.QUERY_BEAN);
        query.setCustomObj(customObj);
        query.setWhereClause(null);
        sql = this.buildClientSelectionCriteria(false);
        query.setWhereClause(sql);
        this.doPostCustomInitialization(query, RMT2ServletConst.SEARCH_MODE_NEW);
        this.doOrderByClause(query);
        this.getSession().setAttribute(RMT2ServletConst.QUERY_BEAN, query);
        return;
    }

    /**
     * This method queries the database using the selection criteria from the
     * previus query, which is stored in the session object.
     * 
     * @throws ActionHandlerException
     */
    public void useExistingSearchCriteria() throws ActionCommandException {
        RMT2TagQueryBean query = (RMT2TagQueryBean) this.getSession()
                .getAttribute(RMT2ServletConst.QUERY_BEAN);
        this.doPostCustomInitialization(query, RMT2ServletConst.SEARCH_MODE_OLD);
        return;
    }

    /**
     * Displays the Search page for the first time. Since this will be
     * considered as the initial entry point into this session, the session
     * object designated as RMT2ServletConst.QUERY_BEAN will be initialized
     * without any pertinent values which will render the search criteria
     * section with blank values and the Search Result Set section with an empty
     * result set.
     * 
     * @throws ActionHandlerException
     */
    public void startSearchConsole() throws ActionCommandException {
        String criteria = null;
        this.firstTime = true;
        RMT2TagQueryBean query;
        try {
            query = new RMT2TagQueryBean();
        } catch (Exception e) {
            query = null;
        }

        Object customObj = this.doCustomInitialization();
        query.setCustomObj(customObj);
        query.setWhereClause(null);

        // Force an empty result set by purposely constructing erroneous
        // selection criteria.
        criteria = this.doInitialCriteria(query);
        query.setWhereClause(criteria);
        this.doOrderByClause(query);
        this.getSession().setAttribute(RMT2ServletConst.QUERY_BEAN, query);
        return;
    }

    /**
     * Returns the condition that serves as the default selection criteria
     * applied to a datasource for first time usage. The default criteria,
     * "id = -1", is expected to return an empty result set and is assuming that
     * the datasource includes a primary key column named, "id", and that id's
     * value is always greater than zero. The developer is expected to override
     * this method in order to implement custom selection criteria for the
     * datasource especially if the default criteria yields a "column not found"
     * SQL error.
     * 
     * @param _query
     *            The Query Object.
     * @return The selection criteria.as a String
     * @throws ActionHandlerException
     */
    protected String doInitialCriteria(RMT2TagQueryBean _query)
            throws ActionCommandException {
        return "id = -1";
    }

    /**
     * Override this method to gather and package the user's input of selection
     * criteria data from the request object into its proper object. This is
     * also a good place to initialize the base view which is required to build
     * the selection criteria via reflection.
     * 
     * @return Object this is the selection criteria packaged in its respective
     *         object.
     * @throws ActionHandlerException
     */
    protected Object doCustomInitialization() throws ActionCommandException {
        return null;
    }

    /**
     * Applies additional search criteria to _query. Override this method to
     * implement custom criteria logic using _query.
     * 
     * @param _query
     *            {@link RMT2TagQueryBean} object.
     * @param __searchMode
     *            Set to either {@link RMT2ServletConst.SEARCH_MODE_NEW} or
     *            {@link RMT2ServletConst.SEARCH_MODE_OLD}
     * @throws ActionHandlerException
     */
    protected void doPostCustomInitialization(RMT2TagQueryBean _query,
            int _searchMode) throws ActionCommandException {
        return;
    }

    /**
     * Constructs selection criteria from the client's JSP that is to be applied
     * to a datasource for the purpose of querying the database.
     * <p>
     * A few client-side JSP design prerequisites must be sataisfied in order
     * for this method to be functional and useful.
     * <ul>
     * <li>Client JSP field names, which are designated to contain selection
     * criteria data, are required to comply to this naming format: "qry_" +
     * (datasource view property name). For example, qry_XactId.</li>
     * <li>The crux of the field names must match the spelling of it
     * corresponding DataSource View property name</li>
     * <li>If multiple instances of a given field name are used to represent a
     * range of values, then the JSP is required to comply to this naming
     * format: "qry_" + (datasource view property name) + "_" + index value. For
     * example, qry_XactId_0, qry_XactId_1.</li>
     * <li>When appending an index to the name of a field, ensure that the index
     * is a number and do not use leading zeros.</li>
     * <li>Values representing relational operators associated with a given
     * selection criteria field must be prefixed with the following:
     * "qryRelOp_". For example, qryRelOp_XactId. When a relational operator has
     * been selected to be associated with a field that is part of a multiple
     * instance selection criteria field, then the relational operator field
     * must be named with the same index that of is assoicated selection
     * criteria field. For example, qry_XactId_1 ==> qryRelOp_XactId_1.</li>
     * <li>All values of a selection criteria field that equal null or spaces
     * will be ignored during the construction of the criteria.</li>
     * </ul>
     * <p>
     * At this time all fields and values that are a part of single instances
     * and multiple instances will be joined together using the logical
     * operator, "and" .
     * 
     * @param _exactDate
     *            Indicates if date should be compared exactly or be compared to
     *            exist between a 24 hour period.
     * @throws ActionHandlerException
     */
    protected String buildClientSelectionCriteria(boolean _exactDate)
            throws ActionCommandException {
        String method = "buildCriteriaFromClient()";
        StringBuffer criteria = new StringBuffer(100);
        StringBuffer fieldCriteria = new StringBuffer(100);
        String elementName = null;
        String elementValue = null;
        String elementNdxToken = null;
        String dbName = null;
        String tableName = null;
        String colName = null;
        String dataTypeStr = null;
        String dsFieldName = null;
        String relOpValue = null;
        String relOpFieldName = null;
        String customCriteria = null;
        String msg = null;
        int elementValueNdx = 0;
        int dataType = 0;
        List<String> valueList = null;
        Enumeration parmNames = this.request.getParameterNames();

        // DatabaseTransApi tx = DatabaseTransFactory.create();
        // DataSourceApi dso = DataSourceFactory.create((DatabaseConnectionBean)
        // tx.getConnector(), this.baseView);
        if (dso == null) {
            msg = "DataSource object is invalid.   Client input was not processed.";
            logger.log(Level.ERROR, "SystemException: " + msg);
            throw new ActionCommandException(msg);
        }

        try {
            while (parmNames.hasMoreElements()) {
                elementName = (String) parmNames.nextElement();
                // Ignore advanced search option fields
                if (elementName
                        .endsWith(AbstractActionHandler.FIELD_NAME_SUFFIX)) {
                    continue;
                }
                elementValue = this.request.getParameter(elementName);

                // Bypass this field if no value was input.
                if (elementValue == null || elementValue.length() <= 0) {
                    continue;
                }

                // Use only those fields that are apart of the selection
                // criteria and are do not represent relational operators (begin
                // with qry_ )
                if (elementName.startsWith(AbstractActionHandler.QRY_PREFIX)) {
                    valueList = RMT2String.getTokens(elementName, "_");
                    if (valueList == null) {
                        continue;
                    }
                    if (valueList.size() > 0) {
                        // Ignore first token
                        dsFieldName = (String) valueList.get(0);
                        // This is the one we want
                        dsFieldName = (String) valueList.get(1);

                        // Get the index of query field in the event there are
                        // multiple instances.
                        // The index will be used to track down the relational
                        // operator input, if one exist.
                        elementNdxToken = AbstractActionHandler.QRY_PREFIX
                                + dsFieldName + "_";
                        elementValueNdx = this.getQueryFieldNameIndex(
                                elementName, elementNdxToken);

                        // Go to next field if datasource view field name is not
                        // found or is invalid
                        if (elementValueNdx == AbstractActionHandler.INDEX_CODE_INVALID) {
                            continue;
                        }

                        // Get associated relational operator value provided
                        // that it exist.
                        if (elementValueNdx == AbstractActionHandler.INDEX_CODE_NONE) {
                            // There is only one instance of this field name
                            relOpFieldName = AbstractActionHandler.QRY_PREFIX_RELOP
                                    + dsFieldName;
                        }
                        if (elementValueNdx > AbstractActionHandler.INDEX_CODE_NONE) {
                            // More than one instance of this field name exist.
                            relOpFieldName = AbstractActionHandler.QRY_PREFIX_RELOP
                                    + dsFieldName + "_" + elementValueNdx;
                        }
                        relOpValue = this.request.getParameter(relOpFieldName);
                        relOpValue = relOpValue == null
                                || relOpValue.equals("") ? " = " : relOpValue;
                    }
                }
                else {
                    continue;
                }

                // At this point we have a valid selection criteria field.
                try {
                    dbName = (String) dso.getDataSourceAttib()
                            .getColumnAttribute(dsFieldName, "dbName");
                    tableName = (String) dso.getDataSourceAttib()
                            .getColumnAttribute(dsFieldName, "tableName");
                    tableName = (String) dso.getDataSourceAttib()
                            .getTableAttribute(tableName, "dbName");
                    dataTypeStr = (String) dso.getDataSourceAttib()
                            .getColumnAttribute(dsFieldName, "sqlType");
                    dataType = RMT2Utility.getJavaType(dataTypeStr);
                    colName = tableName + "." + dbName;

                    // Begin to build selection criteria.
                    fieldCriteria.delete(0, fieldCriteria.length());
                    fieldCriteria.append(colName);
                    switch (dataType) {
                        case Types.INTEGER:
                        case Types.TINYINT:
                        case Types.SMALLINT:
                        case Types.REAL:
                        case Types.FLOAT:
                        case Types.NUMERIC:
                        case Types.DOUBLE:
                        case Types.DECIMAL:
                        case Types.BIGINT:
                            fieldCriteria.append(relOpValue);
                            fieldCriteria.append(elementValue);
                            break;

                        case Types.CHAR:
                        case Types.VARCHAR:
                        case Types.LONGVARCHAR:
                            // Recognize advanced search options, if applicable
                            String advSrchOptField = elementName
                                    + AbstractActionHandler.FIELD_NAME_SUFFIX;
                            String advSrchOpt = this.request
                                    .getParameter(advSrchOptField);
                            if (advSrchOpt == null
                                    || advSrchOpt
                                            .equals(AbstractActionHandler.ADV_SRCH_BEGIN)) {
                                // Apply default search option
                                fieldCriteria.append(" like");
                                fieldCriteria.append(" \'");
                                fieldCriteria.append(elementValue);
                                fieldCriteria.append("%\'");
                            }
                            else if (advSrchOpt
                                    .equals(AbstractActionHandler.ADV_SRCH_END)) {
                                // Apply Ends with comparison
                                fieldCriteria.append(" like");
                                fieldCriteria.append("\'%");
                                fieldCriteria.append(elementValue);
                                fieldCriteria.append("\'");
                            }
                            else if (advSrchOpt
                                    .equals(AbstractActionHandler.ADV_SRCH_CONTATIN)) {
                                // Apply Contains comparison
                                fieldCriteria.append(" like");
                                fieldCriteria.append("\'%");
                                fieldCriteria.append(elementValue);
                                fieldCriteria.append("%\'");
                            }
                            else if (advSrchOpt
                                    .equals(AbstractActionHandler.ADV_SRCH_EXACT)) {
                                // Apply Exact Match comparison
                                fieldCriteria.append(" = ");
                                fieldCriteria.append("\'");
                                fieldCriteria.append(elementValue);
                                fieldCriteria.append("\'");
                            }
                            break;

                        case Types.DATE:
                        case Types.TIME:
                        case Types.TIMESTAMP:
                            if (_exactDate) {
                                fieldCriteria.append(relOpValue);
                                fieldCriteria.append("\'");
                                fieldCriteria.append(elementValue);
                                fieldCriteria.append("\'");
                            }
                            else {
                                fieldCriteria.append(" >= \'");
                                fieldCriteria.append(elementValue);
                                fieldCriteria.append("\' and ");
                                fieldCriteria.append(colName);
                                fieldCriteria.append(" <= \'");
                                fieldCriteria.append(elementValue);
                                fieldCriteria.append(" 23:59:59\'");
                            }
                            break;

                        default:
                            break;
                    } // end switch

                    // Check if custom criteria exist for current Selection
                    // Criteria field
                    customCriteria = this
                            .buildCustomClientCriteria(elementName);

                    if (customCriteria != null && customCriteria.equals("")) {
                        // bypass adding column to criteria
                        continue;
                    }
                    if (criteria.length() > 0) {
                        criteria.append(" and ");
                    }
                    // Add either custom criteria or criteria constructed above
                    // for current field
                    criteria.append((customCriteria == null ? fieldCriteria
                            .toString() : customCriteria));
                } catch (NotFoundException e) {
                    msg = "["
                            + AbstractActionHandler.class.getClass().getName()
                            + "." + method + "]  NotFoundException: ";
                    msg += e.getMessage() + " - Property: " + elementName;
                    logger.log(Level.DEBUG, msg);
                    continue;
                }
            } // end while

            customCriteria = this.postBuildCustomClientCriteria();
            if (customCriteria != null) {
                if (criteria.length() > 0) {
                    criteria.append(" and ");
                }
                criteria.append(customCriteria);
            }

            return criteria.toString();
        } // end try
        catch (Exception e) {
            msg = "Unable to build client action handler selection criteria for "
                    + AbstractActionHandler.class.getClass().getName();
            logger.log(Level.ERROR, msg);
            throw new ActionCommandException(msg, e);
        } finally {
            dso.close();
            // tx.close();
            dso = null;
            // tx = null;
        }
    }

    // private void loadAdvanceSearchFields() {
    // if (this.request == null) {
    // return;
    // }
    //
    // Enumeration<String> e = this.request.getParameterNames();
    // while (e.hasMoreElements()) {
    // String parmName = e.nextElement();
    // if (parmName.endsWith(AbstractActionHandler.FIELD_NAME_SUFFIX)) {
    // String parmVal = this.request.getParameter(parmName);
    // this.advSrchFields.put(parmName, parmVal);
    // }
    // }
    // }

    /**
     * This method is invoked from buildClientSelectionCriteria() and should be
     * overriden for the purpose of customizing the selection criteria for
     * _fieldName when needed.
     * 
     * @param _fieldName
     *            The name of the field that is to be represented by customized
     *            selection criteria.
     * @return A non-null, properly formatted sql where clause string which
     *         contains the selection criteria pertaining to _fieldName. Retun
     *         null to indicate that there is no selection criteria to be
     *         applied and to simply use what was provided at the ancestor
     *         level. Return "" to indicate that the selection criteria at the
     *         ancestor level for _fieldName should be ignored.
     */
    protected String buildCustomClientCriteria(String _fieldName) {
        return null;
    }

    /**
     * This method is invoked from the buildClientSelectionCriteria method after
     * the selection criteria for all fields that have not been examined. It is
     * advisable to override this method for the purpose of applying post
     * selection criteria logic.
     * 
     * @return String that represents the custom selection criteria that is to
     *         be applied to the SQL where clause beyond what has been
     *         configured in the ancestor. Return null to indicate that there is
     *         no custom selection criteria to be applied.
     */
    protected String postBuildCustomClientCriteria() {
        return null;
    }

    /**
     * Invoked from buildClientSelectionCriteria() for the purpose of setting
     * the SQL order by clause.
     * 
     * @param query
     *            The query bean that houses SQL related properties.
     */
    protected void doOrderByClause(RMT2TagQueryBean query) {
        return;
    }

    /**
     * This method determines if _src is part of a multiple instance group and
     * makes an attempt to obtain its index id using _prefix as a search token.
     * 
     * @return int value >= zero if an index is found, -1 when the field name is
     *         valid but is a single instance, and -2 when an invalid index
     *         value is appended to the field name and the index cannot be
     *         converted to a number.
     */
    private int getQueryFieldNameIndex(String _src, String _prefix) {
        List<String> list = null;
        String ndxStr = null;
        int ndx = 0;

        list = RMT2String.getTokens(_src, _prefix);

        // Return -1 if only one instance of view field
        if (list == null) {
            return AbstractActionHandler.INDEX_CODE_NONE;
        }

        ndxStr = (String) list.get(0);

        try {
            ndx = Integer.parseInt(ndxStr);
        } catch (NumberFormatException e) {
            // return invalid field index error code
            return AbstractActionHandler.INDEX_CODE_INVALID;
        }
        // Return valid index
        return ndx;
    }

    /**
     * Deletes row(s) from a datasource that represents one table.
     * <p>
     * Rules for deleteing groups and its code details: 1. A group can only be
     * deleted if it is not associated with any details. 2. Code details can be
     * deleted only if the code is not tied to any other tables in the system as
     * a foreign key. 3. If either steps 1 or 2 are violated, throw an exception
     * and display the error message to the client. 4. If a row is not selected
     * the client, throw an exception and display the error message to the
     * client.
     * 
     * @param ds
     *            Represents the name of the datasource to perform the deletion
     *            on
     * @param keyName
     *            The name of the HTML or JSP input control that contains the
     *            value of the primary key of the target row.
     * @return int - 1=Success, -1=Failure
     * @throws SystemException
     * @throws DatabaseException
     * @throws NotFoundException
     */
    public int deleteAction(String ds, String keyName) throws SystemException,
            DatabaseException, NotFoundException {
        int row = this.getSelectedRow(keyName);
        String uid = request.getParameter("Id" + row);

        // Throw an exception if a row is not selected
        if (uid == null) {
            throw new SystemException(
                    RMT2SystemExceptionConst.MSG_DELETEROW_NOT_SELECTED,
                    RMT2SystemExceptionConst.RC_DELETEROW_NOT_SELECTED);
        }

        // Retrieve the item so it can be deleted!
        DatabaseConnectionBean dbConn = DatabaseConnectionPool
                .getAvailConnectionBean();
        DataSourceApi dso = DataSourceFactory.create(dbConn, ds);
        dso.setDatasourceSql(DbSqlConst.WHERE_KEY, null);
        dso.setDatasourceSql(DbSqlConst.WHERE_KEY, "id = " + uid);

        // Create non-scrollable updateable resultset
        dso.executeQuery(true, true);
        if (!dso.nextRow()) {
            throw new NotFoundException(DbSqlConst.MSG_ROW_NOTFOUND,
                    DbSqlConst.RC_ROW_NOTFOUND);
        }
        try {
            dso.deleteRow();
            // dso.commitUOW();
            return RMT2Base.SUCCESS;
        } catch (DatabaseException e) {
            // Try to display a more user-friendly exception message regarding
            // database errors.

            // SQL Server Referential integrity conflict
            if (e.getErrorCode() == RMT2SystemExceptionConst.RC_SQLSERVER_REF_INTEGRITY_CONFLICT) {
                // Attempting to delete a Code Details that exist in dependent
                // tables throughout the database
                e.setAltMessage(RMT2SystemExceptionConst.MSG_SQLSERVER_REF_INTEGRITY_CONFLICT);
                e.setErrorCode(RMT2SystemExceptionConst.RC_SQLSERVER_REF_INTEGRITY_CONFLICT);
            }

            logger.log(Level.ERROR, "Delete of row failed.");
            logger.log(Level.ERROR, "Error Code: " + e.getErrorCode());
            logger.log(Level.ERROR, "Error Message: " + e.getMessage());
            // dso.rollbackUOW();
            throw e;
        } finally {
            dbConn.close();
            dbConn = null;
        }

    }

    /**
     * Obtains the value of an input control item, "property", from the client's
     * page. This method possesses functionality to determine if the value exist
     * in a list of multiple occurrencess or as a single occurrence within the
     * client's request. The first order of operation is to attempt to locate
     * the item from a list which requires "property" to be uniquely named and
     * "selector" must exist generally as a check box or radio button containing
     * an Unique Identifier (UID) value. The uniqueness of the property is
     * computed as <property + UID>. Otherwise, it is assumed that the property
     * exist as a single occurrence and is treated as such.
     * 
     * @param property
     *            The name of the property to obtain value.
     * @param selector
     *            The name of the input control (check box or radio button)
     *            containing the unique value to locate property. If null, the
     *            default, "selCbx", is used.
     * @return The value of property.
     */
    public String getInputValue(String property, String selector) {
        String defaultSelector = GeneralConst.CLIENTROW_PROPERTY;
        String temp = null;
        String subMsg = null;
        int row = 0;

        // Determine if we are coming from a page that presents data as a list
        // of orders or as single order.
        try {
            // Get selected row number from client page. If this row number
            // exist, then we
            // are coming from page that contains a list of orders.
            row = this.getSelectedRow((selector == null ? defaultSelector
                    : selector));
            temp = this.getPropertyValue(property + row);
            return temp;
        } catch (SystemException e) {
            subMsg = "Failed to detect list containing one or more properties named, "
                    + property
                    + ".  Will try to obtain from single record format.";
            logger.log(Level.WARN, subMsg);
        } catch (NotFoundException e) {
            subMsg = "Detected list data but failed to find property named, "
                    + property
                    + ".  Will try to obtain from single record format.";
            logger.log(Level.WARN, subMsg);
        }
        // At this point ths system has determine that we are not dealing with a
        // list of items.
        // We will try to obtain the property from a single record form.
        try {
            temp = this.getPropertyValue(property);
            return temp;
        } catch (NotFoundException e) {
            subMsg = "Input control does not exist by the name, " + property;
            logger.log(Level.WARN, subMsg);
            return null;
        }
    }

    /**
     * Retrieves the selected row index from a list of request parameters.
     * Genereally, the row value returned to the caller is used as an index to
     * access related data from the HttpServletRequest object.
     * 
     * @param _property
     *            Name of Parameter of the HttpServletRequst object that
     *            contains the selected row number.
     * @return int - The selected row number.
     * @throws SystemException
     */
    public int getSelectedRow(String _property) throws SystemException {
        String strRow = this.request.getParameter(_property);
        int row = 0;

        try {
            row = Integer.valueOf(strRow).intValue();
            return row;
        } catch (NumberFormatException e) {
            throw new SystemException("Problem identifying selected row.");
        }
    }

    /**
     * Gets the selected row number that was retreived from the client's
     * request, if available.
     * 
     * @return row number
     */
    public int getSelectedRow() {
        return this.selectedRow;
    }

    /**
     * Locates the value of _property in the HttpServletRequest object.
     * 
     * @param _property
     *            The name of the HttpServletRequest parameter.
     * @return The value associated with _property as a String
     * @throws NotFoundException
     *             When the property does not exist.
     */
    public String getPropertyValue(String _property) throws NotFoundException {
        String value = null;

        // Get Purchase Order id.
        value = this.request.getParameter(_property);
        if (value == null || value.equals("")) {
            this.msg = "Property value not found and does not exist: "
                    + _property;
            logger.log(Level.WARN, msg);
            throw new NotFoundException(this.msg);
        }
        return value;
    }

    /**
     * Adds a message to the message queue.
     * 
     * @param _msg
     */
    public void addMessages(String _msg) {
        String key = String.valueOf(this.messages.size() + 1);
        this.messages.put(key, _msg);
    }

    /**
     * Returns the associated servlet context
     * 
     * @return ServletContext
     */
    public Context getContext() {
        return this.context;
    }

    /**
     * Returns the associated user session object
     * 
     * @return HttpSession
     */
    public Session getSession() {
        return this.session;
    }

    /**
     * Returns the associated request object
     * 
     * @return HttpServletRequest
     */
    public Request getRequest() {
        return this.request;
    }

    /**
     * Returns the name of the base view
     * 
     * @return String
     */
    public String getBaseView() {
        return this.baseView;
    }

    /**
     * Sets the name of the base view
     * 
     */
    public void setBaseView(String value) {
        this.baseView = value;
    }

    /**
     * Returns the name of the base class
     * 
     * @return String
     */
    public String getBaseClass() {
        return this.baseClass;
    }

    /**
     * Sets the name of the base class
     * 
     */
    public void setBaseClass(String value) {
        this.baseClass = value;
    }

    /**
     * Sets the entry point state of this session.
     * 
     * @param value
     *            true if is tis the first time for this session , and false if
     *            it is two or more times returning to the entry point of this
     *            session.
     */
    public void setFirstTime(boolean value) {
        this.firstTime = value;
    }

    /**
     * Gets the state of the session entry point.
     * 
     * @return boolean Rettuns true if user has reached the entry point to this
     *         session for the first time, and fales if the user is a successive
     *         entry for the session.
     */
    public boolean isFirstTime() {
        return this.firstTime;
    }

    /**
     * Prepares the client to add a data item. This method makes use of the
     * folloiwng methods in the order specified: <blockquote> add()
     * <p>
     * sendClientData() </blockquote>
     * 
     * @throws ActionHandlerException
     */
    protected void addData() throws ActionCommandException {
        this.add();
        this.sendClientData();
    }

    /**
     * Prepares the client to edit a data item. This method makes use of the
     * folloiwng methods in the order specified: <blockquote> edit()
     * <p>
     * sendClientData() </blockquote>
     * 
     * @throws ActionHandlerException
     */
    protected void editData() throws ActionCommandException {
        this.receiveClientData();
        this.edit();
        this.sendClientData();
    }

    /**
     * Prepares the client to save a data item. This method makes use of the
     * folloiwng methods in the order specified <blockquote> receiveClientData()
     * <p>
     * save()
     * <p>
     * sendClientData() </blockquote>
     * 
     * @throws ActionHandlerException
     */
    protected void saveData() throws ActionCommandException {
        this.receiveClientData();
        try {
            this.save();
        } catch (Exception e) {
            this.setError(true);
            throw new ActionCommandException(e);
        } finally {
            this.sendClientData();
        }

    }

    /**
     * Prepares the client to delete a data item. This method makes use of the
     * folloiwng methods in the order specified: <blockquote>
     * receiveClientData()
     * <p>
     * delete()
     * <p>
     * sendClientData() </blockquote>
     * 
     * @throws ActionHandlerException
     */
    protected void deleteData() throws ActionCommandException {
        this.receiveClientData();
        try {
            this.delete();
        } catch (DatabaseException e) {
            throw new ActionCommandException(e);
        }
        this.sendClientData();
    }

    /**
     * Releases the data provider connection assoiciated with this action
     * handler.
     * 
     * @throws ActionHandlerException
     */
    public void close() throws ActionCommandException {
        return;
    }

    /**
     * Stub method for handling the request to navigate the user to the previous
     * page.
     * 
     * @throws ActionHandlerException.
     */
    protected void doBack() throws ActionCommandException {
        return;
    }

    /**
     * This method must be implemented in order to provide customized
     * functionality for retrieveing data from the client's request.
     * 
     * @throws ActionHandlerException
     */
    protected abstract void receiveClientData() throws ActionCommandException;

    /**
     * This method must be implemented in order to provide customized
     * functionality for sending data back to the client.
     * 
     * @throws ActionHandlerException
     */
    protected abstract void sendClientData() throws ActionCommandException;

    /**
     * Convert Java based data objects to XML documents. This can used in
     * situations where data is required to be sent back to the client in the
     * form of XML. Override this method with specific logic.
     * 
     * @return null.
     * @throws ActionHandlerException
     */
    protected String getXmlResults() throws ActionCommandException {
        return null;
    }

    /**
     * @return the error
     */
    public boolean isError() {
        return error;
    }

    /**
     * @param error
     *            the error to set
     */
    public void setError(boolean error) {
        this.error = error;
    }

}