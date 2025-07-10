package ticket_system.config.exceptions;

public class InvalidTicketStatusTransitionException extends RuntimeException {
    public InvalidTicketStatusTransitionException(String message) {
        super(message);
    }
}
