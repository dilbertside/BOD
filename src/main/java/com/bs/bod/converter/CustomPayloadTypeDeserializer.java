/**
 * CustomPayloadTypeDeserializer
 */
package com.bs.bod.converter;

import java.io.IOException;
import java.util.Collection;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * from http://www.dilipkumarg.com/dynamic-polymorphic-type-handling-jackson/
 * 
 * @author dbs on Dec 30, 2015 5:16:06 PM
 *
 * @since 0.0.3
 * @version 1.0
 * @version 1.1 fixed error form upgrade to 2.9.6
 * 
 */
public class CustomPayloadTypeDeserializer extends AsPropertyTypeDeserializer {

  private static final long serialVersionUID = 7616195001095498468L;

  public CustomPayloadTypeDeserializer(final JavaType bt, final TypeIdResolver idRes, final String typePropertyName, final boolean typeIdVisible,
      final Class<?> defaultImpl) {
    super(bt, idRes, typePropertyName, typeIdVisible, TypeFactory.defaultInstance().constructFromCanonical(defaultImpl.getCanonicalName()));
  }

  public CustomPayloadTypeDeserializer(final AsPropertyTypeDeserializer src, final BeanProperty property) {
    super(src, property);
  }

  @Override
  public TypeDeserializer forProperty(final BeanProperty prop) {
    return (prop == _property) ? this : new CustomPayloadTypeDeserializer(this, prop);
  }

  @Override
  public Object deserializeTypedFromObject(final JsonParser jp, final DeserializationContext ctxt) throws IOException {
    JsonNode node = jp.readValueAsTree();
    Class<?> subType = findSubType(node);
    JavaType type = //com.fasterxml.jackson.databind.type.SimpleType.construct(subType);
    TypeFactory.defaultInstance().constructParametricType(Collection.class, subType);
    JsonParser jsonParser = new TreeTraversingParser(node, jp.getCodec());
    if (jsonParser.getCurrentToken() == null) {
      jsonParser.nextToken();
    }
    /*
     * 16-Dec-2010, tatu: Since nominal type we get here has no (generic) type parameters, we actually now need to explicitly narrow from base type (which may
     * have parameterization) using raw type.
     *
     * One complication, though; can not change 'type class' (simple type to container); otherwise we may try to narrow a SimpleType (Object.class) into MapType
     * (Map.class), losing actual type in process (getting SimpleType of Map.class which will not work as expected)
     */
    if (_baseType != null && _baseType.getClass() == type.getClass()) {
      type = _baseType.forcedNarrowBy(type.getRawClass());
    }
    JsonDeserializer<Object> deser = ctxt.findContextualValueDeserializer(type, _property);
    return deser.deserialize(jsonParser, ctxt);
  }

  protected Class<?> findSubType(JsonNode node) throws IOException {
    Class<? /* extends Property */> subType = null;
    try {
      String type = node.get("@class").asText();
      subType = ClassUtils.forName(type, null);
    } catch (ClassNotFoundException | LinkageError e) {
      throw new IOException(e.getCause());
    }
    /*
     * if (type.equals("number")) { String format = node.get("format").asText(); if (format.equals("int32")) { subType = IntegerProperty.class; } else if
     * (format.equals("int64")) { subType = LongProperty.class; } } else if (type.equals("string")) { String format = node.get("format").asText(); if
     * (format.equals("date")) { subType = DateProperty.class; } else if (format.equals("null")) { // string don't have format subType = StringProperty.class; }
     * }
     */
    return subType;
  }
}
