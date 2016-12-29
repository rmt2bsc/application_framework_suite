package com.api.persistence.db.orm.query;

import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;

import com.SystemException;
import com.api.Product;
import com.api.ProductBuilder;
import com.api.ProductBuilderException;
import com.api.ProductDirector;
import com.api.persistence.db.orm.OrmBean;
import com.api.persistence.db.orm.bean.TableUsageBean;

/**
 * This class provides complex processes to construct and deconstruct SQL delete
 * statements as {@link com.api.Product Product}.
 * 
 * @author RTerrell
 * 
 */
class OrmSqlDeleteQuery extends AbstractOrmQuery implements ProductBuilder {
    private static final long serialVersionUID = 1874037456487871309L;

    private Logger logger;

    private OrmBean pojo;

    /**
     * Constructs a OrmSqlUpdateQuery with DaoApi data source and the tartget
     * POJO object.
     * 
     * @param src
     *            {@link com.api.DaoApi DaoApi}
     * @param pojo
     *            {@link bean.OrmBean OrmBean}
     * @throws SystemException
     */
    public OrmSqlDeleteQuery(Object src, OrmBean pojo) throws SystemException {
        super(src);
        this.pojo = pojo;
        this.logger = Logger.getLogger("OrmSqlDeleteQuery");
    }

    /**
     * Constructs a SQL update statement from an ORM data source.
     * 
     * @throws ProductBuilderException
     */
    public void assemble() throws ProductBuilderException {
        this.logger.log(Level.DEBUG, "Assembling ORM Delete Statement");
        StringBuffer deleteSql = new StringBuffer(100);
        String sql = null;
        Hashtable tables = null;
        Enumeration tempEnum = null;
        TableUsageBean tableBean = null;

        // Get Table data
        tables = this.dsAttr.getTables();
        tempEnum = tables.elements();
        if (tempEnum.hasMoreElements()) {
            tableBean = (TableUsageBean) tempEnum.nextElement();
            deleteSql.append("Delete From  ");
            deleteSql.append(tableBean.getDbName());
        }

        // Build where clause.
        String whereSql;
        try {
            ProductBuilder builder = OrmQueryBuilderFactory.getCriteriaQuery(
                    this.getSrc(), pojo);
            Product sqlObj = ProductDirector.construct(builder);
            whereSql = sqlObj.toString();
        } catch (ProductBuilderException e) {
            whereSql = null;
        }

        // String together the three parts of the where statement.
        sql = deleteSql.toString()
                + (whereSql == null ? "" : " where " + whereSql);

        this.setQueryString(sql);
        this.setQueryComp(this.dsAttr);
    }

    /**
     * No implementation for disassebling an SQL delete.
     */
    public void disAssemble() throws ProductBuilderException {
        return;
    }

}
