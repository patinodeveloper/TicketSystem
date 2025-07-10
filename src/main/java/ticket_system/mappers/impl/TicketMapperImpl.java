package ticket_system.mappers.impl;

import org.springframework.stereotype.Component;
import ticket_system.entities.Ticket;
import ticket_system.entities.dto.TicketDTO;
import ticket_system.entities.requests.ticket.TicketClientUpdateRequestDTO;
import ticket_system.entities.requests.ticket.TicketRequestDTO;
import ticket_system.entities.requests.ticket.TicketSupportUpdateRequestDTO;
import ticket_system.mappers.TicketMapper;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TicketMapperImpl implements TicketMapper {
    @Override
    public TicketDTO toDTO(Ticket ticket) {
        if (ticket == null) return null;

        return new TicketDTO(ticket);
    }

    @Override
    public List<TicketDTO> toDTOList(List<Ticket> tickets) {
        if (tickets == null) return null;

        return tickets.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public Ticket toEntity(TicketRequestDTO ticketRequestDTO) {
        if (ticketRequestDTO == null) return null;

        Ticket ticket = new Ticket();

        ticket.setTitle(ticketRequestDTO.getTitle());
        ticket.setDescription(ticketRequestDTO.getDescription());
        ticket.setClientEvidence(ticketRequestDTO.getClientEvidence());

        return ticket;
    }

    @Override
    public void updateEntityFromClientDTO(Ticket ticket, TicketClientUpdateRequestDTO requestDTO) {
        if (ticket == null || requestDTO == null) return;

        ticket.setTitle(requestDTO.getTitle());
        ticket.setDescription(requestDTO.getDescription());
        ticket.setClientEvidence(requestDTO.getClientEvidence());
    }

    @Override
    public void updateEntityFromSupportDTO(Ticket ticket, TicketSupportUpdateRequestDTO requestDTO) {
        if (ticket == null || requestDTO == null) return;

        ticket.setResolution(requestDTO.getResolution());
        ticket.setSupportEvidence(requestDTO.getSupportEvidence());
    }
}
