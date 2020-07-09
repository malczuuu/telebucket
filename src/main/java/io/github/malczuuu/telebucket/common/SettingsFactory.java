package io.github.malczuuu.telebucket.common;

import java.util.Properties;

class SettingsFactory {

  Settings createSettings(Properties properties) {
    return new Settings(
        properties.getProperty("telebucket.rabbit-host", "localhost"),
        Integer.parseInt(properties.getProperty("telebucket.rabbit-port", "5672")),
        properties.getProperty("telebucket.rabbit-username", "user"),
        properties.getProperty("telebucket.rabbit-password", "user"),
        properties.getProperty("telebucket.rabbit-exchange", "amq.topic"),
        properties.getProperty("telebucket.rabbit-exchange-type", "topic"),
        properties.getProperty("telebucket.rabbit-binding-routing-key", "telebucket.*"),
        properties.getProperty("telebucket.rabbit-queue", "telebucket.v1"),
        properties.getProperty("telebucket.mongo-uri", "mongodb://localhost:27017"),
        properties.getProperty("telebucket.mongo-database", "telebucket"),
        Integer.parseInt(properties.getProperty("telebucket.bucket-size", "200")));
  }
}
