package ticket_system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ticket_system.entities.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
