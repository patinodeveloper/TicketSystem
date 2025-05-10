package auth_api.entities.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PermissionRequestDTO {
    @NotBlank(message = "El nombre del permiso es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre del permiso debe tener entre 3 y 50 caracteres")
    private String name;

    @NotBlank(message = "El slug del permiso es obligatorio")
    @Size(min = 3, max = 50, message = "El slug del permiso debe tener entre 3 y 50 caracteres")
    private String slug;

    private String description;

    @NotNull(message = "Debe proporcionar el ID del módulo")
    private Long moduleId;
}