package com.malex.repository;

import com.malex.model.entity.SubscriptionEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends MongoRepository<SubscriptionEntity, String> {

  @Query("{'id': ?0 }")
  @Update(update = "{ $set: { isActive : false }}")
  Integer updateRssSubscriptionEntity(String id);

  @Query("{'isActive': ?0 }")
  List<SubscriptionEntity> findAllByActive(boolean active);
}
