package auth_api.mappers.impl;

import auth_api.entities.Permission;
import auth_api.entities.Module;
import auth_api.entities.dto.PermissionDTO;
import auth_api.entities.dto.permissions.SlugPermissionDTO;
import auth_api.entities.requests.PermissionRequestDTO;
import auth_api.mappers.PermissionMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PermissionMapperImpl implements PermissionMapper {

    @Override
    public PermissionDTO toDTO(Permission permission) {
        if (permission == null) return null;
        return new PermissionDTO(permission);
    }

    @Override
    public Permission toEntity(PermissionRequestDTO requestDTO, Module module) {
        if (requestDTO == null) return null;
        Permission permission = new Permission();
        permission.setName(requestDTO.getName());
        permission.setSlug(requestDTO.getSlug());
        permission.setDescription(requestDTO.getDescription());
        permission.setModule(module);
        return permission;
    }

    @Override
    public List<PermissionDTO> toDTOList(List<Permission> permissions) {
        return permissions.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public SlugPermissionDTO toBasicDTO(Permission permission) {
        if (permission == null) return null;
        return new SlugPermissionDTO(permission);
    }

    @Override
    public List<SlugPermissionDTO> toBasicDTOList(List<Permission> permissions) {
        return permissions.stream().map(this::toBasicDTO).collect(Collectors.toList());
    }
}
