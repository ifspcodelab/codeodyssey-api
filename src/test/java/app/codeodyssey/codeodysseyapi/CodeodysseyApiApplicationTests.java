package app.codeodyssey.codeodysseyapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
class CodeodysseyApiApplicationTests {

    @Container
    public static PostgreSQLContainer container = new PostgreSQLContainer("postgres:15.3-alpine3.18")
            .withUsername("postgres")
            .withPassword("postgres")
            .withDatabaseName("codeodyssey-api-test");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

    @Test
    void contextLoads() {

    }
}
