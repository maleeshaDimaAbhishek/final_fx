package edu.MD.Fx_final.repository;

import edu.MD.Fx_final.dbConnection.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserLoginRepositoryImpl implements UserLoginRepository{
    Connection connection = DBConnection.getInstance().getConnection();

    public ResultSet getUserRole(String username) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT email,role_id,name FROM user_details WHERE NIC = ?;");
        preparedStatement.setString(1, username);
        return preparedStatement.executeQuery();
    }
}
