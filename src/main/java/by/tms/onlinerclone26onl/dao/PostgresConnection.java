package by.tms.onlinerclone26onl.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresConnection {
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgresql13");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
