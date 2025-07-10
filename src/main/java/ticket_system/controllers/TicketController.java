package ticket_system.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ticket_system.entities.User;
import ticket_system.entities.dto.TicketDTO;
import ticket_system.entities.requests.ticket.TicketClientUpdateRequestDTO;
import ticket_system.entities.requests.ticket.TicketRequestDTO;
import ticket_system.entities.requests.ticket.TicketSupportUpdateRequestDTO;
import ticket_system.entities.responses.ApiResponse;
import ticket_system.services.ITicketService;

import java.util.List;

@RestController
@RequestMapping("/ticketsystem/api/v1/tickets")
public class TicketController {
    private final ITicketService ticketService;

    public TicketController(ITicketService ticketService) {
        this.ticketService = ticketService;
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

    @PostMapping
    public ResponseEntity<ApiResponse<TicketDTO>> create(@Valid @RequestBody TicketRequestDTO requestDTO,
                                                         @AuthenticationPrincipal User user) {
        TicketDTO ticketDTO = ticketService.save(requestDTO, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(ticketDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TicketDTO>> update(@PathVariable Long id,
                                                         @Valid @RequestBody TicketClientUpdateRequestDTO requestDTO,
                                                         @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success(ticketService.update(id, requestDTO, user)));
    }

    @PutMapping("/{id}/resolve")
    public ResponseEntity<ApiResponse<TicketDTO>> resolveTicket(@PathVariable Long id,
                                                                @Valid @RequestBody TicketSupportUpdateRequestDTO requestDTO,
                                                                @AuthenticationPrincipal User user) {
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
}
