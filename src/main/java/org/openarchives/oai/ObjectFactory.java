package org.openarchives.oai;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;

import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.openarchives.oai package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _OAIPMH_QNAME = new QName("http://www.openarchives.org/OAI/2.0/", "OAI-PMH");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.openarchives.oai
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link OAIPMH }
     * 
     */
    public OAIPMH createOAIPMHtype() {
        return new OAIPMH();
    }

    /**
     * Create an instance of {@link Request }
     * 
     */
    public Request createRequestType() {
        return new Request();
    }

    /**
     * Create an instance of {@link OAIPMHerror }
     * 
     */
    public OAIPMHerror createOAIPMHerrorType() {
        return new OAIPMHerror();
    }

    /**
     * Create an instance of {@link Identify }
     * 
     */
    public Identify createIdentifyType() {
        return new Identify();
    }

    /**
     * Create an instance of {@link ListMetadataFormats }
     * 
     */
    public ListMetadataFormats createListMetadataFormatsType() {
        return new ListMetadataFormats();
    }

    /**
     * Create an instance of {@link ListSets }
     * 
     */
    public ListSets createListSetsType() {
        return new ListSets();
    }

    /**
     * Create an instance of {@link GetRecord }
     * 
     */
    public GetRecord createGetRecordType() {
        return new GetRecord();
    }

    /**
     * Create an instance of {@link ListRecords }
     * 
     */
    public ListRecords createListRecordsType() {
        return new ListRecords();
    }

    /**
     * Create an instance of {@link ListIdentifiers }
     * 
     */
    public ListIdentifiers createListIdentifiersType() {
        return new ListIdentifiers();
    }

    /**
     * Create an instance of {@link Record }
     * 
     */
    public Record createRecordType() {
        return new Record();
    }

    /**
     * Create an instance of {@link Header }
     * 
     */
    public Header createHeaderType() {
        return new Header();
    }

    /**
     * Create an instance of {@link Metadata }
     * 
     */
    public Metadata createMetadataType() {
        return new Metadata();
    }

    /**
     * Create an instance of {@link About }
     * 
     */
    public About createAboutType() {
        return new About();
    }

    /**
     * Create an instance of {@link ResumptionToken }
     * 
     */
    public ResumptionToken createResumptionTokenType() {
        return new ResumptionToken();
    }

    /**
     * Create an instance of {@link Description }
     * 
     */
    public Description createDescriptionType() {
        return new Description();
    }

    /**
     * Create an instance of {@link MetadataFormat }
     * 
     */
    public MetadataFormat createMetadataFormatType() {
        return new MetadataFormat();
    }

    /**
     * Create an instance of {@link Set }
     * 
     */
    public Set createSetType() {
        return new Set();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OAIPMH }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link OAIPMH }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.openarchives.org/OAI/2.0/", name = "OAI-PMH")
    public JAXBElement<OAIPMH> createOAIPMH(OAIPMH value) {
        return new JAXBElement<OAIPMH>(_OAIPMH_QNAME, OAIPMH.class, null, value);
    }

}
