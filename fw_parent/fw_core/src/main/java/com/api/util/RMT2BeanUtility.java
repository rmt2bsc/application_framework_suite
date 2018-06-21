package com.api.util;

import java.beans.BeanInfo;
import java.beans.Beans;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import com.NotFoundException;
import com.SystemException;
import com.api.constants.RMT2SystemExceptionConst;

/**
 * This class provides the ability to create beans, dynamically access bean
 * properties, and invoke bean methods using introspection.
 * 
 * @author roy.terrell
 * 
 */
public class RMT2BeanUtility {
    private Object bean;

    private Class classInst;

    private BeanInfo info;

    private BeanInfo addInfo[];

    private PropertyDescriptor pd[];

    private MethodDescriptor md[];

    private Hashtable methods;

    private String currentProperty = null;

    private String msg;

    /**
     * Default constructor
     * 
     */
    public RMT2BeanUtility() {
        return;
    }

    /**
     * Constructs a RMT2BeanUtility using a bean.
     * 
     * @param _bean
     *            The bean that is to be manipulated.
     * @throws SystemException
     */
    public RMT2BeanUtility(Object _bean) throws SystemException {
        this();
        this.bean = _bean;
        this.init();
        this.setupBeanHashtables();
    }

    /**
     * Initializes the class instance, BeanInfo class, and PropertyDescriptor
     * arrary.
     * 
     * @throws SystemException
     */
    private void init() throws SystemException {
        try {
            this.classInst = bean.getClass();
            this.info = Introspector.getBeanInfo(this.classInst);
            this.addInfo = this.info.getAdditionalBeanInfo();
            this.pd = info.getPropertyDescriptors();
            this.md = info.getMethodDescriptors();
        } catch (IntrospectionException e) {
            String msg = (e.getMessage() == null ? "" : e.getMessage());
            throw new SystemException("Introspection Exception occurred.  "
                    + msg, e);
        }
    }

    /**
     * Poopulates the setMethods and getMethods Hashtables.
     * 
     * @return Total number of properties found and zero if no proerties exist.
     * @throws SystemException
     */
    private int setupBeanHashtables() throws SystemException {
        String propName = null;

        // There is no need to process any further if no properties exist
        if (this.pd == null) {
            return 0;
        }

        // Initialized methods Hashtable
        methods = new Hashtable();

        // Retrieve the each property and its get/set methods
        for (int ndx = 0; ndx < pd.length; ndx++) {
            // Ensure that the property name conforms to the naming
            // convention spceifiec by Javabean Introspection
            propName = RMT2Utility.getBeanMethodName(pd[ndx].getName());

            // Get the getter and setter methods associated with the property
            Method getMethod = pd[ndx].getReadMethod();
            Method setMethod = pd[ndx].getWriteMethod();

            // Add methods to the Hastables with the property name being the key
            if (getMethod != null) {
                this.methods.put("get" + propName, getMethod);
            }
            if (setMethod != null) {
                this.methods.put("set" + propName, setMethod);
            }
        }
        return this.methods.size();
    }

