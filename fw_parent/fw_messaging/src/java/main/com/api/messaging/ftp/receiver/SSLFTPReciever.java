package com.api.messaging.ftp.receiver;

import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FTPFile;
import com.enterprisedt.net.ftp.ssl.SSLFTPClient;

import java.io.FileOutputStream;
import java.io.IOException;

public class SSLFTPReciever {

    private String host;

    private String port;

    private String username;

    private String password;

    private SSLFTPClient sslFtp = null;

    public SSLFTPReciever() {

        host = "170.138.216.30";
        port = "21";
        username = "RANATCOMM";
        password = "00012345";

        // host = "webserv.medfone.com";
        // port = "21";
        // username = "Ameripath";
        // password = "medfone";

        // host = "sftp.ameripath.com";
        // port = "990";
        // username = "bgilbertson";
        // password = "wU=R_2uS";

    }

    public static void main(String arg[]) throws Exception {

        SSLFTPReciever rc = new SSLFTPReciever();
        /*
         * String localdir="C:\\d01\\"; //String hostdir="/USER1/BPEL/TEST/NEW";
         * String hostdir="/USER1/EVM/TEST/OUT"; //USER1:[BPEL.TEST.BADFILE]
         * //String archdir="/USER1/BPEL/TEST/NEW"; String
         * archdir="/USER1/EVM/TEST/OUT/SENT";//"/USER1/BPEL/TEST/BADFILE";
         * String fileformat=".TXT";
         */

        // rc.connectImplicitFTPS();
        System.out.println("Before connection");
        rc.connectExplicitFTPS();
        // rc.putAndArchieve(localdir,hostdir,archdir,fileformat);
        rc.disconnect();
    }

    public SSLFTPReciever(String host, String port, String username,
            String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public boolean connectImplicitFTPS() throws Exception {
        return connectAndLogin(true);
    }

    public boolean connectExplicitFTPS() throws Exception {
        return connectAndLogin(false);
    }

    private boolean connectAndLogin(boolean i) throws Exception {
        try {
            if ((sslFtp != null) && (sslFtp.connected())) {
                disconnect();
            }
            sslFtp = new SSLFTPClient();
            // System.out.println("Setting configuration flags===>Implicit FTPS");
            // if (i)
            // ftp.setConfigFlags(i;

            /*
             * sslFtp.setImplicitFTPS(i);
             * 
             * sslFtp.setRemoteHost(host);
             * sslFtp.setRemotePort(Integer.parseInt(port));
             * sslFtp.setValidateServer(false); sslFtp.connect();
             */

            sslFtp.setImplicitFTPS(i);
            // sslFtp.loadRootCertificates("server.cert.pem");
            System.out.println("Setting host");
            sslFtp.setRemoteHost(host);
            // sslFtp.setRemotePort(Integer.parseInt(port));
            sslFtp.setValidateServer(false);
            System.out.println("About to host");
            sslFtp.connect();

            try {
                sslFtp.auth(SSLFTPClient.AUTH_TLS);
                // sslFtp.auth(SSLFTPClient.PROT_PRIVATE);
                System.out.println("auth() succeeded");
            } catch (FTPException ex) {
                ex.printStackTrace();
            }
            sslFtp.login(username, password);
            // ftp.chdir("NOTHING");
            return true;
        } catch (FTPException ftpEx) {
            ftpEx.printStackTrace();
            return false;
        } catch (IOException ioEx) {
            System.out.println("Could not connect!");
            ioEx.printStackTrace();
            return false;
        }
    }

    public boolean disconnect() throws Exception {
        try {
            if (sslFtp.connected() == true) {
                System.out.println("Quitting client");
                sslFtp.quit();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void putAndArchieve(String localDir, String hostDir,
            String hostArchDir, String FileFormat) {

        try {

            System.out.println("Connected succesfully");
            sslFtp.chdir(hostDir);
            // "/USER1/BPEL/TEST/NEW
            FTPFile files[] = sslFtp.dirDetails(hostDir + "/*");
            for (int i = 0; i < files.length; i++) {
                FTPFile ftpfile = files[i];
                String filename = ftpfile.getName();
                System.out.println(">>>" + filename + " File Format"
                        + FileFormat);
                if (filename.contains(FileFormat)) {

                    System.out.println(">>>" + filename);
                    // "C:\\d01\\"
                    // ftpClient.get(localDir+filename,filename);
                    System.out.println("inside the file format" + filename);
                    byte filestr[] = sslFtp.get(filename);
                    System.out.println("Before changing host archive dir"
                            + hostArchDir);
                    sslFtp.chdir(hostArchDir);
                    System.out.println("After changing host archive dir");
                    System.out.println("Before writing the file to host arch");
                    sslFtp.put(filestr, filename);
                    FileOutputStream fout = new FileOutputStream(localDir + "/"
                            + filename);
                    fout.write(filestr);
                    // ftp.setType(FTPTransferType.BINARY);
                    System.out.println("Before deleting file");
                    sslFtp.chdir(hostDir);
                    sslFtp.delete(filename + ";*");

                    System.out.println("After deleting file");
                    // ftpClient.chdir("/");
                }

            }
        } catch (Exception ee) {
            System.out.println("Exception is" + ee.getMessage());
        }

    }

}
