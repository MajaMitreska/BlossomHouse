package mk.ukim.finki.wp.blossomhouse.web.contoller;

import mk.ukim.finki.wp.blossomhouse.model.BlossomUser;
import mk.ukim.finki.wp.blossomhouse.model.exceptions.InvalidArgumentsException;
import mk.ukim.finki.wp.blossomhouse.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/login")
public class LoginController
{
    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getLoginPage(Model model, @RequestParam (required = false) String error)
    {
        if(error!=null && !error.isEmpty())
        {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }

        model.addAttribute("bodyContent", "login");
        return "master-template";
    }

    @PostMapping
    public String login (HttpServletRequest request, Model model)
    {
        BlossomUser user = null;
        try
        {
            user = this.userService.login(request.getParameter("username"), request.getParameter("password"));
            request.getSession().setAttribute("user", user);
            return "redirect:/home";
        }
        catch (InvalidArgumentsException exception)
        {
            model.addAttribute("hasError", true);
            model.addAttribute("error", exception.getMessage());
            return "/login";
        }
    }

}
