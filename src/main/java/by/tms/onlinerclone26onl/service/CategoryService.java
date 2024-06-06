package by.tms.onlinerclone26onl.service;

import by.tms.onlinerclone26onl.dao.CategoryDAO;
import by.tms.onlinerclone26onl.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryDAO categoryDAO;

    public Optional<Category> getCategory(long id) {
        return categoryDAO.findById(id);
    }

    public List<Category> getAllCategories() {
        return categoryDAO.findAll();
    }

    public void saveCategory(Category category) {
        categoryDAO.add(category);
    }

    public Category findCategoryById(Long id) {
        return categoryDAO.findById(id).orElse(null);
    }

}
