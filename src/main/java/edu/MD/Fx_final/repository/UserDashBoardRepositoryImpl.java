package edu.MD.Fx_final.repository;

import edu.MD.Fx_final.dbConnection.DBConnection;
import edu.MD.Fx_final.model.BookCardDetails;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDashBoardRepositoryImpl implements UserDashBoardRepository{
    Connection connection = DBConnection.getInstance().getConnection();
    @Override
    public ResultSet getAllBookDetails() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT title,author,publisher,published_year,category,available_copies,book_image FROM BookDetails");
        return preparedStatement.executeQuery();
    }
}
