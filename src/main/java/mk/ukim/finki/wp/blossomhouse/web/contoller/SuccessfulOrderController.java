package mk.ukim.finki.wp.blossomhouse.web.contoller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/successful-order")
public class SuccessfulOrderController {

    @GetMapping
    public String getSuccessfulOrderPage(Model model)
    {
        model.addAttribute("bodyContent", "successful-order");
        return "master-template";
    }

}
