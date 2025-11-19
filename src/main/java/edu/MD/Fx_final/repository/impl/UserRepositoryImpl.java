package edu.MD.Fx_final.repository.impl;

import edu.MD.Fx_final.dbConnection.DBConnection;
import edu.MD.Fx_final.model.dto.UserRegistrationDTO;
import edu.MD.Fx_final.repository.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepositoryImpl implements UserRepository {
    Connection connection = DBConnection.getInstance().getConnection();
    @Override
    public boolean registerUser(UserRegistrationDTO userRegistrationDetails) throws SQLException {
        PreparedStatement preparedStatement=connection.prepareStatement("INSERT INTO user_details (NIC, name, dob, email, phone_number,address) VALUES (?, ?, ?, ?, ?,?);");
        preparedStatement.setObject(1,userRegistrationDetails.getNIC());
        preparedStatement.setObject(2,userRegistrationDetails.getName());
        preparedStatement.setObject(3,userRegistrationDetails.getDob());
        preparedStatement.setObject(4,userRegistrationDetails.getMail());
        preparedStatement.setObject(5,userRegistrationDetails.getPhoneNumber());
        preparedStatement.setObject(6,userRegistrationDetails.getAddress());
        return preparedStatement.executeUpdate()>0;
    }
    @Override
    public ResultSet getUserRole(String username) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT email,role_id,name FROM user_details WHERE NIC = ?;");
        preparedStatement.setString(1, username);
        return preparedStatement.executeQuery();
    }
}
