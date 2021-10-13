package mk.ukim.finki.wp.blossomhouse.service;


import mk.ukim.finki.wp.blossomhouse.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService
{
    List<Category> listCategories();
    Optional<Category> findById(Long id);
    Optional<Category> findByName(String name);
}
