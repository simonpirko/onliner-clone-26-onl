package by.tms.onlinerclone26onl.service;

import by.tms.onlinerclone26onl.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private final List<Product> products = new ArrayList<>();
    private long id = 0;

    public List<Product> list() {
        return products;
    }

    // Метод сохранения с автоикрементом id при добавлении товара
    public void saveProduct(Product product){
        product.setId(++id);
        products.add(product);
    }

    // Метод удаления продукта
    public void deleteProduct(Long id){
        products.removeIf(product -> product.getId().equals(id));
    }

    public Product getProductById(Long id) {
        for (Product product : products){
            if (product.getId().equals(id)) return product;
        }
        return null;
    }
}
