package com.malex.repository;

import com.malex.model.entity.FilterEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface FilterRepository extends MongoRepository<FilterEntity, String> {

  @Query("{'isActive': ?0 }")
  List<FilterEntity> findAllByActive(boolean active);

  /** Update Filter and set topic inactivity */
  @Query("{'id': ?0}")
  @Update(update = "{ $set: { isActive : false}}")
  Long setInactiveFilterStatusById(String id);
}
