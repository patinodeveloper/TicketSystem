package ticket_system.services.impl;

import org.springframework.stereotype.Service;
import ticket_system.config.exceptions.NotFoundException;
import ticket_system.entities.Company;
import ticket_system.entities.Project;
import ticket_system.entities.dto.ProjectDTO;
import ticket_system.entities.requests.projects.ProjectRequestDTO;
import ticket_system.mappers.ProjectMapper;
import ticket_system.repositories.CompanyRepository;
import ticket_system.repositories.ProjectRepository;
import ticket_system.services.IProjectService;

import java.util.List;

@Service
public class ProjectServiceImpl implements IProjectService {

    private final ProjectRepository projectRepository;
    private final CompanyRepository companyRepository;
    private final ProjectMapper projectMapper;

    public ProjectServiceImpl(ProjectRepository projectRepository, CompanyRepository companyRepository,
                              ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.companyRepository = companyRepository;
        this.projectMapper = projectMapper;
    }

    @Override
    public List<ProjectDTO> findAll() {
        List<Project> projects = projectRepository.findAll();
        return projectMapper.toDTOList(projects);
    }

    @Override
    public ProjectDTO findById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Proyecto no encontrado con el id: " + id));

        return projectMapper.toDTO(project);
    }

    @Override
    public ProjectDTO save(ProjectRequestDTO projectRequestDTO) {
        Company company = companyRepository.findById(projectRequestDTO.getCompanyId())
                .orElseThrow(() -> new NotFoundException("Compañía no encontrada con el id: " + projectRequestDTO.getCompanyId()));

        Project project = projectMapper.toEntity(projectRequestDTO);
        project.setCompany(company);

        Project savedProject = projectRepository.save(project);
        return projectMapper.toDTO(savedProject);
    }

    @Override
    public ProjectDTO update(Long id, ProjectRequestDTO projectRequestDTO) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Proyecto no encontrado con el id: " + id));

        Company company = companyRepository.findById(projectRequestDTO.getCompanyId())
                .orElseThrow(() -> new NotFoundException("Compañía no encontrada con el id: " + projectRequestDTO.getCompanyId()));

        project.setName(projectRequestDTO.getName());
        project.setDescription(projectRequestDTO.getDescription());
        project.setCompany(company);

        Project updatedProject = projectRepository.save(project);
        return new ProjectDTO(updatedProject);
    }

    @Override
    public void delete(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new NotFoundException("Proyecto no encontrado con el ID: " + id);
        }
        projectRepository.deleteById(id);
    }
}
