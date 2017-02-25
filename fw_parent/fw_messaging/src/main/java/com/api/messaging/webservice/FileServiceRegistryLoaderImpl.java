package com.api.messaging.webservice;

import java.io.InputStream;
import java.util.Hashtable;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.NotFoundException;
import com.SystemException;
import com.api.DaoApi;
import com.api.messaging.MessageRoutingInfo;
import com.api.persistence.DatabaseException;
import com.api.xml.RMT2XmlUtility;
import com.api.xml.XmlApiFactory;
import com.util.RMT2File;

/**
 * An implementation of the ServiceRegistry interface where the service entries
 * are loaded from a XML file.
 * <p>
 * The XML file's location is expected to be specified at the time the
 * FileServiceRegistryLoaderImpl class is constructred.
 * 
 * @author roy terrell
 * @deprecated Use the {@link SystemConfiguratorServiceRegistryLoaderImpl} class
 *             since service registry data is obtained from the
 *             SystemConfigurator object.
 * 
 */
class FileServiceRegistryLoaderImpl extends AbstractServiceRegistryImpl {

    private static Logger logger = Logger
            .getLogger(FileServiceRegistryLoaderImpl.class);

    private static final String DEFAULT_XML_FILE = "xml/SoapWebServiceList.xml";

    // private Map<String, MessageRoutingInfo> registry;

    private String fileName;

    private String xml;

    /**
     * Create a FileServiceRegistryLoaderImpl by identifying the location of the
     * XML file containing the list of web service entries.
     * 
     * @param filePath
     *            the complete path of the XML file containing the web service
     *            entries.
     */
    public FileServiceRegistryLoaderImpl(String filePath) {
        super();
        this.loadFile(filePath);
    }

    /**
     * Create service registry by loading all entries.
     * 
     * @param rsrcSubTypeId
     *            the resource sub type
     * @return Map<String,
     *         {@link com.api.messaging.webservice.MessageRoutingInfo
     *         MessageRoutingInfo}>
     * @throws SystemException
     *             General and database access errors.
     */
    @Override
    public Map<String, MessageRoutingInfo> loadServices() {
        if (AbstractServiceRegistryImpl.getServices() != null) {
            return AbstractServiceRegistryImpl.getServices();
        }
        try {
            Map<String, MessageRoutingInfo> registry = (Map<String, MessageRoutingInfo>) this
                    .createServiceList(this.xml, 0);
            AbstractServiceRegistryImpl.setServices(registry);
            return registry;
        } catch (SystemException e) {
            this.msg = "Error occurred loading/creating the Web Service Registry using a XML file source";
            throw new SystemException(this.msg, e);
        }
    }

    /**
     * Create service registry by loading those entries related to a particular
     * resource sub type id.
     * <p>
     * By dfault, all web services with a resource type of "3" are loaded.
     * 
     * @param rsrcSubTypeId
     *            the resource sub type
     * @return Map<String,
     *         {@link com.api.messaging.webservice.MessageRoutingInfo
     *         MessageRoutingInfo}>
     * @throws SystemException
     *             General and database access errors.
     */
    public Map<String, MessageRoutingInfo> loadServices(int rsrcSubTypeId) {
        if (AbstractServiceRegistryImpl.getServices() != null) {
            return AbstractServiceRegistryImpl.getServices();
        }
        try {
            Map<String, MessageRoutingInfo> registry = (Map<String, MessageRoutingInfo>) this
                    .createServiceList(this.xml, rsrcSubTypeId);
            AbstractServiceRegistryImpl.setServices(registry);
            return registry;
        } catch (SystemException e) {
            this.msg = "Error occurred loading/creating the Web Service Registry using a XML file source";
            throw new SystemException(this.msg, e);
        }
    }

