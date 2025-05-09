package auth_api.entities.dto;

import auth_api.entities.Permission;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class PermissionDTO {
    private final Long id;
    private final String name;
    private final Set<RoleBasicDTO> roles;

    public PermissionDTO(Permission permission) {
        this.id = permission.getId();
        this.name = permission.getName();
        this.roles = permission.getRoles().stream()
                .map(role -> new RoleBasicDTO(role.getId(), role.getName()))
                .collect(Collectors.toSet());
    }

    @Getter
    public static class RoleBasicDTO {
        private final Long id;
        private final String name;

        public RoleBasicDTO(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}