package com.malex.service.subscription;

import static com.malex.configuration.InMemoryCacheConfiguration.SUBSCRIPTION_CACHE_ACTIVE;
import static com.malex.configuration.InMemoryCacheConfiguration.SUBSCRIPTION_CACHE_IDS;
import static com.malex.configuration.InMemoryCacheConfiguration.SUBSCRIPTION_CACHE_NAME;

import com.malex.model.entity.SubscriptionEntity;
import com.malex.model.request.SubscriptionRequest;
import com.malex.model.response.SubscriptionResponse;

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
public class SubscriptionCacheService {

  private final SubscriptionStorageService subscriptionStorageService;

  @CacheEvict(allEntries = true)
  public SubscriptionResponse subscribeAndCacheEvict(SubscriptionRequest request) {
    log.info("CacheEvict: subscribe to RSS - '{}', chanel id - '{}'", request.rss(), request.chatId());
    return subscriptionStorageService.subscribe(request);
  }

  @CacheEvict(allEntries = true)
  public void unsubscribeFromSubscriptionAndEvictCache(String subscriptionId) {
    log.info("CacheEvict: unsubscribe form RSS by id - '{}'", subscriptionId);
    subscriptionStorageService.unsubscribe(subscriptionId);
  }

  @Cacheable
  public List<SubscriptionResponse> findSubscriptionsCacheable() {
    log.info("Cacheable: find all subscriptions");
    return subscriptionStorageService.findSubscriptions();
  }

  @Cacheable(key = SUBSCRIPTION_CACHE_ACTIVE)
  public List<SubscriptionEntity> findAllActiveSubscriptionsCacheable() {
    log.info("Cacheable: find all active subscriptions");
    return subscriptionStorageService.findAllActiveSubscriptions();
  }

  /** Find all subscription ids for active user subscriptions */
  @Cacheable(key = SUBSCRIPTION_CACHE_IDS)
  public List<String> findAllActiveSubscriptionIdsCacheable() {
    log.info("Cacheable: find all active subscription ids");
    return subscriptionStorageService.findAllActiveSubscriptionIds();
  }
}
