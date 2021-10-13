package mk.ukim.finki.wp.blossomhouse.repository;

import mk.ukim.finki.wp.blossomhouse.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long>
{

}
