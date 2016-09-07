/**
 * Component
 */
package com.bs.bod;


import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Wrapper to component to avoid  using @JsonTypeInfo in {@link Component}
 * @author dbs on Dec 27, 2015 4:14:36 PM
 * @version 1.0
 * @since V0.0.1
 *
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY)
public class ComponentInArray extends Component{}
