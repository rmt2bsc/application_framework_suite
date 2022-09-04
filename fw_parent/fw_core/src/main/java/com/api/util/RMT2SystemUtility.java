/**
 * 
 */
package com.api.util;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.SystemException;

/**
 * @author Roy Terrell
 * 
 */
public class RMT2SystemUtility {

    static boolean RESOLOVE_DNS = Boolean.getBoolean("resolve.dns"); // Boolean.valueOf(System.getProperty("resolve.dns",
                                                                     // "false")).booleanValue();

    /**
     * Verifies that val is <= max memory
     * 
     * @param buf_name
     * @param val
     */
    public static void checkMemoryBufferSize(String buf_name, long val) {
        // sanity check that max_credits doesn't exceed memory allocated to VM
        // by -Xmx
        long max_mem = Runtime.getRuntime().maxMemory();
        if (val > max_mem) {
            throw new IllegalArgumentException(buf_name + "("
                    + RMT2SystemUtility.printBytes(val)
                    + ") exceeds max memory allocated to VM ("
                    + RMT2SystemUtility.printBytes(max_mem) + ")");
        }
    }

    /**
     * 
     * @param bytes
     * @return
     */
    public static String printBytes(long bytes) {
        double tmp;
        NumberFormat f = NumberFormat.getNumberInstance();

        if (bytes < 1000) {
            return bytes + "b";
        }
        if (bytes < 1000000) {
            tmp = bytes / 1000.0;
            return f.format(tmp) + "KB";
        }
        if (bytes < 1000000000) {
            tmp = bytes / 1000000.0;
            return f.format(tmp) + "MB";
        }
        else {
            tmp = bytes / 1000000000.0;
            return f.format(tmp) + "GB";
        }
    }

    /**
     * 
     * @param bytes
     * @return
     */
    public static String printBytes(double bytes) {
        double tmp;
        NumberFormat f = NumberFormat.getNumberInstance();

        if (bytes < 1000) {
            return bytes + "b";
        }
        if (bytes < 1000000) {
            tmp = bytes / 1000.0;
            return f.format(tmp) + "KB";
        }
        if (bytes < 1000000000) {
            tmp = bytes / 1000000.0;
            return f.format(tmp) + "MB";
        }
        else {
            tmp = bytes / 1000000000.0;
            return f.format(tmp) + "GB";
        }
    }

    /**
     * 
     * @return
     */
    public static boolean isLinux() {
        String os = System.getProperty("os.name");
        return os != null && os.toLowerCase().startsWith("linux");
    }

    /**
     * 
     * @return
     */
    public static boolean isSolaris() {
        String os = System.getProperty("os.name");
        return os != null && os.toLowerCase().startsWith("sun");
    }

    /**
     * 
     * @return
     */
    public static boolean isWindows() {
        String os = System.getProperty("os.name");
        return os != null && os.toLowerCase().startsWith("win");
    }

    /**
     * 
     * @return
     */
    public static boolean isMac() {
        String os = System.getProperty("os.name");
        return os != null && os.toLowerCase().startsWith("mac os x");
    }


    /**
     * 
     * @return
     */
    public static String getOSVersion() {
        return System.getProperty("os.version");
    }

