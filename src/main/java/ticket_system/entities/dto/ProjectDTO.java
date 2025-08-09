package ticket_system.entities.dto;

import lombok.Getter;
import ticket_system.entities.Project;

@Getter
public class ProjectDTO {
    private final Long id;
    private final String name;
    private final String description;
    private final boolean isActive;
    private final CompanyBasicDTO company;

    public ProjectDTO(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.description = project.getDescription();
        this.isActive = project.isActive();
        this.company = project.getCompany() != null ? new CompanyBasicDTO(project.getCompany().getId(), project.getCompany().getName()) : null;
    }

    public record CompanyBasicDTO(Long id, String name) {
    }
}
