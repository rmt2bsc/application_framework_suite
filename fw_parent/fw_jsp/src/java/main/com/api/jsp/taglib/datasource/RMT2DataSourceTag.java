package com.api.jsp.taglib.datasource;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.SystemException;
import com.api.DataSourceApi;
import com.api.jsp.taglib.RMT2TagSupportBase;
import com.api.persistence.DatabaseException;
import com.api.persistence.db.DatabaseConnectionBean;
import com.api.persistence.db.orm.DataSourceFactory;
import com.api.security.RMT2TagQueryBean;

/**
 * Creates a RMT2DataSourceApi and executes its query for the JSP page.
 * 
 * @author roy.terrell
 * 
 */
public class RMT2DataSourceTag extends RMT2TagSupportBase {
    private static final long serialVersionUID = -8756323109942185125L;

    private static Logger logger = Logger.getLogger(RMT2DataSourceTag.class);

    /**
     * The name of the connection object to assoicate with the datasource object
     */
    protected String connection = null;

    /** The name of the mapping schema to assoicate with the datasource */
    protected String query = null;

    /**
     * The type of mapping schema used. The type can be "xml" (a XML datasource)
     * or "sql" (an actual SQL statement String).
     */
    protected String type = null; // sql, xml

    /** The selection criteria to apply to the datasource */
    protected String where = null;

    /** The ordering criteria to apply to the datasource */
    protected String order = null;

    /**
     * The name of a session attribute that exist as selection criteria to be
     * used with the datasource.
     */
    protected String queryId = null;

    /**
     * Sets the name of the connection.
     * 
     * @param value
     */
    public void setConnection(String value) {
        this.connection = value;
    }

    /**
     * Sets the name of the datasource Query.
     * 
     * @param value
     */
    public void setQuery(String value) {
        this.query = value;
    }

    /**
     * Sets the type of datasource querey.
     * 
     * @param value
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Sets the selection criteria that is to be used with the datasource
     * 
     * @param value
     */
    public void setWhere(String value) {
        this.where = value;
    }

    /**
     * Sets the order criteria that is to be used with the datasource
     * 
     * @param value
     */
    public void setOrder(String value) {
        this.order = value;
    }

    /**
     * Sets the name of the selection criteria stored on the session that is to
     * be used with the datasource.
     * 
     * @param value
     */
    public void setQueryId(String value) {
        this.queryId = value;
    }

    /**
     * Initializes <i>obj</i> property to to null before executing ancestor
     * script.
     */
    public int doStartTag() throws JspException {
        this.obj = null;
        return super.doStartTag();
    }

    /**
     * Creates the RMT2DataSourceApi object and uses the values contained in
     * this class' memeber variables to execute the target SQL query.
     * 
     * @return Object desquised as a RMT2DataSourceApi type.
     * @throws JspException
     *             Database access errors, SystemParams.properties is
     *             inaccessible, or the datasource query file is not supplied.
     */
    protected Object initObject() throws JspException {
        super.initObject();
        String msg = null;
        Object obj = null;
        HttpSession session = null;
        RMT2TagQueryBean queryObj = null;

        try {
            session = pageContext.getSession();
            if (session == null) {
                msg = "A valid session could not be obtained.  User may not be logged on to the system";
                logger.log(Level.ERROR, msg);
                throw new JspException(msg);
            }

            // Get selection criteria
            if (this.queryId != null) {
                queryObj = (RMT2TagQueryBean) session
                        .getAttribute(this.queryId);
            }

            DatabaseConnectionBean connectionBean = (DatabaseConnectionBean) pageContext
                    .getAttribute("con", pageContext.PAGE_SCOPE);
            if (connectionBean == null) {
                msg = "A valid database RMT2ConnectionBean object could not be obtained from the Database Collective.   Database may de down";
                logger.log(Level.ERROR, msg);
                throw new JspException(msg);
            }
            if (connectionBean.getNativeConnection() == null) {
                msg = "A valid Connection object does not exist within the selected Database Connection object.   Database may de down";
                logger.log(Level.ERROR, msg);
                throw new JspException(msg);
            }
            if (this.query == null) {
                msg = "Query parameter cannot be null for Datasource Tag";
                logger.log(Level.ERROR, msg);
                throw new JspException(msg);
            }
            if (this.type == null) {
                this.type = "xml"; // throw new
                                   // JspException("Query Type parameter cannot be null for Datasource Tag");
            }

            // Add any selection criteria and/or ordering criteria that exist
            // from session.
            if (queryObj != null) {
                String _sessionWhere = queryObj.getWhereClause();
                String _sessionOrderby = queryObj.getOrderByClause();
                if (_sessionWhere != null
                        && !_sessionWhere.equalsIgnoreCase("null")) {
                    if (this.where != null && this.where.length() > 0
                            && !this.where.equals("")) {
                        this.where += " and " + _sessionWhere;
                    }
                    else {
                        this.where = _sessionWhere;
                    }
                }

                if (_sessionOrderby != null) {
                    if (this.order != null && this.order.length() > 0
                            && !this.order.equals("")) {
                        this.order += " ,  " + _sessionOrderby;
                    }
                    else {
                        this.order = _sessionOrderby;
                    }
                }
            }
            if (this.type == null) {
                this.type = "xml";
            }
            RMT2TagQueryBean queryData = new RMT2TagQueryBean(this.query,
                    this.type, this.where, this.order);
            logger.log(Level.DEBUG,
                    "Begin to create DAO Object from custom JSP tag");
            obj = DataSourceFactory.createDao(connectionBean, queryData);
            if (obj == null) {
                msg = "A valid Datasource Object could not be obtained for this custom tag";
                logger.log(Level.ERROR, msg);
                throw new JspException(msg);
            }
            ((DataSourceApi) obj).executeQuery();

            // restore query bean's selection and order criteria
            if (queryObj != null) {
                session.setAttribute(this.queryId, queryObj);
            }
            return obj;
        }

        catch (DatabaseException e) {
            throw new JspException(msg + e.getMessage());
        } catch (SystemException e) {
            throw new JspException(e.getMessage());
        } catch (Exception e) {
            throw new JspException(msg + e.getMessage());
        }
    }

}