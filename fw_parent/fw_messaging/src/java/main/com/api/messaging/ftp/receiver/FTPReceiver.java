package com.api.messaging.ftp.receiver;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.api.config.old.ProviderConfig;
import com.api.config.old.ProviderConnectionException;
import com.api.messaging.ftp.CommonFtpClient;
import com.enterprisedt.net.ftp.FTPFile;

import java.io.FileOutputStream;

import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPTransferType;

public class FTPReceiver extends CommonFtpClient {
    private static Logger logger = Logger.getLogger(FTPReceiver.class);

    // FTP credentials
    public FTPReceiver(String host, String port, String username,
            String password) throws Exception {
        super(host, port, username, password);
    }

    // Wrong FTP Credentials
    public FTPClient connect(ProviderConfig config)
            throws ProviderConnectionException {
        return super.connect(config);
    }

    public boolean disconnect() throws Exception {
        return super.disconnect();
    }

    public String getAndArchieve(String localDir, String hostDir,
            String hostArchDir, String filesNames[], String FP_LocName,
            String numFiles, String excluFiles, String fileTransMode)
            throws Exception {
        String sentFile = "";
        int fileCount = -1;
        try {
            if (numFiles != null) {
                if (!numFiles.equalsIgnoreCase("")
                        && !numFiles.equalsIgnoreCase("all")) {
                    fileCount = Integer.parseInt(numFiles);
                }
            }
            String exclFiles[] = null;
            if (excluFiles.trim().length() > 0) {
                exclFiles = new String[excluFiles.split(",").length - 1];
                exclFiles = excluFiles.split(",");
            }
            int counter = 0;
            this.ftp.setConnectMode(com.enterprisedt.net.ftp.FTPConnectMode.PASV);
            this.ftp.setType(FTPTransferType.BINARY);

            if (fileTransMode.equalsIgnoreCase("ASCII")) {
                logger.log(Level.DEBUG, "setting to ASCII");
                this.ftp.setType(FTPTransferType.ASCII);
            }

            if (filesNames.length > 0) {
                for (int j = 0; j < filesNames.length; j++) {
                    this.ftp.chdir(hostDir);
                    String file = filesNames[j];
                    if (file.contains("*.")) {
                        FTPFile files[] = this.ftp.dirDetails(hostDir + "/"
                                + file);
                        if (files != null) {
                            Files: for (int i = 0; i < files.length; i++) {
                                // break if file count is specified
                                if (fileCount != -1 && fileCount == counter) {
                                    break;
                                }
                                this.ftp.chdir(hostDir);
                                FTPFile ftpfile = files[i];
                                String filename = "";
                                if (!ftpfile.isDir()) {
                                    filename = ftpfile.getName();
                                }
                                // /for absolute path
                                if (filename.contains("/")) {
                                    int len = filename.split("/").length;
                                    if (len > 1) {
                                        filename = filename.split("/")[len - 1];
                                    }
                                }
                                if (filename.length() > 2
                                        && file.substring(file.length() - 3)
                                                .equalsIgnoreCase(
                                                        filename.substring(filename
                                                                .length() - 3))) {
                                    // /exclude files
                                    if (exclFiles != null) {
                                        for (int k = 0; k < exclFiles.length; k++) {
                                            if (filename.contains(exclFiles[k]))
                                                continue Files;
                                        }
                                    }
                                    byte filestr[] = this.ftp.get(filename);
                                    // this.ftp.get();
                                    // if archive directory is specified then
                                    // archive it.
                                    if (hostArchDir.length() > 1) {
                                        if (!hostArchDir
                                                .equalsIgnoreCase("DELETE")) {
                                            this.ftp.chdir(hostArchDir);
                                            if (FP_LocName
                                                    .equalsIgnoreCase("@Version_client@")) {
                                                this.ftp.setType(FTPTransferType.ASCII);
                                                this.ftp.put(filestr, filename);
                                                this.ftp.setConnectMode(com.enterprisedt.net.ftp.FTPConnectMode.PASV);
                                                this.ftp.setType(FTPTransferType.BINARY);

                                            }
                                            else {
                                                this.ftp.put(filestr, filename);
                                            }
                                        }
                                        this.ftp.chdir(hostDir);
                                        if (FP_LocName
                                                .equalsIgnoreCase("@Version_client@")) {
                                            this.ftp.delete(filename + ";*");
                                        }
                                        else {
                                            this.ftp.delete(filename);
                                        }
                                    }
                                    // Write to local directory file///
                                    FileOutputStream fout = new FileOutputStream(
                                            localDir + "/" + filename);
                                    fout.write(filestr);
                                    // this.ftp.chdir(hostDir);

                                    counter++;

                                    // this.ftp.
                                    // this.ftp.chdir("/");
                                    sentFile = filename + "," + sentFile;
                                }
                            }
                        }// end of file null
                    }
                    else {

                        String filename = filesNames[j];
                        byte filestr[] = this.ftp.get(filename);
                        if (hostArchDir.length() > 1) {
                            this.ftp.chdir(hostArchDir);
                            this.ftp.put(filestr, filename);
                            this.ftp.chdir(hostDir);
                            if (FP_LocName.equalsIgnoreCase("@Version_client@")) {
                                logger.log(Level.DEBUG,
                                        "Inside deleting verision client");
                                this.ftp.delete(filename + ";*");
                            }
                            else {
                                this.ftp.delete(filename);
                            }
                        }
                        FileOutputStream fout = new FileOutputStream(localDir
                                + "/" + filename);
                        fout.write(filestr);
                        // ftp.setType(FTPTransferType.BINARY);
                        sentFile = filename + "," + sentFile;
                    }
                }
            }
            sentFile.trim();
            // String files[] = sentFile.split(",");
        } catch (Exception ee) {
            System.out.println("Exception is" + ee.getMessage());
            throw ee;
        }
        return sentFile;
    }

    public void putAndArchieve(String localDir, String hostDir,
            String hostArchDir, String FileFormat) throws Exception {
        try {
            this.ftp.chdir(hostDir);
            // "/USER1/BPEL/TEST/NEW
            FTPFile files[] = this.ftp.dirDetails(hostDir + "/*");
            for (int i = 0; i < files.length; i++) {
                FTPFile ftpfile = files[i];
                String filename = ftpfile.getName();
                if (filename.contains(FileFormat)) {

                    byte filestr[] = this.ftp.get(filename);
                    logger.log(Level.INFO, "Before changing host archive dir"
                            + hostArchDir);
                    this.ftp.chdir(hostArchDir);
                    this.ftp.put(filestr, filename);
                    FileOutputStream fout = new FileOutputStream(localDir + "/"
                            + filename);
                    fout.write(filestr);
                    this.ftp.chdir(hostDir);
                    this.ftp.delete(filename + "*");
                }
            }
        } catch (Exception ee) {
            System.out.println("Exception is" + ee.getMessage());
        }

    }

}
