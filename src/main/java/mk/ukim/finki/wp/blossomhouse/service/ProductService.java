package mk.ukim.finki.wp.blossomhouse.service;

import mk.ukim.finki.wp.blossomhouse.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductService
{
    List<Product> findAll();
    Optional<Product> findById(Long id);
    Optional<Product> findByName(String name);
    List<Product> listByCategory(Long categoryId);
    Optional<Product> save(String name, Integer price, Integer quantity, Long categoryId, Long supplierId, String image, String description, String details);
    void deleteById(Long id);
    Product like(Long id);
    Optional<Product> edit(Long id, String name, Integer price, Integer quantity, Long categoryId, Long supplierId, String image, String description, String details);
    List<Product> findAllByNameLike(String name);

    Page<Product> findAllWithPagination(Pageable pageable);

}
