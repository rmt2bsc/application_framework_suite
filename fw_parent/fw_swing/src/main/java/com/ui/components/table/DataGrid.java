package com.ui.components.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.swing.DefaultListSelectionModel;
import javax.swing.DefaultRowSorter;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.apache.log4j.Logger;

import com.DateStringComparator;
import com.GuiEnvSetup;
import com.NumericStringComparator;
import com.ui.components.ColumnDefinitionException;
import com.ui.event.CustomItemDoubleClickedEvent;
import com.ui.event.CustomItemSelectedEvent;
import com.ui.event.CustomSelectionListener;
import com.ui.event.EventDispatcher;
import com.ui.event.EventDispatcherImpl;
import com.util.RMT2File;
import com.util.RMT2SwingUtil;

/**
 * An extension of {@link JTable} bearing common functionality for the
 * application.
 * <p>
 * <b>How To Use</b>
 * 
 * <pre>
 * // This is an empty list. For all intents and purposes, imagine that
 * // the list is populated with arbitrary java beans to be displayed as
 * // grid data.
 * List listData = new ArrayList();
 * 
 * // Setup column definitions
 * List&lt;TableColumnDefinition&gt; colDefs = new ArrayList&lt;TableColumnDefinition&gt;();
 * colDefs.add(new TableColumnDefinition(&quot;server&quot;, &quot;Server&quot;, 0));
 * colDefs.add(new TableColumnDefinition(&quot;storeNo&quot;, &quot;Store&quot;, 1));
 * 
 * // Set the size of the data grid component
 * Dimension size = new Dimension(400, 175);
 * // Or set the size of the data grid by equating the width to &quot;0&quot; so that the
 * // framework will calculate the width of the table.
 * Dimension size = new Dimension(0, 175);
 * 
 * // Create table component
 * DataGrid table = new DataGrid(list, colDefs,
 *         ListSelectionModel.SINGLE_SELECTION, size);
 * table.setup();
 * // Add parent class instance which should be an implementation of
 * // CustomSelectionListener interfacee
 * table.addItemSelectionListener(this);
 * 
 * // Create ScrollableDataGrid grid component
 * ScrollableDataGrid dg = new ScrollableDataGrid(table);
 * 
 * </pre>
 * 
 * @author rterrell
 *
 */
public class DataGrid extends JTable {

    private static final long serialVersionUID = -8132586284229300549L;

    private static final int EVENT_ID_SELECTIONCHANGED = 100;

    private static final int EVENT_ID_DOUBLECLICKED = 101;

    private static final Logger logger = Logger.getLogger(DataGrid.class);

    private Color normBg;

    private Color normFg;

    private Color altBg;

    private Color altFg;

    private Color selBg;

    private Color selFg;

    private List tableData;

    private List<TableColumnDefinition> colDefs;

    private Object selection;

    private int selectionMode;

    private Dimension size;

    private EventDispatcher evtDispatcher;

    /**
     * Message text memeber variable
     */
    protected String msg;

    /**
     * Create DataGrid without a table model, table column model, a list
     * selection model, with a default width and height of 400 by 400 and the
     * selection mode turned off.
     * 
     * selection mode of {@link ListSelectionModel#MULTIPLE_INTERVAL_SELECTION
     * MULTIPLE_INTERVAL_SELECTION}.
     */
    protected DataGrid() {
        super();
        logger.info("DataGrid Logger Intializied");
        this.size = new Dimension(400, 400);
        // this.selectionMode = ListSelectionModel.MULTIPLE_INTERVAL_SELECTION;
        this.selectionMode = -1;
        // Setup event listener
        this.listenerList = new EventListenerList();
        // Setup Event dispatcher
        this.evtDispatcher = new EventDispatcherImpl(this.listenerList);
        return;
    }

    /**
     * Create a ScrollableDataGrid initialized with a specified list of data to
     * be displayed and related column definitions for each element included in
     * <i>tableData</i>, a default width and height of 400 by 400 and selection
     * mode of {@link ListSelectionModel#MULTIPLE_INTERVAL_SELECTION
     * MULTIPLE_INTERVAL_SELECTION}
     * 
     * @param tableData
     *            a List of arbitrary objects to be associated with the table
     *            model and presented as the data grid.
     * @param colDefs
     *            a List of {@link ColumnDefinition} objects to be assoicated
     *            with underlying JTable.
     */
    public DataGrid(List tableData, List<TableColumnDefinition> colDefs) {
        this();
        if (colDefs == null) {
            return;
        }
        this.tableData = tableData;
        this.colDefs = colDefs;
        this.loadTable(tableData, colDefs);
        return;
    }

