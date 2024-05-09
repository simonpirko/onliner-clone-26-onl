package by.tms.onlinerclone26onl.service;

import by.tms.onlinerclone26onl.dto.ProductDAO;
import by.tms.onlinerclone26onl.dto.UserDao;
import by.tms.onlinerclone26onl.model.Product;
import by.tms.onlinerclone26onl.model.User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductService {

    ProductDAO productDAO = new ProductDAO();
    UserDao userDao = new UserDao();

    public Product getProductById(long id) {
        Optional<Product> product = productDAO.findById(id);
        return product.orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public void add(Product product, long userID, long quantity) {
        productDAO.add(product, userID, quantity);
    }

    public List<User> findProductSellers(long productID) {
        List<Long> sellersID = productDAO.findProductSellers(productID);
        List<User> users = new ArrayList<>();
        for (long sellerID : sellersID) {
            User user = userDao.findById(sellerID).orElse(null);
            if (user != null) {
                users.add(user);
            }
        }
        return users;
    }

    public void delete(long productID, long userID) {
        productDAO.delete(productID, userID);
    }

    public void buy(long productID, long userID, long quantity) {
        productDAO.buy(productID, userID, quantity);
    }

    public List<Product> findProductsBySellerId(long userID) {
        return productDAO.findProductsBySellerId(userID);
    }

    public void update(long productId, long userID, long quantity, double price) {
        productDAO.update(productId, userID, quantity, price);
    }

    public List<Double> searchPriceMinAndMax (long productID) {
        List<Double> PriceMinAndMax = new ArrayList<>();
        Map<Long, Double> allPrice = searchAllPrice(productID);
        if (!allPrice.isEmpty()) {
            PriceMinAndMax.add(Collections.min(allPrice.values()));
            PriceMinAndMax.add(Collections.max(allPrice.values()));
        }
        return PriceMinAndMax;
    }

    public Map<Long, Double> searchAllPrice (long productID) {
        return productDAO.searchAllPrice(productID);
    }

}
