package by.tms.onlinerclone26onl.controller;

import by.tms.onlinerclone26onl.model.Product;
import by.tms.onlinerclone26onl.model.User;
import by.tms.onlinerclone26onl.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/catalog")
public class CatalogController {

    private final ProductService productService;

    public CatalogController(ProductService productService) {
        this.productService = productService;
    }

    // тут логика каталога

    @GetMapping("/{idProduct}") //TODO доработать ссылку
    public String getProduct(@PathVariable("idProduct") long idProduct, Model model) {
        Product product = productService.getProductById(idProduct);
        List<Double> pricesMinAndMax = productService.searchPriceMinAndMax(idProduct);
        Map<Long, Double> allPrice = productService.searchAllPrice(idProduct);
        model.addAttribute("allPrice", allPrice);
        String photo = Base64.getEncoder().encodeToString(product.getPhoto());
        model.addAttribute("photo", photo);
        model.addAttribute("product", product);
        List<User> productSellers = productService.findProductSellers(idProduct);
        model.addAttribute("productSellers", productSellers);
        model.addAttribute("pricesMinAndMax", pricesMinAndMax);

        return "product";
    }
}
