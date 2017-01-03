package com.api.jsp.taglib.datasource;

import javax.servlet.jsp.JspException;

import com.api.jsp.taglib.RMT2TagSupportBase;
import com.api.persistence.db.DatabaseConnectionBean;
import com.constants.RMT2ServletConst;

/**
 * This custom tag releases all resources such as connections, cope varibles,
 * and etc.
 * 
 * @author roy.terrell
 * 
 */
public class RMT2DisposeTag extends RMT2TagSupportBase {
    private static final long serialVersionUID = -7721307584558054343L;

    /**
     * Removes the database connecton from the session and puts the connection
     * back into the database connection pool.
     */
    public int doStartTag() throws JspException {
        DatabaseConnectionBean connectionBean = (DatabaseConnectionBean) pageContext
                .getAttribute(RMT2ServletConst.DB_CONNECTION_BEAN);
        if (connectionBean != null) {
            connectionBean.close();
        }
        return SKIP_BODY;
    }
}