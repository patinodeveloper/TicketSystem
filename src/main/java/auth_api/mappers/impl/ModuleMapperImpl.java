package auth_api.mappers.impl;

import auth_api.entities.dto.ModuleDTO;
import auth_api.entities.requests.ModuleRequestDTO;
import auth_api.mappers.ModuleMapper;
import auth_api.entities.Module;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ModuleMapperImpl implements ModuleMapper {

    @Override
    public ModuleDTO toDTO(Module module) {
        if (module == null) return null;
        return new ModuleDTO(module);
    }

    @Override
    public Module toEntity(ModuleDTO dto) {
        if (dto == null) return null;
        Module module = new Module();
        module.setId(dto.getId());
        module.setName(dto.getName());
        module.setDescription(dto.getDescription());
        return module;
    }

    @Override
    public List<ModuleDTO> toDTOList(List<Module> modules) {
        return modules.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public Module toEntity(ModuleRequestDTO requestDTO) {
        if (requestDTO == null) return null;
        Module module = new Module();
        module.setName(requestDTO.getName());
        module.setDescription(requestDTO.getDescription());
        return module;
    }
}
