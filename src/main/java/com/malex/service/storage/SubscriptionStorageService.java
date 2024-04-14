package com.malex.service.storage;

import com.malex.mapper.ObjectMapper;
import com.malex.model.entity.SubscriptionEntity;
import com.malex.model.request.RssSubscriptionRequest;
import com.malex.model.response.RssSubscriptionResponse;
import com.malex.repository.RssSubscriptionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionStorageService {

  private final RssSubscriptionRepository subscriptionRepository;
  private final ObjectMapper mapper;

  public RssSubscriptionResponse subscribe(RssSubscriptionRequest request) {
    log.info("Subscribe to RSS - '{}', chanel id - '{}'", request.rss(), request.chatId());
    var entity = mapper.dtoToEntity(request);
    var persistenceEntity = subscriptionRepository.save(entity);
    return mapper.entityToDto(persistenceEntity);
  }

  public List<RssSubscriptionResponse> findSubscriptions() {
    return subscriptionRepository.findAll().stream() //
        .map(mapper::entityToDto)
        .toList();
  }

  // TODO >>  @Cacheable !!!
  /** Find all subscription ids for active user subscriptions */
  public List<String> findAllActiveSubscriptionIds() {
    return subscriptionRepository.findAllByActive(true).stream()
        .map(SubscriptionEntity::getId)
        .toList();
  }

  /** Find all Active subscriptions */
  public List<SubscriptionEntity> findAllActiveSubscriptions() {
    return subscriptionRepository.findAllByActive(true);
  }

  public Integer unsubscribe(String subscriptionId) {
    return subscriptionRepository.updateRssSubscriptionEntity(subscriptionId);
  }
}
