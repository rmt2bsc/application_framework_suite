package com.api.xml.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.NotFoundException;
import com.RMT2Base;
import com.SystemException;
import com.api.DaoApi;
import com.api.persistence.DatabaseException;
import com.api.util.RMT2BeanUtility;
import com.api.util.RMT2Utility;
import com.api.xml.XmlApiFactory;
import com.api.xml.XmlDao;

/**
 * RMT2 Business System Corp's own implementation of XmlBeanAdapter interface
 * without the usage of any third party class API's.
 * 
 * @author appdev
 * 
 */
class NativeAdapterImpl extends RMT2Base implements XmlBeanAdapter {
    private static final String BEANCLASSNAME = "beanClassName";

    private Logger logger;

    /**
     * Default Constructor which simply creates a NativeAdapterImpl object.
     */
    public NativeAdapterImpl() {
        super();
        this.logger = Logger.getLogger("NativeAdapterImpl");
        return;
    }

    /**
     * Converts <i>bean</i> to a XML Element object. Returns the String
     * representation of <i>bean</i> as a XML document. The following javabeans
     * are supported: primitive data types, non-native complex javabeans, List,
     * Map, and Set objects. When <i>bean</i> is of type List, Set, or Map, the
     * outer element will be identified as <i>Collection</i>. Otherwise, the
     * outer element's name is that of the target class.
     * 
     * @param bean
     *            The target javabean.
     * @return String representation as a XML document.
     * @throws SystemException
     */
    public String beanToXml(Object bean) throws SystemException {
        if (bean == null) {
            return null;
        }

        Element root = null;
        if (bean instanceof List || bean instanceof Map || bean instanceof Set) {
            root = buildComplexBeanElement("Collection", bean);
        }
        else {
            root = this.buildRootElement(bean);
        }
        if (root == null) {
            return null;
        }

        // Output document as a String
        Document doc = new Document(root);
        XMLOutputter serializer = new XMLOutputter(Format.getCompactFormat()
                .setOmitDeclaration(true));
        String xml = serializer.outputString(doc);

        return xml;
    }

    /**
     * This is the starting point where <i>bean</i> is converted to an XML
     * Element object. First, a root element is created using the class name of
     * <i>bean</i>. Lastly, each property of <i>bean</i> is then analyzed and
     * eventually converted to an XML element. The class name of <i>bean</i>
     * will be used as the enclosing element to all properties of <i>bean</i>
     * that are converted to XML elements.
     * 
     * @param bean
     *            The target bean.
     * @return org.jdom.Element
     * @throws SystemException
     */
    private Element buildRootElement(Object bean) throws SystemException {
        RMT2BeanUtility util = null;
        util = new RMT2BeanUtility(bean);
        String className = RMT2Utility.getBeanClassName(bean);
        String classPackage = bean.getClass().getName();

        // Do not include certain complex object types in XML Document based on
        // which the package they live
        if (classPackage.indexOf("java.io.") > -1) {
            return null;
        }

        // handle classes that we do not want to include is document.
        if (className.equals("Class") || className.equals("ObjectInput")
                || className.equals("FileOutputStream")
                || className.equals("FileInputStream")
                || className.equals("ObjectOutput")
                || className.equals("String")) {
            return null;
        }
        Element beanRoot = new Element(className);

        // Add class name with complete package so that XML can
        // be adapted back to its respective java object.
        Element classPkgElement = new Element(NativeAdapterImpl.BEANCLASSNAME);
        classPkgElement.addContent(classPackage);
        beanRoot.addContent(classPkgElement);

        List<String> propNames = util.getPropertyNames();
        for (String name : propNames) {
            Element element = this.buildBeanElement(util, name);
            if (element != null) {
                beanRoot.addContent(element);
            }
        }
        return beanRoot;
    }

    /**
     * Creates a org.jdom.Element object where the tag name is <i>propname</i>
     * and the contents of the tag will be set to <i>value</i>.
     * 
     * @param propName
     *            The target property name of the bean.
     * @param value
     *            The value mapped to <i>propName</i>.
     * @return org.jdom.Element
     * @throws SystemException
     */
    private Element buildSimpleBeanElement(String propName, Object value)
            throws SystemException {
        Element element = new Element(propName);
        element.addContent(value.toString());
        return element;
    }

