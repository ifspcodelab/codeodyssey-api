package app.codeodyssey.codeodysseyapi.activity.service;

import app.codeodyssey.codeodysseyapi.activity.api.ActivityResponse;
import app.codeodyssey.codeodysseyapi.activity.data.Activity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ActivityMapper {

    ActivityResponse to(Activity activity);

    List<ActivityResponse> to(List<Activity> activities);
}
