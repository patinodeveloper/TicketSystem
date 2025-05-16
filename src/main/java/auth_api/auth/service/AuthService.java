package auth_api.auth.service;

import auth_api.auth.controllers.dtos.AuthResponse;
import auth_api.auth.controllers.dtos.LoginRequest;
import auth_api.auth.controllers.dtos.RegisterRequest;
import auth_api.config.exceptions.NotFoundException;
import auth_api.entities.Role;
import auth_api.entities.User;
import auth_api.repositories.RoleRepository;
import auth_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));
        User user = userRepository.findByUsername(loginRequest.username()).orElseThrow();
        String token = jwtService.getToken(user);

        return new AuthResponse(token);

    }

    public AuthResponse register(RegisterRequest registerRequest) {
        // Crea el usuario con la informacion basica
        User user = User.builder()
                .username(registerRequest.username())
                .password(passwordEncoder.encode(registerRequest.password()))
                .firstName(registerRequest.firstName())
                .lastName(registerRequest.lastName())
                .email(registerRequest.email())
                .build();

        // Asigna rol por defecto
        Role defaultRole = roleRepository.findBySlug("user")
                .orElseThrow(() -> new NotFoundException("Rol por defecto no encontrado"));

        Set<Role> roles = new HashSet<>();
        roles.add(defaultRole);
        user.setRoles(roles);

        // Guarda el usuario con su rol
        userRepository.save(user);

        String token = jwtService.getToken(user);
        return new AuthResponse(token);
    }
}
