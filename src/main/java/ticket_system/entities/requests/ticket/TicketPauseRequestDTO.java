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
public class TicketPauseRequestDTO {

    @NotBlank(message = "La razón de pausa es obligatoria")
    @Size(max = 255, message = "La razón no puede exceder 255 caracteres")
    private String pauseReason;
}
