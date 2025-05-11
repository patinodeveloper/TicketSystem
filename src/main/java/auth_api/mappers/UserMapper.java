package auth_api.mappers;

import auth_api.entities.User;
import auth_api.entities.dto.UserDTO;
import auth_api.entities.requests.UserRequestDTO;

import java.util.List;

public interface UserMapper {
    UserDTO toDTO(User user);

    List<UserDTO> toDTOList(List<User> users);

    User toEntity(UserRequestDTO requestDTO);
}
