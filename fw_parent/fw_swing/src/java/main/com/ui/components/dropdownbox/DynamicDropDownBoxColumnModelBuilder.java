package com.ui.components.dropdownbox;

import java.awt.Dimension;
import java.util.List;

import javax.swing.ComboBoxModel;

import com.ui.components.ColumnDefinitionException;

/**
 * Builds a DropDownBox column model, DropDownBoxColumnModelBuilder, in which
 * the model is based on a {@link DropDownBoxColumnDefinition} object.
 * <p>
 * The column model created by this builder is expected to used with
 * {@link DropDownBox} input control.
 * <p>
 * DropDownBoxColumnDefinition represents information as ow to display and
 * manage the contents of a DropDownBox control.
 * 
 * @author rterrell
 *
 */
public class DynamicDropDownBoxColumnModelBuilder<E> {

    private E[] data;

    private DynamicDropDownBoxModel<E> model;

    private DynamicDropDownBoxLabelRenderer<E> renderer;
    // private CustomDropDownBoxRenderer<E> renderer;

    private DropDownBoxColumnDefinition colDef;

    private Dimension size;

    /**
     * Creates a DropDownBoxColumnModelBuilder initialized with data and column
     * definitions.
     */
    public DynamicDropDownBoxColumnModelBuilder(List<E> items,
            DropDownBoxColumnDefinition colDef) {
        this.data = (E[]) items.toArray();
        this.colDef = colDef;
    }

    /**
     * Creates a DropDownBoxColumnModelBuilder initialized with data, column
     * definitions, and dimensions of the control.
     * 
     * @param items
     * @param colDef
     * @param size
     */
    public DynamicDropDownBoxColumnModelBuilder(List<E> items,
            DropDownBoxColumnDefinition colDef, Dimension size) {
        this(items, colDef);
        this.size = size;
    }

    /**
     * Creates and returns an instance of <i>DropDownBox</i>.
     * 
     * @return {@link DropDownBox}
     * @throws ColumnDefinitionException
     *             validation errors
     */
    public DynamicDropDownBox<E> createInstance() {
        this.validateColumnDefs();
        DynamicDropDownBox<E> ddb = new DynamicDropDownBox<E>();
        this.model = new DynamicDropDownBoxModel<E>(data);
        this.model.setColDef(colDef);
        this.renderer = new DynamicDropDownBoxLabelRenderer<E>();
        // this.renderer = new CustomDropDownBoxRenderer<E>();
        this.renderer.setColumnDefs(this.colDef);
        ddb.setModel(this.model);
        ddb.setRenderer(this.renderer);
        return ddb;
    }

    /**
     * Ensure that display column property is populated with the actual column
     * name of the data source.
     * 
     * @throws ColumnDefinitionException
     *             THe column definition object is null, or the display column
     *             property column definition is not available or null.
     */
    protected void validateColumnDefs() throws ColumnDefinitionException {
        String msg = null;
        if (this.colDef == null) {
            throw new ColumnDefinitionException(
                    "DropDownBox's column definition object is null");
        }
        if (this.colDef.getDisplayValue() == null) {
            StringBuffer buf = new StringBuffer();
            buf.append("The display value is not configured for the Drop Down Box control.");
            buf.append("/nCheck DropDownBoxColumnDefinition configuration for Drop Down Box instance, ");
            buf.append(this.getClass().getName());
            msg = buf.toString();
            throw new ColumnDefinitionException(msg);
        }
    }

    /**
     * Adds a DropDownBoxColumnDefinition instance tot he builder.
     * 
     * @param def
     *            an instance of {@link DropDownBoxColumnDefinition}
     * @return boolean true when <i>def</i> is successfully added to the
     *         builder. Otherwise, false is returned or when <i>def</i> is null.
     */
    public boolean addCol(DropDownBoxColumnDefinition def) {
        if (def == null) {
            return false;
        }
        if (def.getId() == null || def.getId().equals("")) {
            return false;
        }
        if (def.getDisplayValue() == null || def.getDisplayValue().equals("")) {
            return false;
        }

        if (def.getIndex() == null || def.getIndex() < 0) {
            def.setIndex(0);
        }

        // // Create TableColumn instance using the approriate constructor
        // TableColumn col = null;
        // if (def.getWidth() != null && def.getCellRenderer() != null
        // && def.getCellEditor() != null) {
        // col = new TableColumn(def.getIndex(), def.getWidth(),
        // def.getCellRenderer(), def.getCellEditor());
        // }
        // else if (def.getWidth() != null) {
        // col = new TableColumn(def.getIndex(), def.getWidth());
        // }
        // else {
        // col = new TableColumn(def.getIndex());
        // }
        //
        // // Continue to set the remaining properties as needed.
        // col.setIdentifier(def.getId());
        // col.setHeaderValue(def.getDisplayValue());
        // col.setResizable(def.isResizeable());
        //
        // if (def.getPrefWidth() != null) {
        // col.setPreferredWidth(def.getPrefWidth());
        // }
        // if (def.getMaxWidth() != null) {
        // col.setMaxWidth(def.getMaxWidth());
        // }
        // if (def.getMinWidth() != null) {
        // col.setMinWidth(def.getMinWidth());
        // }
        //
        // this.model.addColumn(col);
        this.colDef = def;
        return true;
    }

    // /**
    // * Removes a column from the table column model.
    // * <p>
    // *
    // * @param propName
    // * the name of the column assoicated with the table column model.
    // * @return boolean true when the column is removed and false otherwise.
    // */
    // public boolean removeCol(String propName) {
    // try {
    // int ndx = this.model.get ColumnIndex(propName);
    // TableColumn colDef = this.model.getColumn(ndx);
    // this.model.removeColumn(colDef);
    // } catch (Exception e) {
    // // Column Def was not found in the Table Column Modle.
    // return false;
    // }
    //
    // // Verify that the column has been removed.
    // try {
    // this.model.getColumnIndex(propName);
    // // This indicates that the column still exists
    // return false;
    // } catch (Exception e) {
    // // Column was removed successfully
    // return true;
    // }
    // }

    /**
     * Return the column definition.
     * 
     * @return List of {@link DropDownBoxColumnDefinition}
     */
    public DropDownBoxColumnDefinition getColDef() {
        return colDef;
    }

    /**
     * Retrun the table model
     * 
     * @return {@link TableColumnModel}
     */
    public ComboBoxModel<E> getModel() {
        return this.model;
    }
}
