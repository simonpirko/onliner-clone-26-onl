package by.tms.onlinerclone26onl.controller;


import by.tms.onlinerclone26onl.model.Product;
import by.tms.onlinerclone26onl.model.User;
import by.tms.onlinerclone26onl.service.ProductService;
import by.tms.onlinerclone26onl.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

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
    public String loginPost(@ModelAttribute("user") User user, Model model) {

        Optional<User> userOptional = userService.findByName(user.getName());

        if (userOptional.isPresent()) {
            User newUser = userOptional.get();
            model.addAttribute("user", newUser);

            if (userService.findPasswordById(newUser.getId()).equals(user.getPassword())) {
                return "redirect:/profile";
            }
        }

        return "redirect:/";

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
            user.setType("seller");
        } else {
            user.setType("buyer");
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

    @GetMapping("profile/add-product")
    public String addProduct() {
        return "add-product";
    }

    @PostMapping("profile/add-product")
    public String addProduct(@RequestParam("name") String name,
                             @RequestParam("price") int price,
                             @RequestParam("description") String description,
                             @RequestParam("photo") MultipartFile photo,
                             User user, Model model) {
        try {
            byte[] photoBytes = photo.getBytes();
            Product newProduct = new Product();
            newProduct.setPhoto(photoBytes);
            newProduct.setPrice(price);
            newProduct.setDescription(description);
            newProduct.setName(name);
            productService.add(newProduct, user);
            model.addAttribute("successMessage", "Product added successfully");
        } catch (IOException e) {
            model.addAttribute("errorMessage", "Error uploading photo");
        }
        return "add-product";
    }

}