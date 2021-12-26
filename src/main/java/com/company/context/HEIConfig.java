package com.company.context;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = {"com.company.controllers", "com.company.services"})
@EnableJpaRepositories(basePackages = {"com.company.repository.dao"})
@EntityScan(basePackages = {"com.company.domain"})
@Import(HibernateConfig.class)
public class HEIConfig {
}
