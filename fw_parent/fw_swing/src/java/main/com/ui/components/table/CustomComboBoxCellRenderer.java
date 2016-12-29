package com.ui.components.table;

import java.awt.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.ui.components.dropdownbox.DropDownBoxColumnDefinition;
import com.util.RMT2BeanUtility;

/**
 * A custom renderer for DataGrid cells.
 * 
 * @author rterrell
 *
 */
public class CustomComboBoxCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 2404258800351688598L;

    private DropDownBoxColumnDefinition colDef;

    private Map<Integer, CustomComboBoxLookupItem> itemMap;

    /**
     * 
     */
    public CustomComboBoxCellRenderer(DropDownBoxColumnDefinition colDef) {
        this.colDef = colDef;
    }

    public CustomComboBoxCellRenderer(DropDownBoxColumnDefinition colDef,
            List itemList) {
        this.colDef = colDef;
        RMT2BeanUtility bu = null;
        Object key = null;
        String displayValue = null;
        this.itemMap = new HashMap<Integer, CustomComboBoxLookupItem>();
        for (Object item : itemList) {
            bu = new RMT2BeanUtility(item);
            key = bu.getPropertyValue(colDef.getId());
            displayValue = bu.getPropertyValue(colDef.getDisplayValue())
                    .toString();
            CustomComboBoxLookupItem i = new CustomComboBoxLookupItem();
            if (key instanceof Integer) {
                i.setCode((Integer) key);
            }
            i.setDesc(displayValue);
            this.itemMap.put((Integer) key, i);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent
     * (javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        if (value instanceof Integer) {
            CustomComboBoxLookupItem i = this.itemMap.get(value);
            if (i != null) {
                this.setText(i.getDesc());
            }
        }
        return this;
    }

    /**
     * @param itemMap
     *            the itemMap to set
     */
    public void setItemMap(Map<Integer, CustomComboBoxLookupItem> itemMap) {
        this.itemMap = itemMap;
    }

}
