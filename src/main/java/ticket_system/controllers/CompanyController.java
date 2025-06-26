package ticket_system.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ticket_system.entities.dto.CompanyDTO;
import ticket_system.entities.requests.company.CompanyRequestDTO;
import ticket_system.entities.responses.ApiResponse;
import ticket_system.services.ICompanyService;

import java.util.List;

@RestController
@RequestMapping("/ticketsystem/api/v1/companies")
public class CompanyController {

    private final ICompanyService companyService;

    public CompanyController(ICompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CompanyDTO>>> findAll() {
        return ResponseEntity.ok(ApiResponse.success(companyService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CompanyDTO>> findById(@PathVariable Long id) {
        CompanyDTO companyDTO = companyService.findById(id);
        return ResponseEntity.ok(ApiResponse.success(companyDTO));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CompanyDTO>> create(@Valid @RequestBody CompanyRequestDTO requestDTO) {
        CompanyDTO companyDTO = companyService.save(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(companyDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CompanyDTO>> update(@PathVariable Long id,
                                                          @Valid @RequestBody CompanyRequestDTO requestDTO) {
        return ResponseEntity.ok(ApiResponse.success(companyService.update(id, requestDTO)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        companyService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Empresa eliminada exitosamente", null));
    }
}
