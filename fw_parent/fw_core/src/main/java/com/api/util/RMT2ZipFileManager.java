package com.api.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

/**
 * Utility class to create and or update a zip file. Entries may be added,
 * updated, and/or deleted.
 * 
 * @author Roy Terrell
 * 
 */
public class RMT2ZipFileManager {

    private static Logger logger = Logger.getLogger("RMT2ZipFileManager");

    public static final int FILEPATH_NONE = 0;

    public static final int FILEPATH_RELATIVE = 1;

    public static final int FILEPATH_FULL = 2;

    public static final int FILEPATH_FULL_DRIVE = 3;

    private int filePathType;

    private ZipFile zipFile;

    private File file;

    private Map zipEntries = new TreeMap(); // Key: String name, Value:
                                            // ZipStreamEntry

    /**
     * Open a zip file. If the filename exists it is assumed to be a valid zip
     * file and all current entries will be loaded. If the filename does not
     * exist the assumption is that a new zip file should be created.
     * 
     * @param fileName
     *            The name of the zip file to open.
     * 
     * @throws ZipFileManagerException
     *             if an error is encountered opening the zip file.
     */
    public RMT2ZipFileManager(String fileName) {
        this(new File(fileName));
    }

    /**
     * Open a zip file. If the file exists it is assumed to be a valid zip file
     * and all current entries will be loaded. If the file does not exist the
     * assumption is that a new zip file should be created.
     * 
     * @param file
     *            File object representing the zip file to open/create.
     * 
     * @throws ZipFileManagerException
     *             if an error is encountered opening the zip file.
     */
    public RMT2ZipFileManager(File file) throws ZipFileException {
        this.filePathType = RMT2ZipFileManager.FILEPATH_NONE;
        if (null == file) {
            throw new ZipFileException("Zipfile may not be null");
        }

        this.file = file;
        try {
            if (file.exists()) {
                zipFile = new ZipFile(file);
                loadZipFile();
            }
        } catch (Exception e) {
            throw new ZipFileException("Error opening zipfile "
                    + file.getName(), e);
        }
    }

    /**
     * Add an entry to the zip file using an input stream. For file objects on
     * the local (or mapped) drive(s) the version of addEntry which takes a
     * java.io.File object is preferred as it reduces the number of files that
     * need to be open at one time.
     * 
     * The ZipFileManager class assumes responsibility for the InputStream
     * object passed and will close it after the zip file is updated/created.
     * The calling application should not perform further operations on this
     * stream.
     * 
     * Note that the entry is not actually persisted to the zip file until the
     * ZipFileManager object is closed.
     * 
     * @param name
     *            The fully qualified name of the entry in the zip file (i.e.
     *            somedir/myfile.txt).
     * @param inStream
     *            The InputStream representing the data to be associated with
     *            this name in the zip file.
     * 
     * @throws IllegalArgumentException
     *             if either name or inStream are null.
     */
    public void addEntry(String name, InputStream inStream)
            throws IllegalArgumentException {
        validate();
        if (null == name)
            throw new IllegalArgumentException("Name may not be null");

        if (null == inStream)
            throw new IllegalArgumentException("Input stream may not be null");

        deleteEntry(name);
        ZipEntry ze = new ZipEntry(name);
        ZipStreamEntry entry = new ZipStreamEntry(ze, inStream);
        zipEntries.put(name, entry);
    }

