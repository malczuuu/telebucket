package io.github.malczuuu.telebucket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import io.github.malczuuu.telebucket.common.ObjectMapperFactory;
import io.github.malczuuu.telebucket.common.Settings;
import io.github.malczuuu.telebucket.core.Storage;
import io.github.malczuuu.telebucket.entity.BucketEntity;
import io.github.malczuuu.telebucket.model.Pack;
import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {

  private static final Logger log = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) {
    new Application().run();
  }

  private final Settings settings = Settings.fromSystemProperties();
  private final ObjectMapper mapper = new ObjectMapperFactory().getObjectMapper();
  private final Clock clock = Clock.systemUTC();

  private void run() {
    try {
      MongoClient mongo = connectToMongo();
      Runtime.getRuntime().addShutdownHook(new Thread(() -> close(mongo)));
      Connection connection = connectToRabbit();
      Runtime.getRuntime().addShutdownHook(new Thread(() -> close(connection)));

      MongoDatabase database = mongo.getDatabase(settings.getMongoDatabase());
      MongoCollection<BucketEntity> buckets =
          database.getCollection(BucketEntity.COLLECTION, BucketEntity.class);
      Storage storage = new Storage(buckets, settings.getBucketSize());

      Channel channel = connection.createChannel();

      channel.exchangeDeclare(
          settings.getRabbitExchange(),
          settings.getRabbitExchangeType(),
          true,
          false,
          new HashMap<>());

      channel.queueDeclare(settings.getRabbitQueue(), true, false, false, new HashMap<>());

      channel.queueBind(
          settings.getRabbitQueue(),
          settings.getRabbitExchange(),
          settings.getRabbitBindingRoutingKey());

      channel.basicConsume(
          settings.getRabbitQueue(),
          new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(
                String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
                throws IOException {
              Instant now = clock.instant();
              double acquisitionTime =
                  (double) now.getEpochSecond() + 0.000_000_001 * now.getNano();
              Pack pack;
              try {
                pack = mapper.readValue(body, Pack.class);
              } catch (JsonProcessingException e) {
                if (log.isDebugEnabled()) {
                  String base64encoded = Base64.getEncoder().encodeToString(body);
                  log.debug("Ignore invalid message (base64-encoded) '{}'", base64encoded);
                }
                return;
              }
              pack = pack.resolve(acquisitionTime);
              pack.forEach(storage::store);
            }
          });
    } catch (TimeoutException | IOException e) {
      log.error("Failed to bootstrap application", e);
      System.exit(-1);
    }
  }

  private MongoClient connectToMongo() {
    ConnectionString connectionString = new ConnectionString(this.settings.getMongoUri());
    CodecRegistry pojoCodecRegistry =
        CodecRegistries.fromRegistries(
            MongoClientSettings.getDefaultCodecRegistry(),
            CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
    MongoClientSettings settings =
        MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .codecRegistry(pojoCodecRegistry)
            .build();
    return MongoClients.create(settings);
  }

  private Connection connectToRabbit() throws IOException, TimeoutException {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(settings.getRabbitHost());
    factory.setPort(settings.getRabbitPort());
    factory.setUsername(settings.getRabbitUsername());
    factory.setPassword(settings.getRabbitPassword());
    factory.setVirtualHost("/");
    return factory.newConnection();
  }

  private void close(MongoClient mongo) {
    mongo.close();
    log.info("Closed connection with MongoDB");
  }

  private void close(Connection connection) {
    for (int i = 0; i < 5; ++i) {
      try {
        connection.close();
        log.info("Closed connection with Rabbit");
        break;
      } catch (IOException e) {
        log.error("Unable to gracefully close Rabbit connection. Attempt {}", i + 1, e);
      }
    }
  }
}
