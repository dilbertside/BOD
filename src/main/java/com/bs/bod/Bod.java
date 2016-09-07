/**
 * Bod
 */
package com.bs.bod;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.bs.bod.error.BodError;
import com.bs.bod.error.ErrorType;
import com.bs.bod.signature.MessageDigestBuilder;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * {@linkplain http://www.oagi.org/oagis/9.0/Documentation/Architecture.html}
 * @author dbs on Dec 25, 2015 11:13:35 AM
 * @since 0.0.1
 * @version 1.0
 * @version 1.0 add helper {@link #getError()} and {@link #addError(ErrorType, String)}
 *
 */
@JsonAutoDetect(fieldVisibility = Visibility.NON_PRIVATE, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
@JsonInclude(Include.NON_ABSENT)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Bod implements Serializable{

  private static final long serialVersionUID = -2753961254855108497L;
  
  /**
   * this environment variable may be used to discard unwanted non-production messages
   */
  static String __sysEnvCode = "prod";
  
  /**
   * this private key will be used to protect integrity of message being exchanged<br>
   * Sender and receiver must share the same key for the receiver to be able to verify the BOD<br>
   * this will be used to compute a digest checksum of all BOD properties being set by sender with a private key known only by both the sender and receiver.<br>
   * Digest is written in {@link com.bs.bod.ApplicationArea#signature}<br>
   * <strong>Important:</strong> To deactivate checksum computing set the key to <strong>null</strong>
   * 
   * Default key is <strong>changeme</strong><br>
   * <br>
   * To set the private key, example<br>
   * <code>
   * static{<br>
   *   BodFactory.init("QAF", "web", ConfirmationCode.onError);<br>
   * }<br>
   * </code>
   */
  static String __sharedPrivateKey = "changeme";
  
  /**
   * Release ID is used to identify the release of OAGIS that the BOD belongs. 
   * For the BODs from OAGIS 9.0 the value of this attribute will be “9.0”. 
   * The release ID is a required attribute of the BOD.
   */
  @JsonProperty("r")
  String releaseId = "9.0";
  /**
   * Version ID is used to identify the version of the Business Object Document.   
   * Each BOD has its own revision number to specifically identify the level of that BOD, not just the release ID of OAGIS. 
   * The specific BOD version number is documented in each chapter of OAGIS. The outermost element name no longer includes the version number; 
   * it is instead now carried as an attribute of the BOD. 
   * The version ID attribute is an optional attribute of the BOD.
   */
  @JsonProperty("v")
  String versionId = "1.0";
  /**
   * The System Environment Code is used to identify whether this particular BOD is being sent as a result of a test or as production level integration. 
   * Often times as new systems are brought online testing must be performed in a production environment in order to ensure integration with existing systems. 
   * This attribute allows the integrator to flag these test messages as such. The environment attribute is an optional attribute of the BOD.
   * <br>
   * <strong>default value is 'prod'<strong>
   */
  @JsonProperty("s")
  String sysEnvCode = __sysEnvCode;
  /**
   * The languageCode attributes indicates the language of the data being carried in the BOD message. 
   * It is possible to override the BOD level language for fields that may need to carry multi-lingual information. 
   * Examples of this are Notes and Description.
   */
  @JsonProperty("l")
  String languageCode = "en";
  
  @JsonProperty("a")
  ApplicationArea applicationArea;
  
  @JsonProperty("d")
  DataArea dataArea;
  
  /**
   * package visibility for factory
   * @param confirmationCode 
   */
  Bod(ConfirmationCode confirmationCode){
    applicationArea = getApplicationArea();
    Sender sender = new Sender(confirmationCode);
    applicationArea.setSender(sender);
    
    dataArea = getDataArea(); 
  }
  
  Bod(){
    applicationArea = new ApplicationArea();
    Sender sender = new Sender();
    applicationArea.setSender(sender);
    
    dataArea = new DataArea(new Verb(), new Noun()); 
  }

  /**
   * @return the releaseId
   */
  public String getReleaseId() {
    return releaseId;
  }

  /**
   * @param releaseId the releaseId to set
   */
  Bod setReleaseId(String releaseId) {
    this.releaseId = releaseId;
    return this;
  }

  /**
   * @return the versionId
   */
  public String getVersionId() {
    return versionId;
  }

  /**
   * @param versionId the versionId to set
   */
  Bod setVersionId(String versionId) {
    this.versionId = versionId;
    return this;
  }

  /**
   * @return the sysEnvCode
   */
  public String getSysEnvCode() {
    return sysEnvCode;
  }

  /**
   * @param sysEnvCode the sysEnvCode to set
   */
  Bod setSysEnvCode(String sysEnvCode) {
    this.sysEnvCode = sysEnvCode;
    return this;
  }

  /**
   * @return the languageCode
   */
  public String getLanguageCode() {
    return languageCode;
  }

  /**
   * @param languageCode the languageCode to set
   * @return 
   */
  public Bod setLanguageCode(String languageCode) {
    this.languageCode = languageCode;
    return this;
  }

  /**
   * @return the applicationArea, never null
   */
  public ApplicationArea getApplicationArea() {
    if(null == applicationArea)
      applicationArea = new ApplicationArea();
    return applicationArea;
  }

  /**
   * @param applicationArea the applicationArea to set
   */
  Bod setApplicationArea(ApplicationArea applicationArea) {
    this.applicationArea = applicationArea;
    return this;
  }

  /**
   * @return the dataArea, never null
   */
  public DataArea getDataArea() {
    if(null == dataArea)
      dataArea = new DataArea();
    return dataArea;
  }

  /**
   * @param dataArea the dataArea to set
   */
  Bod setDataArea(DataArea dataArea) {
    this.dataArea = dataArea;
    return this;
  }

  /**
   * set conversation mode {@link ConfirmationCode.onError}
   * @return <code>this</code> for easy chaining
   */
  @JsonIgnore
  public Bod setConfirmationCodeOnError() {
    this.getApplicationArea().getSender().setConfirmationCode(ConfirmationCode.onError);
    return this;
  }
  /**
   * set conversation mode {@link ConfirmationCode.never}
   * @return <code>this</code> for easy chaining
   */
  @JsonIgnore
  public Bod setConfirmationCodeNever() {
    this.getApplicationArea().getSender().setConfirmationCode(ConfirmationCode.never);
    return this;
  }
  /**
   * set conversation mode {@link ConfirmationCode.always}
   * @return <code>this</code> for easy chaining
   */
  @JsonIgnore
  public Bod setConfirmationCodeAlways() {
    this.getApplicationArea().getSender().setConfirmationCode(ConfirmationCode.always);
    return this;
  }

  /**
   * set conversation mode {@link ConfirmationCode.always}
   * @return <code>this</code> for easy chaining
   */
  @JsonIgnore
  public Bod setVerbAction(VerbEnum verbEnum) {
    getDataArea().getVerb().setVerb(verbEnum);
    return this;
  }

  /**
   * add object payload to BOD<br>
   * Will eventually compute a payload SHA-1 digest if the private key is not null
   * @param payload to serialize
   * @return <code>this</code> for easy chaining
   */
  @JsonIgnore
  public Bod setPayload(Object payload) {
    setPayload(payload, NounEnum.element);
    return this;
  }
  
  /**
   * add string payload to BOD<br>
   * Will eventually compute a payload SHA-1 digest if the private key is not null
   * @param payload to serialize
   * @return <code>this</code> for easy chaining
   */
  @JsonIgnore
  public Bod setPayloadMessage(String message) {
    setPayload(message, NounEnum.element);
    return this;
  }
  
  /**
   * add object payload to BOD and its type<br>
   * Will eventually compute a payload SHA-1 digest if the private key is not null
   * @param payload to serialize
   * @param nounEnum know object type
   * @return <code>this</code> for easy chaining
   */
  @JsonIgnore
  public Bod setPayload(Object payload, NounEnum nounEnum) {
    getDataArea().getNoun().setComponent(payload);
    getDataArea().getNoun().setNoun(nounEnum);
    if(null != payload && null != Bod.getSharedPrivateKey()){
    	getApplicationArea().signature =  MessageDigestBuilder.hash(payload);
    }
    return this;
  }

  
  /**
   * for serialization only, remove noun with no interesting values
   * @return <code>this</code> for easy chaining
   */
  @JsonIgnore
  public Bod removeNoun() {
    getDataArea().setNoun(null);
    return this;
  }
  /**
   * helper to retrieve BOD CreationDateTime or timestamp
   * @return {@link ApplicationArea#creationDateTime} never null
   */
  @JsonIgnore
  public long getTimestamp() {
    return getApplicationArea().getCreationDateTime().getTime();
  }
  
  /**
   * helper to set creation date or timestamp
   * @param timestamp
   * @return <code>this</code> for easy chaining
   */
  @JsonIgnore
  public Bod setTimestamp(Long timestamp) {
    assert timestamp != null : "BOD creation Date cannot be null";  
    Sender sender = null;
    if(getApplicationArea().isSetSender())
      sender = getApplicationArea().getSender();
    String bodId = getApplicationArea().getBodId();
    setApplicationArea(new ApplicationArea(new Date(timestamp), bodId));
    if(sender != null)
      getApplicationArea().setSender(sender);
    return this;
  }

  /**
   * helper to retrieve BOD ID
   * @return {@link ApplicationArea#bodId} never null
   */
  @JsonIgnore
  public String getBodId() {
    return getApplicationArea().getBodId();
  }
  
  /**
   * helper to set an externally generated BOD ID
   * @param bodId
   * @return <code>this</code> for easy chaining
   */
  @JsonIgnore
  public Bod setBodId(String bodId) {
    assert bodId != null : "BOD Id cannot be null";
    Sender sender = null;
    if(getApplicationArea().isSetSender())
      sender = getApplicationArea().getSender();
    Date dt = getApplicationArea().getCreationDateTime();
    setApplicationArea(new ApplicationArea(dt, bodId));
    if(sender != null)
      getApplicationArea().setSender(sender);
    return this;
  }
  
  /**
   * helper to set an externally generated BOD ID and creation date or timestamp
   * @param bodId non null
   * @param timestamp non null 
   * @return <code>this</code> for easy chaining
   */
  @JsonIgnore
  public Bod setBodIdAndTimestamp(String bodId, Long timestamp) {
    assert bodId != null : "BOD Id cannot be null";
    assert timestamp != null : "BOD creation Date cannot be null";
    Sender sender = null;
    if(getApplicationArea().isSetSender())
      sender = getApplicationArea().getSender();
    setApplicationArea(new ApplicationArea(new Date(timestamp), bodId));
    if(sender != null)
      getApplicationArea().setSender(sender);
    return this;
  }

  /**
   * extract payload or data component
   * @return  maybe null
   */
  @JsonIgnore
  public Object getPayload(){
    return getDataArea().getNoun().getComponent();
  }
  
  /**
   * return true if either the verb or the noun has an error set
   * @return <code>true</code> if yes, <code>false</code> otherwise
   */
  @JsonIgnore
  public boolean hasError() {
    return null != dataArea && (getDataArea().getNoun().hasError() || getDataArea().getVerb().isError());
  }
  
  /**
   * helper to return unique or first error message
   * @return 'No Error' if no error was set
   */
  @JsonIgnore
  public String getErrorMessage() {
    if(hasError())
      return getDataArea().getNoun().getError().getErrorMessage();
    return "No Error";
  }
  

  /**
   * helper to return {@link BodError} message
   * 
   * @return <code>null</code> if no error was set
   */
  @JsonIgnore
  public BodError getError() {
    if(hasError())
      return getDataArea().getNoun().getError();
    return null;
  }
  
  /**
   * Helper add an error to an already existing error BOD, if not set a new one will be created
   * 
   * @param errorType {@link ErrorType}
   * @param error additional error message to add 
   * @return <code>this</code> for convenient chaining
   */
  public Bod addError(ErrorType errorType, String error){
    if(hasError()){
      getDataArea().getNoun().getError().putError(errorType, error);
    }else{
      getDataArea().getNoun().setError(new BodError(errorType, error));
      getDataArea().getVerb().setError(true);
    }
    return this;
  }
  /**
   * helper to retrieve reply message string if any
   * @return an empty string if none found or wrong type (not a String)
   */
  @JsonIgnore
  public String getReplyMessage() {
    if(getDataArea().getNoun().isInstanceOf(String.class) && getDataArea().getNoun().isSetComponent())
      return getDataArea().getNoun().getComponent().toString();
    return "";
  }
  
  /**
   * helper to retrieve reply object if any
   * @return null if not found
   */
  @JsonIgnore
  public Object getReplyPayload() {
    if(getDataArea().getNoun().isSetComponent())
      return getDataArea().getNoun().getComponent();
    return null;
  }
  
  /**
   * Helper to quickly check if a route is set 
   * @return <code>false</code> if rout e is not set <code>true</code> otherwise
   */
  @JsonIgnore
  public boolean isSetRoute() {
    return null != dataArea &&  getDataArea().isSetRoute();
  }
  
  /**
   * Helper to quickly get a route<br>
   * use {@link #isSetRoute()} to check its pre-existence<br>
   * <strong>Beware</strong> it creates a noun if it doesn't exist 
   * @return route, maybe null
   */
  @JsonIgnore
  public String getRoute() {
    return getDataArea().getNoun().getRoute();
  }
  
  /**
   * Helper to quickly get the action<br>
   * use {@link #isSetRoute()} to check its pre-existence<br>
   * <strong>Beware</strong> it creates a verb if it doesn't exist 
   * @return route, maybe null
   */
  @JsonIgnore
  public String getAction() {
    return getDataArea().getVerb().getVerbString();
  }
  
  /**
   * Helper to quickly get the command control<br>
   * <strong>Beware</strong> it creates a verb if it doesn't exist 
   * @return control, maybe null
   */
  @JsonIgnore
  public String getControl() {
    return getDataArea().getVerb().getControl();
  }

  
  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((applicationArea == null) ? 0 : applicationArea.hashCode());
    result = prime * result + ((dataArea == null) ? 0 : dataArea.hashCode());
    result = prime * result + ((languageCode == null) ? 0 : languageCode.hashCode());
    result = prime * result + ((releaseId == null) ? 0 : releaseId.hashCode());
    result = prime * result + ((sysEnvCode == null) ? 0 : sysEnvCode.hashCode());
    result = prime * result + ((versionId == null) ? 0 : versionId.hashCode());
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
    if (!(obj instanceof Bod)) {
      return false;
    }
    Bod other = (Bod) obj;
    if (applicationArea == null) {
      if (other.applicationArea != null) {
        return false;
      }
    } else if (!applicationArea.equals(other.applicationArea)) {
      return false;
    }
    if (dataArea == null) {
      if (other.dataArea != null) {
        return false;
      }
    } else if (!dataArea.equals(other.dataArea)) {
      return false;
    }
    if (languageCode == null) {
      if (other.languageCode != null) {
        return false;
      }
    } else if (!languageCode.equals(other.languageCode)) {
      return false;
    }
    if (releaseId == null) {
      if (other.releaseId != null) {
        return false;
      }
    } else if (!releaseId.equals(other.releaseId)) {
      return false;
    }
    if (sysEnvCode == null) {
      if (other.sysEnvCode != null) {
        return false;
      }
    } else if (!sysEnvCode.equals(other.sysEnvCode)) {
      return false;
    }
    if (versionId == null) {
      if (other.versionId != null) {
        return false;
      }
    } else if (!versionId.equals(other.versionId)) {
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

	/**
	 * @return the __sharedPrivateKey
	 */
	public static String getSharedPrivateKey() {
		return __sharedPrivateKey;
	}

}
