package by.tms.onlinerclone26onl.dto;

import by.tms.onlinerclone26onl.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class UserDao {

    public User save(User user) {

        try (Connection connection = PostgresConnection.getConnection()) {

            connection.setAutoCommit(false);
            PreparedStatement preparedStatement =
                    connection.prepareStatement("INSERT INTO user_account VALUES (DEFAULT, ?, ?, ?)");
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getSurname());
            preparedStatement.setString(3, user.getType());
            preparedStatement.execute();
            connection.commit();

            PreparedStatement preparedStatementPassword = connection.prepareStatement("INSERT INTO user_password VALUES (DEFAULT, ?, ?)");
            preparedStatementPassword.setString(1, user.getPassword());
            Optional<User> newUserSeller = findByName(user.getName());
            preparedStatementPassword.setLong(2, newUserSeller.get().getId());
            preparedStatementPassword.execute();

            connection.commit();
            preparedStatement.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return user;
    }

    public boolean checkUserType(User user) {

        return false;

    }

    public void delete(long id) {

        try (Connection connection = PostgresConnection.getConnection()) {

            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM user_account WHERE id = ?");
            preparedStatement.setLong(1, id);
            connection.commit();
            preparedStatement.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Optional<User> findById(long id) {

        try (Connection connection = PostgresConnection.getConnection()) {

            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user_account WHERE id = ?");
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setSurname(resultSet.getString("surname"));
                user.setType(resultSet.getString("type"));
                return Optional.of(user);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();

    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();

        try (Connection connection = PostgresConnection.getConnection()) {

            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user_account");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setSurname(resultSet.getString("surname"));
                users.add(user);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return users;

    }

    public String findPasswordById(long id) {

        try (Connection connection = PostgresConnection.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user_password WHERE id_user = ?");
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getString("password");
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void updateName(User user, String name) {

        try (Connection connection = PostgresConnection.getConnection()) {

            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE user_account SET name = ? WHERE id = ?");
            preparedStatement.setString(1, name);
            preparedStatement.setLong(2, user.getId());
            preparedStatement.execute();
            connection.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void updateImg(byte[] file, Long id) {

        try (Connection connection = PostgresConnection.getConnection()) {

            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE user_account SET avatar = ? WHERE id = ?");
            preparedStatement.setBytes(1, file);
            preparedStatement.setLong(2, id);
            preparedStatement.execute();
            connection.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Optional<User> findByName(String name) {

        try (Connection connection = PostgresConnection.getConnection()) {

            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user_account WHERE name = ?");
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setSurname(resultSet.getString("surname"));
                user.setType(resultSet.getString("type"));
                return Optional.of(user);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();

    }

    public void updatePassword(long id, String password) {

        try (Connection connection = PostgresConnection.getConnection()) {

            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE user_password SET password = ? WHERE id_user = ?");
            preparedStatement.setString(1, password);
            preparedStatement.setLong(2, id);
            preparedStatement.execute();
            connection.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
