package com.malex.configuration;

import jakarta.annotation.PostConstruct;
import java.util.TimeZone;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimeZoneConfiguration {

  public static final String UTC_TIME_ZONE = "UTC";

  @PostConstruct
  public void init() {
    TimeZone.setDefault(TimeZone.getTimeZone(UTC_TIME_ZONE));
  }
}
