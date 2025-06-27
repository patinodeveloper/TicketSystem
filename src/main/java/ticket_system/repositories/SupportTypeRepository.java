package ticket_system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ticket_system.entities.SupportType;

public interface SupportTypeRepository extends JpaRepository<SupportType, Long> {

}
