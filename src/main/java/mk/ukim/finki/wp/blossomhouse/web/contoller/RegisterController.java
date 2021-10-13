package mk.ukim.finki.wp.blossomhouse.web.contoller;

import mk.ukim.finki.wp.blossomhouse.model.enumerations.Role;
import mk.ukim.finki.wp.blossomhouse.model.exceptions.InvalidArgumentsException;
import mk.ukim.finki.wp.blossomhouse.model.exceptions.PasswordsDoNotMatchException;
import mk.ukim.finki.wp.blossomhouse.model.exceptions.UsernameAlreadyExistsException;
import mk.ukim.finki.wp.blossomhouse.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/register")
public class RegisterController
{
    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getRegisterPage(@RequestParam (required = false) String error, Model model)
    {
        if(error!=null && !error.isEmpty())
        {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        model.addAttribute("bodyContent", "register");
        return "master-template";
    }

    @PostMapping
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String repeatedPassword,
                           @RequestParam String email,
                           @RequestParam String name,
                           @RequestParam String surname)
    {

        try {
            this.userService.register(username, password, repeatedPassword, email, name, surname, Role.ROLE_USER);
            return "redirect:/login";
        }
        catch (InvalidArgumentsException | PasswordsDoNotMatchException | UsernameAlreadyExistsException exception)
        {
            return "redirect:/register?error=" + exception.getMessage();
        }
    }
}