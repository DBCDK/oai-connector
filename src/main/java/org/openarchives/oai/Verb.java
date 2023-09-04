package org.openarchives.oai;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for verbType.
 * 
 * <p>The following schema fragment specifies the expected         content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="verbType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Identify"/&gt;
 *     &lt;enumeration value="ListMetadataFormats"/&gt;
 *     &lt;enumeration value="ListSets"/&gt;
 *     &lt;enumeration value="GetRecord"/&gt;
 *     &lt;enumeration value="ListIdentifiers"/&gt;
 *     &lt;enumeration value="ListRecords"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "verbType")
@XmlEnum
public enum Verb {

    @XmlEnumValue("Identify")
    IDENTIFY("Identify"),
    @XmlEnumValue("ListMetadataFormats")
    LIST_METADATA_FORMATS("ListMetadataFormats"),
    @XmlEnumValue("ListSets")
    LIST_SETS("ListSets"),
    @XmlEnumValue("GetRecord")
    GET_RECORD("GetRecord"),
    @XmlEnumValue("ListIdentifiers")
    LIST_IDENTIFIERS("ListIdentifiers"),
    @XmlEnumValue("ListRecords")
    LIST_RECORDS("ListRecords");
    private final String value;

    Verb(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Verb fromValue(String v) {
        for (Verb c: Verb.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
