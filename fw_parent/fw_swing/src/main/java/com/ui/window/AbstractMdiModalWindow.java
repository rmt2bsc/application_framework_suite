package com.ui.window;

import java.awt.AWTEvent;
import java.awt.ActiveEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.MenuComponent;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.EventListenerList;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import org.apache.log4j.Logger;

import com.InvalidDataException;
import com.InvalidGuiDataException;
import com.ProcessDataFailureException;
import com.api.config.AppPropertyPool;
import com.ui.event.EventDispatcher;
import com.ui.event.EventDispatcherImpl;
import com.util.AppUtil;

/**
 * A dialog to be used throughout the application which pocesses common
 * funcationality for all its descendents.
 * <p>
 * The most outstanding feature of this abstraction is its modal
 * characteristics. This object is best used as a window for displaying form
 * that captures the user's input blocking the application from continuing until
 * the object (window) is closed., hence modal type.
 * 
 * @author rterrell
 *
 */
public abstract class AbstractMdiModalWindow extends JInternalFrame implements
        ActionListener, InternalFrameListener, WindowActionListener {

    private static final long serialVersionUID = -2001475361405763220L;

    private static final Logger logger = Logger
            .getLogger(AbstractMdiModalWindow.class);

    /**
     * The main content panel component
     */
    protected JPanel contentPanel;

    /**
     * A panel component for managing command buttons.
     */
    protected JPanel buttonPane;

    /**
     * The OK Command button
     */
    protected JButton okButton;

    /**
     * The Cancel command button
     */
    protected JButton cancelButton;

    /**
     * The window title.
     */
    protected String winTitle;

    /**
     * A general variable for holding message text.
     */
    protected String msg;

    /**
     * A variable for holding the windows's postion coordinates
     */
    protected Point position;

    /**
     * A variable for holding the window's width and height
     */
    protected Dimension size;

    /**
     * A variable for holding the data captured from the user's input.
     */
    protected Object inData;

    /**
     * A {@link JLabel} componenet serving as the window's message area.
     */
    protected JLabel msgLbl;

    private Object results;

    // Indicate that window should be modal
    protected boolean modal = false;

    protected EventDispatcher evtDispatcher;

    /**
     * Creates a AbstractMdiModalWindow without a owner, without any size and
     * positioning, and without a sub title.
     */
    public AbstractMdiModalWindow() {
        super();
        this.initDialog();
        this.updateView();
    }

    /**
     * Creates a AbstractMdiModalWindow with an owner Dialog and without a sub
     * title.
     * 
     * @param title
     *            window title
     */
    public AbstractMdiModalWindow(String title) {
        super(title);
        this.initDialog();
        this.updateView();
    }

    /**
     * Creates a AbstractMdiModalWindow with an owner Dialog and without a sub
     * title.
     * 
     * @param title
     *            window title
     * @param data
     *            an arbitrary object representing the data that is to displayed
     *            and maniupulated by the dialog.
     */
    public AbstractMdiModalWindow(String title, Object data) {
        super(title);
        this.inData = data;
        this.initDialog();
        this.updateView();
    }

    /**
     * Creates a AbstractMdiModalWindow with a sub title.
     * 
     * @param title
     *            window title
     * @param data
     *            an arbitrary object representing the data that is to displayed
     *            and maniupulated by the dialog.
     * @param size
     *            the window's dimensions in terms of size and position
     * @param pos
     *            the location where the window is to be positioned.
     * @param winTitle
     *            a String that will be appended to the title of the window.
     */
    public AbstractMdiModalWindow(String title, Object data, Dimension size,
            Point pos) {
        super();
        this.size = size;
        this.position = pos;
        this.winTitle = title;
        this.initDialog();
        this.updateView();
    }

    /**
     * Performs common constructor related initialization tasks.
     */
    protected void initDialog() {
        // Setup event listener
        this.listenerList = new EventListenerList();
        // Setup Event dispatcher
        this.evtDispatcher = new EventDispatcherImpl(this.listenerList);

        this.setResizable(true);

        // Initialize message label
        this.msgLbl = new JLabel();

        // Manage window size and postion
        int posX = 100;
        int posY = 100;
        int locW = 450;
        int locH = 300;
        if (this.position != null) {
            posX = this.position.x;
            posY = this.position.y;
        }
        if (this.size != null) {
            locW = this.size.width;
            locH = this.size.height;
        }
        this.setBounds(posX, posY, locW, locH);

        this.getContentPane().setLayout(new BorderLayout());

        this.contentPanel = this.createContentLayout();

        // The content panel could potentially be null if createContentDisplay
        // is not properly implemented
        if (this.contentPanel == null) {
            this.msg = "The createContentDisplay method of "
                    + this.getClass().getName()
                    + " must be implemented in such a way as to initialize the main content panel of the framework with a valid instance of JPanel";
            logger.error(this.msg);
            throw new RuntimeException(this.msg);
        }
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.getContentPane().add(contentPanel, BorderLayout.CENTER);

        // Build Button panel
        buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));

        okButton = new JButton("OK");
        okButton.setActionCommand(WindowActionListener.ACTION_COMMAND_PROCESS);
        okButton.addActionListener(this);
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(this.okButton);

        cancelButton = new JButton("Cancel");
        cancelButton
                .setActionCommand(WindowActionListener.ACTION_COMMAND_CANCEL);
        cancelButton.addActionListener(this);
        buttonPane.add(cancelButton);

        // Add button panel to the main content pane
        this.getContentPane().add(this.buttonPane, BorderLayout.SOUTH);

        // Set window title.
        String appTitle = AppPropertyPool.getProperty(AppUtil.PROP_APP_TITLE);
        if (this.winTitle != null) {
            appTitle = this.winTitle;
        }
        this.setTitle(appTitle);

        // this.setModal(true);

        this.addInternalFrameListener(this);

        // Show total records retrieved count
        this.displayRecordCount();

        // // Center Window
        // this.setLocationRelativeTo(null);

        logger.info("Base Modal Internal Frame Dialog initialized");
    }

    /**
     * Updates the view with data
     */
    public void updateView() {
        return;
    }

    /**
     * Handles the click actions of the OK and Cancel JButtons
     * 
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (this.msgLbl != null) {
            this.msgLbl.setVisible(false);
        }
        String command = e.getActionCommand();
        if (command.equals(WindowActionListener.ACTION_COMMAND_PROCESS)
                || command
                        .equals(WindowActionListener.ACTION_COMMAND_PROCESS_CLOSE)) {
            this.processAction(e);
            // Window processing was successful.
            if (command
                    .equals(WindowActionListener.ACTION_COMMAND_PROCESS_CLOSE)) {
                this.dispose();
            }
            return;
        }
        if (command.equals(WindowActionListener.ACTION_COMMAND_CANCEL)) {
            this.closeWithoutProcessingAction(e);
            return;
        }
        return;
    }

    /**
     * Handler for the OK Button.
     * 
     * @param e
     */
    protected Object processAction(ActionEvent e) {
        try {
            Object data = this.getInputData();
            Object results = this.processData(data);
            return results;
        } catch (InvalidDataException e1) {
            this.msg = "Invalid data error occured while gathering input data from window";
            logger.error(this.msg, e1);
            e1.printStackTrace();
            throw new RuntimeException(this.msg, e1);
        } catch (ProcessDataFailureException e1) {
            this.msg = "General error occured while processng window data";
            logger.error(this.msg, e1);
            e1.printStackTrace();
            throw new RuntimeException(this.msg, e1);
        }
    }

    /**
     * Handler for the Cancel button.
     * 
     * @param e
     */
    protected void closeWithoutProcessingAction(ActionEvent e) {
        this.dispose();
        return;
    }

    /**
     * A stub method for displaying a message concerning the total number of
     * records retrieved.
     * <p>
     * When implementing this method, be aware that its invocation is at the end
     * of the initDialog method.
     */
    protected void displayRecordCount() {
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
     */
    @Override
    public void internalFrameOpened(InternalFrameEvent e) {
        System.out
                .println("InternalFrameListener method called: internalFrameOpened.");
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
     */
    @Override
    public void internalFrameClosing(InternalFrameEvent e) {
        System.out
                .println("InternalFrameListener method called: internalFrameClosing.");
        this.closeWithoutProcessingAction(null);
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
     */
    @Override
    public void internalFrameClosed(InternalFrameEvent e) {
        System.out
                .println("InternalFrameListener method called: internalFrameClosed.");
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
     */
    @Override
    public void internalFrameIconified(InternalFrameEvent e) {
        System.out
                .println("InternalFrameListener method called: internalFrameIconified.");
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent
     * )
     */
    @Override
    public void internalFrameDeiconified(InternalFrameEvent e) {
        System.out
                .println("InternalFrameListener method called: internalFrameDeiconified.");
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
     */
    @Override
    public void internalFrameActivated(InternalFrameEvent e) {
        System.out
                .println("InternalFrameListener method called: internalFrameActivated.");
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent
     * )
     */
    @Override
    public void internalFrameDeactivated(InternalFrameEvent e) {
        System.out
                .println("InternalFrameListener method called: internalFrameDeactivated.");
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ui.window.WindowActionListener#createContentLayout()
     */
    @Override
    public JPanel createContentLayout() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ui.window.WindowActionListener#getInputData()
     */
    @Override
    public Object getInputData() throws InvalidGuiDataException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ui.window.WindowActionListener#processData(java.lang.Object)
     */
    @Override
    public Object processData(Object data) throws ProcessDataFailureException {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getResults() {
        return results;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JInternalFrame#show()
     */
    @Override
    public void show() {
        super.show();
        if (this.modal) {
            startModal();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JComponent#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (modal) {
            if (aFlag) {
                startModal();
            }
            else {
                stopModal();
            }
        }
    }

    private synchronized void startModal() {
        try {
            if (SwingUtilities.isEventDispatchThread()) {
                EventQueue theQueue = getToolkit().getSystemEventQueue();
                while (isVisible()) {
                    AWTEvent event = theQueue.getNextEvent();
                    Object source = event.getSource();
                    boolean dispatch = true;

                    if (event instanceof MouseEvent) {
                        MouseEvent e = (MouseEvent) event;
                        MouseEvent m = SwingUtilities.convertMouseEvent(
                                (Component) e.getSource(), e, this);
                        if (!this.contains(m.getPoint())
                                && e.getID() != MouseEvent.MOUSE_DRAGGED)
                            dispatch = false;
                    }

                    if (dispatch)
                        if (event instanceof ActiveEvent) {
                            ((ActiveEvent) event).dispatch();
                        }
                        else if (source instanceof Component) {
                            ((Component) source).dispatchEvent(event);
                        }
                        else if (source instanceof MenuComponent) {
                            ((MenuComponent) source).dispatchEvent(event);
                        }
                        else {
                            System.err.println("Unable to dispatch: " + event);
                        }
                }
            }
            else {
                while (isVisible()) {
                    wait();
                }
            }
        } catch (InterruptedException ignored) {
        }

    }

    private synchronized void stopModal() {
        notifyAll();
    }

    public void setModal(boolean modal) {
        this.modal = modal;
    }

    public boolean isModal() {
        return this.modal;
    }

}
