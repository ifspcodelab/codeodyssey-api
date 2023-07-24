package app.codeodyssey.codeodysseyapi.user;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse to(User user);
}
