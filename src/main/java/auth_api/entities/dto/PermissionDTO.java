package auth_api.entities.dto;

import auth_api.entities.Permission;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class PermissionDTO {
    private final Long id;
    private final String name;
    private final String slug;
    private final Set<RoleBasicDTO> roles;

    public PermissionDTO(Permission permission) {
        this.id = permission.getId();
        this.name = permission.getName();
        this.slug = permission.getSlug();
        this.roles = permission.getRoles().stream()
                .map(role -> new RoleBasicDTO(role.getId(), role.getName(), role.getSlug()))
                .collect(Collectors.toSet());
    }

    @Getter
    public static class RoleBasicDTO {
        private final Long id;
        private final String name;
        private final String slug;

        public RoleBasicDTO(Long id, String name, String slug) {
            this.id = id;
            this.name = name;
            this.slug = slug;
        }
    }
}