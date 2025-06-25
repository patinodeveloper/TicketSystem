package ticket_system.entities.dto;

import ticket_system.entities.User;
import lombok.Getter;
import ticket_system.enums.RoleName;

@Getter
public class UserDTO {
    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String username;
    private final String email;
    private final RoleName roles;

    public UserDTO(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.roles = user.getRole();
    }
}