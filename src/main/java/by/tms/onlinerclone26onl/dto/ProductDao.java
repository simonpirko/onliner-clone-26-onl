package by.tms.onlinerclone26onl.dto;

import by.tms.onlinerclone26onl.model.Product;
import by.tms.onlinerclone26onl.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class ProductDao {

    private final ProductService productService;
    private final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private final String USER = "postgres";
    private final String PASSWORD = "root";

    public void saveProduct(Product product) {

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {

            connection.setAutoCommit(false);
            PreparedStatement preparedStatement =
                    connection.prepareStatement("INSERT INTO products (title, description, price, address, seller) VALUES (?, ?, ?, ?, ?)");
            preparedStatement.setString(1, product.getTitle());
            preparedStatement.setString(2, product.getDescription());
            preparedStatement.setDouble(3, product.getPrice());
            preparedStatement.setString(4, product.getAddress());
            preparedStatement.setString(5, product.getSeller());
            preparedStatement.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkProductType(Product product) {

        return false;
    }

    public void deleteProduct(Long id) {

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {

            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM products WHERE id = ?");
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            connection.commit();
            preparedStatement.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Product> findById(long id) {

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {

            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM products WHERE id = ?");
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                List<Product> productList = productService.list();
                Product product = new Product();
                product.setId(resultSet.getLong("id"));
                product.setTitle(resultSet.getString("title"));
                product.setDescription(resultSet.getString("description"));
                product.setPrice(resultSet.getDouble("price"));
                product.setAddress(resultSet.getString("address"));
                productList.add(product);
                return Optional.of(product);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();

    }

    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {

            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM products");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                List<Product> productList = productService.list();
                Product product = new Product();
                product.setId(resultSet.getLong("id"));
                product.setTitle(resultSet.getString("title"));
                product.setDescription(resultSet.getString("description"));
                product.setPrice(resultSet.getDouble("price"));
                product.setAddress(resultSet.getString("address"));
                productList.add(product);
            }
            return products;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateProduct(Product product) {

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {

            connection.setAutoCommit(false);
            PreparedStatement preparedStatement =
                    connection.prepareStatement("UPDATE products SET title = ?, description = ?, price = ?, address = ? WHERE id = ?");
            preparedStatement.setString(1, product.getTitle());
            preparedStatement.setString(2, product.getDescription());
            preparedStatement.setDouble(3, product.getPrice());
            preparedStatement.setString(4, product.getAddress());
            preparedStatement.setLong(5, product.getId());
            preparedStatement.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
