package auth_api.services;

import auth_api.entities.dto.RoleDTO;
import auth_api.entities.requests.RoleRequestDTO;

import java.util.List;
import java.util.Optional;

public interface IRoleService {
    List<RoleDTO> findAll();

    Optional<RoleDTO> findById(Long id);

    RoleDTO save(RoleRequestDTO roleRequestDTO);

    RoleDTO update(Long id, RoleRequestDTO roleRequestDTO);

    void delete(Long id);
}