    /**
     * Add an entry to the zip file using a File object.
     * 
     * Note that the entry is not actually persisted to the zip file until the
     * ZipFileManager object is closed.
     * 
     * @param name
     *            The fully qualified name of the entry in the zip file (i.e.
     *            somedir/myfile.txt).
     * @param inFile
     *            The File object representing the data to be associated with
     *            this name in the zip file.
     * 
     * @throws IllegalArgumentException
     *             if either name or inFile are null.
     */
    public void addEntry(String name, File inFile) {
        validate();
        if (null == name)
            throw new IllegalArgumentException("Name may not be null");

        if (null == inFile)
            throw new IllegalArgumentException("Input file may not be null");

        if (inFile.isFile()) {
            String entryName = this.getFileEntryName(name, inFile);
            logger.info("Adding " + inFile.getAbsolutePath());
            deleteEntry(entryName);
            ZipEntry ze = new ZipEntry(entryName);
            ze.setTime(inFile.lastModified());
            ZipStreamEntry entry = new ZipStreamEntry(ze, inFile);
            zipEntries.put(entryName, entry);
        }
        else if (inFile.isDirectory()) {
            this.addFilesFromDirectory(inFile);
        }

    }

    /**
     * 
     * @param dir
     */
    private void addFilesFromDirectory(File dir) {
        logger.info("Processing files from directory " + dir.getAbsolutePath());
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isDirectory()) {
                // Recusrsively process sub directory
                this.addFilesFromDirectory(file);
                continue;
            }
            logger.info("Adding " + file.getAbsolutePath());
            String entryName = this.getFileEntryName(file.getName(), file);
            deleteEntry(entryName);

