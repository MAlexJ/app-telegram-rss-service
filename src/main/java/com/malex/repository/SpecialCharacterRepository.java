package com.malex.repository;

import com.malex.model.entity.SpecialCharacterEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialCharacterRepository
    extends MongoRepository<SpecialCharacterEntity, String> {}
