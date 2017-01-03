package com.ui.components.table;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 * A JLabel cell renderer for {@link DataGrid}.
 * 
 * @author appdev
 *
 */
public class CustomLabelCellRenderer extends DefaultTableCellRenderer implements
        TableCellRenderer {

    private static final long serialVersionUID = -6621705753199929973L;

    /**
     * 
     */
    public CustomLabelCellRenderer() {
        this.setVerticalAlignment(SwingConstants.CENTER);
    }

    public CustomLabelCellRenderer(int verticalAlign) {
        this.setVerticalAlignment(verticalAlign);
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax
     * .swing.JTable, java.lang.Object, boolean, boolean, int, int)
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        Font f = table.getFont();
        this.setFont(f);
        if (value != null) {
            setText(value.toString());
        }
        else {
            setText("");
        }
        return this;
    }
}
