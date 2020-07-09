package io.github.malczuuu.telebucket.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PackTests {

  private final double tolerance = 0.0001d;

  private final Record record1 =
      Record.builder().withName("name1").withValue(20.0d).withTime(10.0d).build();
  private final Record record2 =
      Record.builder().withName("name2").withStringValue("stringValue").withTime(-4.0d).build();
  private final Record record3 =
      Record.builder().withName("name3").withBooleanValue(false).withTime(1594191594.0d).build();

  private final Pack pack = Pack.of(record1, record2, record3);

  @Test
  void shouldBeEmpty() {
    Pack emptyPack = Pack.empty();

    assertTrue(emptyPack.isEmpty());
  }

  @Test
  void shouldNotBeEmpty() {
    assertFalse(pack.isEmpty());
  }

  @Test
  void shouldBeProperSize() {
    assertEquals(3, pack.size());
  }

  @Test
  void shouldReturnProperRecord() {
    assertEquals(record2, pack.getRecord(1));
  }

  @Test
  void shouldResolveRelativeTime() {
    double acquisitionTime = 1594193584.0d;
    double expectedTime1 = acquisitionTime + record1.getTime();

    Pack resolved = pack.resolve(acquisitionTime);

    assertTrue(Math.abs(expectedTime1 - resolved.getRecord(0).getTime()) < tolerance);
  }

  @Test
  void shouldResolveAbsoluteTime() {
    double acquisitionTime = 1594193584.0d;
    double expectedTime3 = record3.getTime();

    Pack resolved = pack.resolve(acquisitionTime);

    assertTrue(Math.abs(expectedTime3 - resolved.getRecord(2).getTime()) < tolerance);
  }
}
