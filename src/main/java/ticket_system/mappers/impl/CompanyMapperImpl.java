package ticket_system.mappers.impl;

import org.springframework.stereotype.Component;
import ticket_system.entities.Company;
import ticket_system.entities.dto.CompanyDTO;
import ticket_system.entities.requests.company.CompanyRequestDTO;
import ticket_system.mappers.CompanyMapper;
import ticket_system.mappers.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CompanyMapperImpl implements CompanyMapper {

    @Override
    public CompanyDTO toDTO(Company company) {
        if (company == null) return null;

        return new CompanyDTO(
                company.getId(),
                company.getName(),
                company.getLegalName(),
                company.getRfc(),
                company.getGiro(),
                company.getEmail(),
                company.isActive()
        );
    }

    @Override
    public List<CompanyDTO> toDTOList(List<Company> companies) {
        return companies.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public Company toEntity(CompanyRequestDTO requestDTO) {
        if (requestDTO == null) return null;

        Company company = new Company();

        company.setName(requestDTO.getName());
        company.setLegalName(requestDTO.getLegalName());
        company.setRfc(requestDTO.getRfc());
        company.setGiro(requestDTO.getGiro());
        company.setAddress(requestDTO.getAddress());
        company.setPhone(requestDTO.getPhone());
        company.setSecondPhone(requestDTO.getSecondPhone());
        company.setEmail(requestDTO.getEmail());

        return company;
    }

}
