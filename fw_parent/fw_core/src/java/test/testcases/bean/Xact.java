package testcases.bean;

import java.util.Date;

import com.SystemException;
import com.api.persistence.db.orm.OrmBean;

/**
 * Peer object that maps to the xact database table/view.
 * 
 * 
 */
public class Xact extends OrmBean {

    // Property name constants that belong to respective DataSource, XactView

    /**
     * The property name constant equivalent to property, XactId, of respective
     * DataSource view.
     */
    public static final String PROP_XACTID = "XactId";
    /**
     * The property name constant equivalent to property, XactTypeId, of
     * respective DataSource view.
     */
    public static final String PROP_XACTTYPEID = "XactTypeId";
    /**
     * The property name constant equivalent to property, XactSubtypeId, of
     * respective DataSource view.
     */
    public static final String PROP_XACTSUBTYPEID = "XactSubtypeId";
    /**
     * The property name constant equivalent to property, XactDate, of
     * respective DataSource view.
     */
    public static final String PROP_XACTDATE = "XactDate";
    /**
     * The property name constant equivalent to property, XactAmount, of
     * respective DataSource view.
     */
    public static final String PROP_XACTAMOUNT = "XactAmount";
    /**
     * The property name constant equivalent to property, TenderId, of
     * respective DataSource view.
     */
    public static final String PROP_TENDERID = "TenderId";
    /**
     * The property name constant equivalent to property, NegInstrNo, of
     * respective DataSource view.
     */
    public static final String PROP_NEGINSTRNO = "NegInstrNo";
    /**
     * The property name constant equivalent to property, BankTransInd, of
     * respective DataSource view.
     */
    public static final String PROP_BANKTRANSIND = "BankTransInd";
    /**
     * The property name constant equivalent to property, ConfirmNo, of
     * respective DataSource view.
     */
    public static final String PROP_CONFIRMNO = "ConfirmNo";
    /**
     * The property name constant equivalent to property, EntityRefNo, of
     * respective DataSource view.
     */
    public static final String PROP_ENTITYREFNO = "EntityRefNo";
    /**
     * The property name constant equivalent to property, PostedDate, of
     * respective DataSource view.
     */
    public static final String PROP_POSTEDDATE = "PostedDate";
    /**
     * The property name constant equivalent to property, Reason, of respective
     * DataSource view.
     */
    public static final String PROP_REASON = "Reason";
    /**
     * The property name constant equivalent to property, DateCreated, of
     * respective DataSource view.
     */
    public static final String PROP_DATECREATED = "DateCreated";
    /**
     * The property name constant equivalent to property, DateUpdated, of
     * respective DataSource view.
     */
    public static final String PROP_DATEUPDATED = "DateUpdated";
    /**
     * The property name constant equivalent to property, UserId, of respective
     * DataSource view.
     */
    public static final String PROP_USERID = "UserId";

    /** The javabean property equivalent of database column xact.xact_id */
    private int xactId;
    /** The javabean property equivalent of database column xact.xact_type_id */
    private int xactTypeId;
    /** The javabean property equivalent of database column xact.xact_subtype_id */
    private int xactSubtypeId;
    /** The javabean property equivalent of database column xact.xact_date */
    private java.util.Date xactDate;
    /** The javabean property equivalent of database column xact.xact_amount */
    private double xactAmount;
    /** The javabean property equivalent of database column xact.tender_id */
    private int tenderId;
    /** The javabean property equivalent of database column xact.neg_instr_no */
    private String negInstrNo;
    /** The javabean property equivalent of database column xact.bank_trans_ind */
    private String bankTransInd;
    /** The javabean property equivalent of database column xact.confirm_no */
    private String confirmNo;
    /** The javabean property equivalent of database column xact.entity_ref_no */
    private String entityRefNo;
    /** The javabean property equivalent of database column xact.posted_date */
    private java.util.Date postedDate;
    /** The javabean property equivalent of database column xact.reason */
    private String reason;
    /** The javabean property equivalent of database column xact.date_created */
    private java.util.Date dateCreated;
    /** The javabean property equivalent of database column xact.date_updated */
    private java.util.Date dateUpdated;
    /** The javabean property equivalent of database column xact.user_id */
    private String userId;

    // Getter/Setter Methods

    /**
     * Default constructor.
     * 
     * 
     */
    public Xact() throws SystemException {
        super();
    }

    /**
     * Sets the value of member variable xactId
     * 
     * 
     */
    public void setXactId(int value) {
        this.xactId = value;
    }

