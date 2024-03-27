package com.malex.repository;

import com.malex.model.entity.TemplateEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateRepository extends MongoRepository<TemplateEntity, String> {

  @Query("{'id': ?0 }")
  @Update(update = "{ $set: { template : ?1 }}")
  Long updateMessageTemplateEntityBy(String id, String template);
}
