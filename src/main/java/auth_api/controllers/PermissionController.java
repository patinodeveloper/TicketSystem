package auth_api.controllers;

import auth_api.entities.User;
import auth_api.entities.dto.PermissionDTO;
import auth_api.entities.dto.permissions.SlugPermissionDTO;
import auth_api.entities.requests.PermissionRequestDTO;
import auth_api.entities.responses.ApiResponse;
import auth_api.services.IPermissionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/permissions")
public class PermissionController {

    private final IPermissionService permissionService;

    public PermissionController(IPermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<PermissionDTO>>> findAll() {
        return ResponseEntity.ok(ApiResponse.success(permissionService.findAll()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PermissionDTO>> findById(@PathVariable Long id) {
        PermissionDTO permissionDTO = permissionService.findById(id);
        return ResponseEntity.ok(ApiResponse.success(permissionDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<PermissionDTO>> create(@Valid @RequestBody PermissionRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(permissionService.save(request)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PermissionDTO>> update(@PathVariable Long id, @Valid @RequestBody PermissionRequestDTO request) {
        return ResponseEntity.ok(ApiResponse.success(permissionService.update(id, request)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        permissionService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Permiso eliminado exitosamente", null));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/user/me")
    public ResponseEntity<ApiResponse<List<String>>> findByUserId(@AuthenticationPrincipal User user) {
        List<SlugPermissionDTO> permissions = permissionService.findByUserId(user.getId());

        // Se extraen los slugs en una lista
        List<String> slugs = permissions.stream()
                .map(SlugPermissionDTO::getSlug)
                .toList();

        return ResponseEntity.ok(ApiResponse.success(slugs));
    }

}
