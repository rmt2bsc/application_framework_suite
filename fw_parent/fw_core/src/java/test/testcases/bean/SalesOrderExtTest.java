package testcases.bean;

import java.util.List;

import com.RMT2Base;
import com.RMT2BaseBean;
import com.SystemException;

/**
 * Sales order graph class.
 * 
 * 
 */
public class SalesOrderExtTest extends RMT2BaseBean {

    private int salesOrderId;

    private int customerId;

    private int businessId;

    private int invoiced;

    private double orderTotal;

    private List items;

    private String reason;

    private java.util.Date dateCreated;

    private java.util.Date dateUpdated;

    private String userId;

    /**
     * Default constructor.
     * 
     * 
     */
    public SalesOrderExtTest() throws SystemException {
        super();
    }

    /**
     * Gets the value of member variable soId
     * 
     * 
     */
    public int getSalesOrderId() {
        return salesOrderId;
    }

    /**
     * Sets the value of member variable soId
     * 
     * 
     */
    public void setSalesOrderId(int salesOrderId) {
        this.salesOrderId = salesOrderId;
    }

    /**
     * Sets the value of member variable customerId
     * 
     * 
     */
    public void setCustomerId(int value) {
        this.customerId = value;
    }

    /**
     * Gets the value of member variable customerId
     * 
     * 
     */
    public int getCustomerId() {
        return this.customerId;
    }

    /**
     * Get business id
     * 
     * @return
     */
    public int getBusinessId() {
        return businessId;
    }

    /**
     * Set business id
     * 
     * @param businessId
     */
    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }

    /**
     * Sets the value of member variable invoiced
     * 
     * 
     */
    public void setInvoiced(int value) {
        this.invoiced = value;
    }

    /**
     * Gets the value of member variable invoiced
     * 
     * 
     */
    public int getInvoiced() {
        return this.invoiced;
    }

    /**
     * Sets the value of member variable orderTotal
     * 
     * 
     */
    public void setOrderTotal(double value) {
        this.orderTotal = value;
    }

    /**
     * Gets the value of member variable orderTotal
     * 
     * 
     */
    public double getOrderTotal() {
        return this.orderTotal;
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
     * @return the items
     */
    public List getItems() {
        return items;
    }

    /**
     * @param items
     *            the items to set
     */
    public void setItems(List items) {
        this.items = items;
    }

    /**
     * @return the reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * @param reason
     *            the reason to set
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /*
     * (non-Javadoc)
     * 
     * @see testcases.bean.RMT2BaseBean#initBean()
     */
    @Override
    public void initBean() throws SystemException {
        // TODO Auto-generated method stub

    }

}