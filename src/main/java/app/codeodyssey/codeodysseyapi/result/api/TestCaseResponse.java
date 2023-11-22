package app.codeodyssey.codeodysseyapi.result.api;

import java.util.UUID;

public record TestCaseResponse(UUID id,
                               String testName,
                               boolean success,
                               String info,
                               Double time) {
}
