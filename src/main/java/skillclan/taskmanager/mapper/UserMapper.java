package skillclan.taskmanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import skillclan.taskmanager.dto.UserDto;
import skillclan.taskmanager.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto userToUserDto(User user);

    @Mapping(target = "id", ignore = true)
    User userDtoToUser(UserDto userDto);
}
