package by.tms.onlinerclone26onl.controller;


import by.tms.onlinerclone26onl.model.Product;
import by.tms.onlinerclone26onl.model.User;
import by.tms.onlinerclone26onl.service.ProductService;
import by.tms.onlinerclone26onl.service.UserService;
import by.tms.onlinerclone26onl.util.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Controller
@RequiredArgsConstructor
@SessionAttributes("user")
@RequestMapping("/")
public class UserController {

    private final UserService userService;
    private final ProductService productService;


    @GetMapping
    public String index() {
        return "home";
    }


    @GetMapping("/login")
    public String loginGet(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String loginPost(@ModelAttribute("user") User user, Model model, HttpSession session) {

        Optional<User> userOptional = userService.findByName(user.getName());

        if (userOptional.isPresent()) {
            User newUser = userOptional.get();
            model.addAttribute("user", newUser);

            if (userService.findPasswordById(newUser.getId()).equals(userService.encodingPassword(user.getPassword()))) {
                session.setAttribute("user", newUser);
                return "redirect:/profile";
            }
        }
        model.addAttribute("successMessage", "Incorrect name or password");
        return "login";

    }

    @GetMapping("/reg")
    public String regGet(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "reg";
    }

    @PostMapping("/reg")
    public String regPost(@RequestParam(name = "seller", required = false) boolean seller,
                          @ModelAttribute("user") User user, Model model) {
        if (seller) {
            user.setType(true);
        } else {
            user.setType(false);
        }

        if (userService.findByName(user.getName()).isPresent()) {
            return "redirect:/login";
        }
        userService.save(user);

        model.addAttribute("user", user);
        return "redirect:/profile";
    }

    @GetMapping("/profile")
    public String profile(@SessionAttribute("user") User user, Model model) {
        String image = Base64.getEncoder().encodeToString(user.getImage());
        model.addAttribute("image", image);
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/profile/setting")
    public String setting () {
        return "setting/setting";
    }

    @GetMapping("/profile/add-product")
    public String addProduct() {
        return "add-product";
    }

    @PostMapping("/profile/add-product")
    public String addProductPost (@RequestParam("name") String name,
                                  @RequestParam("price") BigDecimal price,
                                  @RequestParam("description") String description,
                                  @RequestParam("photo") MultipartFile photo,
                                  @RequestParam("quantity") long quantity,
                                  Model model,
                                  @SessionAttribute("user") User user){
        try{
            byte[] photoBytes = photo.getBytes();
            Product newProduct = new Product();
            newProduct.setName(name);
            newProduct.setPrice(price);
            newProduct.setDescription(description);
            newProduct.setPhoto(photoBytes);
            productService.add(newProduct, user.getId(), quantity);
            model.addAttribute("successMessage", "Product added");
        } catch (IOException e) {
            model.addAttribute("successMessage", "Error uploading image");
        }
        return "add-product";
    }

    @GetMapping("/profile/my-products")
    public String myProducts(@SessionAttribute("user") User user, Model model) {
        List<Product> products = productService.findProductsBySellerId(user.getId());
        model.addAttribute("products", products);
        return "my-products";
    }

    @PostMapping("/profile/update")
    public String update(@SessionAttribute("user") User user, @RequestParam long productId, @RequestParam long quantity, @RequestParam BigDecimal price, @RequestParam(required = false) Boolean delete) {
        if (delete!= null && delete) {
            productService.delete(user.getId(), productId);
        } else {
            productService.update(productId, user.getId(), quantity, price);
        }
        return "my-products";
    }

}
