package com.api;

import java.io.File;
import java.util.List;

/**
 * A general interface for processing batches of files obtained from some
 * external datasource.
 * 
 * @author appdev
 * 
 */
public interface BatchFileProcessor {

    /**
     * Setup connection for some external datasource which the configuration is
     * transparent to the user.
     * 
     * @throws BatchFileException
     */
    void initConnection() throws BatchFileException;

    /**
     * Setup connection for an arbitrary external datasource which the
     * configuration is known at implementation.
     * 
     * @param extSource
     * @throws BatchFileException
     */
    void initConnection(Object extSource) throws BatchFileException;

    /**
     * Setup connections for two external datasources which the configurations
     * are specified by the user as .properties files.
     * 
     * @param configFile1
     *            configuration for datasource #1
     * @param configFile2
     *            configuration for datasource #2
     * @throws BatchFileException
     */
    void initConnection(String configFile1, String configFile2)
            throws BatchFileException;

    /**
     * Release resources
     */
    void close();

    /**
     * The implementation of the method should function to obtain a listing of
     * file names to process.
     * 
     * @return List<String> a list of String values representing file names
     */
    List<String> getFileListing();

    /**
     * Driver of the batch file process
     * 
     * @return int
     * @throws BatchFileException
     */
    int processBatch() throws BatchFileException;

    /**
     * Process all files specified in directory, <i>dir</i>
     * 
     * @param dir
     *            the directory path containing all files to process
     * @param parent
     * @return int
     * @throws BatchFileException
     */
    Object processDirectory(File dir, Object parent) throws BatchFileException;

    /**
     * The implementation of this method should function to process a list of
     * <i>files</i>.
     * 
     * @param files
     *            a List<String> of fiel names to process
     * @param parent
     * @return int
     * @throws BatchFileException
     */
    Object processFiles(List<String> files, Object parent)
            throws BatchFileException;

    /**
     * Processes a single file. The single file is represented by its filename.
     * 
     * @param fileName
     *            the name of the file to process.
     * @param parent
     * @return int
     * @throws BatchFileException
     */
    Object processSingleFile(String fileName, Object parent)
            throws BatchFileException;

    /**
     * Processes a single file. THe file is represented as a java.io.File
     * instance.
     * 
     * @param file
     *            an instance of java.io.File
     * @param parent
     * @return int
     * @throws BatchFileException
     */
    Object processSingleFile(File file, Object parent)
            throws BatchFileException;

}
