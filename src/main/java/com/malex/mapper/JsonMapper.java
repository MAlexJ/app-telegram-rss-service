package com.malex.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JsonMapper {

  private final ObjectMapper mapper;

  /*
   * Issue with java 8 Datetime
   * link: https://stackoverflow.com/questions/74188846/how-to-fix-jackson-java-8-data-time-error
   */
  public <T> String writeValueAsString(T obj) {
    try {
      // todo: Working with Date Parameters in Spring
      //  link: https://www.baeldung.com/spring-date-parameters
      return mapper.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      log.error(e.getMessage());
      return obj.toString();
    }
  }
}
