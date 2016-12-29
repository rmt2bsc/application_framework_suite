package com.api.messaging.email.pop;

import com.api.config.old.ProviderConfig;
import com.api.messaging.MessagingResourceFactory;

/**
 * Creates instances of POP3 email related classes.
 * 
 * @author RTerrell
 * 
 */
public class Pop3Factory extends MessagingResourceFactory {

    /**
     * Creates POP3 provider where the configuration data is supplied by the
     * user.
     * 
     * @param pop3ServerName
     * @param authenticate
     * @param userId
     * @param password
     * @return
     */
    public static ProviderConfig getPop3ConfigInstance(String pop3ServerName,
            boolean authenticate, String userId, String password) {
        ProviderConfig config = MessagingResourceFactory.getConfigInstance();
        config.setHost(pop3ServerName);
        config.setAuthenticate(authenticate);
        if (authenticate) {
            config.setUserId(userId);
            config.setPassword(password);
        }
        return config;
    }

}
