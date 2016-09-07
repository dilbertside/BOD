/**
 * ConfirmationCodeSerializer
 */
package com.bs.bod.converter;

import java.io.IOException;

import com.bs.bod.ConfirmationCode;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @author dbs on Dec 26, 2015 2:19:43 PM
 * @version 1.0
 * @since V0.0.1
 *
 */
public class ConfirmationCodeSerializer extends JsonSerializer<ConfirmationCode> {

  @Override
  public void serialize(ConfirmationCode value, JsonGenerator jgen, SerializerProvider serializers) throws IOException, JsonProcessingException {
    jgen.writeNumber(value.ordinal());
  }

}
