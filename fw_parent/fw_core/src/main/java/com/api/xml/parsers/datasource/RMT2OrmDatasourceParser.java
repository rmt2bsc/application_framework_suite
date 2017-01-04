package com.api.xml.parsers.datasource;

import com.SystemException;
import com.api.persistence.db.orm.bean.ObjectMapperAttrib;

/**
 * Interface for reading in an ORM Datasource configuration and parsing its
 * contents into an attribute mapping.
 * <p>
 * RMT2OrmDatasourceParser is the interface that the ORM Datasource parser must
 * implement. The
 * {@link com.api.xml.parsers.datasource.RMT2OrmDatasourceParser#parseDocument()
 * parseDocument} method must operate in a synchronous fashion, waiting for the
 * entire configuration to be processed and returning the results of the parse
 * operation as an instance of {@link bean.db.ObjectMapperAttrib
 * ObjectMapperAttrib}.
 * 
 * @author appdev
 * 
 */
public interface RMT2OrmDatasourceParser {

    /**
     * Performs any environment initialization needed to ensure that the
     * configuration is parsed successfully.
     */
    void getResources();

    /**
     * Parses the ORM Datasource configuration into an instance of
     * ObjectMapperAttrib
     * 
     * @return {@link bean.db.ObjectMapperAttrib ObjectMapperAttrib}
     * @throws SystemException
     */
    ObjectMapperAttrib parseDocument() throws SystemException;
}
