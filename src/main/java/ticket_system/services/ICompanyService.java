package ticket_system.services;

import ticket_system.entities.dto.CompanyDTO;

import java.util.List;

public interface ICompanyService {
    List<CompanyDTO> findAll();

    CompanyDTO findById(Long id);

//    CompanyDTO save(CompanyRequestDTO companyRequestDTO);

//    CompanyDTO update(Long id, CompanyUpdateRequestDTO companyRequestDTO);

    void delete(Long id);
}
