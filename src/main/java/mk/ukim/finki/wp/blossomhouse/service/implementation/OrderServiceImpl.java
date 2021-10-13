package mk.ukim.finki.wp.blossomhouse.service.implementation;

import mk.ukim.finki.wp.blossomhouse.model.BlossomUser;
import mk.ukim.finki.wp.blossomhouse.model.Order;
import mk.ukim.finki.wp.blossomhouse.model.Product;
import mk.ukim.finki.wp.blossomhouse.model.ShoppingCart;
import mk.ukim.finki.wp.blossomhouse.model.exceptions.ProductNotFoundException;
import mk.ukim.finki.wp.blossomhouse.repository.OrderRepository;
import mk.ukim.finki.wp.blossomhouse.service.OrderService;
import mk.ukim.finki.wp.blossomhouse.service.ShoppingCartService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService
{
    private final OrderRepository orderRepository;
    private final ShoppingCartService shoppingCartService;

    public OrderServiceImpl(OrderRepository orderRepository, ShoppingCartService shoppingCartService) {
        this.orderRepository = orderRepository;
        this.shoppingCartService = shoppingCartService;
    }

    @Override
    public Optional<Order> findById(Long orderId) {
        return this.orderRepository.findById(orderId);
    }

    @Override
    public void deleteProductsFromOrder(Long orderId) {

    }

    @Override
    public Optional<Order> saveProductsFromShoppingCartToOrder(BlossomUser user,
                                                               String name,
                                                               String address,
                                                               String phoneNumber,
                                                               String email) {

        ShoppingCart shoppingCart = this.shoppingCartService.getActiveShoppingCart(user.getUsername());
        List<Product> productsFromShoppingCart = shoppingCart.getProducts();
        Integer totalAmount = shoppingCart.getTotalAmount();
        Order order = new Order(user, name, address, phoneNumber, email, totalAmount);


        for (Product product : productsFromShoppingCart) {
            order.getProducts().add(product);
        }

        return Optional.of(this.orderRepository.save(order));
    }


}
