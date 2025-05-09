package auth_api.entities.dto;


import auth_api.entities.Role;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class RoleDTO {
    private final Long id;
    private final String name;
    private final Set<UserBasicDTO> users;
    private final Set<PermissionBasicDTO> permissions;

    public RoleDTO(Role role) {
        this.id = role.getId();
        this.name = role.getName();
        this.users = role.getUsers().stream()
                .map(user -> new UserBasicDTO(user.getId(), user.getUsername()))
                .collect(Collectors.toSet());
        this.permissions = role.getPermissions().stream()
                .map(permission -> new PermissionBasicDTO(permission.getId(), permission.getName()))
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

        public PermissionBasicDTO(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}