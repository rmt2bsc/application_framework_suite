package com.api.persistence.db.orm;

import java.util.List;

import org.apache.log4j.Logger;

import com.NotFoundException;
import com.SystemException;
import com.api.DataSourceApi;
import com.api.persistence.DatabaseException;
import com.api.util.RMT2BeanUtility;
import com.api.util.RMT2Utility;

/**
 * A Thread derivative class that is used to transfer the data of a large
 * Datasource object to its respective bean.
 * 
 * @author rterrell
 * 
 */
public class DatasourceToBeanConverterThread extends Thread {

    private static final Logger logger = Logger
            .getLogger(DatasourceToBeanConverterThread.class);

    private DataSourceApi dso;

    private List<OrmBean> list;

    private int actualProcessCount = 0;

    public DatasourceToBeanConverterThread(DataSourceApi dso, List<OrmBean> list) {
        this.dso = dso;
        this.list = list;
        this.setName("Resultset-ORM-Converter");
    }

    /**
     * Cycles through the {@link DataSourceApi} instance and transfers the data
     * from each row to an element in the List of {@link OrmBean} derived
     * objects that is managed internally to this class instance.
     */
    public void run() {
        try {
            int ndx = 0;
            while (dso.nextRow()) {
                OrmBean newBean = null;
                try {
                    newBean = this.list.get(ndx);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                packageBean(dso, newBean);
                ndx++;
                this.actualProcessCount++;
            }
        } catch (DatabaseException e) {
            logger.warn(e);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            throw new RuntimeException(
                    "Error proceesing Datasource To Bean converstion", e);
        }
        return;
    }

    /**
     * Transfers the contents of the current row in <i>dso</i> to the
     * <i>OrmBean</i> derived object, <i>bean</i>.
     * 
     * @param dso
     *            An instance of {@link DataSourceApi} in which the current row
     *            is targeted for data transfer.
     * @param bean
     *            An instance of {@link OrmBean} which serves as the destination
     *            of the data transfer.
     * @return The actual number of properties successfully processed.
     * @throws SystemException
     */
    public int packageBean(DataSourceApi _dso, Object _bean)
            throws SystemException {
        String property = null;
        Object value = null;
        List<String> props = null;

        if (_dso == null) {
            throw new SystemException(
                    "Datasource is not valid or does not have a valid connection object");
        }

        // Setup the Bean Utility for _bean
        RMT2BeanUtility srcBeanUtil = new RMT2BeanUtility(_bean);

        // Retrieve all property names associated with _bean
        props = srcBeanUtil.getPropertyNames();

        int actualPropsProcessed = 0;
        try {
            String msg = null;
            // Obtain the value of each property name of the datasource
            // and assign that value to the respective property name of _bean.
            for (int ndx = 0; ndx < props.size(); ndx++) {
                try {
                    property = RMT2Utility.getBeanMethodName(props.get(ndx)
                            .toString());
                    if (property == null) {
                        msg = "packageBean encountered a null property";
                        DatasourceToBeanConverterThread.logger.error(msg);
                        throw new NotFoundException(msg);
                    }
                    Class clazz = srcBeanUtil.getPropertyType(property);
                    String name = null;
                    if (clazz != null) {
                        name = clazz.getName();
                    }
                    if (name != null && name.equals("java.io.InputStream")) {
                        value = _dso.getColumnBinaryValue(property);
                    }
                    else {
                        value = _dso.getColumnValue(property);
                    }
                    srcBeanUtil.setPropertyValue(property, value);
                    DatasourceToBeanConverterThread.logger
                            .warn("packageBean Property: " + property);
                    DatasourceToBeanConverterThread.logger
                            .warn("packageBean Value: " + value);
                    actualPropsProcessed++;
                } catch (NotFoundException e) {
                    // Go to next property if the property does not exist in the
                    // datasource
                    DatasourceToBeanConverterThread.logger.warn(e.getMessage()
                            + " - Property: " + property);
                    continue;
                }
            }
            return actualPropsProcessed;
        } catch (DatabaseException e) {
            throw new SystemException(e);
        }
    }

    /**
     * Determines whether or not the actual number of rows processed is
     * equivalent to the actual number of beans destined to receive the data
     * transfer.
     * 
     * @return true when the the number of rows processed is equal to the
     *         expected number beans to receive the data. False is returned when
     *         the two subjects are not equal.
     */
    public boolean isCompleted() {
        return (this.actualProcessCount + 1) == this.list.size();
    }

}
