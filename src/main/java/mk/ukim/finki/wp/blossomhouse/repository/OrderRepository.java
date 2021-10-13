package mk.ukim.finki.wp.blossomhouse.repository;

import mk.ukim.finki.wp.blossomhouse.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
