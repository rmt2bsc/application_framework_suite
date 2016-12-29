package com.ui.components.table;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

/**
 * A custom JTextField editor for DataGrid cells.
 * 
 * @author rterrell
 *
 */
public class CustomTextFieldCellEditor extends AbstractCellEditor implements
        TableCellEditor {

    private static final long serialVersionUID = -4245359924652799988L;

    private JComponent component;

    /**
     * 
     */
    public CustomTextFieldCellEditor() {
        this.component = new JTextField();
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.CellEditor#getCellEditorValue()
     */
    @Override
    public Object getCellEditorValue() {
        return ((JTextField) this.component).getText();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing
     * .JTable, java.lang.Object, boolean, int, int)
     */
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        if (value == null) {
            ((JTextField) this.component).setText("");
        }
        else {
            ((JTextField) this.component).setText(value.toString());
        }
        return component;
    }

}
