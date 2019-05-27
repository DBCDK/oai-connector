package org.openarchives.oai;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for OAI-PMHtype complex type.
 * 
 * <p>The following schema fragment specifies the expected         content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OAI-PMHtype"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="responseDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="request" type="{http://www.openarchives.org/OAI/2.0/}requestType"/&gt;
 *         &lt;choice&gt;
 *           &lt;element name="error" type="{http://www.openarchives.org/OAI/2.0/}OAI-PMHerrorType" maxOccurs="unbounded"/&gt;
 *           &lt;element name="Identify" type="{http://www.openarchives.org/OAI/2.0/}IdentifyType"/&gt;
 *           &lt;element name="ListMetadataFormats" type="{http://www.openarchives.org/OAI/2.0/}ListMetadataFormatsType"/&gt;
 *           &lt;element name="ListSets" type="{http://www.openarchives.org/OAI/2.0/}ListSetsType"/&gt;
 *           &lt;element name="GetRecord" type="{http://www.openarchives.org/OAI/2.0/}GetRecordType"/&gt;
 *           &lt;element name="ListIdentifiers" type="{http://www.openarchives.org/OAI/2.0/}ListIdentifiersType"/&gt;
 *           &lt;element name="ListRecords" type="{http://www.openarchives.org/OAI/2.0/}ListRecordsType"/&gt;
 *         &lt;/choice&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlRootElement 
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OAI-PMHtype", propOrder = {
    "responseDate",
    "request",
    "error",
    "identify",
    "listMetadataFormats",
    "listSets",
    "getRecord",
    "listIdentifiers",
    "listRecords"
})
public class OAIPMH {

    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar responseDate;
    @XmlElement(required = true)
    protected Request request;
    protected List<OAIPMHerror> error;
    @XmlElement(name = "Identify")
    protected Identify identify;
    @XmlElement(name = "ListMetadataFormats")
    protected ListMetadataFormats listMetadataFormats;
    @XmlElement(name = "ListSets")
    protected ListSets listSets;
    @XmlElement(name = "GetRecord")
    protected GetRecord getRecord;
    @XmlElement(name = "ListIdentifiers")
    protected ListIdentifiers listIdentifiers;
    @XmlElement(name = "ListRecords")
    protected ListRecords listRecords;

    /**
     * Gets the value of the responseDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getResponseDate() {
        return responseDate;
    }

    /**
     * Sets the value of the responseDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setResponseDate(XMLGregorianCalendar value) {
        this.responseDate = value;
    }

    /**
     * Gets the value of the request property.
     * 
     * @return
     *     possible object is
     *     {@link Request }
     *     
     */
    public Request getRequest() {
        return request;
    }

    /**
     * Sets the value of the request property.
     * 
     * @param value
     *     allowed object is
     *     {@link Request }
     *     
     */
    public void setRequest(Request value) {
        this.request = value;
    }

    /**
     * Gets the value of the error property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the error property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getError().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OAIPMHerror }
     * 
     * 
     */
    public List<OAIPMHerror> getError() {
        if (error == null) {
            error = new ArrayList<OAIPMHerror>();
        }
        return this.error;
    }

    /**
     * Gets the value of the identify property.
     * 
     * @return
     *     possible object is
     *     {@link Identify }
     *     
     */
    public Identify getIdentify() {
        return identify;
    }

    /**
     * Sets the value of the identify property.
     * 
     * @param value
     *     allowed object is
     *     {@link Identify }
     *     
     */
    public void setIdentify(Identify value) {
        this.identify = value;
    }

    /**
     * Gets the value of the listMetadataFormats property.
     * 
     * @return
     *     possible object is
     *     {@link ListMetadataFormats }
     *     
     */
    public ListMetadataFormats getListMetadataFormats() {
        return listMetadataFormats;
    }

    /**
     * Sets the value of the listMetadataFormats property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListMetadataFormats }
     *     
     */
    public void setListMetadataFormats(ListMetadataFormats value) {
        this.listMetadataFormats = value;
    }

    /**
     * Gets the value of the listSets property.
     * 
     * @return
     *     possible object is
     *     {@link ListSets }
     *     
     */
    public ListSets getListSets() {
        return listSets;
    }

    /**
     * Sets the value of the listSets property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListSets }
     *     
     */
    public void setListSets(ListSets value) {
        this.listSets = value;
    }

    /**
     * Gets the value of the getRecords property.
     * 
     * @return
     *     possible object is
     *     {@link GetRecord }
     *     
     */
    public GetRecord getGetRecord() {
        return getRecord;
    }

    /**
     * Sets the value of the getRecords property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetRecord }
     *     
     */
    public void setGetRecord(GetRecord value) {
        this.getRecord = value;
    }

    /**
     * Gets the value of the listIdentifiers property.
     * 
     * @return
     *     possible object is
     *     {@link ListIdentifiers }
     *     
     */
    public ListIdentifiers getListIdentifiers() {
        return listIdentifiers;
    }

    /**
     * Sets the value of the listIdentifiers property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListIdentifiers }
     *     
     */
    public void setListIdentifiers(ListIdentifiers value) {
        this.listIdentifiers = value;
    }

    /**
     * Gets the value of the listRecords property.
     * 
     * @return
     *     possible object is
     *     {@link ListRecords }
     *     
     */
    public ListRecords getListRecords() {
        return listRecords;
    }

    /**
     * Sets the value of the listRecords property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListRecords }
     *     
     */
    public void setListRecords(ListRecords value) {
        this.listRecords = value;
    }

}
