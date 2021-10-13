package mk.ukim.finki.wp.blossomhouse.web.contoller;

import mk.ukim.finki.wp.blossomhouse.model.BlossomUser;
import mk.ukim.finki.wp.blossomhouse.model.Product;
import mk.ukim.finki.wp.blossomhouse.model.exceptions.ProductNotFoundException;
import mk.ukim.finki.wp.blossomhouse.service.ProductService;
import mk.ukim.finki.wp.blossomhouse.service.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/")
public class HomeController
{
    private final ProductService productService;
    private final UserService userService;

    public HomeController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping({ "/","/home"})
    public String getHomePage(Model model,
                              HttpServletRequest request)
    {
        Product flower1 = this.productService.findById(1006L).
                orElseThrow(() -> new ProductNotFoundException(1006L));

        Product flower2 = this.productService.findById(1007L).
                orElseThrow(() -> new ProductNotFoundException(1007L));

        Product flower3 = this.productService.findById(1004L).
                orElseThrow(() -> new ProductNotFoundException(1004L));


        Product plant1 = this.productService.findById(1018L).
                orElseThrow(() -> new ProductNotFoundException(1018L));

        Product plant2 = this.productService.findById(2023L).
                orElseThrow(() -> new ProductNotFoundException(2023L));

        Product plant3 = this.productService.findById(1019L).
                orElseThrow(() -> new ProductNotFoundException(1019L));


        Product gift1 = this.productService.findById(1021L).
                orElseThrow(() -> new ProductNotFoundException(1021L));

        Product gift2 = this.productService.findById(1023L).
                orElseThrow(() -> new ProductNotFoundException(1023L));

        Product gift3 = this.productService.findById(1027L).
                orElseThrow(() -> new ProductNotFoundException(1027L));


        model.addAttribute("flower1", flower1);
        model.addAttribute("flower2", flower2);
        model.addAttribute("flower3", flower3);

        model.addAttribute("plant1", plant1);
        model.addAttribute("plant2", plant2);
        model.addAttribute("plant3", plant3);

        model.addAttribute("gift1", gift1);
        model.addAttribute("gift2", gift2);
        model.addAttribute("gift3", gift3);

        String username = request.getRemoteUser();
        BlossomUser user = new BlossomUser();
        if (username != null && username.length() > 50) {
            user = this.userService.saveGoogleUser(username);
            model.addAttribute("remoteUser", user.getUsername());
        } else if (username!=null){
            user = this.userService.findByUsername(username);
            model.addAttribute("remoteUser", user.getUsername());
        }
        else {
            model.addAttribute("remoteUser", user.getUsername());
        }

        model.addAttribute("header-fragment", "header-fragment");
        model.addAttribute("bodyContent", "home");
        return "master-template";
    }
}
