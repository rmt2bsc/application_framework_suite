package com.api.jsp.taglib.datasource;

import java.io.IOException;

import java.sql.SQLException;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.IterationTag;

import com.api.DataSourceApi;
import com.api.jsp.taglib.RMT2BodyTagSupportBase;

/**
 * A custom tag that iterates through the result set of a RMT2DataSourceApi
 * object and exposes the object to the body content.
 * 
 * @author roy.terrell
 * 
 */
public class RMT2ForEachRowTag extends RMT2BodyTagSupportBase {
    private static final long serialVersionUID = -2435729634260878040L;

    private int row = 0;

    /**
     * This is the entry point into the custom tag. It ensures that the data
     * source is valid and begins the iteration of the api object by positioning
     * itself at the first record.
     * 
     * @return IterationTag.EVAL_BODY_AGAIN if there is at least one item to
     *         process. Otherwise, IterationTag.SKIP_BODY is returned.
     * @throws JspException
     *             when data source is null of the result set within the data
     *             source is null.
     */
    public int doStartTag() throws JspException {
        try {
            this.className = "RMT2ForEachRow";
            this.obj = null;
            this.obj = this.getObject();
            if (this.obj == null) {
                throw new JspException(
                        "Error: Unable to get a reference to the iterative datasource object.");
            }
            if (((DataSourceApi) this.obj).getRs() == null) {
                throw new JspException(
                        "RMt2ForEachRowTag Error: Unable to get a valid reference to the ResultSet object of the datasource. Resultset is equal to null");
            }
            if (((DataSourceApi) this.obj).getRs().next()) {
                return IterationTag.EVAL_BODY_AGAIN;
            }
            else {
                return IterationTag.SKIP_BODY;
            }
        } catch (SQLException e) {
            throw new JspException(e.getMessage());
        }
    }

    /**
     * This process is triggered once the remaining content of the current
     * iteration has been evaluated. The content is written to the JSP via the
     * JSP writer and the next iteration of body content is set to begin which
     * includes the exporting of data until all iterations have been exhausted.
     * 
     * @return IterationTag.EVAL_BODY_AGAIN to process the next iteration of the
     *         body. IterationTag.SKIP_BODY when there is no more content to
     *         process.
     * @throws JspException
     *             unable to write body content to JSP.
     */
    public int doAfterBody() throws JspException {
        try {
            this.getBodyContent().writeOut(this.getPreviousOut());
            this.getBodyContent().clear();
            if (((DataSourceApi) this.obj).getRs().next()) {
                this.exportData();
                return IterationTag.EVAL_BODY_AGAIN;
            }
            else {
                return IterationTag.SKIP_BODY;
            }
        } catch (IOException e) {
            throw new JspException(
                    "Error: IOExeption while writing body content to the user");
        } catch (SQLException e) {
            throw new JspException(e.getMessage());
        }

    }

    /**
     * This method is used to identify and obtain the list of data source
     * objects that need to be processed.
     * 
     * @return An Object disguised as an ArrayList.
     * @throws JspException
     *             If a problem occured getting the list collection.
     */
    private Object getObject() throws JspException {
        return (this.obj == null ? this.getObject(this.dataSource,
                this.objScope) : this.obj);
    }

    /**
     * PUblishes the row id to the JSP Page which is identified as, "ROW" on the
     * PageContext object.
     * 
     * @throws JspException
     */
    protected void exportData() throws JspException {

        // Publish the current iteration count as "ROW" at page scope
        pageContext.setAttribute("ROW", String.valueOf(row),
                PageContext.PAGE_SCOPE);
        row++;
    }

}