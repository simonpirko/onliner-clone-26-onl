package by.tms.onlinerclone26onl.dao;

import by.tms.onlinerclone26onl.model.Subcategory;
import by.tms.onlinerclone26onl.service.CategoryService;
import by.tms.onlinerclone26onl.service.SubcategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@Repository
public class SubcategoryDAO {

    private final CategoryService categoryService;
    private final SubcategoryService subcategoryService;

    public void add(Subcategory subcategory) {
        try (Connection connection = PostgresConnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("insert into subcategory (id, name, category_id) values (default, ?, ?) returning id");
            statement.setString(1, subcategory.getName());
            statement.setLong(2, (subcategoryService.findCategoryId(subcategory)));
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Subcategory> findAllSubcategoryByCategoryId(Long categoryId) {
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

        public List<Subcategory> findAll() {
        List<Subcategory> subcategories = new ArrayList<>();
        try (Connection connection = PostgresConnection.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from subcategory");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Subcategory subcategory = new Subcategory();
                subcategory.setId(resultSet.getLong("id"));
                subcategory.setName(resultSet.getString("name"));
                subcategory.setCategory(categoryService.findCategoryById(resultSet.getLong("category_id")));
                subcategories.add(subcategory);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return subcategories;
    }

    public Optional<Subcategory> findById(long id) {
        try (Connection connection = PostgresConnection.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM subcategory WHERE id = ?");
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Subcategory subcategory = new Subcategory();
                subcategory.setId(resultSet.getLong("id"));
                subcategory.setName(resultSet.getString("name"));
                subcategory.setCategory(categoryService.findCategoryById(resultSet.getLong("category_id")));
                return Optional.of(subcategory);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public void delete(long id) {
        try (Connection connection = PostgresConnection.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("delete from subcategory where id = ?");
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Subcategory subcategory) {
        try (Connection connection = PostgresConnection.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("update subcategory set name = ?, category_id = ? where id = ?");
            preparedStatement.setString(1, subcategory.getName());
            preparedStatement.setLong(2, subcategory.getCategory().getId());
            preparedStatement.setLong(3, subcategory.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
