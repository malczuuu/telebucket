package org.example.telebucket.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import org.example.telebucket.common.Escape;

@JsonSerialize(using = Record.Serializer.class)
@JsonDeserialize(using = Record.Deserializer.class)
public class Record {

  public static Builder builder() {
    return new Builder();
  }

  private final String name;
  private final Double value;
  private final String stringValue;
  private final Boolean booleanValue;
  private final ByteBuffer dataValue;
  private final Double time;
  private final String unit;

  private Record(
      String name,
      Double value,
      String stringValue,
      Boolean booleanValue,
      ByteBuffer dataValue,
      Double time,
      String unit) {
    this.name = name;
    this.value = value;
    this.stringValue = stringValue;
    this.booleanValue = booleanValue;
    this.dataValue = dataValue != null ? dataValue.duplicate() : null;
    this.time = time;
    this.unit = unit;
  }

  private Record(
      String name,
      Double value,
      String stringValue,
      Boolean booleanValue,
      byte[] dataValue,
      Double time,
      String unit) {
    this(
        name,
        value,
        stringValue,
        booleanValue,
        dataValue != null ? ByteBuffer.wrap(dataValue) : null,
        time,
        unit);
  }

  public boolean isValid() {
    int values = 0;
    if (value != null) {
      ++values;
    }
    if (stringValue != null) {
      ++values;
    }
    if (booleanValue != null) {
      ++values;
    }
    if (dataValue != null) {
      ++values;
    }
    return name != null && values <= 1;
  }

  public Record resolve(double acquisitionTime) {
    double time = getTime() != null ? getTime() : 0;
    if (time < 268_435_456.0d) {
      time = acquisitionTime + time;
    }
    return new Record(
        getName(),
        getValue(),
        getStringValue(),
        getBooleanValue(),
        getDataValue(),
        time,
        getUnit());
  }

  public String getName() {
    return name;
  }

  public Double getValue() {
    return value;
  }

  public String getStringValue() {
    return stringValue;
  }

  public Boolean getBooleanValue() {
    return booleanValue;
  }

  public ByteBuffer getDataValue() {
    return dataValue != null ? dataValue.duplicate() : null;
  }

  public Double getTime() {
    return time;
  }

  public String getUnit() {
    return unit;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Record that = (Record) obj;
    return Objects.equals(getName(), that.getName())
        && Objects.equals(getValue(), that.getValue())
        && Objects.equals(getStringValue(), that.getStringValue())
        && Objects.equals(getBooleanValue(), that.getBooleanValue())
        && Objects.equals(getDataValue(), that.getDataValue())
        && Objects.equals(getTime(), that.getTime())
        && Objects.equals(getUnit(), that.getUnit());
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        getName(),
        getValue(),
        getStringValue(),
        getBooleanValue(),
        getDataValue(),
        getTime(),
        getUnit());
  }

  @Override
  public String toString() {
    Escape escape = Escape.getEscape();
    List<String> lines = new ArrayList<>();
    if (getName() != null) {
      lines.add("\"n\": \"" + escape.escape(getName()) + "\"");
    }
    if (getValue() != null) {
      lines.add("\"v\": " + getValue());
    }
    if (getStringValue() != null) {
      lines.add("\"vs\": \"" + escape.escape(getStringValue()) + "\"");
    }
    if (getBooleanValue() != null) {
      lines.add("\"vb\": " + getBooleanValue());
    }
    if (getDataValue() != null) {
      lines.add(
          "\"vd\": \""
              + escape.escape(Base64.getEncoder().encodeToString(getDataValue().array()))
              + "\"");
    }
    if (getTime() != null) {
      lines.add("\"t\": " + getTime());
    }
    if (getUnit() != null) {
      lines.add("\"u\": \"" + escape.escape(getUnit()) + "\"");
    }
    return "{ " + String.join(", ", lines) + " }";
  }

  static class Deserializer extends JsonDeserializer<Record> {
    @Override
    public Record deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
      JsonNode json = p.readValueAsTree();

      JsonNode nameNode = json.get("n");
      JsonNode valueNode = json.get("v");
      JsonNode stringValueNode = json.get("vs");
      JsonNode booleanValueNode = json.get("vb");
      JsonNode dataValueNode = json.get("vd");
      JsonNode timeNode = json.get("t");
      JsonNode unitNode = json.get("u");

      return new Record(
          nameNode != null ? nameNode.asText() : null,
          valueNode != null ? valueNode.asDouble() : null,
          stringValueNode != null ? stringValueNode.asText() : null,
          booleanValueNode != null ? booleanValueNode.asBoolean() : null,
          dataValueNode != null ? Base64.getDecoder().decode(dataValueNode.asText()) : null,
          timeNode != null ? timeNode.asDouble() : null,
          unitNode != null ? unitNode.asText() : null);
    }
  }

  static class Serializer extends JsonSerializer<Record> {
    @Override
    public void serialize(Record value, JsonGenerator gen, SerializerProvider serializers)
        throws IOException {
      gen.writeStartObject();
      if (value.getName() != null) {
        gen.writeStringField("n", value.getName());
      }
      if (value.getValue() != null) {
        gen.writeNumberField("v", value.getValue());
      }
      if (value.getStringValue() != null) {
        gen.writeStringField("vs", value.getStringValue());
      }
      if (value.getBooleanValue() != null) {
        gen.writeBooleanField("vb", value.getBooleanValue());
      }
      if (value.getDataValue() != null) {
        gen.writeStringField(
            "vd", Base64.getEncoder().encodeToString(value.getDataValue().array()));
      }
      if (value.getTime() != null) {
        gen.writeNumberField("t", value.getTime());
      }
      if (value.getUnit() != null) {
        gen.writeStringField("u", value.getUnit());
      }
      gen.writeEndObject();
    }
  }

  public static class Builder {

    private String name;
    private Double value;
    private String stringValue;
    private Boolean booleanValue;
    private ByteBuffer dataValue;
    private Double time;
    private String unit;

    public Builder() {}

    public Builder withName(String name) {
      this.name = name;
      return this;
    }

    public Builder withValue(double value) {
      return withValue(Double.valueOf(value));
    }

    public Builder withValue(Double value) {
      this.value = value;
      this.stringValue = null;
      this.booleanValue = null;
      this.dataValue = null;
      return this;
    }

    public Builder withStringValue(String stringValue) {
      this.value = null;
      this.stringValue = stringValue;
      this.booleanValue = null;
      this.dataValue = null;
      return this;
    }

    public Builder withBooleanValue(Boolean booleanValue) {
      this.value = null;
      this.stringValue = null;
      this.booleanValue = booleanValue;
      this.dataValue = null;
      return this;
    }

    public Builder withDataValue(ByteBuffer dataValue) {
      this.value = null;
      this.stringValue = null;
      this.booleanValue = null;
      this.dataValue = dataValue;
      return this;
    }

    public Builder withDataValue(byte[] dataValue) {
      this.value = null;
      this.stringValue = null;
      this.booleanValue = null;
      this.dataValue = dataValue != null ? ByteBuffer.wrap(dataValue) : null;
      return this;
    }

    public Builder withTime(double time) {
      this.time = time;
      return this;
    }

    public Builder withTime(Double time) {
      this.time = time;
      return this;
    }

    public Builder withUnit(String unit) {
      this.unit = unit;
      return this;
    }

    public Record build() {
      return new Record(name, value, stringValue, booleanValue, dataValue, time, unit);
    }
  }
}
