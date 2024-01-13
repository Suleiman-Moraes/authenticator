package com.moraes.authenticator.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.moraes.authenticator.api.repository.ExtendedRepository;

@Configuration
@EnableJpaRepositories(basePackages = "com.moraes.authenticator.api.repository", repositoryBaseClass = ExtendedRepository.class)
public class ApiConfig {

}
