package by.tms.onlinerclone26onl.controller;

import by.tms.onlinerclone26onl.model.User;
import by.tms.onlinerclone26onl.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@SessionAttributes("user")
@RequestMapping("/login")
public class LoginController {

    private final UserService userService;

    @GetMapping
    public String login(Model model) {
        model.addAttribute("newUser", new User());
        return "login";
    }

    @PostMapping
    public String login(@ModelAttribute("newUser") User newUser, Model model) {

        Optional<User> userOptional = userService.findByName(newUser.getName());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            model.addAttribute("user", user);

            if (userService.findPasswordById(user.getId()).equals(newUser.getPassword())) {
                return "redirect:/profile";
            }
        }

        return "redirect:/";

    }

}
