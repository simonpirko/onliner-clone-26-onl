package by.tms.onlinerclone26onl.service;

import by.tms.onlinerclone26onl.dao.SubcategoryDAO;
import by.tms.onlinerclone26onl.model.Category;
import by.tms.onlinerclone26onl.model.Subcategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubcategoryService {

    @Autowired
    private SubcategoryDAO subcategoryDAO;
    @Autowired
    private CategoryService categoryService;

    public void saveSubcategory(Subcategory subcategory) {
        subcategoryDAO.add(subcategory);
    }

    public List<Subcategory> getAllSubcategories() {
        return subcategoryDAO.findAll();
    }

    public long findCategoryId(Subcategory subcategory) {
        Category category = subcategory.getCategory();
        return category.getId();
    }

    public Subcategory getSubcategoryById(Long id) {
        Subcategory subcategory = subcategoryDAO.findById(id).orElseThrow(() -> new RuntimeException("Subcategory not found"));
        Category category = categoryService.findCategoryById(subcategory.getCategory().getId());
        subcategory.setCategory(category);
        return subcategory;
    }

}

