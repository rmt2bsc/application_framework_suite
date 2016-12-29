package com.api.persistence.db;

import com.SystemException;
import com.api.persistence.DatabaseException;
import com.api.persistence.db.orm.DataSourceConverter;

/**
 * Factory for creating DynamicSqlApi objects.
 * 
 * @author roy.terrell
 * 
 */
public class DynamicSqlFactory extends DataSourceConverter {

    /**
     * Creates a new instance of DynamicSqlApi using a
     * {@link DatabaseConnectionBean}.
     * 
     * @param _dbo
     *            The connection bean
     * @return {@link DynamicSqlApi}
     */
    public static DynamicSqlApi create(DatabaseConnectionBean _dbo) {
        try {
            DynamicSqlApi api = new DynamicSqlImpl(_dbo.getNativeConnection(),
                    _dbo.getDbUserId());
            api.setConnector(_dbo);
            return api;
        } catch (SystemException e) {
            return null;
        } catch (DatabaseException d) {
            return null;
        }
    }

}
