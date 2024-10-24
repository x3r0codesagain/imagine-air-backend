package com.project.imagineair.config;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class AppConfig {
  @Bean
  public Mapper mapper() {
    return new DozerBeanMapper();
  }

  @Bean
  public ValidatingMongoEventListener validatingMongoEventListener(
      final LocalValidatorFactoryBean factory) {
    return new ValidatingMongoEventListener(factory);
  }
}
