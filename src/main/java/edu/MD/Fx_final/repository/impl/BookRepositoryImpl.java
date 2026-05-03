package edu.MD.Fx_final.repository.impl;

import edu.MD.Fx_final.dbConnection.DBConnection;
import edu.MD.Fx_final.model.dto.BookCardDTO;
import edu.MD.Fx_final.repository.BookRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookRepositoryImpl implements BookRepository {

    private static final String SELECT_ALL_BOOKS =
            "SELECT title, author, publisher, published_year, category, available_copies, book_image FROM BookDetails";

    private Connection getConnection() throws SQLException {
        return DBConnection.getInstance().getConnection();
    }

    @Override
    public List<BookCardDTO> getAllBookDetails() throws SQLException {
        List<BookCardDTO> books = new ArrayList<>();
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(SELECT_ALL_BOOKS);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                books.add(new BookCardDTO(
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("publisher"),
                        resultSet.getString("published_year"),
                        resultSet.getString("category"),
                        resultSet.getInt("available_copies"),
                        resultSet.getString("book_image")
                ));
            }
        }
        return books;
    }
}
