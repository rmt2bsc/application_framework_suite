package com.ui.components.table;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import com.api.util.RMT2BeanUtility;
import com.ui.components.dropdownbox.DropDownBoxColumnDefinition;

/**
 * A custom JComboBox editor for DataGrid cells.
 * 
 * @author rterrell
 *
 */
public class CustomComboBoxCellEditor extends AbstractCellEditor implements
        TableCellEditor, ActionListener {

    private static final long serialVersionUID = -8841759637019873921L;
    private DropDownBoxColumnDefinition colDef;
    private List<CustomComboBoxLookupItem> itemList;
    private Map<Integer, CustomComboBoxLookupItem> itemMap;
    private CustomComboBoxLookupItem selectedItem;

    /**
     * 
     */
    public CustomComboBoxCellEditor(List items,
            DropDownBoxColumnDefinition colDef) {
        this.colDef = colDef;
        RMT2BeanUtility bu = null;
        Object key = null;
        String displayValue = null;
        this.itemList = new ArrayList<CustomComboBoxLookupItem>();
        this.itemMap = new HashMap<Integer, CustomComboBoxLookupItem>();
        for (Object item : items) {
            bu = new RMT2BeanUtility(item);
            key = bu.getPropertyValue(colDef.getId());
            displayValue = bu.getPropertyValue(colDef.getDisplayValue())
                    .toString();
            CustomComboBoxLookupItem i = new CustomComboBoxLookupItem();
            if (key instanceof Integer) {
                i.setCode((Integer) key);
            }
            i.setDesc(displayValue);
            this.itemList.add(i);
            this.itemMap.put((Integer) key, i);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.CellEditor#getCellEditorValue()
     */
    @Override
    public Object getCellEditorValue() {
        return this.selectedItem;
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
        if (value instanceof Integer) {
            this.selectedItem = this.itemMap.get(value);
        }
        JComboBox<CustomComboBoxLookupItem> cb = new JComboBox<CustomComboBoxLookupItem>();
        for (CustomComboBoxLookupItem item : this.itemList) {
            cb.addItem(item);
        }
        cb.setSelectedItem(this.selectedItem);
        cb.addActionListener(this);
        // if (isSelected) {
        // cb.setBackground(table.getSelectionBackground());
        // }
        // else {
        // cb.setBackground(table.getSelectionForeground());
        // }
        return cb;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent evt) {
        JComboBox<CustomComboBoxLookupItem> cb = (JComboBox<CustomComboBoxLookupItem>) evt.getSource();
        this.selectedItem = (CustomComboBoxLookupItem) cb.getSelectedItem();
    }

}
