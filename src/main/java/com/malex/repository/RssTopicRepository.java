package com.malex.repository;

import com.malex.model.entity.RssTopicEntity;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface RssTopicRepository extends MongoRepository<RssTopicEntity, String> {

  /** Fids firs active topic */
  Optional<RssTopicEntity> findFirstByIsActiveOrderByCreatedAsc(boolean isActive);

  /** Update RSS topic and set topic inactivity */
  @Query("{'id': ?0 }")
  @Update(update = "{ $set: { isActive : false }}")
  void updateRssTopicEntity(String id);

  Optional<RssTopicEntity> findRssTopicEntitiesByMd5Hash(String md5Hash);
}
