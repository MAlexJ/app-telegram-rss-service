package com.malex.service.storage;

import com.malex.exception.subscription.StatusCode;
import com.malex.exception.subscription.SubscriptionException;
import com.malex.mapper.ObjectMapper;
import com.malex.model.entity.SubscriptionEntity;
import com.malex.model.request.SubscriptionRequest;
import com.malex.model.response.SubscriptionResponse;
import com.malex.repository.RssSubscriptionRepository;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionStorageService {

  private final ObjectMapper mapper;

  private final RssSubscriptionRepository subscriptionRepository;

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
  public SubscriptionResponse subscribe(SubscriptionRequest request) {
    var transientEntity = mapper.dtoToEntity(request);
    var persistentEntity = subscriptionRepository.save(transientEntity);
    return mapper.entityToDto(persistentEntity);
  }

  /**
   * Unsubscribed from an active subscription
   *
   * @param subscriptionId - subscription identifier
   */
  @Transactional
  public void unsubscribe(String subscriptionId) {
    var numberOfTopicsUpdated = subscriptionRepository.updateRssSubscriptionEntity(subscriptionId);

    if (Objects.isNull(numberOfTopicsUpdated) || numberOfTopicsUpdated <= 0) {
      var errorMessage = "No record was found by id - " + subscriptionId;
      throw new SubscriptionException(StatusCode.NOT_FOUND, errorMessage);
    }

    if (numberOfTopicsUpdated > 1) {
      var errorMessage = "Multiple records found by id - " + subscriptionId;
      throw new SubscriptionException(StatusCode.MULTIPLE_SUBSCRIPTIONS, errorMessage);
    }
  }

  public List<SubscriptionResponse> findSubscriptions() {
    return subscriptionRepository.findAll().stream().map(mapper::entityToDto).toList();
  }

  /** Find all Active subscriptions */
  public List<SubscriptionEntity> findAllActiveSubscriptions() {
    return subscriptionRepository.findAllByActive(true);
  }

  /** Find all subscription ids for active user subscriptions */
  public List<String> findAllActiveSubscriptionIds() {
    var allActiveSubscriptions = subscriptionRepository.findAllByActive(true);
    return allActiveSubscriptions.stream().map(SubscriptionEntity::getId).toList();
  }
}
