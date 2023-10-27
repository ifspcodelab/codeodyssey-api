package app.codeodyssey.codeodysseyapi.resolution.service;

import app.codeodyssey.codeodysseyapi.resolution.api.ResolutionResponse;
import app.codeodyssey.codeodysseyapi.resolution.data.Resolution;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ResolutionMapper {
    ResolutionResponse to(Resolution resolution);
}
