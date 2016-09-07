package com.bs.bod;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.bs.bod.converter.NounEnumDeserializer;
import com.bs.bod.converter.NounEnumSerializer;
import com.bs.bod.converter.ObjectDeserializer;
import com.bs.bod.error.BodError;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Noun is the object or document that is being acted upon. Examples include PurchaseOrder, RequestForQuote, and Invoice. Nouns are extensible within OAGIS,
 * meaning that they can include content that was not originally designed by OAGI.
 * 
 * There are different types of verbs or actions that can be performed on a PurchaseOrder; as such the base Noun (e.g. PurchaseOrder) contains all of the
 * information that might be present on a PurchaseOrder. The instantiation of each of the possible verb and noun combinations then further defines what must be
 * provided to perform the intended transaction. For example in a ProcessPurchaseOrder transaction business partners and line item data must be provided,
 * whereas in a CancelPurchaseOrder only the order identifier needs to be provided.
 * 
 * Nouns are extensible within OAGIS meaning that additional content (fields, compounds and components can be added to an existing Noun). This additional
 * content can be defined external to OAGIS and added through the use of In-Line extensions. In-Line extensions will be discussed later in this document.
 * 
 * 
 * @author dbs on Dec 25, 2015 11:13:35 AM
 * @since 0.0.1
 * @version 1.0
 * @version 1.1 add route property
 *
 */
@JsonAutoDetect(fieldVisibility = Visibility.NON_PRIVATE, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
@JsonInclude(Include.NON_ABSENT)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Noun implements Serializable{

  private static final long serialVersionUID = 1L;

  @JsonProperty("n")
  @JsonSerialize(using = NounEnumSerializer.class)
  @JsonDeserialize(using = NounEnumDeserializer.class)
  NounEnum noun = NounEnum.none;
  
  /**
   * if noun enum is not part of the standard ones, add it here 
   */
  @JsonProperty("oe")
  String nounExtra;
  
  /**
   * class object serialized via json
   */
  @JsonProperty("c")
  String clazz;

  /**
   * payload object document of type {@link NounEnum}<br>
   * and of {@link Class} in {@link #clazz}
   * @see {@linkplain https://github.com/FasterXML/jackson-docs/wiki/JacksonPolymorphicDeserialization}
   * and
   * @see {@link com.bs.bod.converter.PayloadTypeResolver}
   * <br/>
   * <strong>BEWARE</strong> if the Payload is a Collection, wrap the object in an extension such as:<br/>
     <code>
     @JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY)
     <br>
      public class ComponentInArray extends Component{}
     </code> 
   */
  @JsonProperty("o")
  @JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.EXTERNAL_PROPERTY, property="c", defaultImpl = ObjectDeserializer.class)
  //@JsonTypeResolver(PayloadTypeResolver.class)
  Object component;
  
  /**
   * {@link BodError} standard error message
   */
  @JsonProperty("err")
  BodError error;
  
  /**
   * used for filtering or routing, that information may be hidden in the payload 
   * and will not be accessible by the messaging system
   */
  @JsonProperty("r")
  String route;

  public Noun(){
  }
  /**
   * 
   * @param noun
   * @param component
   */
  public Noun(NounEnum noun, Object component) {
    this.noun = noun;
    this.clazz = component.getClass().getCanonicalName();
    this.component = component;
  }
  /**
   * 
   * @param noun
   * @param clazz
   * @param component
   */
  public Noun(String noun, String route, Class<?> clazz , Object component) {
    this.route = route;
    this.noun = NounEnum.find(noun);
    if(this.noun == NounEnum.none && !noun.startsWith(NounEnum.none.getIdentifier())){
      this.nounExtra = noun;
    }
    this.clazz = clazz.getCanonicalName();
    this.component = component;
  }
  
  public Noun(String noun) {
    this(noun, null, null, null);
  }
  /**
   * @return the error, never null
   */
  public BodError getError() {
    return error;
  }
  /**
   * 
   * @return true if an error has been set
   */
  public boolean hasError() {
    return null != error;
  }

  /**
   * @param error the error to set
   * @return 
   * @return this for convenient chaining
   */
  public Noun setError(BodError error) {
    this.error = error;
    return this;
  }

  
  @JsonIgnore
  public boolean isInstanceOf(Class<?> clazz){
    if(null != this.clazz){
      String _dataClass = clazz.getCanonicalName();
      return this.clazz.equals(_dataClass);
    }
    return false;
  }
  
  @JsonIgnore
  public Noun setDataClass(Class<?> clazz) {
    this.clazz = clazz.getCanonicalName();
    return this;
  }
  /**
   * @return the noun
   */
  public NounEnum getNoun() {
    return noun;
  }
  
  /**
   * @param noun the noun to set
   */
  public Noun setNoun(NounEnum noun) {
    this.noun = noun;
    return this;
  }
  /**
   * @return the clazz
   */
  public String getClazz() {
    return clazz;
  }
  /**
   * @param clazz the clazz to set
   */
  public Noun setClazz(String clazz) {
    this.clazz = clazz;
    return this;
  }
  /**
   * @return the component
   */
  @JsonIgnore
  public Object getComponent() {
    return component;
  }
  
  @JsonIgnore
  public boolean isSetComponent() {
    return null != component;
  }
  
  /**
   * @param component the component to set
   */
  @JsonIgnore
  public Noun setComponent(Object component) {
    this.clazz = component.getClass().getCanonicalName();
    this.component = component;
    return this;
  }
  /**
   * @return the nounExtra
   */
  public String getNounExtra() {
    return nounExtra;
  }
  
  /**
   * @param nounExtra the nounExtra to set
   * @return 
   * @return this for convenient chaining
   */
  public Noun setNounExtra(String nounExtra) {
    if(!nounExtra.startsWith(NounEnum.none.getIdentifier())){
      this.noun = NounEnum.none;
      this.nounExtra = nounExtra;
    }else
      this.nounExtra = null;
    return this;
  }
  /**
   * @return the route
   */
  public String getRoute() {
    return route;
  }

  /**
   * @param route the route to set
   * @return 
   * @return this for convenient chaining
   */
  public Noun setRoute(String route) {
    this.route = route;
    return this;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((clazz == null) ? 0 : clazz.hashCode());
    result = prime * result + ((component == null) ? 0 : component.hashCode());
    result = prime * result + ((error == null) ? 0 : error.hashCode());
    result = prime * result + ((noun == null) ? 0 : noun.hashCode());
    result = prime * result + ((nounExtra == null) ? 0 : nounExtra.hashCode());
    result = prime * result + ((route == null) ? 0 : route.hashCode());
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
    if (!(obj instanceof Noun)) {
      return false;
    }
    Noun other = (Noun) obj;
    if (clazz == null) {
      if (other.clazz != null) {
        return false;
      }
    } else if (!clazz.equals(other.clazz)) {
      return false;
    }
    if (component == null) {
      if (other.component != null) {
        return false;
      }
    } else if (!component.equals(other.component)) {
      return false;
    }
    if (error == null) {
      if (other.error != null) {
        return false;
      }
    } else if (!error.equals(other.error)) {
      return false;
    }
    if (noun != other.noun) {
      return false;
    }
    if (nounExtra == null) {
      if (other.nounExtra != null) {
        return false;
      }
    } else if (!nounExtra.equals(other.nounExtra)) {
      return false;
    }
    if (route == null) {
      if (other.route != null) {
        return false;
      }
    } else if (!route.equals(other.route)) {
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
