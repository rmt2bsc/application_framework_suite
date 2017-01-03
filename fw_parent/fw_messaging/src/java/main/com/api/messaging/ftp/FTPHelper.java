package com.api.messaging.ftp;

import java.io.File;

import java.util.*;

import java.io.*;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;

import org.xml.sax.InputSource;

import com.api.messaging.ftp.sender.ACTIVEFTPSender;
import com.api.messaging.ftp.sender.FTPSender;
import com.api.messaging.ftp.sender.SSHFTPSender;
import com.api.messaging.ftp.sender.SSLFTPSender;
import com.util.RMT2Base64Decoder;

//import sun.misc.BASE64Decoder;

public class FTPHelper {
    public FTPHelper() {
        return;
    }

    public void ParseandFTP(String xml, Hashtable dbdetails) throws Exception {

        SSLFTPSender sslFTP = null;
        SSHFTPSender sshFTP = null;
        FTPSender norFTP = null;
        ACTIVEFTPSender actFTP = null;
        boolean connected = false;
        String FP_LocName = (String) dbdetails.get("FP_LocName");
        String ftpType = (String) dbdetails.get("ftpType");
        String host = (String) dbdetails.get("host");
        String port = (String) dbdetails.get("port");
        String userID = (String) dbdetails.get("userID");
        String passWD = (String) dbdetails.get("passWD");
        String SourceRootDir = (String) dbdetails.get("SourceRootDir");
        String keyFile = (String) dbdetails.get("keyFile");
        String useKey = (String) dbdetails.get("useKey");
        String hostDir = (String) dbdetails.get("hostDir");

        String arcDir = SourceRootDir + "archives";
        String inDir = SourceRootDir + "in";

        String conditioncheck = "false";

        try {
            // /Details from database

            // /////

            HashMap info = (HashMap) xmlData(xml);
            String locID = (String) info.get("locID");
            String FromDir = (String) info.get("FromDir");
            HashMap ftpinfo = (HashMap) info.get("ftpinfo");
            String appFlag = (String) info.get("appFlag");
            System.out.println("FTP TYPE IS" + ftpType);
            if (ftpType.equalsIgnoreCase("SSLFTP")) {
                sslFTP = new SSLFTPSender(host.trim(), port.trim(),
                        userID.trim(), passWD.trim());
                connected = sslFTP.connectImplicitFTPS();
            }

            if (ftpType.equalsIgnoreCase("FTP")) {
                norFTP = new FTPSender(host.trim(), port.trim(), userID.trim(),
                        passWD.trim());
                connected = norFTP.connectImplicitFTPS();
            }

            if (ftpType.equalsIgnoreCase("ACTIVEFTP")) {
                actFTP = new ACTIVEFTPSender(host.trim(), port.trim(),
                        userID.trim(), passWD.trim());
                connected = actFTP.connectImplicitFTPS();
            }

            if (ftpType.equalsIgnoreCase("SSHFTP")) {

                if (useKey.equalsIgnoreCase("true")
                        || useKey.equalsIgnoreCase("yes")) {

                    sshFTP = new SSHFTPSender(host.trim(), userID.trim(),
                            keyFile, passWD);
                    connected = sshFTP.connect();

                }
                else {
                    sshFTP = new SSHFTPSender(host.trim(), userID.trim(),
                            passWD);
                    connected = sshFTP.connect();

                }
            }

            for (int i = 0; i < ftpinfo.size(); i++) {

                String ftp_inf = "ftp-info" + i;
                HashMap ftpFileInfo = (HashMap) ftpinfo.get(ftp_inf);
                String fileName = (String) ftpFileInfo.get("File-Name");
                String fileData = (String) ftpFileInfo.get("File-Data");
                String destDir = (String) ftpFileInfo.get("destDir");
                byte[] pdfData = new byte[32768];
                boolean pdfFile = false;
                // To check whether file name exists
                if (fileName != null && !fileName.equalsIgnoreCase("")
                        && fileName.length() > 1) {
                    if (fileName.endsWith("pdf")) {
                        pdfFile = true;
                        pdfData = decodeData(fileData);
                        String pdfdatasrt = new String(pdfData);
                    }
                    if (ftpType.equalsIgnoreCase("SSLFTP")) {

                        if (hostDir != null && !hostDir.equalsIgnoreCase("")) {
                            destDir = hostDir;
                        }

                        if (FromDir != null && FromDir.equalsIgnoreCase("Yes")) {
                            conditioncheck = "true";

                            if (destDir != null
                                    && !destDir.equalsIgnoreCase("")) {
                                sslFTP.putFile(destDir, inDir, fileName, arcDir);
                            }
                            else {
                                sslFTP.sendFile(inDir, fileName, arcDir);
                            }
                        }
                        else {
                            conditioncheck = "true";
                            if (destDir != null
                                    && !destDir.equalsIgnoreCase("")) {
                                if (pdfFile) {

                                    sslFTP.sendBytes(destDir, fileName,
                                            pdfData, arcDir);
                                }
                                else {

                                    sslFTP.sendAppendStream(destDir, fileName,
                                            fileData, arcDir, appFlag);
                                }
                            }
                            else {
                                if (pdfFile) {

                                    sslFTP.sendBytes(pdfData, fileName, arcDir);
                                }
                                else {
                                    // /For appending the data

                                    sslFTP.sendAppendStream(fileData, fileName,
                                            arcDir, appFlag);
                                }
                            }

                        }

                    }
                    // ////Normal FTP Process//////////////////

                    if (ftpType.equalsIgnoreCase("FTP")) {
                        if (hostDir != null && !hostDir.equalsIgnoreCase("")) {
                            destDir = hostDir;
                        }

                        if (FromDir != null && FromDir.equalsIgnoreCase("Yes")) {
                            conditioncheck = "true";
                            if (destDir != null
                                    && !destDir.equalsIgnoreCase("")) {
                                norFTP.putFile(destDir, inDir, fileName,
                                        arcDir, FP_LocName);
                            }
                            else {
                                norFTP.sendFile(inDir, fileName, arcDir);
                            }
                        }
                        else {
                            conditioncheck = "true";
                            if (destDir != null
                                    && !destDir.equalsIgnoreCase("")) {
                                if (pdfFile) {

                                    norFTP.sendBytes(destDir, fileName,
                                            pdfData, arcDir);
                                }
                                else {
                                    norFTP.sendAppendStream(destDir, fileName,
                                            fileData, arcDir, appFlag);
                                }
                            }
                            else {
                                if (pdfFile) {
                                    norFTP.sendBytes(pdfData, fileName, arcDir);
                                }
                                else {
                                    // /For appending the data
                                    sslFTP.sendAppendStream(fileData, fileName,
                                            arcDir, appFlag);
                                }
                            }

                        }

                    }

                    // ////////////////////////////////////////////////////////

                    // ////Active FTP Process//////////////////

                    if (ftpType.equalsIgnoreCase("ACTIVEFTP")) {

                        if (hostDir != null && !hostDir.equalsIgnoreCase("")) {
                            destDir = hostDir;
                        }

                        if (FromDir != null && FromDir.equalsIgnoreCase("Yes")) {
                            conditioncheck = "true";

                            if (destDir != null
                                    && !destDir.equalsIgnoreCase("")) {
                                actFTP.putFile(destDir, inDir, fileName,
                                        arcDir, FP_LocName);
                            }
                            else {
                                actFTP.sendFile(inDir, fileName, arcDir);
                            }
                        }
                        else {
                            conditioncheck = "true";
                            if (destDir != null
                                    && !destDir.equalsIgnoreCase("")) {
                                if (pdfFile) {

                                    actFTP.sendBytes(destDir, fileName,
                                            pdfData, arcDir);
                                }

                            }
                            else {
                                if (pdfFile) {

                                    actFTP.sendBytes(pdfData, fileName, arcDir);
                                }
                            }

                        }

                    }

                    // ////////////////////////////////////////////////////////

                    // ///If the ftp type SSH FTP ///////
                    if (ftpType.equalsIgnoreCase("SSHFTP")) {

                        if (hostDir != null && !hostDir.equalsIgnoreCase("")) {
                            destDir = hostDir;
                        }

                        if (FromDir != null && FromDir.equalsIgnoreCase("Yes")) {
                            conditioncheck = "true";
                            if (destDir != null
                                    && !destDir.equalsIgnoreCase("")) {
                                if (connected)
                                    sshFTP.sendFileOver(inDir + "/" + fileName,
                                            destDir, arcDir);
                            }

                        }
                        else {
                            conditioncheck = "true";
                            if (destDir != null
                                    && !destDir.equalsIgnoreCase("")) {

                                if (hostDir != null) {
                                    if (pdfFile) {

                                        sshFTP.sendBytes(destDir, fileName,
                                                pdfData, arcDir);
                                    }
                                    else {

                                        sshFTP.sendAppendStream(hostDir,
                                                fileName, fileData, arcDir,
                                                appFlag);
                                    }
                                }

                            }
                        }
                        System.out.println("Connected");

                    }
                    // //Archive files//////
                    try {

                        if (FromDir != null && FromDir.equalsIgnoreCase("Yes")) {
                            archiveFile(SourceRootDir, fileName);
                        }
                        else {
                            if (pdfFile) {
                                archiveFile(arcDir, fileName, pdfData);
                            }
                            else {
                                archiveFile(arcDir, fileName, fileData, appFlag);
                            }
                        }
                    } catch (Exception ee) {
                        throw new Exception("Archive Exception");
                    }
                    // End of archive
                    if (conditioncheck.equalsIgnoreCase("false")) {
                        throw new Exception("Conditions not met");
                    }
                }
            } // End of if file length

        } catch (Exception ee) {
            throw ee;
        } finally {
            if (ftpType.equalsIgnoreCase("SSLFTP") && sslFTP != null
                    && connected) {
                sslFTP.disconnect();
            }
            if (ftpType.equalsIgnoreCase("SSHFTP") && sshFTP != null
                    && connected) {
                sshFTP.disconnect();
            }

            if (ftpType.equalsIgnoreCase("FTP") && sshFTP != null && connected) {
                norFTP.disconnect();
            }

            if (ftpType.equalsIgnoreCase("ACTIVEFTP") && actFTP != null
                    && connected) {
                actFTP.disconnect();
            }
        }

    }

