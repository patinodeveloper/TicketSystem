package ticket_system.entities.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CompanyDTO {
    private final Long id;
    private final String name;
    private final String legalName;
    private final String rfc;
    private final String giro;
    private final String email;
    private final boolean isActive;

    public CompanyDTO(Long id, String name, String legalName, String rfc, String giro, String email, boolean isActive) {
        this.id = id;
        this.name = name;
        this.legalName = legalName;
        this.rfc = rfc;
        this.giro = giro;
        this.email = email;
        this.isActive = isActive;
    }
}
