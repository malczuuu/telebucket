package io.github.malczuuu.telebucket.core;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import io.github.malczuuu.telebucket.entity.BucketEntity;
import io.github.malczuuu.telebucket.model.Record;
import org.bson.conversions.Bson;

public class Storage {

  private final RecordMapper recordMapper = new RecordMapper();
  private final MongoCollection<BucketEntity> buckets;
  private final long bucketSize;

  public Storage(MongoCollection<BucketEntity> buckets, long bucketSize) {
    this.buckets = buckets;
    this.bucketSize = bucketSize;
  }

  public void store(Record record) {
    RecordMapping mapping = recordMapper.map(record);

    Bson filter =
        Filters.and(
            Filters.eq(BucketEntity.NAME, record.getName()),
            Filters.eq(BucketEntity.DATE, mapping.getDate().toString()),
            Filters.lt(BucketEntity.SIZE, bucketSize));

    Bson update =
        Updates.combine(
            Updates.push(BucketEntity.RECORDS, mapping.getEntity()),
            Updates.min(BucketEntity.SINCE, mapping.getTimeAsNanos()),
            Updates.max(BucketEntity.UNTIL, mapping.getTimeAsNanos()),
            Updates.inc(BucketEntity.SIZE, 1L));

    UpdateOptions options = new UpdateOptions().upsert(true);

    buckets.updateOne(filter, update, options);
  }
}
