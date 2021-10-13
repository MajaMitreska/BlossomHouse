package mk.ukim.finki.wp.blossomhouse.service;

import mk.ukim.finki.wp.blossomhouse.model.BlossomUser;
import mk.ukim.finki.wp.blossomhouse.model.enumerations.Role;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    BlossomUser login(String username, String password);
    BlossomUser register(String username, String password, String repeatPassword, String email, String name, String surname, Role role);
    BlossomUser findByUsername(String username);
    BlossomUser saveGoogleUser(String parameters);
    BlossomUser findGoogleUserByUsername(String parameters);
}
