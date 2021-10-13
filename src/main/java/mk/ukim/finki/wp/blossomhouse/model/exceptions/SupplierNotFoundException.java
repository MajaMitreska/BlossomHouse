package mk.ukim.finki.wp.blossomhouse.model.exceptions;

public class SupplierNotFoundException extends RuntimeException {
    public SupplierNotFoundException(Long supplierId) {
        super(String.format("Supplier with id %d not found", supplierId));
    }
}
