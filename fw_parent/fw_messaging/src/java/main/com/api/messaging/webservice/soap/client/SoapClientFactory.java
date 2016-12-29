package com.api.messaging.webservice.soap.client;

import java.util.Map;

import javax.xml.soap.SOAPMessage;

import com.api.config.ConfigConstants;
import com.api.config.old.ProviderConfig;
import com.api.messaging.MessagingResourceFactory;

/**
 * A factory for creating objects related to the SOAP Api.
 * 
 * @author appdev
 * 
 */
public class SoapClientFactory extends MessagingResourceFactory {

    /**
     * 
     */
    public SoapClientFactory() {
        return;
    }

    /**
     * Obtains the ProviderConfig instance for the SOAP processor. The only
     * property set in the ProviderConfig instance will be the SOAP Host. The
     * SOAP Host should be configured as the System property and can be fetched
     * as {@link com.api.config.PropertyFileSystemResourceConfigImpl.SOAP_HOST
     * SOAP_HOST}
     * 
     * @return {@link com.api.messaging.ProviderConfig ProviderConfig}
     */
    public static ProviderConfig getSoapConfigInstance() {
        ProviderConfig config = MessagingResourceFactory.getConfigInstance();
        String soapHost = System.getProperty(ConfigConstants.SOAP_HOST);
        config.setHost(soapHost);
        return config;
    }

    /**
     * Create an instance of SoapClient using the SOAP messaging transport
     * implementation.
     * 
     * @return {@link com.api.messaging.webservice.soap.client.SoapClient
     *         SoapClient}
     */
    public static final SoapClient getClient() {
        try {
            SoapClient api = new SoapTransportImpl();
            return api;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Create an instance of SoapProductBuilderImpl requiring the user to
     * customize the construction for the SOAP message using its various
     * methods.
     */
    public static final SoapProductBuilder getSoapBuilderInstance() {
        return new SoapProductBuilderImpl();
    }

    /**
     * Create an instance of SoapProductBuilderImpl that will be used to
     * disassemble a SOAPMessage instance to a String.
     * 
     * @param message
     *            SOAPMessage instance to disassemble.
     */
    public static final SoapProductBuilder getSoapBuilderInstance(
            SOAPMessage message) {
        return new SoapProductBuilderImpl(message);
    }

    /**
     * Create an instance of SoapProductBuilderImpl that will be used to
     * assemble a SOAPMessage instance from a String SOAP message.
     * 
     * @param message
     *            String representation of the a SOAP message.
     */
    public static final SoapProductBuilder getSoapBuilderInstance(String message) {
        return new SoapProductBuilderImpl(message);
    }

    /**
     * Create an instance of SoapProductBuilderImpl intended for assembling a
     * RMT2 related or generic SOAP message with normal body. The product of the
     * assembly will be an instance of SOAPMessage.
     * 
     * @param serviceId
     *            the id of the service that will process the message. This
     *            parameter is required and is typically used when the assembly
     *            of a RMT2 relate message is desired.
     * @param queueName
     *            the name of the JMS queue where the message should be sent.
     *            This parameter is optional and is typically used when the
     *            assembly of a RMT2 relate message is desired.
     * @param body
     *            the data that will represent the SOAP message's envelope body.
     *            This parameter is required.
     */
    public static final SoapProductBuilder getSoapBuilderInstance(
            String serviceId, String queueName, String body) {
        return new SoapProductBuilderImpl(serviceId, queueName, body);
    }

    /**
     * Create an instance of SoapProductBuilderImpl intended for assembling a
     * RMT2 related or generic SOAP Fault message. The product of the assembly
     * will be an instance of SOAPMessage.
     * 
     * @param serviceId
     *            the id of the service that will process the message. This
     *            parameter is optional and is typically used when the assembly
     *            of a RMT2 relate message is desired.
     * @param queueName
     *            the name of the JMS queue where the message should be sent.
     *            This parameter is optional and is typically used when the
     *            assembly of a RMT2 relate message is desired.
     * @param faultCode
     *            the code indicating the cause of the SOAP fault and will
     *            appear as a subelement of the Fault element. Valid fault code
     *            values are <i>VerisionMismatch</i>, <i>MustUnderstand</i>,
     *            <i>Client</i>, and <i>Server</i>. This paramter is required.
     * @param faultMessage
     *            a human readable description of the fault element. This
     *            parameter is required due to it must appear as a subelement of
     *            the Fault element.
     * @param faultActor
     *            this is an indication of the system tht was responsible for
     *            the fault. This parameter is optional.
     * @param faultDetails
     *            a Map containing the key/value pairs representing the
     *            additional fault details. The key is considered to be the
     *            element name and the value is considered to be the value.
     * 
     */
    public static final SoapProductBuilder getSoapBuilderInstance(
            String serviceId, String queueName, String faultCode,
            String faultMessage, String faultActor,
            Map<String, String> faultDetails) {
        return new SoapProductBuilderImpl(serviceId, queueName, faultCode,
                faultMessage, faultActor, faultDetails);
    }

}
