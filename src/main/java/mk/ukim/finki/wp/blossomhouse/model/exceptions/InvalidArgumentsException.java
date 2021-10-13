package mk.ukim.finki.wp.blossomhouse.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidArgumentsException extends RuntimeException {
    public InvalidArgumentsException(){
        super("Please fill out all forms and try again.");
    }
}
