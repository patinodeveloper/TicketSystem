package auth_api.entities.dto;

import auth_api.entities.Module;
import lombok.Getter;

@Getter
public class ModuleDTO {
    private final Long id;
    private final String name;
    private final String description;

    public ModuleDTO(Module module) {
        this.id = module.getId();
        this.name = module.getName();
        this.description = module.getDescription();
    }
}
