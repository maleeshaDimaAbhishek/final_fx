package edu.MD.Fx_final.dbConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String DB_URL = System.getenv().getOrDefault(
            "BOOK_BORROW_DB_URL",
            "jdbc:mysql://localhost:3306/book_borrowing_fx?useSSL=false&serverTimezone=UTC"
    );
    private static final String DB_USER = System.getenv().getOrDefault("BOOK_BORROW_DB_USER", "root");
    private static final String DB_PASSWORD = System.getenv().getOrDefault("BOOK_BORROW_DB_PASSWORD", "123456789");

    private static volatile DBConnection instance;
    private Connection connection;

    private DBConnection() {
        openConnection();
    }

    public static DBConnection getInstance() {
        if (instance == null) {
            synchronized (DBConnection.class) {
                if (instance == null) {
                    instance = new DBConnection();
                }
            }
        }
        return instance;
    }

    private synchronized void openConnection() {
        try {
            this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to establish database connection.", e);
        }
    }

    public synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            openConnection();
        }
        return connection;
    }
}
