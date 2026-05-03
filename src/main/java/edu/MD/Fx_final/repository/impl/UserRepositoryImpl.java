package edu.MD.Fx_final.repository.impl;

import edu.MD.Fx_final.dbConnection.DBConnection;
import edu.MD.Fx_final.model.dto.UserDetailsDTO;
import edu.MD.Fx_final.model.dto.UserLoginDTO;
import edu.MD.Fx_final.model.dto.UserRegistrationDTO;
import edu.MD.Fx_final.repository.UserRepository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

    private static final String INSERT_USER =
            "INSERT INTO user_details (NIC, name, dob, email, phone_number, address, role_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_LOGIN_BY_NIC =
            "SELECT email, role_id, name FROM user_details WHERE NIC = ?";
    private static final String SELECT_BY_ROLE =
            "SELECT NIC, name, dob, email, phone_number, address FROM user_details WHERE role_id = ?";
    private static final String UPDATE_USER =
            "UPDATE user_details SET name = ?, email = ?, phone_number = ?, address = ?, dob = ? WHERE nic = ?";
    private static final String DELETE_USER = "DELETE FROM user_details WHERE nic = ?";

    private Connection getConnection() throws SQLException {
        return DBConnection.getInstance().getConnection();
    }

    @Override
    public boolean registerUser(UserRegistrationDTO userRegistrationDetails) throws SQLException {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(INSERT_USER)) {
            preparedStatement.setString(1, userRegistrationDetails.getNIC());
            preparedStatement.setString(2, userRegistrationDetails.getName());
            preparedStatement.setDate(3, Date.valueOf(userRegistrationDetails.getDob()));
            preparedStatement.setString(4, userRegistrationDetails.getMail());
            preparedStatement.setString(5, userRegistrationDetails.getPhoneNumber());
            preparedStatement.setString(6, userRegistrationDetails.getAddress());
            preparedStatement.setInt(7, userRegistrationDetails.getRoleId());
            return preparedStatement.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<UserLoginDTO> getUserRole(String userId) throws SQLException {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(SELECT_LOGIN_BY_NIC)) {
            preparedStatement.setString(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new UserLoginDTO(
                            resultSet.getString("email"),
                            resultSet.getInt("role_id"),
                            resultSet.getString("name")
                    ));
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public List<UserDetailsDTO> getAllUserDetails() throws SQLException {
        return getUsersByRole(10);
    }

    @Override
    public int updateUser(UserDetailsDTO updatedUser) throws SQLException {
        return updateUserRecord(updatedUser);
    }

    @Override
    public int deleteUser(String nic) throws SQLException {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(DELETE_USER)) {
            preparedStatement.setString(1, nic);
            return preparedStatement.executeUpdate();
        }
    }

    @Override
    public int updateStaff(UserDetailsDTO updatedUser) throws SQLException {
        return updateUserRecord(updatedUser);
    }

    @Override
    public List<UserDetailsDTO> getAllStaffDetails() throws SQLException {
        return getUsersByRole(12);
    }

    @Override
    public int deleteSStaff(String nic) throws SQLException {
        return deleteUser(nic);
    }

    private int updateUserRecord(UserDetailsDTO updatedUser) throws SQLException {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(UPDATE_USER)) {
            preparedStatement.setString(1, updatedUser.getName());
            preparedStatement.setString(2, updatedUser.getEmail());
            preparedStatement.setString(3, updatedUser.getPhoneNumber());
            preparedStatement.setString(4, updatedUser.getAddress());
            preparedStatement.setDate(5, Date.valueOf(updatedUser.getDob()));
            preparedStatement.setString(6, updatedUser.getNIC());
            return preparedStatement.executeUpdate();
        }
    }

    private List<UserDetailsDTO> getUsersByRole(int roleId) throws SQLException {
        List<UserDetailsDTO> userDetailsList = new ArrayList<>();
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(SELECT_BY_ROLE)) {
            preparedStatement.setInt(1, roleId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    userDetailsList.add(mapUser(resultSet));
                }
            }
        }
        return userDetailsList;
    }

    private UserDetailsDTO mapUser(ResultSet resultSet) throws SQLException {
        return new UserDetailsDTO(
                resultSet.getString("NIC"),
                resultSet.getString("name"),
                resultSet.getDate("dob").toLocalDate(),
                resultSet.getString("email"),
                resultSet.getString("phone_number"),
                resultSet.getString("address")
        );
    }
}
