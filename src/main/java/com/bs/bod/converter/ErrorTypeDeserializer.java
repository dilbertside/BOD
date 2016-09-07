/**
 * ErrorTypeDeserializer
 */
package com.bs.bod.converter;

import java.io.IOException;

import com.bs.bod.error.ErrorType;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * @author dbs on Dec 28, 2015 9:23:56 PM
 * @version 1.0
 * @since V0.0.2
 *
 */
public class ErrorTypeDeserializer extends JsonDeserializer<ErrorType> {

  @Override
  public ErrorType deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    try {
      return ErrorType.values()[jp.getValueAsInt()];
    } catch (Exception e) {
    }
    return ErrorType.none;
  }

}
