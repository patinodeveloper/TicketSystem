package ticket_system.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ticket_system.entities.Ticket;
import ticket_system.enums.TicketStatus;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Ticket t SET t.status = :status WHERE t.id = :id")
    void updateTicketStatus(@Param("id") Long id, @Param("status") TicketStatus status);
}
