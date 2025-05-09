package auth_api.entities.dto;

import auth_api.entities.User;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class UserDTO {
    private final Long id;
    private final String username;
    private final String email;
    private final Set<RoleBasicDTO> roles;

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.roles = user.getRoles().stream()
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