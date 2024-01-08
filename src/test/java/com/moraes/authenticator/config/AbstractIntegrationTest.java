package com.moraes.authenticator.config;

import java.util.Map;
import java.util.stream.Stream;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public abstract class AbstractIntegrationTest {

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        static PostgreSQLContainer<?> postgresql = new PostgreSQLContainer<>("postgres");

        private static void startContainers() throws Exception {
            Startables.deepStart(Stream.of(postgresql)).join();
        }

        private static Map<String, String> createConnectionConfiguration() {
            return Map.of(
                    "spring.datasource.url", postgresql.getJdbcUrl(),
                    "spring.datasource.username", postgresql.getUsername(),
                    "spring.datasource.password", postgresql.getPassword());
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            try {
                startContainers();
                ConfigurableEnvironment environment = applicationContext.getEnvironment();
                MapPropertySource testcontainers = new MapPropertySource("testcontainers",
                        (Map) createConnectionConfiguration());
                environment.getPropertySources().addFirst(testcontainers);
            } catch (Exception e) {
                // Bom seria usar lombok
                e.printStackTrace();
            }
        }
    }
}