/**
 * PayloadTypeResolver
 */
package com.bs.bod.converter;

import java.util.Collection;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;

/**
 * @author dbs on Dec 30, 2015 5:18:57 PM
 *
 * @version 1.0
 * @since 0.0.3
 * 
 */
public class PayloadTypeResolver extends StdTypeResolverBuilder {

  @Override
  public TypeDeserializer buildTypeDeserializer(final DeserializationConfig config, final JavaType baseType, final Collection<NamedType> subtypes) {
    return new CustomPayloadTypeDeserializer(baseType, null, _typeProperty, _typeIdVisible, _defaultImpl);
  }

}
