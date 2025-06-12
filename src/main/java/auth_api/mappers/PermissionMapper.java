package auth_api.mappers;

import auth_api.entities.Module;
import auth_api.entities.Permission;
import auth_api.entities.dto.PermissionDTO;
import auth_api.entities.dto.permissions.SlugPermissionDTO;
import auth_api.entities.requests.PermissionRequestDTO;

import java.util.List;

public interface PermissionMapper {
    PermissionDTO toDTO(Permission permission);

    Permission toEntity(PermissionRequestDTO requestDTO, Module module);

    List<PermissionDTO> toDTOList(List<Permission> permissions);

    SlugPermissionDTO toBasicDTO(Permission permission);

    List<SlugPermissionDTO> toBasicDTOList(List<Permission> permissions);
}
