package com.moraes.authenticator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    public static final String MEDIA_TYPE_APPLICATION_YML_VALUE = "application/x-yaml";
    private static final MediaType MEDIA_TYPE_APPLICATION_YML = MediaType.valueOf(MEDIA_TYPE_APPLICATION_YML_VALUE);

    @Value("${cors.originPatterns:default}")
    private String corsOriginPatterns = "";

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorParameter(false).ignoreAcceptHeader(false)
                .useRegisteredExtensionsOnly(false).defaultContentType(MediaType.APPLICATION_JSON)
                .mediaType("json", MediaType.APPLICATION_JSON)
                .mediaType("xml", MediaType.APPLICATION_XML)
                .mediaType("x-yaml", MEDIA_TYPE_APPLICATION_YML);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // .allowedMethods("GET", "POST")
                .allowedMethods("POST")
                .allowedOrigins(corsOriginPatterns.split(","))
                .allowedHeaders("*")
                .allowCredentials(Boolean.TRUE);
    }
}
