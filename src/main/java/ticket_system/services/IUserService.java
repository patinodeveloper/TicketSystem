package ticket_system.services;

import ticket_system.entities.dto.UserDTO;
import ticket_system.entities.requests.UserRequestDTO;
import ticket_system.entities.requests.UserUpdateRequestDTO;

import java.util.List;

public interface IUserService {
    List<UserDTO> findAll();

    UserDTO findById(Long id);

    UserDTO save(UserRequestDTO userRequestDTO);

    UserDTO update(Long id, UserUpdateRequestDTO userRequestDTO);

    void delete(Long id);
}
