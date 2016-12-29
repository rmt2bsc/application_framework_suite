package com.api.jsp.action;

import com.RMT2BaseBean;
import com.SystemException;

/**
 * Abstract class that provides properties and methods that are used as part of
 * a mapping scheme to bind common JSP input values to a particular data source
 * view document. The functionality of AncestorQueryCriteria is designed to be
 * used within an extended implementation of {@link AbstractActionhandler} where
 * the client's input values can be mapped to a DataSource View document using
 * one of two approaches:
 * <ol>
 * <li>By bean introspection using one of the {@link DataSourceConverter}
 * .package* methods.</li>
 * <li>By manual means using custom logic to map the values.</li>
 * <li>A combination of introspection and manual means.</li>
 * </ol>
 * 
 * In order to map the client's input values using the first approach, a few
 * client-side JSP design rules must be sataisfied for this class or any of its
 * descendents to recognize one or more JSP input controls as a query field
 * versus a common input field.
 * <ul>
 * <li>Client JSP input control names, which are designated to contain selection
 * criteria data, must be prefixed with the following string, "qry_", and the
 * rest of the name must be that of one of the DataSource view property names
 * which the first character following the prefix must be capitalized. For
 * example, qry_XactId.</li>
 * <li>The root of the JSP input control name must match the spelling of it
 * corresponding DataSource View property name.</li>
 * <li>If multiple instances of a given input control name are used to represent
 * a range of values, then the JSP is required to comply to this naming format:
 * 1) must be prefixed with the following string, "qry_", 2) the root part of
 * the name must be that of one of the DataSource view property names which the
 * first character following the prefix must be capitalized, and 3) the
 * remaining portion of the name must have an underscore ("_") plus a numerical
 * index value (1..n) appended. For example, qry_XactId_0, qry_XactId_1.</li>
 * <li>When appending an index to the name of a field, ensure that the index is
 * a number and do not use leading zeros.</li>
 * <li>Values representing relational operators associated with a given
 * selection criteria field must be prefixed with the following: "qryRelOp_".
 * For example, qryRelOp_XactId. When a relational operator has been selected to
 * be associated with a field that is part of a multiple instance selection
 * criteria field, then the relational operator field must be named with the
 * same index that of is assoicated selection criteria field. For example,
 * qry_XactId_1 ==> qryRelOp_XactId_1.</li>
 * </ul>
 * <p>
 * When the desire exists to ignore the introspection of one or more JSP query
 * input controls so that a custom mapping implementation may be used, the
 * following rule must be met: ensure that the client JSP query input control
 * name(s) do not conform to any of the property names that exist in this class
 * or any of its descendents.
 * <p>
 * This implementation provides functionality for the following common external
 * data fields: id, date_created, date_updated, and user_id.
 * 
 * @author roy.terrell
 * 
 */
public abstract class AncestorQueryCriteria extends RMT2BaseBean {

    private static final long serialVersionUID = 5535242853654987403L;

    private String qry_Id;

    private String qry_DateCreated;

    private String qry_DateUpdated;

    private String qry_UserId;

    /**
     * Default constructor.
     * 
     * @throws SystemException
     */
    public AncestorQueryCriteria() throws SystemException {
        super();
        return;
    }

    /**
     * Insitializes all properties to spaces. This will prevent the JSP client
     * from having to include speceial logic to check for null values in order
     * to suppress the display of the value, "null" in the input controls.
     * 
     * @throws SystemException
     */
    public void initBean() throws SystemException {
        this.qry_Id = "";
        this.qry_DateCreated = "";
        this.qry_DateUpdated = "";
        this.qry_UserId = "";
    }

    /**
     * Sets the value of the id property
     * 
     * @param value
     *            to assign.
     */
    public void setQry_Id(String value) {
        this.qry_Id = value;
    }

    /**
     * Get the value of the id property
     * 
     * @return value of id
     */
    public String getQry_Id() {
        return this.qry_Id;
    }

    /**
     * Set the value of date created property.
     * 
     * @param value
     *            to assign.
     */
    public void setQry_DateCreated(String value) {
        this.qry_DateCreated = value;
    }

    /**
     * Get the value of date created property.
     * 
     * @return value date created property.
     */
    public String getQry_DateCreated() {
        return this.qry_DateCreated;
    }

    /**
     * Set the value of date updated property.
     * 
     * @param value
     *            to assign.
     */
    public void setQry_DateUpdated(String value) {
        this.qry_DateUpdated = value;
    }

    /**
     * Get the value of date updated property.
     * 
     * @return value of date updated property.
     */
    public String getQry_DateUpdated() {
        return this.qry_DateUpdated;
    }

    /**
     * Set the value of user id property.
     * 
     * @param value
     *            to assign.
     */
    public void setQry_UserId(String value) {
        this.qry_UserId = value;
    }

    /**
     * Get the value of userid property.
     * 
     * @return value of user id property.
     */
    public String getQry_UserId() {
        return this.qry_UserId;
    }

}