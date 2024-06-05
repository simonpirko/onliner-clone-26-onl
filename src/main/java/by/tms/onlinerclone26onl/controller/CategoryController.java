package by.tms.onlinerclone26onl.controller;

import by.tms.onlinerclone26onl.model.Category;
import by.tms.onlinerclone26onl.model.User;
import by.tms.onlinerclone26onl.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public String showCategories(@SessionAttribute("user") User user, Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("user", user);
        return "category";
    }

    @GetMapping("/add")
    public String showAddCategoryPage(@SessionAttribute("user") User user, Model model) {
        if (Boolean.TRUE.equals(user.getType())) {
            model.addAttribute("category", new Category());
            return "add-category";
        } else {
            model.addAttribute("errorMessage", "You do not have permission to access this page.");
            return "redirect:/category";
        }
    }

    @PostMapping("/add")
    public String addCategory(@SessionAttribute("user") User user, @ModelAttribute Category category, Model model) {
        if (Boolean.TRUE.equals(user.getType())) {
            categoryService.saveCategory(category);
            model.addAttribute("successMessage", "Category added successfully.");
            return "redirect:/category";
        } else {
            model.addAttribute("errorMessage", "You do not have permission to perform this action.");
            return "redirect:/category";
        }
    }

}
