package com.api.xml.adapters;

import com.SystemException;

/**
 * Interface for adapting XML documents to class instances and vice versa.
 * 
 * @author RTerrell
 * 
 */
public interface XmlBeanAdapter {

    /**
     * Converts a class instance to an XML document. Returns the XML document as
     * a String.
     * 
     * @param bean
     *            The target class instance.
     * @return String representation as a XML document.
     * @throws SystemException
     */
    String beanToXml(Object bean) throws SystemException;

    /**
     * Converts an XML document to an equivalent class instance.
     * 
     * @param xml
     *            The XML document
     * @return Class instance of <i>xml</i>
     * @throws SystemException
     */
    Object xmlToBean(String xml) throws SystemException;
}