    /**
     * Verifies if <i>bean</i> represents a java native data type or a complex
     * data type.
     * 
     * @param bean
     *            The bean that is to be evaluated.
     * @return true when <i>bean</i> is a native type and false for all other
     *         types.
     * @throws SystemException
     */
    private boolean isSimpleBean(Object bean) throws SystemException {
        RMT2BeanUtility util = new RMT2BeanUtility();
        return util.isPrimitiveType(bean.getClass());

    }

    /**
     * Creates a Element object using the property name and its value contained
     * in <i>beanUtil</i>. The Element created will derive from either a simple
     * data type or a complex data type.
     * 
     * @param beanUtil
     *            {@link util.RMT2BeanUtility RMT2BeanUtility}
     * @param propName
     *            The name of the property to build the Element.
     * @return org.jdom.Element
     * @throws SystemException
     */
    private Element buildBeanElement(RMT2BeanUtility beanUtil, String propName)
            throws SystemException {
        try {
            Class classType = beanUtil.getPropertyType(propName);
            if (classType == null) {
                return null;
            }
            Object value = beanUtil.getPropertyValue(propName);
            Element element = null;
            if (beanUtil.isPrimitiveType(classType)) {
                element = new Element(propName);
                element.addContent(value == null ? null : value.toString());
            }
            else {
                element = this.buildComplexBeanElement(propName, value);
            }
            return element;
        } catch (NotFoundException e) {
            return null;
        }
    }

    /**
     * Builds an Element object from a single property that exist in the
     * specified complex data type, <i>bean</i>. The following complex data
     * types are supported: non-native javabeans, List, Map, and Set objects.
     * 
     * @param propName
     *            The property name containing the value to build Element.
     * @param bean
     *            The actual complex javabean to containing the target property.
     * @return org.jdom.Element
     * @throws SystemException
     */
    private Element buildComplexBeanElement(String propName, Object bean)
            throws SystemException {
        if (bean instanceof List) {
            return this.buildListBeanElement(propName, (List) bean);
        }

        if (bean instanceof Map) {
            return this.buildMapBeanElement(propName, (Map) bean);
        }

        if (bean instanceof Set) {
            return this.buildSetBeanElement(propName, (Set) bean);
        }

        // Make property name the parent element of the bean
        Element mainElement = new Element(propName);
        // Build XML element for non-collection bean object
        Element beanRoot = this.buildRootElement(bean);
        if (beanRoot == null) {
            return null;
        }
        mainElement.addContent(beanRoot);
        return mainElement;
    }

    /**
     * Build an Element object from an object that implements the List
     * interface.
     * 
     * @param propName
     *            The name of the property containing the value to build
     *            Element.
     * @param list
     *            The List implementation.
     * @return org.jdom.Element
     * @throws SystemException
     */
    private Element buildListBeanElement(String propName, List list)
            throws SystemException {
        if (list == null || list.size() <= 0) {
            return null;
        }
        // Make property name the parent element of the bean
        Element mainElement = new Element(propName);
        // Build XML element for all list elements
        for (int ndx = 0; ndx < list.size(); ndx++) {
            Object bean = list.get(ndx);
            Element beanRoot = null;
            if (this.isSimpleBean(bean)) {
                beanRoot = this.buildSimpleBeanElement("ListItem", bean);
            }
            else {
                beanRoot = this.buildRootElement(bean);
            }
            if (beanRoot == null) {
                continue;
            }
            mainElement.addContent(beanRoot);
        }
        return mainElement;
    }

