package testcases.ldap.beans;

/**
 * @author rterrell
 * 
 */
public class TestLdapCommonComputer extends TestLdapCommonEntity {

    private String env;

    private String hn;

    private String ipServicePort;

    private String isStaticIp;

    private String ou;

    private String protocolInformation;

    private String hostName;

    private String ipHostNumber;

    private String macAddress;

    private String uid;

    public TestLdapCommonComputer() {
        super();
    }

    /**
     * @return the env
     */
    public String getEnv() {
        return env;
    }

    /**
     * @param env
     *            the env to set
     */
    public void setEnv(String env) {
        this.env = env;
    }

    /**
     * @return the hn
     */
    public String getHn() {
        return hn;
    }

    /**
     * @param hn
     *            the hn to set
     */
    public void setHn(String hn) {
        this.hn = hn;
    }

    /**
     * @return the ipServicePort
     */
    public String getIpServicePort() {
        return ipServicePort;
    }

    /**
     * @param ipServicePort
     *            the ipServicePort to set
     */
    public void setIpServicePort(String ipServicePort) {
        this.ipServicePort = ipServicePort;
    }

    /**
     * @return the isStaticIp
     */
    public String getIsStaticIp() {
        return isStaticIp;
    }

    /**
     * @param isStaticIp
     *            the isStaticIp to set
     */
    public void setIsStaticIp(String isStaticIp) {
        this.isStaticIp = isStaticIp;
    }

    /**
     * @return the ou
     */
    public String getOu() {
        return ou;
    }

    /**
     * @param ou
     *            the ou to set
     */
    public void setOu(String ou) {
        this.ou = ou;
    }

    /**
     * @return the protocolInformation
     */
    public String getProtocolInformation() {
        return protocolInformation;
    }

    /**
     * @param protocolInformation
     *            the protocolInformation to set
     */
    public void setProtocolInformation(String protocolInformation) {
        this.protocolInformation = protocolInformation;
    }

    /**
     * @return the hostName
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * @param hostName
     *            the hostName to set
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     * @return the ipHostNumber
     */
    public String getIpHostNumber() {
        return ipHostNumber;
    }

    /**
     * @param ipHostNumber
     *            the ipHostNumber to set
     */
    public void setIpHostNumber(String ipHostNumber) {
        this.ipHostNumber = ipHostNumber;
    }

    /**
     * @return the macAddress
     */
    public String getMacAddress() {
        return macAddress;
    }

    /**
     * @param macAddress
     *            the macAddress to set
     */
    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    /**
     * @return the uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * @param uid
     *            the uid to set
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

}