    /**
     * Determines if Mac OS is Catalina version or later which is equipped with
     * dual partitions better known as Macintosh HD and Macintosh HD - Data.
     * 
     * @return true when Mac OS is Catelina or later. Otherwise, returns false
     *         when OS is not a Mac or the version is less than Catalina.
     *         returned.
     */
    public static boolean isMacOSNewGenerationArchitecture() {
        if (!RMT2SystemUtility.isMac()) {
            return false;
        }
        String ver = RMT2SystemUtility.getOSVersion();
        String parsedVer[] = ver.split("\\.");
        int minVer = 0;
        int majVer = RMT2Money.stringToNumber(parsedVer[0], "##").intValue();
        if (majVer > 10) {
            // Mac OS is Catalina or later...
            return true;
        }
        else if (majVer == 10) {
            if (parsedVer.length > 1) {
                minVer = RMT2Money.stringToNumber(parsedVer[1], "##").intValue();
                if (minVer >= 15) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 
     */
    public static void crash() {
        System.exit(-1);
    }

    /**
     * 
     * @return
     * @throws SocketException
     */
    public static InetAddress getFirstNonLoopbackAddress()
            throws SocketException {
        Enumeration<NetworkInterface> en = NetworkInterface
                .getNetworkInterfaces();
        boolean preferIpv4 = Boolean.getBoolean("java.net.preferIPv4Stack");
        boolean preferIPv6 = Boolean.getBoolean("java.net.preferIPv6Addresses");
        while (en.hasMoreElements()) {
            NetworkInterface i = (NetworkInterface) en.nextElement();
            for (Enumeration<InetAddress> en2 = i.getInetAddresses(); en2
                    .hasMoreElements();) {
                InetAddress addr = en2.nextElement();
                if (!addr.isLoopbackAddress()) {
                    if (addr instanceof Inet4Address) {
                        if (preferIPv6) {
                            continue;
                        }
                        return addr;
                    }
                    if (addr instanceof Inet6Address) {
                        if (preferIpv4) {
                            continue;
                        }
                        return addr;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 
     * @return
     * @throws SocketException
     */
    public static InetAddress getFirstNonLoopbackIPv6Address()
            throws SocketException {
        Enumeration<NetworkInterface> en = NetworkInterface
                .getNetworkInterfaces();
        boolean preferIpv4 = false;
        boolean preferIPv6 = true;
        while (en.hasMoreElements()) {
            NetworkInterface i = (NetworkInterface) en.nextElement();
            for (Enumeration<InetAddress> en2 = i.getInetAddresses(); en2
                    .hasMoreElements();) {
                InetAddress addr = en2.nextElement();
                if (!addr.isLoopbackAddress()) {
                    if (addr instanceof Inet4Address) {
                        if (preferIPv6) {
                            continue;
                        }
                        return addr;
                    }
                    if (addr instanceof Inet6Address) {
                        if (preferIpv4) {
                            continue;
                        }
                        return addr;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 
     * @return
     * @throws SocketException
     */
    public static List<NetworkInterface> getAllAvailableInterfaces()
            throws SocketException {
        List<NetworkInterface> retval = new ArrayList<NetworkInterface>(10);
        NetworkInterface intf;
        for (Enumeration<NetworkInterface> en = NetworkInterface
                .getNetworkInterfaces(); en.hasMoreElements();) {
            intf = (NetworkInterface) en.nextElement();
            retval.add(intf);
        }
        return retval;
    }

    /**
     * Returns the name of the computer host relative to the runtime of this
     * method invocation
     * 
     * @return String
     */
    public static String getHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception ex) {
            return "localhost";
        }

    }

    /**
     * Checks whether 2 Addresses are on the same host
     * 
     * @param host1
     * @param host2
     * @return
     */
    public static boolean sameHost(String host1, String host2) {
        InetAddress a, b;
        String hostA, hostB;

        if (host1 == null || host2 == null) {
            return false;
        }

        try {
            a = InetAddress.getByName(host1);
            b = InetAddress.getByName(host2);
            if (a == null || b == null) {
                return false;
            }
        } catch (UnknownHostException e) {
            throw new SystemException(e);
        }

        hostA = a.getHostAddress();
        hostB = b.getHostAddress();

        // System.out.println("host_a=" + host_a + ", host_b=" + host_b);
        return hostA.equals(hostB);
    }

    /**
     * Returns the name of the host in which the host name is in the format of
     * <i><hostname>.<lower level domain>.<top level domain></i>.
     * <p>
     * For example,
     * <ul>
     * <li>rmt2.net</li>
     * <li>example.co.uk</li>
     * </ul>
     * 
     * @param hostname
     *            hostname in String format
     * @return String
     */
    public static String shortName(String hostname) {
        int index;
        StringBuffer sb = new StringBuffer();

        if (hostname == null) {
            return null;
        }

        index = hostname.indexOf('.');
        if (index > 0 && !Character.isDigit(hostname.charAt(0))) {
            sb.append(hostname.substring(0, index));
        }
        else {
            sb.append(hostname);
        }
        return sb.toString();
    }

    /**
     * Returns the name of the host in which the host name is in the format of
     * <i><hostname>.<lower level domain>.<top level domain></i>.
     * <p>
     * For example,
     * <ul>
     * <li>rmt2.net</li>
     * <li>example.co.uk</li>
     * </ul>
     * 
     * @param hostname
     *            an instance of {@link InetAddress}
     * @return String
     */
    public static String shortName(InetAddress hostname) {
        if (hostname == null)
            return null;
        StringBuffer sb = new StringBuffer();
        if (RESOLOVE_DNS) {
            sb.append(hostname.getHostName());
        }
        else {
            sb.append(hostname.getHostAddress());
        }
        return sb.toString();
    }

    /**
     * Returns the version of java used.
     * 
     * @return int as RMT2 java version.
     */
    public static int getJavaVersion() {
        String version = System.getProperty("java.version");
        int retval = 0;
        if (version != null) {
            if (version.startsWith("1.2"))
                return 12;
            if (version.startsWith("1.3"))
                return 13;
            if (version.startsWith("1.4"))
                return 14;
            if (version.startsWith("1.5"))
                return 15;
            if (version.startsWith("5"))
                return 15;
            if (version.startsWith("1.6"))
                return 16;
            if (version.startsWith("6"))
                return 16;
            if (version.startsWith("7"))
                return 17;
            if (version.startsWith("8"))
                return 18;
            if (version.startsWith("9"))
                return 19;
            if (version.startsWith("10"))
                return 20;
            if (version.startsWith("11"))
                return 21;
            if (version.startsWith("12"))
                return 22;
            if (version.startsWith("13"))
                return 23;
            if (version.startsWith("14"))
                return 24;
        }
        return retval;
    }

    /**
     * 
     * @param gc
     * @return
     */
    public static String memStats(boolean gc) {
        StringBuffer sb = new StringBuffer();
        Runtime rt = Runtime.getRuntime();
        if (gc) {
            rt.gc();
        }
        long free_mem, total_mem, used_mem;
        free_mem = rt.freeMemory();
        total_mem = rt.totalMemory();
        used_mem = total_mem - free_mem;
        sb.append("Free mem: ").append(free_mem).append("\nUsed mem: ")
                .append(used_mem);
        sb.append("\nTotal mem: ").append(total_mem);
        return sb.toString();
    }

    /**
     * Replaces variables of ${var:default} with System.getProperty(var,
     * default). If no variables are found, returns the same string, otherwise a
     * copy of the string with variables substituted.
     * 
     * @param val
     * @return A string with vars replaced, or the same string if no vars found
     */
    public static String substituteVariable(String val) {
        if (val == null)
            return val;
        String retval = val, prev;

        while (retval.indexOf("${") >= 0) { // handle multiple variables in val
            prev = retval;
            retval = _substituteVar(retval);
            if (retval.equals(prev))
                break;
        }
        return retval;
    }

    /**
     * 
     * @param val
     * @return
     */
    private static String _substituteVar(String val) {
        int start_index, end_index;
        start_index = val.indexOf("${");
        if (start_index == -1)
            return val;
        end_index = val.indexOf("}", start_index + 2);
        if (end_index == -1)
            throw new IllegalArgumentException("missing \"}\" in " + val);

        String tmp = getSystemSubstituteProperty(val.substring(start_index + 2,
                end_index));
        if (tmp == null)
            return val;
        StringBuffer sb = new StringBuffer();
        sb.append(val.substring(0, start_index));
        sb.append(tmp);
        sb.append(val.substring(end_index + 1));
        return sb.toString();
    }

    /**
     * 
     * @param s
     * @return
     */
    private static String getSystemSubstituteProperty(String s) {
        String var, default_val, retval = null;
        int index = s.indexOf(":");
        if (index >= 0) {
            var = s.substring(0, index);
            default_val = s.substring(index + 1);
            retval = System.getProperty(var, default_val);
        }
        else {
            var = s;
            retval = System.getProperty(var);
        }
        return retval;
    }

    /**
     * Create strong passwords.
     * 
     * @param size
     *            the maximum size of the password String
     * @return password
     */
    public static final String generatePassword(int size) {
        String dCase = "abcdefghijklmnopqrstuvwxyz";
        String uCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String sChar = "!@#$%^&*+=?{}[]/\\";
        String intChar = "0123456789";
        Random r = new Random();

        String pass = "";
        while (pass.length() != size) {
            int rPick = r.nextInt(4);
            if (rPick == 0) {
                int spot = r.nextInt(25);
                pass += dCase.charAt(spot);
            }
            else if (rPick == 1) {
                int spot = r.nextInt(25);
                pass += uCase.charAt(spot);
            }
            else if (rPick == 2) {
                int spot = r.nextInt(17);
                pass += sChar.charAt(spot);
            }
            else if (rPick == 3) {
                int spot = r.nextInt(9);
                pass += intChar.charAt(spot);
            }
        }

        return pass;
    }

    /**
     * Validate password with regular expression
     * <p>
     * Regular expression pattern description<br>
     * <blockquote> (?=.*\d) # must contains one digit from 0-9 (?=.*[a-z]) #
     * must contains one lowercase characters (?=.*[A-Z]) # must contains one
     * uppercase characters (?=.*[@#$%!?*]) # must contains one special symbols
     * in the list "@#$%" . # match anything with previous condition checking
     * {8,20} # length at least 8 characters and maximum of 20 </blockquote>
     * 
     * @param password
     *            password for validation
     * @return true valid password, false invalid password
     */
    public static final boolean isStrongPassword(String password) {
        String passwordPattern = "^((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!&^*()-_<>?/\\,:;.])(?=\\S+$).{8,20})$";
        Pattern pattern = Pattern.compile(passwordPattern);
        Matcher matcher = null;
        matcher = pattern.matcher(password);
        return matcher.matches();
    }
}