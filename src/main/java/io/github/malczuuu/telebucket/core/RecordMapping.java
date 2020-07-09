package io.github.malczuuu.telebucket.core;

import io.github.malczuuu.telebucket.entity.RecordEntity;
import java.time.LocalDate;

public class RecordMapping {

  private final RecordEntity entity;
  private final LocalDate date;
  private final long timeAsNanos;

  public RecordMapping(RecordEntity entity, LocalDate date, long timeAsNanos) {
    this.entity = entity;
    this.date = date;
    this.timeAsNanos = timeAsNanos;
  }

  public RecordEntity getEntity() {
    return entity;
  }

  public LocalDate getDate() {
    return date;
  }

  public long getTimeAsNanos() {
    return timeAsNanos;
  }

  @Override
  public String toString() {
    return "RecordMapping( " + entity.toString() + " )";
  }
}
