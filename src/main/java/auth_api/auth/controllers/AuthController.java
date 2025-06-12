package auth_api.auth.controllers;

import auth_api.auth.controllers.dtos.AuthResponse;
import auth_api.auth.controllers.dtos.LoginRequest;
import auth_api.auth.controllers.dtos.RegisterRequest;
import auth_api.auth.controllers.dtos.RefreshTokenRequest;
import auth_api.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        AuthResponse response = authService.register(registerRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshRequest) {
        AuthResponse response = authService.refreshToken(refreshRequest);
        return ResponseEntity.ok(response);
    }
}