    /**
     * Build an Element object from an object that implements the Map interface.
     * 
     * @param propName
     *            The name of the property containing the value to build
     *            Element.
     * @param map
     *            The Map implementation.
     * @return org.jdom.Element
     * @throws SystemException
     */
    private Element buildMapBeanElement(String propName, Map map)
            throws SystemException {
        if (map == null || map.isEmpty()) {
            return null;
        }
        // Make property name the parent element of the bean
        Element mainElement = new Element(propName);
        // Build XML element for all set elements
        Set keys = map.keySet();
        Iterator iter = keys.iterator();
        while (iter.hasNext()) {
            Object key = iter.next();
            Object bean = map.get(key);
            Element beanRoot = null;

            if (this.isSimpleBean(bean)) {
                beanRoot = this.buildSimpleBeanElement(key.toString(), bean);
            }
            else {
                beanRoot = new Element(key.toString());
                Element beanElement = this.buildRootElement(bean);
                beanRoot.addContent(beanElement);
            }
            if (beanRoot == null) {
                continue;
            }
            mainElement.addContent(beanRoot);
        }
        return mainElement;
    }

    /**
     * Build an Element object from an object that implements the Set interface.
     * 
     * @param propName
     *            The name of the property containing the value to build
     *            Element.
     * @param map
     *            The Set implementation.
     * @return org.jdom.Element
     * @throws SystemException
     */
    private Element buildSetBeanElement(String propName, Set set)
            throws SystemException {
        if (set == null || set.isEmpty()) {
            return null;
        }
        // Make property name the parent element of the bean
        Element mainElement = new Element(propName);
        // Build XML element for all set elements
        Iterator iter = set.iterator();
        while (iter.hasNext()) {
            Object bean = iter.next();
            Element beanRoot = null;
            if (this.isSimpleBean(bean)) {
                beanRoot = this.buildSimpleBeanElement("SetItem", bean);
            }
            else {
                beanRoot = this.buildRootElement(bean);
            }
            if (beanRoot == null) {
                continue;
            }
            mainElement.addContent(beanRoot);
        }
        return mainElement;
    }

    /**
     * Converts <i>xml</i> to an equivalent generic object.
     * 
     * @param xml
     *            Teh XML document to convert
     * @return A generic object representing <i>xml</i>
     * @throws SystemException
     */
    public Object xmlToBean(String xml) throws SystemException {
        RMT2BeanUtility beanUtil = null;
        try {
            beanUtil = this.createBeanInstance(xml);
            Object bean = this.buildBeanProperty(beanUtil, xml, "/");
            return bean;
        } catch (DatabaseException e) {
            throw new SystemException(e);
        }
    }

    /**
     * Builds a javabean using <i>xml</i> as the source. First, all first level
     * bean properties are built. Lastly, it tries to process any complex
     * instance structures found at the current level as a stande alone bean
     * which is recursively done until an end point is reached for that bean.
     * Each complex child javabean that is successfully processed will be
     * properly assoicated to its pareent.
     * 
     * @param beanUtil
     *            {@link util.RMT2BeanUtility RMT2BeanUtility} used to manage
     *            the properties of the bean.
     * @param xml
     *            The XML document containing the data needed to build the
     *            javabean.
     * @param query
     *            XPath query used to target the starting node.
     * @return An ojbect which its class name is equivalent to the root element
     *         in <i>xml</i>.
     * @throws DatabaseException
     *             Data access errors.
     * @throws SystemException
     *             Data migration related errors.
     */
    protected Object buildBeanProperty(RMT2BeanUtility beanUtil, String xml,
            String query) throws DatabaseException, SystemException {
        Map<String, Class> complexProps = new HashMap<String, Class>();

        // Build first level bean properties.
        complexProps = this.buildSimpleBeanProperty(beanUtil, xml, query);

        // Determine XPath root for all complex property elements
        String xPath = null;
        if (query.startsWith("//")) {
            xPath = query;
        }
        else {
            String rootClassName = beanUtil.getRootClassName(beanUtil
                    .getBeanInstance());
            xPath = "//" + rootClassName;
        }

        // Manage the complex property types
        Iterator<String> iter = complexProps.keySet().iterator();
        while (iter.hasNext()) {
            String prop = iter.next();
            Class clazz = complexProps.get(prop);
            Object childBean = this.buildComplexBeanProperty(xml, prop, clazz,
                    xPath);
            try {
                beanUtil.setPropertyValue(prop, childBean);
            } catch (Exception e) {
                this.msg = "Property, "
                        + prop
                        + ", could not be found as a complex type in bean utility instance";
                this.logger.log(Level.WARN, this.msg);
                continue;

            }
        }
        return beanUtil.getBeanInstance();

    }