    /**
     * Create a ScrollableDataGrid initialized with a specified list of data to
     * be displayed and related column definitions for each element included in
     * <i>tableData</i>, a default width and height of 400 by 400, and the
     * specified selection mode of <i>selectionMode</i>.
     * 
     * @param tableData
     *            a List of arbitrary objects to be associated with the table
     *            model and presented as the data grid.
     * @param colDefs
     *            a List of {@link ColumnDefinition} objects to be assoicated
     *            with underlying JTable.
     * @param selectionMode
     *            a int value representing one of the following values:
     *            <ul>
     *            <li>{@link ListSelectionModel#MULTIPLE_INTERVAL_SELECTION
     *            MULTIPLE_INTERVAL_SELECTION}</li>
     *            <li>{@link ListSelectionModel#SINGLE_INTERVAL_SELECTION
     *            SINGLE_INTERVAL_SELECTION}</li>
     *            <li>{@link ListSelectionModel#SINGLE_SELECTION
     *            SINGLE_SELECTION}</li>
     *            </ul>
     * 
     */
    public DataGrid(List tableData, List<TableColumnDefinition> colDefs,
            int selectionMode) {
        this(tableData, colDefs);
        this.selectionMode = selectionMode;
    }

    /**
     * Create a ScrollableDataGrid initialized with a specified list of data to
     * be displayed and related column definitions for each element included in
     * <i>tableData</i>, a specified selection mode of <i>selectionMode</i>, and
     * a specified component size.
     * 
     * @param tableData
     *            a List of arbitrary objects to be associated with the table
     *            model and presented as the data grid.
     * @param colDefs
     *            a List of {@link ColumnDefinition} objects to be assoicated
     *            with underlying JTable.
     * @param selectionMode
     *            a int value representing one of the following values:
     *            <ul>
     *            <li>{@link ListSelectionModel#MULTIPLE_INTERVAL_SELECTION
     *            MULTIPLE_INTERVAL_SELECTION}</li>
     *            <li>{@link ListSelectionModel#SINGLE_INTERVAL_SELECTION
     *            SINGLE_INTERVAL_SELECTION}</li>
     *            <li>{@link ListSelectionModel#SINGLE_SELECTION
     *            SINGLE_SELECTION}</li>
     *            </ul>
     * @param size
     *            an instance of {@link Dimension}. When the width property is
     *            less than or equal to zero, the table width will be calculated
     *            automatcially based on the sum of the column widths of the
     *            table.
     */
    public DataGrid(List tableData, List<TableColumnDefinition> colDefs,
            int selectionMode, Dimension size) {
        this(tableData, colDefs, selectionMode);
        this.size = size;
    }

    /**
     * Performs basic initialization of the DataGrid.
     */
    public void setup() {
        // Undocumented line of code that will force the JTable to update model
        // when focus is lost
        this.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        this.loadTable(tableData, colDefs);

        // this.setCellSelectionEnabled(false);
        this.setAutoCreateRowSorter(true);
        this.setRowSelectionAllowed(true);
        this.setColumnSelectionAllowed(false);
        this.setShowGrid(false);

        // Configure data grid row colors
        this.configTableGrid();

        // Enable the table's resize property for all columns to resize when the
        // parent window is resized. This setting will enaable the vertical
        // scrollbar and disable the usage of the horizontal scrollbar of the
        // JScrollPane component. Also, Swing ignores column widths set by you
        // when resize mode of the JTable is not AUTO_RESIZE_OFF. When setting
        // the column widths, use setPreferedWidth instead of setWidth.
        this.setAutoResizeMode(DataGrid.AUTO_RESIZE_ALL_COLUMNS);

        Font df = GuiEnvSetup.getDataGridRowFont();
        Font hf = GuiEnvSetup.getDataGridHeaderFont();
        this.setFont(df);
        this.getTableHeader().setFont(hf);
        // This line forces the column headers to horizontally scroll with the
        // column data!
        this.getTableHeader().setPreferredSize(new Dimension(10000, 32));

        // Assign DataGrid instance as the viewpot for this JScrollPane so the
        // tableView is scrollable
        int totColWidth = this.calcTotalColumnWidth();
        if (this.size.getWidth() <= 0) {
            this.size.setSize(totColWidth, this.size.getHeight());
        }
        this.setPreferredScrollableViewportSize(this.size);
        this.setFillsViewportHeight(true);

        // Assign mouse listener
        this.addMouseListener(new TableViewMouseAdapter(this));
        if (this.selectionMode >= 0) {
            this.getSelectionModel().addListSelectionListener(this);
        }
        this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        return;
    }

