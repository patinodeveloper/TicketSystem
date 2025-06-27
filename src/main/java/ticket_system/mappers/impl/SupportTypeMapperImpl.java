package ticket_system.mappers.impl;

import org.springframework.stereotype.Component;
import ticket_system.entities.SupportType;
import ticket_system.entities.dto.SupportTypeDTO;
import ticket_system.entities.requests.support_type.SupportTypeRequestDTO;
import ticket_system.mappers.SupportTypeMapper;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SupportTypeMapperImpl implements SupportTypeMapper {
    @Override
    public SupportTypeDTO toDTO(SupportType supportType) {
        if (supportType == null) return null;

        return new SupportTypeDTO(supportType);
    }

    @Override
    public List<SupportTypeDTO> toDTOList(List<SupportType> supportTypes) {
        return supportTypes.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public SupportType toEntity(SupportTypeRequestDTO supportTypeRequestDTO) {
        if (supportTypeRequestDTO == null) return null;

        SupportType supportType = new SupportType();

        supportType.setName(supportTypeRequestDTO.getName());
        supportType.setDescription(supportTypeRequestDTO.getDescription());

        return supportType;
    }
}
