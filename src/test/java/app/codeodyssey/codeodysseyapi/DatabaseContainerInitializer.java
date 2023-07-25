package app.codeodyssey.codeodysseyapi;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

public class DatabaseContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15-alpine");

    static {
        postgreSQLContainer.start();
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        TestPropertyValues.of(
                        "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                        "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                        "spring.datasource.password=" + postgreSQLContainer.getPassword())
                .applyTo(applicationContext.getEnvironment());
    }
}
