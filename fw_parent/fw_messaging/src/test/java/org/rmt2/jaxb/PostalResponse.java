//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.04.01 at 08:13:01 PM CDT 
//


package org.rmt2.jaxb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.RMT2Base;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="header" type="{}header_type"/>
 *         &lt;element name="reply_status" type="{}reply_status_type" minOccurs="0"/>
 *         &lt;element name="zip_short" type="{}zipcode_type" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="zip_full" type="{}zipcode_full_type" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="countries" type="{}country_type" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="states" type="{}state_type" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ip_data" type="{}ip_details"/>
 *         &lt;element name="timezones" type="{}timezone_type" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="pagination_info" type="{}pagination_type"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "header",
    "replyStatus",
    "zipShort",
    "zipFull",
    "countries",
    "states",
    "ipData",
    "timezones",
    "paginationInfo"
})
@XmlRootElement(name = "PostalResponse")
public class PostalResponse
    extends RMT2Base
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected HeaderType header;
    @XmlElement(name = "reply_status")
    protected ReplyStatusType replyStatus;
    @XmlElement(name = "zip_short", nillable = true)
    protected List<ZipcodeType> zipShort;
    @XmlElement(name = "zip_full", nillable = true)
    protected List<ZipcodeFullType> zipFull;
    @XmlElement(nillable = true)
    protected List<CountryType> countries;
    @XmlElement(nillable = true)
    protected List<StateType> states;
    @XmlElement(name = "ip_data", required = true, nillable = true)
    protected IpDetails ipData;
    @XmlElement(nillable = true)
    protected List<TimezoneType> timezones;
    @XmlElement(name = "pagination_info", required = true)
    protected PaginationType paginationInfo;

    /**
     * Gets the value of the header property.
     * 
     * @return
     *     possible object is
     *     {@link HeaderType }
     *     
     */
    public HeaderType getHeader() {
        return header;
    }

    /**
     * Sets the value of the header property.
     * 
     * @param value
     *     allowed object is
     *     {@link HeaderType }
     *     
     */
    public void setHeader(HeaderType value) {
        this.header = value;
    }

    /**
     * Gets the value of the replyStatus property.
     * 
     * @return
     *     possible object is
     *     {@link ReplyStatusType }
     *     
     */
    public ReplyStatusType getReplyStatus() {
        return replyStatus;
    }

    /**
     * Sets the value of the replyStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReplyStatusType }
     *     
     */
    public void setReplyStatus(ReplyStatusType value) {
        this.replyStatus = value;
    }

    /**
     * Gets the value of the zipShort property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the zipShort property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getZipShort().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ZipcodeType }
     * 
     * 
     */
    public List<ZipcodeType> getZipShort() {
        if (zipShort == null) {
            zipShort = new ArrayList<ZipcodeType>();
        }
        return this.zipShort;
    }

    /**
     * Gets the value of the zipFull property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the zipFull property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getZipFull().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ZipcodeFullType }
     * 
     * 
     */
    public List<ZipcodeFullType> getZipFull() {
        if (zipFull == null) {
            zipFull = new ArrayList<ZipcodeFullType>();
        }
        return this.zipFull;
    }

    /**
     * Gets the value of the countries property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the countries property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCountries().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CountryType }
     * 
     * 
     */
    public List<CountryType> getCountries() {
        if (countries == null) {
            countries = new ArrayList<CountryType>();
        }
        return this.countries;
    }

    /**
     * Gets the value of the states property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the states property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStates().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StateType }
     * 
     * 
     */
    public List<StateType> getStates() {
        if (states == null) {
            states = new ArrayList<StateType>();
        }
        return this.states;
    }

    /**
     * Gets the value of the ipData property.
     * 
     * @return
     *     possible object is
     *     {@link IpDetails }
     *     
     */
    public IpDetails getIpData() {
        return ipData;
    }

    /**
     * Sets the value of the ipData property.
     * 
     * @param value
     *     allowed object is
     *     {@link IpDetails }
     *     
     */
    public void setIpData(IpDetails value) {
        this.ipData = value;
    }

    /**
     * Gets the value of the timezones property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the timezones property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTimezones().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TimezoneType }
     * 
     * 
     */
    public List<TimezoneType> getTimezones() {
        if (timezones == null) {
            timezones = new ArrayList<TimezoneType>();
        }
        return this.timezones;
    }

    /**
     * Gets the value of the paginationInfo property.
     * 
     * @return
     *     possible object is
     *     {@link PaginationType }
     *     
     */
    public PaginationType getPaginationInfo() {
        return paginationInfo;
    }

    /**
     * Sets the value of the paginationInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link PaginationType }
     *     
     */
    public void setPaginationInfo(PaginationType value) {
        this.paginationInfo = value;
    }

}
