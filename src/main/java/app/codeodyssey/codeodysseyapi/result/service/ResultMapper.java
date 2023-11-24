package app.codeodyssey.codeodysseyapi.result.service;

import app.codeodyssey.codeodysseyapi.result.api.ResultResponse;
import app.codeodyssey.codeodysseyapi.result.data.Result;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ResultMapper {

    ResultResponse to(Result results);
}
