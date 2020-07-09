package io.github.malczuuu.telebucket.common;

import java.util.Properties;

public class Settings {

  public static Settings fromSystemProperties() {
    Properties properties = System.getProperties();
    return new SettingsFactory().createSettings(properties);
  }

  private final String rabbitHost;
  private final int rabbitPort;
  private final String rabbitUsername;
  private final String rabbitPassword;
  private final String rabbitExchange;
  private final String rabbitExchangeType;
  private final String rabbitBindingRoutingKey;
  private final String rabbitQueue;
  private final String mongoUri;
  private final String mongoDatabase;
  private final long bucketSize;

  public Settings(
      String rabbitHost,
      int rabbitPort,
      String rabbitUsername,
      String rabbitPassword,
      String rabbitExchange,
      String rabbitExchangeType,
      String rabbitBindingRoutingKey,
      String rabbitQueue,
      String mongoUri,
      String mongoDatabase,
      long bucketSize) {
    this.rabbitHost = rabbitHost;
    this.rabbitPort = rabbitPort;
    this.rabbitUsername = rabbitUsername;
    this.rabbitPassword = rabbitPassword;
    this.rabbitExchange = rabbitExchange;
    this.rabbitExchangeType = rabbitExchangeType;
    this.rabbitBindingRoutingKey = rabbitBindingRoutingKey;
    this.rabbitQueue = rabbitQueue;
    this.mongoUri = mongoUri;
    this.mongoDatabase = mongoDatabase;
    this.bucketSize = bucketSize;
  }

  public String getRabbitHost() {
    return rabbitHost;
  }

  public int getRabbitPort() {
    return rabbitPort;
  }

  public String getRabbitUsername() {
    return rabbitUsername;
  }

  public String getRabbitPassword() {
    return rabbitPassword;
  }

  public String getRabbitExchange() {
    return rabbitExchange;
  }

  public String getRabbitExchangeType() {
    return rabbitExchangeType;
  }

  public String getRabbitBindingRoutingKey() {
    return rabbitBindingRoutingKey;
  }

  public String getRabbitQueue() {
    return rabbitQueue;
  }

  public String getMongoUri() {
    return mongoUri;
  }

  public String getMongoDatabase() {
    return mongoDatabase;
  }

  public long getBucketSize() {
    return bucketSize;
  }
}
