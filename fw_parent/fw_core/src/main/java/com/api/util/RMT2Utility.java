package com.api.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import com.InvalidDataException;
import com.SystemException;
import com.api.constants.GeneralConst;
import com.api.persistence.DatabaseException;

/**
 * Class contains a collection general purpose utilities.
 * 
 * @author roy.terrell
 * 
 */
public class RMT2Utility {

    protected static String className = "RMT2Utility";

    protected static String methodName = "";

    // /** The name of the system ResourceBundle */
    // public static final String CONFIG_SYSTEM = "SystemConfigParms";

    /**
     * Obtains the value of a datasource's property from the ResultSet. The
     * value is converted and passed back to the caller as a String.
     * 
     * @param _rs
     *            The source of data.
     * @param _property
     *            The name of the column in _rs to target data.
     * @param _type
     *            The data type of _property.
     * @return The value of _property as a String.
     * @throws DatabaseException
     */
    public static final String getPropertyValue(ResultSet _rs,
            String _property, int _type) throws DatabaseException {
        String value = null;

        try {
            switch (_type) {
                case Types.INTEGER:
                case Types.TINYINT:
                case Types.SMALLINT:
                    value = new Integer(_rs.getInt(_property)).toString();
                    break;

                case Types.BIGINT:
                    value = new Long(_rs.getLong(_property)).toString();
                    break;

                case Types.CHAR:
                case Types.VARCHAR:
                case Types.LONGVARCHAR:
                    value = _rs.getString(_property);
                    break;

                case Types.REAL:
                case Types.FLOAT:
                    value = new Float(_rs.getFloat(_property)).toString();
                    break;

                case Types.NUMERIC:
                case Types.DOUBLE:
                case Types.DECIMAL:
                    value = new Double(_rs.getDouble(_property)).toString();
                    break;

                case Types.DATE:
                    value = _rs.getDate(_property).toString();
                    break;

                case Types.TIME:
                    value = _rs.getTime(_property).toString();
                    break;

                case Types.TIMESTAMP:
                    value = _rs.getTimestamp(_property).toString();
                    break;
            } // end switch

            return value;

        } // end try

        catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Obtains the corresponding java data type class name of _type. The value
     * is returned to the caller as a String.
     * 
     * @param _type
     *            The integer representation of the native java class type or
     *            java sql type.
     * @return The class name.
     */
    public static final String getJavaType(int _type) {

        String type = null;

        switch (_type) {
            case Types.INTEGER:
                type = "java.lang.Integer";
                break;
            case Types.TINYINT:
            case Types.SMALLINT:
                type = "java.lang.Short";
                break;

            case Types.BIGINT:
                type = "java.lang.Long";
                break;

            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
                type = "java.lang.String";
                break;

            case Types.REAL:
            case Types.FLOAT:
                type = "java.lang.Float";
                break;

            case Types.NUMERIC:
            case Types.DOUBLE:
            case Types.DECIMAL:
                type = "java.lang.Double";
                break;

            case Types.DATE:
                type = "java.sql.Types.Date";
                break;

            case Types.TIME:
                type = "java.sql.Types.Time";
                break;

            case Types.TIMESTAMP:
                type = "java.sql.Types.Timestamp";
                break;

            default:
                type = "java.lang.Object";
                break;
        } // end switch

        return type;
    }

    /**
     * Obtains the corresponding integer representation of _type. The value is
     * returned to the caller as an integer.
     * 
     * @param _type
     *            The class name of a native java type or java.sql type.
     * @return The class name.
     */
    public static final int getJavaType(String _type) {

        if (_type.equalsIgnoreCase("java.lang.Integer")
                || _type.equalsIgnoreCase("java.sql.Types.INTEGER")
                || _type.equalsIgnoreCase("java.sql.Types.BIGINT")
                || _type.equalsIgnoreCase("java.sql.Types.SMALLINT")
                || _type.equalsIgnoreCase("java.sql.Types.TINYINT")
                || _type.equalsIgnoreCase("integer")
                || _type.equalsIgnoreCase("int"))
            return Types.INTEGER;

        else if (_type.equalsIgnoreCase("java.lang.String")
                || _type.equalsIgnoreCase("java.sql.Types.CHAR")
                || _type.equalsIgnoreCase("java.sql.Types.VARCHAR")
                || _type.equalsIgnoreCase("java.sql.Types.LONGVARCHAR")
                || _type.equalsIgnoreCase("varchar")
                || _type.equalsIgnoreCase("char"))
            return Types.VARCHAR;

        else if (_type.equalsIgnoreCase("java.lang.Float")
                || _type.equalsIgnoreCase("java.sql.Types.FLOAT")
                || _type.equalsIgnoreCase("java.sql.Types.REAL")
                || _type.equalsIgnoreCase("float")
                || _type.equalsIgnoreCase("real"))
            return Types.FLOAT;

        else if (_type.equalsIgnoreCase("java.lang.Double")
                || _type.equalsIgnoreCase("java.sql.Types.DOUBLE")
                || _type.equalsIgnoreCase("java.sql.Types.DECIMAL")
                || _type.equalsIgnoreCase("java.sql.Types.NUMERIC")
                || _type.equalsIgnoreCase("double")
                || _type.equalsIgnoreCase("decimal")
                || _type.equalsIgnoreCase("numeric")
                || _type.equalsIgnoreCase("number"))
            return Types.DOUBLE;

        else if (_type.equalsIgnoreCase("java.util.Date")
                || _type.equalsIgnoreCase("java.sql.Types.DATE")
                || _type.equalsIgnoreCase("date"))
            return Types.DATE;

        else if (_type.equalsIgnoreCase("java.sql.Types.TIME")
                || _type.equalsIgnoreCase("time"))
            return Types.TIME;

        else if (_type.equalsIgnoreCase("java.sql.Types.Timestamp")
                || _type.equalsIgnoreCase("timestamp")
                || _type.equalsIgnoreCase("datetime"))
            return Types.TIMESTAMP;
        else if (_type.equalsIgnoreCase("java.io.InputStream")
                || _type.equalsIgnoreCase("InputStream"))
            return Types.LONGVARBINARY;
        else
            return -1;

    }

    /**
     * Returns the java data type of a particular wrapper object.
     * 
     * @param wrapperObj
     *            The primitive wrapper object.
     * @return int {@link java.sql.Types.* Types}
     */
    public static final int getJavaType(Object wrapperObj) {

        if (wrapperObj instanceof Integer)
            return Types.INTEGER;

        else if (wrapperObj instanceof Short)
            return Types.SMALLINT;

        else if (wrapperObj instanceof String)
            return Types.VARCHAR;

        else if (wrapperObj instanceof Float)
            return Types.FLOAT;

        else if (wrapperObj instanceof Double || wrapperObj instanceof Number)
            return Types.DOUBLE;

        else if (wrapperObj instanceof Date)
            return Types.DATE;

        else if (wrapperObj instanceof Time)
            return Types.TIME;

        else if (wrapperObj instanceof Timestamp)
            return Types.TIMESTAMP;

        else
            return -1;

    }

    /**
     * Accepts a String argument that is in the format of
     * <value1_value2_valuex>, word capitalizes each portion of the String that
     * is separated by an underscore character (value1, value2, and valuex) and
     * removes all underscores. Example: ORDER_DETAIL_TABLE yields
     * OrderDetailTable. This method is handy for converting the names of
     * objects described as metadata into bean names that conform to the java
     * bean specification.
     * 
     * @param entityName
     * @return A Rreformatted entity name conforming to the naming convention
     *         rules of of DataSource view.
     */
    public static final String formatDsName(String entityName) {

        String newValue = "";
        String token = "";

        StringTokenizer str = new StringTokenizer(entityName, "_");
        while (str.hasMoreTokens()) {
            token = str.nextToken();
            newValue += RMT2String.wordCap(token.toLowerCase());
        }
        return newValue;
    }

    /**
     * Formats a string to conform to the method naming conventions of the
     * Javabean specification by converting the first character of the string to
     * upper case.
     * 
     * @param entityName
     *            Source to be converted.
     * @return The method name.
     */
    public static final String getBeanMethodName(String entityName) {

        StringBuffer propName = new StringBuffer(50);

        propName.append(entityName.substring(0, 1).toUpperCase());
        propName.append(entityName.substring(1));

        return propName.toString();
    }

    /**
     * Extracts the class name from the fully qualified package name of _bean.
     * 
     * @param _bean
     *            The object to retreive the root class name.
     * @return The name of the bean, null when _bean is invalid or an empty
     *         string ("") when package name of _bean could not be parsed.
     */
    public static final String getBeanClassName(Object _bean) {
        String temp = null;
        String beanName = null;
        List<String> list = null;

        if (_bean == null) {
            return null;
        }

        temp = _bean.getClass().getName();
        list = RMT2String.getTokens(temp, ".");

        if (list == null || list.size() <= 0) {
            return "";
        }
        beanName = (String) list.get(list.size() - 1);
        return beanName;
    }

    //

    /**
     * Attempts to instantiate a class using _className.
     * 
     * @param _className
     *            The name of the class to instantiate.
     * @return An object of type _className.
     * @throws SystemException
     *             if an initializer error occurs, or linkage fails, or the
     *             class cannot be found, or class is illeaglly accessed, or a
     *             general instantiation problem.
     */
    public static Object getClassInstance(String _className)
            throws SystemException {
        String msg = "Error instantiating class:" + _className + ".  ";
        Object obj = null;

        try {
            obj = Class.forName(_className).newInstance();
            return obj;
        } catch (ExceptionInInitializerError e) {
            msg += " The initialization provoked by this method failed: ";
            throw new SystemException(msg);
        } catch (LinkageError e) {
            msg += " Linkage failed";
            throw new SystemException(msg);
        } catch (ClassNotFoundException e) {
            msg += " The class could not be found";
            throw new SystemException(msg);
        } catch (IllegalAccessException e) {
            msg += " The class or its nullary constructor is not accessible";
            throw new SystemException(msg);
        } catch (InstantiationException e) {
            msg += "Failure due to either 1) class represents an abstract class, an interface, an array class, a primitive type, or void;";
            msg += " 2) the class has no nullary constructor; or  3) for some other reason";
            throw new SystemException(msg);
        }
    }

    /**
     * Restorses a serailized object to some previous state.
     * 
     * @param _source
     *            The file that contains the seriali data needed to be restored
     *            to an object.
     * @return the restored object
     * @throws SystemException
     */
    public static Object deSerializeObject(String _source)
            throws SystemException {
        Object obj = null;
        String msg = "File deserialization failed for file " + _source + ": ";
        String filename = RMT2File.getSerialLocation(_source);

        try {
            // Read from disk using FileInputStream.
            FileInputStream f_in = new FileInputStream(filename);

            // Read object using ObjectInputStream.
            ObjectInputStream obj_in = new ObjectInputStream(f_in);

            // Read an object.
            obj = obj_in.readObject();
            return obj;
        } catch (FileNotFoundException e) {
            throw new SystemException(msg + " File was not found");
        } catch (ClassNotFoundException e) {
            throw new SystemException(msg + " Class was not found");
        } catch (IOException e) {
            throw new SystemException(msg + " General file IO error occurred");
        }
    }

    /**
     * Obtains the stack trace pertaing to <b>e</b> and converts and
     * concatenates each StackTraceElement into a String.
     * 
     * @param e
     *            The Exception object to obtai stack trace from.
     * @return Stack trace as a String.
     */
    public static final String getStackTrace(Throwable e) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        e.printStackTrace(ps);
        String results = baos.toString();
        return results;
    }

