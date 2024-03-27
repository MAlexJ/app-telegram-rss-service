package com.malex.configuration;

import com.malex.security.JwtTokenFilter;
import java.nio.charset.StandardCharsets;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class FilterConfiguration {

  @Bean
  public RestTemplate restTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate
        .getMessageConverters()
        .addFirst(new StringHttpMessageConverter(StandardCharsets.UTF_8));
    return restTemplate;
  }

  @Bean
  public FilterRegistrationBean<JwtTokenFilter> jwtFilter(JwtTokenFilter jwtTokenFilter) {
    FilterRegistrationBean<JwtTokenFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(jwtTokenFilter);
    registrationBean.addUrlPatterns("/v1/*");
    return registrationBean;
  }
}
