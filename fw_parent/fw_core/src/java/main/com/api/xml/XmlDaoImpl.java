package com.api.xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.xpath.XPathConstants;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.NotFoundException;
import com.RMT2Constants;
import com.SystemException;
import com.api.DaoApi;
import com.api.DaoApiStub;
import com.api.persistence.DatabaseException;

/**
 * This class implements the XmlDao interface which provides the ability to
 * query a XML document and to navigate the document bi-directionally. The idea
 * is to think of and manipulate the document as a table of rows and columns.
 * 
 * @author roy.terrell
 * 
 */
public class XmlDaoImpl extends DaoApiStub implements XmlDao {
    private static Logger logger = Logger.getLogger(XmlDaoImpl.class);

    protected InputSource xmlInSrc;

    protected NodeList nodes;

    protected NodeList curRowChildNodes;

    protected Node curRowNode;

    protected int curRow;

    protected String doc;

    /**
     * Creates a XmlDaoImpl object using a XML document.
     * <p>
     * The xml document, which can be of type String or Node, is converted to a
     * org.xml.sax.InputSource for the purpose of applying XPath expressions to
     * query the document. When <i>doc</i> is of type Node, it is converted to a
     * XML String.
     * 
     * @param doc
     *            The XML document to process. Must be of type String or Node.
     * @throws SystemException
     *             When xmlDoc is null, an invalid data type, or empty.
     */
    public XmlDaoImpl(Object doc) throws SystemException {
        if (doc == null) {
            this.msg = "The input XML document is invalid or null";
            logger.log(Level.ERROR, this.msg);
            throw new SystemException(this.msg);
        }
        boolean isString = doc instanceof String;
        boolean isNode = doc instanceof Node;
        if (!isString && !isNode) {
            this.msg = "The input XML document must exist as either a String or a Node";
            logger.log(Level.ERROR, this.msg);
            throw new SystemException(this.msg);
        }
        String xmlDoc = null;
        if (isString) {
            xmlDoc = doc.toString();
        }
        if (isNode) {
            xmlDoc = RMT2XmlUtility.transformNodeToString((Node) doc);
        }
        if (xmlDoc.length() <= 0) {
            this.msg = "The XML document input source does not contain any content";
            logger.log(Level.ERROR, this.msg);
            throw new SystemException(this.msg);
        }
        this.doc = xmlDoc;
        this.xmlInSrc = (InputSource) this.getConnector();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApiStub#close()
     */
    @Override
    public void close() throws DatabaseException {
        this.xmlInSrc = null;
        this.nodes = null;
        this.curRowChildNodes = null;
        ;
        this.curRowNode = null;
        this.doc = null;
        super.close();
    }

    /**
     * Gets the the XML document as an InputSource.
     * 
     * @return An org.xml.sax.InputSource instance as an arbitrary object.
     */
    public Object getConnector() {
        try {
            return RMT2XmlUtility.createXmlDocument(this.doc);
        } catch (SystemException e) {
            XmlDaoImpl.logger.log(Level.ERROR, e.getMessage());
            return null;
        }

    }

    /**
     * Sets the input source that represents the xml document.
     * 
     * @param xml
     *            org.xml.sax.InputSource as an arbitrary object.
     */
    public void setConnector(Object xml) {
        if (xml == null) {
            this.xmlInSrc = null;
        }
        boolean isInputSource = xml instanceof InputSource;
        if (!isInputSource) {
            this.xmlInSrc = null;
        }
    }

    /**
     * Fetches one or more rows pertaining to the specified query. The fetched
     * rows will exist as NodeList collection.
     * 
     * @param query
     *            An XPath expression.
     * @return An array Object where the first element an Integer object
     *         representing the number of rows fetched and the second element
     *         contains the NodeList.
     * @throws DatabaseException
     *             If query is not of type String or a problem occurs attempting
     *             to apply the XPath expression.
     */
    public Object[] retrieve(Object query) throws DatabaseException {
        Object result[];
        String xmlQuery;
        boolean isQueryValid = query instanceof String;
        if (isQueryValid) {
            // Valid String
            xmlQuery = query.toString();
        }
        else {
            this.msg = "Xpath Expression must be of type String";
            logger.log(Level.ERROR, this.msg);
            throw new DatabaseException(this.msg);
        }

        // Ensure that the expression is prefixed with the double front slash
        if (!xmlQuery.startsWith("//") && !xmlQuery.equals("/")) {
            xmlQuery = "//" + xmlQuery;
        }

        try {
            this.curRow = XmlDaoImpl.BOF;
            NodeList list = (NodeList) RMT2XmlUtility.executeXpathQuery(
                    xmlQuery, this.xmlInSrc, XPathConstants.NODESET);
            if (list.getLength() > 0) {
                result = new Object[2];
                result[0] = new Integer(list.getLength());
                result[1] = list;
                this.nodes = list;

            }
            else {
                // XPath query did not return any results...not found
                result = new Object[1];
                result[0] = new Integer(0);
                this.nodes = null;
            }
            return result;
        } catch (SystemException e) {
            this.msg = e.getMessage();
            logger.log(Level.ERROR, this.msg);
            throw new DatabaseException(
                    "Error retrieving data for XPath expression, " + xmlQuery,
                    e);
        }
    }

    /**
     * Positions the result set to the first node.
     * 
     * @return True when successful. Otherwise, false is returned.
     * @throws DatabaseException
     * @throws SystemException
     */
    public boolean firstRow() throws DatabaseException, SystemException {
        // No need to continue if result set is not valid.
        if (!this.isValidResultSet()) {
            return false;
        }

        boolean rowFound = false;
        this.curRow = 0;
        this.curRowNode = this.nodes.item(this.curRow);
        this.curRowChildNodes = this.curRowNode.getChildNodes();
        rowFound = true;

        return rowFound;
    }

    /**
     * Positions the result set to the last node.
     * 
     * @return True when successful. Otherwise, false is returned.
     * @throws DatabaseException
     * @throws SystemException
     */
    public boolean lastRow() throws DatabaseException, SystemException {
        // No need to continue if result set is not valid.
        if (!this.isValidResultSet()) {
            return false;
        }

        boolean rowFound = false;
        this.curRow = (this.nodes.getLength() - 1);
        this.curRowNode = this.nodes.item(this.curRow);
        this.curRowChildNodes = this.curRowNode.getChildNodes();
        rowFound = true;
        return rowFound;
    }

    /**
     * Positions the result set to the next available node.
     * 
     * @return True when successful. Otherwise, false is returned.
     * @throws DatabaseException
     * @throws SystemException
     */
    public boolean nextRow() throws DatabaseException, SystemException {
        boolean rowFound = false;

        // No need to continue if result set is not valid.
        if (!this.isValidResultSet()) {
            return false;
        }
        // We cannot navigate to the next node when the end-of-file point has
        // been reached.
        if ((this.curRow) == DaoApi.EOF) {
            return false;
        }
        // The current cursor position cannot be greater than the total number
        // of nodes.
        if ((this.curRow + 1) == this.nodes.getLength()) {
            // Position cursor to the end of file mark
            this.curRow = DaoApi.EOF;
            return false;
        }

        // In the event the cursor is pointing to BOF, set cursor position to a
        // point
        // where we can navigate to the first record.
        if (this.curRow == DaoApi.BOF) {
            this.curRow = -1;
        }

        // At this point it is okay to move to the next row
        this.curRow++;
        this.curRowNode = this.nodes.item(this.curRow);
        this.curRowChildNodes = this.curRowNode.getChildNodes();
        rowFound = true;

        return rowFound;
    }

    /**
     * Positions the result set to previous available node.
     * 
     * @return True when successful. Otherwise, false is returned.
     * @throws DatabaseException
     * @throws SystemException
     */
    public boolean previousRow() throws DatabaseException, SystemException {
        boolean rowFound = false;

        // No need to continue if result set is not valid.
        if (!this.isValidResultSet()) {
            return false;
        }
        // We cannot navigate to the previous node when the begin-of-file point
        // has been reached.
        if ((this.curRow) == DaoApi.BOF) {
            return false;
        }
        // The current cursor position cannot be the first element.
        if ((this.curRow - 1) == -1) {
            // Position the cursor to begin-of-file mark
            this.curRow = DaoApi.BOF;
            return false;
        }

        // In the event the cursor is pointing to EOF, set cursor position to a
        // point
        // where we can navigate to the last record.
        if (this.curRow == DaoApi.EOF) {
            this.curRow = this.nodes.getLength();
        }

        // At this point it is okay to move to the previous row
        this.curRow--;
        this.curRowNode = this.nodes.item(this.curRow);
        this.curRowChildNodes = this.curRowNode.getChildNodes();
        rowFound = true;

        return rowFound;
    }

    /**
     * Verifies whether or not the XPath query yielded a result set. The query
     * should have either returned a single node or a list of nodes.
     * 
     * @return false when result set is null or empty. Otherwise, true is
     *         returned.
     */
    private boolean isValidResultSet() {
        if (this.nodes == null) {
            return false;
        }
        if (this.nodes.getLength() <= 0) {
            return false;
        }
        return true;
    }

    /**
     * Fetches the value of the specified child XML element.
     * <p>
     * Prior to executing this method, an XPath query should have been performed
     * and as a result, positioned the cursor at <i>element</i>'s parent node.
     * This will provide this method the capability to traverse the parent's
     * child nodes in search of <i>element</i>.
     * 
     * @param element
     *            The name of the XML element for which to obtain its value.
     * @return The single value of <i>element</i> or XML if <i>element</i> exist
     *         as a sub-document. Returns null when element is not found
     * @throws DatabaseException
     *             Result set is positioned either at BOF or EOF.
     * @throws NotFoundException
     *             Element does not exist in the result set produced from the
     *             current XPath expression.
     * @throws SystemException
     *             N/A
     */
    public String getColumnValue(String element) throws DatabaseException,
            NotFoundException, SystemException {
        if (this.curRow == DaoApi.BOF || this.curRow == DaoApi.EOF) {
            this.msg = "Result set cursor needs to be positioned using one of the navigator methods: nextRow, previousRow, firstRow, or lastRow";
            XmlDaoImpl.logger.log(Level.ERROR, this.msg);
            throw new DatabaseException(this.msg);
        }
        String val = this.getColumnValue(element, this.curRowNode);
        if (val == null) {
            throw new NotFoundException("XML element was not found: " + element);
        }
        return val;

    }

    /**
     * Fetches the value of an XML element from its parent node.
     * <p>
     * The parent node is traversed until either the target element is found or
     * the list of child nodes is exhausted.
     * 
     * @param element
     *            The name of the XML element for which to obtain its value.
     * @param node
     *            The parent XML node to search
     * @return The XML element's value when found. Otherwise, null is returned.
     */
    protected String getColumnValue(String element, Node node) {
        // do i have a child node to handle?
        if (node.hasChildNodes()) {
            int chileNodesCount = node.getChildNodes().getLength();
            for (int i = 0; i < chileNodesCount; i++) {
                Node curNode = node.getChildNodes().item(i);
                if (curNode.getNodeName().equalsIgnoreCase(element)) {
                    StringBuffer buf = new StringBuffer();
                    if (curNode.hasChildNodes()) {
                        int subChildNodesCount = curNode.getChildNodes()
                                .getLength();
                        for (int ndx = 0; ndx < subChildNodesCount; ndx++) {
                            buf.append(this.getColumnValue(curNode
                                    .getChildNodes().item(ndx)));
                        }
                    }
                    return buf.toString();
                }
                // else {
                // return this.getColumnValue(element, curNode);
                // }
            }
        }
        return null;
    }

    /**
     * Returns the contents of a XML node.
     * <p>
     * This method basically transforms the XML element's value as either a
     * simple text value or a XML document representing the list of child nodes
     * belonging to <i>node</i>.
     * 
     * @param node
     *            the XML element to obtain the contents of.
     * @return The contnets of the <i>node</i> which is usually text. This text
     *         value can be either simple content or a list of XML child nodes
     *         in the form of a XML document.
     */
    protected String getColumnValue(Node node) {
        if (node.getNodeName() != null) {
            if (node.getNodeName().equals("#text")) {
                return node.getTextContent();
            }

            StringBuffer nodeString = new StringBuffer();
            nodeString.append("<" + node.getNodeName() + ">");

            // do i have a child node to handle?
            if (node.hasChildNodes()) {
                for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                    nodeString.append(this.getColumnValue(node.getChildNodes()
                            .item(i)));
                }
            }
            if (!node.getNodeName().equals("#text")) {
                nodeString.append("</" + node.getNodeName() + ">");
            }
            return nodeString.toString();
        }
        return null;
    }

