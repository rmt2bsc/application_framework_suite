package com.api.messaging.ftp.sender;

import com.api.config.old.ProviderConfig;
import com.api.config.old.ProviderConnectionException;
import com.api.messaging.ftp.CommonFtpClient;
import com.enterprisedt.net.ftp.FTPConnectMode;

import java.io.File;

import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPTransferType;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class FTPSender extends CommonFtpClient {
    private static Logger logger = Logger.getLogger(FTPSender.class);

    public FTPSender(String host, String port, String username, String password) {
        super(host, port, username, password);
    }

    public FTPClient connect(ProviderConfig config)
            throws ProviderConnectionException {
        return super.connect(config);
    }

    public boolean disconnect() throws Exception {
        return super.disconnect();
    }

    public void sendFile(File file, boolean deleteOnSend) throws Exception {
        sendFile(file, deleteOnSend, null);
    }

    public synchronized void sendBytes(byte[] content, String file,
            String archiveDirPath) throws Exception {
        try {
            // set up passive transfers

            logger.log(Level.DEBUG, "Setting up passive, Byte transfers");
            this.ftp.setConnectMode(FTPConnectMode.PASV);
            this.ftp.setType(FTPTransferType.BINARY); // send everything in as a
            // binary
            ByteArrayInputStream byt = new ByteArrayInputStream(content);
            this.ftp.put(byt, file);
            // archiveFile(archiveDirPath,file,content);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    public synchronized void sendBytes(String destDir, String file,
            byte[] content, String archDir) throws Exception {
        logger.log(Level.DEBUG, "Setting up passive, ASCII transfers");
        try {
            this.ftp.setConnectMode(FTPConnectMode.PASV);
            this.ftp.setType(FTPTransferType.BINARY); // send everything in as a
            // binary
            this.ftp.chdir(destDir);
            ByteArrayInputStream byt = new ByteArrayInputStream(content);
            this.ftp.put(byt, file);
            this.ftp.chdir("/");
            // archiveFile(archDir,file,content);

        } catch (Exception e) {
            System.out.println("Exception occured" + e);
            e.printStackTrace();
            throw e;
        }
    }

    public synchronized void sendFile(File file, boolean deleteOnSend,
            String archiveDirPath) throws Exception {
        String filePath = file.getAbsolutePath();
        String fileName = file.getName();
        logger.log(Level.DEBUG,
                " Does file " + filePath + " exist: " + file.exists());
        if ((file == null) || (!file.exists())) {
            if (!file.exists()) {
                logger.log(Level.DEBUG,
                        "Cannot send a file that does not exist!");
            }
            return;
        }
        try {
            // set up passive transfers
            logger.log(Level.DEBUG, "Setting up passive, ASCII transfers");
            this.ftp.setConnectMode(FTPConnectMode.PASV);
            this.ftp.setType(FTPTransferType.BINARY); // send everything in as a
            // binary

            this.ftp.put(file.getAbsolutePath(), file.getName());

            if (deleteOnSend) {
                file.delete();
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public synchronized void sendFile(String sourceDir, String file,
            String archiveDirPath) throws Exception {
        try {
            // set up passive transfers
            String fileloc = sourceDir + "/" + file;
            FileInputStream fis = new FileInputStream(fileloc);
            logger.log(Level.DEBUG, "Setting up passive, ASCII transfers");
            this.ftp.setConnectMode(FTPConnectMode.PASV);
            this.ftp.setType(FTPTransferType.BINARY); // send everything in as a
            // binary
            this.ftp.put(fis, file);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public synchronized void sendAppendStream(String content, String file,
            String archiveDirPath, String appFlag) throws Exception {
        try {
            this.ftp.setType(FTPTransferType.ASCII);
            if (appFlag.equalsIgnoreCase("true")) {
                // put(content,file,true);
                // SSLFTPSingleton.AppendToFile(ftp,file,content);
            }
            // set up passive transfers
            else {
                // this.ftp.setConnectMode(FTPConnectMode.PASV);
                // this.ftp.setType(FTPTransferType.BINARY); // send everything
                // in as
                // a binary
                ByteArrayInputStream byt = new ByteArrayInputStream(
                        content.getBytes());
                this.ftp.put(byt, file);
            }
            // archiveFile(archiveDirPath,file,content);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    public synchronized void sendAppendStream(String destDir, String file,
            String content, String archDir, String appFlag) throws Exception {

        try {
            this.ftp.chdir(destDir);
            this.ftp.setType(FTPTransferType.ASCII);
            if (appFlag.equalsIgnoreCase("true")) {
                // put(content,file,true);
                // SSLFTPSingleton.AppendToFile(ftp,file,content);
                // ByteArrayInputStream byt = new
                // ByteArrayInputStream(content.getBytes());
                // this.ftp.put(byt,file,true);
            }
            else {
                ByteArrayInputStream byt = new ByteArrayInputStream(
                        content.getBytes());
                this.ftp.put(byt, file);
            }
            this.ftp.chdir("/");

            // archiveFile(archDir,file,content);

        } catch (Exception e) {
            System.out.println("Exception occured" + e);
            e.printStackTrace();
            throw e;
        }
    }

    public void sendFiles(List files) {
    }

    private static void showFiles(String[] files) {
        if (files.length == 0)
            logger.log(Level.INFO, "  no files");
        else
            for (int i = 0; i < files.length; i++)
                logger.log(Level.INFO, "  " + files[i]);
    }

}
