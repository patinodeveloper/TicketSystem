package ticket_system.auth.service;

import ticket_system.auth.controllers.dtos.AuthResponse;
import ticket_system.auth.controllers.dtos.LoginRequest;
import ticket_system.auth.controllers.dtos.RegisterRequest;
import ticket_system.auth.controllers.dtos.RefreshTokenRequest;
import ticket_system.entities.User;
import ticket_system.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        String accessToken = jwtService.getAccessToken(user);
        String refreshToken = jwtService.getRefreshToken(user);

        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse register(RegisterRequest request) {
        User user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();

        userRepository.save(user);

        String accessToken = jwtService.getAccessToken(user);
        String refreshToken = jwtService.getRefreshToken(user);

        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.refreshToken();

        // Verifica que el token sea válido y sea un refresh token
        if (!jwtService.isRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("Refresh token invalido");
        }

        String username = jwtService.getUserNameFromToken(refreshToken);
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // Verifica que el refresh token sea válido para este usuario
        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new IllegalArgumentException("Refresh token invalido o expirado");
        }

        // Genera los nuevos tokens
        String newAccessToken = jwtService.getAccessToken(user);
        String newRefreshToken = jwtService.getRefreshToken(user);

        return new AuthResponse(newAccessToken, newRefreshToken);
    }
}