    /**
     * Retrieve the name of the current node.
     * 
     * @return String as the node name
     * @throws DatabaseException
     *             Resultset is not positioned on any given node.
     */
    public String getCurrentNodeName() throws DatabaseException {
        if (this.curRow == DaoApi.BOF || this.curRow == DaoApi.EOF) {
            this.msg = "Result set is not positioned on a node in order to obtain node name";
            XmlDaoImpl.logger.log(Level.ERROR, this.msg);
            throw new DatabaseException(this.msg);
        }

        // If element is the current node, convert
        // node to XML String and return to caller.
        if (this.curRowNode != null) {
            return this.curRowNode.getNodeName();
        }
        return null;
    }

    /**
     * Verifies whether or not the current node has children.
     * 
     * @return true if children exist and false otherwise.
     * @throws DatabaseException
     *             Resultset is not positioned on any given node.
     */
    public boolean hasChildren() throws DatabaseException {
        if (this.curRow == DaoApi.BOF || this.curRow == DaoApi.EOF) {
            this.msg = "There is no current node to determine the existence of children";
            XmlDaoImpl.logger.log(Level.ERROR, this.msg);
            throw new DatabaseException(this.msg);
        }
        return this.curRowNode.hasChildNodes();
    }

    /**
     * Verfifies whether or not a node identified as <i>element</i> has
     * children.
     * 
     * @param element
     *            The name of the node to target.
     * @return true if children exist and false otherwise.
     * @throws DatabaseException
     *             Resultset is not positioned on any given node.
     * @throws NotFoundException
     *             Node is not found as <i>element</i>
     */
    public boolean hasChildren(String element) throws DatabaseException,
            NotFoundException {
        if (this.curRow == DaoApi.BOF || this.curRow == DaoApi.EOF) {
            this.msg = "Result set is not positioned on a node to determine the existence of children";
            XmlDaoImpl.logger.log(Level.ERROR, this.msg);
            throw new DatabaseException(this.msg);
        }

        // If element is the current node, convert node to XML String and return
        // to caller.
        if (element.equalsIgnoreCase(this.curRowNode.getNodeName())) {
            return this.curRowNode.hasChildNodes();
        }

        // // Search for element in the child nodes.
        // NodeDataSource nodeDs = XmlApiFactory
        // .createNodeDataSource(this.curRowChildNodes);
        // Object node = nodeDs.getNode(element);
        // if (node == null) {
        // this.msg = "Node not found for element, " + element;
        // XmlDaoImpl.logger.log(Level.ERROR, this.msg);
        // throw new NotFoundException(this.msg);
        // }
        // if (node instanceof Node) {
        // return ((Node) node).hasChildNodes();
        // }
        return false;
    }

