package mk.ukim.finki.wp.blossomhouse.service;

import mk.ukim.finki.wp.blossomhouse.model.BlossomUser;
import mk.ukim.finki.wp.blossomhouse.model.Order;
import mk.ukim.finki.wp.blossomhouse.model.Product;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderService
{
    Optional<Order> findById(Long orderId);
    void deleteProductsFromOrder(Long orderId);
    Optional<Order> saveProductsFromShoppingCartToOrder(BlossomUser user, String name, String address, String phoneNumber, String email);

}