    /**
     * Assigns the data values found in <i>xml</i> for all first-level bean
     * properties. These data values originate from properties of primitive
     * types. Builds a hash of non-primitive classes encountered which the key
     * is the class name and points to the Class instance of the class.
     * 
     * @param beanUtil
     *            {@link util.RMT2BeanUtility RMT2BeanUtility} used to manage
     *            the properties of the bean.
     * @param xml
     *            XML document acting as the source of data.
     * @param query
     *            XPath query to the starting node.
     * @return Map of complex properties encountered.
     * @throws DatabaseException
     *             Data access errors.
     * @throws SystemException
     *             Data migration related errors.
     */
    protected Map buildSimpleBeanProperty(RMT2BeanUtility beanUtil, String xml,
            String query) throws DatabaseException, SystemException {
        DaoApi api = null;
        try {
            Map<String, Class> complexProps = new HashMap<String, Class>();
            List props = beanUtil.getPropertyNames();
            api = XmlApiFactory.createXmlDao(xml);
            api.retrieve(query);
            String prop = null;
            String propVal = null;
            if (api.nextRow()) {
                for (int ndx = 0; ndx < props.size(); ndx++) {
                    try {
                        // Get data from the XML source
                        prop = (String) props.get(ndx);
                        if (this.isCollecionType(prop, beanUtil)) {
                            // Temporarily save all property names that are
                            // complex data types
                            Class clz = beanUtil.getPropertyType(prop);
                            complexProps.put(prop, clz);
                            continue;
                        }
                        else {
                            propVal = api.getColumnValue(prop);
                        }

                        // Set bean property value
                        beanUtil.setPropertyValue(prop, propVal);
                    } catch (NotFoundException e) {
                        this.msg = "Property, "
                                + prop
                                + ", could not be found in bean utility instance";
                        this.logger.log(Level.WARN, this.msg);
                    }
                } // end for
            }
            else {
                return null;
            }
            return complexProps;
        } catch (DatabaseException e) {
            throw new SystemException(e);
        } finally {
            api.close();
            api = null;
        }
    }