    /**
     * Gets the value of member variable xactId
     * 
     * 
     */
    public int getXactId() {
        return this.xactId;
    }

    /**
     * Sets the value of member variable xactTypeId
     * 
     * 
     */
    public void setXactTypeId(int value) {
        this.xactTypeId = value;
    }

    /**
     * Gets the value of member variable xactTypeId
     * 
     * 
     */
    public int getXactTypeId() {
        return this.xactTypeId;
    }

    /**
     * Sets the value of member variable xactSubtypeId
     * 
     * 
     */
    public void setXactSubtypeId(int value) {
        this.xactSubtypeId = value;
    }

    /**
     * Gets the value of member variable xactSubtypeId
     * 
     * 
     */
    public int getXactSubtypeId() {
        return this.xactSubtypeId;
    }

    /**
     * Sets the value of member variable xactDate
     * 
     * 
     */
    public void setXactDate(java.util.Date value) {
        this.xactDate = value;
    }

    /**
     * Gets the value of member variable xactDate
     * 
     * 
     */
    public java.util.Date getXactDate() {
        return this.xactDate;
    }

    /**
     * Sets the value of member variable xactAmount
     * 
     * 
     */
    public void setXactAmount(double value) {
        this.xactAmount = value;
    }

    /**
     * Gets the value of member variable xactAmount
     * 
     * 
     */
    public double getXactAmount() {
        return this.xactAmount;
    }

    /**
     * Sets the value of member variable tenderId
     * 
     * 
     */
    public void setTenderId(int value) {
        this.tenderId = value;
    }

    /**
     * Gets the value of member variable tenderId
     * 
     * 
     */
    public int getTenderId() {
        return this.tenderId;
    }

    /**
     * Sets the value of member variable negInstrNo
     * 
     * 
     */
    public void setNegInstrNo(String value) {
        this.negInstrNo = value;
    }

    /**
     * Gets the value of member variable negInstrNo
     * 
     * 
     */
    public String getNegInstrNo() {
        return this.negInstrNo;
    }

    /**
     * Sets the value of member variable bankTransInd
     * 
     * 
     */
    public void setBankTransInd(String value) {
        this.bankTransInd = value;
    }

    /**
     * Gets the value of member variable bankTransInd
     * 
     * 
     */
    public String getBankTransInd() {
        return this.bankTransInd;
    }

    /**
     * Sets the value of member variable confirmNo
     * 
     * 
     */
    public void setConfirmNo(String value) {
        this.confirmNo = value;
    }

    /**
     * Gets the value of member variable confirmNo
     * 
     * 
     */
    public String getConfirmNo() {
        return this.confirmNo;
    }

    /**
     * Sets the value of member variable entityRefNo
     * 
     * 
     */
    public void setEntityRefNo(String value) {
        this.entityRefNo = value;
    }

    /**
     * Gets the value of member variable entityRefNo
     * 
     * 
     */
    public String getEntityRefNo() {
        return this.entityRefNo;
    }

    /**
     * Sets the value of member variable postedDate
     * 
     * 
     */
    public void setPostedDate(java.util.Date value) {
        this.postedDate = value;
    }

    /**
     * Gets the value of member variable postedDate
     * 
     * 
     */
    public java.util.Date getPostedDate() {
        return this.postedDate;
    }

    /**
     * Sets the value of member variable reason
     * 
     * 
     */
    public void setReason(String value) {
        this.reason = value;
    }

    /**
     * Gets the value of member variable reason
     * 
     * 
     */
    public String getReason() {
        return this.reason;
    }

    /**
     * Sets the value of member variable dateCreated
     * 
     * 
     */
    public void setDateCreated(java.util.Date value) {
        this.dateCreated = value;
    }

    /**
     * Gets the value of member variable dateCreated
     * 
     * 
     */
    public java.util.Date getDateCreated() {
        return this.dateCreated;
    }

    /**
     * Sets the value of member variable dateUpdated
     * 
     * 
     */
    public void setDateUpdated(java.util.Date value) {
        this.dateUpdated = value;
    }

    /**
     * Gets the value of member variable dateUpdated
     * 
     * 
     */
    public java.util.Date getDateUpdated() {
        return this.dateUpdated;
    }

    /**
     * Sets the value of member variable userId
     * 
     * 
     */
    public void setUserId(String value) {
        this.userId = value;
    }

    /**
     * Gets the value of member variable userId
     * 
     * 
     */
    public String getUserId() {
        return this.userId;
    }

    /**
     * Stubbed initialization method designed to implemented by developer.
     * 
     * 
     * 
     */
    public void initBean() throws SystemException {
    }
}