package mk.ukim.finki.wp.blossomhouse.web.contoller;

import mk.ukim.finki.wp.blossomhouse.model.BlossomUser;
import mk.ukim.finki.wp.blossomhouse.model.Product;
import mk.ukim.finki.wp.blossomhouse.model.exceptions.ProductNotFoundException;
import mk.ukim.finki.wp.blossomhouse.service.ProductService;
import mk.ukim.finki.wp.blossomhouse.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/products/{id}/details")
public class ProductDetailsController {

    private final ProductService productService;
    private final UserService userService;

    public ProductDetailsController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping
    public String getProductDetailsPage(@PathVariable Long id, Model model, HttpServletRequest request)
    {
        Product product = this.productService.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        String username = request.getRemoteUser();
        BlossomUser user = new BlossomUser();
        if (username != null && username.length() > 50) {
            user = this.userService.saveGoogleUser(username);
        } else {
            user = this.userService.findByUsername(username);
        }
        model.addAttribute("remoteUser", user.getUsername());
        model.addAttribute("bodyContent", "product-details");
        model.addAttribute("product", product);
        return "master-template";
    }
}
