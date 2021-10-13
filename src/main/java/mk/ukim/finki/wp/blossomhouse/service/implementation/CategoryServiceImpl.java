package mk.ukim.finki.wp.blossomhouse.service.implementation;

import mk.ukim.finki.wp.blossomhouse.model.Category;
import mk.ukim.finki.wp.blossomhouse.repository.CategoryRepository;
import mk.ukim.finki.wp.blossomhouse.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService
{
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> listCategories() {
        return this.categoryRepository.findAll();
    }

    @Override
    public Optional<Category> findById(Long id) {
        return this.categoryRepository.findById(id);
    }

    @Override
    public Optional<Category> findByName(String name) {
        return this.categoryRepository.findAllByNameContainingIgnoreCase(name);
    }
}
