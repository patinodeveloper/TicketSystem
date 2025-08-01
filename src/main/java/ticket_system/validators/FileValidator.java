package ticket_system.validators;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ticket_system.entities.dto.TicketDTO;
import ticket_system.entities.responses.ApiResponse;
import ticket_system.services.storage.FileStorageService;

@Component
public class FileValidator {
    private final FileStorageService fileStorageService;

    public FileValidator(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    /**
     * Valida archivos para tickets y retorna error HTTP si es inválido
     */
    public ResponseEntity<ApiResponse<TicketDTO>> validateFileForTicket(MultipartFile file) {
        if (!fileStorageService.isValidFileType(file)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("TIpo de archivo no permitido"));
        }

        if (!fileStorageService.isValidFileSize(file)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("El archivo excede el tamaño máximo permitido (10MB)"));
        }

        return null;
    }
    /**
     * Validación booleana simple
     */
    public boolean isValidFile(MultipartFile file) {
        return fileStorageService.isValidFileType(file) &&
                fileStorageService.isValidFileSize(file);
    }
}
