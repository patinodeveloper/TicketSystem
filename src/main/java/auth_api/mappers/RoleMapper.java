package auth_api.mappers;

import auth_api.entities.Role;
import auth_api.entities.dto.RoleDTO;
import auth_api.entities.requests.RoleRequestDTO;

import java.util.List;

public interface RoleMapper {

    RoleDTO toDTO(Role role);

    Role toEntity(RoleRequestDTO roleRequestDTO);

    List<RoleDTO> toDTOList(List<Role> roles);
}
