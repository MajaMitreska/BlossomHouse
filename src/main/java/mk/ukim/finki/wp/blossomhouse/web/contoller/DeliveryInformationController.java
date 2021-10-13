package mk.ukim.finki.wp.blossomhouse.web.contoller;

import mk.ukim.finki.wp.blossomhouse.model.BlossomUser;
import mk.ukim.finki.wp.blossomhouse.model.Product;
import mk.ukim.finki.wp.blossomhouse.model.ShoppingCart;
import mk.ukim.finki.wp.blossomhouse.service.OrderService;
import mk.ukim.finki.wp.blossomhouse.service.ProductInShoppingCartService;
import mk.ukim.finki.wp.blossomhouse.service.ShoppingCartService;
import mk.ukim.finki.wp.blossomhouse.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/delivery-information")
public class DeliveryInformationController
{

    private final ShoppingCartService shoppingCartService;
    private final ProductInShoppingCartService productInShoppingCartService;
    private final OrderService orderService;
    private final UserService userService;

    public DeliveryInformationController(ShoppingCartService shoppingCartService, ProductInShoppingCartService productInShoppingCartService, OrderService orderService, UserService userService) {
        this.shoppingCartService = shoppingCartService;
        this.productInShoppingCartService = productInShoppingCartService;
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping
    public String getDeliveryInformationPage(Model model, HttpServletRequest request)
    {
        String remoteUser = request.getRemoteUser();
        BlossomUser user = new BlossomUser();
        if (remoteUser.length() > 50) {
            user = this.userService.findGoogleUserByUsername(remoteUser);
        } else {
            user = this.userService.findByUsername(remoteUser);
        }
        ShoppingCart shoppingCart = this.shoppingCartService.getActiveShoppingCart(user.getUsername());
        Integer totalAmount = shoppingCart.getTotalAmount();
        List<Product> productList = productInShoppingCartService.getProductsInShoppingCart(shoppingCart.getId());


        model.addAttribute("remoteUser", user.getUsername());
        model.addAttribute("products", productList);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("numProducts", productList.size());
        model.addAttribute("bodyContent", "delivery-information");

        return "master-template";
    }

    @PostMapping()
    public String saveDeliveryInformation(@RequestParam String name,
                                          @RequestParam String address,
                                          @RequestParam String phoneNumber,
                                          @RequestParam String email,
                                          HttpServletRequest request,
                                          Model model)
    {

        String remoteUser = request.getRemoteUser();
        BlossomUser user = new BlossomUser();
        if (remoteUser.length() > 50) {
            user = this.userService.findGoogleUserByUsername(remoteUser);
        } else {
            user = this.userService.findByUsername(remoteUser);
        }
        ShoppingCart shoppingCart = this.shoppingCartService.getActiveShoppingCart(user.getUsername());
        List<Product> productList = shoppingCart.getProducts();
        model.addAttribute("products", productList);
        BlossomUser blossomUser = this.userService.findByUsername(user.getUsername());

        this.orderService.saveProductsFromShoppingCartToOrder(blossomUser, name, address, phoneNumber, email);

        return "redirect:/payment-information";
    }
}
