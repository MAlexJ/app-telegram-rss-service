package com.malex.configuration;

import java.util.concurrent.Executor;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Short explanation:
 *
 * <p>Link to info: <a href="https://habr.com/ru/articles/771112/">Async scheduling jobs</a>
 *
 * <p>Additional configuration:
 *
 * <p>1. mark main class with @EnableAsync <br>
 * 2. mark scheduler job with @Async annotation<br>
 * 3. configure ThreadPoolTaskExecutor
 */
@EnableAsync
@EnableScheduling
@Configuration
public class AsyncSchedulingConfiguration implements AsyncConfigurer {

  @Override
  public Executor getAsyncExecutor() {
    var executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(10);
    executor.setMaxPoolSize(30);
    executor.setThreadNamePrefix("AsyncExecutor-");
    executor.initialize();
    return executor;
  }

  @Override
  public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
    return new SimpleAsyncUncaughtExceptionHandler();
  }
}
