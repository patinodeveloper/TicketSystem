package auth_api.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
@ToString(exclude = "roles")
@EqualsAndHashCode(exclude = "roles")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @JsonManagedReference
    private Set<Role> roles = new HashSet<>();

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return roles.stream()
//                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getSlug().toUpperCase()))
//                .collect(Collectors.toSet());
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        // Agregar roles con el prefijo ROLE_
        roles.forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getSlug().toUpperCase()));

            // Agregar permisos de cada rol
            role.getPermissions().forEach(permission ->
                    authorities.add(new SimpleGrantedAuthority(permission.getSlug()))
            );
        });

        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
