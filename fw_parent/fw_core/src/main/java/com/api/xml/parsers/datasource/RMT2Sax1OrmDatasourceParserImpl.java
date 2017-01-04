package com.api.xml.parsers.datasource;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.SystemException;
import com.util.RMT2File;
import com.util.RMT2Utility;

import com.api.persistence.db.DbSqlConst;
import com.api.persistence.db.orm.OrmConfigHelper;
import com.api.persistence.db.orm.bean.DataSourceColumn;
import com.api.persistence.db.orm.bean.ObjectMapperAttrib;
import com.api.persistence.db.orm.bean.TableUsageBean;
import com.api.security.RMT2TagQueryBean;
import com.api.xml.RMT2SaxAttributesBean;

import org.xml.sax.AttributeList;
import org.xml.sax.HandlerBase;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;

/**
 * An implementation of RMT2OrmDatasourceParser which uses the SAX1 parser
 * bundled with the JDK to read in and processes a XML document known as the ORM
 * Datasource configuration.
 * 
 * @author roy.terrell
 * 
 */
class RMT2Sax1OrmDatasourceParserImpl extends HandlerBase implements
        RMT2OrmDatasourceParser {

    private static Logger logger = Logger
            .getLogger(RMT2Sax1OrmDatasourceParserImpl.class);

    private ObjectMapperAttrib ds;

    private RMT2SaxAttributesBean attObj;

    private Hashtable selectStatement;

    private Hashtable colDefs;

    private Hashtable tables;

    private String dsName;

    private String orderByClause;

    private String whereClause;

    private int tableCount;

    private Hashtable elementAtts;

    private SAXParser parser;

    private Object obj;

    private String msg;

    private String text;

    private String docPath;

    private String docName;

    /**
     * Default constructor
     * 
     * @throws SystemException
     */
    private RMT2Sax1OrmDatasourceParserImpl() throws SystemException {
        logger.log(Level.DEBUG, "Creating RMT2Sax1OrmDatasourceParserImpl()");
        this.msg = null;
        // set Datasource's Document Type
        this.ds = new ObjectMapperAttrib();
        this.ds.setType("xml");
        this.attObj = new RMT2SaxAttributesBean();
        this.selectStatement = new Hashtable();
        this.colDefs = new Hashtable();
        this.tables = new Hashtable();
        this.tableCount = 0;
    }

    /**
     * Constructs a RMT2Sax1OrmDatasourceParserImpl object using a
     * RMT2TagQueryBean instance as the source for the ORM Datasource
     * configuration.
     * 
     * @param queryData
     *            Contains custom selection criteria and sequencing data to be
     *            applied to parsed DataSource view
     * @param docType
     *            The type of document to parse.
     * @throws SystemException
     *             General error.
     */
    protected RMT2Sax1OrmDatasourceParserImpl(RMT2TagQueryBean queryData)
            throws SystemException {
        this(queryData.getQuerySource());
        logger.log(Level.DEBUG,
                "Creating RMT2Sax1OrmDatasourceParserImpl(RMT2TagQueryBean, int)");
        this.whereClause = queryData.getWhereClause();
        this.orderByClause = queryData.getOrderByClause();
        this.ds.setWhereClause(this.whereClause);
        this.ds.setOrderByClause(this.orderByClause);
    }

    /**
     * Creates a RMT2Sax1OrmDatasourceParserImpl object that will initialize
     * SAX1 parser bundled with the JDK.
     * 
     * @param ormDsName
     *            The name of the ORM Datasource configuration XML Document
     * @throws SystemException
     *             Illegal access or resources, problem instatitating document,
     *             SAX driver cannot be found or general errors.
     */
    protected RMT2Sax1OrmDatasourceParserImpl(String ormDsName)
            throws SystemException {
        this();
        logger.log(Level.DEBUG,
                "Creating RMT2Sax1OrmDatasourceParserImpl(String");
        this.ds.setName(ormDsName);
        this.getResources();
        try {

            // Get Parser
            SAXParserFactory f = SAXParserFactory.newInstance();
            f.setValidating(false);
            this.parser = f.newSAXParser();
        } catch (Exception e) {
            throw new SystemException(e.getMessage());
        }
    }

    /**
     * Obtains the full direcotry path of the target ORM Datasource and assigns
     * it to the "docName" property.
     */
    public void getResources() {
        this.docName = OrmConfigHelper.getOrmDatasourceFullDirPath(this.ds
                .getName());
    }

    /**
     * Parses the ORM Datasource configuration XML document.
     * 
     * @return {@link bean.db.ObjectMapperAttrib ObjectMapperAttrib} a
     *         collection of data mappings pertaining to the ORM datasource.
     * @throws SystemException
     *             General parser errors.
     */
    public ObjectMapperAttrib parseDocument() throws SystemException {
        try {
            InputStream is = RMT2File.getFileInputStream(this.docName);
            this.parser.parse(is, this);
            return this.ds;
        } catch (Exception e) {
            String msg = "Problem parsing doc: "
                    + (this.docName == null ? "[ORM Datasource Configuration document name N/A]"
                            : this.docName);
            logger.error(msg);
            throw new SystemException(msg, e);
        }
    }

    /**
     * Logs the beginning of this method invocation
     */
    public void startDocument() throws SAXException {
        logger.log(Level.DEBUG, "startDocument is called:");
    }

    /**
     * Initializes the Name, SQL Select statement, column definitions and table
     * names of the ORM Datasource Object Attribute Mapper once the end of the
     * document is reach during parsing.
     */
    public void endDocument() throws SAXException {
        this.ds.setName(dsName); // set Datasource's Identity (Name)
        this.ds.setSelectStatement(this.selectStatement); // set Datasource's
                                                          // selectStatement
        this.ds.setColumnDef(this.colDefs); // set Datasource's Column
                                            // Definitions
        this.ds.setTables(this.tables); // set Datasource's Table List
        logger.log(Level.DEBUG, "endDocument at decendent is called:");
    }

    /**
     * Creates a Hashtable of RMT2SaxAttributesBean objects by transferring data
     * values(name, type, and value) from each hash element of <i>amap</i>.
     */
    public void startElement(String name, AttributeList amap)
            throws SAXException {
        logger.log(Level.DEBUG, "startElement is called: element name=" + name);

        this.text = null;
        try {
            this.elementAtts = new Hashtable();
            for (int i = 0; i < amap.getLength(); i++) {
                RMT2SaxAttributesBean att = new RMT2SaxAttributesBean();
                String elementName = amap.getName(i);
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
    private void buildTableElements(String elementName, AttributeList amap)
            throws SAXException {
        Hashtable atts = this.getElementAtts();
        int count = 0;

        // Obtain Datasource information
        if (elementName.equalsIgnoreCase("datasource")) {
            attObj = (RMT2SaxAttributesBean) atts.get(amap.getName(0));
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
    public void endElement(String name) throws SAXException {
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

    /** This method is called when warnings occur */
    public void warning(SAXParseException exception) {
        System.err.println("WARNING: line " + exception.getLineNumber() + ": "
                + exception.getMessage());
    }

    /** This method is called when errors occur */
    public void error(SAXParseException exception) {
        System.err.println("ERROR: line " + exception.getLineNumber() + ": "
                + exception.getMessage());
    }

    /** This method is called when non-recoverable errors occur. */
    public void fatalError(SAXParseException exception) throws SAXException {
        System.err.println("FATAL: line " + exception.getLineNumber() + ": "
                + exception.getMessage());
        throw (exception);
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
     * Get Element attributes
     * 
     * @return Hashtable of attribute name and values.
     */
    public Hashtable getElementAtts() {
        return this.elementAtts;
    }

}