    /**
     * Uses <i>xml</i> to determine the complex data type from <i>targetProp</i>
     * in order to create a valid class instance. This instance will be
     * considered as child javabean that is destined to be assoicated with its
     * parent. The bean instance can be a java bean or a generic collection type
     * such as a List, Map, or Set. This version is requires that all collection
     * types contain instances that are of homogeneous data types as their
     * elements.
     * <p>
     * Any non-primitive types found in <i>targetProp</i> will be recursively
     * processed until an end point is reached.
     * 
     * @param xml
     *            XML document acting as the source of data to build bean.
     * @param targetProp
     *            The property to target in <i>xml</i>.
     * @param targetClass
     *            A Class instance of <i>targetProp</i>.
     * @param query
     *            XPath query to target starting point of node.
     * @return A generic object representing <i>targetProp</i>
     * @throws DatabaseException
     *             Data access errors.
     * @throws SystemException
     *             Data migration related errors.
     */
    protected Object buildComplexBeanProperty(String xml, String targetProp,
            Class targetClass, String query) throws DatabaseException,
            SystemException {
        RMT2BeanUtility beanUtil = null;

        // Determine Complex data type from targetProp and create instance.
        // Can be a java bean or a collection.

        // If collection, create List, Map, or Set instance.
        boolean collectType = this.isCollecionType(targetClass);
        Object collectionObj = null;
        if (collectType) {
            collectionObj = this.createCollecion(targetClass);
        }
        else {
            // Otherwise, create bean utility and manage properties.
            Object childObj = RMT2Utility.getClassInstance(targetClass
                    .getName());
            beanUtil = new RMT2BeanUtility(childObj);
            return this.buildBeanProperty(beanUtil, xml, query);
        }

        // At this point we must be dealing with a collection. We need
        // to get to the level of each bean in the collection so that
        // its proerties can be managed.
        XmlDao api = null;
        XmlDao api2 = null;
        try {
            api = XmlApiFactory.createXmlDao(xml);
            api.retrieve(query + "/" + targetProp);

            // Cycle through each element of the collection, evaluate its type,
            // and process accordingly.
            // Each type can represent a simple data type, javabean, or another
            // collection.
            if (api.nextRow()) {
                // Each item can be either a simple (property of a javabean) or
                // complex (element of a collection object or another
                // collection) type.
                List<String> childNames = null;
                if (api.hasChildren()) {
                    childNames = api.getChildrenNames(targetProp);
                    for (String elementName : childNames) {
                        api2 = XmlApiFactory.createXmlDao(xml);
                        String xPath = query + "/" + targetProp + "/"
                                + elementName;
                        api2.retrieve(xPath);

                        // Cycle through all like "elementName" elements.
                        int ndx = 0;
                        while (api2.nextRow()) {
                            // Expect all elements to be of the same data type.
                            List<String> childNames2 = api2
                                    .getChildrenNames(elementName);
                            if (childNames2 != null && childNames2.size() > 0) {
                                // First, determine the complex class type of
                                // current element
                                String className = null;
                                try {
                                    className = api2
                                            .getColumnValue(NativeAdapterImpl.BEANCLASSNAME);
                                } catch (Exception e) {
                                    throw new SystemException(e);
                                }

                                Object bean = RMT2Utility
                                        .getClassInstance(className);
                                xPath = query + "/" + targetProp + "/"
                                        + elementName + "[position() = "
                                        + ++ndx + "]";
                                if (this.isCollecionType(bean.getClass())) {
                                    // Recursively process collection type.
                                    bean = this
                                            .buildComplexBeanProperty(xml,
                                                    elementName,
                                                    bean.getClass(), xPath);
                                }
                                else {
                                    RMT2BeanUtility beanUtil2 = new RMT2BeanUtility(
                                            bean);
                                    bean = this.buildBeanProperty(beanUtil2,
                                            xml, xPath);
                                }
                                // Add bean to the collection
                                this.addBeanToCollection(collectionObj,
                                        elementName, bean);
                            }
                            else {
                                // Add simple element value to collection
                                String propVal = api2
                                        .getColumnValue(elementName);
                                this.addBeanToCollection(collectionObj,
                                        elementName, propVal);
                            }
                        } // end while api2.nextRow
                    } // end for
                } // end if api.hasChildren
            } // end if api.nextRow

            // Make collections null if empty
            collectionObj = this.nullifyEmptyCollection(collectionObj);
            return collectionObj;
        } catch (Exception e) {
            return null;
        } finally {
            if (api != null) {
                api.close();
                api = null;
            }
            if (api2 != null) {
                api2.close();
                api2 = null;
            }

        }

    }

    /**
     * Determines if <i>clazz</i> is an instanceof List, Map, or Set.
     * 
     * @param clazz
     *            an instance of Class.
     * @return true when <i>clazz</i> evaluates to a collection type or false
     *         otherwise.
     */
    private boolean isCollecionType(Class clazz) {
        List listObj = new ArrayList();
        Map mapObj = new HashMap();
        Set setObj = new HashSet();
        return (clazz.isInstance(listObj) || clazz.isInstance(mapObj) || clazz
                .isInstance(setObj));
        // if (classType.equalsIgnoreCase("java.util.List") ||
        // classType.equalsIgnoreCase("java.util.List") ||
        // classType.equalsIgnoreCase("java.util.Map"));
    }

    /**
     * Determines if the underlying bean in <i>beanUtil</i> is an instanceof
     * List, Map, or Set.
     * 
     * @param prop
     *            The property to target in beanUtil.
     * @param beanUtil
     *            The bean utility used to manage the properties of the bean.
     * @return true when <i>clazz</i> evaluates to a collection type or false
     *         otherwise.
     */
    private boolean isCollecionType(String prop, RMT2BeanUtility beanUtil) {
        Class clazz = beanUtil.getPropertyType(prop);
        if (clazz == null) {
            return false;
        }
        return this.isCollecionType(clazz);
    }

