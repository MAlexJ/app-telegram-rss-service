package com.malex.configuration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Cache - Speed up your App <br>
 * link: <a href="https://dev.to/noelopez/spring-cache-speed-up-your-app-1gf6">Spring Cache</a>
 */
@EnableCaching
@Configuration
public class InMemoryCacheConfiguration {

  public static final String SUBSCRIPTION_CACHE_NAME = "subscriptions";
  public static final String SUBSCRIPTION_CACHE_IDS = "'subscriptions_ids'";
  public static final String SUBSCRIPTION_CACHE_ACTIVE = "#root.methodName";

  public static final String TEMPLATES_CACHE_NAME = "templates";
  public static final String TEMPLATES_CACHE_TEMPLATE_KEY_ID = "#templateId";
  public static final String TEMPLATES_CACHE_KEY_ID = "#id";

  public static final String FILTERS_CACHE_NAME = "filters";

  public static final String SPECIAL_CHARACTER_CACHE_NAME = "special_characters";

  public static final String CUSTOMIZATION_CACHE_NAME = "customization";
  public static final String CUSTOMIZATION_CACHE_KEY_ID = "#customizationId";

  @Bean
  public CacheManager cacheManager() {
    return new ConcurrentMapCacheManager();
  }
}
