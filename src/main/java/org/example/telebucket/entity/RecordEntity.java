package org.example.telebucket.entity;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.example.telebucket.common.Escape;

public class RecordEntity {

  private static final String VALUE = "v";
  private static final String STRING_VALUE = "vs";
  private static final String BOOLEAN_VALUE = "vb";
  private static final String DATA_VALUE = "vd";
  private static final String TIME = "t";
  private static final String UNIT = "u";
  private static final String ARRIVAL_TIME = "at";

  private final Double value;
  private final String stringValue;
  private final Boolean booleanValue;
  private final byte[] dataValue;
  private final Long time;
  private final String unit;
  private final Long arrivalTime;

  @BsonCreator
  public RecordEntity(
      @BsonProperty(VALUE) Double value,
      @BsonProperty(STRING_VALUE) String stringValue,
      @BsonProperty(BOOLEAN_VALUE) Boolean booleanValue,
      @BsonProperty(DATA_VALUE) byte[] dataValue,
      @BsonProperty(TIME) Long time,
      @BsonProperty(UNIT) String unit,
      @BsonProperty(ARRIVAL_TIME) Long arrivalTime) {
    this.value = value;
    this.stringValue = stringValue;
    this.booleanValue = booleanValue;
    this.dataValue = dataValue;
    this.time = time;
    this.unit = unit;
    this.arrivalTime = arrivalTime;
  }

  @BsonProperty(VALUE)
  public Double getValue() {
    return value;
  }

  @BsonProperty(STRING_VALUE)
  public String getStringValue() {
    return stringValue;
  }

  @BsonProperty(BOOLEAN_VALUE)
  public Boolean getBooleanValue() {
    return booleanValue;
  }

  @BsonProperty(DATA_VALUE)
  public byte[] getDataValue() {
    return dataValue;
  }

  @BsonProperty(TIME)
  public Long getTime() {
    return time;
  }

  @BsonProperty(UNIT)
  public String getUnit() {
    return unit;
  }

  @BsonProperty(ARRIVAL_TIME)
  public Long getArrivalTime() {
    return arrivalTime;
  }

  @Override
  public String toString() {
    Escape escape = Escape.getEscape();
    List<String> lines = new ArrayList<>();
    if (getValue() != null) {
      lines.add("\"" + VALUE + "\": " + getValue());
    }
    if (getStringValue() != null) {
      lines.add("\"" + STRING_VALUE + "\": \"" + escape.escape(getStringValue()) + "\"");
    }
    if (getBooleanValue() != null) {
      lines.add("\"" + BOOLEAN_VALUE + "\": " + getBooleanValue());
    }
    if (getDataValue() != null) {
      String base64encoded = escape.escape(Base64.getEncoder().encodeToString(getDataValue()));
      lines.add("\"" + DATA_VALUE + "\": \"" + base64encoded + "\"");
    }
    if (getTime() != null) {
      lines.add("\"" + TIME + "\": " + getTime());
    }
    if (getUnit() != null) {
      lines.add("\"" + UNIT + "\": \"" + escape.escape(getUnit()) + "\"");
    }
    if (getArrivalTime() != null) {
      lines.add("\"" + ARRIVAL_TIME + "\": " + getArrivalTime());
    }
    return "{ " + String.join(", ", lines) + " }";
  }
}
