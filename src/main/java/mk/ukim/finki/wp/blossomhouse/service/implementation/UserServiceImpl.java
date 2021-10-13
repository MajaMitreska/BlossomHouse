package mk.ukim.finki.wp.blossomhouse.service.implementation;

import mk.ukim.finki.wp.blossomhouse.model.BlossomUser;
import mk.ukim.finki.wp.blossomhouse.model.enumerations.Role;
import mk.ukim.finki.wp.blossomhouse.model.exceptions.*;
import mk.ukim.finki.wp.blossomhouse.repository.BlossomUserRepository;
import mk.ukim.finki.wp.blossomhouse.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class UserServiceImpl implements UserService {

    private final BlossomUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(BlossomUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public BlossomUser register(String username, String password, String repeatPassword, String email, String name, String surname, Role userRole) {
        if (username==null || username.isEmpty()  || password==null || password.isEmpty())
            throw new InvalidUsernameOrPasswordException();
        if (!password.equals(repeatPassword))
            throw new PasswordsDoNotMatchException();
        if(this.userRepository.findByUsername(username).isPresent())
            throw new UsernameAlreadyExistsException(username);
        BlossomUser user = new BlossomUser(username,passwordEncoder.encode(password),name,surname, email, userRole);
        return userRepository.save(user);
    }

    @Override
    public BlossomUser findByUsername(String username) {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    public BlossomUser saveGoogleUser(String parameters) {
        //Name: [107802586112517228564],
        // Granted Authorities: [[ROLE_USER,
        // SCOPE_https://www.googleapis.com/auth/userinfo.email,
        // SCOPE_https://www.googleapis.com/auth/userinfo.profile,
        // SCOPE_openid]],
        // User Attributes: [{at_hash=qRWUDV9utcS3vzjv4NGKlQ,
        // sub=107802586112517228564,
        // email_verified=true,
        // iss=https://accounts.google.com,
        // given_name=Maja,
        // locale=en,
        // nonce=Dl9wzB7LybcBQIM3bUh7L45b6XeVvvXv5nxHKpsk5r0,
        // picture=https://lh6.googleusercontent.com/-tb7G1pTGyGc/AAAAAAAAAAI/AAAAAAAAAAA/AMZuuclfoVVyRHZoVn_nwBLN-IhPJl1jwQ/s96-c/photo.jpg,
        // aud=[578031800085-84l7ah1e6ojileppc1tqcn06da955v7g.apps.googleusercontent.com],
        // azp=578031800085-84l7ah1e6ojileppc1tqcn06da955v7g.apps.googleusercontent.com,
        // name=Maja Mitreska,
        // exp=2021-03-29T20:49:24Z,
        // family_name=Mitreska,
        // iat=2021-03-29T19:49:24Z,
        // email=mitreskamaja7@gmail.com}]
        String [] splitParameters = parameters.split(", ");
        String username = splitParameters[15].substring(5);
        String name = splitParameters[9].substring(11);
        String surname = splitParameters[17].substring(12);
        String tmpEmail = splitParameters[19].substring(6);
        String email = tmpEmail.substring(0, (tmpEmail.length() - 2));
        long password = (long) (9 + Math.random() * 100000);
        String strPassword = Long.toString(password);
        //(String username, String password, String name, String surname, String email, Role role)
        BlossomUser user = new BlossomUser(username, passwordEncoder.encode(strPassword), name, surname, email, Role.ROLE_USER);
        return this.userRepository.save(user);
    }

    public BlossomUser findGoogleUserByUsername(String parameters){
        String [] splitParameters = parameters.split(", ");
        String username = splitParameters[15].substring(5);
        return this.findByUsername(username);
    }

    @Override
    public BlossomUser login(String username, String password) {
        if (username==null || username.isEmpty() || password==null || password.isEmpty()) {
            throw new InvalidUsernameOrPasswordException();
        }
        return userRepository.findByUsernameAndPassword(username,
                passwordEncoder.encode(password)).orElseThrow(InvalidArgumentsException::new);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(s).orElseThrow(() -> new UsernameNotFoundException(s));
    }

}
