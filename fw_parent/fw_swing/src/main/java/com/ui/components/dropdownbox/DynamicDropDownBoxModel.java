package com.ui.components.dropdownbox;

import javax.swing.DefaultComboBoxModel;

import com.api.util.RMT2BeanUtility;
import com.api.util.RMT2Date;

/**
 * The model that is to be used along with the DropDownBox.
 * 
 * @author rterrell
 *
 */
public class DynamicDropDownBoxModel<E> extends DefaultComboBoxModel<E> {
    private static final long serialVersionUID = -8901577182048931663L;

    private DropDownBoxColumnDefinition colDef;

    public DynamicDropDownBoxModel(E[] items) {
        super(items);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.DefaultComboBoxModel#getSelectedItem()
     */
    @Override
    public E getSelectedItem() {
        return (E) super.getSelectedItem();
    }

    /**
     * Selects the drop down item that matches the value that the <i>id</i>
     * property points to.
     * 
     * @param value
     *            the value to match
     */
    public void selectItemByKey(Object value) {
        String keyName = this.colDef.getId();
        this.selectDropDownItem(keyName, value.toString());
    }

    /**
     * Selects the drop down item that matches the value that the
     * <i>displayValue</i> property points to.
     * 
     * @param value
     *            the value to match
     */
    public void selectItemByDisplayValue(Object value) {
        String keyName = this.colDef.getDisplayValue();
        this.selectDropDownItem(keyName, value.toString());
    }

    /**
     * Cycles through all dropdown items looking for a match.
     * <p>
     * 
     * @param property
     *            the property name represented by either <i>id</i> or
     *            <i>displayValue</i>.
     * @param targetValue
     *            the value to be compared to each dropdown item's value that is
     *            mapped to <i>property</i>.
     * @return true when a match is found. Otherwise, false is returned.
     */
    private boolean selectDropDownItem(String property, String targetValue) {
        int total = this.getSize();
        boolean matchFound;
        for (int ndx = 0; ndx < total; ndx++) {
            E item = this.getElementAt(ndx);
            RMT2BeanUtility beanUtil = new RMT2BeanUtility(item);
            Object value = beanUtil.getPropertyValue(property);
            Class typeClass = beanUtil.getPropertyType(property);
            String className = typeClass.getName();
            switch (className) {
                case "boolean":
                case "byte":
                case "char":
                case "java.lang.String":
                    matchFound = value.toString().trim()
                            .equalsIgnoreCase(targetValue.trim());
                    break;
                case "double":
                case "float":
                    matchFound = Double.parseDouble(value.toString()) == Double
                            .parseDouble(targetValue);
                    break;
                case "int":
                case "long":
                case "short":
                    matchFound = Double.valueOf(value.toString()).intValue() == Double
                            .valueOf(targetValue).intValue();
                    break;
                case "java.util.Date":
                    matchFound = RMT2Date.stringToDate(value.toString(),
                            "MM/dd/yyyy") == RMT2Date.stringToDate(
                            targetValue.toString(), "MM/dd/yyyy");
                    break;
                default:
                    matchFound = false;
            }
            if (matchFound) {
                this.setSelectedItem(item);
                return true;
            }
        }
        return false;
    }

    /**
     * @return the colDef
     */
    public DropDownBoxColumnDefinition getColDef() {
        return colDef;
    }

    /**
     * @param colDef
     *            the colDef to set
     */
    public void setColDef(DropDownBoxColumnDefinition colDef) {
        this.colDef = colDef;
    }
}
