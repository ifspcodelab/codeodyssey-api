package app.codeodyssey.codeodysseyapi.result.service;

import app.codeodyssey.codeodysseyapi.result.api.TestCaseResponse;
import app.codeodyssey.codeodysseyapi.result.data.TestCase;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TestCaseMapper {

    List<TestCaseResponse> to(List<TestCase> testCases);
}