    /**
     * Creates a collection type from <i>clazz</i>.
     * 
     * @param clazz
     *            An instance of Class.
     * @return List, Map, or Set.
     */
    private Object createCollecion(Class clazz) {
        List listObj = new ArrayList();
        Map mapObj = new HashMap();
        Set setObj = new HashSet();
        if (clazz.isInstance(listObj)) {
            return listObj;
        }
        if (clazz.isInstance(mapObj)) {
            return mapObj;
        }
        if (clazz.isInstance(setObj)) {
            return setObj;
        }
        return null;
    }

    /**
     * Adds a bean to the collection as an element. The actual bean instance is
     * identified as <i>value</i>.
     * 
     * @param collectObj
     *            The collection which
     * @param property
     *            The property name of the bean to be added to the collection.
     *            This is used mainly for a Map collection where there the data
     *            exist in key/value pairs. <i>property</i> will serve as the
     *            key.
     * @param value
     *            The bean instance that is added to <i>collectObj</i>. For a
     *            Map collection, <i>value</i> will serve as the value of the
     *            key/value pair.
     */
    private void addBeanToCollection(Object collectObj, Object property,
            Object value) {
        if (collectObj instanceof List) {
            ((List) collectObj).add(value);
            return;
        }
        if (collectObj instanceof Set) {
            ((Set) collectObj).add(value);
            return;
        }
        if (collectObj instanceof Map) {
            ((Map) collectObj).put(property, value);
            return;
        }
        return;
    }

    /**
     * Returns null if the collection is found to be empty.
     * 
     * @param collectObj
     *            A List, Map, or Set collection.
     * @return null when <i>collectObj</i> is empty or <i>collectObj</i> as is
     *         when otherwise.
     */
    private Object nullifyEmptyCollection(Object collectObj) {
        boolean makeNull = false;

        if (collectObj instanceof List) {
            makeNull = ((List) collectObj).isEmpty();
        }
        if (collectObj instanceof Set) {
            makeNull = ((Set) collectObj).isEmpty();
        }
        if (collectObj instanceof Map) {
            makeNull = ((Map) collectObj).isEmpty();
        }

        if (makeNull) {
            return null;
        }
        return collectObj;
    }

    /**
     * Creates a class instance based on the supplied XML document and wraps the
     * bean within an instance of RMT2BeanUtility. The fully qualified class
     * name of the bean to be instantiated should found in the input XML
     * document as "beanClassName".
     * 
     * @param xml
     *            XML document containing the data needed to instantiate the
     *            target bean.
     * @return {@link util.RMT2BeanUtility RMT2BeanUtility}
     * @throws DatabaseException
     *             General data access errors.
     * @throws SystemException
     *             The fully qualified class name is not found in <i>xml</i> or
     *             problem instantiating the bean.
     */
    private RMT2BeanUtility createBeanInstance(String xml)
            throws DatabaseException, SystemException {
        if (xml == null || xml.length() <= 0) {
            throw new SystemException("XML document is null or invalid");
        }

        DaoApi api = XmlApiFactory.createXmlDao(xml);
        String clazzName = null;
        try {
            api.retrieve("/");
            if (api.nextRow()) {
                clazzName = api.getColumnValue(NativeAdapterImpl.BEANCLASSNAME);
                if (clazzName == null) {
                    throw new SystemException(
                            "Problem identifying class name for xml to bean operation");
                }
            }
            else {
                throw new SystemException(
                        "Problem retrieving root element from XML datasource for the xml to bean operation");
            }
            Object bean = RMT2Utility.getClassInstance(clazzName);
            RMT2BeanUtility beanUtil = new RMT2BeanUtility(bean);
            return beanUtil;
        } catch (NotFoundException e) {
            throw new SystemException(e);
        } finally {
            api.close();
            api = null;
        }
    }
}
