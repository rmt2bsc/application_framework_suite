package com.ui.components.table;

import javax.swing.SwingConstants;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.ui.components.ColumnDefinition;

/**
 * Java bean class that manages the JTable-to-Model mapping definition details
 * of a specified column.
 * 
 * @author rterrell
 *
 */
public class TableColumnDefinition extends ColumnDefinition {
    public static final int JUSTIFICATION_NOT_SET = -1;

    private String displayValueFormat;

    private boolean resizeable;

    private int justification;

    private TableCellEditor cellEditor;

    private TableCellRenderer cellRenderer;

    private TableCellRenderer headerRenderer;

    /**
     * Creates a {@link ColumnDefinition} without any column definitions.
     * <p>
     * Defaults all columns to be resizable. This will allow the user to employ
     * a pointing device such as a mouse to adjust the width of a column by
     * dragging the borders of the column header inward and outward.
     */
    public TableColumnDefinition() {
        super();
        this.resizeable = true;
        this.displayValueFormat = null;
        this.justification = TableColumnDefinition.JUSTIFICATION_NOT_SET;
        return;
    }

    /**
     * Creates a {@link ColumnDefinition} identified by the column's assoicated
     * bean property name, its header display value, and its position in JTable
     * column list.
     * 
     * @param propertyName
     *            The property name belonging to the model (java bean) that will
     *            map to the JTable column.
     * @param displayValue
     *            The description of the column that will be displayed as column
     *            header
     * @param index
     *            The column postion where the column will be placed in the
     *            JTable.
     */
    public TableColumnDefinition(String propertyName, String displayValue,
            int index) {
        super(propertyName, displayValue, index);
        this.justification = TableColumnDefinition.JUSTIFICATION_NOT_SET;
    }

    /**
     * Creates a {@link ColumnDefinition} identified by the column's assoicated
     * bean property name, its header display value, its position in JTable
     * column list, and its width.
     *
     * param propertyName The property name belonging to the model (java bean)
     * that will map to the JTable column.
     *
     * @param displayValue
     *            The description of the column that will be displayed as column
     *            header
     * @param index
     *            The column postion where the column will be placed in the
     *            JTable.
     * @param width
     *            The width of the column. This will also set the prefered width
     *            property to this value.
     */
    public TableColumnDefinition(String propertyName, String displayValue,
            int index, int width) {
        super(propertyName, displayValue, index, width);
        this.justification = TableColumnDefinition.JUSTIFICATION_NOT_SET;
    }

    /**
     * Creates a {@link ColumnDefinition} identified by the column's assoicated
     * bean property name, its header display value, and its position in JTable
     * column list.
     * 
     * @param propertyName
     *            The property name belonging to the model (java bean) that will
     *            map to the JTable column.
     * @param displayValue
     *            The description of the column that will be displayed as column
     *            header
     * @param format
     *            The format <i>displayValue</i> is to be presented as.
     * @param index
     *            The column postion where the column will be placed in the
     *            JTable.
     * @param width
     *            The width of the column. This will also set the prefered width
     *            property to this value.
     */
    public TableColumnDefinition(String propertyName, String displayValue,
            String format, int index, int width) {
        super(propertyName, displayValue, index, width);
        this.displayValueFormat = format;
        this.justification = TableColumnDefinition.JUSTIFICATION_NOT_SET;
    }

    /**
     * Creates a {@link ColumnDefinition} identified by the column's assoicated
     * bean property name, its header display value, and its position in JTable
     * column list.
     * 
     * @param propertyName
     *            The property name belonging to the model (java bean) that will
     *            map to the JTable column.
     * @param displayValue
     *            The description of the column that will be displayed as column
     *            header
     * @param format
     *            The format <i>displayValue</i> is to be presented as.
     * @param index
     *            The column postion where the column will be placed in the
     *            JTable.
     * @param width
     *            The width of the column. This will also set the prefered width
     *            property to this value.
     * @param justification
     *            The justification of the cell. Use either
     *            {@link javax.swing.SwingConstants#RIGHT},
     *            {@link javax.swing.SwingConstants#LEFT}, or
     *            {@link javax.swing.SwingConstants#CENTER}. If a value is
     *            passed in other than what is specified above, the
     *            justification will default to left justified.
     */
    public TableColumnDefinition(String propertyName, String displayValue,
            String format, int index, int width, int justification) {
        this(propertyName, displayValue, format, index, width);
        if (justification != SwingConstants.RIGHT
                && justification != SwingConstants.LEFT
                && justification != SwingConstants.CENTER) {
            justification = SwingConstants.LEFT;
        }
        this.justification = justification;
    }

    /**
     * @return the resizeable
     */
    public boolean isResizeable() {
        return resizeable;
    }

    /**
     * @param resizeable
     *            the resizeable to set
     */
    public void setResizeable(boolean resizeable) {
        this.resizeable = resizeable;
    }

    /**
     * 
     * @return
     */
    public boolean isCellEditable() {
        return (this.cellEditor != null);
    }

    /**
     * @return the cellEditor
     */
    public TableCellEditor getCellEditor() {
        return cellEditor;
    }

    /**
     * @param cellEditor
     *            the cellEditor to set
     */
    public void setCellEditor(TableCellEditor cellEditor) {
        this.cellEditor = cellEditor;
    }

    /**
     * @return the cellRenderer
     */
    public TableCellRenderer getCellRenderer() {
        return cellRenderer;
    }

    /**
     * @param cellRenderer
     *            the cellRenderer to set
     */
    public void setCellRenderer(TableCellRenderer cellRenderer) {
        this.cellRenderer = cellRenderer;
    }

    /**
     * @return the headerRenderer
     */
    public TableCellRenderer getHeaderRenderer() {
        return headerRenderer;
    }

    /**
     * @param headerRenderer
     *            the headerRenderer to set
     */
    public void setHeaderRenderer(TableCellRenderer headerRenderer) {
        this.headerRenderer = headerRenderer;
    }

    /**
     * @return the displayValueFormat
     */
    public String getDisplayValueFormat() {
        return displayValueFormat;
    }

    /**
     * @param displayValueFormat
     *            the displayValueFormat to set
     */
    public void setDisplayValueFormat(String displayValueFormat) {
        this.displayValueFormat = displayValueFormat;
    }

    /**
     * @return the justification
     */
    public int getJustification() {
        return justification;
    }

}
