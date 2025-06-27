package ticket_system.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticket_system.entities.dto.ProjectDTO;
import ticket_system.entities.requests.project.ProjectRequestDTO;
import ticket_system.entities.responses.ApiResponse;
import ticket_system.services.IProjectService;

import java.util.List;

@RestController
@RequestMapping("/ticketsystem/api/v1/projects")
public class ProjectController {
    private final IProjectService projectService;

    public ProjectController(IProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProjectDTO>>> findAll() {
        return ResponseEntity.ok(ApiResponse.success(projectService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectDTO>> findById(@PathVariable Long id) {
        ProjectDTO projectDTO = projectService.findById(id);
        return ResponseEntity.ok(ApiResponse.success(projectDTO));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProjectDTO>> create(@Valid @RequestBody ProjectRequestDTO requestDTO) {
        ProjectDTO projectDTO = projectService.save(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(projectDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectDTO>> update(@PathVariable Long id,
                                                          @Valid @RequestBody ProjectRequestDTO requestDTO) {
        return ResponseEntity.ok(ApiResponse.success(projectService.update(id, requestDTO)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        projectService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Proyecto eliminado exitosamente", null));
    }
}
