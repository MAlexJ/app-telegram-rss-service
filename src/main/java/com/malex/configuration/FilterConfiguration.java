package com.malex.configuration;

import com.malex.security.JwtTokenFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfiguration {

  @Bean
  public FilterRegistrationBean<JwtTokenFilter> jwtFilter(JwtTokenFilter jwtTokenFilter) {
    FilterRegistrationBean<JwtTokenFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(jwtTokenFilter);
    registrationBean.addUrlPatterns("/v1/*");
    return registrationBean;
  }
}
