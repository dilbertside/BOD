/**
 * Sender
 */
package com.bs.bod;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.bs.bod.converter.ConfirmationCodeDeserializer;
import com.bs.bod.converter.ConfirmationCodeSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author dbs on Dec 25, 2015 11:13:35 AM
 * @version 1.0
 * @since 0.0.1
 */
@JsonAutoDetect(fieldVisibility = Visibility.NON_PRIVATE, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
@JsonInclude(Include.NON_NULL)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class Sender implements Serializable{
  
  private static final long serialVersionUID = 1L;
  
  static String __logicalId = null;
  static String __componentId = null;
  static String __taskId = null;
  static String __referenceId = null;
  static ConfirmationCode __confirmationCode = ConfirmationCode.never;
  static String __authorizationId = null;
  
  
  /**
   * The Logical Identifier element provides the logical location of the server and application from which the Business Object Document originated. It can be
   * used to establish a logical to physical mapping, however its use is optional.
   * 
   * Each system or combination of systems should maintain an external central reference table containing the logical names or logical addresses of the
   * application systems in the integration configuration. This enables the logical names to be mapped to the physical network addresses of the resources
   * needed on the network.
   * 
   * Note: The technical implementation of this Domain Naming Service is not dictated by this specification.
   * 
   * This logical to physical mapping may be done at execution time by the application itself or by a middleware transport mechanism, depending on the
   * integration architecture used.
   * 
   * This provides for a simple but effective directory access capability while maintaining application independence from the physical location of those
   * resources on the network.
   * <br><strong>default is <code>null</code></strong>
   */
  @JsonProperty("lid")
  String logicalId = __logicalId;

  /**
   * The Component ID provides a finer level of control than Logical Identifier and represents the business application that issued the Business Object
   * document. Its use is optional.
   * 
   * The Open Applications Group has not constructed the list of valid Component names. A suggestion for naming is to use the application component names used
   * in the scenario diagrams in section two of OAGIS. Example Components may be “Inventory”, or “Payroll”.
   * <br><strong>default is <code>null</code></strong>
   */
  @JsonProperty("cid")
  String componentId = __componentId;

  /**
   * The Task ID describes the business event that initiated the need for the Business Object Document to be created. 
   * Its use is optional. Although the Task may differ depending on the specific implementation, 
   * it is important to enable drill back capability. Example Tasks may be “Receipt” or “Adjustment”.
   * 
   * <br><strong>default is <code>null</code></strong>
   */
  @JsonProperty("tid")
  String taskId = __taskId;

  /**
   * Reference ID enables the sending application to indicate the instance identifier of the event or task that caused the BOD to be created. 
   * This allows drill back from the BOD message into the sending application. 
   * The may be required in environments where an audit trail must be maintained for all transactions.
   * <br><strong>default is <code>null</code></strong>
   * @see transaction ID
   */
  @JsonProperty("rid")
  String referenceId = __referenceId;

  /**
   * The Confirmation Code request is an option controlled by the Sender business application.
   * It is a request to the receiving application to send back a confirmation BOD to the sender.
   * The confirmation Business Object Document may indicate the successful processing of the original Business Object Document
   * or return error conditions if the original Business Object Document was unsuccessful.
   * <br><strong>default is assumed to be ConfirmationCode.never</strong>
   */
  @JsonProperty("cc")
  @JsonSerialize(using = ConfirmationCodeSerializer.class)
  @JsonDeserialize(using = ConfirmationCodeDeserializer.class)
  ConfirmationCode confirmationCode = __confirmationCode;

  /**
   * The Authorization Identifier describes the point of entry, such as the machine or device the user uses to perform the task that caused the creation of
   * the Business Object Document.
   * 
   * The Authorization Identifier is used as a return routing mechanism for a subsequent BOD, or for diagnostic or auditing purposes. 
   * Valid Authorization Identifiers are implementation specific. 
   * The Authorization Identifier might be used for authentication in the business process. As an example, in the case of Plant Data Collection, 
   * the Authorization Identifier is used to fully define the path between the user of a hand held terminal, any intermediate
   * controller and the receiving application.
   * 
   * In returning a BOD, the receiving application would pass the Authorization Identifier back to the controller to allow the message to be routed back to
   * the hand held terminal.
   * 
   * <br><strong>default is <code>null</code></strong>
   */
  @JsonProperty("aid")
  String authorizationId = __authorizationId;

  public Sender(){
    
  }
  
  public Sender(ConfirmationCode confirmationCode){
    this.confirmationCode = confirmationCode;
  }
  
  public Sender(String logicalId, String componentId, String taskId, String referenceId, ConfirmationCode confirmationCode, String authorizationId) {
    this(confirmationCode);
    this.logicalId = logicalId;
    this.componentId = componentId;
    this.taskId = taskId;
    this.referenceId = referenceId;
    this.authorizationId = authorizationId;
  }



  /**
   * @return the logicalId
   */
  public String getLogicalId() {
    return logicalId;
  }
  /**
   * @param logicalId the logicalId to set
   * @return this for convenient chaining
   */
  public Sender setLogicalId(String logicalId) {
    this.logicalId = logicalId;
    return this;
  }
  /**
   * @return the componentId
   */
  public String getComponentId() {
    return componentId;
  }
  /**
   * @param componentId the componentId to set
   * @return this for convenient chaining
   */
  public Sender setComponentId(String componentId) {
    this.componentId = componentId;
    return this;
  }
  /**
   * @return the taskId
   */
  public String getTaskId() {
    return taskId;
  }
  /**
   * @param taskId the taskId to set
   * @return this for convenient chaining
   */
  public Sender setTaskId(String taskId) {
    this.taskId = taskId;
    return this;
  }
  /**
   * @return the referenceId
   */
  public String getReferenceId() {
    return referenceId;
  }
  /**
   * @param referenceId the referenceId to set
   * @return this for convenient chaining
   */
  public Sender setReferenceId(String referenceId) {
    this.referenceId = referenceId;
    return this;
  }
  /**
   * @return the confirmationCode
   */
  public ConfirmationCode getConfirmationCode() {
    return confirmationCode;
  }
  /**
   * @param confirmationCode the confirmationCode to set
   * @return this for convenient chaining
   */
  public Sender setConfirmationCode(ConfirmationCode confirmationCode) {
    this.confirmationCode = confirmationCode;
    return this;
  }
  /**
   * @return the authorizationId
   */
  public String getAuthorizationId() {
    return authorizationId;
  }
  /**
   * @param authorizationId the authorizationId to set
   * @return this for convenient chaining
   */
  public Sender setAuthorizationId(String authorizationId) {
    this.authorizationId = authorizationId;
    return this;
  }
  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((authorizationId == null) ? 0 : authorizationId.hashCode());
    result = prime * result + ((componentId == null) ? 0 : componentId.hashCode());
    result = prime * result + ((confirmationCode == null) ? 0 : confirmationCode.hashCode());
    result = prime * result + ((logicalId == null) ? 0 : logicalId.hashCode());
    result = prime * result + ((referenceId == null) ? 0 : referenceId.hashCode());
    result = prime * result + ((taskId == null) ? 0 : taskId.hashCode());
    return result;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Sender other = (Sender) obj;
    if (authorizationId == null) {
      if (other.authorizationId != null)
        return false;
    } else if (!authorizationId.equals(other.authorizationId))
      return false;
    if (componentId == null) {
      if (other.componentId != null)
        return false;
    } else if (!componentId.equals(other.componentId))
      return false;
    if (confirmationCode != other.confirmationCode)
      return false;
    if (logicalId == null) {
      if (other.logicalId != null)
        return false;
    } else if (!logicalId.equals(other.logicalId))
      return false;
    if (referenceId == null) {
      if (other.referenceId != null)
        return false;
    } else if (!referenceId.equals(other.referenceId))
      return false;
    if (taskId == null) {
      if (other.taskId != null)
        return false;
    } else if (!taskId.equals(other.taskId))
      return false;
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
