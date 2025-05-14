package com.malex.service.customisation;

import com.malex.mapper.ObjectMapper;
import com.malex.model.entity.CustomizationEntity;
import com.malex.model.request.CustomizationRequest;
import com.malex.model.response.CustomizationResponse;
import com.malex.repository.CustomizationRepository;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomizationStorageService {

  private final ObjectMapper mapper;

  private final CustomizationRepository repository;

  public Page<CustomizationResponse> findAll(PageRequest pageable) {
    var customizationEntityPage = repository.findAll(pageable);
    return customizationEntityPage.map(mapper::entityToDto);
  }

  public Optional<CustomizationEntity> findById(String customizationId) {
    return repository.findById(customizationId);
  }

  /*
   * In Hibernate, after saving an entity using the save() or persist() methods,
   * its status changes depending on the context of the persistence operation.
   * possible statuses:
   *
   * Transient:
   *     Before saving an entity, if it's not yet associated with any persistence context, it is in a transient state.
   *     The entity is not tracked by Hibernate and doesn't exist in the database.
   *
   * Persistent:
   *     Once you save the entity using save() or persist(), it moves to the persistent state.
   *     This means that the entity is now managed by the persistence context and any changes to it will be tracked
   *     and synchronized with the database when the transaction commits or the session is flushed.
   *     save() method: Returns the generated identifier after the entity is saved.
   *     persist() method: Does not return the identifier, but it ensures the entity becomes persistent.
   *
   * Detached:
   *     Once the session is closed, the entity moves into the detached state.
   *     Hibernate will no longer track changes to this entity, and you must reattach it to a new session
   *     using methods like merge() or update() to persist any further modifications.
   */
  public CustomizationResponse save(CustomizationRequest request) {
    var transientEntity = mapper.dtoToEntity(request);
    var persistentEntity = repository.save(transientEntity);
    // Detached entity
    return mapper.entityToDto(persistentEntity);
  }
}
