/**
 * ConfirmationCodeDeserializer
 */
package com.bs.bod.converter;

import java.io.IOException;

import com.bs.bod.ConfirmationCode;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * @author dbs on Dec 26, 2015 2:19:17 PM
 * @version 1.0
 * @since V0.0.1
 *
 */
public class ConfirmationCodeDeserializer extends JsonDeserializer<ConfirmationCode> {

  @Override
  public ConfirmationCode deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    try {
      return ConfirmationCode.values()[jp.getValueAsInt()];
    } catch (Exception e) {
    }
    return ConfirmationCode.never;
  }

}
