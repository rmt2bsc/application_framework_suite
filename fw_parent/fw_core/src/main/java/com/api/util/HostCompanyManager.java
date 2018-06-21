package com.api.util;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.jdom.output.Format;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.SystemException;
import com.api.config.AppPropertyPool;
import com.api.config.ConfigConstants;

public class HostCompanyManager {
    private Logger logger = Logger.getLogger("RMT2XmlUtility");

    private static final String APP_RESUOURCES = "com.AppResourcesEnglish";

    /**
     * Default constructor
     * 
     */
    private HostCompanyManager() {

    }

    /**
     * Creates an instance of HostCompanyManager
     * 
     * @return HostCompanyManager
     */
    public static HostCompanyManager getInstance() {
        return new HostCompanyManager();
    }

    /**
     * Retrieves the host comapny data from a resource bundle.
     * 
     * @return Company data in XML format.
     */
    public String getHostCompanyInfo() {
        String results = null;

        CompanyInfo comp = new CompanyInfo();
        Document doc = this.createXMLDocument(comp);

        // serialize it onto System.out
        try {
            XMLOutputter serializer = new XMLOutputter();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Format format = serializer.getFormat();
            format.setOmitDeclaration(true);
            format.setOmitEncoding(true);
            serializer.setFormat(format);
            serializer.output(doc, baos);
            results = baos.toString();
        } catch (IOException e) {
            System.err.println(e);
        }

        logger.log(Level.INFO, results);
        return results;
    }

    /**
     * Creates an XML object based on the Host Company data obtained from the
     * properties file.
     * 
     * @param comp
     *            CompanyInfo Object
     * @return Document.
     */
    private Document createXMLDocument(CompanyInfo comp) {
        String entityName = "company";
        Element root = new Element(entityName);
        try {
            this.setupElement(root, "name", comp.name);
            if (comp.contact != null && comp.contact.length() > 0) {
                this.setupElement(root, "contact", comp.contact);
            }
            if (comp.Addr1 != null && comp.Addr1.length() > 0) {
                this.setupElement(root, "address1", comp.Addr1);
            }
            if (comp.Addr2 != null && comp.Addr2.length() > 0) {
                this.setupElement(root, "address2", comp.Addr2);
            }
            if (comp.Addr3 != null && comp.Addr3.length() > 0) {
                this.setupElement(root, "address3", comp.Addr3);
            }
            if (comp.Addr4 != null && comp.Addr4.length() > 0) {
                this.setupElement(root, "address4", comp.Addr4);
            }
            this.setupElement(root, "city", comp.city);
            this.setupElement(root, "state", comp.state);
            this.setupElement(root, "zip", comp.zip);
            if (comp.phone != null && comp.phone.length() > 0) {
                this.setupElement(root, "phone", comp.phone);
            }
            if (comp.fax != null && comp.fax.length() > 0) {
                this.setupElement(root, "fax", comp.fax);
            }
            if (comp.email != null && comp.email.length() > 0) {
                this.setupElement(root, "email", comp.email);
            }
            if (comp.website != null && comp.website.length() > 0) {
                this.setupElement(root, "website", comp.website);
            }
            Document doc = new Document(root);
            return doc;
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * Creates an XML element tag using root, elementName, and value.
     * 
     * @param root
     *            The root element
     * @param elementName
     *            the name of the element to create.
     * @param value
     *            The value of element
     * @return Element
     * @throws Exception
     */
    private Element setupElement(Element root, String elementName, String value)
            throws Exception {
        Element element = null;
        try {
            element = new Element(elementName);
            element.setText(value);
            root.addContent(element);
            return element;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Inner class that represents the company info
     * 
     * @author roy.terrell
     * 
     */
    private class CompanyInfo {
        private String name;

        private String contact;

        private String Addr1;

        private String Addr2;

        private String Addr3;

        private String Addr4;

        private String city;

        private String state;

        private String zip;

        private String phone;

        private String fax;

        private String email;

        private String website;

        /**
         * Constructor retrieves company data from a resource bundle.
         * 
         */
        private CompanyInfo() {
            name = AppPropertyPool.getProperty(ConfigConstants.OWNER_NAME);
            contact = AppPropertyPool
                    .getProperty(ConfigConstants.OWNER_CONTACT);
            Addr1 = AppPropertyPool.getProperty(ConfigConstants.OWNER_ADDR1);
            Addr2 = AppPropertyPool.getProperty(ConfigConstants.OWNER_ADDR2);
            Addr3 = AppPropertyPool.getProperty(ConfigConstants.OWNER_ADDR3);
            Addr4 = AppPropertyPool.getProperty(ConfigConstants.OWNER_ADDR4);
            city = AppPropertyPool.getProperty(ConfigConstants.OWNER_CITY);
            state = AppPropertyPool.getProperty(ConfigConstants.OWNER_STATE);
            zip = AppPropertyPool.getProperty(ConfigConstants.OWNER_ZIP);
            phone = AppPropertyPool.getProperty(ConfigConstants.OWNER_PHONE);
            fax = AppPropertyPool.getProperty(ConfigConstants.OWNER_FAX);
            email = AppPropertyPool.getProperty(ConfigConstants.OWNER_EMAIL);
            website = AppPropertyPool
                    .getProperty(ConfigConstants.OWNER_WEBSITE);
        }
    }

} // end class

