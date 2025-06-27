package ticket_system.mappers;

import ticket_system.entities.Project;
import ticket_system.entities.dto.ProjectDTO;
import ticket_system.entities.requests.project.ProjectRequestDTO;

import java.util.List;

public interface ProjectMapper {
    ProjectDTO toDTO(Project project);

    List<ProjectDTO> toDTOList(List<Project> projects);

    Project toEntity(ProjectRequestDTO requestDTO);
}
