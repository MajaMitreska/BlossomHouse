package mk.ukim.finki.wp.blossomhouse.web.contoller;

import mk.ukim.finki.wp.blossomhouse.model.BlossomUser;
import mk.ukim.finki.wp.blossomhouse.model.ShoppingCart;
import mk.ukim.finki.wp.blossomhouse.service.ShoppingCartService;
import mk.ukim.finki.wp.blossomhouse.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping
public class HeaderController {

    private final ShoppingCartService shoppingCartService;
    private final UserService userService;

    public HeaderController(ShoppingCartService shoppingCartService, UserService userService) {
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
    }

    @GetMapping
    public String getHeaderPage(Model model,
                                HttpServletRequest request)
    {

        String username = request.getRemoteUser();
        BlossomUser user = new BlossomUser();
        if (username != null && username.length() > 50) {
            user = this.userService.saveGoogleUser(username);
        } else {
            user = this.userService.findByUsername(username);
        }

        Integer currentNumberOfProductsInShoppingCart = 0;

        if(user.getUsername()!=null && !user.getUsername().isEmpty()) {
            ShoppingCart shoppingCart = this.shoppingCartService.getActiveShoppingCart(user.getUsername());
            currentNumberOfProductsInShoppingCart = shoppingCart.getProducts().size();
        }

        model.addAttribute("numProducts", currentNumberOfProductsInShoppingCart);
        model.addAttribute("header-fragment", "header-fragment");

        return "master-template";
    }

}
