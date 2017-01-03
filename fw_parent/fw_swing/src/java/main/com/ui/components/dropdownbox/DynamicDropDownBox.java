package com.ui.components.dropdownbox;

import javax.swing.JComboBox;

/**
 * Custom JComboBox.
 * 
 * @author rterrell
 *
 */
// public class DropDownBox<E> extends JComboBox<E> implements TableCellRenderer
// {
public class DynamicDropDownBox<E> extends JComboBox<E> {

    private static final long serialVersionUID = 2618187117637232655L;

    public DynamicDropDownBox() {
        super();
        return;
    }

    /**
     * Selects the drop down item that matches the value of the id property.
     * 
     * @param keyValue
     *            the value mapped to the key property to match
     */
    public void selectItemByKey(Object keyValue) {
        DynamicDropDownBoxModel<E> model = (DynamicDropDownBoxModel) this
                .getModel();
        model.selectItemByKey(keyValue);
    }

    /**
     * Selects the drop down item that matches the value of the display-value
     * property.
     * 
     * @param displayValue
     *            the value mapped to the display-value property to match
     */
    public void selectItemByDisplayValue(Object displayValue) {
        DynamicDropDownBoxModel<E> model = (DynamicDropDownBoxModel) this
                .getModel();
        model.selectItemByDisplayValue(displayValue);
    }

    // /*
    // * (non-Javadoc)
    // *
    // * @see
    // * javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax
    // * .swing.JTable, java.lang.Object, boolean, boolean, int, int)
    // */
    // @Override
    // public Component getTableCellRendererComponent(JTable table, Object
    // value,
    // boolean isSelected, boolean hasFocus, int row, int column) {
    // if (isSelected) {
    // // CustomDropDownBoxModel<E> model = (CustomDropDownBoxModel) this
    // // .getModel();
    // // DropDownBoxColumnDefinition def = model.getColDef();
    // // RMT2BeanUtility bu = null;
    // try {
    // this.selectItemByKey(value);
    // // bu = new RMT2BeanUtility(obj);
    // // Object val = bu.getPropertyValue(def.getDisplayValue());
    // // if (val == null) {
    // // this.setSelectedItem("Undefined");
    // // }
    // // else {
    // // this.setSelectedItem(obj);
    // // }
    // } catch (NotFoundException e) {
    // this.setSelectedItem("Item Not Found");
    // } catch (SystemException e) {
    // this.setSelectedItem("Item Error");
    // }
    // }
    // return this;
    // }
}
