package by.tms.onlinerclone26onl.controller;

import by.tms.onlinerclone26onl.model.Product;
import by.tms.onlinerclone26onl.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/catalog")
public class CatalogController {

    private final ProductService productService;

    public CatalogController(ProductService productService) {
        this.productService = productService;
    }

    // тут логика каталога

    @GetMapping("/{product}")
    public String getProduct(@RequestParam("id") long idProduct, Model model, @PathVariable String product) {

        Product productObj = productService.getProductById(idProduct);
        model.addAttribute("productObj", productObj);
        product = productObj.getName();
        return "product";
    }
}