    /**
     * Obtain the names of all children that exist for a given node.
     * 
     * @param element
     *            The name of the node to target.
     * @return List of Strings representing the names of each child node in
     *         ascending order.
     * @throws DatabaseException
     *             Resultset is not positioned on any given node.
     * @throws NotFoundException
     *             Node is not found as <i>element</i>
     */
    public List getChildrenNames(String element) throws DatabaseException,
            NotFoundException {
        if (this.curRow == DaoApi.BOF || this.curRow == DaoApi.EOF) {
            this.msg = "Result set is not positioned on a node to get the names of its children";
            XmlDaoImpl.logger.log(Level.ERROR, this.msg);
            throw new DatabaseException(this.msg);
        }

        NodeList children = null;

        // If element is the current node, get its node list
        if (element.equalsIgnoreCase(this.curRowNode.getNodeName())) {
            children = this.curRowNode.getChildNodes();
            return this.createUniqueNameList(children);
        }
        return null;
    }

    /**
     * Create list of unique element names and sort in ascending order.
     * 
     * @param children
     * @return
     */
    protected List createUniqueNameList(NodeList children) {
        List nameList = new ArrayList();
        Map nameMap = new TreeMap();
        for (int ndx = 0; ndx < children.getLength(); ndx++) {
            String name = children.item(ndx).getNodeName();
            nameMap.put(name, name);
        }
        if (!nameMap.isEmpty()) {
            Set keySet = nameMap.keySet();
            nameList = Arrays.asList(keySet.toArray());
        }
        return nameList;
    }

