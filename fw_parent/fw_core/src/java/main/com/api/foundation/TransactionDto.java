package com.api.foundation;

/**
 * A contract for managing timestamp attributes for transactions.
 * 
 * @author Roy Terrell
 * 
 */
public interface TransactionDto extends BaseDto {

    /**
     * Sets the date transaction was created.
     * 
     * @param date
     *            an instance of {@link java.util.Date}
     */
    void setDateCreated(java.util.Date date);

    /**
     * Gets the date transcation was created
     */
    java.util.Date getDateCreated();

    /**
     * Sets the date transacton was modifed.
     * 
     * @param date
     *            an instance of {@link java.util.Date}
     */
    void setDateUpdated(java.util.Date date);

    /**
     * Gets the date the transaction was modified.
     */
    java.util.Date getDateUpdated();

    /**
     * Sets the login id of the person or process that created or modified the
     * transaction.
     * 
     * @param userName
     *            an instance of {@link String}
     */
    void setUpdateUserId(String userName);

    /**
     * Gets the login id of the person or process that created or modified the
     * transaction.
     */
    String getUpdateUserId();

    /**
     * Sets the IP address of the owner creating a new record.
     * 
     * @param value
     *            String
     */
    void setIpCreated(String value);

    /**
     * Gets the IP address of the owner creating a new record.
     * 
     * @return String
     */
    String getIpCreated();

    /**
     * Sets the IP address of the owner updating an existing record.
     * 
     * @param value
     *            String
     */
    void setIpUpdated(String value);

    /**
     * Gets the IP address of the owner updating an existing record.
     * 
     * @return String
     */
    String getIpUpdated();

}