    protected void loadTable(List data, List<TableColumnDefinition> colDefs) {
        this.colDefs = colDefs;
        // load data into table
        this.loadTable(data);

        // Validate column definiations
        this.validateColumnDefs();
    }

    public void loadTable(List data) {

        // Create tableView model
        DynamicDataGridModelImpl tableModel = new DynamicDataGridModelImpl(
                data, colDefs);
        // Setup DataGrid instance
        this.setModel(tableModel);

        // Create list selection model from default implementation
        if (this.selectionMode >= 0) {
            ListSelectionModel listSelModel = new DefaultListSelectionModel();
            listSelModel.setSelectionMode(this.selectionMode);
            this.setSelectionModel(listSelModel);
        }

        this.setColumnModel(tableModel.getColModel());

        this.tableData = data;
    }

    /**
     * Identify and configure the background and foreground colors of the
     * table's grid.
     */
    private void configTableGrid() {
        Properties props = RMT2File
                .loadPropertiesObject(GuiEnvSetup.CONFIG_COMMON_FILE);
        this.normBg = RMT2SwingUtil.createColorInstance(
                props.getProperty("datagrid.norm.bg"), Color.WHITE);
        this.normFg = RMT2SwingUtil.createColorInstance(
                props.getProperty("datagrid.norm.fg"), Color.BLACK);
        this.altBg = RMT2SwingUtil.createColorInstance(
                props.getProperty("datagrid.alt.bg"), Color.WHITE);
        this.altFg = RMT2SwingUtil.createColorInstance(
                props.getProperty("datagrid.alt.fg"), Color.BLACK);
        this.selBg = RMT2SwingUtil.createColorInstance(
                props.getProperty("datagrid.sel.bg"), Color.WHITE);
        this.selFg = RMT2SwingUtil.createColorInstance(
                props.getProperty("datagrid.sel.fg"), Color.BLACK);
    }

    /**
     * Ensure that the index property of each {@link TableColumn} model instance
     * does not equal or exceed the to total number of column defintions.
     * 
     * @throws ColumnDefinitionException
     *             When the index property of a table column definition is found
     *             to exceed the bounds of the List of
     *             {@link com.ui.components.table.ColumnDefinition}.
     */
    private void validateColumnDefs() throws ColumnDefinitionException {
        TableColumnModel model = this.getColumnModel();
        int colCount = model.getColumnCount();
        Enumeration<TableColumn> e = model.getColumns();
        while (e.hasMoreElements()) {
            TableColumn c = e.nextElement();
            if (c.getModelIndex() >= colCount) {
                StringBuffer buf = new StringBuffer();
                buf.append("The index for table column definition, ");
                buf.append(c.getHeaderValue());
                buf.append(", is out or range [Index=");
                buf.append(c.getModelIndex());
                buf.append(", Column Total=");
                buf.append(colCount);
                buf.append("].  Check ColumnDefinition configuration for JTable instance, ");
                buf.append(this.getClass().getName());
                this.msg = buf.toString();
                throw new ColumnDefinitionException(this.msg);
            }
        }
    }

    /**
     * Adds a row the grid's data model.
     * <p>
     * The row added to the data model is also highlighted.
     * 
     * @param data
     *            an arbitrary object representing the row to add.
     * @return the row index of the item added.
     */
    public int addGridRow(Object data) {
        DataGridModel model = (DataGridModel) this.getModel();
        int row = model.addRow(data);
        this.scrollToVisible(row);

        return row;
    }

    /**
     * Removes the row specified as <i>index</i> from the grid.
     * 
     * @param index
     *            the index of the model row
     * @return 1 for success and -1 for failure.
     */
    public int removeGridRow(int index) {
        DataGridModel model = (DataGridModel) this.getModel();
        int viewIndex = this.convertRowIndexToModel(index);
        int rc = model.removeRow(viewIndex);
        AbstractTableModel model2 = (AbstractTableModel) this.getModel();
        model2.fireTableRowsDeleted(0, 0);
        return rc;
    }

