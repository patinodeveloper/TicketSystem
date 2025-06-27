package ticket_system.services.impl;

import org.springframework.stereotype.Service;
import ticket_system.config.exceptions.NotFoundException;
import ticket_system.entities.SupportType;
import ticket_system.entities.dto.SupportTypeDTO;
import ticket_system.entities.requests.support_type.SupportTypeRequestDTO;
import ticket_system.mappers.SupportTypeMapper;
import ticket_system.repositories.SupportTypeRepository;
import ticket_system.services.ISupportTypeService;

import java.util.List;

@Service
public class SupportTypeServiceImpl implements ISupportTypeService {

    private final SupportTypeRepository supportTypeRepository;
    private final SupportTypeMapper supportTypeMapper;

    public SupportTypeServiceImpl(SupportTypeRepository supportTypeRepository, SupportTypeMapper supportTypeMapper) {
        this.supportTypeRepository = supportTypeRepository;
        this.supportTypeMapper = supportTypeMapper;
    }

    @Override
    public List<SupportTypeDTO> findAll() {
        List<SupportType> supportTypes = supportTypeRepository.findAll();
        return supportTypeMapper.toDTOList(supportTypes);
    }

    @Override
    public SupportTypeDTO findById(Long id) {
        SupportType supportType = supportTypeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tipo de Soporte no encontrado con el id: " + id));
        return supportTypeMapper.toDTO(supportType);
    }

    @Override
    public SupportTypeDTO save(SupportTypeRequestDTO supportTypeRequestDTO) {
        SupportType supportType = supportTypeMapper.toEntity(supportTypeRequestDTO);
        SupportType savedSupportType = supportTypeRepository.save(supportType);
        return supportTypeMapper.toDTO(savedSupportType);
    }

    @Override
    public SupportTypeDTO update(Long id, SupportTypeRequestDTO supportTypeRequestDTO) {
        SupportType supportType = supportTypeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tipo de Soporte no encontrado con el id: " + id));

        supportType.setName(supportTypeRequestDTO.getName());
        supportType.setDescription(supportTypeRequestDTO.getDescription());

        SupportType updatedSupportType = supportTypeRepository.save(supportType);
        return supportTypeMapper.toDTO(updatedSupportType);
    }

    @Override
    public void delete(Long id) {
        if (!supportTypeRepository.existsById(id)) {
            throw new NotFoundException("Tipo de Soporte no encontrado con el ID: " + id);
        }
        supportTypeRepository.deleteById(id);
    }
}
