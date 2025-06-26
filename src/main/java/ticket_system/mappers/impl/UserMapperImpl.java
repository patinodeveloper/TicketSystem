package ticket_system.mappers.impl;

import ticket_system.entities.User;
import ticket_system.entities.dto.UserDTO;
import ticket_system.entities.requests.UserRequestDTO;
import ticket_system.mappers.UserMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO toDTO(User user) {
        if (user == null) return null;
        return new UserDTO(user);
    }

    @Override
    public List<UserDTO> toDTOList(List<User> users) {
        return users.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public User toEntity(UserRequestDTO requestDTO) {
        if (requestDTO == null) return null;

        User user = new User();
        user.setFirstName(requestDTO.getFirstName());
        user.setLastName(requestDTO.getLastName());
        user.setSecondLastName(requestDTO.getSecondLastName());
        user.setEmail(requestDTO.getEmail());
        user.setPassword(requestDTO.getPassword());
        user.setRole(requestDTO.getRole());
        return user;
    }
}
