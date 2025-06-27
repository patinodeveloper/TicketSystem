package ticket_system.entities.dto;

import lombok.Getter;
import ticket_system.entities.Company;

@Getter
public class CompanyDTO {
    private final Long id;
    private final String name;
    private final String legalName;
    private final String rfc;
    private final String giro;
    private final String address;
    private final String phone;
    private final String secondPhone;
    private final String email;
    private final boolean isActive;

    public CompanyDTO(Company company) {
        this.id = company.getId();
        this.name = company.getName();
        this.legalName = company.getLegalName();
        this.rfc = company.getRfc();
        this.giro = company.getGiro();
        this.address = company.getAddress();
        this.phone = company.getPhone();
        this.secondPhone = company.getSecondPhone();
        this.email = company.getEmail();
        this.isActive = company.isActive();
    }
}
