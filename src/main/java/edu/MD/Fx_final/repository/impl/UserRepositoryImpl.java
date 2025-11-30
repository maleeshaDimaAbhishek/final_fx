package edu.MD.Fx_final.repository.impl;

import edu.MD.Fx_final.dbConnection.DBConnection;
import edu.MD.Fx_final.model.dto.UserDetailsDTO;
import edu.MD.Fx_final.model.dto.UserRegistrationDTO;
import edu.MD.Fx_final.repository.UserRepository;

import java.sql.*;

public class UserRepositoryImpl implements UserRepository {
    Connection connection = DBConnection.getInstance().getConnection();
    @Override
    public boolean registerUser(UserRegistrationDTO userRegistrationDetails) throws SQLException {
        PreparedStatement preparedStatement=connection.prepareStatement("INSERT INTO user_details (NIC, name, dob, email, phone_number,address,role_id) VALUES (?, ?, ?, ?, ?,?,?);");
        preparedStatement.setObject(1,userRegistrationDetails.getNIC());
        preparedStatement.setObject(2,userRegistrationDetails.getName());
        preparedStatement.setObject(3,userRegistrationDetails.getDob());
        preparedStatement.setObject(4,userRegistrationDetails.getMail());
        preparedStatement.setObject(5,userRegistrationDetails.getPhoneNumber());
        preparedStatement.setObject(6,userRegistrationDetails.getAddress());
        preparedStatement.setObject(7,userRegistrationDetails.getRoleId());
        return preparedStatement.executeUpdate()>0;
    }
    @Override
    public ResultSet getUserRole(String username) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT email,role_id,name FROM user_details WHERE NIC = ?;");
        preparedStatement.setString(1, username);
        return preparedStatement.executeQuery();
    }

    @Override
    public ResultSet getAllUserDetails() throws SQLException {
        PreparedStatement preparedStatement=connection.prepareStatement("SELECT NIC,name,dob,email,phone_number,address from user_details where role_id=10;");
        return preparedStatement.executeQuery();
    }

    @Override
    public int updateUser(UserDetailsDTO updatedUser) throws SQLException {
        String query = "UPDATE user_details SET name = ?, email = ?, phone_number = ?, address = ?, dob = ? WHERE nic = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setString(1, updatedUser.getName());
        preparedStatement.setString(2, updatedUser.getEmail());
        preparedStatement.setString(3, updatedUser.getPhoneNumber());
        preparedStatement.setString(4, updatedUser.getAddress());
        preparedStatement.setDate(5, Date.valueOf(updatedUser.getDob()));
        preparedStatement.setString(6, updatedUser.getNIC());
        return preparedStatement.executeUpdate();
    }

    @Override
    public int deleteUser(String nic) throws SQLException {
        String query = "DELETE FROM user_details WHERE nic = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, nic);
        return preparedStatement.executeUpdate();
        }

    @Override
    public int updateStaff(UserDetailsDTO updatedUser) throws SQLException {
        String query = "UPDATE user_details SET name = ?, email = ?, phone_number = ?, address = ?, dob = ? WHERE nic = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setString(1, updatedUser.getName());
        preparedStatement.setString(2, updatedUser.getEmail());
        preparedStatement.setString(3, updatedUser.getPhoneNumber());
        preparedStatement.setString(4, updatedUser.getAddress());
        preparedStatement.setDate(5, Date.valueOf(updatedUser.getDob()));
        preparedStatement.setString(6, updatedUser.getNIC());
        return preparedStatement.executeUpdate();
    }

    @Override
    public ResultSet getAllStaffDetails() throws SQLException {
        PreparedStatement preparedStatement=connection.prepareStatement("SELECT NIC,name,dob,email,phone_number,address from user_details where role_id=12;");
        return preparedStatement.executeQuery();
    }

    @Override
    public int deleteSStaff(String nic) throws SQLException {
        String query = "DELETE FROM user_details WHERE nic = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, nic);
        return preparedStatement.executeUpdate();
    }
}