    /**
     * Creates a List of messages from the stack trace of a Throwable instance,
     * <b>e</b>.
     * 
     * @param e
     * @return a List of Strings represnting each message in the stack trace.
     */
    public static final List getStackTraceMessages(Throwable e) {
        List<String> errors = new ArrayList<String>();
        StackTraceElement ste[] = e.getStackTrace();
        for (int ndx = 0; ndx < ste.length; ndx++) {
            errors.add(ste[ndx].toString());
        }
        return errors;
    }

    /**
     * Verifies that _obj is a valid primitive wrapper data type.
     * 
     * @param _obj
     *            Object to verify
     * @return true data type is one othe primitvie wrappers. false when
     *         incorrect data type.
     */
    public static boolean isWrapperType(Object _obj) {
        return (_obj instanceof Date || _obj instanceof String
                || _obj instanceof Integer || _obj instanceof Float
                || _obj instanceof Double || _obj instanceof Number || _obj instanceof Short);
    }

    /**
     * Verifies that _obj is an array
     * 
     * @param _obj
     *            Object to verify
     * @return true if object is an array. Otherwise, false is returned.
     */
    public static boolean isArray(Object _obj) {
        try {
            java.lang.reflect.Array.getLength(_obj);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Breaks up the equality statements of the specified selection criteria
     * into a Map of key/value pairs. For example, "AccountNo=9439392" would be
     * added to a Map collection as key/value pairs where AccountNo would be the
     * key and 9439392 would be the value. This would apply to all equality
     * statements of the selection criteria.
     * 
     * @param criteria
     *            The selection criteria to parse.
     * @return Map
     */
    public static Map<String, String> convertCriteriaToHash(String criteria) {
        criteria = RMT2String.replaceAll(criteria, "*", " and ");
        List tokens = RMT2String.getTokens(criteria, "*");
        Map<String, String> hash = new HashMap<String, String>();
        if (tokens != null) {
            for (int ndx = 0; ndx < tokens.size(); ndx++) {
                String element = (String) tokens.get(ndx);
                List tokens2 = RMT2Utility.separateCriteriaOperands(element);
                if (tokens2 == null) {
                    continue;
                }
                String key = (String) tokens2.get(0);
                String value = (String) tokens2.get(1);
                hash.put(key.trim(), value.trim());
            }
        }
        return hash;
    }

    /**
     * Parses a SQL predicate clause into two distince halfs separated by the
     * operator. The supported operators are =, like, >, <, <=, >=, <>, is null,
     * and is not null.
     * 
     * @param clause
     *            The SQL predicate.
     * @return List of disjuncted predicates elements or null if <i>clause</i>
     *         contains an operator that is not supported.
     */
    private static List separateCriteriaOperands(String clause) {
        List tokens = null;
        String temp;

        tokens = RMT2String.getTokens(clause, "=");
        if (tokens != null && tokens.size() > 1) {
            return tokens;
        }
        temp = RMT2String.replaceAll(clause, "*", " like ");
        tokens = RMT2String.getTokens(temp, "*");
        if (tokens != null && tokens.size() > 1) {
            return tokens;
        }
        tokens = RMT2String.getTokens(clause, "<");
        if (tokens != null && tokens.size() > 1) {
            return tokens;
        }
        tokens = RMT2String.getTokens(clause, ">");
        if (tokens != null && tokens.size() > 1) {
            return tokens;
        }
        tokens = RMT2String.getTokens(clause, "<=");
        if (tokens != null && tokens.size() > 1) {
            return tokens;
        }
        tokens = RMT2String.getTokens(clause, ">=");
        if (tokens != null && tokens.size() > 1) {
            return tokens;
        }
        tokens = RMT2String.getTokens(clause, "<>");
        if (tokens != null && tokens.size() > 1) {
            return tokens;
        }
        temp = RMT2String.replaceAll(clause, "*", " is null ");
        tokens = RMT2String.getTokens(temp, "*");
        if (tokens != null && tokens.size() > 1) {
            return tokens;
        }
        temp = RMT2String.replaceAll(clause, "*", " is not null ");
        tokens = RMT2String.getTokens(temp, "*");
        if (tokens != null && tokens.size() > 1) {
            return tokens;
        }

        // Return null if operator is not found
        return null;
    }

    /**
     * Removes all special characters from a String phone number and identifies
     * the phone extension, if available. First, the base phone number is parsed
     * by removing the following characters in the order specified: <i>(</i>,
     * <i>)</i>, <i>-</i>, and all spaces. Lastly, the phone extension is
     * parsed, if available, searching for the following values in the specified
     * order: <i>ext</i>, <i>Ext</i>, <i>x</i>, and <i>X</i>.
     * 
     * @param phoneNo
     *            the phone number to be evaluated
     * @return String a List of Strings which the first element is the phone
     *         number without any special characters and spaces, and the second
     *         element is the phone extension, if available.
     */
    public static List cleanUpPhoneNo(String phoneNo) {
        // parse base phone number.
        String value = RMT2String.replace(phoneNo, "", "(");
        value = RMT2String.replace(value, "", ")");
        value = RMT2String.replace(value, "", "-");
        value = RMT2String.replaceAll(value, "", " ");
        List<String> tokens = null;
        if (value != null && !value.equals("")) {
            tokens = new ArrayList<String>();
            tokens.add(value.trim());
        }

        // Try to parse phone extension, if available.
        List<String> ext = null;
        if (value.indexOf("ext") >= 0) {
            ext = RMT2String.getTokens(value, "ext");
        }
        else if (value.indexOf("Ext") >= 0) {
            ext = RMT2String.getTokens(value, "Ext");
        }
        else if (value.indexOf("x") >= 0) {
            ext = RMT2String.getTokens(value, "x");
        }
        else if (value.indexOf("X") >= 0) {
            ext = RMT2String.getTokens(value, "X");
        }

        if (ext != null && ext.size() == 2) {
            return ext;
        }
        return tokens;
    }

    public static String execShellCommand(String command) {
        Process process = null;
        StringBuffer inMsg = new StringBuffer();
        StringBuffer errMsg = new StringBuffer();
        try {
            process = Runtime.getRuntime().exec(command);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(
                    process.getErrorStream()));
            if (!stdInput.ready()) {
                System.out.println("Command Shell Input buffer is empty");
                return null;
            }
            String s = null;
            while ((s = stdInput.readLine()) != null) {
                inMsg.append(s);
            }
            if (!stdError.ready()) {
                System.out.println("Command Shell Error buffer is empty");
                return null;
            }
            while ((s = stdError.readLine()) != null) {
                errMsg.append(s);
            }
            if (errMsg.length() > 0) {
                inMsg.append("\nError: ");
                inMsg.append(errMsg);
            }
            return inMsg.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Recursively traverses all nested Throwable causes of <i>e</i> until the
     * Throwable.getMessage() method returns an error message.
     * 
     * @param e
     * @return String the error message.
     */
    public static String getRootErrorMessage(Throwable e) {
        final String unwantedMsg = "Original Exception runtime type is unknown";
        String msg = e.getMessage();
        if (msg == null || msg.trim().equalsIgnoreCase(unwantedMsg)) {
            Throwable prev = e.getCause();
            if (prev != null) {
                msg = RMT2Utility.getRootErrorMessage(prev);
            }
        }
        return msg;
    }

    /**
     * Converts an IPv4 IP address to its integer equivalent.
     * 
     * @param ipAddr
     *            The IP address represented in IPv4 format
     * @return a long representation of the IPv4 address
     * @throws InvalidDataException <i>ipAddr</i> is null or invalid
     */
    public static long convertIp(String ipAddr) throws InvalidDataException {
        if (ipAddr == null) {
            throw new InvalidDataException("IP address cannot be null");
        }
        String msg = null;
        String ipDiv[] = ipAddr.split("\\.");
        if (ipDiv.length != 4) {
            msg = "IP address is invalid...must contain four octets: " + ipAddr;
            throw new InvalidDataException(msg);
        }

        long oct1 = 0;
        long oct2 = 0;
        long oct3 = 0;
        long oct4 = 0;
        try {
            oct1 = Integer.parseInt(ipDiv[0]);
            oct2 = Integer.parseInt(ipDiv[1]);
            oct3 = Integer.parseInt(ipDiv[2]);
            oct4 = Integer.parseInt(ipDiv[3]);
        } catch (NumberFormatException e) {
            msg = "IP address contains an invalid octect: " + ipAddr;
            throw new InvalidDataException(msg);
        }

        // long ip = ((oct1 * 256 + oct2) * 256 + oct3) * 256 + oct4;
        long ip = (16777216 * oct1) + (65536 * oct2) + (256 * oct3) + oct4;
        return ip;
    }

    /**
     * Converts an IP integer to an IPv4 representation.
     * 
     * @param ipInt
     *            The long value of the IPv4 address.
     * @return IPv4 representation of the integer IP address.
     */
    public static String convertIp(long ipInt) {
        long a, b, c, d;
        a = (ipInt / 16777216) % 256;
        b = (ipInt / 65536) % 256;
        c = (ipInt / 256) % 256;
        d = (ipInt) % 256;

        // a = (netAddr & 0xff000000) >> 24;
        // b = (netAddr & 0x00ff0000) >> 16;
        // c = (netAddr & 0x0000ff00) >> 8;
        // d = (netAddr & 0x000000ff);
        return a + "." + b + "." + c + "." + d;
    }

    /**
     * 
     * @param array
     * @return
     */
    public static String array2String(long[] array) {
        StringBuffer ret = new StringBuffer("[");

        if (array != null) {
            for (int i = 0; i < array.length; i++)
                ret.append(array[i]).append(" ");
        }

        ret.append(']');
        return ret.toString();
    }

    /**
     * 
     * @param array
     * @return
     */
    public static String array2String(int[] array) {
        StringBuffer ret = new StringBuffer("[");

        if (array != null) {
            for (int i = 0; i < array.length; i++)
                ret.append(array[i]).append(" ");
        }

        ret.append(']');
        return ret.toString();
    }

    /**
     * 
     * @param array
     * @return
     */
    public static String array2String(boolean[] array) {
        StringBuffer ret = new StringBuffer("[");

        if (array != null) {
            for (int i = 0; i < array.length; i++)
                ret.append(array[i]).append(" ");
        }
        ret.append(']');
        return ret.toString();
    }

    /**
     * 
     * @param array
     * @return
     */
    public static String array2String(Object[] array) {
        StringBuffer ret = new StringBuffer("[");

        if (array != null) {
            for (int i = 0; i < array.length; i++)
                ret.append(array[i]).append(" ");
        }
        ret.append(']');
        return ret.toString();
    }

    /**
     * Tries to load the class from the current thread's context class loader.
     * If not successful, tries to load the class from the current instance.
     * 
     * @param classname
     *            Desired class.
     * @param clazz
     *            Class object used to obtain a class loader if no context class
     *            loader is available.
     * @return Class, or null on failure.
     */
    public static Class loadClass(String classname, Class clazz)
            throws ClassNotFoundException {
        ClassLoader loader;

        try {
            loader = Thread.currentThread().getContextClassLoader();
            if (loader != null) {
                return loader.loadClass(classname);
            }
        } catch (Throwable t) {
        }

        if (clazz != null) {
            try {
                loader = clazz.getClassLoader();
                if (loader != null) {
                    return loader.loadClass(classname);
                }
            } catch (Throwable t) {
            }
        }

        try {
            loader = ClassLoader.getSystemClassLoader();
            if (loader != null) {
                return loader.loadClass(classname);
            }
        } catch (Throwable t) {
        }

        throw new ClassNotFoundException(classname);
    }

    /**
     * Retrieves the classpath of the current JVM as a List of Strings.
     * 
     * @return
     */
    public static final List<String> getClassPath() {
        List<String> list = new ArrayList<String>();
        // Get the System Classloader
        ClassLoader sysClassLoader = ClassLoader.getSystemClassLoader();

        // Get the URLs
        URL[] urls = ((URLClassLoader) sysClassLoader).getURLs();

        for (int ndx = 0; ndx < urls.length; ndx++) {
            list.add(urls[ndx].getFile());
        }
        return list;
    }

    /**
     * Fetches the all of the XML namespace configurations into a ResourceBundle
     * instance.
     * 
     * @return ResourceBundle a hash collection containing namespace URI's keyed
     *         by the namespace URI prefix.
     * @throws SystemException
     *             a problem occurred accessing or loading the ResourceBundle
     */
    public static ResourceBundle getXmlNamespaces() throws SystemException {
        if (RMT2Utility.NAMESPACES == null) {
            // I changed some of the namespaces in
            // "com.api.xml.RMT2NamespaceContext" so when
            // searching for SOAP header namespaces returns null.
            RMT2Utility.NAMESPACES = RMT2File
                    .loadAppConfigProperties(GeneralConst.NAMESPACES_RESOURCES);
        }
        return RMT2Utility.NAMESPACES;
    }

    public static ResourceBundle NAMESPACES;

} // end class

