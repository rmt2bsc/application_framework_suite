package com.ui.components.table;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellEditor;

/**
 * A custom JTextArea editor for DataGrid cells.
 * 
 * @author rterrell
 *
 */
public class CustomTextAreaCellEditor extends AbstractCellEditor implements
        TableCellEditor {

    private static final long serialVersionUID = -4245359924652799988L;

    private JComponent component;

    /**
     * 
     */
    public CustomTextAreaCellEditor() {
        this.component = new JTextArea();
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.CellEditor#getCellEditorValue()
     */
    @Override
    public Object getCellEditorValue() {
        return ((JTextArea) this.component).getText();
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
            ((JTextArea) this.component).setText("");
        }
        else {
            ((JTextArea) this.component).setText(value.toString());
        }
        return component;
    }

}
