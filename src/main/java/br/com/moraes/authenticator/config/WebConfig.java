package br.com.moraes.authenticator.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import br.com.moraes.authenticator.api.enums.DynamicPropertyEnum;
import br.com.moraes.authenticator.api.model.DynamicProperty;
import br.com.moraes.authenticator.api.service.DynamicPropertyService;
import jakarta.annotation.PostConstruct;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Autowired
	private DynamicPropertyService dynamicPropertyService;

	private DynamicProperty corsOriginPatterns;

	@PostConstruct
	public void postConstruct() {
		corsOriginPatterns = dynamicPropertyService.findById(DynamicPropertyEnum.CORS_ORIGIN_PATTERNS);
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		final String[] allowedOrigins = corsOriginPatterns.getValue().split(",");
		registry.addMapping("/**")
//		.allowedMethods("GET", "POST", "PUT")
				.allowedMethods("*").allowedOrigins(allowedOrigins).allowCredentials(true);

	}
}
