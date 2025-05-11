package auth_api.mappers.impl;

import auth_api.entities.User;
import auth_api.entities.dto.UserDTO;
import auth_api.entities.requests.UserRequestDTO;
import auth_api.mappers.UserMapper;
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
        user.setUsername(requestDTO.getUsername());
        user.setEmail(requestDTO.getEmail());
        user.setPassword(requestDTO.getPassword());
        return user;
    }
}
