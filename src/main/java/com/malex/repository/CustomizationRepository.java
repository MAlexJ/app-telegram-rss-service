package com.malex.repository;

import com.malex.model.entity.CustomizationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomizationRepository extends MongoRepository<CustomizationEntity, String> {

  Page<CustomizationEntity> findAll(Pageable pageable);
}
