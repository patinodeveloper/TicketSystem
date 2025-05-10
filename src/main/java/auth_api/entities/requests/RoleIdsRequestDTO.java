package auth_api.entities.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleIdsRequestDTO {

    @NotEmpty(message = "La lista de roles no puede estar vacía")
    @Size(min = 1, message = "Debe proporcionar al menos un rol")
    private Set<Long> roleIds;
}
