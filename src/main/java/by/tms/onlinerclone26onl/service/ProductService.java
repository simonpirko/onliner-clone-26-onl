package by.tms.onlinerclone26onl.service;

import by.tms.onlinerclone26onl.dto.ProductDAO;
import by.tms.onlinerclone26onl.model.Product;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    ProductDAO productDAO = new ProductDAO();
    public Product getProductById(long id) {

        return new Product();
    }

    public void add(Product product) {
        productDAO.add(product);
    }
}