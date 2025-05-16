package auth_api.config.exceptions;

public class InvalidTokenException extends RuntimeException {
    
    public InvalidTokenException(String message) {
        super(message);
    }
}
