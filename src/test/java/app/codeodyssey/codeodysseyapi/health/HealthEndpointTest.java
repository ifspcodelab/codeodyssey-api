package app.codeodyssey.codeodysseyapi.health;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class HealthEndpointTest {

    @Test
    public void testHealthEndpoint() {
        HealthEndpoint healthController = new HealthEndpoint();
        ResponseEntity<HealthResponse> responseEntity = healthController.health();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("UP", responseEntity.getBody().status());
        assertEquals("Server is UP", responseEntity.getBody().message());
    }
}
