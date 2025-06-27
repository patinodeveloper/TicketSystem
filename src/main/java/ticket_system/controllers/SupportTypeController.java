package ticket_system.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticket_system.entities.dto.SupportTypeDTO;
import ticket_system.entities.requests.support_type.SupportTypeRequestDTO;
import ticket_system.entities.responses.ApiResponse;
import ticket_system.services.ISupportTypeService;

import java.util.List;

@RestController
@RequestMapping("/ticketsystem/api/v1/support-types")
public class SupportTypeController {
    private final ISupportTypeService supportTypeService;

    public SupportTypeController(ISupportTypeService supportTypeService) {
        this.supportTypeService = supportTypeService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SupportTypeDTO>>> findAll() {
        return ResponseEntity.ok(ApiResponse.success(supportTypeService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SupportTypeDTO>> findById(@PathVariable Long id) {
        SupportTypeDTO supportTypeDTO = supportTypeService.findById(id);
        return ResponseEntity.ok(ApiResponse.success(supportTypeDTO));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SupportTypeDTO>> create(@Valid @RequestBody SupportTypeRequestDTO requestDTO) {
        SupportTypeDTO SupportTypeDTO = supportTypeService.save(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(SupportTypeDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SupportTypeDTO>> update(@PathVariable Long id,
                                                              @Valid @RequestBody SupportTypeRequestDTO requestDTO) {
        return ResponseEntity.ok(ApiResponse.success(supportTypeService.update(id, requestDTO)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        supportTypeService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Tipo de Soporte eliminado exitosamente", null));
    }
}
