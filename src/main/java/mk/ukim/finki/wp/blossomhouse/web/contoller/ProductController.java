package mk.ukim.finki.wp.blossomhouse.web.contoller;

import mk.ukim.finki.wp.blossomhouse.model.BlossomUser;
import mk.ukim.finki.wp.blossomhouse.model.Category;
import mk.ukim.finki.wp.blossomhouse.model.Product;
import mk.ukim.finki.wp.blossomhouse.model.Supplier;
import mk.ukim.finki.wp.blossomhouse.model.exceptions.ProductNotFoundException;
import mk.ukim.finki.wp.blossomhouse.service.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final SupplierService supplierService;
    private final UserService userService;

    public ProductController(ProductService productService, CategoryService categoryService, SupplierService supplierService, UserService userService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.supplierService = supplierService;
        this.userService = userService;
    }

    @GetMapping("/flowers")
    public String getFlowersPage(@RequestParam(required = false) String error, Model model, HttpServletRequest request) {
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }

        List<Product> flowers = this.productService.listByCategory(0L);
        flowers.sort(Comparator.comparing(Product::getName));

        request.getSession().setAttribute("categoryName", "flowers");

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
        model.addAttribute("categoryName", "Flowers");
        model.addAttribute("products", flowers);
        model.addAttribute("bodyContent", "products");
        return "master-template";
    }

    @GetMapping("/plants")
    public String getPlantsPage(@RequestParam(required = false) String error, Model model, HttpServletRequest request) {
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }

        List<Product> plants = this.productService.listByCategory(1L);
        plants.sort(Comparator.comparing(Product::getName));

        request.getSession().setAttribute("categoryName", "plants");

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
        model.addAttribute("categoryName", "Plants");
        model.addAttribute("products", plants);
        model.addAttribute("bodyContent", "products");

        return "master-template";
    }

    @GetMapping("/gifts")
    public String getGiftsPage(@RequestParam(required = false) String searchString,
                               @RequestParam(required = false) String error,
                               Model model,
                               HttpServletRequest request) {
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        List<Product> gifts;
        if (searchString == null || searchString.isEmpty()) {
            gifts = this.productService.listByCategory(2L);
        } else {
            gifts = this.productService.findAllByNameLike(searchString);
        }
        gifts.sort(Comparator.comparing(Product::getName));

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
        request.getSession().setAttribute("categoryName", "gifts");
        model.addAttribute("categoryName", "Gifts");
        model.addAttribute("products", gifts);
        model.addAttribute("bodyContent", "products");
        return "master-template";
    }

    @PostMapping("/search")
    public String getSearchedProducts(@RequestParam(required = false) String searchString,
                                      Model model) {
        List<Product> searchedProducts;
        if (searchString == null || searchString.isEmpty()) {
            searchedProducts = this.productService.findAll();
        } else {
            searchedProducts = this.productService.findAllByNameLike(searchString);
        }

        model.addAttribute("products", searchedProducts);
        model.addAttribute("bodyContent", "products");
        return "master-template";
    }

    @PostMapping("/{id}/like")
    public String like(@PathVariable Long id) {
        this.productService.like(id);
        Product product = this.productService.findById(id).get();
        String categoryName = product.getCategory().getName().toLowerCase(Locale.ROOT);
        return "redirect:/products/" + categoryName;
    }

    @GetMapping("/add-product")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addProductPage(Model model, HttpServletRequest request) {

        List<Supplier> suppliers = this.supplierService.findAll();
        model.addAttribute("suppliers", suppliers);

        String categoryName = (String) request.getSession().getAttribute("categoryName");
        model.addAttribute("categoryName", request.getSession().getAttribute("categoryName"));
        model.addAttribute("bodyContent", "add-product");
        return "master-template";
    }

    @PostMapping("/add")
    public String saveProduct(
            @RequestParam(required = false) Long id,
            @RequestParam String name,
            @RequestParam Integer price,
            @RequestParam Integer quantity,
            @RequestParam Long supplier,
            @RequestParam String description,
            @RequestParam String details,
            HttpServletRequest request) {

        String imageName = (String) request.getSession().getAttribute("imageName");
        String categoryName = (String) request.getSession().getAttribute("categoryName");

        Long categoryId = this.categoryService.findByName(categoryName).get().getId();

        if (id != null) {
            Product product = this.productService.findById(id).get();
            if (imageName == null){
                imageName = product.getImage();
            }
            this.productService.edit(id, name, price, quantity, categoryId, supplier, imageName, description, details);
        } else {
            this.productService.save(name, price, quantity, categoryId, supplier, imageName, description, details);
        }
        return "redirect:/products/" + categoryName;
    }


    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getEditPage(@PathVariable Long id,
                              Model model,
                              HttpServletRequest request) {
        Product product = this.productService.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        List<Category> categories = this.categoryService.listCategories();
        List<Supplier> suppliers = this.supplierService.findAll();
        model.addAttribute("product", product);
        model.addAttribute("suppliers", suppliers);

        String categoryName = (String) request.getSession().getAttribute("categoryName");
        model.addAttribute("categoryName", request.getSession().getAttribute("categoryName"));
        model.addAttribute("bodyContent", "add-product");
        return "master-template";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteProduct(@PathVariable Long id,
                                HttpServletRequest request) {
        String categoryName = (String) request.getSession().getAttribute("categoryName");
        this.productService.deleteById(id);
        return "redirect:/products/" + categoryName;
    }
}
