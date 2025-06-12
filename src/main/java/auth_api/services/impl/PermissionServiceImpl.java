package auth_api.services.impl;

import auth_api.config.exceptions.NotFoundException;
import auth_api.entities.Module;
import auth_api.entities.Permission;
import auth_api.entities.dto.PermissionDTO;
import auth_api.entities.dto.permissions.SlugPermissionDTO;
import auth_api.entities.requests.PermissionRequestDTO;
import auth_api.mappers.PermissionMapper;
import auth_api.repositories.ModuleRepository;
import auth_api.repositories.PermissionRepository;
import auth_api.repositories.UserRepository;
import auth_api.services.IPermissionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements IPermissionService {

    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    private final PermissionMapper permissionMapper;
    private final ModuleRepository moduleRepository;

    public PermissionServiceImpl(PermissionRepository permissionRepository, UserRepository userRepository,
                                 PermissionMapper permissionMapper, ModuleRepository moduleRepository) {
        this.permissionRepository = permissionRepository;
        this.userRepository = userRepository;
        this.permissionMapper = permissionMapper;
        this.moduleRepository = moduleRepository;
    }

    @Override
    public List<PermissionDTO> findAll() {
        List<Permission> permissions = permissionRepository.findAll();
        return permissionMapper.toDTOList(permissions);
    }

    @Override
    public PermissionDTO findById(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Permiso no encontrado con el ID: " + id));

        return permissionMapper.toDTO(permission);
    }

    @Override
    public PermissionDTO save(PermissionRequestDTO requestDTO) {
        Module module = moduleRepository.findById(requestDTO.getModuleId())
                .orElseThrow(() -> new NotFoundException("Permiso no encontrado con el ID: " + requestDTO.getModuleId()));

        Permission permission = permissionMapper.toEntity(requestDTO, module);
        Permission savedPermission = permissionRepository.save(permission);
        return permissionMapper.toDTO(savedPermission);
    }

    @Override
    public PermissionDTO update(Long id, PermissionRequestDTO requestDTO) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Permiso no encontrado con el ID: " + id));

        Module module = moduleRepository.findById(requestDTO.getModuleId())
                .orElseThrow(() -> new NotFoundException("Permiso no encontrado con el ID: " + id));

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
            throw new NotFoundException("Permiso no encontrado con el ID: " + id);
        }
        permissionRepository.deleteById(id);
    }

    @Override
    public List<SlugPermissionDTO> findByUserId(Long id) {
        return permissionMapper.toBasicDTOList(permissionRepository.findByUserId(id));
    }
}
