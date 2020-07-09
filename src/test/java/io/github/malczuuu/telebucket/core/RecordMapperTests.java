package io.github.malczuuu.telebucket.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.malczuuu.telebucket.model.Record;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecordMapperTests {

  private final double tolerance = 0.0001d;

  private final Record record =
      Record.builder()
          .withName("name")
          .withValue(20.3d)
          .withTime(1594191594.0d)
          .withUnit("l")
          .build();

  private RecordMapper recordMapper;

  @BeforeEach
  void beforeEach() {
    recordMapper = new RecordMapper();
  }

  @Test
  void shouldMapDate() {
    RecordMapping mapping = recordMapper.map(record);

    assertEquals(LocalDate.of(2020, 7, 8), mapping.getDate());
  }

  @Test
  void shouldMapTimeAsNanos() {
    RecordMapping mapping = recordMapper.map(record);

    long expectedTimeAsNanos = 1594191594000000000L;
    assertEquals(expectedTimeAsNanos, mapping.getTimeAsNanos());
  }

  @Test
  void shouldMapEntity() {
    RecordMapping mapping = recordMapper.map(record);

    double expectedValue = 20.3d;
    assertTrue(Math.abs(mapping.getEntity().getValue() - expectedValue) < tolerance);

    long expectedTimeAsNanos = 1594191594000000000L;
    assertEquals(expectedTimeAsNanos, mapping.getEntity().getTime());
    assertEquals(expectedTimeAsNanos, mapping.getEntity().getArrivalTime());

    assertEquals("l", mapping.getEntity().getUnit());

    assertNull(mapping.getEntity().getStringValue());
    assertNull(mapping.getEntity().getBooleanValue());
    assertNull(mapping.getEntity().getDataValue());
  }
}
