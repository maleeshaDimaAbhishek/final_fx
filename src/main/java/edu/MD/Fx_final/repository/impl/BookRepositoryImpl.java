package edu.MD.Fx_final.repository.impl;

import edu.MD.Fx_final.dbConnection.DBConnection;
import edu.MD.Fx_final.repository.BookRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookRepositoryImpl implements BookRepository {
    Connection connection = DBConnection.getInstance().getConnection();
    @Override
    public ResultSet getAllBookDetails() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT title,author,publisher,published_year,category,available_copies,book_image FROM BookDetails");
        return preparedStatement.executeQuery();
    }
}