    /**
     * 
     * @param filePath
     */
    private void loadFile(String filePath) {
        if (filePath == null) {
            this.fileName = FileServiceRegistryLoaderImpl.DEFAULT_XML_FILE;
            logger.info("Loading web service registry with default XML file, "
                    + FileServiceRegistryLoaderImpl.DEFAULT_XML_FILE + "...");
        }
        else {
            this.fileName = filePath;
            logger.info("Loading web service registry with user specified XML file, "
                    + filePath + "...");
        }

        try {
            InputStream is = RMT2File.getFileInputStream(this.fileName);
            this.xml = RMT2File.getStreamStringData(is);
            logger.info("Web service registry was loaded successfully");
        } catch (SystemException e) {
            this.msg = "Error occurred loading XML content from file, "
                    + this.fileName;
            logger.error(this.msg);
            this.xml = null;
        }
    }

    /**
     * Creates a list of available services in the form of key/value pairs and
     * persist the list in memory for repetive usage. The key represents the
     * service name, and the value represents the service URL. The list is used
     * as a lookup mechanism for service requests. The query method used to
     * extract the data is XPath, which targets each user_resource tag element
     * as a row of data.
     * 
     * @param xml
     *            A XML document representing data from the user_resource table.
     *            Each row of data will be enclosed by the xml tag,
     *            user_resource.
     * @return Hashtable of service name/service URL key/value pairs.
     * @throws DatabaseException
     *             General database errors
     * @throws SystemException
     *             General system errors.
     */
    private Hashtable<String, MessageRoutingInfo> createServiceList(String xml,
            int rsrcSubTypeId) throws DatabaseException, SystemException {
        String msg;
        int loadCount = 0;

        if (xml == null) {
            return null;
        }

        // Determine if Root element name must qualified with namespace
        String rootElement = "resources";
        rootElement = RMT2XmlUtility.getDocumentName(xml);

        try {
            Document doc = RMT2XmlUtility.stringToDocument(xml);
            Element e = doc.getDocumentElement();
            rootElement = e.getTagName();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        // Retrieve items
        logger.info("Fetching web service registry entries...");
        DaoApi api = XmlApiFactory.createXmlDao(xml);
        String query = null;
        try {
            if (rsrcSubTypeId > 0) {
                query = "//" + rootElement + "/user_resource[rsrc_subtype_id="
                        + rsrcSubTypeId + "]";
            }
            else {
                query = "//" + rootElement + "user_resource";
            }
            Hashtable<String, MessageRoutingInfo> srvHash = null;
            // Get all user_resource elements
            Object result[] = api.retrieve(query);
            int rows = ((Integer) result[0]).intValue();
            if (rows <= 0) {
                return srvHash;
            }

            // Begin to build services map
            while (api.nextRow()) {
                // Instantiate service map for the first interation.
                if (srvHash == null) {
                    srvHash = new Hashtable<String, MessageRoutingInfo>();
                }
                String name;
                String url;
                String secured;
                String host;
                String routerType;
                try {
                    name = api.getColumnValue("name");
                    url = api.getColumnValue("url");
                    secured = api.getColumnValue("secured");
                    host = api.getColumnValue("host");
                    routerType = api.getColumnValue("router_type");
                    MessageRoutingInfo srvc = new MessageRoutingInfo();
                    srvc.setMessageId(name);
                    srvc.setDestination(url);
                    srvc.setSecured(secured.equals("1"));
                    srvc.setHost(host == null || host.equals("") ? null : host);
                    srvc.setRouterType(routerType);
                    srvHash.put(name, srvc);
                    loadCount++;
                } catch (NotFoundException e) {
                    msg = "Could not locate remote service data: "
                            + e.getMessage();
                    logger.log(Level.WARN, msg);
                }
            } // Go to next element
            return srvHash;
        } catch (DatabaseException e) {
            throw e;
        } catch (SystemException e) {
            throw e;
        } finally {
            logger.log(Level.INFO, "Total number of services loaded: "
                    + loadCount);
        }
    }

}
