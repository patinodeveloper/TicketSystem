package auth_api.controllers;

import auth_api.entities.dto.ModuleDTO;
import auth_api.entities.requests.ModuleRequestDTO;
import auth_api.entities.responses.ApiResponse;
import auth_api.services.IModuleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/modules")
public class ModuleController {

    private final IModuleService moduleService;

    public ModuleController(IModuleService moduleService) {
        this.moduleService = moduleService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ModuleDTO>>> getAllModules() {
        List<ModuleDTO> moduleDTOS = moduleService.findAll();

        return ResponseEntity.ok(
                ApiResponse.success(moduleDTOS)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ModuleDTO>> getModuleById(@PathVariable Long id) {
        Optional<ModuleDTO> moduleDTO = moduleService.findById(id);
        return moduleDTO
                .map(dto -> ResponseEntity.ok(ApiResponse.success(dto)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ModuleDTO>> createModule(@Valid @RequestBody ModuleRequestDTO moduleRequestDTO) {
        ModuleDTO created = moduleService.save(moduleRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ModuleDTO>> updateModule(
            @PathVariable Long id,
            @Valid @RequestBody ModuleRequestDTO moduleRequestDTO) {
        ModuleDTO updated = moduleService.update(id, moduleRequestDTO);
        return ResponseEntity.ok(ApiResponse.success(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteModule(@PathVariable Long id) {
        moduleService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Module deleted successfully"));
    }
}
