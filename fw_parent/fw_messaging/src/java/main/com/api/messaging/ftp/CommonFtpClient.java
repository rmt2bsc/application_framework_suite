package com.api.messaging.ftp;

import com.api.config.old.ProviderConfig;
import com.api.config.old.ProviderConnectionException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPTransferType;
import com.enterprisedt.net.ftp.FTPException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class CommonFtpClient {
    private static Logger logger = Logger.getLogger(CommonFtpClient.class);

    private String host;

    private String port;

    private String username;

    private String password;

    protected FTPClient ftp = null;

    public CommonFtpClient(String host, String port, String username,
            String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public FTPClient connect(ProviderConfig config)
            throws ProviderConnectionException {
        String errMsg = null;
        if (config.getHost() == null) {
            errMsg = "Host name is required";
            logger.log(Level.ERROR, errMsg);
            throw new ProviderConnectionException(errMsg);
        }
        if (config.getPort() == null) {
            errMsg = "Port number is required";
            logger.log(Level.ERROR, errMsg);
            throw new ProviderConnectionException(errMsg);
        }
        if (config.getUserId() == null) {
            errMsg = "User Id is required";
            logger.log(Level.ERROR, errMsg);
            throw new ProviderConnectionException(errMsg);
        }
        if (config.getPassword() == null) {
            errMsg = "Password is required";
            logger.log(Level.ERROR, errMsg);
            throw new ProviderConnectionException(errMsg);
        }

        ftp = new FTPClient();
        try {
            ftp.setRemoteHost(config.getHost());
            ftp.setRemotePort(new Integer(config.getPort()));
            ftp.connect();
            ftp.login(config.getUserId(), config.getPassword());
            return ftp;
        } catch (IOException e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new ProviderConnectionException(e);
        } catch (FTPException e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new ProviderConnectionException(e);
        }
    }

    public boolean connectImplicitFTPS() throws Exception {
        return connectAndLogin(true);
    }

    public boolean connectExplicitFTPS() throws Exception {
        return connectAndLogin(false);
    }

    private boolean connectAndLogin(boolean i) throws Exception {

        System.out.println("inside connection>>>>>afterclien");
        try {

            if ((ftp != null) && (ftp.connected())) {
                disconnect();
            }
            System.out.println("connecting>>> ftp cleint");
            ftp = new FTPClient();
            ftp.setRemoteHost(host);
            ftp.setRemotePort(Integer.parseInt(port));
            System.out.println(" before connecting>>>");
            ftp.connect();
            System.out.println("before logging>>>");
            ftp.login(username, password);
            return true;
        } catch (Exception e) {
            System.out.println("Unable to Login.");
            e.printStackTrace();
            throw e;
        }

    }

    public boolean disconnect() throws Exception {
        try {
            if (ftp.connected() == true) {
                System.out.println("Quitting client");
                ftp.quit();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    protected void copy(File src, File dst) throws IOException {
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

    protected void archiveFile(String archiveDirPath, String fileName, File file)
            throws Exception {
        if ((archiveDirPath != null) && (archiveDirPath.length() > 0)) {
            File archDirObj = new File(archiveDirPath);
            if (!archDirObj.exists())
                throw new FileNotFoundException(
                        "Specified Archive Directory does not exist!");

            File newFile = new File(archDirObj.getAbsolutePath()
                    + File.separator + fileName);
            newFile.createNewFile();

            copy(file, newFile);
        }

    }

    protected void archiveFile(String archiveDirPath, String fileName,
            String Content) throws Exception {
        if ((archiveDirPath != null) && (archiveDirPath.length() > 0)) {
            FileOutputStream out = new FileOutputStream(archiveDirPath + "/"
                    + fileName);
            PrintStream p = new PrintStream(out);
            p.print(Content);
            p.close();
            out.close();
        }

    }

    public synchronized void putFile(String destDir, File file)
            throws Exception {
        String filePath = file.getAbsolutePath();
        String fileName = file.getName();
        System.out.println(" Does file " + filePath + " exist: "
                + file.exists());
        if ((file == null) || (!file.exists())) {
            if (!file.exists()) {
                System.out.println("Cannot send a file that does not exist!");
            }
        }

        try {
            ftp.setType(FTPTransferType.BINARY);
            FileInputStream fis = new FileInputStream(file);
            ftp.chdir(destDir);
            ftp.put(fis, file.getName());
            ftp.chdir("/");

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public synchronized void putFile(String destDir, String SourceDir,
            String file, String archDir) throws Exception {
        String fileAct = SourceDir + "/" + file;
        try {
            ftp.setType(FTPTransferType.BINARY);
            // System.out.println("Putting file using ASCII");
            // ftp.setType(FTPTransferType.ASCII);
            FileInputStream fis = new FileInputStream(fileAct);
            ftp.chdir(destDir);
            ftp.put(fis, file);
            ftp.chdir("/");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public synchronized void putFile(String destDir, String SourceDir,
            String file, String archDir, String clientName) throws Exception {
        String fileAct = SourceDir + "/" + file;
        try {

            if (clientName.equalsIgnoreCase("IDX")) {
                System.out.println("Sending ASCII Client Name is" + clientName);
                ftp.setType(FTPTransferType.ASCII);
            }
            else {
                System.out.println("Sending binary data for Client Name is"
                        + clientName);
                ftp.setType(FTPTransferType.BINARY);
            }
            // System.out.println("Putting file using ASCII");
            // ftp.setType(FTPTransferType.ASCII);
            FileInputStream fis = new FileInputStream(fileAct);
            ftp.chdir(destDir);
            ftp.put(fis, file);
            ftp.chdir("/");

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

    public void deleteFile() {
        // delete file from server
        // System.out.println("Deleting " + filename);
        // ftp.delete(filename);
    }

    private static void showFiles(String[] files) {
        if (files.length == 0)
            System.out.println("  no files");
        else
            for (int i = 0; i < files.length; i++)
                System.out.println("  " + files[i]);
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
