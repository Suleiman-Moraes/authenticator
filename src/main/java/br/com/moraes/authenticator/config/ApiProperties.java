package br.com.moraes.authenticator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("api.prop")
public class ApiProperties {

}
