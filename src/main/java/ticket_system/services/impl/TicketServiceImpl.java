package ticket_system.services.impl;

import org.springframework.stereotype.Service;
import ticket_system.config.exceptions.ForbiddenException;
import ticket_system.config.exceptions.InvalidTicketStatusTransitionException;
import ticket_system.config.exceptions.NotFoundException;
import ticket_system.config.exceptions.TicketClosed;
import ticket_system.entities.*;
import ticket_system.entities.dto.TicketDTO;
import ticket_system.entities.requests.ticket.TicketClientUpdateRequestDTO;
import ticket_system.entities.requests.ticket.TicketRequestDTO;
import ticket_system.entities.requests.ticket.TicketSupportUpdateRequestDTO;
import ticket_system.enums.RoleName;
import ticket_system.enums.TicketStatus;
import ticket_system.mappers.TicketMapper;
import ticket_system.repositories.*;
import ticket_system.services.ITicketService;

import java.time.LocalDate;
import java.util.List;

@Service
public class TicketServiceImpl implements ITicketService {

    private final TicketRepository ticketRepository;
    private final ProjectRepository projectRepository;
    private final SupportTypeRepository typeRepository;
    private final UserRepository userRepository;
    private final TicketMapper ticketMapper;

    public TicketServiceImpl(TicketRepository ticketRepository, ProjectRepository projectRepository,
                             SupportTypeRepository typeRepository, UserRepository userRepository,
                             TicketMapper ticketMapper) {
        this.ticketRepository = ticketRepository;
        this.projectRepository = projectRepository;
        this.typeRepository = typeRepository;
        this.userRepository = userRepository;
        this.ticketMapper = ticketMapper;
    }

    @Override
    public List<TicketDTO> findAll() {
        List<Ticket> tickets = ticketRepository.findAll();
        return ticketMapper.toDTOList(tickets);
    }

    @Override
    public TicketDTO findById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ticket no encontrado con el id: " + id));

        return ticketMapper.toDTO(ticket);
    }

    @Override
    public TicketDTO save(TicketRequestDTO requestDTO, User client) {
        Project project = projectRepository.findById(requestDTO.getProjectId())
                .orElseThrow(() ->
                        new NotFoundException("Proyecto no encontrado con el id: " + requestDTO.getProjectId()));
        SupportType supportType = typeRepository.findById(requestDTO.getSupportTypeId())
                .orElseThrow(() ->
                        new NotFoundException("Tipo de Soporte no encontrado con el id: " + requestDTO.getSupportTypeId()));

        Ticket ticket = ticketMapper.toEntity(requestDTO);
        ticket.setProject(project);
        ticket.setSupportType(supportType);
        ticket.setClient(client);

        Ticket savedTicket = ticketRepository.save(ticket);
        return ticketMapper.toDTO(savedTicket);
    }

    @Override
    public TicketDTO update(Long id, TicketClientUpdateRequestDTO requestDTO, User user) {
        Ticket existingTicket = ticketRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Ticket no encontrado con el id: " + id));

        if (existingTicket.getStatus() == TicketStatus.CLOSED) throw new TicketClosed("Ticket cerrado");

        if (!existingTicket.getClient().getId().equals(user.getId()))
            throw new ForbiddenException("No tienes permiso para modificar este ticket");

        Project project = projectRepository.findById(requestDTO.getProjectId())
                .orElseThrow(() ->
                        new NotFoundException("Proyecto no encontrado con el id: " + requestDTO.getProjectId()));
        SupportType supportType = typeRepository.findById(requestDTO.getSupportTypeId())
                .orElseThrow(() ->
                        new NotFoundException("Tipo de Soporte no encontrado con el id: " + requestDTO.getSupportTypeId()));

        ticketMapper.updateEntityFromClientDTO(existingTicket, requestDTO);
        existingTicket.setProject(project);
        existingTicket.setSupportType(supportType);

        Ticket updatedTicket = ticketRepository.save(existingTicket);
        return ticketMapper.toDTO(updatedTicket);
    }

    @Override
    public TicketDTO resolveTicket(Long id, TicketSupportUpdateRequestDTO requestDTO, User user) {
        if (user.getRole() != RoleName.SUPPORT && user.getRole() != RoleName.ADMIN)
            throw new ForbiddenException("No tienes permiso para atender tickets");

        Ticket existingTicket = ticketRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Ticket no encontrado con el id: " + id));

        ticketMapper.updateEntityFromSupportDTO(existingTicket, requestDTO);
        existingTicket.setStatus(TicketStatus.CLOSED);
        existingTicket.setEndDate(LocalDate.now());
        existingTicket.setSupport(user);

        Ticket resolvedTicket = ticketRepository.save(existingTicket);
        return ticketMapper.toDTO(resolvedTicket);
    }

    @Override
    public TicketDTO updateTicketStatus(Long id, User user) {
        // Valida que el usuario es SUPPORT o ADMIN
        if (user.getRole() != RoleName.SUPPORT && user.getRole() != RoleName.ADMIN) {
            throw new ForbiddenException("No tienes permiso para cambiar el estado de tickets");
        }

        Ticket existingTicket = ticketRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ticket no encontrado con el id: " + id));

        // Valida que el ticket esté en status OPEN
        if (existingTicket.getStatus() != TicketStatus.OPEN) {
            throw new InvalidTicketStatusTransitionException("Solo se pueden cambiar a IN_PROGRESS los tickets con estado OPEN");
        }

        // Cambiar el status a IN_PROGRESS
        existingTicket.setStatus(TicketStatus.IN_PROGRESS);

        // Asignamos el ticket al usuario SUPPORT que lo está tomando
        if (existingTicket.getSupport() == null) {
            existingTicket.setSupport(user);
        }

        Ticket updatedTicket = ticketRepository.save(existingTicket);
        return ticketMapper.toDTO(updatedTicket);
    }

    @Override
    public void delete(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new NotFoundException("Ticket no encontrado con el ID: " + id);
        }
        ticketRepository.deleteById(id);
    }
}
