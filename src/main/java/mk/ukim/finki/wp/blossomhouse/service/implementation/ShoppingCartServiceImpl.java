package mk.ukim.finki.wp.blossomhouse.service.implementation;

import mk.ukim.finki.wp.blossomhouse.model.BlossomUser;
import mk.ukim.finki.wp.blossomhouse.model.Product;
import mk.ukim.finki.wp.blossomhouse.model.ProductInShoppingCart;
import mk.ukim.finki.wp.blossomhouse.model.ShoppingCart;
import mk.ukim.finki.wp.blossomhouse.model.enumerations.ShoppingCartStatus;
import mk.ukim.finki.wp.blossomhouse.model.exceptions.ProductAlreadyInShoppingCartException;
import mk.ukim.finki.wp.blossomhouse.model.exceptions.ProductNotFoundException;
import mk.ukim.finki.wp.blossomhouse.model.exceptions.ShoppingCartNotFoundException;
import mk.ukim.finki.wp.blossomhouse.model.exceptions.UserNotFoundException;
import mk.ukim.finki.wp.blossomhouse.repository.BlossomUserRepository;
import mk.ukim.finki.wp.blossomhouse.repository.ProductInShoppingCartRepository;
import mk.ukim.finki.wp.blossomhouse.repository.ProductRepository;
import mk.ukim.finki.wp.blossomhouse.repository.ShoppingCartRepository;
import mk.ukim.finki.wp.blossomhouse.service.ProductInShoppingCartService;
import mk.ukim.finki.wp.blossomhouse.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService
{

    private final ShoppingCartRepository shoppingCartRepository;
    private final BlossomUserRepository blossomUserRepository;
    private final ProductRepository productRepository;
    private final ProductInShoppingCartService productInShoppingCartService;

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository, BlossomUserRepository blossomUserRepository, ProductRepository productRepository, ProductInShoppingCartService productInShoppingCartService) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.blossomUserRepository = blossomUserRepository;
        this.productRepository = productRepository;
        this.productInShoppingCartService = productInShoppingCartService;
    }

    @Override
    public List<Product> listAllProductsInShoppingCart(Long cartId)
    {
        if(!this.shoppingCartRepository.findById(cartId).isEmpty()) {

            return this.productInShoppingCartService.getProductsInShoppingCart(cartId);
        }


        throw new ShoppingCartNotFoundException(cartId);
    }

    @Override
    public ShoppingCart getActiveShoppingCart(String username)
    {
        BlossomUser user = this.blossomUserRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        return this.shoppingCartRepository.findByUser(user)
                .orElseGet(() -> {
                     ShoppingCart shoppingCart = new ShoppingCart(user);
                     shoppingCart.setStatus(ShoppingCartStatus.CREATED);
                     return this.shoppingCartRepository.save(shoppingCart);
                });

    }

    @Override
    public ShoppingCart addProductToShoppingCart(String username, Long productId)
    {
        ShoppingCart shoppingCart = this.getActiveShoppingCart(username);
        Product product = this.productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));


        List<Product> products = this.productInShoppingCartService.getProductsInShoppingCart(shoppingCart.getId());

        if(products.stream().filter(p -> p.getId().equals(productId)).count() != 0)
        {
            throw new ProductAlreadyInShoppingCartException(product.getName(), username);
        }

        this.productInShoppingCartService.save(shoppingCart.getId(), productId);
        return this.shoppingCartRepository.save(this.computeTotalProductsSum(shoppingCart));
    }

    @Override
    public ShoppingCart removeProductFromShoppingCart(String username, Long productId)
    {
        BlossomUser user = this.blossomUserRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        ShoppingCart shoppingCart = this.shoppingCartRepository.findByUser(user).get();

        Product product = this.productRepository.findById(productId).get();

        List<Product> products = this.productInShoppingCartService.getProductsInShoppingCart(shoppingCart.getId());

        if(products.contains(product))
        {
            this.productInShoppingCartService.delete(shoppingCart.getId(), productId);
        }
        return this.shoppingCartRepository.save(this.computeTotalProductsSum(shoppingCart));
    }

    @Override
    public ShoppingCart computeTotalProductsSum(ShoppingCart shoppingCart)
    {
        Integer totalSum = 0;
        List<ProductInShoppingCart> products = this.productInShoppingCartService.findAllByShoppingCart(shoppingCart.getId());

        for (ProductInShoppingCart product : products)
        {
            totalSum += product.getProduct().getPrice() * product.getQuantity();
        }

        shoppingCart.setTotalAmount(totalSum);
        return this.shoppingCartRepository.save(shoppingCart);
    }
}
