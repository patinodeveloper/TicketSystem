package ticket_system.mappers;

import ticket_system.entities.Ticket;
import ticket_system.entities.dto.TicketDTO;
import ticket_system.entities.requests.ticket.TicketClientUpdateRequestDTO;
import ticket_system.entities.requests.ticket.TicketRequestDTO;
import ticket_system.entities.requests.ticket.TicketSupportUpdateRequestDTO;

import java.util.List;

public interface TicketMapper {
    TicketDTO toDTO(Ticket ticket);

    List<TicketDTO> toDTOList(List<Ticket> tickets);

    Ticket toEntity(TicketRequestDTO ticketRequestDTO);

    void updateEntityFromClientDTO(Ticket ticket, TicketClientUpdateRequestDTO requestDTO);

    void updateEntityFromSupportDTO(Ticket ticket, TicketSupportUpdateRequestDTO requestDTO);
}
