package ticket_system.services;

import ticket_system.entities.dto.ProjectDTO;
import ticket_system.entities.requests.project.ProjectRequestDTO;

import java.util.List;

public interface IProjectService {
    List<ProjectDTO> findAll();

    ProjectDTO findById(Long id);

    ProjectDTO save(ProjectRequestDTO projectRequestDTO);

    ProjectDTO update(Long id, ProjectRequestDTO projectRequestDTO);

    void delete(Long id);
}
