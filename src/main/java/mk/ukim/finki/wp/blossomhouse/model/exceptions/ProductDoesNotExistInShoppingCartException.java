package mk.ukim.finki.wp.blossomhouse.model.exceptions;

public class ProductDoesNotExistInShoppingCartException extends RuntimeException {
    public ProductDoesNotExistInShoppingCartException(Long cartId, Long productId) {
        super(String.format("The product with id: %d does not exist in the shopping cart with id: %d", cartId, productId));
    }
}
