package auth_api.auth.controllers.dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank String username,
        @NotBlank String password
) {
}
