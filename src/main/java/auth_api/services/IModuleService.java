package auth_api.services;

import auth_api.entities.dto.ModuleDTO;
import auth_api.entities.requests.ModuleRequestDTO;

import java.util.List;
import java.util.Optional;

public interface IModuleService {
    List<ModuleDTO> findAll();

    Optional<ModuleDTO> findById(Long id);

    ModuleDTO save(ModuleRequestDTO moduleRequestDTO);

    ModuleDTO update(Long id, ModuleRequestDTO moduleRequestDTO);

    void delete(Long id);
}
