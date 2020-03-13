package com.api.util;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.RMT2RuntimeException;
import com.api.BatchFileException;

/**
 * Backups all files in a directory by packaging all files in a zip archive and
 * copying to some destination directory. Optionally, all files processed can be
 * deleted.
 * 
 * @author Roy Terrell
 * 
 */
public class DirectoryBackup {

    private static Logger logger;

    /**
     * The entry point to the file backup process.
     * 
     * @param args
     *            The first argument is the source directory, the second
     *            argument is the destination directory, the third is the name
     *            of zip file, and, optionally, the fourth argument is delete
     *            all files processed flag (true or false).
     */
    public static void main(String[] args) {
        String msg = null;
        if (args == null) {
            msg = "DirectoryBackup requires command line argumens";
            logger.error(msg);
            throw new RMT2RuntimeException(msg);
        }
        if (args.length < 3) {
            msg = "DirectoryBackup requires to  command line argumens: source/destination directories, the zip file name, and optional clean up flag, respectively";
            logger.error(msg);
            throw new RMT2RuntimeException(msg);
        }
        String srcDir = args[0];
        String destDir = args[1];
        String zipFileName = args[2];
        boolean cleanUp = false;

        if (args.length > 3) {
            cleanUp = Boolean.parseBoolean(args[3]);
        }

        DirectoryBackup mgr = new DirectoryBackup();
        int results;
        try {
            results = mgr.process(srcDir, destDir, zipFileName, cleanUp);
        } catch (BatchFileException e) {
            msg = "DirectoryBackup encountered an error processing batch of files";
            logger.error(msg, e);
            throw new RMT2RuntimeException(msg, e);
        }
        logger.info("Total files processed: " + results);

    }

    /**
     * 
     */
    public DirectoryBackup() {
        // Setup Logging environment
        String logPath = "config.localFwLog4j";
        Properties props = RMT2File.loadPropertiesFromClasspath(logPath);
        PropertyConfigurator.configure(props);
        logger = Logger.getLogger(DirectoryBackup.class);
        logger.info("Logger initialized");
    }

    /**
     * 
     * @param srcDir
     *            The directory containing the files to be zipped.
     * @param destDir
     *            This is where the packaged zip file will be stored. The
     *            directory must exist or an exception is thrown
     * @return
     */
    public int process(String srcDir, String destDir, String zipFileName,
            boolean cleanUp) throws BatchFileException {
        String msg = null;
        int count = 0;
        File srcDirFile = new File(srcDir);
        File destDirFile = new File(destDir);

        logger.info("Copying data files from " + srcDir + " to " + destDir);

        if (!srcDirFile.exists()) {
            msg = "The source directory does not exist: " + srcDir;
            logger.error(msg);
            throw new BackupFileDoesNotExistException(msg);
        }

        if (!destDirFile.exists()) {
            msg = "The destination directory does not exist: " + destDir;
            logger.error(msg);
            throw new BackupFileDoesNotExistException(msg);
        }

        List<String> fileListing = RMT2File.getDirectoryListing(srcDir, null);

        String zipFile = this.createZipFile(srcDirFile.getAbsolutePath(),
                this.buildZipFileName(zipFileName), fileListing);

        // Copy zip file to its destination
        try {
            System.out.println("copy zip file, " + zipFile
                    + ", to its destination, " + destDir);
            RMT2File.copyFileWithOverwrite(zipFile, destDir);
        } catch (IOException e) {
            throw new BatchFileException("Error occured copying " + zipFile
                    + " to " + destDir, e);
        }
        if (cleanUp) {
            for (String item : fileListing) {
                File f = new File(srcDir + "/" + item);
                RMT2File.deleteFile(f);
            }
        }

        // Remove temp zip file
        RMT2File.deleteFile(zipFile);
        return count;
    }

    private String buildZipFileName(String zipFileName) {
        Calendar dt = new GregorianCalendar();
        StringBuffer buf = new StringBuffer();
        buf.append(zipFileName);
        buf.append("_");
        buf.append(dt.get(Calendar.YEAR));
        buf.append("_");
        buf.append((dt.get(Calendar.MONTH) + 1) < 10 ? "0"
                + (dt.get(Calendar.MONTH) + 1) : (dt.get(Calendar.MONTH) + 1));
        buf.append("_");
        buf.append(dt.get(Calendar.DAY_OF_MONTH) < 10 ? "0"
                + dt.get(Calendar.DAY_OF_MONTH) : dt.get(Calendar.DAY_OF_MONTH));
        buf.append(".zip");
        return buf.toString();
    }

    private String createZipFile(String dirPath, String zipFileName,
            List<String> fileListing) {
        String filePath = dirPath + "/" + zipFileName;
        File tempFile = new File(filePath);
        if (tempFile.exists()) {
            RMT2File.deleteFile(tempFile);
        }
        RMT2ZipFileManager mgr = new RMT2ZipFileManager(filePath);
        mgr.setFilePathType(RMT2ZipFileManager.FILEPATH_FULL);
        for (String item : fileListing) {
            File f = new File(dirPath + "/" + item);
            if (f.exists()) {
                logger.info("Adding file, " + f.getPath() + ", to archive");
                System.out.println("Adding file, " + f.getPath()
                        + ", to archive");
                mgr.addEntry(f.getAbsolutePath(), f);
            }
        }
        File dir = new File(dirPath);

        mgr.addEntry(dir.getName(), dir);
        System.out.println("Constructing zip file...");
        logger.info("Constructing zip file...");
        mgr.close();
        System.out.println("Zip file Construction complete.");
        logger.info("Zip file Construction complete.");
        return filePath;
    }

}
