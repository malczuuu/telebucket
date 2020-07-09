package io.github.malczuuu.telebucket.model;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.malczuuu.telebucket.common.ObjectMapperFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PackDeserializationTests {

  private static final double TOLERANCE = 0.0001d;

  private static final String TEST_PACK_STRING =
      "["
          + "{ \"n\": \"name1\", \"v\": 10.2, \"t\": 10 },"
          + "{ \"n\": \"name2\", \"vs\": \"stringValue\", \"t\": -4 },"
          + "{ \"n\": \"name3\", \"vb\": false, \"t\": 1594191594 },"
          + "{ \"n\": \"name3\", \"vb\": true },"
          + "{ \"n\": \"name4\", \"vd\": \"aGVsbG8sIHdvcmxkIQ==\" },"
          + "{ \"n\": \"name5\", \"v\": 12.3, \"u\": \"l\" }"
          + "]";

  private ObjectMapper objectMapper;

  @BeforeEach
  void beforeEach() {
    objectMapper = new ObjectMapperFactory().getObjectMapper();
  }

  @Test
  void shouldDeserializeAllRecords() throws JsonProcessingException {
    Pack pack = objectMapper.readValue(TEST_PACK_STRING, Pack.class);

    assertEquals(6, pack.size());
  }

  @Test
  void shouldDeserializeName() throws JsonProcessingException {
    Pack pack = objectMapper.readValue(TEST_PACK_STRING, Pack.class);

    assertEquals("name1", pack.getRecord(0).getName());
  }

  @Test
  void shouldDeserializeValue() throws JsonProcessingException {
    double expected = 10.2d;

    Pack pack = objectMapper.readValue(TEST_PACK_STRING, Pack.class);

    assertTrue(Math.abs(pack.getRecord(0).getValue() - expected) < TOLERANCE);
  }

  @Test
  void shouldDeserializeStringValue() throws JsonProcessingException {
    Pack pack = objectMapper.readValue(TEST_PACK_STRING, Pack.class);

    assertEquals("stringValue", pack.getRecord(1).getStringValue());
  }

  @Test
  void shouldDeserializeBooleanValueFalse() throws JsonProcessingException {
    Pack pack = objectMapper.readValue(TEST_PACK_STRING, Pack.class);

    assertEquals(false, pack.getRecord(2).getBooleanValue());
  }

  @Test
  void shouldDeserializeBooleanValueTrue() throws JsonProcessingException {
    Pack pack = objectMapper.readValue(TEST_PACK_STRING, Pack.class);

    assertEquals(true, pack.getRecord(3).getBooleanValue());
  }

  @Test
  void shouldDeserializeDataValue() throws JsonProcessingException {
    Pack pack = objectMapper.readValue(TEST_PACK_STRING, Pack.class);

    assertArrayEquals("hello, world!".getBytes(), pack.getRecord(4).getDataValue().array());
  }

  @Test
  void shouldDeserializeUnit() throws JsonProcessingException {
    Pack pack = objectMapper.readValue(TEST_PACK_STRING, Pack.class);

    assertEquals("l", pack.getRecord(5).getUnit());
  }

  @Test
  void shouldDeserializeTimeRelativePositive() throws JsonProcessingException {
    double expected = 10.0d;

    Pack pack = objectMapper.readValue(TEST_PACK_STRING, Pack.class);

    assertTrue(Math.abs(pack.getRecord(0).getTime() - expected) < TOLERANCE);
  }

  @Test
  void shouldDeserializeTimeRelativeNegative() throws JsonProcessingException {
    double expected = -4.0d;

    Pack pack = objectMapper.readValue(TEST_PACK_STRING, Pack.class);

    assertTrue(Math.abs(pack.getRecord(1).getTime() - expected) < TOLERANCE);
  }

  @Test
  void shouldDeserializeTimeAbsolute() throws JsonProcessingException {
    double expected = 1594191594.0d;

    Pack pack = objectMapper.readValue(TEST_PACK_STRING, Pack.class);

    assertTrue(Math.abs(pack.getRecord(2).getTime() - expected) < TOLERANCE);
  }
}
