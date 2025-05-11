package auth_api.services.impl;

import auth_api.entities.Module;
import auth_api.entities.User;
import auth_api.entities.dto.ModuleDTO;
import auth_api.entities.dto.UserDTO;
import auth_api.entities.requests.ModuleRequestDTO;
import auth_api.mappers.ModuleMapper;
import auth_api.repositories.ModuleRepository;
import auth_api.services.IModuleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ModuleServiceImpl implements IModuleService {

    private final ModuleRepository moduleRepository;
    private final ModuleMapper moduleMapper;

    public ModuleServiceImpl(ModuleRepository moduleRepository, ModuleMapper moduleMapper) {
        this.moduleRepository = moduleRepository;
        this.moduleMapper = moduleMapper;
    }

    @Override
    public List<ModuleDTO> findAll() {
        List<Module> modules = moduleRepository.findAll();
        return moduleMapper.toDTOList(modules);
    }

    @Override
    public Optional<ModuleDTO> findById(Long id) {
        Optional<Module> optionalModule = moduleRepository.findById(id);
        if (optionalModule.isPresent()) {
            Module module = optionalModule.get();

            ModuleDTO moduleDTO = moduleMapper.toDTO(module);

            return Optional.of(moduleDTO);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public ModuleDTO save(ModuleRequestDTO moduleRequestDTO) {
        Module module = moduleMapper.toEntity(moduleRequestDTO);
        Module savedModule = moduleRepository.save(module);
        return moduleMapper.toDTO(savedModule);
    }

    @Override
    public ModuleDTO update(Long id, ModuleRequestDTO moduleRequestDTO) {
        // Verificamos si el modulo existe
        Module module = moduleRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Módulo no encontrado"));
        module.setName(moduleRequestDTO.getName());
        module.setDescription(moduleRequestDTO.getDescription());

        // Guardamos la entidad actualizada
        Module updatedModule = moduleRepository.save(module);
        // Convertimos la entidad actualizada a DTO
        return moduleMapper.toDTO(updatedModule);
    }

    @Override
    public void delete(Long id) {
        if (!moduleRepository.existsById(id)) {
            throw new RuntimeException("Módulo no encontrado con el ID: " + id);
        }
        moduleRepository.deleteById(id);
    }
}
