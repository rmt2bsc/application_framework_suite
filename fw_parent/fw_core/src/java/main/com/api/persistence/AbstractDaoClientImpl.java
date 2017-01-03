package com.api.persistence;

import com.RMT2Base;
import com.api.persistence.db.orm.Rmt2OrmClientFactory;

/**
 * Abstract implementation of the {@link DaoClient} interface which functions to
 * create the internal {@link PersistenceClient} instance and closes the data
 * connection associated with the PersistenceClient.
 * 
 * @author rterrell
 * 
 */
public abstract class AbstractDaoClientImpl extends RMT2Base implements
        DaoClient {

    protected String apiName;

    /**
     * The login id of the user who's accessing and manipulating the DaoClient.
     */
    protected String updateUserId;

    /**
     * The ORM mapping interface used to query, insert, update, and delete
     * information to an from the database.
     */
    protected PersistenceClient client;

    private boolean borrowedPersistClient;

    /**
     * Create an AbstractDaoClientImpl which in turn creates the internal data
     * mapping client.
     */
    public AbstractDaoClientImpl() {
        super();
        // this.client = Rmt2OrmClientFactory
        // .createOrmClientInstance(this.apiName);
        this.borrowedPersistClient = false;
        return;
    }

    /**
     * Creates a AbstractDaoClientImpl by targeting a particular JDBC context.
     * 
     * @param contextName
     *            The name of the JDBC source. This could be a JDBC Datasource name, the 
     *            name of the properties file containing the JDBC cnfiguration , and etc.
     * @throws CannotCreateClientException
     *             <i>clinet</i> is null
     */
    public AbstractDaoClientImpl(String contextName) {
        this();
        this.client = Rmt2OrmClientFactory.createOrmClientInstance(contextName);
        this.borrowedPersistClient = false;
        return;
    }

    /**
     * Creates a AbstractDaoClientImpl using an existing PersistienceClient
     * object.
     * 
     * @param client
     *            an instance of {@link PersistenceClient}
     * @throws CannotCreateClientException
     *             <i>clinet</i> is null
     */
    public AbstractDaoClientImpl(PersistenceClient client) {
        this();
        if (client == null) {
            throw new CannotCreateClientException(
                    "Unable to create DAO...connection is invalid or null");
        }
        this.client = client;
        this.borrowedPersistClient = true;
        return;
    }

    /**
     * Close the connection pertaining to the internal {@link PersistenceClient}
     * .
     */
    @Override
    public void close() {
        // Do not try to close connection if we are using someone else's
        // connection.
        if (!this.borrowedPersistClient) {
            if (this.client != null) {
                this.client.close();
            }
        }
        this.client = null;
    }

    /**
     * @return the updateUserId
     */
    public String getDaoUser() {
        return updateUserId;
    }

    /**
     * @param updateUserId
     *            the updateUserId to set
     */
    public void setDaoUser(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.persistence.DaoClient#beginTrans()
     */
    @Override
    public void beginTrans() {
        if (this.client != null) {
            this.client.beginTrans();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.persistence.DaoClient#commitTrans()
     */
    @Override
    public void commitTrans() {
        if (this.client != null) {
            this.client.commitTrans();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.persistence.DaoClient#rollbackTrans()
     */
    @Override
    public void rollbackTrans() {
        if (this.client != null) {
            this.client.rollbackTrans();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.persistence.DaoClient#getClient()
     */
    @Override
    public PersistenceClient getClient() {
        return this.client;
    }

    // /*
    // * (non-Javadoc)
    // *
    // * @see com.api.persistence.DaoClient#getApiName()
    // */
    // @Override
    // public String getApiName() {
    // return null;
    // }

}
