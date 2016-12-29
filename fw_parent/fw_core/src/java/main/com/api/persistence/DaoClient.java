package com.api.persistence;

/**
 * The base interface in which all application DAO's should implement or
 * inherit.
 * 
 * @author Roy Terrell
 * 
 */
public interface DaoClient {

    /**
     * Return the underlying connection of this Dao client.
     * 
     * @return an instance of {@link PersistenceClient}
     */
    PersistenceClient getClient();

    /**
     * Close the connection assoicated with this DAO client.
     */
    void close();

    /**
     * Returns the login id of the user who is manipulating the <i>DaoClient</i>
     * 
     * @return login id
     */
    String getDaoUser();

    /**
     * Sets the login id of the user who is manipulating the <i>DaoClient</i>.
     * 
     * @param userName
     *            login id
     */
    void setDaoUser(String userName);

    /**
     * Starts a unit of work for one or more transactions.
     */
    void beginTrans();

    /**
     * Commits one or more transactions which make up a unit of work.
     */
    void commitTrans();

    /**
     * Rollback one or more transactions which make up a unit of work.
     */
    void rollbackTrans();

    // String getApiName();
}
