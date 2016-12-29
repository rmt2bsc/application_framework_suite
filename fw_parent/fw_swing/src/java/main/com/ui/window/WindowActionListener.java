package com.ui.window;

import javax.swing.JPanel;

import com.InvalidGuiDataException;
import com.ProcessDataFailureException;

/**
 * Interface for processing the data belonging to the UI.
 * <p>
 * This interface is most beneficial for UI's that requires the gathering and
 * processing of data before disposing of the UI. For example, a dialog that
 * accepts input from the user, and subsequently the input data is saved to a
 * database.
 * 
 * @author rterrell
 *
 */
public interface WindowActionListener {

    /**
     * The action command to save the contents of window.
     */
    public static final String ACTION_COMMAND_PROCESS = "PROCESS_WINDOW";

    /**
     * The action command to save the contents of the winodow and close
     * immediately after success.
     */
    public static final String ACTION_COMMAND_PROCESS_CLOSE = "PROCESS_WINDOW_AND_CLOSE";

    /**
     * The action command to save th contents of the winodow but do not close
     * the window.
     */
    public static final String ACTION_COMMAND_SAVE = "SAVE_WINDOW_ONLY";

    /**
     * The action command to close the window without saving.
     */
    public static final String ACTION_COMMAND_CANCEL = "CANCEL_WINDOW";

    /**
     * The action command to edit selected row.
     */
    public static final String ACTION_COMMAND_EDIT = "EDIT";

    /**
     * The action command to add.
     */
    public static final String ACTION_COMMAND_ADD = "ADD";

    /**
     * The action command to delete.
     */
    public static final String ACTION_COMMAND_DELETE = "DELETE";

    /**
     * The action command to perform search.
     */
    public static final String ACTION_COMMAND_SEARCH = "SEARCH";

    /**
     * The action command to reset.
     */
    public static final String ACTION_COMMAND_RESET = "RESET";

    /**
     * Implementation of this method should provide logic to create intial
     * presentation layout container and return that container as an instance of
     * JPanel.
     */
    JPanel createContentLayout();

    /**
     * Implement this method for the purpose of gathering input information from
     * a UI.
     * 
     * @return an arbitrary object as the input data
     * @throws InvalidGuiDataException
     */
    Object getInputData() throws InvalidGuiDataException;

    /**
     * Implement this method to process the data a UI contains.
     * 
     * @param data
     *            an arbitrary object acting as the input data to be processed.
     * @return an arbitary object representing the results of processing input
     *         data or null if there is nothing to be returned.
     * @throws ProcessDataFailureException
     */
    Object processData(Object data) throws ProcessDataFailureException;
}
