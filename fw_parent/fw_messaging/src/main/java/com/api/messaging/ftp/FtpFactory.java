package com.api.messaging.ftp;

import org.apache.log4j.Logger;

import com.api.config.old.ProviderConfig;
import com.api.messaging.MessagingResourceFactory;

/**
 * Creates instances of FTP related classes.
 * 
 * @author RTerrell
 * 
 */
public class FtpFactory extends MessagingResourceFactory {

    private static final Logger logger = Logger.getLogger(FtpFactory.class);

    /**
     * Creates an instance of FtpApi by which the FTP configuration information
     * is provided via the method parameters.
     * 
     * @param host
     *            the FTP host naem
     * @param port
     *            the FTP port which defaults to "21"
     * @param userId
     *            the FTP user id
     * @param password
     *            the FTP password
     * @param sessionId
     *            a unique id representing the user's session
     * @return instance of {@link FtpApi}
     */
    public static final FtpApi getInstance(String host, String port, String userId, String password, String sessionId) {
        ProviderConfig config = FtpFactory.getConfigInstance();
        config.setHost(host);
        config.setUserId(userId);
        config.setPassword(password);
        config.setPort(port);
        config.setSessionId(sessionId);
        return FtpFactory.getInstance(config);
    }



    /**
     * Creates an instance of FtpApi using the Apache Commons Net library
     * implementation which is initialized with a provider configuration
     * instance.
     * 
     * @param config
     *            an instance of {@link ProviderConfig}
     * @return {@link FtpApi} or null when class cannot be instantiated.
     */
    public static final FtpApi getInstance(ProviderConfig config) {
        FtpApi api = null;
        try {
            api = new ApacheCommonsNetFTPImpl(config);
            return api;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            return null;
        }
    }


}
