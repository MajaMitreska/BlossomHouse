package mk.ukim.finki.wp.blossomhouse.service;

import mk.ukim.finki.wp.blossomhouse.model.BlossomUser;
import mk.ukim.finki.wp.blossomhouse.model.enumerations.Role;

public interface BlossomUserService
{
    BlossomUser login(String username, String password);
    BlossomUser register(String username, String password, String repeatPassword, String name, String surname, Role role);
}
