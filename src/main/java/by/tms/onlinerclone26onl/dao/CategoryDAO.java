package by.tms.onlinerclone26onl.dao;

import by.tms.onlinerclone26onl.model.Category;
import by.tms.onlinerclone26onl.model.Subcategory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CategoryDAO {

    public void add(Category category) {
        try (Connection connection = PostgresConnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("insert into category (id, name) values (default, ?)");
            statement.setString(1, category.getName());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Category> findById(long id) {
        try (Connection connection = PostgresConnection.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from category where id = ?");
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Category category = new Category();
                category.setId(resultSet.getLong("id"));
                category.setName(resultSet.getString("name"));
                return Optional.of(category);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public List<Category> findAll() {
        List<Category> categories = new ArrayList<>();
        try (Connection connection = PostgresConnection.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from category");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Category category = new Category();
                category.setId(resultSet.getLong("id"));
                category.setName(resultSet.getString("name"));
                category.setSubcategories(getSubcategoriesByCategoryId(category.getId()));
                categories.add(category);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categories;
    }

    private List<Subcategory> getSubcategoriesByCategoryId(Long categoryId) {
        List<Subcategory> subcategories = new ArrayList<>();
        try (Connection connection = PostgresConnection.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from subcategory where category_id = ?");
            preparedStatement.setLong(1, categoryId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Subcategory subcategory = new Subcategory();
                subcategory.setId(resultSet.getLong("id"));
                subcategory.setName(resultSet.getString("name"));
                subcategories.add(subcategory);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return subcategories;
    }

        public void delete ( long id){
            try (Connection connection = PostgresConnection.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement("delete from category where id = ?");
                preparedStatement.setLong(1, id);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        public void update (Category category){
            try (Connection connection = PostgresConnection.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement("update category set name = ? where id = ?");
                preparedStatement.setString(1, category.getName());
                preparedStatement.setLong(2, category.getId());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
