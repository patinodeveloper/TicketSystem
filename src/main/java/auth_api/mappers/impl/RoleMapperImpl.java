package auth_api.mappers.impl;

import auth_api.entities.Role;
import auth_api.entities.dto.RoleDTO;
import auth_api.entities.requests.RoleRequestDTO;
import auth_api.mappers.RoleMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoleMapperImpl implements RoleMapper {

    @Override
    public RoleDTO toDTO(Role role) {
        if (role == null) return null;
        return new RoleDTO(role);
    }

    @Override
    public Role toEntity(RoleRequestDTO roleRequestDTO) {
        if (roleRequestDTO == null) return null;
        Role role = new Role();
        role.setName(roleRequestDTO.getName());
        role.setSlug(roleRequestDTO.getSlug());
        role.setDescription(roleRequestDTO.getDescription());
        return role;
    }

    @Override
    public List<RoleDTO> toDTOList(List<Role> roles) {
        return roles.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
