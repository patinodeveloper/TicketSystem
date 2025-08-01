package ticket_system.services.impl;

import org.springframework.stereotype.Service;
import ticket_system.entities.Ticket;
import ticket_system.entities.TicketStatusHistory;
import ticket_system.entities.User;
import ticket_system.enums.TicketStatus;
import ticket_system.repositories.TicketStatusHistoryRepository;
import ticket_system.services.ITicketStatusHistoryService;

import java.util.List;

@Service
public class TicketStatusHistoryServiceImpl implements ITicketStatusHistoryService {

    private final TicketStatusHistoryRepository historyRepository;

    public TicketStatusHistoryServiceImpl(TicketStatusHistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @Override
    public TicketStatusHistory recordStatusChange(Ticket ticket, TicketStatus previousStatus,
                                                  TicketStatus newStatus, User changedBy,
                                                  String notes, String pauseReason) {
        TicketStatusHistory history = TicketStatusHistory.builder()
                .ticket(ticket)
                .previousStatus(previousStatus)
                .newStatus(newStatus)
                .changedBy(changedBy)
                .notes(notes)
                .pauseReason(pauseReason)
                .build();

        return historyRepository.save(history);
    }

    @Override
    public List<TicketStatusHistory> getTicketHistory(Long ticketId) {
        return historyRepository.findByTicketIdOrderByChangedAtAsc(ticketId);
    }
}
