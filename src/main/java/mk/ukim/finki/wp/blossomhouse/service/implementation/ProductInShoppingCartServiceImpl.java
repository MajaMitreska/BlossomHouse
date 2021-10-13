package mk.ukim.finki.wp.blossomhouse.service.implementation;

import mk.ukim.finki.wp.blossomhouse.model.Product;
import mk.ukim.finki.wp.blossomhouse.model.ProductInShoppingCart;
import mk.ukim.finki.wp.blossomhouse.model.ShoppingCart;
import mk.ukim.finki.wp.blossomhouse.model.exceptions.ProductDoesNotExistInShoppingCartException;
import mk.ukim.finki.wp.blossomhouse.model.exceptions.ProductNotFoundException;
import mk.ukim.finki.wp.blossomhouse.model.exceptions.RecordDoesNotExistException;
import mk.ukim.finki.wp.blossomhouse.model.exceptions.ShoppingCartNotFoundException;
import mk.ukim.finki.wp.blossomhouse.repository.BlossomUserRepository;
import mk.ukim.finki.wp.blossomhouse.repository.ProductInShoppingCartRepository;
import mk.ukim.finki.wp.blossomhouse.repository.ProductRepository;
import mk.ukim.finki.wp.blossomhouse.repository.ShoppingCartRepository;
import mk.ukim.finki.wp.blossomhouse.service.ProductInShoppingCartService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductInShoppingCartServiceImpl implements ProductInShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductInShoppingCartRepository productInShoppingCartRepository;
    private final BlossomUserRepository userRepository;
    private final ProductRepository productRepository;

    public ProductInShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository,
                                            ProductInShoppingCartRepository productInShoppingCartRepository,
                                            BlossomUserRepository userRepository,
                                            ProductRepository productRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.productInShoppingCartRepository = productInShoppingCartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }


    @Override
    public List<Product> getProductsInShoppingCart(Long cartId) {

        if(this.shoppingCartRepository.findById(cartId).isPresent())
        {
            if (!this.productInShoppingCartRepository.allProductsInShoppingCart(cartId).isEmpty()) {
                return this.productInShoppingCartRepository.allProductsInShoppingCart(cartId);

            }
            else {
                return new ArrayList<>();
            }


        }

        throw new ShoppingCartNotFoundException(cartId);
    }

    @Override
    public ProductInShoppingCart findRecord(Long cartId, Long productId) {
        ShoppingCart shoppingCart = this.shoppingCartRepository.findById(cartId).orElseThrow(
                () -> new ShoppingCartNotFoundException(cartId)
        );
        Product product = this.productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));

        if(this.productInShoppingCartRepository.getProductInShoppingCartByShoppingCartAndProduct(shoppingCart, product).isPresent())
        {
            return this.productInShoppingCartRepository.getProductInShoppingCartByShoppingCartAndProduct(shoppingCart, product)
                    .orElseThrow(() -> new ProductDoesNotExistInShoppingCartException(cartId, productId));
        }
        throw new ProductDoesNotExistInShoppingCartException(cartId, productId);
    }

    @Override
    public ProductInShoppingCart save(Long cartId, Long productId) {
        ShoppingCart shoppingCart = this.shoppingCartRepository.findById(cartId).orElseThrow(
                () -> new ShoppingCartNotFoundException(cartId)
        );
        Product product = this.productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
        return this.productInShoppingCartRepository.save(new ProductInShoppingCart(shoppingCart, product));
    }

    @Override
    public void delete(Long cartId, Long productId) {
        ShoppingCart shoppingCart = this.shoppingCartRepository.findById(cartId).orElseThrow(
                () -> new ShoppingCartNotFoundException(cartId)
        );
        Product product = this.productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
        ProductInShoppingCart record = this.productInShoppingCartRepository.getProductInShoppingCartByShoppingCartAndProduct(shoppingCart, product)
                .orElseThrow(()-> new RecordDoesNotExistException(shoppingCart.getId()));
        this.productInShoppingCartRepository.delete(record);
    }

    @Override
    public ProductInShoppingCart increaseQuantity(Long cartId, Long productId) {
        ProductInShoppingCart record = this.findRecord(cartId, productId);
        Integer increasedQuantity = record.getQuantity() + 1;
        record.setQuantity(increasedQuantity);
        return this.productInShoppingCartRepository.save(record);
    }

    @Override
    public ProductInShoppingCart decreaseQuantity(Long cartId, Long productId) {
        ProductInShoppingCart record = this.findRecord(cartId, productId);
        if (record.getQuantity() > 1) {
            Integer decreasedQuantity = record.getQuantity() - 1;
            record.setQuantity(decreasedQuantity);
        }
        return this.productInShoppingCartRepository.save(record);
    }

    @Override
    public ProductInShoppingCart findById(Long id) {
       return this.productInShoppingCartRepository.findById(id).orElseThrow( () -> new RecordDoesNotExistException(id));
    }

    @Override
    public List<ProductInShoppingCart> findAllByShoppingCart(Long cartId) {
        ShoppingCart shoppingCart = this.shoppingCartRepository.findById(cartId).orElseThrow(
                () -> new ShoppingCartNotFoundException(cartId));
        return this.productInShoppingCartRepository.findAllByShoppingCart(shoppingCart);
    }
}
