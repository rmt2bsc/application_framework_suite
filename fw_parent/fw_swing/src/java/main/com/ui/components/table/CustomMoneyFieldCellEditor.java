package com.ui.components.table;

import java.awt.Component;
import java.util.List;

import javax.swing.JTable;

import com.util.RMT2Money;

/**
 * A custom JTextField editor for managing money values of DataGrid cells.
 * 
 * @author rterrell
 *
 */
public class CustomMoneyFieldCellEditor extends CustomTextFieldCellEditor {

    private static final long serialVersionUID = -2722043276904040093L;

    public CustomMoneyFieldCellEditor() {
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

        String numberStr = null;
        DataGridModel model = (DataGridModel) table.getModel();
        List<TableColumnDefinition> colDefs = model.getColumnDefinitions();
        String displayFormat = colDefs.get(column).getDisplayValueFormat();
        if (displayFormat == null) {
            displayFormat = "#,##0.00";
        }
        try {
            // Ensure that this is a valid date value
            if (value instanceof Number) {
                numberStr = RMT2Money.formatNumber((Number) value,
                        displayFormat);
            }
            else {
                Number dec = RMT2Money.stringToNumber(value.toString());
                numberStr = RMT2Money.formatNumber(dec, displayFormat);
            }
        } catch (Exception e) {
            numberStr = "#,##9.99";
        }
        return super.getTableCellEditorComponent(table, numberStr, isSelected,
                row, column);
    }

}