    /**
     * Determines if methodName exists as a method for this object
     * 
     * @param methodName
     *            The name of the method to search
     * @return true for found and false for not found.
     */
    public boolean methodExist(String methodName) {
        // There is no need to process any further if no properties exist
        if (this.md == null) {
            return false;
        }

        // Retrieve the each property and its get/set methods
        for (int ndx = 0; ndx < md.length; ndx++) {
            // Get the getter and setter methods associated with the property
            Method method = md[ndx].getMethod();

            // Add methods to the Hastables with the property name being the key
            if (method != null) {
                // System.out.println("Method name: " + method.getName());
                if (method.getName().equals(methodName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Querys the property descriptor array (pd) for the property's class type.
     * 
     * @param _propName
     *            Property name.
     * @return The Class of _propertyName or null if property is not found.
     */
    public Class getPropertyType(String _propName) {
        for (int ndx = 0; ndx < pd.length; ndx++) {
            if (_propName.equalsIgnoreCase(pd[ndx].getName())) {
                return pd[ndx].getPropertyType();
            }
        }
        return null;
    }

    /**
     * Packages all the property names of "bean" into an ArrayList and returns
     * the ArrayList to the caller.
     * 
     * @return An List of String representing the property names.
     */
    public List<String> getPropertyNames() {

        ArrayList<String> names = new ArrayList<String>();
        for (int ndx = 0; ndx < pd.length; ndx++) {
            names.add(pd[ndx].getName());
        }
        return names;
    }

    /**
     * Obtains the value of the targeted property, "_property".
     * 
     * @param _property
     *            The property to retreive value.
     * @return Object (value of the property retrieved)
     * @throws NotFoundException
     * @throws SystemException
     */
    public Object getPropertyValue(String _property) throws NotFoundException,
            SystemException {
        String nameOfMethod = null;
        Object value = null;
        Object parms[] = new Object[0];
        Method methodObj = null;

        if (_property == null || _property.length() <= 0) {
            this.msg = RMT2SystemExceptionConst.MSG_NULL_METHOD_ARGUMENT + ": "
                    + _property;
            throw new SystemException(msg,
                    RMT2SystemExceptionConst.RC_NULL_METHOD_ARGUMENT);
        }

        this.currentProperty = _property;

        // Build the method name that is to be searched.
        nameOfMethod = "get" + RMT2Utility.getBeanMethodName(_property);
        // Attempt to locate the method from the hashtable
        methodObj = (Method) this.methods.get(nameOfMethod);

        // throw an exception if the method does not exist
        if (methodObj == null) {
            // this.msg = "Method, "
            // + nameOfMethod
            // +
            // ", could not be obtained since it was not mapped to any value in the methods Hashtable";
            // throw new NotFoundException(this.msg, -1);
            return null;
        }

        // Invoke the method
        try {
            value = this.triggerMethod(methodObj, parms);
        } catch (Exception e) {
            this.msg = "Access error occurred trying to discover and invoke method, "
                    + nameOfMethod + ", for property, " + _property;
            throw new NotFoundException(this.msg, e);
        }

        return value == null ? "" : value;
    }

    /**
     * Sets the property named as, "_property", to the value represented by,
     * "_value".
     * 
     * @param _property
     *            The target property
     * @param _value
     *            The value to set.
     * @return The of the setXXX method invocation which is generally null.
     * @throws NotFoundException
     * @throws SystemException
     */
    public Object setPropertyValue(String _property, Object _value)
            throws NotFoundException, SystemException {
        Object parms[] = new Object[1];
        Object objProper;
        String nameOfMethod = null;
        Method methodObj = null;
        String msg = null;

        if (_property == null || _property.length() <= 0) {
            throw new SystemException("Property cannot be null",
                    RMT2SystemExceptionConst.RC_NULL_METHOD_ARGUMENT);
        }

        this.currentProperty = _property;

        // Get the data type of the target property.
        Class propType = this.getPropertyType(_property);

        // Exit method and return null to the caller if property does not exist
        if (propType == null) {
            return null;
        }

        // Translate propType to its primitive wrapper class
        objProper = getPrimitiveType(propType, _value);

        // Value was returned as null because property value was actually null
        // or property is of a complex datatype. Assign object as-is.
        if (objProper == null) {
            objProper = _value;
            // System.out.println("RMT2BeanUtility, setPropertyValue(). Caution!
            // Property was null: " + _property);
        }

        // Build method name that is to be searched.
        nameOfMethod = "set" + RMT2Utility.getBeanMethodName(_property);

        // Locate the method from the method hashtable
        methodObj = (Method) this.methods.get(nameOfMethod);

        // Throw an exception if the method does not exist
        if (methodObj == null) {
            // msg = "Method, "
            // + nameOfMethod
            // +
            // ", could not be obtained since it was not mapped to any value in the methods Hashtable";
            // throw new NotFoundException(msg, -1);
            return null;
        }

        // Set the parameter value
        parms[0] = objProper;

        // invoke method
        try {
            return this.triggerMethod(methodObj, parms);
        } catch (Exception e) {
            throw new NotFoundException(msg, e);
        }
    }

    /**
     * Initializes all primitive properties of the target java bean. Sets
     * numbers to zero, boolean to false, Character and String to "". This
     * method was implemented for the purpose of converting null values to empty
     * string values of String properties of a java bean initially containig
     * null values. This prevents the client JSP from displaying the String,
     * "null", for those string properties of a bean that contain null values.
     * 
     * @return The initialized bean.
     */
    public Object initializeBean() {
        Object parms[] = new Object[1];
        // Object objProper;
        String nameOfMethod = null;
        String property = null;
        Method methodObj = null;
        String msg = null;
        List<String> props = null;

        // Retrieve all property names associated with _bean
        props = this.getPropertyNames();

        for (int ndx = 0; ndx < props.size(); ndx++) {
            try {
                // Get and convert the property name to proper Bean
                // specification casing
                property = RMT2Utility.getBeanMethodName(props.get(ndx)
                        .toString());

                // Get the data type of the target property.
                Class propType = this.getPropertyType(property);
                if (propType == null) {
                    continue;
                }

                // We want to initialize on those properties that are primitive
                // types (numbers and Strings)
                if (propType.getName().equalsIgnoreCase("boolean"))
                    parms[0] = new Boolean("false");
                else if (propType.getName().equalsIgnoreCase("byte"))
                    parms[0] = new Byte("0");
                else if (propType.getName().equalsIgnoreCase("char"))
                    parms[0] = new Character(' ');
                else if (propType.getName().equalsIgnoreCase("double"))
                    parms[0] = new Double("0");
                else if (propType.getName().equalsIgnoreCase("float"))
                    parms[0] = new Float("0");
                else if (propType.getName().equalsIgnoreCase("int"))
                    parms[0] = new Integer("0");
                else if (propType.getName().equalsIgnoreCase("long"))
                    parms[0] = new Long("0");
                else if (propType.getName().equalsIgnoreCase("short"))
                    parms[0] = new Short("0");
                else if (propType.getName()
                        .equalsIgnoreCase("java.lang.String"))
                    parms[0] = "";

                // Build method name that is to be searched.
                nameOfMethod = "set" + RMT2Utility.getBeanMethodName(property);

                // Locate the method from the method hashtable
                methodObj = (Method) this.methods.get(nameOfMethod);

                // Throw an exception if the method does not exist
                if (methodObj == null) {
                    msg = "Method, "
                            + nameOfMethod
                            + ", could not be obtained since it was not mapped to any value in the methods Hashtable";
                    throw new NotFoundException(msg, -1);
                }

                // invoke method
                this.triggerMethod(methodObj, parms);
            } // end try
            catch (NotFoundException e) {
                continue;
            } catch (NumberFormatException e) {
                continue;
            } catch (SystemException e) {
                continue;
            }
        } // end for

        return this.bean;
    }

    /**
     * Assigns "_value" to is proper wrapper class.
     * 
     * @param obj
     *            The Class Object to determine data type.
     * @param _value
     *            The value to wrap
     * @return The value assigned to the proper primitive wrapper class. Returns
     *         null if Class object is not of the primitive types or if
     *         <i>_value</i> is null.
     * @throws SystemException
     */
    private Object getPrimitiveType(Class obj, Object _value)
            throws SystemException {
        String _className = obj.getName();
        if (_value == null) {
            return null;
        }
        String badValue = _value.toString();
        this.msg = "Property: " + this.currentProperty
                + "  Value is not of the correct number format:  " + badValue == null ? "<value not interpreted"
                : badValue;

        try {
            if (_className.equalsIgnoreCase("boolean"))
                return new Boolean(_value.toString());
            else if (_className.equalsIgnoreCase("byte"))
                return new Byte(_value.toString());
            else if (_className.equalsIgnoreCase("char"))
                return new Character(_value.toString().charAt(0));
            else if (_className.equalsIgnoreCase("double"))
                return new Double(_value.toString());
            else if (_className.equalsIgnoreCase("float"))
                return new Float(_value.toString());
            else if (_className.equalsIgnoreCase("int"))
                return new Integer(_value.toString());
            else if (_className.equalsIgnoreCase("long"))
                return new Long(_value.toString());
            else if (_className.equalsIgnoreCase("short"))
                return new Short(_value.toString());
            else if (_className.equalsIgnoreCase("java.lang.String"))
                return _value;
            else if (_className.equalsIgnoreCase("java.util.Date"))
                return RMT2Date.stringToDate(_value.toString());
            else if (_className.equalsIgnoreCase("java.io.InputStream")) {
                return _value;
            }

            return null;
        } catch (NumberFormatException e) {
            // return the proper number wrapper class provided that
            // the NumberFormatException was thrown because the value
            // was either null or spaces.

            // Try to parse numeric value to its raw number representation
            // and include that raw number in its proper wrapper class.

            if (_value != null && _value.toString().trim().length() > 0) {
                if (_className.equalsIgnoreCase("double")) {
                    double dValue = RMT2Money.stringToNumber(_value.toString())
                            .doubleValue();
                    return new Double(dValue);
                }
                else if (_className.equalsIgnoreCase("float")) {
                    float fValue = RMT2Money.stringToNumber(_value.toString())
                            .floatValue();
                    return new Float(fValue);
                }
                else if (_className.equalsIgnoreCase("int")) {
                    int iValue = RMT2Money.stringToNumber(_value.toString())
                            .intValue();
                    return new Integer(iValue);
                }
                else if (_className.equalsIgnoreCase("long")) {
                    long lValue = RMT2Money.stringToNumber(_value.toString())
                            .longValue();
                    return new Long(lValue);
                }
                else if (_className.equalsIgnoreCase("short")) {
                    short sValue = RMT2Money.stringToNumber(_value.toString())
                            .shortValue();
                    return new Short(sValue);
                }
            }

            // Default appropiate wrapper class to 0
            if (_value == null || _value.toString().trim().equals("")) {
                if (_className.equalsIgnoreCase("double"))
                    return new Double(0);
                else if (_className.equalsIgnoreCase("float"))
                    return new Float(0);
                else if (_className.equalsIgnoreCase("int"))
                    return new Integer(0);
                else if (_className.equalsIgnoreCase("long"))
                    return new Long(0);
                else if (_className.equalsIgnoreCase("short"))
                    return new Short("0");
            }

            // otherwise, continue to throw exception
            throw new SystemException(this.msg, -2040);
        }
    }

    /**
     * Checks if <i>obj</i> is of a primitive data type.
     * 
     * @param obj
     *            The object to check data type.
     * @return true for primitive and false for complex object types.
     * @throws SystemException
     */
    public boolean isPrimitiveType(Class obj) throws SystemException {
        String clazzName = obj.getName();
        if (clazzName.equalsIgnoreCase("boolean"))
            return true;
        else if (clazzName.equalsIgnoreCase("byte"))
            return true;
        else if (clazzName.equalsIgnoreCase("char"))
            return true;
        else if (clazzName.equalsIgnoreCase("double"))
            return true;
        else if (clazzName.equalsIgnoreCase("float"))
            return true;
        else if (clazzName.equalsIgnoreCase("int"))
            return true;
        else if (clazzName.equalsIgnoreCase("long"))
            return true;
        else if (clazzName.equalsIgnoreCase("short"))
            return true;
        else if (clazzName.equalsIgnoreCase("java.lang.String"))
            return true;
        else if (clazzName.equalsIgnoreCase("java.util.Date"))
            return true;
        else if (clazzName.equalsIgnoreCase("java.lang.Double"))
            return true;
        else if (clazzName.equalsIgnoreCase("java.lang.Integer"))
            return true;
        else if (clazzName.equalsIgnoreCase("java.lang.Float"))
            return true;
        else if (clazzName.equalsIgnoreCase("java.lang.Character"))
            return true;
        else if (clazzName.equalsIgnoreCase("java.lang.Boolean"))
            return true;
        else if (clazzName.equalsIgnoreCase("java.lang.Byte"))
            return true;

        return false;
    }

    /**
     * Attempts to invoke the method, obj, dynamically.
     * 
     * @param obj
     *            Method to dym=namically invoke
     * @param _parms
     *            The method's parmaeters
     * @return The results of the method invocation.
     * @throws SystemException
     */
    public Object triggerMethod(Method obj, Object _parms[])
            throws SystemException {
        try {
            return obj.invoke(this.bean, _parms);
        } catch (IllegalAccessException e) {
            msg = "The underlying method is inaccessible: " + obj.getName();
            throw new SystemException(msg, e);
        } catch (IllegalArgumentException e) {
            msg = "The number of actual and formal parameters differ, or an unwrapping conversion failed for method, "
                    + obj.getName();
            throw new SystemException(msg, e);
        } catch (InvocationTargetException e) {
            msg = "The underlying method throws an exception: " + obj.getName();
            throw new SystemException(msg, e);
        } catch (NullPointerException e) {
            msg = "The specified object representing the method that is to be dynamically invoked is null.";
            throw new SystemException(msg, e);
        } catch (ExceptionInInitializerError e) {
            msg = "The initialization provoked by this method failed: "
                    + obj.getName();
            throw new SystemException(msg, e);
        }
    }

    /**
     * Creates an instance of the underlying bean, this.bean.
     * 
     * @return Class instance.
     * @throws SystemException
     */
    public Object createInstance() throws SystemException {
        String _className = this.bean.getClass().getName();
        return this.createClassInstance(_className);
    }

    /**
     * Creates an instance of a class name.
     * 
     * @param _className
     *            The name of the class to create instance.
     * @return Class instance.
     * @throws SystemException
     */
    private Object createClassInstance(String _className)
            throws SystemException {
        String msg = null;
        try {
            return Beans.instantiate(null, _className);
        } catch (ClassNotFoundException e) {
            msg = "The the class, " + _className
                    + ", of a serialized object could not be found";
            throw new SystemException(msg, e);
        } catch (IOException e) {
            msg = "an I/O error occured while attempting to create an instance of, "
                    + _className;
            throw new SystemException(msg, e);
        }
    }

    /**
     * Creates an instance of _className using the class loader of an instance
     * of RMT2BeanUtility class and returns that instance to the caller.
     * 
     * @param className
     *            The name of the class to create instance.
     * @return An instance of <i>className</i>
     * @throws SystemException
     *             General IO error or class is not found.
     */
    public final Object createBean(String className) throws SystemException {
        try {
            Object beanObj = null;
            beanObj = Beans.instantiate(this.getClass().getClassLoader(),
                    className);
            return beanObj;
        } catch (IOException e) {
            this.msg = "An I/O error occurred while attempting to create a bean from RMT2Utility.    Class in violation: "
                    + className;
            throw new SystemException(this.msg, e);
        } catch (ClassNotFoundException e) {
            this.msg = "The class of a serialized object could not be found while attempting to create a bean from RMT2Utility.  Class in violation: "
                    + className;
            throw new SystemException(this.msg, e);
        }
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
    public String formatToDataSourceName(String entityName) {
        return RMT2Utility.formatDsName(entityName);
    }

    /**
     * Extracts the root class name from the fully qualified package name of
     * _bean.
     * 
     * @param obj
     *            The object to retreive the root class name.
     * @return The name of the bean, null when _bean is invalid or an empty
     *         string ("") when package name of _bean could not be parsed.
     */
    public String getRootClassName(Object obj) {
        return RMT2Utility.getBeanClassName(obj);
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
    public String formatToMethodName(String entityName) {
        return RMT2Utility.getBeanMethodName(entityName);
    }

    /**
     * Returns the underlying bean instance.
     * 
     * @return Object
     */
    public Object getBeanInstance() {
        return bean;
    }

    public Properties toProperties() {
        List<String> propNames = null;
        Properties props = new Properties();

        // Retrieve all property names associated with _bean
        propNames = this.getPropertyNames();

        for (String propName : propNames) {
            try {
                // Get and convert the property name to proper Bean
                // specification casing
                propName = RMT2Utility.getBeanMethodName(propName);

                // Get the data type of the target property.
                Class propType = this.getPropertyType(propName);
                if (propType == null) {
                    continue;
                }

                Object value = null;
                if (this.isPrimitiveType(propType)) {
                    value = this.getPropertyValue(propName);
                    if (value != null && !value.equals("")) {
                        if (propType.getName().equalsIgnoreCase(
                                "java.util.Date")) {
                            props.setProperty(propName, RMT2Date.formatDate(
                                    (Date) value, "MM/dd/yyyy HH:mm:ss"));
                        }
                        else {
                            props.setProperty(propName, value.toString());
                        }
                    }
                }
                if (value != null) {
                    // TODO: Handle nested objects
                }
            } // end try
            catch (NotFoundException e) {
                continue;
            } catch (NumberFormatException e) {
                continue;
            } catch (SystemException e) {
                continue;
            }
        } // end for

        return props;
    }

} // end class
