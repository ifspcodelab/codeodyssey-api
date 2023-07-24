package app.codeodyssey.codeodysseyapi.user;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    List<UserResponse> to(List<User> user);
}
