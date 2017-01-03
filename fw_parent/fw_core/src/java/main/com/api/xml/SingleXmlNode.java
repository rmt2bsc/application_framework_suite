package com.api.xml;

import com.SystemException;
import com.api.CommonQueryApi;

/**
 * Interface for manipulating single node XML documents.
 * 
 * @author RTerrell
 * 
 */
public interface SingleXmlNode extends CommonQueryApi {

    /**
     * Locates and returns an XML node based on the its node name,
     * <i>element</i>.
     * 
     * @param element
     *            The name of the XML tag to retrieve node.
     * @return org.w3c.dom.Node
     * @throws SystemException
     */
    Object getNode(Object element) throws SystemException;
}
