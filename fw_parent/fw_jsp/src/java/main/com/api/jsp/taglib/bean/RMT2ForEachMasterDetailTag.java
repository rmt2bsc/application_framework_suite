package com.api.jsp.taglib.bean;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Enumeration;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.IterationTag;

import com.api.jsp.taglib.RMT2BodyTagSupportBase;
import com.constants.RMT2TagConst;

/**
 * This custom tag functions to iterate through a set of data that is arranged
 * in a Master/Detail form. Each Master/Detail iteration is exported to the JSP
 * for further processing. The data must exist as a collection of key/value
 * pairs (Hashtable) where the key represents master data and the value
 * represents the details. The key/value pairs can contain primitive wrapper
 * objects or collections of objects so long as the tags ontained within the
 * body knows how to identify and manage the data.
 * 
 * @author roy.terrell
 * 
 */
public class RMT2ForEachMasterDetailTag extends RMT2BodyTagSupportBase {
    private Object item;

    private Object details;

    private Hashtable hashData;

    private Enumeration keys;

    private int row = 0;

    /**
     * Reference name of the Hashtable collection that contains the
     * master/detail data
     */
    protected String dataSet = null;

    /**
     * The class name of the master item which is used to create a scriptable
     * variable in the JSP
     */
    protected String masterItemClassId = null;

    /**
     * Reference to the Master Item object contained in dataSet that will be
     * published as variable to the JSP. This name is used to obtain the key
     * part of the data set
     */
    protected String masterItemName = null;

    /**
     * Reference to the Detail Data Item object contained in dataSet that will
     * be published as variable to the JSP. This name is used to obtain the
     * value part of the data set
     */
    protected String detailDataName = null;

    /**
     * Sets the name of the master item
     * 
     * @param value
     */
    public void setMasterItemName(String value) {
        this.masterItemName = value;
    }

    /**
     * Sets the name of the detail item.
     * 
     * @param value
     */
    public void setDetailDataName(String value) {
        this.detailDataName = value;
    }

    /**
     * Sets the name of the class representing the master item.
     * 
     * @param value
     */
    public void setMasterItemClassId(String value) {
        this.masterItemClassId = value;
    }

    /**
     * Sets the name of the master/detail data set.
     * 
     * @param value
     */
    public void setDataSet(String value) {
        this.dataSet = value;
    }

    /**
     * This is the entry point into the custom tag. Functions to identify the
     * data set, verifies that the data set conforms to the appropriate data
     * type (Hashtable), and determines if data set contain any data to pbe
     * processed.
     * 
     * @return IterationTag.EVAL_BODY_AGAIN if there is at least one item to
     *         process. IterationTag.SKIP_BODY when data set is null or there is
     *         no data to process.
     * @throws JspException
     *             THe data type of the data set is something other than
     *             Hashtable.
     */
    public int doStartTag() throws JspException {
        this.obj = null;
        this.row = 0;
        this.obj = this.getObject();
        // Abort if Hashtable does not exist or is null
        if (this.obj == null) {
            return IterationTag.SKIP_BODY;
        }
        // Be sure that the obj is of the right data type.
        if (this.obj instanceof Hashtable) {
            this.hashData = (Hashtable) this.obj;
        }
        else {
            throw new JspException(
                    "Error: Property, data, must evaluate as type Hashtable for tag <LoopMasterDetail>.   Class: RMT2ForEachMasterDetailTag");
        }
        // We have a valid Hashtable. Now let's get the keys and begin to
        // iterated each.
        this.keys = this.hashData.keys();
        if (this.keys.hasMoreElements()) {
            return IterationTag.EVAL_BODY_AGAIN;
        }
        else {
            return IterationTag.SKIP_BODY;
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

            // Export next master item and its details provided there are more
            // elements to process.
            this.row++;
            if (this.keys.hasMoreElements()) {
                this.exportData();
                return IterationTag.EVAL_BODY_AGAIN;
            }
            else {
                return IterationTag.SKIP_BODY;
            }
        } catch (IOException e) {
            throw new JspException(
                    "Error: IOExeption while writing body content to the user");
        }
    }

    /**
     * Get the Master/Detail collection stored either in the application,
     * session, request, or pageContext scopes.
     * 
     * @return Hashtable
     * @throws JspException
     */
    private Object getObject() throws JspException {
        return (this.obj == null ? this.getObject(this.dataSet, this.objScope)
                : this.obj);
    }

    /**
     * Publishes data from the current iteration to the JSP page using the
     * PageContext object. The following data items are exported:
     * <ul>
     * <li>Master data item which its key is set to the value of the member
     * variable, masterItemName.
     * <li>
     * <li>Detail data item which its key is set to the value of the member
     * variable, detailDataName.
     * <li>
     * <li>Master/Detail Row id which its key is set to
     * RMT2TagConst.ROW_ID_MASTERDETAIL.
     * <li>
     * </ul>
     * 
     * @throws JspException
     */
    protected void exportData() throws JspException {
        // Publish the current master item bean object as "this.bean" at page
        // scope
        this.item = keys.nextElement();
        pageContext.setAttribute(this.masterItemName, this.item,
                PageContext.PAGE_SCOPE);

        // Publish the detail ArrayList which belongs to the current master item
        this.details = this.hashData.get(this.item);
        pageContext.setAttribute(this.detailDataName, this.details,
                PageContext.PAGE_SCOPE);

        // Publish the current iteration count as "ROW" at page scope
        pageContext.setAttribute(RMT2TagConst.ROW_ID_MASTERDETAIL,
                String.valueOf(this.row), PageContext.PAGE_SCOPE);
    }
}