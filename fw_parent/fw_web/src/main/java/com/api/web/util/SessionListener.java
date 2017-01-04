package com.api.web.util;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * The implementation of methods belonging to the HttpSessionListener interface
 * which function to receive notification events when the session becomes active
 * and inactive. This class is configured in the web.xml deployment descriptor
 * of the web application.
 * <p>
 * <b>NOTE: Refactor this class by moving it to the com.api.security package</b>
 * 
 * @author appdev
 * 
 */
public class SessionListener implements HttpSessionListener {

    /**
     * Default constructor
     * 
     */
    public SessionListener() {
        return;
    }

    /**
     * Sends a message to teh console stating that a new session has been
     * established.
     */
    public void sessionCreated(HttpSessionEvent event) {
        Logger logger = Logger.getLogger("SessionListener");
        HttpSession session = event.getSession();
        logger.log(Level.INFO, "Session Notification: New session created");
        logger.log(Level.INFO, "New sessionID: " + session.getId());
    }

    /**
     * Cleans up the user's session by removing the user's session work area and
     * logging the user out of all assoicated applications. Messages are sent to
     * the console revealing the user that has been logged off the system, the
     * id of the session and the start and end timestamp of the session.
     */
    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = null;
        String sessionTimeStamp = null;
        session = event.getSession();
        sessionTimeStamp = RMT2WebUtility.getLogoutMessage(null);

        Logger logger = Logger.getLogger("SessionListener");
        logger.log(Level.INFO, sessionTimeStamp);
        logger.log(Level.INFO,
                "Session has timedout...Session Id: " + session.getId());
        session.invalidate();
        return;
    }

}
