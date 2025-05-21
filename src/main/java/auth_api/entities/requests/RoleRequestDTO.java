package auth_api.entities.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleRequestDTO {
    @NotBlank(message = "El nombre del rol es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre del rol debe tener entre 3 y 50 caracteres")
    private String name;

    @NotBlank(message = "El slug del rol es obligatorio")
    @Size(min = 3, max = 50, message = "El slug del rol debe tener entre 3 y 50 caracteres")
    private String slug;

    private String description;

    @NotEmpty(message = "Debe proporcionar al menos un permiso")
    private Set<Long> permissionIds = new HashSet<>();
}