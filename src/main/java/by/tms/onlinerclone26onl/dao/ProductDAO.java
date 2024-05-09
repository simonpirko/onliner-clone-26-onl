package by.tms.onlinerclone26onl.dao;

import by.tms.onlinerclone26onl.model.Product;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ProductDAO {

    public void add(Product product, long userID, long quantity) {
        try (Connection connection = PostgresConnection.getConnection()) {
            connection.setAutoCommit(false);

            PreparedStatement statementCheck = connection.prepareStatement("SELECT id FROM product WHERE name = ?");
            statementCheck.setString(1, product.getName());
            ResultSet resultSet = statementCheck.executeQuery();
            boolean productNameExists = resultSet.next();

            if(!productNameExists) {
                PreparedStatement statement = connection.prepareStatement("INSERT INTO product VALUES (default, ?, ?, ?) RETURNING id");
                statement.setString(1, product.getName());
                statement.setString(2, product.getDescription());
                statement.setBytes(3, product.getPhoto());


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

            PreparedStatement statement2 = connection.prepareStatement("INSERT INTO products_sellers VALUES (?, ?, ?, ?)");
            statement2.setLong(1, product.getId());
            statement2.setLong(2, userID);
            statement2.setLong(3, quantity);
            statement2.setBigDecimal(4, product.getPrice());
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
                product.setDescription(resultSet.getString("description"));
                product.setPhoto(resultSet.getBytes("photo"));
                return Optional.of(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public List<Long> findProductSellers(long productID) {
        try (Connection connection = PostgresConnection.getConnection()) {
            List<Long> sellersId = new ArrayList<>();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id_seller FROM products_sellers WHERE id_product = ?");
            preparedStatement.setLong(1, productID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Long Id = resultSet.getLong("id_seller");
                sellersId.add(Id);
            }
            return sellersId;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(long productID, long userID) {
        try(Connection connection = PostgresConnection.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM products_sellers WHERE id_product = ? AND id_seller = ?");
            preparedStatement.setLong(1, productID);
            preparedStatement.setLong(2, userID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void buy(long productID, long userID, long quantity) {
        try(Connection connection = PostgresConnection.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE products_sellers SET quantity = quantity -? WHERE id_product =? AND id_seller =?");
            preparedStatement.setLong(1, quantity);
            preparedStatement.setLong(2, productID);
            preparedStatement.setLong(3, userID);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Product> findProductsBySellerId(long userID) {
        List<Product> products = new ArrayList<>();

        try(Connection connection = PostgresConnection.getConnection()) {
            // Шаг 1. Получить идентификаторы и количество продуктов, связанных с продавцом
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM products_sellers WHERE id_seller =?");
            preparedStatement.setLong(1, userID);

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Long> productIDs = new ArrayList<>();
            Map<Long, Long> productQuantities = new HashMap<>();
            Map<Long, BigDecimal> productPrices = new HashMap<>();
            while (resultSet.next()) {
                long productID = resultSet.getLong("id_product");
                long quantity = resultSet.getLong("quantity");
                BigDecimal price = resultSet.getBigDecimal("price");
                productIDs.add(productID);
                productQuantities.put(productID, quantity);
                productPrices.put(productID, price);
            }

            // Шаг 2. Получение сведений о продукте из таблицы продуктов.
            preparedStatement = connection.prepareStatement("SELECT p.*, ps.price FROM product p INNER JOIN products_sellers ps " +
                    "ON p.id = ps.id_product WHERE p.id IN " +
                    "(" + String.join(", ", productIDs.stream().map(String::valueOf).collect(Collectors.toList())) + ")");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Product product = new Product();
                product.setId(resultSet.getLong("id"));
                product.setName(resultSet.getString("name"));
                product.setDescription(resultSet.getString("description"));
                product.setPhoto(resultSet.getBytes("photo"));
                product.setQuantity(productQuantities.get(product.getId()));
                product.setPrice(productPrices.get(product.getId()));

                products.add(product);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return products;
    }



    public void update(long productId, long sellerID, long quantity, BigDecimal price) {
        try(Connection connection = PostgresConnection.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE products_sellers SET quantity = quantity + ? AND price = ? WHERE id_product = ? AND id_seller = ?");
            preparedStatement.setLong(1, quantity);
            preparedStatement.setBigDecimal(2, price);
            preparedStatement.setLong(3, productId);
            preparedStatement.setLong(4, sellerID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<Long, BigDecimal> searchAllPrice(long productID) {
        Map<Long, BigDecimal> prices = new HashMap<>();
        try(Connection connection = PostgresConnection.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT price, id_seller FROM products_sellers WHERE id_product = ?");
            preparedStatement.setLong(1, productID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                prices.put(resultSet.getLong("id_seller"), resultSet.getBigDecimal("price"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return prices;
    }
}
