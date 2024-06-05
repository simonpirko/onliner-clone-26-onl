package by.tms.onlinerclone26onl.dao;

import by.tms.onlinerclone26onl.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class UserDao {

    private final String DEFAULT_IMAGE = "C:\\Users\\Liisa\\Desktop\\уроки Java\\Homework\\onliner-clone-26-onl\\src\\main\\webapp\\defaultImg\\default_img.jpeg";

    public User saveUser(User user) {

        try (Connection connection =
                     PostgresConnection.getConnection()) {

            File imageFile = new File(DEFAULT_IMAGE);
            byte[] image = Files.readAllBytes(imageFile.toPath());
            long id = 0;

            String password = encodePassword(user.getPassword());

            try (PreparedStatement preparedStatement =
                         connection.prepareStatement("INSERT INTO user_account (name, surname, password, type, avatar) VALUES (?, ?, ?, ?, ?) RETURNING id")) {
                preparedStatement.setString(1, user.getName());
                preparedStatement.setString(2, user.getSurname());
                preparedStatement.setString(3, password);
                preparedStatement.setBoolean(4, user.getType());
                preparedStatement.setBytes(5, image);

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    id = resultSet.getLong("id");
                }
                preparedStatement.execute();

            }

            user.setId(id);
            user.setImage(image);

            return user;

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void delete(long id) {

        try (Connection connection = PostgresConnection.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM user_account WHERE id = ?");
            preparedStatement.setLong(1, id);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Optional<User> findById(long id) {

        try (Connection connection = PostgresConnection.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user_account WHERE id = ?");
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setSurname(resultSet.getString("surname"));
                user.setType(resultSet.getBoolean("type"));
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

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user_account WHERE id = ?");
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

            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE user_account SET name = ? WHERE id = ?");
            preparedStatement.setString(1, name);
            preparedStatement.setLong(2, user.getId());
            preparedStatement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void updateImg(byte[] file, Long id) {

        try (Connection connection = PostgresConnection.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE user_account SET avatar = ? WHERE id = ?");
            preparedStatement.setBytes(1, file);
            preparedStatement.setLong(2, id);
            preparedStatement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Optional<User> findByName(String name) {

        try (Connection connection = PostgresConnection.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user_account WHERE name = ?");
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setSurname(resultSet.getString("surname"));
                user.setType(resultSet.getBoolean("type"));
                user.setImage(resultSet.getBytes("avatar"));
                return Optional.of(user);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();

    }

    public void updatePassword(long id, String password) {

        try (Connection connection = PostgresConnection.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE user_password SET password = ? WHERE id_user = ?");
            preparedStatement.setString(1, password);
            preparedStatement.setLong(2, id);
            preparedStatement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public String encodePassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] encodedHash = digest.digest(password.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

}
