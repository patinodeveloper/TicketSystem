package ticket_system.mappers;

import ticket_system.entities.Company;
import ticket_system.entities.dto.CompanyDTO;
import ticket_system.entities.requests.company.CompanyRequestDTO;

import java.util.List;

public interface CompanyMapper {
    CompanyDTO toDTO(Company company);

    List<CompanyDTO> toDTOList(List<Company> companies);

    Company toEntity(CompanyRequestDTO requestDTO);
}
