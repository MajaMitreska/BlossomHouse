package mk.ukim.finki.wp.blossomhouse.service.implementation;

import mk.ukim.finki.wp.blossomhouse.model.Category;
import mk.ukim.finki.wp.blossomhouse.model.Product;
import mk.ukim.finki.wp.blossomhouse.model.ProductInShoppingCart;
import mk.ukim.finki.wp.blossomhouse.model.Supplier;
import mk.ukim.finki.wp.blossomhouse.model.exceptions.CategoryNotFoundException;
import mk.ukim.finki.wp.blossomhouse.model.exceptions.SupplierNotFoundException;
import mk.ukim.finki.wp.blossomhouse.model.exceptions.ProductNotFoundException;
import mk.ukim.finki.wp.blossomhouse.repository.CategoryRepository;
import mk.ukim.finki.wp.blossomhouse.repository.ProductInShoppingCartRepository;
import mk.ukim.finki.wp.blossomhouse.repository.ProductRepository;
import mk.ukim.finki.wp.blossomhouse.repository.SupplierRepository;
import mk.ukim.finki.wp.blossomhouse.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService
{

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
    private final ProductInShoppingCartRepository productInShoppingCartRepository;

    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository,
                              SupplierRepository supplierRepository,
                              ProductInShoppingCartRepository productInShoppingCartRepository) {
        this.productRepository = productRepository;

        this.categoryRepository = categoryRepository;
        this.supplierRepository = supplierRepository;
        this.productInShoppingCartRepository = productInShoppingCartRepository;
    }

    @Override
    public List<Product> findAll() {
        return this.productRepository.findAll();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return this.productRepository.findById(id);
    }

    @Override
    public Optional<Product> findByName(String name) {
        return this.productRepository.findByName(name);
    }

    @Override
    public List<Product> listByCategory(Long categoryId) {
        return this.productRepository.findAll().stream().filter(
                p -> p.getCategory().getId().equals(categoryId)).collect(Collectors.toList());
    }

    @Override
    public Optional<Product> save(String name, Integer price, Integer quantity, Long categoryId, Long supplierId, String image, String description, String details)
    {
        Category category = this.categoryRepository.findById(categoryId).get();
        Supplier supplier = this.supplierRepository.findById(supplierId).get();

        Product product = new Product(name, price, quantity, category, supplier, image, description, details);
        return Optional.of(this.productRepository.save(product));
    }

    @Override
    @Transactional
    public Optional<Product> edit(Long id, String name, Integer price, Integer quantity, Long categoryId, Long supplierId, String image, String description, String details) {

        Product product = this.productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

        product.setName(name);
        product.setPrice(price);
        product.setQuantity(quantity);

        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        product.setCategory(category);

        Supplier manufacturer = this.supplierRepository.findById(supplierId)
                .orElseThrow(() -> new SupplierNotFoundException(supplierId));
        product.setSupplier(manufacturer);

        product.setImage(image);
        product.setDescription(description);
        product.setDetails(details);

        this.productRepository.save(product);
        return Optional.of(product);

    }

    @Override
    public List<Product> findAllByNameLike(String name) {
        return this.productRepository.findAllByNameContainingIgnoreCase(name);
    }

    @Override
    public Page<Product> findAllWithPagination(Pageable pageable) {
        return this.productRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public void deleteById(Long id)
    {
        Product product = this.productRepository.findById(id).get();
        List<ProductInShoppingCart> recordsContainingProduct = this.productInShoppingCartRepository.getAllByProduct(product);
        if (!recordsContainingProduct.isEmpty()){
            this.productInShoppingCartRepository.deleteAllByProduct(product);
        }
        this.productRepository.deleteById(id);
    }

    @Override
    public Product like(Long id) {
        Product product = this.productRepository.findById(id).
                orElseThrow(() -> new ProductNotFoundException(id));
        Integer current_num_likes = product.getLikes();
        product.setLikes(current_num_likes+1);
        return this.productRepository.save(product);
    }
}
