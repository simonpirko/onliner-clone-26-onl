package by.tms.onlinerclone26onl.controller;

import by.tms.onlinerclone26onl.model.Category;
import by.tms.onlinerclone26onl.model.Product;
import by.tms.onlinerclone26onl.model.Subcategory;
import by.tms.onlinerclone26onl.model.User;
import by.tms.onlinerclone26onl.service.CategoryService;
import by.tms.onlinerclone26onl.service.ProductService;
import by.tms.onlinerclone26onl.service.SubcategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/subcategory")
@RequiredArgsConstructor
public class SubcategoryController {

    private final SubcategoryService subcategoryService;
    private final CategoryService categoryService;
    private final ProductService productService;

    @GetMapping("/{subcategoryId}")
    public String showSubcategoryProducts(@PathVariable Long subcategoryId, Model model) {
        Subcategory subcategory = subcategoryService.getSubcategoryById(subcategoryId);
        List<Product> products = productService.getProductsBySubcategoryId(subcategoryId);
        model.addAttribute("subcategory", subcategory);
        model.addAttribute("products", products);
        return "subcategory";
    }

    @GetMapping("/add")
    public String showAddSubcategoryPage(@SessionAttribute("user") User user, Model model) {
        if (Boolean.TRUE.equals(user.getType())) {
            model.addAttribute("subcategory", new Subcategory());
            model.addAttribute("categories", categoryService.getAllCategories());  // Получаем все категории
            return "add-subcategory";
        } else {
            model.addAttribute("errorMessage", "You do not have permission to access this page.");
            return "redirect:/category";
        }
    }

    @PostMapping("/add")
    public String addSubcategory(@SessionAttribute("user") User user, @RequestParam String name, @RequestParam Long categoryId, RedirectAttributes redirectAttributes) {
        if (Boolean.TRUE.equals(user.getType())) {
            Optional<Category> categoryOptional = categoryService.getCategory(categoryId);
            if (categoryOptional.isPresent()) {
                Subcategory subcategory = new Subcategory();
                subcategory.setName(name);
                subcategory.setCategory(categoryOptional.get());
                subcategoryService.saveSubcategory(subcategory);
                redirectAttributes.addFlashAttribute("successMessage", "Subcategory added successfully.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Category not found.");
            }
            return "redirect:/category";
        }
        return "redirect:/category";
    }

}
