package com.ui.components.table;

import java.util.List;

import javax.swing.table.TableModel;

/**
 * A table model interface which serves as an extension of {@link TableModel}.
 * <p>
 * It provides the ability to return an arbitrary object from JTables model
 * based on its index.
 * 
 * @author rterrell
 *
 */
public interface DataGridModel extends TableModel {

    /**
     * Obtain the data object from the TableModel's List indexed at
     * <i>rowIndex</i>.
     * <p>
     * If the contents of the view has been resorted, then <i>rowIndex</i> is
     * mapped to the underlying table model's index. If the view contents have
     * not been sorted then the model and view indices are the same.
     * 
     * @param rowIndex
     *            the index of the row in the view that is to be mapped to the
     *            row in the table model.
     * @return an arbitrary object indexed from the taable model's List.
     */
    Object getSelectedRowData(int rowIndex);

    /**
     * Adds a row to the table model.
     * 
     * @param rowData
     * @return int the row index of the item added or -1 if <i>rowData</i> is
     *         null;
     */
    int addRow(Object rowData);

    /**
     * Deletes a row from the table model.
     * 
     * @param rowIndex
     * @return int 1 is returned when the row is deleted successfully or -1 if
     *         the delete operation failed.
     */
    int removeRow(int rowIndex);

    /**
     * Return the list of column definition configurations.
     * 
     * @return List of {@link TableColumnDefinition}
     */
    List<TableColumnDefinition> getColumnDefinitions();

    /**
     * Returns the data buffer of modified and new objects in the view.
     * 
     * @return List
     */
    List<?> getViewBuf();

    /**
     * Returns the data buffer of all deleted objects.
     * 
     * @return List
     */
    List<?> getDeletedBuf();

}
