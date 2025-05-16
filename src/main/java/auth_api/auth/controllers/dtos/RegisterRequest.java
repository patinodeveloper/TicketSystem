package auth_api.auth.controllers.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String username,
        @Email @NotBlank String email,
        @NotBlank String password
) {
}
