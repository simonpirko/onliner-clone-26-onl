package by.tms.onlinerclone26onl.dto;

import by.tms.onlinerclone26onl.model.Product;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class ProductDAO {

    public void add(Product product) {
        try (Connection connection = PostgresConnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO product VALUES (default, ?, ?, ?, ?)");
            statement.setString(1, product.getName());
            statement.setInt(2, product.getPrice());
            statement.setString(3, product.getDescription());
            statement.setBytes(4, product.getPhoto());
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
