package io.github.malczuuu.telebucket.entity;

import io.github.malczuuu.telebucket.common.Escape;
import java.util.ArrayList;
import java.util.List;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

public class BucketEntity {

  public static final String COLLECTION = "buckets";

  public static final String NAME = "name";
  public static final String SINCE = "since";
  public static final String UNTIL = "until";
  public static final String DATE = "date";
  public static final String SIZE = "size";
  public static final String RECORDS = "records";

  private final ObjectId id;
  private final String name;
  private final long since;
  private final long until;
  private final String date;
  private final int size;
  private final List<RecordEntity> records;

  @BsonCreator
  public BucketEntity(
      @BsonId ObjectId id,
      @BsonProperty(NAME) String name,
      @BsonProperty(SINCE) long since,
      @BsonProperty(UNTIL) long until,
      @BsonProperty(DATE) String date,
      @BsonProperty(SIZE) int size,
      @BsonProperty(RECORDS) List<RecordEntity> records) {
    this.id = id;
    this.name = name;
    this.size = size;
    this.since = since;
    this.until = until;
    this.date = date;
    this.records = records;
  }

  @BsonId
  public ObjectId getId() {
    return id;
  }

  @BsonProperty(NAME)
  public String getName() {
    return name;
  }

  @BsonProperty(SINCE)
  public long getSince() {
    return since;
  }

  @BsonProperty(UNTIL)
  public long getUntil() {
    return until;
  }

  @BsonProperty(DATE)
  public String getDate() {
    return date;
  }

  @BsonProperty(SIZE)
  public int getSize() {
    return size;
  }

  @BsonProperty(RECORDS)
  public List<RecordEntity> getRecords() {
    return records;
  }

  @Override
  public String toString() {
    Escape escape = Escape.getEscape();
    List<String> lines = new ArrayList<>(5);
    lines.add("\"" + NAME + "\": \"" + escape.escape(name) + "\"");
    lines.add("\"" + SINCE + "\": " + since);
    lines.add("\"" + UNTIL + "\": " + until);
    lines.add("\"" + DATE + "\": \"" + date + "\"");
    lines.add("\"" + SIZE + "\": " + size);
    return "{ " + String.join(", ", lines) + " }";
  }
}
