package auth_api.auth.controllers.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
        @JsonProperty("refresh_token")
        @NotBlank(message = "Refresh token es requerido")
        String refreshToken
) {}