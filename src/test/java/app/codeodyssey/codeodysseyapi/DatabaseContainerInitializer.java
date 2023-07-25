package app.codeodyssey.codeodysseyapi;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

public class DatabaseContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static final PostgreSQLContainer<?> CONTAINER = new PostgreSQLContainer<>("postgres:15-alpine");

    static {
        CONTAINER.start();
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        TestPropertyValues.of(
                        "spring.datasource.url=" + CONTAINER.getJdbcUrl(),
                        "spring.datasource.username=" + CONTAINER.getUsername(),
                        "spring.datasource.password=" + CONTAINER.getPassword())
                .applyTo(applicationContext.getEnvironment());
    }
}
