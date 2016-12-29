package com.api.jsp.taglib.bean.dao;

import com.SystemException;
import com.api.DaoApi;

import com.api.persistence.db.orm.DataSourceConverter;

/**
 * Factory for creating java bean resources to be managed by the
 * {@link com.api.DaoApi DaoApi} interface.
 * 
 * @author roy.terrell
 * 
 */
public class BeanDaoFactory extends DataSourceConverter {

    public static DaoApi createApi() {
        try {
            DaoApi api = new BeanDaoImpl();
            return api;
        } catch (SystemException e) {
            return null;
        }
    }

}