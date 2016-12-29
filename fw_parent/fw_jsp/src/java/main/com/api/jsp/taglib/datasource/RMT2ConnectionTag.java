package com.api.jsp.taglib.datasource;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.sql.SQLException;

import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;

import com.api.jsp.taglib.RMT2TagSupportBase;
import com.api.persistence.db.DatabaseConnectionBean;
import com.api.pool.DatabaseConnectionPool;
import com.api.security.authentication.web.AuthenticationConst;
import com.api.web.security.RMT2SessionBean;
import com.constants.RMT2ServletConst;

/**
 * This custom tag associates a database connection within a JSP page. The
 * database connection is exported within the page scope to be utilized by other
 * related custom tags.
 * 
 * @author roy.terrell
 * 
 */
public class RMT2ConnectionTag extends RMT2TagSupportBase {
    private static final long serialVersionUID = -7721307584558054343L;

    private static Logger logger = Logger.getLogger(RMT2ConnectionTag.class);

    /**
     * Entry point into database connection custom tag. Ensures that the member
     * variable that held the previous connection bean is initialized to null
     * before ancestor logic takes over. Invokes processes responsible for
     * determining the connection and exporting the conection to the client JSP.
     * Lastly, the body of the custom tag is skipped by default.
     */
    public int doStartTag() throws JspException {
        this.obj = null;
        return super.doStartTag();
    }

    /**
     * Establishes the association of database connection to the JSP page by
     * gathering a DatabaseConnectionBean from usr's session. Control is passed
     * back to the ancestor script which will perform the actual exporting of
     * the connection to the page.
     * 
     * @return DatabaseConnectionBean
     * @throws JspException
     *             when the connection bean or the JDBC Connection object is
     *             invalid.
     */
    protected Object initObject() throws JspException {
        super.initObject();

        String msg = null;
        DatabaseConnectionBean connectionBean = null;
        HttpSession session = null;

        session = pageContext.getSession();
        if (session == null) {
            msg = "Problem obtaining a session for user";
            logger.log(Level.ERROR, msg);
            throw new JspException(msg);
        }
        RMT2SessionBean sessionBean = (RMT2SessionBean) session
                .getAttribute(RMT2ServletConst.SESSION_BEAN);
        String loginId;
        if (sessionBean != null) {
            // For local users
            loginId = sessionBean.getLoginId();
        }
        else {
            // For remote users.
            loginId = pageContext.getRequest().getParameter(
                    AuthenticationConst.AUTH_PROP_USERID);
        }
        connectionBean = (DatabaseConnectionBean) pageContext
                .getAttribute(RMT2ServletConst.DB_CONNECTION_BEAN);
        if (connectionBean == null) {
            try {
                if (loginId == null) {
                    msg = "DB Connection to JSP page failed.   Problem identifying login id";
                    logger.log(Level.ERROR, msg);
                    throw new JspException(msg);
                }
                connectionBean = DatabaseConnectionPool
                        .getAvailConnectionBean();
            } catch (Exception e) {
                throw new JspException(e.getMessage());
            }

        }
        if (connectionBean == null) {
            msg = "A valid database object could not be obtained from the User Session.   User is not properly signed on to System";
            logger.log(Level.ERROR, msg);
            throw new JspException(msg);
        }
        if (connectionBean.getNativeConnection() == null) {
            msg = "A valid Connection object could not be obtained from the Database Connection object.   Database may de down";
            logger.log(Level.ERROR, msg);
            throw new JspException(msg);
        }
        try {
            logger.log(Level.DEBUG, "Checking if database connection is closed");
            if (connectionBean.getNativeConnection().isClosed()) {
                msg = "A closed database connection was obtained";
                logger.log(Level.ERROR, msg);
                throw new JspException(msg);
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, e.getMessage());
            e.printStackTrace();
            throw new JspException(e);
        }
        pageContext.setAttribute(RMT2ServletConst.DB_CONNECTION_BEAN,
                connectionBean);
        return connectionBean;
    }
}