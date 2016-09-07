/**
 * BodError
 */
package com.bs.bod.error;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.LinkedHashMap;

import javax.annotation.Nullable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.bs.bod.converter.SeverityDeserializer;
import com.bs.bod.converter.SeveritySerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * {@linkplain http://www.onjava.com/pub/a/onjava/2003/11/19/exceptions.html}
 * 
 * @author dbs on Dec 26, 2015 9:11:15 PM
 * @since V0.0.1
 * @version 1.0
 * @version 1.1 add severity and title property
 * @version 1.2 add detailed exception and add logic to extract message and stacktace from an Exception object
 *
 */
@JsonAutoDetect(fieldVisibility = Visibility.NON_PRIVATE, getterVisibility = Visibility.NON_PRIVATE, setterVisibility = Visibility.NON_PRIVATE)
@JsonInclude(Include.NON_ABSENT)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class BodError implements Serializable{

  private static final long serialVersionUID = 1L;
  
  // used for error encoding, we keep the first 15 bits for the error number for that type of error which leaves 65535 possible errors
  static final long TYPE_RESET = 0;
  static final long TYPE_NONE = 1;// not used
  /**
   * Exceptions due to programming errors: In this category, exceptions are generated due to programming errors (e.g., NullPointerException and
   * IllegalArgumentException). The client code usually cannot do anything about programming errors.
   */
  static final long TYPE_EXCEPTION = (1 << 16);
  static final long TYPE_EXCEPTION_DETAIL = (1 << 17);
  /**
   * Exceptions due to resource failures: Exceptions that get generated when resources fail. 
   * For example: the system runs out of memory or a network connection fails. 
   * The client's response to resource failures is context-driven. 
   * The client can retry the operation after some time or just log the resource failure and bring the application to a halt.
   */
  static final long TYPE_TECHNICAL = (1 << 20);
  static final long TYPE_TECHNICAL_DETAIL = (1 << 21);
  /**
   * Exceptions due to client code errors: Client code attempts something not allowed by the API, and thereby violates its contract. The client can take some
   * alternative course of action, if there is useful information provided in the exception. For example: an exception is thrown while parsing an XML document
   * that is not well-formed. The exception contains useful information about the location in the XML document that causes the problem. The client can use this
   * information to take recovery steps.
   */
  static final long TYPE_FUNCTIONAL = (1 << 24);
  static final long TYPE_FUNCTIONAL_DETAIL = (1 << 25);
  // max is 32
  
  public static final long TYPE_ALL = 0x7FFFFFFFFFFF0000L;
  
  /**
   * Beware Severity is set for all errors and NOT for individual error messages or stacktraces
   * default {@link Severity#NONE}
   */
  @JsonSerialize(using = SeveritySerializer.class)
  @JsonDeserialize(using = SeverityDeserializer.class)
  @JsonProperty("s")
  Severity severity = Severity.NONE;
  
  /**
   * maybe be <code>null</code> can be used as title error, short error message, trace, debug, warn, ...
   */
  @JsonProperty("t")
  String title;
  
  /**
   * maybe be <code>null</code>
   */
  @JsonProperty("me")
  LinkedHashMap<Long, String> mapError2Message;

  public BodError(ErrorType errorType, String errorMessage) {
    putError(errorType, errorMessage);
  }

  /**
   * TODO classification of Java exception
   * @param e
   */
  public BodError(Exception e) {
    this(e, Severity.ERROR); 
  }

  public BodError(Exception e, Severity severity) {
    this.severity = severity; 
    putError(e);
  }
  
  public BodError(String e, Severity severity) {
    this.title = e;
    this.severity = severity; 
  }

  /**
   * helper to set an error message and a stacktrace from an {@link Exception}
   * @param e {@link Exception} can be null
   */
  @JsonIgnore
  public void putError(@Nullable Exception e) {
    if(null != e){
      long eid = e.hashCode();
      putError(eid | ErrorType.exception.mask, null == e.getMessage() ? (e instanceof NullPointerException ? "NPE" : e.getClass().getSimpleName()) : e.getMessage());
      putError(eid | ErrorType.exceptionDetail.mask, getStackTrace(e));
    }
  }
  
  @JsonIgnore
  public BodError putError(ErrorType errorType, String errorMessage) {
    putError(errorType.mask, errorMessage);
    return this;
  }
  
  @JsonIgnore
  public BodError putError(Long errorId, String errorMessage) {
    getMapError2Message().put(errorId, errorMessage);
    return this;
  }

  /**
   * true if it is a java or programmatic error
  * @param eid error id
  * @return
  */
  @JsonIgnore
  public Boolean isException(long eid) {
    return (eid & ErrorType.exception.mask) == ErrorType.exception.mask
        || (eid & ErrorType.exceptionDetail.mask) == ErrorType.exceptionDetail.mask;
  }
  
  /**
   * true if it is a functional or client error
  * @param eid error id
  * @return
  */
  @JsonIgnore
  public Boolean isFunctional(long eid) {
    return (eid & ErrorType.functional.mask) == ErrorType.functional.mask
        || (eid & ErrorType.functionalDetail.mask) == ErrorType.functionalDetail.mask;
  }
  
  /**
   * true if it is a technical or resource error
   * @param eid
   * @return
   */
  @JsonIgnore
  public Boolean isTechnical(long eid) {
    return (eid & ErrorType.technical.mask) == ErrorType.technical.mask
        || (eid & ErrorType.technicalDetail.mask) == ErrorType.technicalDetail.mask;
  }
  
  /**
   * extract error number from an encoded error id
   * @param eid error id
   * @return error number
   */
  @JsonIgnore
  public Number getErrorNumber(long eid){
    return eid & ~TYPE_ALL;
  }

  /**
   * helper to check if one and only one error was recorded
   * @return
   */
  @JsonIgnore
  public Boolean isUnique() {
    return getMapError2Message().size() == 1;
  }
  
  /**
   * helper to return unique or first error code
   * true if it is a java or programmatic error
  * @return 
  */
  @JsonIgnore
  public Boolean isException() {
    if(!getMapError2Message().isEmpty())
      return (getMapError2Message().keySet().iterator().next() & ErrorType.exception.mask) == ErrorType.exception.mask;
    return false;
  }
  
  /**
   * helper to return unique or first error code
   * true if it is a functional or client error
  * @return
  */
  @JsonIgnore
  public Boolean isFunctional() {
    if(!getMapError2Message().isEmpty())
      return (getMapError2Message().keySet().iterator().next() & ErrorType.functional.mask) == ErrorType.functional.mask;
    return false;
  }
  
  /**
   * helper to return unique or first error code
   * true if it is a technical or resource error
   * @return
   */
  @JsonIgnore
  public Boolean isTechnical() {
    if(!getMapError2Message().isEmpty())
      return (getMapError2Message().keySet().iterator().next() & ErrorType.technical.mask) == ErrorType.technical.mask;
    return false;
  }
  
  /**
   * helper to return unique or first error code
   * extract error number from an encoded error id
   * @return error number
   */
  @JsonIgnore
  public Number getErrorNumber(){
    if(!getMapError2Message().isEmpty())
      return getMapError2Message().keySet().iterator().next() & ~TYPE_ALL;
    return TYPE_NONE;
  }
  /**
   * helper to return unique or first error message
   * @return null if no error was set
   */
  @JsonIgnore
  public String getErrorMessage(){
    if(!getMapError2Message().isEmpty())
      return getMapError2Message().values().iterator().next();
    return null;
  }
  
  /**
   * helper to return unique or first error type
   * @return ErrorType if found
   */
  @JsonIgnore
  public ErrorType getErrorType(){
    if(!getMapError2Message().isEmpty()){
      return ErrorType.find(getMapError2Message().values().iterator().next());
    }
    return ErrorType.none;
  }
  /**
   * @return the mapErrorI2Message
   */
  LinkedHashMap<Long, String> getMapError2Message() {
    if (null == mapError2Message)
      mapError2Message = new LinkedHashMap<Long, String>(16, 0.75f, false);
    return mapError2Message;
  }

  /**
   * @param mapErrorI2Message
   *          the mapErrorI2Message to set
   * @return this for convenient chaining
   */
  public BodError setMapError2Message(LinkedHashMap<Long, String> mapErrorI2Message) {
    this.mapError2Message = mapErrorI2Message;
    return this;
  }

  /**
   * @return the severity
   */
  public Severity getSeverity() {
    return severity;
  }

  /**
   * @param severity the severity to set
   * @return this for convenient chaining
   */
  public BodError setSeverity(Severity severity) {
    this.severity = severity;
    return this;
  }

  /**
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * @param title the title to set
   * @return 
   * @return this for convenient chaining
   */
  public BodError setTitle(String title) {
    this.title = title;
    return this;
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((mapError2Message == null) ? 0 : mapError2Message.hashCode());
    result = prime * result + ((severity == null) ? 0 : severity.hashCode());
    result = prime * result + ((title == null) ? 0 : title.hashCode());
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
    if (!(obj instanceof BodError)) {
      return false;
    }
    BodError other = (BodError) obj;
    if (mapError2Message == null) {
      if (other.mapError2Message != null) {
        return false;
      }
    } else if (!mapError2Message.equals(other.mapError2Message)) {
      return false;
    }
    if (severity != other.severity) {
      return false;
    }
    if (title == null) {
      if (other.title != null) {
        return false;
      }
    } else if (!title.equals(other.title)) {
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

  static String getStackTrace(final Throwable t) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw, true);
    t.printStackTrace(pw);
    pw.flush();
    sw.flush();
    return sw.toString();
  }
  
}
