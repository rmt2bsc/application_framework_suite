package com.ui.window;

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import org.apache.log4j.Logger;

import com.AppInitException;
import com.api.config.AppPropertyPool;
import com.api.config.old.StandAloneCoreSysConfigurator;
import com.nv.security.GuiSecurityToken;
import com.nv.security.UserSecurityManager;
import com.util.AppUtil;

/**
 * Abstract class that provides basic functionality for creating the main
 * application frame.
 * <p>
 * The main frame requires that certain properties are available in the
 * AppParms.properties file.
 * 
 * @author rterrell
 *
 */
public abstract class AbstractMdiMainFrame extends JFrame {

    private static Logger logger;

    private static final long serialVersionUID = 390363259463959313L;

    private PostUiInitializer worker;

    private JPanel contentPane;

    private AppUtil appUtil;

    protected JDesktopPane desktop;

    /**
     * A Worker Thread inner class used to perform additional initialization of
     * GUI at the decendent level.
     * 
     * @author rterrell
     *
     */
    private class PostUiInitializer extends SwingWorker<Void, Void> {
        private AbstractMdiMainFrame parent;

        protected PostUiInitializer(AbstractMdiMainFrame frame) {
            this.parent = frame;
        }

        @Override
        protected Void doInBackground() throws Exception {
            GuiSecurityToken token = UserSecurityManager.getSecurityToken();
            try {
                this.parent.doPostInitialization();
            } catch (Exception e) {
                StringBuffer buf = new StringBuffer();
                buf.append("An error occurred during an attempt to perform post initialization routine of main frame.\n");
                buf.append("Consult the application support team.\n\n");
                buf.append("Additional technical information:\n");
                buf.append(e.getMessage());
                AbstractMdiMainFrame.logger
                        .fatal("Application failed to start due to post intialization error!");
                AbstractMdiMainFrame.logger.fatal(buf, e);
                JOptionPane.showMessageDialog(this.parent, buf.toString(),
                        "Initialization Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
            return null;
        }
    }

    /**
     * 
     */
    public AbstractMdiMainFrame() {
        String msg = null;
        // do initializations

        this.desktop = new JDesktopPane();
        try {
            this.initializeApp();
        } catch (Exception e) {
            msg = "Unable to initialize application";
            throw new AppInitException(msg, e);
        }

        // Setup content pain of main frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Setup frame's dimensions
        int fx = Integer.parseInt(AppPropertyPool.getProperty("frame.main.x")
                .trim());
        int fy = Integer.parseInt(AppPropertyPool.getProperty("frame.main.y")
                .trim());
        int fw = Integer.parseInt(AppPropertyPool.getProperty(
                "frame.main.width").trim());
        int fh = Integer.parseInt(AppPropertyPool.getProperty(
                "frame.main.height").trim());
        setBounds(fx, fy, fw, fh);

        // contentPane = new JPanel();
        // contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        // contentPane.setLayout(new BorderLayout(0, 0));

        setContentPane(this.desktop);
        // Make dragging a little faster but perhaps uglier.
        desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
        ImageIcon icon = AppUtil.getAppIcon();
        if (icon != null) {
            setIconImage(icon.getImage());
        }

        // Determine if we should pack the main frame
        boolean packFrame = Boolean.parseBoolean(AppPropertyPool
                .getProperty("frame.main.pack"));
        if (packFrame) {
            this.pack();
        }
    }

    /**
     * Performs initialization in terms of the logger, setting up environment
     * variables, creating security token, and intializing iBatis.
     * 
     * @throws AppInitException
     *             When any phase of the intialization process errors.
     */
    protected void initializeApp() {
        String msg = null;
        this.appUtil = new AppUtil();

        try {
            // init logger
            StandAloneCoreSysConfigurator config = new StandAloneCoreSysConfigurator();
            AbstractMdiMainFrame.logger = Logger
                    .getLogger(AbstractMdiMainFrame.class);
            AbstractMdiMainFrame.logger.info("Logger initialization completed");

            // Set application title.
            String appTitle = AppPropertyPool
                    .getProperty(AppUtil.PROP_APP_TITLE);
            this.setTitle(appTitle);

            // Validate database connection
            this.worker = new PostUiInitializer(this);
            this.worker.execute();
        } catch (Exception e) {
            msg = "Unable to start Application due to pre initialization errors.  Consult application support team and logs.";
            throw new AppInitException(msg, e);
        }
    }

    protected abstract void doPostInitialization() throws AppInitException;

}
