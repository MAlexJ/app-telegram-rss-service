package com.malex.repository;

import com.malex.model.entity.CustomizationEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomizationRepository extends MongoRepository<CustomizationEntity, String> {}
