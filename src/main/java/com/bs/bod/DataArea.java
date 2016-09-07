/**
 * AbstractDataArea
 */
package com.bs.bod;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 *  Information that is specific to each BOD
 *  
 *  The Data Area (DataArea) of the Business Object Document contains the instance(s) of data values for the business transaction. 
 *  For example, to send a Purchase Order or Orders to a business partner, the Data Area will contain Verb (the action) 
 *  and the Noun (the object) on which the action is to be performed. 
 *  
 *  The DataArea contains a single verb and one or more occurrences of a noun. 
 *  This is shown in the examples above where the repeating PurchaseOrder element indicates that 1 
 *  or more instances of the “PurchaseOrder”s are to be “Process”ed.

 * @author dbs on Dec 25, 2015 11:13:35 AM
 * @version 1.0
 * @since 0.0.1
 *
 */
@JsonAutoDetect(fieldVisibility = Visibility.NON_PRIVATE, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
@JsonInclude(Include.NON_DEFAULT)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DataArea implements Serializable{

  private static final long serialVersionUID = 1L;

  /**
   * The Verb is the action to be applied to the object (the Noun). Examples of Verbs include 
   * Cancel, Add, Process, and Synchronize. Any additional information that is exclusively related to the action is also stored with the Verb.

    For example a Process verb indicates that it is acknowledgeable and confirmable.
   */
  @JsonProperty("v")
  Verb verb;
  
  /**
   *  
   */
  @JsonProperty("n")
  Set<Noun> nouns;

  DataArea() {
    this(new Verb(), new Noun());
  }
  
  DataArea(Verb verb, String noun) {
    this(verb, new Noun(noun));
    this.verb = verb;
  }
  
  DataArea(Verb verb, Noun noun) {
    this.verb = verb;
    this.nouns = new HashSet<Noun>();
    this.nouns.add(new Noun());
  }

  @JsonIgnore
  public boolean isSetRoute() {
    return nouns != null && nouns.iterator().next().route != null;
  }
  
  /**
   * @return the verb, never null
   */
  public Verb getVerb() {
    if(null == verb)
      verb = new Verb();
    return verb;
  }

  /**
   * @param verb the verb to set
   */
  public void setVerb(Verb verb) {
    this.verb = verb;
  }

  /**
   * Get the unique noun
   * @return the singleton noun, never null
   */
  public Noun getNoun() {
    if(null == nouns){
      this.nouns = new HashSet<Noun>();
      this.nouns.add(new Noun());
    }
    return nouns.iterator().next();
  }
  /**
   * get a new set copyin an existing noun if any found
   * @return a new {@link HashSet} 
   */
  public Set<Noun> getNouns() {
    if(null != nouns){
      Set<Noun> _nouns = new HashSet<Noun>();
      _nouns.add(nouns.iterator().next());
      nouns = _nouns;
    } else if(null == nouns){
      nouns = new HashSet<Noun>();
    }
    return nouns;
  }

  /**
   * @param noun the noun to set
   * @return 
   */
  public DataArea setNoun(Set<Noun> nouns) {
    this.nouns = nouns;
    return this;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((nouns == null) ? 0 : nouns.hashCode());
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
    if (!(obj instanceof DataArea)) {
      return false;
    }
    DataArea other = (DataArea) obj;
    if (nouns == null) {
      if (other.nouns != null) {
        return false;
      }
    } else if (!nouns.equals(other.nouns)) {
      return false;
    }
    if (verb == null) {
      if (other.verb != null) {
        return false;
      }
    } else if (!verb.equals(other.verb)) {
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
