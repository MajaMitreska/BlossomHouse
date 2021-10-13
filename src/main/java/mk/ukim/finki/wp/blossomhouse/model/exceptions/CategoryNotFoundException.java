package mk.ukim.finki.wp.blossomhouse.model.exceptions;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(Long categoryId) {
        super(String.format("Category with id %d not found", categoryId));
    }
}
