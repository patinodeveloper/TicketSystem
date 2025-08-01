package ticket_system.entities.dto;

import lombok.Getter;
import ticket_system.entities.Ticket;
import ticket_system.enums.TicketStatus;

import java.time.Duration;
import java.time.LocalDate;

@Getter
public class TicketDTO {
    private final Long id;
    private final String title;
    private final String description;
    private final TicketStatus status;
    private final String resolution;
    private final String clientEvidence;
    private final String supportEvidence;
    private final ProjectDTO project;
    private final SupportTypeDTO supportType;
    private final UserDTO client;
    private final UserDTO support;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Long totalProgressMinutes;
    private final String formattedProgressTime;

    public TicketDTO(Ticket ticket) {
        this.id = ticket.getId();
        this.title = ticket.getTitle();
        this.description = ticket.getDescription();
        this.status = ticket.getStatus();
        this.resolution = ticket.getResolution();
        this.clientEvidence = ticket.getClientEvidence();
        this.supportEvidence = ticket.getSupportEvidence();
        this.project = ticket.getProject() != null ? new ProjectDTO(ticket.getProject()) : null;
        this.supportType = ticket.getSupportType() != null ? new SupportTypeDTO(ticket.getSupportType()) : null;
        this.client = ticket.getClient() != null ? new UserDTO(ticket.getClient()) : null;
        this.support = ticket.getSupport() != null ? new UserDTO(ticket.getSupport()) : null;
        this.startDate = ticket.getStartDate();
        this.endDate = ticket.getEndDate();
        this.totalProgressMinutes = ticket.getCurrentTotalProgressMinutes();
        this.formattedProgressTime = formatDuration(Duration.ofMinutes(this.totalProgressMinutes));

    }

    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        return String.format("%d horas, %d minutos", hours, minutes);
    }
}
