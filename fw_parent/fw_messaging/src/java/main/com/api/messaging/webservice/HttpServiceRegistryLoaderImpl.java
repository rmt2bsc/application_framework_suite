package com.api.messaging.webservice;

import java.io.InputStream;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.NotFoundException;
import com.SystemException;
import com.api.DaoApi;
import com.api.config.ConfigConstants;
import com.api.messaging.MessageRoutingInfo;
import com.api.messaging.webservice.http.HttpClient;
import com.api.messaging.webservice.http.HttpException;
import com.api.persistence.DatabaseException;
import com.api.xml.RMT2XmlUtility;
import com.api.xml.XmlApiFactory;
import com.constants.GeneralConst;
import com.util.RMT2File;

/**
 * An implementation of the ServiceRegistry interface where the service entries
 * are loaded from web service via the HTTP protocol.
 * 
 * @author Roy Terrell
 * 
 */
class HttpServiceRegistryLoaderImpl extends AbstractServiceRegistryImpl {

    private static Logger logger = Logger
            .getLogger(HttpServiceRegistryLoaderImpl.class);

    /**
     * Create a HttpServiceRegistryLoaderImpl containing an uninitialized
     * service registry.
     */
    public HttpServiceRegistryLoaderImpl() {
        super();
        return;
    }

    /**
     * Build service registry based on the resource sub type,
     * <i>rsrcSubTypeId</i>.
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
    public Map<String, MessageRoutingInfo> loadServices() {
        if (AbstractServiceRegistryImpl.getServices() != null) {
            return AbstractServiceRegistryImpl.getServices();
        }
        try {
            Object results = this.fetchAvailableServices();
            String data = null;
            if (results instanceof Throwable) {
                throw (Throwable) results;
            }
            if (results instanceof String) {
                data = (String) results;
            }
            Map<String, MessageRoutingInfo> registry = (Map<String, MessageRoutingInfo>) this
                    .createServiceList(data);
            AbstractServiceRegistryImpl.setServices(registry);
            return registry;
        } catch (Throwable e) {
            throw new SystemException(
                    "Error occurred loading/creating the Web Service Registry",
                    e);
        }
    }

    /**
     * Build web service service registry based on the resource sub type,
     * <i>rsrcSubTypeId</i>. By dfault, all web services with a resource type of
     * "3" are loaded.
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
            Object results = this.fetchAvailableServices(rsrcSubTypeId);
            String data = null;
            if (results instanceof Throwable) {
                throw (Throwable) results;
            }
            if (results instanceof String) {
                data = (String) results;
            }
            Map<String, MessageRoutingInfo> registry = (Map<String, MessageRoutingInfo>) this
                    .createServiceList(data);
            AbstractServiceRegistryImpl.setServices(registry);
            return registry;
        } catch (Throwable e) {
            throw new SystemException(
                    "Error occurred loading/creating the Web Service Registry",
                    e);
        }
    }

    /**
     * Fetches all available services.
     * 
     * @return
     */
    private Object fetchAvailableServices() {
        Properties prop = new Properties();
        String url = System.getProperty(ConfigConstants.PROPNAME_SERVER);

        // Get the services host and the servlet to contact.
        url = url + "/"
                + System.getProperty(ConfigConstants.PROPNAME_SERVICELOAD_HOST);
        // Get the command module we are targeting.
        url = url
                + System.getProperty(ConfigConstants.PROPNAME_SERVICELOAD_COMMAND);
        // Get Service Id
        String serviceId = System
                .getProperty(ConfigConstants.PROPNAME_SERVICELOAD_SERVICEID);
        // Recognize the client action as the service id.
        if (serviceId == null) {
            return null;
        }
        prop.setProperty(GeneralConst.REQ_CLIENTACTION, serviceId);

        try {
            HttpClient http = new HttpClient(url);
            InputStream in = (InputStream) http.sendPostMessage(prop);
            Object data = null;
            data = RMT2File.getStreamObjectData(in);
            http.close();
            return data;
        } catch (HttpException e) {
            throw new SystemException(e);
        }
    }

    /**
     * Fetches all services associated with ressource sub type.
     * 
     * @param rsrcSubTypeId
     * @return
     */
    private Object fetchAvailableServices(int rsrcSubTypeId) {
        Properties prop = new Properties();
        String url = System.getProperty(ConfigConstants.PROPNAME_SERVER);

        // Get the services host and the servlet to contact.
        url = url + "/"
                + System.getProperty(ConfigConstants.PROPNAME_SERVICELOAD_HOST);
        // Get the command module we are targeting.
        url = url
                + System.getProperty(ConfigConstants.PROPNAME_SERVICELOAD_COMMAND);
        // Get Service Id
        String serviceId = System
                .getProperty(ConfigConstants.PROPNAME_SERVICELOAD_SERVICEID);
        // Recognize the client action as the service id.
        if (serviceId == null) {
            return null;
        }
        prop.setProperty(GeneralConst.REQ_CLIENTACTION, serviceId);

        // Setup other parameters needed to make a successful service call.
        prop.setProperty(WebServiceConstants.RSRC_TYPE_DBNAME,
                String.valueOf(WebServiceConstants.WEBSERV_VAL));
        prop.setProperty(WebServiceConstants.RSRC_SUBTYPE_DBNAME,
                String.valueOf(rsrcSubTypeId));

        try {
            HttpClient http = new HttpClient(url);
            InputStream in = (InputStream) http.sendPostMessage(prop);
            Object data = null;
            data = RMT2File.getStreamObjectData(in);
            http.close();
            return data;
        } catch (HttpException e) {
            throw new SystemException(e);
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
     * @param data
     *            A XML document representing data from the user_resource table.
     *            Each row of data will be enclosed by the xml tag,
     *            user_resource.
     * @return Hashtable of service name/service URL key/value pairs.
     * @throws DatabaseException
     *             General database errors
     * @throws SystemException
     *             General system errors.
     */
    private Hashtable<String, MessageRoutingInfo> createServiceList(String data)
            throws DatabaseException, SystemException {
        String msg;
        int loadCount = 0;

        if (data == null) {
            return null;
        }

        // Determine if Root element name must qualified with namespace
        String rootElement = "RS_authentication_resource";
        rootElement = RMT2XmlUtility.getDocumentName(data);

        try {
            Document doc = RMT2XmlUtility.stringToDocument(data);
            Element e = doc.getDocumentElement();
            rootElement = e.getTagName();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        // Retrieve items
        logger.info("Fetching web service registry entries...");
        DaoApi api = XmlApiFactory.createXmlDao(data);
        try {
            Hashtable<String, MessageRoutingInfo> srvHash = null;
            // Get all user_resource elements
            Object result[] = api.retrieve("//" + rootElement + "/items");
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
                try {
                    name = api.getColumnValue("rsrc_name");
                    url = api.getColumnValue("url");
                    secured = api.getColumnValue("secured");
                    host = api.getColumnValue("host");
                    MessageRoutingInfo srvc = new MessageRoutingInfo();
                    srvc.setMessageId(name);
                    srvc.setDestination(url);
                    srvc.setSecured(secured == "1");
                    srvc.setHost(host == null || host.equals("") ? null : host);
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
