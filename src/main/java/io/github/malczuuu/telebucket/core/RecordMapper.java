package io.github.malczuuu.telebucket.core;

import io.github.malczuuu.telebucket.entity.RecordEntity;
import io.github.malczuuu.telebucket.model.Record;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

public class RecordMapper {

  public RecordMapping map(Record record) {
    long timeAsNanos = (long) (record.getTime() * 1000_000_000L);
    long seconds = timeAsNanos / 1000_000_000L;
    long nanos = timeAsNanos % 1000_000_000L;
    Instant time = Instant.ofEpochSecond(seconds, nanos);
    LocalDate date = LocalDate.ofInstant(time, ZoneOffset.UTC);

    RecordEntity entity =
        new RecordEntity(
            record.getValue(),
            record.getStringValue(),
            record.getBooleanValue(),
            record.getDataValue() != null ? record.getDataValue().array() : null,
            timeAsNanos,
            record.getUnit(),
            timeAsNanos);

    return new RecordMapping(entity, date, timeAsNanos);
  }
}