    /**
     * @param eleData
     * @param intCount
     * @throws Exception
     */
    private Map xmlData(String xml) throws Exception {
        Map xmlDetails = new HashMap();
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
                    .newInstance();
            javax.xml.parsers.DocumentBuilder docBuilder = docBuilderFactory
                    .newDocumentBuilder();
            docBuilderFactory.setNamespaceAware(true);

            Document doc = docBuilder.parse(new InputSource(new StringReader(
                    xml)));

            NodeList rootElement = doc
                    .getElementsByTagName("FTPProcessRequest");
            if (rootElement != null) {
                Element FTPProcessReq = (Element) rootElement.item(0);
                String locID = getLocNodeValue(FTPProcessReq, "LocationID");
                xmlDetails.put("locID", locID);
                String FromDir = getLocNodeValue(FTPProcessReq, "ReadFromDIR");
                xmlDetails.put("FromDir", FromDir);
                String appFlag = getLocNodeValue(FTPProcessReq, "AppFlag");
                xmlDetails.put("appFlag", appFlag);

                /*
                 * for (int k = 0; k <
                 * FTPProcessReq.getChildNodes().getLength(); k++) {
                 * System.out.println(">>>>> >>>>>> >>>>>>" +
                 * FTPProcessReq.getChildNodes().item(k).getLocalName()); }
                 */

                NodeList ftpInfoND = null;
                // FTPProcessReq.getElementsByTagName("ftpinfo");

                String nameSpaceURI = FTPProcessReq.getNamespaceURI();
                if (nameSpaceURI != null) {

                    ftpInfoND = FTPProcessReq.getElementsByTagNameNS(
                            nameSpaceURI, "ftpinfo");

                }
                else {
                    ftpInfoND = FTPProcessReq.getElementsByTagName("ftpinfo");
                }
                HashMap ftpinfo = new HashMap();
                for (int i = 0; i < ftpInfoND.getLength(); i++) {
                    String fileData = null;
                    String fileName = null;
                    String arcDir = null;
                    String destDir = null;
                    Element fromftpNDEle = (Element) ftpInfoND.item(i);
                    HashMap ftpFileInfo = new HashMap();
                    /*
                     * for (int k = 0; k <
                     * fromftpNDEle.getChildNodes().getLength(); k++) {
                     * System.out.println("------->>>>> >>>>>> >>>>>>" +
                     * fromftpNDEle.getChildNodes().item(k).getLocalName()); }
                     */
                    for (int k = 0; k < fromftpNDEle.getChildNodes()
                            .getLength(); k++) {
                        if (fromftpNDEle.getChildNodes().item(k).getNodeType() == Node.ELEMENT_NODE) {
                            String eleName = "";
                            if (nameSpaceURI != null) {
                                if (eleName != null) {
                                    eleName = fromftpNDEle.getChildNodes()
                                            .item(k).getLocalName();
                                }
                                else {
                                    eleName = fromftpNDEle.getChildNodes()
                                            .item(k).getNodeName();

                                }
                            }
                            if (eleName.equalsIgnoreCase("File-Name")) {
                                fileName = getLocNodeValue(fromftpNDEle,
                                        "File-Name");
                            }
                            if (eleName.equalsIgnoreCase("File-Data")) {
                                fileData = getLocNodeValue(fromftpNDEle,
                                        "File-Data");
                            }
                            if (eleName.equalsIgnoreCase("Change-HostDir")) {
                                destDir = getLocNodeValue(fromftpNDEle,
                                        "Change-HostDir");
                            }
                        }
                    }

                    ftpFileInfo.put("File-Name", fileName);
                    ftpFileInfo.put("File-Data", fileData);
                    ftpFileInfo.put("destDir", destDir);

                    String ftp_inf = "ftp-info" + i;
                    ftpinfo.put(ftp_inf, ftpFileInfo);

                }

                xmlDetails.put("ftpinfo", ftpinfo);

            }
        } catch (Exception ee) {
            System.out.println("Exception is >> " + ee);
            throw ee;
        }
        return xmlDetails;
    }

    private String getLocNodeValue(Element ele, String name) throws Exception {
        NodeList ND = null;
        Element Ele = null;
        NodeList NDList = null;
        String val = "";

        for (int i = 0; i < ele.getChildNodes().getLength(); i++) {
            String nameSpaceURI = ele.getChildNodes().item(i).getNamespaceURI();

            String nodeNspace = ele.getChildNodes().item(i).getPrefix();
            String localName = "";
            if (nodeNspace != null) {
                localName = ele.getChildNodes().item(i).getLocalName();
            }
            else {
                localName = ele.getChildNodes().item(i).getNodeName();
            }

            if (localName.equalsIgnoreCase(name)) {
                if (nodeNspace != null) {
                    ND = ele.getElementsByTagNameNS(nameSpaceURI, name);

                }
                else {
                    ND = ele.getElementsByTagName(name);
                }
                if (ND != null && ND.getLength() > 0) {
                    Ele = (Element) ND.item(0);
                }
                if (Ele != null && Ele.getChildNodes().getLength() > 0) {
                    NDList = Ele.getChildNodes();
                }
                if (NDList != null && NDList.getLength() > 0) {
                    val = NDList.item(0).getNodeValue();

                }
            }
        }

        return val;
    }

    public void archiveFile(String archiveDirPath, String fileName,
            byte[] pdfContent) throws Exception {
        try {

            String xmlPdf = new String(pdfContent);

            if ((archiveDirPath != null) && (archiveDirPath.length() > 0)) {
                FileOutputStream out = new FileOutputStream(archiveDirPath
                        + "/" + fileName);
                File temp = null;
                temp = new File(archiveDirPath + File.separator + fileName);

                // if (temp.createNewFile()) {
                FileOutputStream fos = new FileOutputStream(temp);
                fos.write(pdfContent, 0, pdfContent.length);
                // }
            }
        } catch (Exception ee) {
            throw new Exception("Archive Exception");
        }
    }

    public void archiveFile(String archiveDirPath, String fileName,
            String content, String arcFlag) throws Exception {
        try {
            if ((archiveDirPath != null) && (archiveDirPath.length() > 0)) {
                if (arcFlag.equalsIgnoreCase("true")) {

                    FileWriter fos = new FileWriter(archiveDirPath + "/"
                            + fileName, true);
                    BufferedWriter bw = new BufferedWriter(fos);
                    PrintWriter pw = new PrintWriter(fos, true);
                    pw.println(content);
                    pw.close();
                }
                else {
                    FileOutputStream out = new FileOutputStream(archiveDirPath
                            + "/" + fileName);
                    PrintStream p = new PrintStream(out);
                    p.print(content);
                    p.close();
                    out.close();
                }

            }
        } catch (Exception ee) {
            throw new Exception("Archive Exception" + ee);
        }
    }

    private void archiveFile(String rootDir, String fileName) throws Exception {
        try {
            File fin = new File(rootDir + "in" + "/" + fileName);
            InputStream in = new FileInputStream(fin);
            System.out.println("Arvhive directory is" + rootDir + "archives"
                    + "/" + fileName);
            OutputStream out = new FileOutputStream(rootDir + "archives" + "/"
                    + fileName);
            System.out.println("After arvhive");
            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            fin.delete();
        } catch (Exception ee) {

            throw new Exception(ee);
        }
    }

    private byte[] decodeData(String pdfDataEncoded) throws Exception {
        byte[] base64decoded = new byte[32768];
        try {

            base64decoded = RMT2Base64Decoder.decodeToBytes(pdfDataEncoded);

            // decodePdf= new String(base64decoded);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return base64decoded;
    }

}
