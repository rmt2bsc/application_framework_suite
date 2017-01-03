package com.api.xml.adapters;

import com.RMT2Base;
import com.api.xml.adapters.NativeAdapterImpl;
import com.api.xml.adapters.XmlBeanAdapter;

/**
 * Factory designed to create new XML adapter related class instances.
 * 
 * @author roy.terrell
 * 
 */
public class XmlAdapterFactory extends RMT2Base {

    /**
     * Creates an instance of XmlBeanAdapter using the native implementation.
     * 
     * @return {@link com.api.xml.adapters.XmlBeanAdapter XmlBeanAdapter}
     */
    public static XmlBeanAdapter createNativeAdapter() {
        return new NativeAdapterImpl();
    }

    /**
     * Creates an instance of XmlBeanAdapter using the an XQuery implementation.
     * 
     * @return @return {@link com.api.xml.adapters.XmlBeanAdapter
     *         XmlBeanAdapter}
     */
    public static XmlBeanAdapter createXQueryAdapter() {
        return new XQueryAdapterImpl();
    }
}
