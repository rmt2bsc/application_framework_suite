package com.api;

import java.util.List;

import com.SystemException;

/**
 * @author RTerrell
 * 
 */
public interface BaseDataSource {
    /**
     * Set the base DataSource view name.
     * 
     * @param value
     *            Name of Datasource view.
     * @return The previous datasource view name.
     */
    String setBaseView(String value);

    /**
     * Set the base DataSource class name.
     * 
     * @param value
     *            Name of Datasource class.
     * @return The previous datasource class name.
     */
    String setBaseClass(String value);

    /**
     * Get current DataSource base view name.
     * 
     * @return String
     */
    String getBaseView();

    /**
     * Get current DataSource base class name.
     * 
     * @return String
     */
    String getBaseClass();

    /**
     * Generic method that can be used to retrieve data in any form using custom
     * selection criteria. The user is responsible for setting the proper
     * datasource view and class as well as applying the proper cast on the
     * returned value.
     * 
     * @param _whereClause
     *            SQL where clasue to provide a condition used to restrict the
     *            result set of data.
     * @param _orderByClause
     *            SQL order by clasue.
     * @return An ArrayList of any type of object.
     * @throws SystemException
     */
    List findData(String _whereClause, String _orderByClause)
            throws SystemException;

    /**
     * Realeases any allocated resources.
     * 
     */
    void close();
}
