package com.malex.service.storage;

import static com.malex.configuration.CacheConfiguration.*;

import com.malex.mapper.ObjectMapper;
import com.malex.model.entity.SubscriptionEntity;
import com.malex.model.request.RssSubscriptionRequest;
import com.malex.model.response.RssSubscriptionResponse;
import com.malex.repository.RssSubscriptionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@CacheConfig(cacheNames = SUBSCRIPTION_CACHE_NAME)
@RequiredArgsConstructor
public class SubscriptionStorageService {

  private final RssSubscriptionRepository subscriptionRepository;
  private final ObjectMapper mapper;

  @CacheEvict(allEntries = true)
  public RssSubscriptionResponse subscribe(RssSubscriptionRequest request) {
    log.info("CacheEvict: subscribe - '{}', chanel id - '{}'", request.rss(), request.chatId());
    var entity = mapper.dtoToEntity(request);
    var persistenceEntity = subscriptionRepository.save(entity);
    return mapper.entityToDto(persistenceEntity);
  }

  @CacheEvict(allEntries = true)
  public Integer unsubscribe(String subscriptionId) {
    log.info("CacheEvict: unsubscribe form RSS by id - '{}'", subscriptionId);
    return subscriptionRepository.updateRssSubscriptionEntity(subscriptionId);
  }

  @Cacheable
  public List<RssSubscriptionResponse> findSubscriptions() {
    log.info("Cacheable: find all subscriptions response");
    return subscriptionRepository.findAll().stream() //
        .map(mapper::entityToDto)
        .toList();
  }

  /** Find all Active subscriptions */
  @Cacheable(key = SUBSCRIPTION_CACHE_ACTIVE)
  public List<SubscriptionEntity> findAllActiveSubscriptions() {
    log.info("Cacheable: find all active subscriptions");
    return subscriptionRepository.findAllByActive(true);
  }

  /** Find all subscription ids for active user subscriptions */
  @Cacheable(key = SUBSCRIPTION_CACHE_IDS)
  public List<String> findAllActiveSubscriptionIds() {
    log.info("Cacheable: find all active subscription ids");
    return subscriptionRepository.findAllByActive(true).stream()
        .map(SubscriptionEntity::getId)
        .toList();
  }
}
