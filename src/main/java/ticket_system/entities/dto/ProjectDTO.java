package ticket_system.entities.dto;

import lombok.Getter;
import ticket_system.entities.Project;

@Getter
public class ProjectDTO {
    private final Long id;
    private final String name;
    private final String description;
    private final boolean isActive;
    private final CompanyDTO company;

    public ProjectDTO(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.description = project.getDescription();
        this.isActive = project.isActive();
        this.company = project.getCompany() != null ? new CompanyDTO(project.getCompany()) : null;
    }
}
