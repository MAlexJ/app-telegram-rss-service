package com.malex.repository;

import com.malex.model.entity.ErrorEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrorRepository extends MongoRepository<ErrorEntity, String> {}
