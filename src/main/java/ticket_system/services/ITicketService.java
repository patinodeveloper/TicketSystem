package ticket_system.services;

import ticket_system.entities.User;
import ticket_system.entities.dto.TicketDTO;
import ticket_system.entities.requests.ticket.TicketClientUpdateRequestDTO;
import ticket_system.entities.requests.ticket.TicketPauseRequestDTO;
import ticket_system.entities.requests.ticket.TicketRequestDTO;
import ticket_system.entities.requests.ticket.TicketSupportUpdateRequestDTO;

import java.util.List;

public interface ITicketService {
    List<TicketDTO> findAll();

    TicketDTO findById(Long id);

    TicketDTO save(TicketRequestDTO ticketRequestDTO, User user);

    TicketDTO update(Long id, TicketClientUpdateRequestDTO requestDTO, User user);

    TicketDTO resolveTicket(Long id, TicketSupportUpdateRequestDTO requestDTO, User user);

    TicketDTO updateTicketStatus(Long id, User user);

    TicketDTO pauseTicket(Long id, TicketPauseRequestDTO pauseRequest, User user);

    TicketDTO resumeTicket(Long id, User user);

    void delete(Long id);
}
