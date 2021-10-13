package mk.ukim.finki.wp.blossomhouse.service;

import mk.ukim.finki.wp.blossomhouse.model.Product;
import mk.ukim.finki.wp.blossomhouse.model.ProductInShoppingCart;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ProductInShoppingCartService
{
    List<Product> getProductsInShoppingCart(Long cartId);
    ProductInShoppingCart findRecord(Long cartId, Long productId);
    ProductInShoppingCart save(Long cartId, Long productId);
    void delete(Long cartId, Long productId);
    ProductInShoppingCart increaseQuantity(Long cartId, Long productId);
    ProductInShoppingCart decreaseQuantity(Long cartId, Long productId);
    ProductInShoppingCart findById(Long id);
    List<ProductInShoppingCart> findAllByShoppingCart(Long cartId);
}
