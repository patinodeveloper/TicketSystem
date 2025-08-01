package ticket_system.services.impl;

import org.springframework.stereotype.Service;
import ticket_system.config.exceptions.ForbiddenException;
import ticket_system.config.exceptions.InvalidTicketStatusTransitionException;
import ticket_system.config.exceptions.NotFoundException;
import ticket_system.config.exceptions.TicketClosed;
import ticket_system.entities.*;
import ticket_system.entities.dto.TicketDTO;
import ticket_system.entities.requests.ticket.TicketClientUpdateRequestDTO;
import ticket_system.entities.requests.ticket.TicketPauseRequestDTO;
import ticket_system.entities.requests.ticket.TicketRequestDTO;
import ticket_system.entities.requests.ticket.TicketSupportUpdateRequestDTO;
import ticket_system.enums.RoleName;
import ticket_system.enums.TicketStatus;
import ticket_system.mappers.TicketMapper;
import ticket_system.repositories.*;
import ticket_system.services.ITicketService;
import ticket_system.services.ITicketStatusHistoryService;
import ticket_system.services.storage.FileStorageService;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketServiceImpl implements ITicketService {

    private final TicketRepository ticketRepository;
    private final ProjectRepository projectRepository;
    private final SupportTypeRepository typeRepository;
    private final TicketMapper ticketMapper;
    private final FileStorageService fileStorageService;
    private final ITicketStatusHistoryService historyService;

    public TicketServiceImpl(TicketRepository ticketRepository, ProjectRepository projectRepository,
                             SupportTypeRepository typeRepository, UserRepository userRepository,
                             TicketMapper ticketMapper, FileStorageService fileStorageService,
                             ITicketStatusHistoryService historyService) {
        this.ticketRepository = ticketRepository;
        this.projectRepository = projectRepository;
        this.typeRepository = typeRepository;
        this.ticketMapper = ticketMapper;
        this.fileStorageService = fileStorageService;
        this.historyService = historyService;
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

        // Comprobamos que existe evidencia por parte del cliente
        if (requestDTO.getClientEvidenceFile() != null && !requestDTO.getClientEvidenceFile().isEmpty()) {
            String filePath = fileStorageService.storeFile(
                    requestDTO.getClientEvidenceFile(),
                    "tickets/client-evidence"
            );
            ticket.setClientEvidence(filePath);
        }

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

        // Comprobamos que existe evidencia por parte del cliente
        if (requestDTO.getClientEvidenceFile() != null && !requestDTO.getClientEvidenceFile().isEmpty()) {
            // Eliminamos el archivo anterior si existe
            if (existingTicket.getClientEvidence() != null) {
                fileStorageService.deleteFile(existingTicket.getClientEvidence());
            }

            // Guardamos el nuevo archivo
            String filePath = fileStorageService.storeFile(
                    requestDTO.getClientEvidenceFile(),
                    "tickets/client-evidence"
            );
            requestDTO.setClientEvidence(filePath);
        }

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

        if (existingTicket.getStatus() == TicketStatus.IN_PROGRESS &&
                existingTicket.getCurrentProgressStart() != null) {

            long currentSessionMinutes = Duration.between(
                    existingTicket.getCurrentProgressStart(),
                    LocalDateTime.now()
            ).toMinutes();

            long newTotal = (existingTicket.getTotalProgressMinutes() != null ?
                    existingTicket.getTotalProgressMinutes() : 0L) + currentSessionMinutes;

            existingTicket.setTotalProgressMinutes(newTotal);
            existingTicket.setCurrentProgressStart(null);
        }

        if (requestDTO.getSupportEvidenceFile() != null && !requestDTO.getSupportEvidenceFile().isEmpty()) {
            if (existingTicket.getSupportEvidence() != null) {
                fileStorageService.deleteFile(existingTicket.getSupportEvidence());
            }

            String filePath = fileStorageService.storeFile(
                    requestDTO.getSupportEvidenceFile(),
                    "tickets/support-evidence"
            );
            requestDTO.setSupportEvidence(filePath);
        }

        TicketStatus previousStatus = existingTicket.getStatus();

        ticketMapper.updateEntityFromSupportDTO(existingTicket, requestDTO);
        existingTicket.setStatus(TicketStatus.CLOSED);
        existingTicket.setEndDate(LocalDate.now());
        existingTicket.setSupport(user);

        Ticket resolvedTicket = ticketRepository.save(existingTicket);

        historyService.recordStatusChange(resolvedTicket, previousStatus, TicketStatus.CLOSED,
                user, requestDTO.getResolution(), null);

        return new TicketDTO(resolvedTicket);
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

        TicketStatus previousStatus = existingTicket.getStatus();
        existingTicket.setStatus(TicketStatus.IN_PROGRESS);
        existingTicket.setCurrentProgressStart(LocalDateTime.now());

        // Asignamos el ticket al usuario SUPPORT que lo está tomando
        if (existingTicket.getSupport() == null) {
            existingTicket.setSupport(user);
        }

        Ticket updatedTicket = ticketRepository.save(existingTicket);

        // Registra el cambio de estado
        historyService.recordStatusChange(updatedTicket, previousStatus, TicketStatus.IN_PROGRESS,
                user, "Ticket tomado por soporte", null);

        return new TicketDTO(updatedTicket);
    }

    @Override
    public TicketDTO pauseTicket(Long id, TicketPauseRequestDTO pauseRequest, User user) {
        if (user.getRole() != RoleName.SUPPORT && user.getRole() != RoleName.ADMIN) {
            throw new ForbiddenException("No tienes permiso para pausar tickets");
        }

        Ticket existingTicket = ticketRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ticket no encontrado con el id: " + id));

        if (existingTicket.getStatus() != TicketStatus.IN_PROGRESS) {
            throw new InvalidTicketStatusTransitionException("Solo se pueden pausar tickets que estén EN PROGRESO");
        }

        // Calcula y acumula el tiempo de la sesión actual
        if (existingTicket.getCurrentProgressStart() != null) {
            long currentSessionMinutes = Duration.between(
                    existingTicket.getCurrentProgressStart(),
                    LocalDateTime.now()
            ).toMinutes();

            long newTotal = (existingTicket.getTotalProgressMinutes() != null ?
                    existingTicket.getTotalProgressMinutes() : 0L) + currentSessionMinutes;

            existingTicket.setTotalProgressMinutes(newTotal);
            existingTicket.setCurrentProgressStart(null);
        }

        TicketStatus previousStatus = existingTicket.getStatus();
        existingTicket.setStatus(TicketStatus.PAUSED);

        Ticket updatedTicket = ticketRepository.save(existingTicket);

        // Registra la pausa por alguna razón
        historyService.recordStatusChange(updatedTicket, previousStatus, TicketStatus.PAUSED,
                user, "Ticket pausado", pauseRequest.getPauseReason());

        return new TicketDTO(updatedTicket);
    }

    @Override
    public TicketDTO resumeTicket(Long id, User user) {
        if (user.getRole() != RoleName.SUPPORT && user.getRole() != RoleName.ADMIN) {
            throw new ForbiddenException("No tienes permiso para reanudar tickets");
        }

        Ticket existingTicket = ticketRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ticket no encontrado con el id: " + id));

        if (existingTicket.getStatus() != TicketStatus.PAUSED) {
            throw new InvalidTicketStatusTransitionException("Solo se pueden reanudar tickets que estén PAUSADOS");
        }

        TicketStatus previousStatus = existingTicket.getStatus();
        existingTicket.setStatus(TicketStatus.IN_PROGRESS);
        existingTicket.setCurrentProgressStart(LocalDateTime.now());

        if (existingTicket.getSupport() == null) {
            existingTicket.setSupport(user);
        }

        Ticket updatedTicket = ticketRepository.save(existingTicket);

        // Registrar reanudacion
        historyService.recordStatusChange(updatedTicket, previousStatus, TicketStatus.IN_PROGRESS,
                user, "Ticket reanudado", null);

        return new TicketDTO(updatedTicket);
    }


    @Override
    public void delete(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new NotFoundException("Ticket no encontrado con el ID: " + id);
        }

        // Se obtiene el ticket para eliminar archivos asociados
        Ticket ticket = ticketRepository.findById(id).orElse(null);
        if (ticket != null) {
            // Eliminar archivos de evidencia si existen
            if (ticket.getClientEvidence() != null) {
                fileStorageService.deleteFile(ticket.getClientEvidence());
            }
            if (ticket.getSupportEvidence() != null) {
                fileStorageService.deleteFile(ticket.getSupportEvidence());
            }
        }

        ticketRepository.deleteById(id);
    }
}
