package com.api.messaging.ftp.sender;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.enterprisedt.net.ftp.FTPClientInterface;
import com.enterprisedt.net.ftp.FTPTransferType;
import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.ssh.SSHFTPClient;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

//import com.enterprisedt.util.debug.Level;
//import com.enterprisedt.util.debug.Logger;

public class SSHFTPSender {

    private SSHFTPClient ftp;

    private static int connectCount = 0;

    // public static Logger log = Logger.getLogger(SSHFTPConnectionImpl.class);

    public SSHFTPSender(String host, String username, String keyfile,
            String password) throws FTPException, IOException {

        ftp = new SSHFTPClient();
        ftp.setRemoteHost(host);
        ftp.setAuthentication(keyfile, username, password);
        ftp.getValidator().setHostValidationEnabled(false);

    }

    public SSHFTPSender(String host, String username, String password)
            throws FTPException, IOException {

        ftp = new SSHFTPClient();
        ftp.setRemoteHost(host);
        ftp.setAuthentication(username, password);
        ftp.getValidator().setHostValidationEnabled(false);

    }

    public boolean connect() throws Exception {

        try {
            ftp.connect();
            connectCount = 0;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void disconnect() throws FTPException, IOException {
        // connect to the server
        System.out.println("Diconnecting from server " + ftp.getRemoteHost());
        ftp.quit();
    }

    public void sendFileOver(String filename, String destDir,
            String archiveDirPath) throws Exception {
        putFile(destDir, filename, ftp);
        /*
         * File file = new File(filename); if ( (archiveDirPath!=null)
         * &&(archiveDirPath.length()>0) ) { File archDirObj = new
         * File(archiveDirPath); if (!archDirObj.exists()) throw new
         * FileNotFoundException("Specified Archive Directory does not exist!");
         * 
         * File newFile = new File(archDirObj.getAbsolutePath() + File.separator
         * + file.getName() ); newFile.createNewFile(); copy(file,newFile); }
         * file.delete();
         */
        System.out.println("Have put file: " + filename);
    }

    private void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);
        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    private static boolean putFile(String destDir, String filename,
            FTPClientInterface ftp) throws Exception {

        File f = new File(filename);
        try {
            /*
             * if ( filename.endsWith(".txt") || filename.endsWith(".hl7") ){
             * System.out.println("Ascii Xfer to " + destDir);
             * ftp.setType(FTPTransferType.ASCII); } else{
             * System.out.println("Binary Xfer to " + destDir);
             */
            ftp.setType(FTPTransferType.BINARY);
            // }

            FileInputStream fis = new FileInputStream(f);
            ftp.chdir(destDir);
            ftp.put(fis, f.getName());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public synchronized void sendBytes(String destDir, String file,
            byte[] content, String archDir) throws Exception {
        System.out.println("Setting up passive, Binary transfers");
        try {

            ftp.setType(FTPTransferType.BINARY); // send everything in as a
            // binary
            ftp.chdir(destDir);
            ByteArrayInputStream byt = new ByteArrayInputStream(content);
            ftp.put(byt, file);
            ftp.chdir("/");
            // archiveFile(archDir,file,content);

        } catch (Exception e) {
            System.out.println("Exception occured" + e);
            e.printStackTrace();
            throw e;
        }
    }

    public synchronized void sendAppendStream(String destDir, String file,
            String content, String archDir, String appFlag) throws Exception {
        System.out.println("Setting up passive, ASCII transfers");
        try {
            ftp.chdir(destDir);
            ByteArrayInputStream byt = new ByteArrayInputStream(
                    content.getBytes());
            if (appFlag.equalsIgnoreCase("true")) {
                put(content, file, true);
            }
            else {
                ftp.put(byt, file);

            }
            ftp.chdir("/");

            // archiveFile(archDir,file,content);

        } catch (Exception e) {
            System.out.println("Exception occured" + e);
            e.printStackTrace();
            throw e;
        }
    }

    public synchronized void sendAppendStream(String content, String file,
            String archiveDirPath, String appFlag) throws Exception {
        try {

            if (appFlag.equalsIgnoreCase("true")) {
                put(content, file, true);
            }
            // set up passive transfers
            else {
                ftp.setType(FTPTransferType.BINARY); // send everything in as a
                // binary
                ByteArrayInputStream byt = new ByteArrayInputStream(
                        content.getBytes());
                ftp.put(byt, file);
            }
            // archiveFile(archiveDirPath,file,content);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    public synchronized void put(String content, String filename,
            boolean flagAppend) throws Exception {
        try {
            ByteArrayInputStream byt = new ByteArrayInputStream(
                    content.getBytes());
            ftp.put(byt, filename, true);
            // ftp.setType(FTPTransferType.BINARY);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
