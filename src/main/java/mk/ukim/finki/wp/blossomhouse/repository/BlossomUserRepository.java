package mk.ukim.finki.wp.blossomhouse.repository;

import mk.ukim.finki.wp.blossomhouse.model.BlossomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlossomUserRepository extends JpaRepository<BlossomUser, String>
{
    Optional<BlossomUser> findByUsernameAndPassword (String username, String password);
    Optional<BlossomUser> findByUsername(String username);
}
