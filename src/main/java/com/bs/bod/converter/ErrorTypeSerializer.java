/**
 * ErrorTypeSerializer
 */
package com.bs.bod.converter;

import java.io.IOException;

import com.bs.bod.error.ErrorType;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;


/**
 * @author dbs on Dec 28, 2015 9:25:27 PM
 * @version 1.0
 * @since V0.0.1
 *
 */
public class ErrorTypeSerializer extends JsonSerializer<ErrorType> {

  @Override
  public void serialize(ErrorType value, JsonGenerator jgen, SerializerProvider serializers) throws IOException, JsonProcessingException {
    jgen.writeNumber(value.ordinal());
  }

}