            // create ZipEntry for current file
            ZipEntry ze = new ZipEntry(entryName);
            ze.setTime(file.lastModified());
            ZipStreamEntry entry = new ZipStreamEntry(ze, file);
            zipEntries.put(entryName, entry);
        }
        return;
    }

    /**
     * Remove an existing entry from the zip file. If the entry does not exist
     * this method returns without performing any operation.
     * 
     * @param name
     *            The name of the entry to be removed.
     */
    public void deleteEntry(String name) {
        validate();
        ZipStreamEntry zs = (ZipStreamEntry) zipEntries.remove(name);
        if (null != zs)
            zs.closeStream();
    }

    private String getFileEntryName(String inputName, File inFile) {
        String entryName = null;
        switch (this.filePathType) {
            case RMT2ZipFileManager.FILEPATH_NONE:
                entryName = inputName;
                break;

            case RMT2ZipFileManager.FILEPATH_RELATIVE:
                break;

            case RMT2ZipFileManager.FILEPATH_FULL:
                entryName = RMT2File.getFullFilePathWithoutDrive(inFile
                        .getAbsolutePath());
                break;

            case RMT2ZipFileManager.FILEPATH_FULL_DRIVE:
                entryName = inFile.getAbsolutePath();
                break;
        }
        return entryName;
    }

    /**
     * Close the ZipFileManager and update/create the zip file. All active
     * entries will be persisted to disk in the requested zip file.
     */
    public void close() {
        validate();
        if (null == zipFile)
            createNewZipFile();
        else
            updateExistingZipFile();

        file = null;
    }

    /**
     * Return the number of entries in the current zip file.
     * 
     * @return The count of zip file entries.
     */
    public int getZipEntryCount() {
        return (zipEntries.size());
    }

    /**
     * Retrieve the named zip file entry.
     * 
     * @param name
     *            The name of the entry.
     * 
     * @return The ZipEntry for the named entry or null if the name does not
     *         exist.
     */
    public ZipEntry getZipEntry(String name) {
        ZipStreamEntry zs = (ZipStreamEntry) zipEntries.get(name);
        if (null == zs)
            return (null);

        return (zs.getZipEntry());
    }

    /**
     * Return a list of all the active zip entries in the zip file.
     * 
     * @return A java.utit.List containing a java.util.zip.ZipEntry for each
     *         entry in the zip file.
     */
    public List getZipEntries() {
        List entries = new ArrayList();
        Iterator i = zipEntries.values().iterator();
        while (i.hasNext()) {
            ZipStreamEntry entry = (ZipStreamEntry) i.next();
            entries.add(entry.getZipEntry());
        }

        return (entries);
    }

    /**
     * Return the input stream for reading the contents of a zip member.
     * 
     * @param zipEntry
     *            The zip entry for the stream to be returned.
     * 
     * @return The InputStream for the entry.
     */
    public InputStream getZipInputStream(ZipEntry zipEntry) {
        try {
            return (zipFile.getInputStream(zipEntry));
        } catch (IOException e) {
            throw new ZipFileException("Error getting input stream for "
                    + zipEntry.getName(), e);
        }
    }

    private void validate() {
        if (null == file)
            throw new ZipFileException("No valid zipfile is open");
    }

    private void createNewZipFile() {
        try {
            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(
                    file));
            writeZipOut(zipOut);
        } catch (Exception e) {
            throw new ZipFileException("Error creating zip file: "
                    + e.getMessage(), e);
        }
    }

    private void updateExistingZipFile() {
        try {
            String tmpFileName = "tmp_" + file.getName();
            File tmpFile = File.createTempFile(tmpFileName, "");
            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(
                    tmpFile));
            writeZipOut(zipOut);
            zipFile.close();
            File holdFile = new File(file.getCanonicalFile() + ".old");
            if (holdFile.exists()) {
                if (!holdFile.delete())
                    throw new ZipFileException("Unable to delete " + holdFile);
            }

            if (!file.renameTo(holdFile))
                throw new ZipFileException("Unable to rename " + file + " to "
                        + holdFile);

            if (!tmpFile.renameTo(file))
                throw new ZipFileException("Unable to rename " + tmpFile
                        + " to " + file);
            ;

            holdFile.delete();
        } catch (ZipFileException e) {
            throw e;
        } catch (Exception e) {
            throw new ZipFileException("Error updating zip file: "
                    + e.getMessage(), e);
        }
    }

    private void writeZipOut(ZipOutputStream out) throws IOException {
        byte[] buf = new byte[8192];
        Iterator i = zipEntries.keySet().iterator();
        while (i.hasNext()) {
            String name = (String) i.next();
            ZipStreamEntry entry = (ZipStreamEntry) zipEntries.get(name);
            ZipEntry zipEntry = entry.getZipEntry();
            InputStream in = entry.getInStream();
            if (null == in)
                in = zipFile.getInputStream(zipEntry);

            out.putNextEntry(zipEntry);
            int len = in.read(buf);
            while (len > 0) {
                out.write(buf, 0, len);
                len = in.read(buf);
            }

            in.close();
            out.closeEntry();
        }

        out.close();
    }

    private void loadZipFile() {
        Enumeration entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry e = (ZipEntry) entries.nextElement();
            if (!e.getName().endsWith("/")) {
                ZipStreamEntry entry = new ZipStreamEntry(e);
                zipEntries.put(entry.getName(), entry);
            }
        }
    }

    private static class ZipStreamEntry {
        private ZipEntry zipEntry;

        private InputStream inStream;

        private File inFile;

        public ZipStreamEntry(ZipEntry zipEntry) {
            this.zipEntry = zipEntry;
        }

        public ZipStreamEntry(ZipEntry zipEntry, InputStream inStream) {
            this.zipEntry = zipEntry;
            this.inStream = inStream;
        }

        public ZipStreamEntry(ZipEntry zipEntry, File inFile) {
            this.zipEntry = zipEntry;
            this.inFile = inFile;
        }

        public InputStream getInStream() {
            try {
                if (null == inStream && null != inFile)
                    inStream = new FileInputStream(inFile);
            } catch (FileNotFoundException e) {
                throw new ZipFileException(e.getMessage(), e);
            }

            return (inStream);
        }

        public ZipEntry getZipEntry() {
            return (zipEntry);
        }

        public String getName() {
            return (zipEntry.getName());
        }

        public void closeStream() {
            if (null != inStream)
                try {
                    inStream.close();
                } catch (IOException e) {
                }
        }

    }

    public int getFilePathType() {
        return filePathType;
    }

    public void setFilePathType(int filePathType) {
        this.filePathType = filePathType;
    }

}
