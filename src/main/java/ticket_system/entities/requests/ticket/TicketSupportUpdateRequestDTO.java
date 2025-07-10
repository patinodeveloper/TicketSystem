package ticket_system.entities.requests.ticket;

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
public class TicketSupportUpdateRequestDTO {

    @NotBlank(message = "La resolución es obligatoria")
    @Size(min = 5, message = "La resolucion debe contener más de 5 caracteres")
    private String resolution;

    private String supportEvidence;
}
