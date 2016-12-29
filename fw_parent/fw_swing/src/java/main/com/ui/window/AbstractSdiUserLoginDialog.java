package com.ui.window;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import com.InvalidDataException;
import com.ProcessDataFailureException;
import com.api.security.User;
import com.nv.security.UserSecurityException;
import com.nv.security.UserSecurityManager;
import com.nv.security.UserVo;
import com.util.AppUtil;

/**
 * This is abstract user login screen which accepts and validates the user's
 * credentials.
 * 
 * @author rterrell
 *
 */
public abstract class AbstractSdiUserLoginDialog extends AbstractSdiModalWindow {

    private static final long serialVersionUID = 1062235214399626383L;

    private static final Logger logger = Logger
            .getLogger(AbstractSdiUserLoginDialog.class);

    public static final int PASSWORD_MAX_LEN = 6;

    private JTextField userIdField;

    private JPasswordField passwordField;

    /**
     * Create an AbstractUserLoginDialog object with a known parent, window size
     * properties, window location coordinates, and the window title.
     * 
     * @param owner
     *            the parent window
     * @param size
     *            the size coordinates
     * @param pos
     *            the location coordinates
     * @param winTitle
     *            the title of the window.
     */
    public AbstractSdiUserLoginDialog(Frame owner, Dimension size, Point pos,
            String winTitle) {
        super(owner, null, size, pos, winTitle);
        return;
    }

    /**
     * Create an AbstractUserLoginDialog object with a known parent, window size
     * properties, window location coordinates, and the window title.
     * 
     * @param owner
     *            the parent window
     * @param data
     *            the data to pass to this window
     * @param size
     *            the size coordinates
     * @param pos
     *            the location coordinates
     * @param winTitle
     *            the title of the window.
     */
    public AbstractSdiUserLoginDialog(Frame owner, Object data, Dimension size,
            Point pos, String winTitle) {
        super(owner, data, size, pos, winTitle);
        return;
    }

    /**
     * Initializes the dialog with visual components.
     * 
     * @throws InvalidDataException
     */
    @Override
    protected void initDialog() {
        super.initDialog();
        okButton.setText("Submit");
        okButton.setActionCommand(WindowActionListener.ACTION_COMMAND_PROCESS_CLOSE);
        this.setResizable(false);
        return;
    }

    /**
     * Uses the MigLayout to setup the dialog controls
     */
    @Override
    public JPanel createContentLayout() {
        // Use the MigLayout to setup window
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new MigLayout("", "[][][grow]", "[][]15[]"));
        JLabel lblNewLabel = new JLabel("User Id");
        mainPanel.add(lblNewLabel, "cell 1 0,alignx right");

        this.userIdField = new JTextField();
        mainPanel.add(this.userIdField, "cell 2 0,growx");

        JLabel lblNewLabel_1 = new JLabel("Password");
        mainPanel.add(lblNewLabel_1, "cell 1 1,alignx right");
        this.passwordField = new JPasswordField();
        mainPanel.add(this.passwordField, "cell 2 1,growx");

        this.msgLbl = new JLabel("Error Message Area");
        this.msgLbl.setVisible(false);
        mainPanel.add(this.msgLbl, "cell 1 2 3 1");
        return mainPanel;
    }

    /**
     * Accepts, validates, packages, and returns the input login id and password
     * as a Assoc object.
     * <p>
     * Validation errors are commumicated to the user in the message area.
     * 
     * @return an instance of {@link Assoc} containing the user's login id and
     *         password.
     * @throws InvalidDataException
     *             validation failure
     */
    @Override
    public Object getInputData() throws InvalidDataException {
        try {
            this.validateInput();
        } catch (InvalidDataException e) {
            AppUtil.showMessage(this.msgLbl, e.getMessage(), true);
            logger.error("Login validation errors were found", e);
            throw e;
        }
        User user = new UserVo();

        String userId = this.userIdField.getText();
        user.setLoginId(userId);
        String pw = String.valueOf(this.passwordField.getPassword());
        user.setPassword(pw);
        return user;
    }

    /**
     * Authenticates the user by querying the database with the user's
     * credentials stored in <i>data</i>.
     * <p>
     * 
     * @param data
     *            an instance of {@link Assoc} at runtime.
     * @return the security level of the user
     * @throws ProcessDataFailureException
     *             when the user's security level is not equivalent to a
     *             supervisor or the user's login and/or password is invalid.
     */
    @Override
    public Object processData(Object data) throws ProcessDataFailureException {
        User user = (User) data;

        // Authenticate user.
        UserSecurityManager mgr = new UserSecurityManager();
        try {
            this.authenticateUser(user);
            // User was authenticated successfully. Now update the security
            // token of valid user
            mgr.initUserSecurity();
            mgr.updateSecurityToken(user);
            return UserSecurityManager.getSecurityToken();
        } catch (Exception e) {
            mgr.resetSecurityToken();
            throw new ProcessDataFailureException("User authentication failed",
                    e);
        }
    }

    /**
     * Implement this method to perform the actual user authetication.
     * 
     * @param user
     *            the user to be authenticated
     * @throws UserSecurityException
     */
    protected abstract void authenticateUser(User user)
            throws UserSecurityException;

    /**
     * Validates the login screen by ensuring that the user id and password
     * values exists.
     * 
     * @throws InvalidDataException
     *             <ul>
     *             <li>User id is not entered</li>
     *             <li>Password is not entered</li>
     *             </ul>
     * 
     */
    private void validateInput() throws InvalidDataException {
        if (this.userIdField.getText() == null
                || this.userIdField.getText().length() == 0) {
            this.msg = "User Id is required";
            AbstractSdiUserLoginDialog.logger.error(this.msg);
            throw new InvalidDataException(this.msg);
        }
        String pw = String.valueOf(this.passwordField.getPassword());
        if (pw == null || pw.equals("")) {
            this.msg = "Password is required";
            AbstractSdiUserLoginDialog.logger.error(this.msg);
            throw new InvalidDataException(this.msg);
        }
        return;
    }

}
