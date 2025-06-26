package ticket_system.entities.requests.company;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRequestDTO {
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String name;

//    @NotBlank(message = "La razon social es obligatoria")
    @Size(min = 3, max = 50, message = "La razón social debe tener entre 3 y 150 caracteres")
    private String legalName;

//    @NotBlank(message = "El rfc es obligatorio")
    @Size(min = 3, max = 50, message = "El rfc debe tener entre 3 y 13 caracteres")
    private String rfc;

    @NotBlank(message = "El giro de la empresa es obligatorio")
    private String giro;

    private String address;

    @NotBlank(message = "El número de teléfono es obligatorio")
    private String phone;

    private String secondPhone;

//    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    private String email;
}
