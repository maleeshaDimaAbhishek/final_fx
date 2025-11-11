package edu.MD.Fx_final.dbConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection instance;
    private Connection connection;

    private DBConnection() {
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/book_borrowing_fx", "root", "123456789");
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        }

    }

    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }

        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}

