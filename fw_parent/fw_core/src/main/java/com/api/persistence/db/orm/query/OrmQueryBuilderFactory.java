package com.api.persistence.db.orm.query;

import com.RMT2Base;
import com.SystemException;
import com.api.persistence.db.orm.OrmBean;

/**
 * Factory for creating various ORM based query builder objects.
 * 
 * @author RTerrell
 * 
 */
public class OrmQueryBuilderFactory extends RMT2Base {

    /**
     * Create OrmSqlSelectQuery instance and initialize it with a
     * ObjectMapperAttrib object.
     * 
     * @param src
     *            {@link com.api.DaoApi DaoApi}
     * @return {@link OrmSqlSelectQuery}
     */
    public static OrmSqlSelectQuery getSelectQuery(Object src) {
        try {
            OrmSqlSelectQuery obj = new OrmSqlSelectQuery(src);
            return obj;
        } catch (SystemException e) {
            return null;
        }
    }

    /**
     * Create OrmSqlCriteria instance and initialize it with a Dao object and a
     * target ORM POJO.
     * 
     * @param src
     *            {@link com.api.DaoApi DaoApi}
     * @param pojo
     *            {@link bean.OrmBean OrmBean}
     * @return {@link com.api.db.query.OrmSqlCriteria OrmSqlCriteria}
     */
    public static OrmSqlCriteria getCriteriaQuery(Object src, OrmBean pojo) {
        try {
            OrmSqlCriteria obj = new OrmSqlCriteria(src, pojo);
            return obj;
        } catch (SystemException e) {
            return null;
        }
    }

    /**
     * Create OrmSqlOrdering instance and initialize it with a Dao object and a
     * target ORM POJO.
     * 
     * @param src
     *            {@link com.api.DaoApi DaoApi}
     * @param pojo
     *            {@link bean.OrmBean OrmBean}
     * @return {@link com.api.db.query.OrmSqlOrdering OrmSqlOrdering}
     */
    public static OrmSqlOrdering getOrderQuery(Object src, OrmBean pojo) {
        try {
            OrmSqlOrdering obj = new OrmSqlOrdering(src, pojo);
            return obj;
        } catch (SystemException e) {
            return null;
        }
    }

    /**
     * Create OrmSqlInsertQuery instance and initialize it with a Dao object, a
     * target ORM POJO, and an auto key generate boolean indicator.
     * 
     * @param src
     *            {@link com.api.DaoApi DaoApi}
     * @param pojo
     *            {@link bean.OrmBean OrmBean}
     * @param autoKey
     *            Contain a value of true when auto key generate is desired and
     *            false, otherwise.
     * @return {@link OrmSqlInsertQuery}
     */
    public static OrmSqlInsertQuery getInsertQuery(Object src, OrmBean pojo,
            boolean autoKey) {
        try {
            OrmSqlInsertQuery obj = new OrmSqlInsertQuery(src, pojo, autoKey);
            return obj;
        } catch (SystemException e) {
            return null;
        }
    }

    /**
     * Create OrmSqlUpdateQuery instance and initialize it with a Dao object and
     * a target ORM POJO.
     * 
     * @param src
     *            {@link com.api.DaoApi DaoApi}
     * @param pojo
     *            {@link bean.OrmBean OrmBean}
     * @return {@link OrmSqlUpdateQuery}
     */
    public static OrmSqlUpdateQuery getUpdateQuery(Object src, OrmBean pojo) {
        try {
            OrmSqlUpdateQuery obj = new OrmSqlUpdateQuery(src, pojo);
            return obj;
        } catch (SystemException e) {
            return null;
        }
    }

    /**
     * Create OrmSqlDeleteQuery instance and initialize it with a Dao object and
     * a target ORM POJO.
     * 
     * @param src
     *            {@link com.api.DaoApi DaoApi}
     * @param pojo
     *            {@link bean.OrmBean OrmBean}
     * @return {@link OrmSqlDeleteQuery}
     */
    public static OrmSqlDeleteQuery getDeleteQuery(Object src, OrmBean pojo) {
        try {
            OrmSqlDeleteQuery obj = new OrmSqlDeleteQuery(src, pojo);
            return obj;
        } catch (SystemException e) {
            return null;
        }
    }
}
