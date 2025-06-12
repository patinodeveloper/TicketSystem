package auth_api.repositories;

import auth_api.entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    // Consultas personalizadas
    @Query("SELECT DISTINCT p FROM Permission p " +
            "JOIN p.roles r " +
            "JOIN r.users u " +
            "WHERE u.id = :userId")
    List<Permission> findByUserId(@Param("userId") Long id);
}
