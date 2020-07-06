package org.example.telebucket.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonSerialize(using = Pack.Serializer.class)
@JsonDeserialize(using = Pack.Deserializer.class)
public class Pack implements Iterable<Record> {

  public static Pack empty() {
    return EMPTY;
  }

  private static final Pack EMPTY = new Pack(Collections.emptyList());

  private final List<Record> records;

  public Pack(List<Record> records) {
    this.records = records;
  }

  public Pack resolve(double acquisitionTime) {
    return new Pack(
        getRecords().stream().map(r -> r.resolve(acquisitionTime)).collect(Collectors.toList()));
  }

  @Override
  public Iterator<Record> iterator() {
    return records.iterator();
  }

  public List<Record> getRecords() {
    return Collections.unmodifiableList(records);
  }

  private Record getRecord(int i) {
    return getRecords().get(i);
  }

  public Stream<Record> stream() {
    return records.stream();
  }

  private boolean isEmpty() {
    return records.isEmpty();
  }

  private int size() {
    return records.size();
  }

  static class Serializer extends JsonSerializer<Pack> {
    @Override
    public void serialize(Pack value, JsonGenerator gen, SerializerProvider serializers)
        throws IOException {
      gen.writeObject(value.getRecords());
    }
  }

  static class Deserializer extends JsonDeserializer<Pack> {
    @Override
    public Pack deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
      return new Pack(Arrays.asList(p.readValueAs(Record[].class)));
    }
  }
}
