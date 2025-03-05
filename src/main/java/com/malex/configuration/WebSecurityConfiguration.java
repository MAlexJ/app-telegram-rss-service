package com.malex.configuration;

import com.malex.security.JwtTokenFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebSecurityConfiguration {

  /*
   * REST api
   */
  private static final String REST_API_URL_PATTERN = "/v1/*";

  @Bean
  public FilterRegistrationBean<JwtTokenFilter> jwtFilter() {
    FilterRegistrationBean<JwtTokenFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(new JwtTokenFilter());
    registrationBean.addUrlPatterns(REST_API_URL_PATTERN);
    return registrationBean;
  }
}
