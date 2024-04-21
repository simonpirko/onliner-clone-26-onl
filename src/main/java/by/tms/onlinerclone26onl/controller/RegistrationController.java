package by.tms.onlinerclone26onl.controller;


import by.tms.onlinerclone26onl.model.User;
import by.tms.onlinerclone26onl.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@SessionAttributes("user")
@RequestMapping("/reg")
public class RegistrationController {

    private final UserService userService;

    @GetMapping()
    public String reg(Model model) {
        model.addAttribute("newUser", new User());
        return "reg";
    }

    @PostMapping
    public String submit(@RequestParam(name = "seller", required = false) boolean seller,
                         @ModelAttribute("newUser") User newUser, Model model) {
        if (seller) {
            newUser.setType("seller");
        } else {
            newUser.setType("buyer");
        }

        if (userService.findByName(newUser.getName()).isPresent()) {
            return "redirect:/login";
        }

        userService.save(newUser);
        User user = userService.findByName(newUser.getName()).get();
        model.addAttribute("user", user);
        return "redirect:/profile";
    }

}
