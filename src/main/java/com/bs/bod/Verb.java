/**
 * Verb
 */
package com.bs.bod;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.bs.bod.converter.VerbEnumDeserializer;
import com.bs.bod.converter.VerbEnumSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author dbs on Dec 25, 2015 11:13:35 AM
 * @since 0.0.1
 * @version 1.0
 * @version 1.1 change visibility getter/setter to public, add {@link #getVerbString()}
 * @version 1.2 add a {@link #control} field
 *
 */
@JsonAutoDetect(fieldVisibility = Visibility.NON_PRIVATE, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
@JsonInclude(Include.NON_DEFAULT)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Verb implements Serializable{

  private static final long serialVersionUID = 1L;
  
  @JsonProperty("t")
  @JsonSerialize(using = VerbEnumSerializer.class)
  @JsonDeserialize(using = VerbEnumDeserializer.class)
  VerbEnum verb;
  
  /**
   * additional command control when it is a {@link VerbEnum#control}
   */
  @JsonProperty("ctrl")
  String control;
  
  /**
   * default is <strong>false</strong>
   */
  @JsonProperty("ack")
  Boolean acknowledgeable = false; 
  
  /**
   * default is <strong>false</strong>
   */
  @JsonProperty("conf")
  Boolean confirmable = false;
  
  @JsonProperty("err")
  Boolean error = false;

  public Verb() {
    //set here to force serialization
    verb = VerbEnum.none;
  }

  /**
   * @param verb
   * @param acknowledgeable
   * @param confirmable
   */
  public Verb(VerbEnum verb, Boolean acknowledgeable, Boolean confirmable) {
    this();
    if(null != verb)
      this.verb = verb;
    this.acknowledgeable = acknowledgeable;
    this.confirmable = confirmable;
  }
  
  /**
   * @return the error
   */
  public Boolean isError() {
    return error || verb == VerbEnum.error;
  }

  /**
   * @param error the error to set
   * @return this for convenient chaining
   */
  public Verb setError(Boolean error) {
    this.error = error;
    return this;
  }


  /**
   * @return the verb. or enum action
   */
  public VerbEnum getVerb() {
    return verb;
  }
  
  /**
   * @return the verb. or enum action string
   */
  @JsonIgnore
  public String getVerbString() {
    return verb.name();
  }

  /**
   * @param verb the verb to set
   */
  public Verb setVerb(VerbEnum verb) {
    assert verb != null : "verb cannot be null";
    this.verb = verb;
    return this;
  }

  /**
   * @return the acknowledgeable
   */
  public Boolean isAcknowledgeable() {
    return acknowledgeable;
  }

  /**
   * @param acknowledgeable the acknowledgeable to set
   */
  public Verb setAcknowledgeable(Boolean acknowledgeable) {
    this.acknowledgeable = acknowledgeable;
    return this;
  }

  /**
   * @return the confirmable
   */
  public Boolean isConfirmable() {
    return confirmable;
  }

  /**
   * @param confirmable the confirmable to set
   */
  public Verb setConfirmable(Boolean confirmable) {
    this.confirmable = confirmable;
    return this;
  }
  /**
   * {@link #control }<br>
   * @return the control
   */
  public String getControl() {
    return control;
  }

  /**
   * set by factory only
   * @param control the control to set
   * @return 
   */
  Verb setControl(String control) {
    this.control = control;
    return this;
  }

  /**
   * {@link #acknowledgeable }<br>
   * @return the acknowledgeable
   */
  public Boolean getAcknowledgeable() {
    return acknowledgeable;
  }

  /**
   * {@link #confirmable }<br>
   * @return the confirmable
   */
  public Boolean getConfirmable() {
    return confirmable;
  }

  /**
   * {@link #error }<br>
   * @return the error
   */
  public Boolean getError() {
    return error;
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((acknowledgeable == null) ? 0 : acknowledgeable.hashCode());
    result = prime * result + ((confirmable == null) ? 0 : confirmable.hashCode());
    result = prime * result + ((error == null) ? 0 : error.hashCode());
    result = prime * result + ((verb == null) ? 0 : verb.hashCode());
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
    if (!(obj instanceof Verb)) {
      return false;
    }
    Verb other = (Verb) obj;
    if (acknowledgeable == null) {
      if (other.acknowledgeable != null) {
        return false;
      }
    } else if (!acknowledgeable.equals(other.acknowledgeable)) {
      return false;
    }
    if (confirmable == null) {
      if (other.confirmable != null) {
        return false;
      }
    } else if (!confirmable.equals(other.confirmable)) {
      return false;
    }
    if (error == null) {
      if (other.error != null) {
        return false;
      }
    } else if (!error.equals(other.error)) {
      return false;
    }
    if (verb != other.verb) {
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
