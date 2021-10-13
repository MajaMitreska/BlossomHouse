package mk.ukim.finki.wp.blossomhouse.repository;

import mk.ukim.finki.wp.blossomhouse.model.Product;
import mk.ukim.finki.wp.blossomhouse.model.ProductInShoppingCart;
import mk.ukim.finki.wp.blossomhouse.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductInShoppingCartRepository extends JpaRepository<ProductInShoppingCart, Long> {

    // all products in given shopping cart
    @Query(value = "SELECT pSh.product from ProductInShoppingCart pSh WHERE pSh.shoppingCart.id = :cartId")
    List<Product> allProductsInShoppingCart(@Param("cartId") Long cartId);


    // find record for a given cartId and productId
    Optional<ProductInShoppingCart> getProductInShoppingCartByShoppingCartAndProduct(ShoppingCart shoppingCart, Product product);


    List<ProductInShoppingCart> findAllByShoppingCart(ShoppingCart shoppingCart);

    //@Transactional
    void deleteByShoppingCartAndProduct(ShoppingCart shoppingCart, Product product);

    //@Transactional
    void deleteAllByProduct(Product product);

    List<ProductInShoppingCart> getAllByProduct(Product product);
}

