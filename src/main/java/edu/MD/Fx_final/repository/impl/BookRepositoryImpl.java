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
    private static final String INSERT_BOOK =
            "INSERT INTO BookDetails (title, author, publisher, published_year, category, available_copies, book_image) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_BOOK =
            "UPDATE BookDetails SET title = ?, author = ?, publisher = ?, published_year = ?, category = ?, available_copies = ?, book_image = ? WHERE title = ? AND author = ?";

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

    @Override
    public boolean addBook(BookCardDTO bookCardDTO) throws SQLException {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(INSERT_BOOK)) {
            preparedStatement.setString(1, bookCardDTO.getTitle());
            preparedStatement.setString(2, bookCardDTO.getAuthor());
            preparedStatement.setString(3, bookCardDTO.getPublisher());
            preparedStatement.setString(4, bookCardDTO.getPublished_year());
            preparedStatement.setString(5, bookCardDTO.getCategory());
            preparedStatement.setInt(6, bookCardDTO.getAvailable_copies());
            preparedStatement.setString(7, bookCardDTO.getImageLink());
            return preparedStatement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateBook(String originalTitle, String originalAuthor, BookCardDTO updatedBook) throws SQLException {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(UPDATE_BOOK)) {
            preparedStatement.setString(1, updatedBook.getTitle());
            preparedStatement.setString(2, updatedBook.getAuthor());
            preparedStatement.setString(3, updatedBook.getPublisher());
            preparedStatement.setString(4, updatedBook.getPublished_year());
            preparedStatement.setString(5, updatedBook.getCategory());
            preparedStatement.setInt(6, updatedBook.getAvailable_copies());
            preparedStatement.setString(7, updatedBook.getImageLink());
            preparedStatement.setString(8, originalTitle);
            preparedStatement.setString(9, originalAuthor);
            return preparedStatement.executeUpdate() > 0;
        }
    }
}
