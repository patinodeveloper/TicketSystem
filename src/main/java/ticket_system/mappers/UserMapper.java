package ticket_system.mappers;

import ticket_system.entities.User;
import ticket_system.entities.dto.UserDTO;
import ticket_system.entities.requests.UserRequestDTO;

import java.util.List;

public interface UserMapper {
    UserDTO toDTO(User user);

    List<UserDTO> toDTOList(List<User> users);

    User toEntity(UserRequestDTO requestDTO);
}
