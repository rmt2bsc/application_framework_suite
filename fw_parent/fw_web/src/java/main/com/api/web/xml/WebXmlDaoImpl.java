package com.api.web.xml;

import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.NotFoundException;
import com.SystemException;
import com.api.persistence.DatabaseException;
import com.api.xml.XmlDaoImpl;

/**
 * This class implements the XmlDao interface which provides the ability to
 * query a XML document and to navigate the document bi-directionally. The idea
 * is to think of and manipulate the document as a table of rows and columns.
 * 
 * @author roy.terrell
 * 
 */
class WebXmlDaoImpl extends XmlDaoImpl {
    private static Logger logger = Logger.getLogger(WebXmlDaoImpl.class);

    /**
     * Creates a XmlDaoImpl object using a String formatted XML document. The
     * xml document, which is in String format, is converted to a
     * org.xml.sax.InputSource for the purpose of applying XPath expressions to
     * query the document.
     * 
     * @param The
     *            XML document to process. Must be of type String or Node.
     * @throws SystemException
     *             When xmlDoc is null, an invalid data type, or empty.
     */
    public WebXmlDaoImpl(Object obj) throws SystemException {
        super(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApiStub#close()
     */
    @Override
    public void close() throws DatabaseException {
        super.close();
    }

    /**
     * Extracts the value of the specified element from the child nodes of the
     * current row node.
     * 
     * @param element
     *            The element in the XML document to obtain value.
     * @return The single value of <i>element</i> or XML if <i>element</i> exist
     *         as a sub-document.
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

        String xml = super.getColumnValue(element);
        if (xml != null) {
            return xml;
        }

        // Search for element in the child nodes.
        NodeDataSource nodeDs = WebXmlApiFactory
                .createNodeDataSource(this.curRowChildNodes);
        Object value = nodeDs.get(element);
        if (value == null) {
            this.msg = "Value not found for element, " + element;
            WebXmlDaoImpl.logger.warn(this.msg);
            throw new NotFoundException(this.msg);
        }
        return value.toString();
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
        boolean results = super.hasChildren(element);
        if (results) {
            return results;
        }
        // Search for element in the child nodes.
        NodeDataSource nodeDs = WebXmlApiFactory
                .createNodeDataSource(this.curRowChildNodes);
        Object node = nodeDs.getNode(element);
        if (node == null) {
            this.msg = "Node not found for element, " + element;
            WebXmlDaoImpl.logger.log(Level.ERROR, this.msg);
            throw new NotFoundException(this.msg);
        }
        if (node instanceof Node) {
            return ((Node) node).hasChildNodes();
        }
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
        List results = super.getChildrenNames(element);
        if (results != null) {
            return results;
        }
        NodeList children = null;

        // Get node for said element and obtain its node list
        NodeDataSource nodeDs = WebXmlApiFactory
                .createNodeDataSource(this.curRowChildNodes);
        Object node = nodeDs.getNode(element);
        if (node == null) {
            this.msg = "Node not found for element, " + element;
            WebXmlDaoImpl.logger.log(Level.ERROR, this.msg);
            throw new NotFoundException(this.msg);
        }
        if (node instanceof Node) {
            children = ((Node) node).getChildNodes();
            return this.createUniqueNameList(children);
        }
        else {
            return null;
        }

    }

} // end class

