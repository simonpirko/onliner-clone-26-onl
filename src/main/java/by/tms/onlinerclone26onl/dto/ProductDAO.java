package by.tms.onlinerclone26onl.dto;

import by.tms.onlinerclone26onl.model.Product;
import by.tms.onlinerclone26onl.model.User;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Optional;

@Repository
public class ProductDAO {

    public void add(Product product, Long userID) {
        try (Connection connection = PostgresConnection.getConnection()) {
            connection.setAutoCommit(false);

            PreparedStatement statementCheck = connection.prepareStatement("SELECT id FROM product WHERE name = ?");
            statementCheck.setString(1, product.getName());
            ResultSet resultSet = statementCheck.executeQuery();
            boolean productNameExists = resultSet.next();

            if(!productNameExists) {
                PreparedStatement statement = connection.prepareStatement("INSERT INTO product VALUES (default, ?, ?, ?, ?) RETURNING id");
                statement.setString(1, product.getName());
                statement.setInt(2, product.getPrice());
                statement.setString(3, product.getDescription());
                statement.setBytes(4, product.getPhoto());


                ResultSet generatedKeys = statement.executeQuery();
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong("id");
                    product.setId(id);
                } else {
                    throw new SQLException("Failed to retrieve generated ID.");
                }
            } else {
                long existingProductId = resultSet.getLong("id");
                product.setId(existingProductId);
            }

            PreparedStatement statement2 = connection.prepareStatement("INSERT INTO products_sellers VALUES (?, ?)");
            statement2.setLong(1, product.getId());
            statement2.setLong(2, userID);
            statement2.execute();
            connection.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Product> findById(long id) {
        try (Connection connection = PostgresConnection.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM product WHERE id = ?");
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Product product = new Product();
                product.setId(resultSet.getLong("id"));
                product.setName(resultSet.getString("name"));
                product.setPrice(resultSet.getInt("price"));
                product.setDescription(resultSet.getString("description"));
                product.setPhoto(resultSet.getBytes("photo"));
                return Optional.of(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

}
