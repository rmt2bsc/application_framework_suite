package com.api.persistence.db.orm.query;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;

import com.SystemException;
import com.api.DaoApi;
import com.api.DataSourceApi;
import com.api.persistence.db.orm.bean.ObjectMapperAttrib;

/**
 * Abstract query builder class that provides common functionality for all ORM
 * based query builder subclasses.
 * 
 * @author RTerrell
 * 
 */
class AbstractOrmQuery extends AbstractQueryBuilder {
    private static final long serialVersionUID = -5313515187553376358L;

    private Logger logger;

    protected ObjectMapperAttrib dsAttr;

    /**
     * Constructs an AbstractOrmQuery object which ensures that <i>src</i> is a
     * valid runtime type of DaoApi or DataSourceApi.
     * 
     * @param src
     *            {@link com.api.DaoApi DaoApi} or a
     *            {@link com.api.DataSourceApi DataSourceApi}.
     * @throws SystemException
     *             if <i>src</i> is invalid.
     */
    public AbstractOrmQuery(Object src) throws SystemException {
        super(src);
        this.logger = Logger.getLogger("AbstractOrmQuery");

        if (this.getSrc() instanceof DaoApi) {
            dsAttr = ((DaoApi) this.getSrc()).getDataSourceAttib();
        }
        else if (this.getSrc() instanceof DataSourceApi) {
            dsAttr = ((DataSourceApi) this.getSrc()).getDataSourceAttib();
        }
        else {
            this.msg = "Instantiation of ORM SQL Builder class failed due to an invalid Dao interface";
            this.logger.log(Level.ERROR, this.msg);
            throw new SystemException(this.msg);
        }
    }

}
