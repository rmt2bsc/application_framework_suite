package com.api.ldap.beans;

/**
 * ORM bean for mapping common computer related data coming from a LDAP server.
 * 
 * @author Roy Terrell
 * 
 */
public class LdapCommonComputer extends LdapResource {

    private String env;

    private String hn;

    private String ipServicePort;

    private String isStaticIp;

    private String protocolInformation;

    private String hostName;

    private String ipHostNumber;

    private String macAddress;

    private String computerTypeCatg;

    private String computerTypeCatgBoxDescr;

    /**
     * Creates a LdapCommonComputer object withou intializing any of its
     * properties.
     */
    public LdapCommonComputer() {
        super();
    }

    /**
     * Get the envionment which this computer is used such as DEV, TEST, or
     * PROD.
     * 
     * @return the env
     */
    public String getEnv() {
        return env;
    }

    /**
     * Set the envionment which this computer is used such as DEV, TEST, or
     * PROD.
     * 
     * @param env
     *            the env to set
     */
    public void setEnv(String env) {
        this.env = env;
    }

    /**
     * Get the host name.
     * 
     * @return String
     */
    public String getHn() {
        return hn;
    }

    /**
     * Set the host name.
     * 
     * @param hn
     *            String
     */
    public void setHn(String hn) {
        this.hn = hn;
    }

    /**
     * Get the host name.
     * 
     * @return String
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * Set the host name.
     * 
     * @param hostName
     *            String
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     * Get the port number which the computer services.
     * 
     * @return String
     */
    public String getIpServicePort() {
        return ipServicePort;
    }

    /**
     * Set the port number which the computer services.
     * 
     * @param ipServicePort
     *            String
     */
    public void setIpServicePort(String ipServicePort) {
        this.ipServicePort = ipServicePort;
    }

    /**
     * Get the flag which indicates whether or not this computer is bound by a
     * static IP address.
     * 
     * @return a String value of <i>true</i> when bound by a static IP or
     *         <i>false</i> otherwise.
     */
    public String getIsStaticIp() {
        return isStaticIp;
    }

    /**
     * Set the flag which indicates whether or not this computer is bound by a
     * static IP address.
     * 
     * @param isStaticIp
     *            a String value of <i>true</i> when bound by a static IP or
     *            <i>false</i> otherwise.
     */
    public void setIsStaticIp(String isStaticIp) {
        this.isStaticIp = isStaticIp;
    }

    /**
     * Get the protocol indicator such as http, ftp, jdbc, rmi, etc.
     * 
     * @return String
     */
    public String getProtocolInformation() {
        return protocolInformation;
    }

    /**
     * Set the protocol indicator such as http, ftp, jdbc, rmi, etc.
     * 
     * @param protocolInformation
     *            String
     */
    public void setProtocolInformation(String protocolInformation) {
        this.protocolInformation = protocolInformation;
    }

    /**
     * Get the IP address of this computer
     * 
     * @return a String as 4 octets separated by the period character
     */
    public String getIpHostNumber() {
        return ipHostNumber;
    }

    /**
     * Set the IP address of this computer
     * 
     * @param ipHostNumber
     *            a String as 4 octets separated by the period character
     */
    public void setIpHostNumber(String ipHostNumber) {
        this.ipHostNumber = ipHostNumber;
    }

    /**
     * Get the MAC address of this computer.
     * 
     * @return String
     */
    public String getMacAddress() {
        return macAddress;
    }

    /**
     * Set the MAC address of this computer
     * 
     * @param macAddress
     *            String
     */
    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    /**
     * Return the category name of the computer type which is also known as the
     * resource sub type
     * 
     * @return the subTypeCategory
     */
    public String getComputerTypeCatg() {
        return computerTypeCatg;
    }

    /**
     * Set the category name of the computer type which is also known as the
     * resource sub type
     * 
     * @param catg
     *            the name of the computer sub type category
     */
    public void setComputerTypeCatg(String catg) {
        this.computerTypeCatg = catg;
    }

    /**
     * Return the box description of one of the compurter resource sub type
     * category.
     * <p>
     * The box description should intuitively reflect the sub system it serves
     * and it's platform. For example, Accounting-Internet, which means this is
     * a web server for the accounting application.
     * 
     * @return the subTypeCatgBox
     */
    public String getComputerTypeCatgBoxDescr() {
        return this.computerTypeCatgBoxDescr;
    }

    /**
     * Set the box description of one of the compurter resource sub type
     * category.
     * <p>
     * The box description should intuitively reflect the sub system it serves
     * and it's platform. For example, RMT2-Internet, which means this is a web
     * server for the RMT2 application.
     * 
     * @param boxDescr
     *            the box description.
     */
    public void setComputerTypeCatgBoxDescr(String boxDescr) {
        this.computerTypeCatgBoxDescr = boxDescr;
    }

}
