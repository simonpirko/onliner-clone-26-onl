package by.tms.onlinerclone26onl.controller;


import by.tms.onlinerclone26onl.exception.PasswordException;
import by.tms.onlinerclone26onl.model.User;
import by.tms.onlinerclone26onl.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/setting")
public class SettingController {

    private final UserService userService;

    @GetMapping("/name")
    public String name(Model model) {
        return "setting/name";
    }

    @GetMapping("/avatar")
    public String avatar(Model model) {
        return "setting/avatar";
    }

    @GetMapping("/password")
    public String password(Model model) {
        return "setting/password";
    }

    @PostMapping("/avatar")
    public String avatarPost(Model model, @RequestParam("file") MultipartFile avatar) {
        Optional<User> optionalUser = (Optional<User>) model.getAttribute("user");

        if (optionalUser != null && optionalUser.isPresent()) {
            User user = optionalUser.get();
            try {

                userService.updateImg(avatar.getBytes(),
                        user.getId());
                user.setImage(avatar);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            model.addAttribute("user", user);

        }

        return "redirect:/profile";
    }

    @PostMapping("/name")
    public String namePost(@RequestParam String name,
                           @SessionAttribute("user") User user,
                           RedirectAttributes redirectAttributes,
                           HttpSession session) {

        userService.updateName(user, name);

        User newUser = userService.findById(user.getId()).orElse(null);

        session.setAttribute("user", newUser);

        redirectAttributes.addFlashAttribute("user", newUser);

        return "redirect:/profile";
    }

    @PostMapping("/password")
    public String passwordPost(@SessionAttribute("user") User user,
                               @RequestParam("newPassword") String newPassword,
                               @RequestParam("oldPassword") String oldPassword) throws PasswordException {

        if (newPassword.equals(userService.findPasswordById(user.getId()))) {
            throw new PasswordException("The new password must not be the same as the old one");
        } else if (oldPassword.equals(newPassword)) {
            throw new PasswordException("This is not your password");
        } else {
            userService.updatePassword(user.getId(), newPassword);
        }

        return "redirect:/profile";

    }



}
