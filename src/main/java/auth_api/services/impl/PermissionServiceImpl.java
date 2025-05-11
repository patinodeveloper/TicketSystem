package auth_api.services.impl;

import auth_api.entities.Module;
import auth_api.entities.Permission;
import auth_api.entities.User;
import auth_api.entities.dto.PermissionDTO;
import auth_api.entities.dto.UserDTO;
import auth_api.entities.requests.PermissionRequestDTO;
import auth_api.mappers.PermissionMapper;
import auth_api.repositories.ModuleRepository;
import auth_api.repositories.PermissionRepository;
import auth_api.services.IPermissionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionServiceImpl implements IPermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;
    private final ModuleRepository moduleRepository; // Repositorio de Módulos para obtener el módulo por ID

    public PermissionServiceImpl(PermissionRepository permissionRepository, PermissionMapper permissionMapper,
                                 ModuleRepository moduleRepository) {
        this.permissionRepository = permissionRepository;
        this.permissionMapper = permissionMapper;
        this.moduleRepository = moduleRepository;
    }

    @Override
    public List<PermissionDTO> findAll() {
        List<Permission> permissions = permissionRepository.findAll();
        return permissionMapper.toDTOList(permissions);
    }

    @Override
    public Optional<PermissionDTO> findById(Long id) {
        Optional<Permission> optionalPermission = permissionRepository.findById(id);

        if (optionalPermission.isPresent()) {
            Permission permission = optionalPermission.get();

            PermissionDTO permissionDTO = permissionMapper.toDTO(permission);

            return Optional.of(permissionDTO);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public PermissionDTO save(PermissionRequestDTO requestDTO) {
        Module module = moduleRepository.findById(requestDTO.getModuleId())
                .orElseThrow(() -> new RuntimeException("Módulo no encontrado"));

        Permission permission = permissionMapper.toEntity(requestDTO, module);
        Permission savedPermission = permissionRepository.save(permission);
        return permissionMapper.toDTO(savedPermission);
    }

    @Override
    public PermissionDTO update(Long id, PermissionRequestDTO requestDTO) {
        Permission permission = permissionRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Permiso no encontrado"));

        Module module = moduleRepository.findById(requestDTO.getModuleId())
                .orElseThrow(() -> new RuntimeException("Módulo no encontrado"));

        permission.setName(requestDTO.getName());
        permission.setSlug(requestDTO.getSlug());
        permission.setDescription(requestDTO.getDescription());
        permission.setModule(module);

        Permission updatedPermission = permissionRepository.save(permission);
        return permissionMapper.toDTO(updatedPermission);
    }

    @Override
    public void delete(Long id) {
        if (!permissionRepository.existsById(id)) {
            throw new RuntimeException("Permiso no encontrado con el ID: " + id);
        }
        permissionRepository.deleteById(id);
    }
}
