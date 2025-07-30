package ticket_system.controllers;

import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ticket_system.entities.User;
import ticket_system.entities.dto.TicketDTO;
import ticket_system.entities.requests.ticket.TicketClientUpdateRequestDTO;
import ticket_system.entities.requests.ticket.TicketRequestDTO;
import ticket_system.entities.requests.ticket.TicketSupportUpdateRequestDTO;
import ticket_system.entities.responses.ApiResponse;
import ticket_system.services.ITicketService;
import ticket_system.services.storage.FileStorageService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/ticketsystem/api/v1/tickets")
public class TicketController {
    private final ITicketService ticketService;
    private final FileStorageService fileStorageService;

    public TicketController(ITicketService ticketService, FileStorageService fileStorageService) {
        this.ticketService = ticketService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TicketDTO>>> findAll() {
        return ResponseEntity.ok(ApiResponse.success(ticketService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TicketDTO>> findById(@PathVariable Long id) {
        TicketDTO ticketDTO = ticketService.findById(id);
        return ResponseEntity.ok(ApiResponse.success(ticketDTO));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<TicketDTO>> create(
            @Valid @ModelAttribute TicketRequestDTO requestDTO,
            @AuthenticationPrincipal User user) {

        // Valida que el archivo si existe
        if (requestDTO.getClientEvidenceFile() != null && !requestDTO.getClientEvidenceFile().isEmpty()) {
            ResponseEntity<ApiResponse<TicketDTO>> fileValidationError = validateFile(requestDTO.getClientEvidenceFile());
            if (fileValidationError != null) {
                return fileValidationError;
            }
        }

        TicketDTO ticketDTO = ticketService.save(requestDTO, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(ticketDTO));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<TicketDTO>> update(
            @PathVariable Long id,
            @Valid @ModelAttribute TicketClientUpdateRequestDTO requestDTO,
            @AuthenticationPrincipal User user) {

        if (requestDTO.getClientEvidenceFile() != null && !requestDTO.getClientEvidenceFile().isEmpty()) {
            ResponseEntity<ApiResponse<TicketDTO>> fileValidationError = validateFile(requestDTO.getClientEvidenceFile());
            if (fileValidationError != null) {
                return fileValidationError;
            }
        }

        return ResponseEntity.ok(ApiResponse.success(ticketService.update(id, requestDTO, user)));
    }

    @PutMapping(value = "/{id}/resolve", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<TicketDTO>> resolveTicket(
            @PathVariable Long id,
            @Valid @ModelAttribute TicketSupportUpdateRequestDTO requestDTO,
            @AuthenticationPrincipal User user) {

        if (requestDTO.getSupportEvidenceFile() != null && !requestDTO.getSupportEvidenceFile().isEmpty()) {
            ResponseEntity<ApiResponse<TicketDTO>> fileValidationError = validateFile(requestDTO.getSupportEvidenceFile());
            if (fileValidationError != null) {
                return fileValidationError;
            }
        }

        return ResponseEntity.ok(ApiResponse.success(ticketService.resolveTicket(id, requestDTO, user)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<TicketDTO>> updateStatus(@PathVariable Long id,
                                                               @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success(ticketService.updateTicketStatus(id, user)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        ticketService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Ticket eliminado exitosamente", null));
    }

    @GetMapping("/evidence/{filename}")
    public ResponseEntity<Resource> viewEvidence(
            @PathVariable String filename,
            @RequestParam(value = "download", defaultValue = "false") boolean forceDownload) {

        try {
            Resource resource = fileStorageService.getEvidenceResource(filename);
            String contentType = fileStorageService.getContentType(filename);

            String disposition = forceDownload ? "attachment" : "inline";

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, disposition + "; filename=\"" + filename + "\"")
                    .header(HttpHeaders.CACHE_CONTROL, "max-age=3600")
                    .body(resource);

        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Metodo para encontrar el archivo en los posibles subdirectorios
     *
     * @param filename Nombre del archivo
     * @return ruta
     */
    private Path findEvidenceFile(String filename) {
        // Ubicaciones donde puede estar el archivo
        String[] possiblePaths = {
                "tickets/client-evidence/" + filename,
                "tickets/support-evidence/" + filename,
        };

        for (String possiblePath : possiblePaths) {
            Path filePath = fileStorageService.getFilePath(possiblePath);

            if (Files.exists(filePath)) {
                return filePath;
            }
        }

        return null;
    }

    private ResponseEntity<ApiResponse<TicketDTO>> validateFile(MultipartFile file) {
        if (!fileStorageService.isValidFileType(file)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Tipo de archivo no permitido"));
        }
        if (!fileStorageService.isValidFileSize(file)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("El archivo excede el tamaño máximo permitido (10MB)"));
        }
        return null;
    }
}