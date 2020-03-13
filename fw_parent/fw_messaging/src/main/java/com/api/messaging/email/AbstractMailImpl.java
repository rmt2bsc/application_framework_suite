package com.api.messaging.email;

import java.io.Serializable;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import com.SystemException;
import com.api.config.AppPropertyPool;
import com.api.config.ConfigConstants;
import com.api.config.old.ProviderConfig;
import com.api.config.old.ProviderConnectionException;
import com.api.messaging.AbstractMessagingImpl;
import com.api.messaging.MessageException;
import com.api.util.RMT2String;

/**
 * Provides common implementation for email clients.
 * 
 * @author RTerrell
 * 
 */
public abstract class AbstractMailImpl extends AbstractMessagingImpl {

    private Session emailSession;

    protected MimeMessage email;

    protected EmailMessageBean emailBean;

    protected Properties props = new Properties();

    private class SMTPAuthenticator extends javax.mail.Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
            // The "From" email address must be a valid account that can be
            // authenticated
            String email = emailBean.getFromAddress().getAddress();
            List<String> tokens = RMT2String.getTokens(email, "@");
            String username = "invalid_user";
            if (tokens != null && tokens.size() > 0) {
                username = tokens.get(0);
            }
            String password = AppPropertyPool
                    .getProperty(ConfigConstants.PROPNAME_MAIL_PW);
            return new PasswordAuthentication(username, password);
        }
    }

    /**
     * Creates a sessionless AbstractMailImpl instance.
     */
    protected AbstractMailImpl() {
        super();
    }

    /**
     * Releases the email session.
     * 
     * @throws SystemException
     */
    @Override
    public void close() throws SystemException {
        super.close();
        this.emailSession = null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.messaging.AbstractMessagingImpl#getConnection()
     */
    @Override
    public Object connect(ProviderConfig config)
            throws ProviderConnectionException {
        Authenticator auth = new SMTPAuthenticator();

        // Initialize E-Mail Session
        this.emailSession = Session.getDefaultInstance(props, auth);
        // Create a MIME style email message
        this.email = new MimeMessage(this.emailSession);
        return this.emailSession;
    }

    /**
     * Serves as a stub for implementations that have no use for this method
     * 
     * @return Always returns null
     */
    @Override
    public Serializable getMessage() throws MessageException {
        return null;
    }

    /**
     * Serves as a stub for implementations that have no use for this method
     * 
     * @return Always returns null
     */
    @Override
    public Object sendMessage(Serializable data) throws MessageException {
        return null;
    }

    /**
     * Returns the email session.
     * 
     * @return the emailSession
     */
    protected Session getEmailSession() {
        return emailSession;
    }

    /**
     * Sets the email session.
     * 
     * @param emailSession
     *            the emailSession to set
     */
    protected void setEmailSession(Session emailSession) {
        this.emailSession = emailSession;
    }

}
