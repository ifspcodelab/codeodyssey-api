package app.codeodyssey.codeodysseyapi.result.service;

import app.codeodyssey.codeodysseyapi.result.api.ResultResponse;
import app.codeodyssey.codeodysseyapi.result.data.Result;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ResultMapper {

    List<ResultResponse> to(List<Result> results);
}
