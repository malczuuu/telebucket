package io.github.malczuuu.telebucket.common;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Properties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SettingsFactoryTests {

  private final Settings defaultSettings =
      new Settings(
          "localhost",
          5672,
          "user",
          "user",
          "amq.topic",
          "topic",
          "telebucket.*",
          "telebucket.v1",
          "mongodb://localhost:27017",
          "telebucket",
          200);

  private Properties properties;

  @BeforeEach
  void beforeEach() {
    properties = new Properties();
  }

  @Test
  void shouldCreateDefaultSettings() {
    Settings settings = new SettingsFactory().createSettings(properties);

    assertEquals(defaultSettings.getRabbitHost(), settings.getRabbitHost());
    assertEquals(defaultSettings.getRabbitPort(), settings.getRabbitPort());
    assertEquals(defaultSettings.getRabbitUsername(), settings.getRabbitUsername());
    assertEquals(defaultSettings.getRabbitPassword(), settings.getRabbitPassword());
    assertEquals(defaultSettings.getRabbitExchange(), settings.getRabbitExchange());
    assertEquals(defaultSettings.getRabbitExchangeType(), settings.getRabbitExchangeType());
    assertEquals(
        defaultSettings.getRabbitBindingRoutingKey(), settings.getRabbitBindingRoutingKey());
    assertEquals(defaultSettings.getRabbitQueue(), settings.getRabbitQueue());
    assertEquals(defaultSettings.getMongoUri(), settings.getMongoUri());
    assertEquals(defaultSettings.getMongoDatabase(), settings.getMongoDatabase());
    assertEquals(defaultSettings.getBucketSize(), settings.getBucketSize());
  }
}
