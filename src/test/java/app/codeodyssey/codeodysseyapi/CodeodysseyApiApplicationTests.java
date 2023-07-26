package app.codeodyssey.codeodysseyapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
class CodeodysseyApiApplicationTests {

    @Container
    @ServiceConnection
    public static PostgreSQLContainer<?> container = new PostgreSQLContainer("postgres:15-alpine")
            .withUsername("postgres")
            .withPassword("postgres")
            .withDatabaseName("codeodyssey-api-test");

    @Test
    void contextLoads() {

    }
}
