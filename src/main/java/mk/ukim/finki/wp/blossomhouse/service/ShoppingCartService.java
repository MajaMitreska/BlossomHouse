package mk.ukim.finki.wp.blossomhouse.service;

import mk.ukim.finki.wp.blossomhouse.model.Product;
import mk.ukim.finki.wp.blossomhouse.model.ShoppingCart;

import java.util.List;

public interface ShoppingCartService
{
    List<Product> listAllProductsInShoppingCart(Long cartId);
    ShoppingCart getActiveShoppingCart(String username);
    ShoppingCart addProductToShoppingCart(String username, Long productId);
    ShoppingCart removeProductFromShoppingCart(String username, Long productId);
    ShoppingCart computeTotalProductsSum(ShoppingCart shoppingCart);
}