    /**
     * Refreshes the Grid with the latest changes.
     * 
     */
    public void refreshGrid() {
        AbstractTableModel model = (AbstractTableModel) this.getModel();
        try {
            model.fireTableDataChanged();
        } catch (Throwable e) {
            this.revalidate();
            this.repaint();
        }
    }

    /**
     * Scrolls the JTable to the specified row.
     * 
     * @param rowIndex
     */
    public void scrollToVisible(int rowIndex) {
        // rowIndex = rowIndex - 1;
        this.getSelectionModel().setSelectionInterval(rowIndex, rowIndex);
        Rectangle rect = new Rectangle(this.getCellRect(rowIndex, 0, true));
        this.scrollRectToVisible(rect);
    }

    /**
     * Returns one or more arbitrary objects that corresponds to the total
     * number of selected rows in the JTable view.
     * 
     * @return a List of arbitrary model objects that are stored at the selected
     *         indexes. Returns null when there are no rows selected.
     */
    public List<Object> getAllSelectedRowData() {
        int rows[] = this.getSelectedRows();
        if (rows.length == 0) {
            return null;
        }
        List<Object> list = new ArrayList<Object>();
        for (int ndx = 0; ndx < rows.length; ndx++) {
            Object item = this.getSelectedRowData(rows[ndx]);
            list.add(item);
        }
        return list;
    }

    /**
     * Returns the model object that corresponds to the selected row of the
     * JTable view.
     * 
     * @param viewRow
     *            the index of the selected row
     * @return an arbitrary object that stored in the model at position
     *         <i>viewRow</i>. Returns null when <i>viewRow</i> is a number less
     *         than zero.
     */
    public Object getSelectedRowData(int viewRow) {
        if (viewRow < 0) {
            return null;
        }
        DataGridModel model = (DataGridModel) this.getModel();
        int mappedRow = this.convertRowIndexToModel(viewRow);
        return model.getSelectedRowData(mappedRow);
    }

    /**
     * Alternates the row colors
     */
    public Component prepareRenderer(TableCellRenderer renderer, int rowIndex,
            int vColIndex) {
        Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);

        // Default backgroune and text to normal colors
        c.setForeground(this.normFg);
        c.setBackground(this.normBg);

