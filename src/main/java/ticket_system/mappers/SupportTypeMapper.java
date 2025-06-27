package ticket_system.mappers;

import ticket_system.entities.SupportType;
import ticket_system.entities.dto.SupportTypeDTO;
import ticket_system.entities.requests.support_type.SupportTypeRequestDTO;

import java.util.List;

public interface SupportTypeMapper {
    SupportTypeDTO toDTO(SupportType supportType);

    List<SupportTypeDTO> toDTOList(List<SupportType> supportTypes);

    SupportType toEntity(SupportTypeRequestDTO supportTypeRequestDTO);
}
