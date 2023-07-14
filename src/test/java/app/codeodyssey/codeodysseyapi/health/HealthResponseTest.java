package app.codeodyssey.codeodysseyapi.health;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class HealthResponseTest {

    @Test
    public void testHealthResponse() {
        HealthResponse healthResponse = new HealthResponse("UP", "Server is UP");
        assertEquals("UP", healthResponse.status());
        assertEquals("Server is UP", healthResponse.message());
    }
}
