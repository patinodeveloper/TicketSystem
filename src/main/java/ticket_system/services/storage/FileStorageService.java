package ticket_system.services.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ticket_system.config.exceptions.NotFoundException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    private final Path fileStorageLocation;

    public FileStorageService(@Value("${app.upload.dir:uploads}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("No se pudo crear el directorio de uploads", ex);
        }
    }

    public String storeFile(MultipartFile file, String subDirectory) {
        // Limpia el nombre del archivo
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Verifica que el archivo no esté vacio
            if (fileName.contains("..")) {
                throw new RuntimeException("El nombre del archivo contiene una secuencia de ruta inválida " + fileName);
            }

            // Crea un nombre único para el archivo
            String fileExtension = getFileExtension(fileName);
            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

            // Crea el subdirectorio si no existe
            Path subDirectoryPath = this.fileStorageLocation.resolve(subDirectory);
            Files.createDirectories(subDirectoryPath);

            // Ruta completa del archivo
            Path targetLocation = subDirectoryPath.resolve(uniqueFileName);

            // Copiar archivo desde memoria
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return subDirectory + "/" + uniqueFileName;

        } catch (IOException ex) {
            throw new RuntimeException("No se pudo almacenar el archivo " + fileName, ex);
        }
    }

    public void deleteFile(String filePath) {
        try {
            Path file = this.fileStorageLocation.resolve(filePath).normalize();
            Files.deleteIfExists(file);
        } catch (IOException ex) {
            throw new RuntimeException("No se pudo eliminar el archivo " + filePath, ex);
        }
    }

    public Path getFilePath(String filePath) {
        return this.fileStorageLocation.resolve(filePath).normalize();
    }

    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        return (lastDotIndex == -1) ? "" : fileName.substring(lastDotIndex);
    }

    /**
     * Busca un archivo de evidencia en las posibles ubicaciones
     *
     * @param filename Nombre del archivo
     * @return Resource del archivo encontrado
     * @throws NotFoundException si el archivo no se encuentra
     */
    public Resource getEvidenceResource(String filename) {
        String[] possiblePaths = {
                "tickets/client-evidence/" + filename,
                "tickets/support-evidence/" + filename
        };

        for (String possiblePath : possiblePaths) {
            Path filePath = getFilePath(possiblePath);

            if (Files.exists(filePath)) {
                try {
                    Resource resource = new UrlResource(filePath.toUri());
                    if (resource.exists() && resource.isReadable()) {
                        return resource;
                    }
                } catch (MalformedURLException ex) {
                    throw new RuntimeException("Error al acceder al archivo: " + filename, ex);
                }
            }
        }

        throw new NotFoundException("Archivo de evidencia no encontrado: " + filename);
    }

    /**
     * Obtiene el tipo de contenido de un archivo
     *
     * @param filename Nombre del archivo
     * @return Content type del archivo
     */
    public String getContentType(String filename) {
        String[] possiblePaths = {
                "tickets/client-evidence/" + filename,
                "tickets/support-evidence/" + filename
        };

        for (String possiblePath : possiblePaths) {
            Path filePath = getFilePath(possiblePath);

            if (Files.exists(filePath)) {
                try {
                    String contentType = Files.probeContentType(filePath);
                    return contentType != null ? contentType : "application/octet-stream";
                } catch (IOException ex) {
                    return "application/octet-stream";
                }
            }
        }

        return "application/octet-stream";
    }

    public boolean isValidFileType(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (
                contentType.startsWith("image/") ||
                        contentType.equals("application/pdf") ||
                        contentType.startsWith("video/") ||
                        contentType.equals("application/msword") ||
                        contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
        );
    }

    public boolean isValidFileSize(MultipartFile file) {
        // Límite de 10MB
        return file.getSize() <= 10 * 1024 * 1024;
    }
}