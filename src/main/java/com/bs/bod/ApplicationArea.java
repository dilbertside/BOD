/**
 * ApplicationArea
 */
package com.bs.bod;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * application-specific information common to all BODs
 * 
 * * The ApplicationArea carries information that an application may need to know in order to communicate in an integration
 * of two or more business applications.
 * The ApplicationArea is used at the applications layer of communication. While the integration frameworks web services
 * and middleware provide the communication layer that OAGIS operates on top of.
 * 
 * The ApplicationArea serves four main purposes:
 * 
 * 1. To identify the sender of the message.
 * 2. To identify when the document was created.
 * 3. To provide authentication of the sender through the use of a digital signature, if applicable.
 * 4. To uniquely identify a BOD instance. The BODId field is the Globally Unique Identifier for the BOD instance.
 * 
 * The ApplicationArea is comprised of the following elements:
 * - Sender
 * - Creation – (date and time)
 * - Signature
 * - BODID
 * - UserArea TODO
 * 
 * @author dbs on Dec 25, 2015 11:13:35 AM
 * @version 1.0
 * @since 0.3.5
 *
 */
@JsonAutoDetect(fieldVisibility = Visibility.NON_PRIVATE, getterVisibility = Visibility.NON_PRIVATE, setterVisibility = Visibility.NON_PRIVATE)
@JsonInclude(Include.NON_ABSENT)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplicationArea implements Serializable{

  private static final long serialVersionUID = 1L;

  @JsonProperty("s")
  Sender sender;

  /**
   * CreationDateTime is the date time stamp that the given instance of the Business Object Document was created.
   * This date must not be modified during the life of the Business Object Document.
   * 
   * OAGIS Date time type supports ISO Date Time format.
   */
  @JsonProperty("dt")
  @XmlAttribute(name = "creation-date-time", required = true)
  final Date creationDateTime;

  /**
   * If the BOD is to be signed the signature element is included, otherwise it is not.
   * 
   * Signature will support any digital signature that maybe used by an implementation of OAGIS. The qualifying Agency identifies the agency that provided the
   * format for the signature.
   * 
   * This element supports any digital signature specification that is available today and in the future. This is accomplished by not actually defining the
   * content but by allowing the implementation to specify the digital signature to be used via an external XML Schema namespace declaration. The Signature
   * element is defined to have any content from any other namespace.
   * 
   * This allows the user to carry a digital signature in the xml instance of a BOD. The choice of which digital signature to use is left up to the user and
   * their integration needs.
   * 
   * For more information on the W3C’s XML Signature specification refer to: http://www.w3.org/TR/xmldsig-core/.<br>
   * Alternatively, this field can be used to automatically compute a digest of the message being sent cf {@link com.bs.bod.Bod#__sharedPrivateKey} 
   */
  @JsonProperty("sig")
  @XmlElement
  String signature;

  /**
   * The BODID provides a place to carry a Globally Unique Identifier (GUID) that will make each Business Object Document uniquely identifiable. This is a
   * critical success factor to enable software developers to use the Globally Unique Identifier (GUID) to build the following services or capabilities:
   * 
   * 1. Legally binding transactions,
   * 
   * 2. Transaction logging,
   * 
   * 3. Exception handling,
   * 
   * 4. Re-sending,
   * 
   * 5. Reporting,
   * 
   * 6. Confirmations,
   * 
   * 7. Security.
   */
  @JsonProperty("bid")
  @XmlAttribute(name="bod-id",  required = true)
  final String bodId;

  ApplicationArea() {
    this(new Date(), UUID.randomUUID().toString());
  }

  public ApplicationArea(Sender sender, Date creationDateTime, String signature, String bodId) {
    this(creationDateTime, bodId);
    this.sender = sender;
    this.signature = signature;
  }
  
  public ApplicationArea(Date creationDateTime, String bodId) {
    assert creationDateTime != null : "BOD creation Date cannot be null";  
    assert bodId != null : "BOD Id cannot be null";  
    this.creationDateTime = creationDateTime;
    this.bodId = bodId;
  }
  
  public ApplicationArea(Date creationDateTime, UUID bodId) {
    assert creationDateTime != null : "BOD creation Date cannot be null";  
    assert bodId != null : "BOD ID cannot be null";  
    this.creationDateTime = creationDateTime;
    this.bodId = bodId.toString();
  }
  
  @JsonIgnore
  public boolean isSetSender() {
    return sender != null;
  }
  
  /**
   * @return the sender, never null
   */
  public Sender getSender() {
    if(null == sender)
      sender = new Sender();
    return sender;
  }

  /**
   * @param sender the sender to set
   */
  public ApplicationArea setSender(Sender sender) {
    assert sender != null : "BOD sender cannot be null";
    this.sender = sender;
    return this;
  }

  /**
   * @return the signature
   */
  public String getSignature() {
    return signature;
  }

  /**
   * @param signature the signature to set
   */
  public ApplicationArea setSignature(String signature) {
    this.signature = signature;
    return this;
  }

  /**
   * @return the creationDateTime
   */
  public Date getCreationDateTime() {
    return creationDateTime;
  }

  /**
   * @return the bodId
   */
  public String getBodId() {
    return bodId;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((bodId == null) ? 0 : bodId.hashCode());
    result = prime * result + ((creationDateTime == null) ? 0 : creationDateTime.hashCode());
    result = prime * result + ((sender == null) ? 0 : sender.hashCode());
    result = prime * result + ((signature == null) ? 0 : signature.hashCode());
    return result;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof ApplicationArea)) {
      return false;
    }
    ApplicationArea other = (ApplicationArea) obj;
    if (bodId == null) {
      if (other.bodId != null) {
        return false;
      }
    } else if (!bodId.equals(other.bodId)) {
      return false;
    }
    if (creationDateTime == null) {
      if (other.creationDateTime != null) {
        return false;
      }
    } else if (!creationDateTime.equals(other.creationDateTime)) {
      return false;
    }
    if (sender == null) {
      if (other.sender != null) {
        return false;
      }
    } else if (!sender.equals(other.sender)) {
      return false;
    }
    if (signature == null) {
      if (other.signature != null) {
        return false;
      }
    } else if (!signature.equals(other.signature)) {
      return false;
    }
    return true;
  }
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
      return ToStringBuilder.reflectionToString(this);
  }

}
