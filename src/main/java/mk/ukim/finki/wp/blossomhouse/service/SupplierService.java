package mk.ukim.finki.wp.blossomhouse.service;

import mk.ukim.finki.wp.blossomhouse.model.Supplier;

import java.util.List;
import java.util.Optional;

public interface SupplierService
{
    List<Supplier> findAll();
    Optional<Supplier> findById(Long id);
    Optional<Supplier> save(String name, String address);
    void deleteById(Long id);
}
