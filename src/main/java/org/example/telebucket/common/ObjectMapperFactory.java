package org.example.telebucket.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;

/** Prepares ObjectMapper with default configuration flags turned. */
public class ObjectMapperFactory {

  public ObjectMapper getObjectMapper() {
    return new ObjectMapper()
        .setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL)
        .configure(Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);
  }
}
