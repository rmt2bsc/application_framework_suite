package com.api.xml.parsers.datasource;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import com.SystemException;
import com.api.persistence.db.DbSqlConst;
import com.api.persistence.db.orm.bean.DataSourceColumn;
import com.api.persistence.db.orm.bean.ObjectMapperAttrib;
import com.api.persistence.db.orm.bean.TableUsageBean;
import com.api.xml.RMT2SaxAttributesBean;
import com.api.xml.RMT2XmlUtility;
import com.util.RMT2File;
import com.util.RMT2Utility;

/**
 * Java XML class used to parse XML documents using the a pluggable third party
 * SAX2 API implementation. The chosen implementation is required to be
 * configured in the SystemParms.properties file (could exist at the application
 * context or the web server context) by the key named, "SAXDriver". The value
 * of this key should be the full classpath of the driver's class name.
 * 
 * @author roy.terrell
 * 
 * 
 */
class RMT2Sax2OrmThridPartyDriverImpl extends DefaultHandler implements
        RMT2OrmDatasourceParser {

    private static Logger logger = Logger
            .getLogger("RMT2Sax2OrmThridPartyDriverImpl");

    private int docType;

    private ObjectMapperAttrib ds;

    private Hashtable elementAtts;

    private RMT2SaxAttributesBean attObj;

    private Hashtable selectStatement;

    private Hashtable tables;

    private Hashtable colDefs;

    private String orderByClause;

    private String whereClause;

    private int tableCount;

    private String dsName;

    protected String saxDriver;

    protected XMLReader parser;

    protected Object obj;

    protected String msg;

    protected String text;

    protected String docPath;

    protected String docName;

    /**
     * Default constructor
     * 
     * @throws SystemException
     */
    private RMT2Sax2OrmThridPartyDriverImpl() throws SystemException {
        logger.log(Level.DEBUG, "Creating RMT2Sax2OrmThridPartyDriverImpl()");
        this.msg = null;
        this.attObj = new RMT2SaxAttributesBean();
        this.ds = new ObjectMapperAttrib();
        this.ds.setType("xml");
        this.attObj = new RMT2SaxAttributesBean();
        this.selectStatement = new Hashtable();
        this.colDefs = new Hashtable();
        this.tables = new Hashtable();
        this.tableCount = 0;
    }

    /**
     * Creates a RMT2SaxDocBase object with the XML document.
     * 
     * @param doc
     *            The XML Document
     * @throws SystemException
     *             Illegal access or resources, problem instatitating document,
     *             SAX driver cannot be found or general errors.
     */
    public RMT2Sax2OrmThridPartyDriverImpl(String doc) throws SystemException {
        this();
        logger.log(Level.DEBUG,
                "Creating RMT2Sax2OrmThridPartyDriverImpl(String");
        try {
            this.docName = doc;
            this.getResources();
            // Create the parser
            if (this.saxDriver == null || this.saxDriver.equals("")) {
                this.parser = XMLReaderFactory.createXMLReader();
            }
            else {
                this.parser = XMLReaderFactory.createXMLReader(this.saxDriver);
            }

            // Specify that we don't want validation. This is the SAX2
            // API for requesting parser features. Note the use of a
            // globally unique URL as the feature name. Non-validation is
            // actually the default, so this line isn't really necessary.
            this.parser.setFeature("http://xml.org/sax/features/validation",
                    false);

            // Tell the parser how it should handle the content events and error
            // events
            this.parser.setContentHandler(this);
            this.parser.setErrorHandler(this);
        } catch (SAXNotSupportedException e) {
            msg = "Operation from thrid party SAX class library is not supported";
            throw new SystemException(msg, e);
        } catch (SAXNotRecognizedException e) {
            msg = "Operation from thrid party SAX class library is not recognized";
            throw new SystemException(msg, e);
        } catch (SAXException e) {
            msg = "General SAX Exception";
            throw new SystemException(msg, e);
        }
    }

    /**
     * Constructs a RMT2SaxDocBase object using the document name, document type
     * and system ResourcBundle.
     * 
     * @param doc
     *            The name of the document
     * @param docType
     *            The document type
     * @throws SystemException
     */
    public RMT2Sax2OrmThridPartyDriverImpl(String doc, int docType)
            throws SystemException {
        this(doc);
        logger.log(Level.DEBUG,
                "Creating RMT2Sax2OrmThridPartyDriverImpl(String, int");
        this.docType = docType;
    }

    /**
     * Parses the XML document
     * 
     * @return null
     * @throws SystemException
     */
    public ObjectMapperAttrib parseDocument() throws SystemException {
        // parse document
        logger.info("Preparing to parse Datasource document, " + this.docName
                + "...");
        try {
            InputStream is = RMT2File.getFileInputStream(this.docName);
            InputSource in = new InputSource(is);
            parser.parse(in);
            logger.info(this.docName + "  was parsed successfully");
            return this.ds;
        } catch (Exception e) {
            String targetDoc = this.docName;
            String msg = "Problem parsing doc: " + targetDoc;
            throw new SystemException(msg + ": " + e.getMessage());
        }
    }

    /**
     * Obtains system properties from the system ResourceBunled needed for
     * parsing XML documents.
     * 
     * @throws SystemException
     */
    public void getResources() throws SystemException {
        try {
            this.saxDriver = RMT2XmlUtility.getSaxDriver();
            // These two lines are commented out since the location of the
            // datasource XML file is based on classpath instead of file
            // directory path.

            // this.docName = OrmConfigHelper
            // .getOrmDatasourceFullDirPath(this.docName);
            return;
        } catch (Throwable e) {
            throw new SystemException(e);
        }

    }

    /**
     * Logs the beginning of this method invocation
     */
    public void startDocument() throws SAXException {
        logger.log(Level.DEBUG, "startDocument is called:");
    }

    /**
     * Logs the beginning of this method invocation.
     */
    public void endDocument() throws SAXException {
        logger.log(Level.DEBUG, "endDocument is called:");
        this.ds.setName(dsName); // set Datasource's Identity (Name)
        this.ds.setSelectStatement(this.selectStatement); // set Datasource's
                                                          // selectStatement
        this.ds.setColumnDef(this.colDefs); // set Datasource's Column
                                            // Definitions
        this.ds.setTables(this.tables); // set Datasource's Table List
    }

    /**
     * Creates a RMT2SaxAttributesBean object populated with the data values
     * contained in amap for element identified as, "name".
     */
    public void startElement(String uri, String name, String qName,
            Attributes amap) throws SAXException {
        logger.log(Level.DEBUG, "startElement is called: element name=" + name);

        this.text = null;
        try {
            this.elementAtts = new Hashtable();
            for (int i = 0; i < amap.getLength(); i++) {
                RMT2SaxAttributesBean att = new RMT2SaxAttributesBean();
                // String elementName = amap.getName(i);
                String elementName = amap.getQName(i);
                String type = amap.getType(i);
                String value = amap.getValue(i);
                if (value == null) {
                    value = "";
                }

                att.setAttName(elementName);
                att.setAttType(type);
                att.setAttValue(value);
                this.elementAtts.put(elementName, att);
                logger.log(Level.DEBUG, "  attribute name=" + elementName
                        + " type=" + type + " value=" + value);
            }
        } catch (SystemException e) {
            throw new SAXException(e.getMessage());
        }
        this.buildTableElements(name, amap);
    }

    /**
     * Builds the attributes of the current ORM Datasource element,
     * <i>elementName</i>. The target element will be either a datasource, sql,
     * TableUsage, or colattr XML tags of the ORM DataSource configuration.
     * 
     * @param elementName
     *            the element to process
     * @param amap
     *            a list of data items representing the attributes of
     *            <i>elementName</i>
     */
    private void buildTableElements(String elementName, Attributes amap)
            throws SAXException {
        Hashtable atts = this.getElementAtts();
        int count = 0;

        // Obtain Datasource information
        if (elementName.equalsIgnoreCase("datasource")) {
            attObj = (RMT2SaxAttributesBean) atts.get(amap.getQName(0));
            this.dsName = attObj.getAttValue();
        }

        // Obtain SQL Statement information
        if (elementName.equalsIgnoreCase("sql")) {
            count = atts.size();
            logger.log(Level.DEBUG,
                    "startElement @ decendent - Atribute Count: " + count);
            Enumeration keys = atts.keys();
            String keyName = null;

            while (keys.hasMoreElements()) {
                keyName = (String) keys.nextElement();
                int keyValue = DbSqlConst.getSelectObjectKey(keyName); // get
                                                                       // numeric
                                                                       // key
                                                                       // value
                attObj = (RMT2SaxAttributesBean) atts.get(keyName); // get key's
                                                                    // sql
                                                                    // clause
                logger.log(
                        Level.DEBUG,
                        "startElement @ decendent - Atribute name: "
                                + attObj.getAttName() + "  Value: "
                                + attObj.getAttValue());
                if (attObj.getAttValue() != null) {
                    selectStatement.put(new Integer(keyValue),
                            this.getModifiedValue(attObj));
                }
                else {
                    selectStatement.put(new Integer(keyValue), "");
                }
            }
        }

        // Obtain all Table Entities
        if (elementName.equalsIgnoreCase("TableUsage")) {
            count = atts.size();
            Enumeration keys = atts.keys();
            String keyName = null;
            TableUsageBean dsTable = null;

            try {
                dsTable = new TableUsageBean();
            } catch (SystemException e) {
                throw new SAXException(e.getMessage());
            }
            tableCount++;
            while (keys.hasMoreElements()) {
                keyName = (String) keys.nextElement();
                attObj = (RMT2SaxAttributesBean) atts.get(keyName); // get key's
                                                                    // Table
                                                                    // Attribute

                if (keyName.equalsIgnoreCase("name"))
                    dsTable.setName(attObj.getAttValue());
                else if (keyName.equalsIgnoreCase("dbName"))
                    dsTable.setDbName(attObj.getAttValue());
                else if (keyName.equalsIgnoreCase("Updateable"))
                    dsTable.setUpdateable((attObj.getAttValue()
                            .equalsIgnoreCase("true") ? true : false));
            }
            // Key is stored in numerical order: the order which
            // the tables appear in the XML Document.
            tableCount = tables.size() + 1;
            tables.put(new Integer(tableCount), dsTable);
        }

        // Obtain SQL Column Attributes
        if (elementName.equalsIgnoreCase("colattr")) {
            count = atts.size();
            Enumeration keys = atts.keys();
            String keyName = null;
            DataSourceColumn dsc = null;

            try {
                dsc = new DataSourceColumn();
            } catch (SystemException e) {
                throw new SAXException(e.getMessage());
            }
            while (keys.hasMoreElements()) {
                keyName = (String) keys.nextElement();
                attObj = (RMT2SaxAttributesBean) atts.get(keyName); // get key's
                                                                    // sql
                                                                    // clause

                if (keyName.equalsIgnoreCase("name"))
                    dsc.setName(attObj.getAttValue());
                else if (keyName.equalsIgnoreCase("dbName"))
                    dsc.setDbName(attObj.getAttValue());
                else if (keyName.equalsIgnoreCase("SqlType"))
                    dsc.setSqlType(attObj.getAttValue());
                else if (keyName.equalsIgnoreCase("IsNull"))
                    dsc.setNullable((attObj.getAttValue().equalsIgnoreCase(
                            "true") ? true : false));
                else if (keyName.equalsIgnoreCase("JavaType"))
                    dsc.setJavaType(RMT2Utility.getJavaType(attObj
                            .getAttValue()));
                else if (keyName.equalsIgnoreCase("PrimaryKey"))
                    dsc.setPrimaryKey((attObj.getAttValue().equalsIgnoreCase(
                            "true") ? true : false));
                else if (keyName.equalsIgnoreCase("TableName"))
                    dsc.setTableName(attObj.getAttValue());
                else if (keyName.equalsIgnoreCase("Updateable"))
                    dsc.setUpdateable((attObj.getAttValue().equalsIgnoreCase(
                            "true") ? true : false));
                else if (keyName.equalsIgnoreCase("DisplayHeader"))
                    dsc.setDisplayValue(attObj.getAttValue());
                else if (keyName.equalsIgnoreCase("Computed"))
                    dsc.setComputed((attObj.getAttValue().equalsIgnoreCase(
                            "true") ? true : false));
            }
            colDefs.put(dsc.getName(), dsc);
            logger.log(Level.DEBUG, "startElement @ decendent: Column Def for "
                    + dsc.getName() + ": " + dsc);
        }
        logger.log(Level.DEBUG, "startElement at decendent is called:");
    }

    /**
     * Modifies the where clause or order by clause based on values contained in
     * att. If the stock clause exist then the value of att is appended to that
     * clause. Otherwise, the clause is equal to the appropriate value of att.
     * 
     * @param att
     *            contains the name and value of the where or order by clause.
     * @return Modified where clause or order by clause.
     */
    private String getModifiedValue(RMT2SaxAttributesBean att) {

        String attName = att.getAttName();
        String attValue = att.getAttValue();

        if (!attName.equalsIgnoreCase("Where")
                && !attName.equalsIgnoreCase("OrderBy")) {
            return attValue;
        }

        if (attName.equalsIgnoreCase("Where")) {
            if (this.whereClause != null && this.whereClause.length() > 0) {
                if (attValue == null || attValue.equals("")) {
                    attValue = this.whereClause;
                }
                else {
                    attValue += " and " + this.whereClause;
                }

                return attValue;
            }
        }

        if (attName.equalsIgnoreCase("OrderBy")) {
            if (this.orderByClause != null && this.orderByClause.length() > 0) {
                if (attValue == null || attValue.equals("")) {
                    attValue = this.orderByClause;
                }
                else {
                    attValue += ", " + this.orderByClause;
                }

                return attValue;
            }
        }
        return attValue;
    }

    /**
     * Sets the element attribute varialbe to null.
     */
    public void endElement(String uri, String name, String qName)
            throws SAXException {
        logger.log(Level.DEBUG, "endElement is called: " + name);
        this.elementAtts = null;
    }

    /**
     * Get the contents of the element.
     */
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        int end = start + length;
        for (int ndx = start; ndx < end; ndx++) {
            if (this.text == null) {
                this.text = "";
            }
            this.text += ch[ndx];
        }
        logger.log(Level.DEBUG, "characters is called: "
                + new String(ch, start, length));
    }

    /**
     * Get Document Path.
     * 
     * @return Document path
     */
    public String getDocPath() {
        return this.docPath;
    }

    /**
     * Get the name of document.
     * 
     * @return Document's name
     */
    public String getDocName() {
        return this.docName;
    }

    /**
     * Get the Document type.
     * 
     * @return document type.
     */
    public int getDocType() {
        return this.docType;
    }

    /**
     * Get SAX Driver.
     * 
     * @return Driver name.
     */
    public String getSaxDriver() {
        return this.saxDriver;
    }

    /**
     * Get Element attributes
     * 
     * @return Hashtable of attribute name and values.
     */
    public Hashtable getElementAtts() {
        return this.elementAtts;
    }

}
