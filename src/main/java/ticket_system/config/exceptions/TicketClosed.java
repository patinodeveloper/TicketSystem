package ticket_system.config.exceptions;

public class TicketClosed extends RuntimeException {
    public TicketClosed(String message) {
        super(message);
    }
}
