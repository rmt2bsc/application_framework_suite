package com.api.messaging.ftp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

import com.InvalidDataException;
import com.SystemException;
import com.api.config.old.ProviderConfig;
import com.api.config.old.ProviderConnectionException;
import com.api.messaging.AbstractMessagingImpl;
import com.api.messaging.MessageException;
import com.api.util.RMT2File;
import com.api.util.RMT2Money;
import com.api.util.assistants.Verifier;
import com.api.util.assistants.VerifyException;

/**
 * This class uses the Apache Commons Net library to implement FtpApi interface.
 * 
 * @author RTerrell
 * 
 */
class ApacheCommonsNetFTPImpl extends AbstractMessagingImpl implements FtpApi {

    private static final Logger logger = Logger.getLogger(ApacheCommonsNetFTPImpl.class);

    private FTPClient ftp;
    private int port;
    private List<String> recursiveListing;

    /**
     * Creates an ApacheCommonsNetFTPImpl object which the identification of the
     * FTP host, user id, and password are provided via a ProviderConfig
     * instance.
     * 
     * @throws FtpException
     *             General intialization errors.
     */
    protected ApacheCommonsNetFTPImpl(ProviderConfig config) throws FtpException {
        super();
        this.config = config;
        this.initApi();
        return;
    }

    /**
     * Performs the bulk of instance initialization. To date, the SMTP name is
     * obtained from the System properties collection and any authentication
     * information that may be needed to establish a valid service connection.
     * respnsible for creating the email session object.
     * 
     * @throws FtpException
     *             Problem obtaining the SMTP host server name from System
     *             properties.
     */
    protected void initApi() throws FtpException {
        this.ftp = new FTPClient();

        try {
            this.connect(this.config);
        } catch (Exception e) {
            throw new FtpException("Unable to establish a connection to the FTP server", e);
        }
        logger.info("Successfully connected and logged into the FTP server, " + this.config.getHost());
    }

