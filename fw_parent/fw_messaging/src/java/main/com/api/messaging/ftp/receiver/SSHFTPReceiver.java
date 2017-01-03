package com.api.messaging.ftp.receiver;

import com.enterprisedt.net.ftp.FTPFile;

import java.io.FileOutputStream;
import java.io.IOException;

import com.enterprisedt.net.ftp.FTPTransferType;
import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.ssh.SSHFTPClient;

public class SSHFTPReceiver {

    private SSHFTPClient ftpClient;

    private static int connectCount = 0;

    public SSHFTPReceiver() {
    }

    public SSHFTPReceiver(String host, String username, String port,
            String keyfile, String password) throws FTPException, IOException {

        ftpClient = new SSHFTPClient();
        ftpClient.setRemoteHost(host);
        ftpClient.setAuthentication(keyfile, username, password);
        ftpClient.getValidator().setHostValidationEnabled(false);

    }

    public SSHFTPReceiver(String host, String username, String password)
            throws FTPException, IOException {

        ftpClient = new SSHFTPClient();
        ftpClient.setRemoteHost(host);
        ftpClient.setAuthentication(username, password);
        ftpClient.getValidator().setHostValidationEnabled(false);

    }

    public SSHFTPReceiver(String host, String port, String username,
            String password) throws FTPException, IOException {
        // System.out.println("inside the receiver constructor");
        ftpClient = new SSHFTPClient();
        // System.out.println("initiated the object");
        ftpClient.setRemoteHost(host);
        // System.out.println("afgter serting the host");
        if (port != null && !port.equalsIgnoreCase("")) {
            int portnum = new Integer(port).intValue();
            ftpClient.setRemotePort(portnum);
        }
        ftpClient.setAuthentication(username, password);
        ftpClient.getValidator().setHostValidationEnabled(false);

    }

    public boolean connect() throws Exception {
        try {
            // System.out.println("Before connecting the ssh client");
            ftpClient.connect();
            // System.out.println("after connecting the ssh client");
            connectCount = 0;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            connectCount++;
            if (connectCount < 10)
                return this.connect();
            else
                return false;
        }
    }

    public void disconnect() throws FTPException, IOException {
        // connect to the server
        System.out.println("Diconnecting from server "
                + ftpClient.getRemoteHost());
        ftpClient.quit();
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

            // ftpClient.setConnectMode(com.enterprisedt.net.ftp.FTPConnectMode.PASV);
            ftpClient.setType(FTPTransferType.BINARY);

            if (fileTransMode.equalsIgnoreCase("ASCII")) {
                System.out.println("setting to ASCII");
                ftpClient.setType(FTPTransferType.ASCII);
            }

            if (filesNames.length > 0) {
                for (int j = 0; j < filesNames.length; j++) {
                    ftpClient.chdir(hostDir);
                    String file = filesNames[j];
                    // System.out.println("What host/file is this:"+hostDir+"/"+file);
                    if (file.contains("*.")) {
                        // System.out.println("test1111 ..before getting the directory");
                        FTPFile files[] = ftpClient.dirDetails(hostDir);

                        // System.out.println("test1111 ..After the directory");
                        if (files != null) {
                            // System.out.println("Files not null"+files.length);
                            Files: for (int i = 0; i < files.length; i++) {
                                // break if file count is specified
                                if (fileCount != -1 && fileCount == counter) {
                                    break;
                                }
                                ftpClient.chdir(hostDir);
                                FTPFile ftpfile = files[i];
                                String filename = "";
                                if (!ftpfile.isDir()) {
                                    filename = ftpfile.getName();
                                }
                                // /for absolute path
                                // System.out.println("filename is"+filename);
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
                                    byte filestr[] = ftpClient.get(filename);
                                    // ftpClient.get();
                                    // if archive directory is specified then
                                    // archive it.
                                    if (hostArchDir.length() > 1) {
                                        if (!hostArchDir
                                                .equalsIgnoreCase("DELETE")) {
                                            ftpClient.chdir(hostArchDir);
                                            if (FP_LocName
                                                    .equalsIgnoreCase("@Version_client@")) {
                                                ftpClient
                                                        .setType(FTPTransferType.ASCII);
                                                ftpClient
                                                        .put(filestr, filename);
                                                // ftpClient.setConnectMode(com.enterprisedt.net.ftp.FTPConnectMode.PASV);
                                                ftpClient
                                                        .setType(FTPTransferType.BINARY);

                                            }
                                            else {
                                                ftpClient
                                                        .put(filestr, filename);
                                            }
                                        }
                                        ftpClient.chdir(hostDir);
                                        if (FP_LocName
                                                .equalsIgnoreCase("@Version_client@")) {
                                            ftpClient.delete(filename + ";*");
                                        }
                                        else {
                                            ftpClient.delete(filename);
                                        }
                                    }
                                    // Write to local directory file///
                                    FileOutputStream fout = new FileOutputStream(
                                            localDir + "/" + filename);
                                    // System.out.println("Write to local directory"+localDir+"/"+filename);
                                    fout.write(filestr);
                                    // ftpClient.chdir(hostDir);

                                    counter++;

                                    // ftpClient.
                                    // ftpClient.chdir("/");
                                    sentFile = filename + "," + sentFile;
                                }
                            }
                        }// end of file null
                    }
                    else {

                        String filename = filesNames[j];
                        byte filestr[] = ftpClient.get(filename);
                        if (hostArchDir.length() > 1) {
                            ftpClient.chdir(hostArchDir);
                            ftpClient.put(filestr, filename);
                            ftpClient.chdir(hostDir);
                            if (FP_LocName.equalsIgnoreCase("@Version_client@")) {
                                // System.out.println("Inside deleting verision client");
                                ftpClient.delete(filename + ";*");
                            }
                            else {
                                ftpClient.delete(filename);
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

            ftpClient.chdir(hostDir);
            // "/USER1/BPEL/TEST/NEW
            FTPFile files[] = ftpClient.dirDetails(hostDir + "/*");
            for (int i = 0; i < files.length; i++) {
                FTPFile ftpfile = files[i];
                String filename = ftpfile.getName();
                if (filename.contains(FileFormat)) {

                    byte filestr[] = ftpClient.get(filename);
                    System.out.println("Before changing host archive dir"
                            + hostArchDir);
                    ftpClient.chdir(hostArchDir);
                    ftpClient.put(filestr, filename);
                    FileOutputStream fout = new FileOutputStream(localDir + "/"
                            + filename);
                    fout.write(filestr);
                    ftpClient.chdir(hostDir);
                    ftpClient.delete(filename + "*");
                }
            }
        } catch (Exception ee) {
            System.out.println("Exception is" + ee.getMessage());
        }

    }

}
