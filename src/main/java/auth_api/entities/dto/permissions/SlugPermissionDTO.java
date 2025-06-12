package auth_api.entities.dto.permissions;

import auth_api.entities.Permission;
import lombok.Getter;

@Getter
public class SlugPermissionDTO {
    private final String slug;

    public SlugPermissionDTO(Permission permission) {
        this.slug = permission.getSlug();
    }
}
