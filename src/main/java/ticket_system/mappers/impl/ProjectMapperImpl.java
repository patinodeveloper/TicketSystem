package ticket_system.mappers.impl;

import org.springframework.stereotype.Component;
import ticket_system.entities.Company;
import ticket_system.entities.Project;
import ticket_system.entities.dto.ProjectDTO;
import ticket_system.entities.requests.projects.ProjectRequestDTO;
import ticket_system.mappers.ProjectMapper;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProjectMapperImpl implements ProjectMapper {
    @Override
    public ProjectDTO toDTO(Project project) {
        if (project == null) return null;

        return new ProjectDTO(project);
    }

    @Override
    public List<ProjectDTO> toDTOList(List<Project> projects) {
        return projects.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public Project toEntity(ProjectRequestDTO requestDTO) {
        if (requestDTO == null) return null;

        Project project = new Project();

        project.setName(requestDTO.getName());
        project.setDescription(requestDTO.getDescription());

        return project;
    }
}
