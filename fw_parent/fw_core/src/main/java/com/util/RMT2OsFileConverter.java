package com.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Converts text files so that they can be read by text editors on various
 * systems.
 * <p>
 * This implementation helps to remedy problems with text editors such as
 * Notepad which often display squares in place of line endings they can't
 * understand, causing text formatting to be lost.
 * 
 * @author rterrell
 * 
 */
public class RMT2OsFileConverter {

    /**
     * The system line ending as determined by
     * System.getProperty("line.separator")
     * 
     * @since 1.00.00
     */
    public final static int STYLE_SYSTEM = 0;

    /**
     * The Windows and DOS line ending ("\r\n")
     * 
     * @since 1.00.00
     */
    public final static int STYLE_WINDOWS = 1;

    /**
     * The Windows and DOS line ending ("\r\n")
     * 
     * @since 1.00.00
     */
    public final static int STYLE_DOS = 1;

    /**
     * The Windows and DOS line ending ("\r\n")
     * 
     * @since 1.00.00
     */
    public final static int STYLE_RN = 1;

    /**
     * The UNIX and Java line ending ("\n")
     * 
     * @since 1.00.00
     */
    public final static int STYLE_UNIX = 2;

    /**
     * The UNIX and Java line ending ("\n")
     * 
     * @since 1.00.00
     */
    public final static int STYLE_N = 2;

    /**
     * The UNIX and Java line ending ("\n")
     * 
     * @since 1.00.00
     */
    public final static int STYLE_JAVA = 2;

    /**
     * The Macintosh line ending ("\r")
     * 
     * @since 1.00.00
     */
    public final static int STYLE_MAC = 3;

    /**
     * The Macintosh line ending ("\r")
     * 
     * @since 1.00.00
     */
    public final static int STYLE_R = 3;

    /**
     * Buffer size when reading from input stream.
     * 
     * @since 1.00.00
     */
    private final static int BUFFER_SIZE = 1024;

    private final static int STATE_INIT = 0;

    private final static int STATE_R = 1;

    private final static int MASK_N = 0x01;

    private final static int MASK_R = 0x02;

    private final static int MASK_RN = 0x04;

    // private int style = STYLE_SYSTEM;

    /**
     * Change the line endings of the text contained in a String and write the
     * results it to the designated output stream.
     * 
     * @param data
     *            the data that is to be converted
     * @param outFilePath
     *            the file path where the results are to be output.
     * @param style
     *            line separator style.
     * @return true if the output was modified from the input, false if it is
     *         exactly the same
     * @throws IOException
     *             if an input or output error occurs.
     */
    public static boolean convert(String data, String outFilePath, int style)
            throws IOException {
        File source = new File(outFilePath);
        FileOutputStream fos = new FileOutputStream(source);
        ByteArrayInputStream bais = new ByteArrayInputStream(data.getBytes());
        boolean rc = convert(bais, fos, style);
        fos.flush();
        fos.close();
        bais.close();

        return rc;
    }

    /**
     * Change the line endings of the text on the input stream and write it to
     * the output stream.
     * 
     * The current system's line separator is used.
     * 
     * @param in
     *            stream that contains the text which needs line number
     *            conversion.
     * @param out
     *            stream where converted text is written.
     * @return true if the output was modified from the input, false if it is
     *         exactly the same
     * @throws BinaryDataException
     *             if non-text data is encountered.
     * @throws IOException
     *             if an input or output error occurs.
     * 
     * @since 1.00.00
     */
    public static boolean convert(InputStream in, OutputStream out)
            throws IOException {
        return convert(in, out, STYLE_SYSTEM, true);
    }

    /**
     * Change the line endings of the text on the input stream and write it to
     * the output stream.
     * 
     * @param in
     *            stream that contains the text which needs line number
     *            conversion.
     * @param out
     *            stream where converted text is written.
     * @param style
     *            line separator style.
     * @return true if the output was modified from the input, false if it is
     *         exactly the same
     * @throws BinaryDataException
     *             if non-text data is encountered.
     * @throws IOException
     *             if an input or output error occurs.
     * @throws IllegalArgumentException
     *             if an unknown style is requested.
     * 
     * @since 1.00.00
     */
    public static boolean convert(InputStream in, OutputStream out, int style)
            throws IOException {
        return convert(in, out, style, true);
    }

