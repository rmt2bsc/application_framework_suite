package com.ui.components.dropdownbox;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.NotFoundException;
import com.SystemException;
import com.api.util.RMT2BeanUtility;

/**
 * A DropDownBox renderer that is used to determine how a DropDownBox control is
 * manage and display custom data.
 * 
 * @author rterrell
 *
 */
public class DynamicDropDownBoxLabelRenderer<E> extends JLabel implements
        ListCellRenderer<E> {

    private static final long serialVersionUID = 3571510943076670408L;
    private DropDownBoxColumnDefinition colDef;

    /**
     * Default contructor
     */
    protected DynamicDropDownBoxLabelRenderer() {
        return;
    }

    public void setColumnDefs(DropDownBoxColumnDefinition colDef) {
        this.colDef = colDef;
    }

    /**
     * A renderer for interpretting and displaying data for a DropDownBox
     * control in which the source of data are arbitrary custom data objects.
     * <p>
     * This method will used {@link DropDownBoxColumnDefinition} to introspect
     * the data object and set the text of the DropDownBox cotnrol.
     * 
     * @param list
     *            a list object used behind the scenes to display the items.
     * @param value
     *            the custom object containing the data to be displayed as text.
     * @param index
     *            the index of the object to render.
     * @param isSelected
     *            indicates whether the object to render is selected.
     * @param cellHasFocus
     *            indicates whether the object to render has the focus.
     * @return The JLabel component that is rendered by the DropDownBox control.
     */
    @Override
    public Component getListCellRendererComponent(JList<? extends E> list,
            E value, int index, boolean isSelected, boolean cellHasFocus) {

        RMT2BeanUtility bu = null;
        try {
            bu = new RMT2BeanUtility(value);
            Object val = bu.getPropertyValue(this.colDef.getDisplayValue());
            if (val == null) {
                this.setText("Item Value Not Set");
            }
            else {
                this.setText(val.toString());
            }
        } catch (NotFoundException e) {
            this.setText("Item Not Found");
        } catch (SystemException e) {
            this.setText("Item Error");
        }
        return this;
    }

}
