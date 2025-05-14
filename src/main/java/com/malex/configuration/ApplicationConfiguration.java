package com.malex.configuration;

import java.util.concurrent.Executors;
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;

@Configuration
public class ApplicationConfiguration {

  @Bean
  public TomcatProtocolHandlerCustomizer<?> protocolHandlerVirtualThreadExecutorCustomizer() {
    return protocolHandler ->
        protocolHandler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
  }

  @Bean
  public ApplicationEventMulticaster applicationEventMulticaster() {
    var eventMulticaster = new SimpleApplicationEventMulticaster();
    eventMulticaster.setTaskExecutor(Executors.newVirtualThreadPerTaskExecutor());
    return eventMulticaster;
  }
}
