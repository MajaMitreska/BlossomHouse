package mk.ukim.finki.wp.blossomhouse.model.exceptions;

public class RecordDoesNotExistException extends RuntimeException {
    public RecordDoesNotExistException(Long id) {
        super(String.format("Record with id: %d does not exist", id));
    }
}
