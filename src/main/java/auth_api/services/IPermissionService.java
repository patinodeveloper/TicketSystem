package auth_api.services;

import auth_api.entities.dto.PermissionDTO;
import auth_api.entities.requests.PermissionRequestDTO;

import java.util.List;
import java.util.Optional;

public interface IPermissionService {
    List<PermissionDTO> findAll();

    Optional<PermissionDTO> findById(Long id);

    PermissionDTO save(PermissionRequestDTO permissionRequestDTO);

    PermissionDTO update(Long id, PermissionRequestDTO permissionRequestDTO);

    void delete(Long id);
}
