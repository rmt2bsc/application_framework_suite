package com.api.messaging.ftp;

import java.io.IOException;
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

    /**
     * Determines if the full file path is a directory.
     * 
     * @param path
     *            the full path of the directory
     * @return true when proved to be a directory. Otherwise, false.
     * @throws IOException
     */
    boolean isDirectory(String path) throws IOException;

    /**
     * Determines if the full path is a file.
     * 
     * @param path
     *            the full path of the file
     * @return true when proved to be a file. Otherwise, false.
     * @throws IOException
     */
    boolean isFile(String path) throws IOException;

    /**
     * Get the location of the user's session work area where data serializaton
     * and intermitted file manipulation occurs.
     * 
     * @return the directory path of the user session work area.
     */
    String getUserSessionWorkArea();
}
