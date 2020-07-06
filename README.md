# Telebucket

Persist telemetry messages in NoSQL document database using bucket model.

## Table of Contents

* [Description](#description)
* [Running](#running)
* [Configuration](#configuration)

## Description

Application reads SenML-like messages from RabbitMQ queue and stores it in MongoDB, aggregating
records which share name and date of arrival (processing) timestamp.

User can publish a message using MQTT and `mosquitto_pub`:

```shell
$ mosquitto_pub -u user -P user -t telebucket/device -m '[{ "n": "name", "v": 23 }]'
```

The bucket stored within MongoDB may look as following:

```
{
    "date" : "2020-07-06",
    "name" : "name",
    "since" : 1594048892269558016,
    "size" : 9,
    "until" : 1594048892454284032,
    "records" : [ 
        { "v" : 12.00, "t" : 1594048892269558016, "at" : 1594048892269558016 }, 
        { "v" : 12.33, "t" : 1594048892425519104, "at" : 1594048892425519104 }, 
        { "v" : 12.67, "t" : 1594048892429788160, "at" : 1594048892429788160 }, 
        { "v" : 13.00, "t" : 1594048892432544000, "at" : 1594048892432544000 }, 
        { "v" : 13.33, "t" : 1594048892436172032, "at" : 1594048892436172032 }, 
        { "v" : 13.67, "t" : 1594048892439342848, "at" : 1594048892439342848 }, 
        { "v" : 14.00, "t" : 1594048892442787840, "at" : 1594048892442787840 }, 
        { "v" : 14.33, "t" : 1594048892450636032, "at" : 1594048892450636032 }, 
        { "v" : 14.68, "t" : 1594048892454284032, "at" : 1594048892454284032 }
    ]
}
```

## Running

Use [`docker-compose.yml`](./docker-compose.yml) to setup development environment (RabbitMQ and
MongoDB).

```shell
$ ./gradlew clean build
$ java -jar build/libs/telebucket-jar-with-dependencies.jar
```

## Configuration

|               JVM option                |        default value        |
| --------------------------------------- | --------------------------- |
| `telebucket.rabbit-host`                | `localhost`                 |
| `telebucket.rabbit-port`                | `5672`                      |
| `telebucket.rabbit-username`            | `user`                      |
| `telebucket.rabbit-password`            | `user`                      |
| `telebucket.rabbit-exchange`            | `amq.topic`                 |
| `telebucket.rabbit-exchange-type`       | `topic`                     |
| `telebucket.rabbit-binding-routing-key` | `telebucket.*`              |
| `telebucket.rabbit-queue`               | `telebucket.v1`             |
| `telebucket.mongo-uri`                  | `mongodb://localhost:27017` |
| `telebucket.mongo-database`             | `telebucket`                |
| `telebucket.bucket-size`                | `200`                       |

The meanings of above configuration properties are pretty self-explanatory :D.
