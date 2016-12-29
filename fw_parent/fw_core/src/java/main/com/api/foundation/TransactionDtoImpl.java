package com.api.foundation;

import java.util.Date;

/**
 * Manages time stamp information for recording and tracking transactions.
 * <p>
 * This is the data transfer object (DTO) that is common to any API for
 * transfering data in between disparate applications.
 * 
 * @author rterrell
 * 
 */
public class TransactionDtoImpl extends AbstractBaseDtoImpl {

    /**
     * The user name of the person in control of the transaction.
     */
    protected String updateUserId;

    /**
     * The date representing the when the transaction was created.
     */
    protected Date dateCreated;

    /**
     * The date representing the when the transaction was modified.
     */
    protected Date dateUpdated;

    /**
     * The IP address representing the location of the user cretaing the
     * transaction.
     */
    protected String ipCreated;

    /**
     * The IP address representing the location of the user updating the
     * transaction.
     */
    protected String ipUpdated;

    /**
     * Create a TransactionDtoImpl without initializing its internal data
     * properties.
     */
    public TransactionDtoImpl() {
        return;
    }

    /**
     * A template method designed to assign its main member data object the
     * value of <i>obj</i>.
     * <p>
     * Optionally, the descendent can implement this method to setup its main
     * data object based on the runtime data type of <i>obj</i>.
     * 
     * @param obj
     *            an arbitrary object potentially suited as the main data object
     *            for the class.
     */
    protected void updateObjHeirarchy(Object obj) {
        return;
    }

    /**
     * Initializes the instance with complete time stamp details.
     * 
     * @param createDate
     *            the create date
     * @param updateDate
     *            the modified date
     * @param userId
     *            the login id of the user undergoing the update.
     */
    public void init(Date createDate, Date updateDate, String userId) {
        this.dateCreated = createDate;
        this.dateUpdated = updateDate;
        this.updateUserId = userId;
    }

    /**
     * Return the user name of the person in control of the transaction.
     * 
     * @return the user name.
     */
    public String getUpdateUserId() {
        return updateUserId;
    }

    /**
     * Set the user name of the person in control of the transaction.
     * 
     * @param updateUserId
     *            the user name
     */
    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    /**
     * Return the date representing the when the transaction was created.
     * 
     * @return the {@link Date}
     */
    public Date getDateCreated() {
        return dateCreated;
    }

    /**
     * Set the date representing the when the transaction was created.
     * 
     * @param dateCreated
     *            {@link Date}
     */
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * Return the date representing the when the transaction was modified.
     * 
     * @return {@link Date}
     */
    public Date getDateUpdated() {
        return dateUpdated;
    }

    /**
     * Set the date representing the when the transaction was modified.
     * 
     * @param dateUpdated
     *            {@link Date}
     */
    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    /**
     * Return the IP address representing the location of the user cretaing the
     * transaction.
     * 
     * @return String
     */
    public String getIpCreated() {
        return ipCreated;
    }

    /**
     * Set the IP address representing the location of the user cretaing the
     * transaction.
     * 
     * @param ipCreated
     *            String
     */
    public void setIpCreated(String ipCreated) {
        this.ipCreated = ipCreated;
    }

    /**
     * Return the IP address representing the location of the user updating the
     * transaction.
     * 
     * @return String
     */
    public String getIpUpdated() {
        return ipUpdated;
    }

    /**
     * Set the IP address representing the location of the user updating the
     * transaction.
     * 
     * @param ipUpdated
     *            String
     */
    public void setIpUpdated(String ipUpdated) {
        this.ipUpdated = ipUpdated;
    }

}
