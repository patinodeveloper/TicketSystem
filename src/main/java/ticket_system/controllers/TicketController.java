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
import ticket_system.entities.dto.TicketStatusHistoryDTO;
import ticket_system.entities.requests.ticket.TicketClientUpdateRequestDTO;
import ticket_system.entities.requests.ticket.TicketPauseRequestDTO;
import ticket_system.entities.requests.ticket.TicketRequestDTO;
import ticket_system.entities.requests.ticket.TicketSupportUpdateRequestDTO;
import ticket_system.entities.responses.ApiResponse;
import ticket_system.services.ITicketService;
import ticket_system.services.ITicketStatusHistoryService;
import ticket_system.services.storage.FileStorageService;
import ticket_system.validators.FileValidator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ticketsystem/api/v1/tickets")
public class TicketController {
    private final ITicketService ticketService;
    private final FileStorageService fileStorageService;
    private final ITicketStatusHistoryService historyService;
    private final FileValidator fileValidator;

    public TicketController(ITicketService ticketService, FileStorageService fileStorageService,
                            ITicketStatusHistoryService historyService, FileValidator fileValidator) {
        this.ticketService = ticketService;
        this.fileStorageService = fileStorageService;
        this.historyService = historyService;
        this.fileValidator = fileValidator;
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
            ResponseEntity<ApiResponse<TicketDTO>> fileValidationError =
                    fileValidator.validateFileForTicket(requestDTO.getClientEvidenceFile());
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
            ResponseEntity<ApiResponse<TicketDTO>> fileValidationError =
                    fileValidator.validateFileForTicket(requestDTO.getClientEvidenceFile());
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
            ResponseEntity<ApiResponse<TicketDTO>> fileValidationError =
                    fileValidator.validateFileForTicket(requestDTO.getSupportEvidenceFile());
            if (fileValidationError != null) {
                return fileValidationError;
            }
        }

        return ResponseEntity.ok(ApiResponse.success(ticketService.resolveTicket(id, requestDTO, user)));
    }

    @PatchMapping("/{id}/init")
    public ResponseEntity<ApiResponse<TicketDTO>> updateStatus(@PathVariable Long id,
                                                               @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success(ticketService.updateTicketStatus(id, user)));
    }

    @PatchMapping("/{id}/pause")
    public ResponseEntity<ApiResponse<TicketDTO>> pauseTicket(
            @PathVariable Long id,
            @Valid @RequestBody TicketPauseRequestDTO pauseRequest,
            @AuthenticationPrincipal User user) {

        TicketDTO ticketDTO = ticketService.pauseTicket(id, pauseRequest, user);
        return ResponseEntity.ok(ApiResponse.success(ticketDTO));
    }

    @PatchMapping("/{id}/resume")
    public ResponseEntity<ApiResponse<TicketDTO>> resumeTicket(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {

        TicketDTO ticketDTO = ticketService.resumeTicket(id, user);
        return ResponseEntity.ok(ApiResponse.success(ticketDTO));
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

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        ticketService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Ticket eliminado exitosamente", null));
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<ApiResponse<List<TicketStatusHistoryDTO>>> getTicketHistory(
            @PathVariable Long id) {

        List<TicketStatusHistoryDTO> history = historyService.getTicketHistory(id)
                .stream()
                .map(TicketStatusHistoryDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(history));
    }
}