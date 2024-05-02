package by.tms.onlinerclone26onl.service;

import by.tms.onlinerclone26onl.dto.ProductDAO;
import by.tms.onlinerclone26onl.dto.UserDao;
import by.tms.onlinerclone26onl.model.Product;
import by.tms.onlinerclone26onl.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    ProductDAO productDAO = new ProductDAO();
    UserDao userDao = new UserDao();

    public Product getProductById(long id) {
        Optional<Product> product = productDAO.findById(id);
        return product.orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public void add(Product product, User user) {
        productDAO.add(product, user.getId());
    }

    public List<User> findProductSellers(Long productID) {
        List<Long> sellersID = productDAO.findProductSellers(productID);
        List<User> users = new ArrayList<>();
        for (Long sellerID : sellersID) {
            User user = userDao.findById(sellerID).orElse(null);
            if (user != null) {
                users.add(user);
            }
        }
        return users;
    }
}
