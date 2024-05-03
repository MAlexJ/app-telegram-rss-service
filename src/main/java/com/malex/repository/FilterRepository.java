package com.malex.repository;

import com.malex.model.entity.FilterEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilterRepository extends MongoRepository<FilterEntity, String> {}