    /**
     * Connect and login to the FTP server using Apache Commons Net library
     * 
     * @param config
     *            instance of {@link ProviderConfig} which contains the data
     *            required to establish a connection to the FTP server.
     * @return Boolean true=successful connection and false=connection failed.
     * @throws ProviderConnectionException
     *             <i>config</i> is null or does not contain the required
     *             properties for establishin a FTP server connection or failed
     *             to connect andor login into the FTP server.
     */
    @Override
    public Object connect(ProviderConfig config) throws ProviderConnectionException {
        try {
            this.validate(config);
        } catch (InvalidDataException e) {
            throw new ProviderConnectionException("The ProviderConfig object is invalid", e);
        }

        try {
            this.ftp.connect(config.getHost(), this.port);
            showServerReply(this.ftp, config);
            int replyCode = this.ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                throw new ProviderConnectionException(
                        "FTP server refuses or rejects request to connect operation. Server reply code: " + replyCode);
            }
            boolean success = this.ftp.login(config.getUserId(), config.getPassword());
            showServerReply(this.ftp, config);
            if (!success) {
                throw new ProviderConnectionException("Faild to login into FTP server");
            }
            else {
                logger.info("User, " + config.getUserId() + ", successfully logged into FTP server, " + config.getHost());
                return true;
            }
        } catch (IOException ex) {
            throw new ProviderConnectionException("A general IO error occurred attempting to connect and log into FTP server "
                    + config.getHost());
        }
    }

    private static void showServerReply(FTPClient ftpClient, ProviderConfig config) {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                logger.info("FTP Server [" + config.getHost() + "] : " + aReply);
            }
        }
    }

    /**
     * Validates the ProviderConfig object by ensuring that the object is
     * instantiated, contains the required properties host, port, user id, and
     * password.
     * 
     * @throws InvalidDataException
     *             host, user id, and password are null or port is not a number.
     */
    private void validate(ProviderConfig config) throws InvalidDataException {
        try {
            Verifier.verifyNotNull(config);
        } catch (VerifyException e) {
            throw new InvalidDataException(FtpApiConstants.ERROR_MSG_NULL_PROVIDER_CONFIG);
        }

        try {
            Verifier.verifyNotEmpty(config.getHost());
        } catch (VerifyException e) {
            throw new InvalidDataException(FtpApiConstants.ERROR_MSG_HOST_MISSING);
        }

        try {
            Verifier.verifyNotEmpty(config.getPort());
        } catch (VerifyException e) {
            config.setPort("21");
            logger.info("FTP port was found empty - defaulting to 21");
        }

        try {
            Verifier.verifyTrue(RMT2Money.isNumeric(config.getPort()));
            this.port = Integer.valueOf(config.getPort());
        } catch (VerifyException e) {
            throw new InvalidDataException(FtpApiConstants.ERROR_MSG_PORT_MISSING);
        }

        try {
            Verifier.verifyNotEmpty(config.getUserId());
        } catch (VerifyException e) {
            throw new InvalidDataException(FtpApiConstants.ERROR_MSG_USERID_MISSING);
        }

        try {
            Verifier.verifyNotEmpty(config.getPassword());
        } catch (VerifyException e) {
            throw new InvalidDataException(FtpApiConstants.ERROR_MSG_PASSWORD_MISSING);
        }
    }

    /**
     * Logs out and disconnects user from FTP server
     * 
     * @throws SystemException
     *             for errors occurring during the closing process.
     */
    public void close() throws SystemException {
        super.close();
        try {
            if (this.ftp.isConnected()) {
                this.ftp.logout();
                this.ftp.disconnect();
            }
        } catch (IOException ex) {
            throw new SystemException("Failure to logout and disconnect user from the FTP server, " + config.getHost());
        }
    }

    @Override
    public List<String> listDirectory(String directory, boolean subFolders) throws MessageException {
        String[] array = null;
        try {
            if (subFolders) {
                this.recursiveListing = new ArrayList<>();
                this.listDirectoryAndSubfolders(directory, null);
                return this.recursiveListing;
            }
            else {
                array = this.ftp.listNames(directory);
                return Arrays.asList(array);
            }
        } catch (IOException e) {
            throw new SystemException("Failure to list directory, " + directory);
        }
    }

    /**
     * 
     * @param parentDir
     * @param currentDir
     * @throws MessageException
     */
    protected void listDirectoryAndSubfolders(String parentDir, String currentDir) throws MessageException {
        String dirToList = parentDir;
        if (currentDir != null) {
            dirToList += "/" + currentDir;
        }
        FTPFile[] subFiles;
        try {
            subFiles = this.ftp.listFiles(dirToList);
        } catch (IOException e) {
            throw new SystemException("Error listing files in directory, " + dirToList);
        }
        if (subFiles != null && subFiles.length > 0) {
            for (FTPFile aFile : subFiles) {
                String currentFileName = aFile.getName();
                if (currentFileName.equals(".") || currentFileName.equals("..")) {
                    // skip parent directory and directory itself
                    continue;
                }
                if (aFile.isDirectory()) {
                    this.listDirectoryAndSubfolders(dirToList, currentFileName);
                }
                else {
                    // Add file name to list
                    this.recursiveListing.add(dirToList + "/" + currentFileName);
                }
            }
        }
    }

    @Override
    public String downloadFile(String remoteFile) throws MessageException {
        long fileSize = 1;
        String downLoadLoc = System.getProperty("SerialPath");
        String outputPath = downLoadLoc + config.getSessionId() + "/";
        String outputFile = outputPath + RMT2File.getFileName(remoteFile);
        String downloadedFilePath = null;
        try {
            this.ftp.enterLocalPassiveMode();
            this.ftp.setFileType(FTP.BINARY_FILE_TYPE);

            // Create directory if it does not exists.
            File downLoadDir = new File(outputPath);
            boolean mkdirRc = false;
            if (!downLoadDir.exists()) {
                mkdirRc = downLoadDir.mkdir();
                if (mkdirRc) {
                    logger.info(outputPath + " directory was created to house downloads");
                }
            }

            // Download file to specified location
            File downLoadFile = new File(outputFile);
            OutputStream os = new BufferedOutputStream(new FileOutputStream(downLoadFile));
            boolean success = this.ftp.retrieveFile(remoteFile, os);
            os.flush();
            os.close();

            if (success) {
                logger.info(remoteFile + ": OK");
                fileSize = downLoadFile.length();
                downloadedFilePath = outputFile;
            }
            else {
                logger.error(remoteFile + ": FAILED");
                fileSize = 0;
            }
        } catch (IOException e) {
            logger.error("Eror ocurred downloading fiel, " + remoteFile, e);
        }
        return downloadedFilePath;
    }

    @Override
    public int downloadFile(String directory, boolean subFolders) throws MessageException {
        return 0;
    }

    @Override
    public Serializable getMessage() throws MessageException {
        return null;
    }

    /**
     * Creates and sends an email message using the data contained in
     * <i>emailData</i>.
     * 
     * @param emailData
     *            Required to be n instance of
     *            {@link com.api.messaging.email.EmailMessageBean
     *            EmailMessageBean} containing data representing the components
     *            that comprises an email structure: 'From', 'To', 'Subject',
     *            'Body', and any attachments.
     * @return int The SMTP return code.
     * @throws MessageException
     *             SMTP server is invalid or not named, validation errors,
     *             invalid assignment of data values to the email message, email
     *             transmission errors, or <i>emailData</i> is of the incorrect
     *             type.
     */
    public Object sendMessage(Serializable emailData) throws MessageException {
        return null;
    }

}
