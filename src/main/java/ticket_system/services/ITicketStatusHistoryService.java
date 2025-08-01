package ticket_system.services;

import ticket_system.entities.Ticket;
import ticket_system.entities.TicketStatusHistory;
import ticket_system.entities.User;
import ticket_system.enums.TicketStatus;

import java.util.List;

public interface ITicketStatusHistoryService {

    TicketStatusHistory recordStatusChange(Ticket ticket, TicketStatus previousStatus,
                                           TicketStatus newStatus, User changedBy,
                                           String notes, String pauseReason);

    List<TicketStatusHistory> getTicketHistory(Long ticketId);
}
