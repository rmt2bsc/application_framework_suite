package com.ui.components.table;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.util.RMT2BeanUtility;
import com.util.RMT2Date;
import com.util.RMT2Money;

/**
 * An imiplementation of {@link DynamicListTableModel} for managing JTable
 * presentation data.
 * <p>
 * This implementation requires the presentation data to exist as a List of
 * arbitrary java beans. Another requirement is one or more
 * {@link ColumnDefinition} objects must exist in order to perform the
 * appropriate mapping of properties from the view (JTable) to the model and
 * vice versa. The {@link ColumnDefinition} is the crux to a properly
 * functioning {@link DynamicListTableModel}.
 * 
 * @author rterrell
 *
 */
public class DynamicDataGridModelImpl extends AbstractTableModel implements
        DataGridModel {

    private static final long serialVersionUID = -7964258951414564558L;

    private List viewBuf;
    private List origBuf;
    private List deletedBuf;

    private List<TableColumnDefinition> colDefs;

    private TableColumnModel colModel;

    /**
     * Creates a <i>DynamicListTableModelImpl</i> initialized with a List of
     * data items to be displayed and a List of column definitions.
     */
    public DynamicDataGridModelImpl(List data,
            List<TableColumnDefinition> colDefs) {
        // Establish data buffers
        if (data == null) {
            this.viewBuf = new ArrayList();
        }
        else {
            this.viewBuf = data;
        }
        this.origBuf = new ArrayList();
        this.deletedBuf = new ArrayList();
        // if (this.viewBuf != null) {
        this.origBuf.addAll(this.viewBuf);
        // }

        // Capture column defini
        this.colDefs = colDefs;

        // create column model builder and load column definitions
        TableColumnModelBuilder builder = new TableColumnModelBuilder();
        for (TableColumnDefinition def : colDefs) {
            builder.addCol(def);
        }
        // Create TableColumnModel
        this.colModel = builder.getModel();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount() {
        return (this.viewBuf == null ? 0 : this.viewBuf.size());
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    @Override
    public int getColumnCount() {
        return (this.colDefs == null ? 0 : this.colDefs.size());
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    @Override
    public String getColumnName(int colIndex) {
        return super.getColumnName(colIndex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (this.viewBuf != null && this.viewBuf.size() > 0
                && this.colDefs != null && this.colDefs.size() > 0) {
            // Continue...
        }
        else {
            return null;
        }
        Object rowData = this.viewBuf.get(rowIndex);
        TableColumnDefinition colDef = this.colDefs.get(columnIndex);
        String displayFormat = colDef.getDisplayValueFormat();
        RMT2BeanUtility bu = null;
        try {
            bu = new RMT2BeanUtility(rowData);
            Object val = bu.getPropertyValue(colDef.getId());
            if (val instanceof Date) {
                if (displayFormat == null) {
                    displayFormat = "MM-dd-yyyy";
                }
                val = RMT2Date.formatDate((Date) val, displayFormat);
            }
            if (val instanceof Double || val instanceof Float) {
                if (displayFormat == null) {
                    displayFormat = "#,##0.00";
                }
                val = RMT2Money.formatNumber((Number) val, displayFormat);
            }
            return val;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * Updates the model with UI changes.
     */
    @Override
    public void setValueAt(Object value, int row, int col) {
        // data[row][col] = value;
        Object rowData = this.viewBuf.get(row);
        TableColumn tc = this.colModel.getColumn(col);
        TableColumnDefinition colDef = this.colDefs.get(col);
        Object val = null;
        if (tc.getCellEditor() == null) {
            fireTableCellUpdated(row, col);
            return;
        }
        else {
            Object editorValue = tc.getCellEditor().getCellEditorValue();
            if (tc.getCellEditor() instanceof CustomComboBoxCellEditor) {
                val = ((CustomComboBoxLookupItem) editorValue).getCode();
            }
            else if (tc.getCellEditor() instanceof CustomDateFieldCellEditor) {
                val = RMT2Date.stringToDate(editorValue.toString());
            }
            else if (tc.getCellEditor() instanceof CustomMoneyFieldCellEditor) {
                val = RMT2Money.stringToNumber(editorValue.toString());
            }
            else if (tc.getCellEditor() instanceof CustomTextFieldCellEditor) {
                val = editorValue.toString();
            }
        }

        try {
            RMT2BeanUtility bu = new RMT2BeanUtility(rowData);
            bu.setPropertyValue(colDef.getId(), val);
        } catch (Exception e) {
            e.printStackTrace();
        }
        fireTableCellUpdated(row, col);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ui.components.table.DynamicListTableModel#getSelectedRowData(int)
     */
    @Override
    public Object getSelectedRowData(int rowIndex) {
        return this.viewBuf.get(rowIndex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ui.components.table.DynamicListTableModel#addRow(java.lang.Object)
     */
    @Override
    public int addRow(Object rowData) {
        if (rowData == null) {
            return -1;
        }
        this.viewBuf.add(rowData);
        int insertedRow = this.viewBuf.size() - 1;
        this.fireTableRowsInserted(0, insertedRow);
        return (insertedRow);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ui.components.table.DynamicListTableModel#removeRow(int)
     */
    @Override
    public int removeRow(int rowIndex) {
        boolean indexValidRange = (rowIndex >= 0 && rowIndex <= this
                .getRowCount());
        if (!indexValidRange) {
            return -1;
        }
        this.deletedBuf.add(this.viewBuf.get(rowIndex));
        this.viewBuf.remove(rowIndex);
        this.fireTableRowsDeleted(0, 0);
        return 1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
     */
    @Override
    public Class getColumnClass(int columnIndex) {
        Class c = super.getColumnClass(columnIndex);
        return c;
    }

    /*
     * Enable edit control (Text field, combo box, or check box) for the column
     * in the event the column is editable.
     */
    @Override
    public boolean isCellEditable(int row, int col) {
        TableColumnDefinition def = this.colDefs.get(col);
        if (def.isCellEditable()) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * @param colModel
     *            the colModel to set
     */
    public void setColModel(TableColumnModel colModel) {
        this.colModel = colModel;
    }

    /**
     * @return the colModel
     */
    public TableColumnModel getColModel() {
        return colModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ui.components.table.DataGridModel#getColumnDefinitions()
     */
    @Override
    public List<TableColumnDefinition> getColumnDefinitions() {
        return this.colDefs;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ui.components.table.DataGridModel#getViewBuf()
     */
    @Override
    public List<?> getViewBuf() {
        return this.viewBuf;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ui.components.table.DataGridModel#getDeletedBuf()
     */
    @Override
    public List<?> getDeletedBuf() {
        return this.deletedBuf;
    }

}
