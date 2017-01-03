package com.api.jsp.taglib.datasource;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.IterationTag;

import com.NotFoundException;
import com.SystemException;
import com.api.DataSourceApi;
import com.api.jsp.taglib.PageVariableHelper;
import com.api.jsp.taglib.RMT2AbstractInputControl;
import com.api.persistence.DatabaseException;

/**
 * This abstract class provides base functionality for creating HTML list
 * controls with a "lookup" or corss-reference characteristic. Usually, these
 * controls have a data source containing a comprehensive list of
 * code/descripton values and another data source containing an item that is to
 * be matched against the list of code/description values.
 * 
 * @author roy.terrell
 * 
 */
public abstract class RMT2AbstractLookupTag extends RMT2AbstractInputControl {

    /**
     * The data source that contains the code to be cross referenced. This
     * property is usually linked to the dataSource tag property
     */
    protected DataSourceApi masterDso = null;

    /** The name of the property used to obtain the target code value. */
    protected String masterCodeName = null;

    /** The target code value that is to be cross referenced */
    protected String masterCodeValue = null;

    /** The data source that contains a master list of code/description values */
    protected DataSourceApi lookupDso = null;

    /**
     * A reference to the data source containing the list of code/ddescription
     * pairs.
     */
    protected String lookupSource = null;

    /** The name of the property that represents a lookup code. */
    protected String lookupCodeName = null;

    /**
     * The name of the property that represents the description of the lookup
     * code.
     */
    protected String lookupDisplayName = null;

    /**
     * Sets the lookup data source reference.
     * 
     * @param value
     */
    public void setLookupSource(String value) {
        this.lookupSource = value;
    }

    /**
     * Sets the reference of the target code value
     * 
     * @param value
     */
    public void setMasterCodeName(String value) {
        if (value.equalsIgnoreCase("")) {
            this.masterCodeName = null;
        }
        else {
            this.masterCodeName = value;
        }
    }

    /**
     * Sets the value of the target code.
     * 
     * @param value
     */
    public void setMasterCodeValue(String value) {
        this.masterCodeValue = value;
    }

    /**
     * Sets the reference of the lookup code value.
     * 
     * @param value
     */
    public void setLookupCodeName(String value) {
        this.lookupCodeName = value;
    }

    /** Sets the reference of the lookup display name */
    public void setLookupDisplayName(String value) {
        this.lookupDisplayName = value;
    }

    /**
     * This is the entry point into the custom tag. Obtains the target and the
     * lookup data sources.
     * 
     * @return SKIP_BODY
     * @throws JspException
     *             IO errors, database access errors, System errors, or resource
     *             not found errors.
     */
    public int startUp() throws JspException {
        try {
            PageVariableHelper valueHelper = new PageVariableHelper();
            this.masterCodeValue = (String) valueHelper.getValue(
                    this.pageContext, this.masterCodeValue, null, this.format);
            this.masterDso = (DataSourceApi) this.obj;
            this.lookupDso = (DataSourceApi) pageContext
                    .getAttribute(this.lookupSource);
            this.outputHtml(buildInputControl());
            return IterationTag.SKIP_BODY;
        } catch (IOException e) {
            throw new JspException("IOException: " + e.getMessage());
        } catch (SystemException e) {
            throw new JspException("SystemException: " + e.getMessage());
        } catch (DatabaseException e) {
            throw new JspException("DatabaseException: " + e.getMessage());
        } catch (NotFoundException e) {
            throw new JspException("NotFoundException: " + e.getMessage());
        }
    }

}