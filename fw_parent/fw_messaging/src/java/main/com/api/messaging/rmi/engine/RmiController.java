package com.api.messaging.rmi.engine;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;

import com.api.config.old.ProviderConfig;
import com.api.messaging.rmi.client.RmiClientFactory;
import com.api.web.controller.AbstractServlet;

/**
 * A RMI controller which runs as a servlet and is responsible for setting up
 * the RMI environment.
 * <p>
 * The basic functionality of this servlet is to create the RMI engine in a
 * separate thread. This servlet is dependent upon the availability of
 * server.policy file. The servlet configuration should contain the init
 * parameter, <i>ServerPolicyFile</i>, which its parameter value points to the
 * location of the the security file, server.policy.
 * 
 * @author Roy Terrell
 * 
 */
public class RmiController extends AbstractServlet {

    private static final long serialVersionUID = -8553462605084047068L;

    private static final Logger logger = Logger.getLogger(RmiController.class);

    private static final String POLICY_FILE_PROP = "ServerPolicyFile";

    private RmiEngine engine;

    /**
     * Create a RmiController
     */
    public RmiController() {
        super();
    }

    /**
     * Create and start the RMI Engine which is responsible for initializing the
     * RMI runtime environment.
     * 
     * @param config
     *            an instance of {@link ServletConfig}
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        String policyFile = config.getInitParameter(POLICY_FILE_PROP);
        RmiClientFactory f = new RmiClientFactory();
        ProviderConfig rmiConfig = f.createProviderConfigFromPropertiesFile();
        rmiConfig.setPolicyFile(policyFile);
        this.engine = new RmiEngine(rmiConfig);
        Thread t = new Thread(this.engine);
        t.start();
        RmiController.logger.info("RMI Controller initializaton completed.");
        return;
    }

    /**
     * Invokes the RMI engine's process that removes all of the remote objects
     * contained in RMI registry.
     * <p>
     * To prevent the premature garbage collection of each registterd remote
     * object, the RMI Engine maintains a strong reference of each registered
     * remote object. This in turn causes the RMI server to run indefinitely,
     * hence the idling of the web server shutdown process. Since the RMI server
     * is created from within the web server process, clearing the RMI registry
     * will sort of mimic the applying the Cntrl-C command for a RMi registry
     * running standalone.
     */
    @Override
    public void destroy() {
        super.destroy();
        this.engine.clearRegistry();
        this.engine = null;
    }

}
