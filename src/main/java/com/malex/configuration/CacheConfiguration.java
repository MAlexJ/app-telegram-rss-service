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
public class CacheConfiguration {

  public static final String TEMPLATES_CACHE_NAME = "templates";
  public static final String TEMPLATES_CACHE_KEY_ID = "#id";
  public static final String TEMPLATES_CACHE_TEMPLATE_KEY_ID = "#templateId";


  @Bean
  public CacheManager cacheManager() {
    return new ConcurrentMapCacheManager(TEMPLATES_CACHE_NAME);
  }
}
