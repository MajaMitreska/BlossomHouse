package mk.ukim.finki.wp.blossomhouse.web.rest;
import mk.ukim.finki.wp.blossomhouse.model.Product;
import mk.ukim.finki.wp.blossomhouse.service.CategoryService;
import mk.ukim.finki.wp.blossomhouse.service.ProductService;
import mk.ukim.finki.wp.blossomhouse.service.SupplierService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/products")

public class ProductRestController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final SupplierService supplierService;

    public ProductRestController(ProductService productService, CategoryService categoryService, SupplierService supplierService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.supplierService = supplierService;
    }

//    @GetMapping("/flowers")
//    public String getFlowersPage(@RequestParam(required = false) String error, Model model, HttpServletRequest request) {
//        if (error != null && !error.isEmpty()) {
//            model.addAttribute("hasError", true);
//            model.addAttribute("error", error);
//        }
//
//        List<Product> flowers = this.productService.listByCategory(0L);
//        flowers.sort(Comparator.comparing(Product::getName));
//
//        request.getSession().setAttribute("categoryName", "flowers");
//
//        model.addAttribute("categoryName", "Flowers");
//        model.addAttribute("products", flowers);
//        model.addAttribute("bodyContent", "products");
//        return "master-template";
//    }

    @GetMapping("/flowers/pagination")
    public List<Product> findAllWithPagination(Pageable pageable,
                                               Model model,
                                               HttpServletRequest request){
        //request.getSession().setAttribute("categoryName", "flowers");
        //model.addAttribute("categoryName", "Flowers");

        return this.productService.findAllWithPagination(pageable).getContent();
    }

}
