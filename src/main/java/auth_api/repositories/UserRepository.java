package auth_api.repositories;

import auth_api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Buscam un usuario por username o email
    Optional<User> findByUsernameOrEmail(String username, String email);

    // Busca un usuario solo por email
    Optional<User> findByEmail(String email);

    // Busca un usuario solo por username
    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u JOIN FETCH u.roles r LEFT JOIN FETCH r.permissions WHERE u.username = :username")
    Optional<User> findByUsernameWithRoles(@Param("username") String username);
}