        boolean rowSelected = isCellSelected(rowIndex, vColIndex);
        if (rowSelected) {
            c.setBackground(this.selBg);
            c.setForeground(this.selFg);
            return c;
        }
        if (rowIndex % 2 == 0 && !rowSelected) {
            c.setBackground(this.altBg);
            c.setForeground(this.altFg);
        }
        else {
            // If not shaded, match the table's background
            c.setBackground(getBackground());
        }
        return c;
    }

    /**
     * Assings a {@link NumericStringComparator} comparator to every column
     * specified in <i>cols</i> for the purpose of sorting String values as
     * numerics.
     * 
     * @param cols
     *            an interger array containing the column indexes that require a
     *            {@link NumericStringComparator} instance.
     * @throws RuntimeException
     *             if a column's index is outside the range of the table's
     *             model.
     */
    public void setNumericColumnSorter(int cols[]) {
        if (this.getRowSorter() == null) {
            return;
        }

        DefaultRowSorter sorter = (DefaultRowSorter) this.getRowSorter();
        int colCount = cols.length;
        try {
            for (int ndx = 0; ndx < colCount; ndx++) {
                sorter.setComparator(cols[ndx], new NumericStringComparator());
            }
        } catch (Exception e) {
            String msg = "Error occurred attempting to setup numeric String column comparator for table grid row sorter";
            logger.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    public void setDateColumnSorter(int cols[]) {
        if (this.getRowSorter() == null) {
            return;
        }

        DefaultRowSorter sorter = (DefaultRowSorter) this.getRowSorter();
        int colCount = cols.length;
        try {
            for (int ndx = 0; ndx < colCount; ndx++) {
                sorter.setComparator(cols[ndx], new DateStringComparator());
            }
        } catch (Exception e) {
            String msg = "Error occurred attempting to setup Date String column comparator for table grid row sorter";
            logger.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    /*
     * This method picks good column sizes. If all column heads are wider than
     * the column's cells' contents, then you can just use
     * column.sizeWidthToFit().
     */
    private void initColumnSizes() {
        DataGridModel model = (DataGridModel) this.getModel();
        TableColumn column = null;
        Component comp = null;
        int headerWidth = 0;
        int cellWidth = 0;
        // Object[] longValues = model.longValues;
        TableCellRenderer headerRenderer = this.getTableHeader()
                .getDefaultRenderer();

        for (int i = 0; i < this.colDefs.size(); i++) {
            TableColumnDefinition def = this.colDefs.get(i);
            column = this.getColumnModel().getColumn(i);

            comp = headerRenderer.getTableCellRendererComponent(null,
                    column.getHeaderValue(), false, false, 0, 0);
            headerWidth = comp.getPreferredSize().width;

            comp = this.getDefaultRenderer(model.getColumnClass(i))
                    .getTableCellRendererComponent(this, def.getDisplayValue(),
                            false, false, 0, i);
            cellWidth = comp.getPreferredSize().width;

            System.out.println("Initializing width of column " + i + ". "
                    + "headerWidth = " + headerWidth + "; cellWidth = "
                    + cellWidth);

            column.setPreferredWidth(Math.max(headerWidth, cellWidth));
        }
    }

    private int calcTotalColumnWidth() {
        int sum = 0;
        for (int ndx = 0; ndx < this.colDefs.size(); ndx++) {
            TableColumnDefinition def = (TableColumnDefinition) this.colDefs
                    .get(ndx);
            sum += def.getWidth();
        }
        return sum;
    }

    /**
     * Adds an instance of {@link CustomSelectionListener} interface to the List
     * of Listeners.
     * 
     * @param listener
     *            an instance of a class that has implemented the
     *            {@link CustomSelectionListener} interface.
     */
    public void addItemSelectionListener(CustomSelectionListener listener) {
        if (this.listenerList == null) {
            this.listenerList = new EventListenerList();
        }
        this.listenerList.add(CustomSelectionListener.class, listener);
    }

    /**
     * Removes a listener from the list of {@link CustomSelectionListener}
     * implementations.
     * 
     * @param listener
     *            an instance of {@link CustomSelectionListener} interface
     *            targeted for removal.
     */
    public void removeItemSelectionListener(CustomSelectionListener listener) {
        this.listenerList.remove(CustomSelectionListener.class, listener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event
     * .ListSelectionEvent)
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        super.valueChanged(e);

        // This line of code will prevent this handler from being called twice.
        // Basically, this event is called when the mouse button is pressed down
        // and triggered again when the mouse button is released. Now, when
        // navigating the rows of the JTable using the up/down arrow keys, this
        // handler is only triggered once.
        if (e.getValueIsAdjusting()) {
            return;
        }
        int row = this.getSelectedRow();
        Object rowItem = this.getSelectedRowData(row);
        CustomItemSelectedEvent evt = new CustomItemSelectedEvent(this,
                rowItem, DataGrid.EVENT_ID_SELECTIONCHANGED);
        evt.setSelectedRowIndex(row);
        this.evtDispatcher.fireEvent(evt);
    }

    /**
     * An extension of MouseAdapter to capture and manage mouse related events
     * regarding the JTable view.
     * 
     * @author rterrell
     *
     */
    public class TableViewMouseAdapter extends MouseAdapter {
        private DataGrid table;

        /**
         * Create a TableViewMouseAdapter with a know parent object.
         * 
         * @param parent
         */
        private TableViewMouseAdapter(DataGrid table) {
            this.table = table;
            return;
        }

        /**
         * Responds to the user's interactiion with the DataGrid's rows via
         * mouse clicks.
         * <p>
         * The corresponding model object to the selected row is obtained and is
         * used as a property of the {@link com.ui.event.CustomSelectionEvent}
         * which is later dispatched.
         * 
         * @param e
         *            an instance of {@link MouseEvent}
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            // Get model object behind selected row.
            int row = table.getSelectedRow();
            if (e.getClickCount() == 1) {
                table.selection = table.getSelectedRowData(row);
            }
            // Invoke the proper event handler for when the user double
            // clicks a row by firing the CustomItemDoubleClickedEvent event.
            if (e.getClickCount() == 2) {
                // Prepare to fire custom event
                CustomItemDoubleClickedEvent evt = new CustomItemDoubleClickedEvent(
                        this, table.selection, DataGrid.EVENT_ID_DOUBLECLICKED);
                table.evtDispatcher.fireEvent(evt);
            }
        }
    } // end MainMenuMouseAdapter
}
