package com.malex.repository;

import com.malex.model.entity.RssTopicEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface RssTopicRepository extends MongoRepository<RssTopicEntity, String> {

  /** Fids active topics ordered by date created */
  @Aggregation(
      pipeline = {
        "{ '$match': { 'isActive' : ?0 , 'subscriptionId': ?1} }",
        "{ '$sort' : { 'created' : 1 } }",
        "{ '$limit' : ?2 }"
      })
  List<RssTopicEntity> findActiveTopicBySubscriptionIdOrderByCreatedAsc(
      boolean isActive, String subscriptionId, int limit);

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
