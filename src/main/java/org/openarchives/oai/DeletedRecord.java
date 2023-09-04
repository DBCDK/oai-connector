package org.openarchives.oai;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for deletedRecordType.
 * 
 * <p>The following schema fragment specifies the expected         content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="deletedRecordType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="no"/&gt;
 *     &lt;enumeration value="persistent"/&gt;
 *     &lt;enumeration value="transient"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "deletedRecordType")
@XmlEnum
public enum DeletedRecord {

    @XmlEnumValue("no")
    NO("no"),
    @XmlEnumValue("persistent")
    PERSISTENT("persistent"),
    @XmlEnumValue("transient")
    TRANSIENT("transient");
    private final String value;

    DeletedRecord(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DeletedRecord fromValue(String v) {
        for (DeletedRecord c: DeletedRecord.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
