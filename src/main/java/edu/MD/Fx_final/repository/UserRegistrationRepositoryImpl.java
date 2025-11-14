package edu.MD.Fx_final.repository;

import edu.MD.Fx_final.dbConnection.DBConnection;
import edu.MD.Fx_final.model.UserRegistrationDetails;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserRegistrationRepositoryImpl implements UserRegistrationRepository {
    Connection connection = DBConnection.getInstance().getConnection();
    @Override
    public boolean registerUser(UserRegistrationDetails userRegistrationDetails) throws SQLException {
        PreparedStatement preparedStatement=connection.prepareStatement("INSERT INTO user_details (NIC, name, dob, email, phone_number,address) VALUES (?, ?, ?, ?, ?,?);");
        preparedStatement.setObject(1,userRegistrationDetails.getNIC());
        preparedStatement.setObject(2,userRegistrationDetails.getName());
        preparedStatement.setObject(3,userRegistrationDetails.getDob());
        preparedStatement.setObject(4,userRegistrationDetails.getMail());
        preparedStatement.setObject(5,userRegistrationDetails.getPhoneNumber());
        preparedStatement.setObject(6,userRegistrationDetails.getAddress());
        return preparedStatement.executeUpdate()>0;
    }
}
