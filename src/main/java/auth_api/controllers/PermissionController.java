package auth_api.controllers;

import auth_api.entities.dto.PermissionDTO;
import auth_api.entities.requests.PermissionRequestDTO;
import auth_api.entities.responses.ApiResponse;
import auth_api.services.IPermissionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/permissions")
public class PermissionController {

    private final IPermissionService permissionService;

    public PermissionController(IPermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PermissionDTO>>> findAll() {
        return ResponseEntity.ok(ApiResponse.success(permissionService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PermissionDTO>> findById(@PathVariable Long id) {
        PermissionDTO permissionDTO = permissionService.findById(id);
        return ResponseEntity.ok(ApiResponse.success(permissionDTO));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PermissionDTO>> create(@Valid @RequestBody PermissionRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(permissionService.save(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PermissionDTO>> update(@PathVariable Long id, @Valid @RequestBody PermissionRequestDTO request) {
        return ResponseEntity.ok(ApiResponse.success(permissionService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        permissionService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Permiso eliminado exitosamente", null));
    }
}
