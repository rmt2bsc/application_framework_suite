package com.nv.dao;

import com.RMT2Base;
import com.api.persistence.DaoClient;

/**
 * Common business object designed to provided transactional functionality for
 * Swing components operating against a known data source.
 * 
 * @author rterrell
 *
 */
public class SwingDaoTransaction extends RMT2Base {

    private DaoClient baseDao;

    /**
     * 
     */
    protected SwingDaoTransaction() {
        return;
    }

    /**
     * Close the connection assoicated with this DAO client.
     */
    public void close() {
        if (this.baseDao == null) {
            return;
        }
        this.baseDao.close();
    }

    /**
     * Starts a unit of work for one or more transactions.
     */
    public void beginTrans() {
        if (this.baseDao == null) {
            return;
        }
        this.baseDao.beginTrans();
    }

    /**
     * Commits one or more transactions which make up a unit of work.
     */
    public void commitTrans() {
        if (this.baseDao == null) {
            return;
        }
        this.baseDao.commitTrans();
    }

    /**
     * Rollback one or more transactions which make up a unit of work.
     */
    public void rollbackTrans() {
        if (this.baseDao == null) {
            return;
        }
        this.baseDao.rollbackTrans();
    }

    /**
     * @param baseDao
     *            the baseDao to set
     */
    public void setBaseDao(DaoClient baseDao) {
        this.baseDao = baseDao;
    }

}
