package app.codeodyssey.codeodysseyapi.user;

import app.codeodyssey.codeodysseyapi.user.api.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse to(User user);
}