    /**
     * Change the line endings of the text on the input stream and write it to
     * the output stream.
     * 
     * The current system's line separator is used.
     * 
     * @param in
     *            stream that contains the text which needs line number
     *            conversion.
     * @param out
     *            stream where converted text is written.
     * @param binaryException
     *            throw an exception and abort the operation if binary data is
     *            encountered and binaryExcepion is false.
     * @return true if the output was modified from the input, false if it is
     *         exactly the same
     * @throws BinaryDataException
     *             if non-text data is encountered.
     * @throws IOException
     *             if an input or output error occurs.
     * 
     * @since 1.00.00
     */
    public static boolean convert(InputStream in, OutputStream out,
            boolean binaryException) throws IOException {
        return convert(in, out, STYLE_SYSTEM, binaryException);
    }

    /**
     * Change the line endings of the text on the input stream and write it to
     * the output stream.
     * 
     * @param in
     *            stream that contains the text which needs line number
     *            conversion.
     * @param out
     *            stream where converted text is written.
     * @param style
     *            line separator style.
     * @param binaryException
     *            throw an exception and abort the operation if binary data is
     *            encountered and binaryExcepion is false.
     * @return true if the output was modified from the input, false if it is
     *         exactly the same
     * @throws BinaryDataException
     *             if non-text data is encountered.
     * @throws IOException
     *             if an input or output error occurs.
     * @throws IllegalArgumentException
     *             if an unknown style is requested.
     * 
     * @since 1.00.00
     */
    public static boolean convert(InputStream in, OutputStream out, int style,
            boolean binaryException) throws IOException {
        byte[] lineEnding;
        byte[] formfeed = new byte[] { (byte) '\f' };

        switch (style) {
            case STYLE_SYSTEM: {
                lineEnding = System.getProperty("line.separator").getBytes();
            }
                break;
            case STYLE_RN: {
                lineEnding = new byte[] { (byte) '\r', (byte) '\n' };
            }
                break;
            case STYLE_R: {
                lineEnding = new byte[] { (byte) '\r' };
            }
                break;
            case STYLE_N: {
                lineEnding = new byte[] { (byte) '\n' };
            }
                break;
            default: {
                throw new IllegalArgumentException("Unknown line break style: "
                        + style);
            }
        }
        byte[] buffer = new byte[BUFFER_SIZE];
        int read;
        int state = STATE_INIT;
        int seen = 0x00;
        while ((read = in.read(buffer)) != -1) {
            for (int i = 0; i < read; i++) {
                byte b = buffer[i];
                if (state == STATE_R) {
                    if (b != '\n') {
                        out.write(lineEnding);
                        seen |= MASK_R;
                    }
                }
                if (b == '\r') {
                    state = STATE_R;
                }
                else {
                    if (b == '\n') {
                        if (state == STATE_R) {
                            seen |= MASK_RN;
                        }
                        else {
                            seen |= MASK_N;
                        }
                        out.write(lineEnding);
                    }
                    else if (b == '\f') {
                        if (state == STATE_R) {
                            seen |= MASK_RN;
                        }
                        else {
                            seen |= MASK_N;
                        }
                        out.write(formfeed);
                    }
                    else if (binaryException && b != '\t' && b != '\f'
                            && (b & 0xff) < 32) {
                        throw new RMT2OsFileConverterException(
                                "A error occurred regarding invalid binary data was found");
                    }
                    else {
                        out.write(b);
                    }
                    state = STATE_INIT;
                }
            }
        }
        if (state == STATE_R) {
            out.write(lineEnding);
            seen |= MASK_R;
        }
        if (lineEnding.length == 2 && lineEnding[0] == '\r'
                && lineEnding[1] == '\n') {
            return ((seen & ~MASK_RN) != 0);
        }
        else if (lineEnding.length == 1 && lineEnding[0] == '\r') {
            return ((seen & ~MASK_R) != 0);
        }
        else if (lineEnding.length == 1 && lineEnding[0] == '\n') {
            return ((seen & ~MASK_N) != 0);
        }
        else {
            return true;
        }
    }

