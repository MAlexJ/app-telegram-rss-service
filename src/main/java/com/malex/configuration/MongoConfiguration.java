package com.malex.configuration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Slf4j
@Configuration
@EnableMongoAuditing
@RequiredArgsConstructor
public class MongoConfiguration extends AbstractMongoClientConfiguration {

  private final MongoProperties mongoProperties;

  @Bean
  public MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
    log.info("MongoDb configuration MongoTransactionManager");
    return new MongoTransactionManager(dbFactory);
  }

  @Override
  protected String getDatabaseName() {
    String database = Objects.requireNonNull(mongoProperties.getDatabase());
    log.info("MongoDb configuration database properties: {}", database);
    return mongoProperties.getDatabase();
  }

  @Override
  public MongoClient mongoClient() {
    String uri = Objects.requireNonNull(mongoProperties.getUri());
    log.info("MongoDb configuration URI property: {}", uri);
    return MongoClients.create(uri);
  }
}
