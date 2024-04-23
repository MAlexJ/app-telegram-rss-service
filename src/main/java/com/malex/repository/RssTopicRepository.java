package com.malex.repository;

import com.malex.model.entity.RssTopicEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface RssTopicRepository extends MongoRepository<RssTopicEntity, String> {

  /** Fids firs active topic */
  Optional<RssTopicEntity> findFirstByIsActiveAndSubscriptionIdOrderByCreatedAsc(
      boolean isActive, String subscriptionId);

  /** Update RSS topic and set topic inactivity */
  @Query("{'id': ?0}")
  @Update(update = "{ $set: { isActive : false, messageId : ?1}}")
  void updateRssTopicEntity(String id, Integer messageId);

  /** Update RSS topic and set topic inactivity */
  @Query("{'id': ?0}")
  @Update(update = "{ $set: { isActive : false}}")
  void updateRssTopicEntity(String id);

  List<RssTopicEntity> findRssTopicEntitiesByMd5Hash(String md5Hash);

  List<RssTopicEntity> findAllByCreatedBefore(LocalDateTime created);
}
