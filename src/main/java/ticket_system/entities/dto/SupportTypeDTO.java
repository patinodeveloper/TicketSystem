package ticket_system.entities.dto;

import lombok.Getter;
import ticket_system.entities.SupportType;

@Getter
public class SupportTypeDTO {
    private final Long id;
    private final String name;
    private final String description;
    private final boolean isActive;

    public SupportTypeDTO(SupportType supportType) {
        this.id = supportType.getId();
        this.name = supportType.getName();
        this.description = supportType.getDescription();
        this.isActive = supportType.isActive();
    }
}
