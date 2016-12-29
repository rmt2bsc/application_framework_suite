package com.api.messaging.email.smtp;

import com.api.config.ConfigConstants;
import com.api.config.old.ProviderConfig;
import com.api.messaging.MessagingResourceFactory;
import com.api.messaging.email.EmailException;

/**
 * Creates instances of SMTP email related classes.
 * 
 * @author RTerrell
 * 
 */
public class SmtpFactory extends MessagingResourceFactory {

    /**
     * Creates an instance of SmtpApi by which the SMTP configuration
     * information is discovered internally by the system.
     * 
     * @return {@link com.api.messaging.email.smtp.SmtpApi SmtpApi} or null when
     *         class cannot be instantiated.
     */
    public static final SmtpApi getSmtpInstance() {
        ProviderConfig config = SmtpFactory.getSmtpConfigInstance();
        return SmtpFactory.getSmtpInstance(config);
    }

    /**
     * Creates an instance of SmtpApi where the provider configuration data is
     * supplied by the user.
     * 
     * @param smtpServerName
     * @param authenticate
     * @param userId
     * @param password
     * @return {@link com.api.messaging.email.smtp.SmtpApi SmtpApi} or null when
     *         class cannot be instantiated.
     */
    public static final SmtpApi getSmtpInstance(String smtpServerName,
            boolean authenticate, String userId, String password) {
        ProviderConfig config = SmtpFactory.getSmtpConfigInstance(
                smtpServerName, authenticate, userId, password);
        return SmtpFactory.getSmtpInstance(config);
    }

    /**
     * Creates an instance of SmtpApi that is initialized with a provider
     * configuration instance.
     * 
     * @param config
     *            an instance of {@link ProviderConfig}
     * @return {@link com.api.messaging.email.smtp.SmtpApi SmtpApi} or null when
     *         class cannot be instantiated.
     */
    public static final SmtpApi getSmtpInstance(ProviderConfig config) {
        SmtpApi api = null;
        try {
            api = new SmtpImpl(config);
            return api;
        } catch (EmailException e) {
            return null;
        }
    }

    /**
     * Creates a SMTP provider where the configuration information is obtained
     * as properties from the {@link System} class.
     * 
     * @return {@link com.api.messaging.ProviderConfig ProviderConfig}
     */
    private static ProviderConfig getSmtpConfigInstance() {
        ProviderConfig config = MessagingResourceFactory.getConfigInstance();
        String temp = null;
        temp = System.getProperty(ConfigConstants.PROPNAME_SMTP_SERVER);
        config.setHost(temp);

        // Get Authentication value
        temp = System.getProperty(ConfigConstants.PROPNAME_MAIL_AUTH);
        boolean authRequired = temp == null ? false : new Boolean(temp)
                .booleanValue();
        config.setAuthenticate(authRequired);

        String mailUser = null;
        String mailPassword = null;
        // Get user name and password for mail server if authentication is
        // required
        if (authRequired) {
            // Get User Name
            mailUser = System.getProperty(ConfigConstants.PROPNAME_MAIL_UID);
            // Get Password
            mailPassword = System.getProperty(ConfigConstants.PROPNAME_MAIL_PW);
            config.setUserId(mailUser);
            config.setPassword(mailPassword);
        }
        return config;
    }

    /**
     * Creates SMTP provider where the configuration data is supplied by the
     * user.
     * 
     * @param smtpServerName
     * @param authenticate
     * @param userId
     * @param password
     * @return
     */
    private static ProviderConfig getSmtpConfigInstance(String smtpServerName,
            boolean authenticate, String userId, String password) {
        ProviderConfig config = MessagingResourceFactory.getConfigInstance();
        config.setHost(smtpServerName);
        config.setAuthenticate(authenticate);
        if (authenticate) {
            config.setUserId(userId);
            config.setPassword(password);
        }
        return config;
    }

}
