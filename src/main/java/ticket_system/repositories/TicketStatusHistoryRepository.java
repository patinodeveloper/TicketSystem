package ticket_system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ticket_system.entities.TicketStatusHistory;

import java.util.List;

public interface TicketStatusHistoryRepository extends JpaRepository<TicketStatusHistory, Long> {

    List<TicketStatusHistory> findByTicketIdOrderByChangedAtAsc(Long ticketId);
}