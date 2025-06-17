package auth_api.services.impl;

import auth_api.config.exceptions.NotFoundException;
import auth_api.entities.Role;
import auth_api.entities.User;
import auth_api.entities.dto.UserDTO;
import auth_api.entities.requests.UserRequestDTO;
import auth_api.entities.requests.UserUpdateRequestDTO;
import auth_api.mappers.UserMapper;
import auth_api.repositories.RoleRepository;
import auth_api.repositories.UserRepository;
import auth_api.services.IUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserDTO> findAll() {
        List<User> users = userRepository.findAll();
        return userMapper.toDTOList(users);
    }

    @Override
    public UserDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con el id: " + id));

        return userMapper.toDTO(user);
    }

    @Override
    public UserDTO save(UserRequestDTO userRequestDTO) {
        User user = userMapper.toEntity(userRequestDTO);

        if (userRequestDTO.getPassword() != null && !userRequestDTO.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        } else {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }

        if (userRequestDTO.getRoleIds() != null && !userRequestDTO.getRoleIds().isEmpty()) {

            Set<Role> roles = new HashSet<>(roleRepository.findAllById(userRequestDTO.getRoleIds()));

            if (roles.size() != userRequestDTO.getRoleIds().size()) {
                throw new NotFoundException("Algunos roles no existen");
            }

            user.setRoles(roles);
        }
        User updatedUser = userRepository.save(user);
        return userMapper.toDTO(updatedUser);
    }

    @Override
    public UserDTO update(Long id, UserUpdateRequestDTO userRequestDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        user.setFirstName(userRequestDTO.getFirstName());
        user.setLastName(userRequestDTO.getLastName());
        user.setUsername(userRequestDTO.getUsername());
        user.setEmail(userRequestDTO.getEmail());

        if (userRequestDTO.getPassword() != null &&
                !userRequestDTO.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        }

        if (userRequestDTO.getRoleIds() != null && !userRequestDTO.getRoleIds().isEmpty()) {
            Set<Role> roles = new HashSet<>(roleRepository.findAllById(userRequestDTO.getRoleIds()));

            if (roles.size() != userRequestDTO.getRoleIds().size()) {
                throw new NotFoundException("Algunos roles no existen");
            }

            user.setRoles(roles);
        }
        User updatedUser = userRepository.save(user);
        return userMapper.toDTO(updatedUser);
    }

    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("Usuario no encontrado con el ID: " + id);
        }
        userRepository.deleteById(id);
    }
}
