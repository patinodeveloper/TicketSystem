package auth_api.mappers;

import auth_api.entities.Module;
import auth_api.entities.dto.ModuleDTO;
import auth_api.entities.requests.ModuleRequestDTO;

import java.util.List;

public interface ModuleMapper {
    ModuleDTO toDTO(Module module);

    Module toEntity(ModuleDTO dto);

    List<ModuleDTO> toDTOList(List<Module> modules);

    Module toEntity(ModuleRequestDTO requestDTO);
}
