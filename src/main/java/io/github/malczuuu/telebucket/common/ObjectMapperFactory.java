package io.github.malczuuu.telebucket.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/** Prepares ObjectMapper with default configuration flags turned. */
public class ObjectMapperFactory {

  public ObjectMapper getObjectMapper() {
    return new ObjectMapper()
        .setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);
  }
}
