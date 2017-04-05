package com.api.messaging.webservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.RMT2Exception;
import com.api.ldap.LdapClient;
import com.api.ldap.LdapFactory;
import com.api.ldap.beans.LdapWebServiceConfig;
import com.api.ldap.operation.LdapSearchOperation;
import com.api.messaging.MessageRoutingInfo;

/**
 * A JNDI implementation of the ServiceRegistry interface where the service
 * entries are loaded from a Directory Service using the LDAP protocol.
 * 
 * @author Roy Terrell
 * @deprecated Use the {@link SystemConfiguratorServiceRegistryLoaderImpl} class
 *             since service registry data is obtained from the
 *             SystemConfigurator object.
 * 
 */
class LdapServiceRegistryLoaderImpl extends AbstractServiceRegistryImpl {

    private static Logger logger = Logger.getLogger(LdapServiceRegistryLoaderImpl.class);

    private String ldapMapperClass;

    private String ldapBaseDn;

    /**
     * Create a LdapServiceRegistryLoaderImpl containing an uninitialized
     * service registry.
     */
    LdapServiceRegistryLoaderImpl(String baseDn, String mapperClass) {
        super();
        ldapBaseDn = baseDn;
        ldapMapperClass = mapperClass;
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.messaging.webservice.ServiceRegistry#loadServices()
     */
    @Override
    public Map<String, MessageRoutingInfo> loadServices() {
        if (AbstractServiceRegistryImpl.getServices() != null) {
            return AbstractServiceRegistryImpl.getServices();
        }
        List<LdapWebServiceConfig> services = this.fetchAvailableServices();

        // Create registry by adding processed services to the service registry
        Map<String, MessageRoutingInfo> reg = new HashMap<String, MessageRoutingInfo>();
        for (LdapWebServiceConfig item : services) {
            MessageRoutingInfo rec = new MessageRoutingInfo();
            rec.setCommand(item.getCn().get(0));
            rec.setDestination(item.getRequestUrl());
            // rec.setHandler(null);
            rec.setHost(item.getHost());
            rec.setMessageId(item.getCn().get(0));
            rec.setReplyMessageId(item.getReplyMsgId());
            rec.setRouterType(item.getRouterType());
            rec.setSecured(Boolean.parseBoolean(item.getIsSecured()));
            reg.put(rec.getMessageId(), rec);
        }
        // Assign static constant the registry.
        AbstractServiceRegistryImpl.setServices(reg);
        return reg;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.messaging.webservice.ServiceRegistry#loadServices(int)
     */
    @Override
    public Map<String, MessageRoutingInfo> loadServices(int rsrcSubTypeId) {
        // TODO Auto-generated method stub
        return null;
    }

    private List<LdapWebServiceConfig> fetchAvailableServices() {
        List<String> wsTypegList = null;
        List<LdapWebServiceConfig> srvc = new ArrayList<LdapWebServiceConfig>();
        try {
            wsTypegList = this.fetchAllWebServiceTypes();
            for (String wsType : wsTypegList) {
                List<LdapWebServiceConfig> results = this.fetchWebServices(wsType);
                if (results != null) {
                    srvc.addAll(results);
                }
            }
        } catch (RMT2Exception e) {
            e.printStackTrace();
        }
        return srvc;

    }

    private List<String> fetchAllWebServiceTypes() throws RMT2Exception {
        List<LdapWebServiceConfig> queryResults = null;
        LdapClient ldap = null;
        try {
            LdapFactory f = new LdapFactory();
            ldap = f.createAttributeClient(null);
            LdapSearchOperation op = new LdapSearchOperation();
            // ou=Web Services,ou=Resources,o=RMT2BSC,dc=rmt2,dc=net
            op.setDn(this.ldapBaseDn);
            op.setMappingBeanName(this.ldapMapperClass);
            Object results[] = ldap.retrieve(op);
            queryResults = ldap.extractLdapResults(results);
            if (queryResults == null) {
                return null;
            }
        } catch (Exception e) {
            throw new RMT2Exception(e);
        }

        List<String> list = new ArrayList<String>();
        for (LdapWebServiceConfig item : queryResults) {
            String name = item.getOu().get(0);
            list.add(name);
        }
        return list;
    }

    private List<LdapWebServiceConfig> fetchWebServices(String wsType) throws RMT2Exception {
        if (wsType == null) {
            return null;
        }
        logger.info("Fetching " + wsType + " web services...");
        String baseDn = "ou=" + wsType + "," + this.ldapBaseDn;
        List<LdapWebServiceConfig> queryResults = null;
        LdapClient ldap = null;

        try {
            LdapFactory f = new LdapFactory();
            ldap = f.createAttributeClient(null);
            LdapSearchOperation op = new LdapSearchOperation();
            op.setDn(baseDn);
            op.setUseSearchFilterExpression(false);
            op.setMappingBeanName(this.ldapMapperClass);
            Object results[] = ldap.retrieve(op);
            queryResults = ldap.extractLdapResults(results);
            if (queryResults == null) {
                return null;
            }
        } catch (Exception e) {
            throw new RMT2Exception(e);
        }

        // Update resource type and resource sub type properties.
        for (LdapWebServiceConfig item : queryResults) {
            item.setRsrcTypeName("WS");
            item.setRsrcSubTypeName(wsType);
            // item.setRouterType(wsType);
        }
        logger.info(queryResults + " entries fetched for web service type, " + wsType);
        return queryResults;
    }
}
