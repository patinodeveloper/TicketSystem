package ticket_system.entities.dto;

import ticket_system.entities.User;
import lombok.Getter;
import ticket_system.enums.RoleName;

@Getter
public class UserDTO {
    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String secondLastName;
    private final String email;
    private final RoleName role;
    private final boolean isActive;

    public UserDTO(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.secondLastName = user.getSecondLastName();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.isActive = user.isActive();
    }
}