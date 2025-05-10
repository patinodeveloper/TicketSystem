package auth_api.entities.dto;


import auth_api.entities.Role;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class RoleDTO {
    private final Long id;
    private final String name;
    private final String slug;
    private final String description;
    private final Set<UserBasicDTO> users;
    private final Set<PermissionBasicDTO> permissions;

    public RoleDTO(Role role) {
        this.id = role.getId();
        this.name = role.getName();
        this.slug = role.getSlug();
        this.description = role.getDescription();
        this.users = role.getUsers().stream()
                .map(user -> new UserBasicDTO(user.getId(), user.getUsername()))
                .collect(Collectors.toSet());
        this.permissions = role.getPermissions().stream()
                .map(permission ->
                        new PermissionBasicDTO(permission.getId(), permission.getName(), permission.getSlug()))
                .collect(Collectors.toSet());
    }

    @Getter
    public static class UserBasicDTO {
        private final Long id;
        private final String username;

        public UserBasicDTO(Long id, String username) {
            this.id = id;
            this.username = username;
        }
    }

    @Getter
    public static class PermissionBasicDTO {
        private final Long id;
        private final String name;
        private final String slug;

        public PermissionBasicDTO(Long id, String name, String slug) {
            this.id = id;
            this.name = name;
            this.slug = slug;
        }
    }
}