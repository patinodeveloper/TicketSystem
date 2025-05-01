package auth_api.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "roles")
@EqualsAndHashCode(exclude = "roles")
@Table(name = "permissions")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id")
    @JsonBackReference
    private Module module;

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Role> roles = new HashSet<>();
}