    /**
     * Change the line endings on given file.
     * 
     * The current system's line separator is used.
     * 
     * @param f
     *            File to be converted.
     * @return true if the file was modified, false if it was already in the
     *         correct format
     * @throws BinaryDataException
     *             if non-text data is encountered.
     * @throws IOException
     *             if an input or output error occurs.
     * 
     * @since 1.00.00
     */
    public static boolean convert(File f) throws IOException {
        return convert(f, STYLE_SYSTEM, true);
    }

    /**
     * Change the line endings on given file.
     * 
     * @param f
     *            File to be converted.
     * @param style
     *            line separator style.
     * @return true if the file was modified, false if it was already in the
     *         correct format
     * @throws BinaryDataException
     *             if non-text data is encountered.
     * @throws IOException
     *             if an input or output error occurs.
     * @throws IllegalArgumentException
     *             if an unknown style is requested.
     * 
     * @since 1.00.00
     */
    public static boolean convert(File f, int style) throws IOException {
        return convert(f, style, true);
    }

    /**
     * Change the line endings on given file.
     * 
     * The current system's line separator is used.
     * 
     * @param f
     *            File to be converted.
     * @param binaryException
     *            throw an exception and abort the operation if binary data is
     *            encountered and binaryExcepion is false.
     * @return true if the file was modified, false if it was already in the
     *         correct format
     * @throws BinaryDataException
     *             if non-text data is encountered.
     * @throws IOException
     *             if an input or output error occurs.
     * 
     * @since 1.00.00
     */
    public static boolean convert(File f, boolean binaryException)
            throws IOException {
        return convert(f, STYLE_SYSTEM, binaryException);
    }

    /**
     * Change the line endings on given file.
     * 
     * @param f
     *            File to be converted.
     * @param style
     *            line separator style.
     * @param binaryException
     *            throw an exception and abort the operation if binary data is
     *            encountered and binaryExcepion is false.
     * @return true if the file was modified, false if it was already in the
     *         correct format
     * @throws BinaryDataException
     *             if non-text data is encountered.
     * @throws IOException
     *             if an input or output error occurs.
     * @throws IllegalArgumentException
     *             if an unknown style is requested.
     * 
     * @since 1.00.00
     */
    public static boolean convert(File f, int style, boolean binaryException)
            throws IOException {
        File temp = null;
        InputStream in = null;
        OutputStream out = null;
        boolean modified = false;
        try {
            in = new FileInputStream(f);
            temp = File.createTempFile("LineEnds", null, null);
            out = new FileOutputStream(temp);
            modified = convert(in, out, style, binaryException);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
            if (modified) {
                RMT2OsFileConverter.move(temp, f, true);
            }
            else {
                if (!temp.delete()) {
                    throw new IOException(
                            "Unable to delete temporary file conversion operation");
                }
            }
        } finally {
            if (in != null) {
                in.close();
                in = null;
            }
            if (out != null) {
                out.flush();
                out.close();
                out = null;
            }
        }
        return modified;
    }

    /**
     * Move a file from one location to another. An attempt is made to rename
     * the file and if that fails, the file is copied and the old file deleted.
     * 
     * @param from
     *            file which should be moved.
     * @param to
     *            desired destination of the file.
     * @param overwrite
     *            If false, an exception will be thrown rather than overwrite a
     *            file.
     * @throws IOException
     *             if an error occurs.
     * 
     * @since ostermillerutils 1.00.00
     */
    public static void move(File from, File to, boolean overwrite)
            throws IOException {
        if (to.exists()) {
            if (overwrite) {
                if (!to.delete()) {
                    throw new IOException(
                            "Unable to delete existing destination file during move operation");
                }
            }
            else {
                throw new IOException(
                        "Unable to perform move operation due to overwrite directive is not enable for existing destination file");
            }
        }

        if (from.renameTo(to))
            return;

        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(from);
            out = new FileOutputStream(to);
            copy(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
            if (!from.delete()) {
                throw new IOException(
                        "Unable to delete source file during move operation");
            }
        } finally {
            if (in != null) {
                in.close();
                in = null;
            }
            if (out != null) {
                out.flush();
                out.close();
                out = null;
            }
        }
    }

    /**
     * Copy the data from the input stream to the output stream.
     * 
     * @param in
     *            data source
     * @param out
     *            data destination
     * @throws IOException
     *             in an input or output error occurs
     * 
     * @since ostermillerutils 1.00.00
     */
    private static void copy(InputStream in, OutputStream out)
            throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

}
