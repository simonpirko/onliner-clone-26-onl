package by.tms.onlinerclone26onl.controller;

import by.tms.onlinerclone26onl.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@SessionAttributes("user")
@RequestMapping("/profile")
public class ProfileController {

    @GetMapping()
    public String profile(@SessionAttribute("user") User user, Model model) {
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/setting")
    public String setting () {
        return "setting";
    }

}
