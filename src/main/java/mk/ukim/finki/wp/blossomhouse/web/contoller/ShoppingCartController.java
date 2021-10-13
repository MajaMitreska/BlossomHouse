package mk.ukim.finki.wp.blossomhouse.web.contoller;

import mk.ukim.finki.wp.blossomhouse.model.*;
import mk.ukim.finki.wp.blossomhouse.repository.BlossomUserRepository;
import mk.ukim.finki.wp.blossomhouse.service.ProductInShoppingCartService;
import mk.ukim.finki.wp.blossomhouse.service.ShoppingCartService;
import mk.ukim.finki.wp.blossomhouse.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Controller
@RequestMapping("/shopping-cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final UserService userService;
    private final ProductInShoppingCartService productInShoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService, UserService userService, ProductInShoppingCartService productInShoppingCartService) {
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
        this.productInShoppingCartService = productInShoppingCartService;
    }

    @GetMapping
    public String getShoppingCartPage(@RequestParam(required = false) String error,
                                      HttpServletRequest request,
                                      Model model) {
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }

        String username = request.getRemoteUser();
        BlossomUser user = new BlossomUser();
        if (username.length() > 40) {
            user = this.userService.saveGoogleUser(username);
        } else {
            user = this.userService.findByUsername(username);
        }
        ShoppingCart shoppingCart = this.shoppingCartService.getActiveShoppingCart(user.getUsername());
        List<Product> productsInShoppingCart = this.productInShoppingCartService.getProductsInShoppingCart(shoppingCart.getId());
        Integer totalAmount = shoppingCart.getTotalAmount();
        Integer currentNumberOfProductsInShoppingCart = productsInShoppingCart.size();


        List<Integer> quantities = new ArrayList<>();
        for (Product p : productsInShoppingCart) {
            ProductInShoppingCart record = this.productInShoppingCartService.findRecord(shoppingCart.getId(), p.getId());
            quantities.add(record.getQuantity());
        }


        List<ProductQuantity> productsAndQuantities = new ArrayList<>();

        for (int i = 0; i < productsInShoppingCart.size(); i++) {
            ProductQuantity pair = new ProductQuantity(productsInShoppingCart.get(i), quantities.get(i));
            productsAndQuantities.add(pair);
        }


        model.addAttribute("remoteUser", user.getUsername());
        model.addAttribute("productsAndQuantities", productsAndQuantities);
        model.addAttribute("numProducts", currentNumberOfProductsInShoppingCart);
        model.addAttribute("products", this.shoppingCartService.listAllProductsInShoppingCart(shoppingCart.getId()));
        model.addAttribute("userName", user.getUsername());
        model.addAttribute("totalAmount", totalAmount);

        model.addAttribute("bodyContent", "shopping-cart");

        return "master-template";
    }

    @PostMapping("/add-product/{id}")
    public String addProductToShoppingCart(@PathVariable Long id,
                                           HttpServletRequest request,
                                           Authentication authentication) {
        try {
            String categoryName = request.getSession().getAttribute("categoryName").toString().toLowerCase();
            String username = request.getRemoteUser();
            BlossomUser user = new BlossomUser();
            if (username.length() > 50) {
                user = this.userService.saveGoogleUser(username);
            } else {
                user = this.userService.findByUsername(username);
            }
            //BlossomUser user1 = (BlossomUser) authentication.getPrincipal();

            this.shoppingCartService.addProductToShoppingCart(user.getUsername(), id);

            return "redirect:/products/"+categoryName;

        } catch (RuntimeException exception) {
            return "redirect:/shopping-cart?error=" + exception.getMessage();
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteProductFromShoppingCart(@PathVariable Long id,
                                                HttpServletRequest request,
                                                Model model) {

        String remoteUser = request.getRemoteUser();
        BlossomUser user = new BlossomUser();
        if (remoteUser.length() > 50) {
            user = this.userService.saveGoogleUser(remoteUser);
        } else {
            user = this.userService.findByUsername(remoteUser);
        }
        model.addAttribute("totalAmount", this.shoppingCartService.getActiveShoppingCart(user.getUsername()).getTotalAmount());
        this.shoppingCartService.removeProductFromShoppingCart(user.getUsername(), id);
        return "redirect:/shopping-cart";
    }


    @PostMapping("increaseQuantity/{id}")
    public String increaseQuantity(@PathVariable Long id, //this id is productId
                                   Model model,
                                   HttpServletRequest request) {
        String remoteUser = request.getRemoteUser();
        BlossomUser user = new BlossomUser();
        if (remoteUser.length() > 50) {
            user = this.userService.saveGoogleUser(remoteUser);
        } else {
            user = this.userService.findByUsername(remoteUser);
        }
        ShoppingCart shoppingCart = this.shoppingCartService.getActiveShoppingCart(user.getUsername());
        ProductInShoppingCart record = this.productInShoppingCartService.findRecord(shoppingCart.getId(), id);
        this.productInShoppingCartService.increaseQuantity(record.getShoppingCart().getId(), record.getProduct().getId());
        this.shoppingCartService.computeTotalProductsSum(shoppingCart);
        return "redirect:/shopping-cart";
    }

    @PostMapping("decreaseQuantity/{id}")
    public String decreaseQuantity(@PathVariable Long id, //this id is productId
                                   Model model,
                                   HttpServletRequest request) {
        String remoteUser = request.getRemoteUser();
        BlossomUser user = new BlossomUser();
        if (remoteUser.length() > 50) {
            user = this.userService.saveGoogleUser(remoteUser);
        } else {
            user = this.userService.findByUsername(remoteUser);
        }
        ShoppingCart shoppingCart = this.shoppingCartService.getActiveShoppingCart(user.getUsername());
        ProductInShoppingCart record = this.productInShoppingCartService.findRecord(shoppingCart.getId(), id);
        this.productInShoppingCartService.decreaseQuantity(record.getShoppingCart().getId(), record.getProduct().getId());
        this.shoppingCartService.computeTotalProductsSum(shoppingCart);
        return "redirect:/shopping-cart";
    }


}

