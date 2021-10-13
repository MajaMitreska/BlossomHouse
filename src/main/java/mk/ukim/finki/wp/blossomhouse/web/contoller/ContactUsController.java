package mk.ukim.finki.wp.blossomhouse.web.contoller;


import mk.ukim.finki.wp.blossomhouse.model.BlossomUser;
import mk.ukim.finki.wp.blossomhouse.model.exceptions.InvalidArgumentsException;
import mk.ukim.finki.wp.blossomhouse.service.EmailService;
import mk.ukim.finki.wp.blossomhouse.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/contact")
public class ContactUsController {

    private final UserService userService;
    private final EmailService emailService;

    public ContactUsController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @GetMapping
    public String getContactUsPage(Model model, HttpServletRequest request)
    {
        String username = request.getRemoteUser();
        BlossomUser user = new BlossomUser();
        if (username != null && username.length() > 50) {
            user = this.userService.saveGoogleUser(username);
        } else {
            user = this.userService.findByUsername(username);
        }
        model.addAttribute("remoteUser", user.getUsername());
        model.addAttribute("bodyContent", "contact");
        return "master-template";
    }

    @PostMapping
    public String saveContactUsForm(@RequestParam String firstName,
                                    @RequestParam String lastName,
                                    @RequestParam String number,
                                    @RequestParam String email,
                                    @RequestParam String reason,
                                    HttpServletRequest request,
                                    Model model) {

        String username = request.getRemoteUser();
        BlossomUser user = new BlossomUser();
        if (username.length() > 50) {
            user = this.userService.findGoogleUserByUsername(username);
        } else {
            user = this.userService.findByUsername(username);
        }

        try {
            String personalizedMessage =
                            "First name: " + firstName + "\n"+
                            "Last name: " + lastName + "\n"+
                            "Number: " + number + "\n"+
                                    "E-mail: " + email + "\n"+
                                    "Reason: " + reason + "\n";


            this.emailService.sendSimpleMessage("noreply.blossomhouse@gmail.com",
                    "User "+firstName+" "+lastName+" has a problem!",
                    personalizedMessage);
            return "redirect:/home";
        }
        catch (InvalidArgumentsException exception)
        {
            return "redirect:/contact?error=" + exception.getMessage();
        }
    }
}