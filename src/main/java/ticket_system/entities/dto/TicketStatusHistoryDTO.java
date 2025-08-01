package ticket_system.entities.dto;

import lombok.Getter;
import ticket_system.entities.TicketStatusHistory;
import ticket_system.enums.TicketStatus;

import java.time.LocalDateTime;

@Getter
public class TicketStatusHistoryDTO {
    private final Long id;
    private final TicketStatus previousStatus;
    private final TicketStatus newStatus;
    private final UserDTO changedBy;
    private final String notes;
    private final String pauseReason;
    private final LocalDateTime changedAt;

    public TicketStatusHistoryDTO(TicketStatusHistory history) {
        this.id = history.getId();
        this.previousStatus = history.getPreviousStatus();
        this.newStatus = history.getNewStatus();
        this.changedBy = new UserDTO(history.getChangedBy());
        this.notes = history.getNotes();
        this.pauseReason = history.getPauseReason();
        this.changedAt = history.getChangedAt();
    }
}
