package ticket_system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ticket_system.entities.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
