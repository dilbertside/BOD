/**
 * VerbEnumDeserializer
 */
package com.bs.bod.converter;

import java.io.IOException;

import com.bs.bod.VerbEnum;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * @author dbs on Dec 26, 2015 2:13:37 PM
 * @version 1.0
 * @since V0.0.1
 *
 */
public class VerbEnumDeserializer extends JsonDeserializer<VerbEnum> {

  @Override
  public VerbEnum deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    try {
      return VerbEnum.values()[jp.getValueAsInt()];
    } catch (Exception e) {
    }
    return VerbEnum.none;
  }

}
