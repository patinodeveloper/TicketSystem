package auth_api.services;

import auth_api.entities.dto.UserDTO;
import auth_api.entities.requests.UserRequestDTO;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<UserDTO> findAll();

    Optional<UserDTO> findById(Long id);

    UserDTO save(UserRequestDTO userRequestDTO);

    UserDTO update(Long id, UserRequestDTO userRequestDTO);

    void delete(Long id);
}