    /**
     * Not implemented.
     * 
     * @param query
     * @return null
     * @throws DatabaseException
     * @throws SystemException
     */
    public String executeXmlQuery(String query) throws DatabaseException,
            SystemException {
        return null;
    }

    /**
     * Not implemented.
     * 
     * @param query
     * @returnnull
     * @throws DatabaseException
     */
    public Object[] findData(Object query) throws DatabaseException {
        return null;
    }

    /**
     * Not implemented.
     * 
     * @param element
     * @param value
     * @throws SystemException
     * @throws NotFoundException
     * @throws DatabaseException
     */
    public void setColumnValue(String element, Object value)
            throws SystemException, NotFoundException, DatabaseException {
        return;
    }

    /**
     * Not implemented.
     * 
     * @param _obj
     * @param _autoKey
     * @return zero
     * @throws DatabaseException
     */
    public int insertRow(Object _obj, boolean _autoKey)
            throws DatabaseException {
        return 0;
    }

    /**
     * Not implemented.
     * 
     * @param _obj
     * @return zero.
     * @throws DatabaseException
     */
    public int updateRow(Object _obj) throws DatabaseException {
        return 0;
    }

    /**
     * Not implemented.
     * 
     * @param _obj
     * @return zero.
     * @throws DatabaseException
     */
    public int deleteRow(Object _obj) throws DatabaseException {
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#getColumnBinaryValue(java.lang.String)
     */
    public InputStream getColumnBinaryValue(String property)
            throws DatabaseException, NotFoundException, SystemException {
        throw new UnsupportedOperationException(
                RMT2Constants.MSG_METHOD_NOT_SUPPORTED);
    }

} // end class

