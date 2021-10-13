package mk.ukim.finki.wp.blossomhouse.web.contoller;

import mk.ukim.finki.wp.blossomhouse.model.BlossomUser;
import mk.ukim.finki.wp.blossomhouse.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/about")
public class AboutUsController
{
    private final UserService userService;

    public AboutUsController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getAboutUsPage(Model model, HttpServletRequest request)
    {
        String username = request.getRemoteUser();
        BlossomUser user = new BlossomUser();
        if (username != null && username.length() > 50) {
            user = this.userService.saveGoogleUser(username);
        } else {
            user = this.userService.findByUsername(username);
        }
        model.addAttribute("remoteUser", user.getUsername());
        model.addAttribute("bodyContent", "about");
        return "master-template";
    }
}