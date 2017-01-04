package com.api.persistence.db.orm.bean;

import com.RMT2BaseBean;
import com.SystemException;

/**
 * This class managaes the properties of the TableUsage tag of the DataSource
 * view document.
 * 
 * @author roy.terrell
 * 
 */
public class TableUsageBean extends RMT2BaseBean {
    private static final long serialVersionUID = -6933415379257667005L;

    private String name;

    private String dbName;

    private boolean updateable;

    /**
     * Default constructor.
     * 
     * @throws SystemException
     */
    public TableUsageBean() throws SystemException {
        super();
    }

    /**
     * No Action.
     */
    public void initBean() {
    }

    /**
     * Get the view name of the table.
     * 
     * @return String.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set the view name of the table.
     * 
     * @param value
     *            Srting.
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Get database name of the table.
     * 
     * @return String
     */
    public String getDbName() {
        return this.dbName;
    }

    /**
     * Sets the database name of table.
     * 
     * @param value
     */
    public void setDbName(String value) {
        this.dbName = value;
    }

    /**
     * Get the table updateable indicator.
     * 
     * @return boolean
     */
    public boolean isUpdateable() {
        return this.updateable;
    }

    /**
     * Set the table updateable indicator.
     * 
     * @param value
     *            boolean.
     */
    public void setUpdateable(boolean value) {
        this.updateable = value;
    }
}
