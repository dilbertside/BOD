/**
 * ObjectDeserializer
 */
package com.bs.bod.converter;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * Default deserialization if no object class is found it will return the string as is, to be further processed by the implementer
 * @author dbs on Dec 27, 2015 9:20:04 PM
 * @version 1.0
 * @since V0.0.1
 *
 */
public class ObjectDeserializer extends JsonDeserializer<String> {

  @Override
  public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    return jp.getText();
  }

}
