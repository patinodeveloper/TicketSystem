package ticket_system.services.impl;

import ticket_system.config.exceptions.NotFoundException;
import ticket_system.entities.Company;
import ticket_system.entities.User;
import ticket_system.entities.dto.UserDTO;
import ticket_system.entities.requests.UserRequestDTO;
import ticket_system.entities.requests.UserUpdateRequestDTO;
import ticket_system.mappers.UserMapper;
import ticket_system.repositories.CompanyRepository;
import ticket_system.repositories.UserRepository;
import ticket_system.services.IUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, CompanyRepository companyRepository,
                           UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
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
    public UserDTO save(Long companyId, UserRequestDTO userRequestDTO) {
        User user = userMapper.toEntity(userRequestDTO);

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Compañía no encontrada para el id: " + companyId));
        user.setCompany(company);

        if (userRequestDTO.getPassword() != null && !userRequestDTO.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        } else {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }

        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    @Override
    public UserDTO update(Long id, UserUpdateRequestDTO userRequestDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        user.setFirstName(userRequestDTO.getFirstName());
        user.setLastName(userRequestDTO.getLastName());
        user.setSecondLastName(userRequestDTO.getSecondLastName());
        user.setEmail(userRequestDTO.getEmail());
        user.setRole(userRequestDTO.getRole());

        if (userRequestDTO.getPassword() != null &&
                !userRequestDTO.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
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
