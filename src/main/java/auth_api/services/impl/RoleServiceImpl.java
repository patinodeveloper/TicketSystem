package auth_api.services.impl;

import auth_api.entities.Permission;
import auth_api.entities.Role;
import auth_api.entities.User;
import auth_api.entities.dto.RoleDTO;
import auth_api.entities.dto.UserDTO;
import auth_api.entities.requests.RoleRequestDTO;
import auth_api.mappers.RoleMapper;
import auth_api.repositories.PermissionRepository;
import auth_api.repositories.RoleRepository;
import auth_api.services.IRoleService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RoleServiceImpl implements IRoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleMapper roleMapper;

    public RoleServiceImpl(RoleRepository roleRepository, PermissionRepository permissionRepository,
                           RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    public List<RoleDTO> findAll() {
        List<Role> roles = roleRepository.findAll();
        return roleMapper.toDTOList(roles);
    }

    @Override
    public Optional<RoleDTO> findById(Long id) {
        Optional<Role> optionalRole = roleRepository.findById(id);

        if (optionalRole.isPresent()) {
            Role role = optionalRole.get();

            RoleDTO roleDTO = roleMapper.toDTO(role);

            return Optional.of(roleDTO);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public RoleDTO save(RoleRequestDTO roleRequestDTO) {
        Role role = roleMapper.toEntity(roleRequestDTO);

        if (roleRequestDTO.getPermissionIds() != null && !roleRequestDTO.getPermissionIds().isEmpty()) {
            Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(roleRequestDTO.getPermissionIds()));
            if (permissions.size() != roleRequestDTO.getPermissionIds().size()) {
                throw new RuntimeException("Algunos permisos no existen");
            }
            role.setPermissions(permissions);
        }
        Role updatedRole = roleRepository.save(role);
        return roleMapper.toDTO(updatedRole);
    }

    @Override
    public RoleDTO update(Long id, RoleRequestDTO roleRequestDTO) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
        role.setName(roleRequestDTO.getName());
        role.setSlug(roleRequestDTO.getSlug());
        role.setDescription(roleRequestDTO.getDescription());

        if (roleRequestDTO.getPermissionIds() != null && !roleRequestDTO.getPermissionIds().isEmpty()) {
            Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(roleRequestDTO.getPermissionIds()));
            if (permissions.size() != roleRequestDTO.getPermissionIds().size()) {
                throw new RuntimeException("Algunos permisos no existen");
            }
            role.setPermissions(permissions);
        }
        Role updatedRole = roleRepository.save(role);
        return roleMapper.toDTO(updatedRole);
    }

    @Override
    public void delete(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new RuntimeException("Role no encontrado con el ID: " + id);
        }
        roleRepository.deleteById(id);
    }

//    private Set<Permission> getPermissionsFromIds(Set<Long> permissionIds) {
//        Set<Permission> permissions = new HashSet<>();
//        if (permissionIds != null && !permissionIds.isEmpty()) {
//            permissions = new HashSet<>(permissionRepository.findAllById(permissionIds));
//
//            // Verifica que todos los permisos solicitados existen
//            if (permissions.size() != permissionIds.size()) {
//                throw new RuntimeException("No se encontraron los siguientes permisos: " + permissionIds);
//            }
//        }
//        return permissions;
//    }
}
