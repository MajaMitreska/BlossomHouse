package mk.ukim.finki.wp.blossomhouse.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ShoppingCartNotFoundException extends RuntimeException
{
    public ShoppingCartNotFoundException(Long cartId)
    {
        super(String.format("Shopping Cart with ID %d was not found!", cartId));
    }
}
