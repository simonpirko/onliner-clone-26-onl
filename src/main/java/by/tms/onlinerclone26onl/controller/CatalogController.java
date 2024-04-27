package by.tms.onlinerclone26onl.controller;

import by.tms.onlinerclone26onl.model.Product;
import by.tms.onlinerclone26onl.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/catalog")
public class CatalogController {
    private final ProductService productService;

    @Autowired
    public CatalogController(ProductService productService) {
        this.productService = productService;
    }
    // тут логика каталога

    @GetMapping("/product")
    public String getProduct(@RequestParam("idProduct") long idProduct, Model model) {

        Product productObj = productService.getProductById(idProduct);
        model.addAttribute("productObj", productObj);
        model.addAttribute("product", productObj.getName());
        return "product";
    }


    @GetMapping("/add-product")
    public String addProduct() {
        return "add-product";
    }

    @PostMapping("/add-product")
    public String addProduct(@RequestParam("name") String name,
                             @RequestParam("price") int price,
                             @RequestParam("description") String description,
                             @RequestParam("photo") MultipartFile photo,
                             Model model) {
        try {
            byte[] photoBytes = photo.getBytes();
            Product newProduct = new Product();
            newProduct.setPhoto(photoBytes);
            newProduct.setPrice(price);
            newProduct.setDescription(description);
            newProduct.setName(name);
            productService.add(newProduct);
            model.addAttribute("successMessage", "Product added successfully");
        } catch (IOException e) {
            model.addAttribute("errorMessage", "Error uploading photo");
        }
        return "add-product";
    }

}
