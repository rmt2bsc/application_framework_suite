package com.api.persistence.db.orm.query;

import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.SystemException;
import com.api.DataSourceApi;
import com.api.ProductBuilder;
import com.api.ProductBuilderException;
import com.api.persistence.db.orm.OrmBean;
import com.api.persistence.db.orm.bean.DataSourceColumn;
import com.api.persistence.db.orm.bean.TableUsageBean;

/**
 * This class provides complex processes to construct and deconstruct SQL insert
 * statements as {@link com.api.Product Product}.
 * 
 * @author RTerrell
 * 
 */
class OrmSqlInsertQuery extends AbstractOrmQuery implements ProductBuilder {
    private static final long serialVersionUID = 523429398759741833L;

    private Logger logger;

    private OrmBean pojo;

    private boolean autoKey;

    /**
     * Constructs a OrmSqlInsertQuery with DaoApi data source, the tartget POJO
     * object, and the auto primary key generate indicator.
     * 
     * @param src
     *            {@link com.api.DaoApi DaoApi}
     * @param pojo
     *            {@link bean.OrmBean OrmBean}
     * @param autoKey
     *            boolean indicating genreate primary key when true and
     *            otherwise, when false.
     * @throws SystemException
     */
    public OrmSqlInsertQuery(Object src, OrmBean pojo, boolean autoKey)
            throws SystemException {
        super(src);
        this.pojo = pojo;
        this.autoKey = autoKey;
        this.logger = Logger.getLogger("OrmSqlInsertQuery");
    }

    /**
     * Constructs a SQL insert statement from an ORM data source.
     * 
     * @throws ProductBuilderException
     */
    public void assemble() throws ProductBuilderException {
        this.logger.log(Level.DEBUG, "Assembling ORM Insert Statement");
        StringBuffer insertSql = new StringBuffer(100);
        StringBuffer colSql = new StringBuffer(100);
        StringBuffer valSql = new StringBuffer(100);
        String sql = null;
        Hashtable tables = null;
        Enumeration tempEnum = null;
        TableUsageBean tableBean = null;
        DataSourceColumn colBean = null;
        int cnt = 0;

        // Get Table data
        tables = this.dsAttr.getTables();
        tempEnum = tables.elements();
        if (tempEnum.hasMoreElements()) {
            tableBean = (TableUsageBean) tempEnum.nextElement();
            insertSql.append("Insert into ");
            insertSql.append(tableBean.getDbName());
        }

        // Get Column data
        tempEnum = this.dsAttr.getColumnDef().elements();
        while (tempEnum.hasMoreElements()) {
            colBean = (DataSourceColumn) tempEnum.nextElement();

            // do not include primary key if it is supposed to be auto generated
            // by the database.
            if (colBean.isPrimaryKey() && autoKey) {
                continue;
            }

            // Check if user intended for column to be set as null.
            if (this.pojo.isNull(colBean.getName())) {
                colBean.setDataValue(OrmBean.DB_NULL);
            }

            // Build the column list and values list
            if (cnt++ > 0) {
                colSql.append(", ");
                valSql.append(", ");
            }
            colSql.append(colBean.getDbName());
            valSql.append(((DataSourceApi) this.getSrc())
                    .getSqlColumnValue(colBean));
        } // end while

        // String together the three parts of the insert statement.
        sql = insertSql.toString() + " (" + colSql.toString() + ")  values ("
                + valSql.toString() + ") ";
        this.setQueryString(sql);
        this.setQueryComp(this.dsAttr);
    }

    /**
     * No implementation for disassebling an SQL insert.
     */
    public void disAssemble() throws ProductBuilderException {
        return;
    }

}
