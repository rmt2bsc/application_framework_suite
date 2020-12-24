package com.api.messaging.ftp;

import java.util.List;

import com.api.messaging.MessageException;
import com.api.messaging.MessageManager;

/**
 * Interface that provides methods for listing, downloading and uploading files
 * from one FTP server to another via the FTP protocol.
 * 
 * @author RTerrell
 * 
 */
public interface FtpApi extends MessageManager {


    /**
     * Downloads a single file from the FTP server.
     * 
     * @param remoteFile
     *            the complete path for the file to download
     * @return String the full path where the file was downloaded
     * @throws FtpException
     */
    String downloadFile(String remoteFile) throws MessageException;

    /**
     * Downloads one or more files from a directory on the FTP server
     * 
     * @param directory
     *            the target directory to downlonad files
     * @param subFolders
     *            indicates whether or not to traverse subfolders. <i>true</i> =
     *            fetch subdirectories and <i>false</i> = do not tarvers sub
     *            directories.
     * @return int total number files saved to disk
     * @throws FtpException
     */
    int downloadFile(String directory, boolean subFolders) throws MessageException;

    /**
     * List the contents of a directory on a FTP server.
     * 
     * @param directory
     *            the target directory to obtain file listing
     * @param subFolders
     *            indicates whether or not to traverse subfolders. <i>true</i> =
     *            list subdirectories and <i>false</i> = do not list sub
     *            directories.
     * @return List of Strings containing all files and directories found.
     * @throws FtpException
     */
    List<String> listDirectory(String directory, boolean subFolders) throws MessageException;
}
