package com.api.messaging.ftp;

import java.io.Serializable;
import java.util.List;

import com.api.messaging.MessageException;
import com.api.messaging.MessageManager;

/**
 * Interface that provides methods for setting up, downloading and uploading
 * files from one FTP server to another via the FTP protocol.
 * 
 * @author RTerrell
 * 
 */
public interface FtpApi extends MessageManager {


    /**
     * Downloads a single file from the FTP server.
     * 
     * @param path
     *            the complete path for the file to download
     * @return Serializable an arbitrary object representing the downloaded
     *         file.
     * @throws FtpException
     */
    Serializable downloadFile(String path) throws MessageException;

    /**
     * Downloads one or more files from a directory on the FTP server
     * 
     * @param directory
     *            the target directory to downlonad files
     * @param subFolders
     *            indicates whether or not to traverse subfolders. <i>true</i> =
     *            fetch subdirectories and <i>false</i> = do not tarvers sub
     *            directories.
     * @return Serializable which should be a list of arbitrary objects
     *         representing the downloaded files.
     * @throws FtpException
     */
    Serializable downloadFile(String directory, boolean subFolders) throws MessageException;

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
    List<String> getDirectoryListing(String directory, boolean subFolders) throws MessageException;
}
