package ticket_system.services;

import ticket_system.entities.dto.SupportTypeDTO;
import ticket_system.entities.requests.support_type.SupportTypeRequestDTO;

import java.util.List;

public interface ISupportTypeService {
    List<SupportTypeDTO> findAll();

    SupportTypeDTO findById(Long id);

    SupportTypeDTO save(SupportTypeRequestDTO supportTypeRequestDTO);

    SupportTypeDTO update(Long id, SupportTypeRequestDTO supportTypeRequestDTO);

    void delete(Long id);
}
