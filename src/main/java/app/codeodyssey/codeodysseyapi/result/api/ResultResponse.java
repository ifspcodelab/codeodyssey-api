package app.codeodyssey.codeodysseyapi.result.api;

import java.util.List;
import java.util.UUID;

public record ResultResponse(UUID id,
                             String name,
                             Double time,
                             String error,
                             List<TestCaseResponse> testCases) {
}
