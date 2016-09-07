/**
 * SeveritySerializer
 */
package com.bs.bod.converter;

import java.io.IOException;

import com.bs.bod.error.Severity;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;


/**
 * @author dbs on Dec 28, 2015 9:23:06 PM
 * @version 1.0
 * @since V0.0.2
 *
 */
public class SeveritySerializer extends JsonSerializer<Severity> {

  @Override
  public void serialize(Severity value, JsonGenerator jgen, SerializerProvider serializers) throws IOException, JsonProcessingException {
    jgen.writeNumber(value.ordinal());
  }

}
