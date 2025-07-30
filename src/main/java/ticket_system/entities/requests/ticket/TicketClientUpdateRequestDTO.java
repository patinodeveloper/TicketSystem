package ticket_system.entities.requests.ticket;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketClientUpdateRequestDTO {

    @NotBlank(message = "El titulo es obligatorio")
    @Size(min = 3, max = 120, message = "El titulo debe tener entre 5 y 120 caracteres")
    private String title;

    @NotBlank(message = "La descripcion es obligatoria")
    private String description;

    // Campo para la ruta del archivo (se guarda en BD)
    private String clientEvidence;

    // Campo para el archivo que se sube (se guarda en el proyecto por ahora)
    private MultipartFile clientEvidenceFile;

    @NotNull(message = "El ID del Proyecto es obligatorio")
    private Long projectId;

    @NotNull(message = "El ID del Tipo de Soporte es obligatorio")
    private Long supportTypeId;
}
