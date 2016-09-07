/**
 * Component
 */
package com.bs.bod;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author dbs on Dec 27, 2015 4:14:36 PM
 * @version 1.0
 * @since V0.0.1
 *
 */
public class Component {

  @JsonProperty
  public String timestamp;
  @JsonProperty
  public Long total;
  @JsonProperty
  public List<String> strings;
  
}
