package auth_api.services;

import auth_api.entities.dto.PermissionDTO;
import auth_api.entities.dto.permissions.SlugPermissionDTO;
import auth_api.entities.requests.PermissionRequestDTO;

import java.util.List;

public interface IPermissionService {
    List<PermissionDTO> findAll();

    PermissionDTO findById(Long id);

    PermissionDTO save(PermissionRequestDTO permissionRequestDTO);

    PermissionDTO update(Long id, PermissionRequestDTO permissionRequestDTO);

    void delete(Long id);

    List<SlugPermissionDTO> findByUserId(Long id);
}
