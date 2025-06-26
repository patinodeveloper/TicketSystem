package ticket_system.services;

import ticket_system.entities.dto.CompanyDTO;
import ticket_system.entities.requests.company.CompanyRequestDTO;

import java.util.List;

public interface ICompanyService {
    List<CompanyDTO> findAll();

    CompanyDTO findById(Long id);

    CompanyDTO save(CompanyRequestDTO companyRequestDTO);

    CompanyDTO update(Long id, CompanyRequestDTO companyRequestDTO);

    void delete(Long id);
}
