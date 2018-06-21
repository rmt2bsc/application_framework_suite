package com.ui.components.table;

import java.awt.Component;
import java.util.Date;
import java.util.List;

import javax.swing.JTable;

import com.api.util.RMT2Date;

/**
 * A custom JTextField editor for managing date values of DataGrid cells.
 * 
 * @author rterrell
 *
 */
public class CustomDateFieldCellEditor extends CustomTextFieldCellEditor {

    private static final long serialVersionUID = -2722043276904040093L;

    public CustomDateFieldCellEditor() {
        super();
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ui.components.table.CustomTextFieldCellEditor#getTableCellEditorComponent
     * (javax.swing.JTable, java.lang.Object, boolean, int, int)
     */
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {

        String dateStr = null;
        DataGridModel model = (DataGridModel) table.getModel();
        List<TableColumnDefinition> colDefs = model.getColumnDefinitions();
        String displayFormat = colDefs.get(column).getDisplayValueFormat();
        if (displayFormat == null) {
            displayFormat = "MM-dd-yyyy";
        }
        try {
            // Ensure that this is a valid date value
            if (value instanceof Date) {
                dateStr = RMT2Date.formatDate((Date) value, displayFormat);
            }
            else {
                Date dt = RMT2Date.stringToDate(value.toString());
                dateStr = RMT2Date.formatDate(dt, displayFormat);
            }
        } catch (Exception e) {
            dateStr = "MM-dd-yyyy";
        }
        return super.getTableCellEditorComponent(table, dateStr, isSelected,
                row, column);
    }

}
