package com.hubspot.nebula.jersey;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * This class binds request parameters to a parameter object using Jackson.
 *
 */
// shamelessly borrowed from dropwizard-hubspot
@Singleton
public class ParameterConverter {
  private final ObjectMapper mapper;

  @Inject
  public ParameterConverter(ObjectMapper mapper) {
    //There may be query params that don't match object fields, we don't want to fail in this case
    this.mapper = mapper.copy().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        //.registerModule(new ResultSetBooleanModule());
  }

  public <T> T convert(Type type, Map<String, Object> params) throws IllegalArgumentException {
    JavaType javaType = mapper.getTypeFactory().constructType(type);
    return mapper.convertValue(expandNestedProperties(unwrapSingleElementCollections(params)), javaType);
  }

  private static <K> Map<K, Object> unwrapSingleElementCollections(Map<K, Object> map) {
    for (Map.Entry<K, Object> entry : map.entrySet()) {
      Object value = entry.getValue();

      if (value instanceof Collection) {
        Collection<?> collection = (Collection<?>) value;

        if (collection.size() == 1) {
          entry.setValue(collection.iterator().next());
        }
      }
    }

    return map;
  }

  @SuppressWarnings("unchecked")
  private static Map<String, Object> expandNestedProperties(Map<String, Object> original) {
    Map<String, Object> map = Maps.newHashMapWithExpectedSize(original.size());

    for (Map.Entry<String, Object> entry : original.entrySet()) {
      String key = entry.getKey();

      if (isNested(key)) {
        List<String> segments = Lists.newArrayList(Splitter.on('.').split(key));
        String lastSegment = segments.remove(segments.size() - 1);

        Map<String, Object> subMap = map;
        for (String segment : segments) {
          if (!subMap.containsKey(segment) || !(subMap.get(segment) instanceof Map)) {
            subMap.put(segment, Maps.<String, Object>newHashMap());
          }

          subMap = (Map<String, Object>) subMap.get(segment);
        }

        subMap.put(lastSegment, entry.getValue());
      } else {
        map.put(entry.getKey(), entry.getValue());
      }
    }

    return map;
  }

  private static boolean isNested(String key) {
    return key.contains(".");
  }
}