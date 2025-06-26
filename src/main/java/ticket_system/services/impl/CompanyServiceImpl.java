package ticket_system.services.impl;

import org.springframework.stereotype.Service;
import ticket_system.config.exceptions.NotFoundException;
import ticket_system.entities.Company;
import ticket_system.entities.dto.CompanyDTO;
import ticket_system.entities.requests.company.CompanyRequestDTO;
import ticket_system.mappers.CompanyMapper;
import ticket_system.repositories.CompanyRepository;
import ticket_system.services.ICompanyService;

import java.util.List;

@Service
public class CompanyServiceImpl implements ICompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    public CompanyServiceImpl(CompanyRepository companyRepository, CompanyMapper companyMapper) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
    }

    @Override
    public List<CompanyDTO> findAll() {
        List<Company> companies = companyRepository.findAll();
        return companyMapper.toDTOList(companies);
    }

    @Override
    public CompanyDTO findById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Empresa no encontrada con el id: " + id));

        return companyMapper.toDTO(company);
    }

    @Override
    public CompanyDTO save(CompanyRequestDTO companyRequestDTO) {
        Company company = companyMapper.toEntity(companyRequestDTO);
        Company savedCompany = companyRepository.save(company);
        return companyMapper.toDTO(savedCompany);
    }

    @Override
    public CompanyDTO update(Long id, CompanyRequestDTO companyRequestDTO) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Empresa no encontrada con el id: " + id));

        company.setName(companyRequestDTO.getName());
        company.setLegalName(companyRequestDTO.getLegalName());
        company.setRfc(companyRequestDTO.getRfc());
        company.setGiro(companyRequestDTO.getGiro());
        company.setAddress(companyRequestDTO.getAddress());
        company.setPhone(companyRequestDTO.getPhone());
        company.setSecondPhone(companyRequestDTO.getSecondPhone());
        company.setEmail(companyRequestDTO.getEmail());

        Company updatedCompany = companyRepository.save(company);
        return companyMapper.toDTO(updatedCompany);
    }

    @Override
    public void delete(Long id) {
        if (!companyRepository.existsById(id)) throw new NotFoundException("Empresa no encontrada con el id:" + id);
        companyRepository.deleteById(id);
    }
}
