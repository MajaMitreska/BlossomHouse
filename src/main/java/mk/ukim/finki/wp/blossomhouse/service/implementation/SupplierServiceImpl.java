package mk.ukim.finki.wp.blossomhouse.service.implementation;

import mk.ukim.finki.wp.blossomhouse.model.Supplier;
import mk.ukim.finki.wp.blossomhouse.repository.SupplierRepository;
import mk.ukim.finki.wp.blossomhouse.service.SupplierService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierServiceImpl implements SupplierService
{

    private final SupplierRepository supplierRepository;

    public SupplierServiceImpl(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    public List<Supplier> findAll() {
        return this.supplierRepository.findAll();
    }

    @Override
    public Optional<Supplier> findById(Long id) {
        return this.supplierRepository.findById(id);
    }

    @Override
    public Optional<Supplier> save(String name, String address)
    {
        Supplier supplier = new Supplier(name, address);
        this.supplierRepository.save(supplier);
        return Optional.of(supplier);
    }

    @Override
    public void deleteById(Long id)
    {
        this.supplierRepository.deleteById(id);
    }
}
