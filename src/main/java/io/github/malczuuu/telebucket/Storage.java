package io.github.malczuuu.telebucket;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import io.github.malczuuu.telebucket.entity.BucketEntity;
import io.github.malczuuu.telebucket.entity.RecordEntity;
import io.github.malczuuu.telebucket.model.Record;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import org.bson.conversions.Bson;

public class Storage {

  private final MongoCollection<BucketEntity> buckets;
  private final long bucketSize;

  public Storage(MongoCollection<BucketEntity> buckets, long bucketSize) {
    this.buckets = buckets;
    this.bucketSize = bucketSize;
  }

  public void store(Record record) {
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

    Bson filter =
        Filters.and(
            Filters.eq(BucketEntity.NAME, record.getName()),
            Filters.eq(BucketEntity.DATE, date.toString()),
            Filters.lt(BucketEntity.SIZE, bucketSize));

    Bson update =
        Updates.combine(
            Updates.push(BucketEntity.RECORDS, entity),
            Updates.min(BucketEntity.SINCE, timeAsNanos),
            Updates.max(BucketEntity.UNTIL, timeAsNanos),
            Updates.inc(BucketEntity.SIZE, 1L));

    UpdateOptions options = new UpdateOptions().upsert(true);

    buckets.updateOne(filter, update, options);
  }
}
