package edu.MD.Fx_final.repository;

import edu.MD.Fx_final.model.dto.UserDetailsDTO;
import edu.MD.Fx_final.model.dto.UserRegistrationDTO;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface UserRepository {
    boolean registerUser(UserRegistrationDTO userRegistrationDetails) throws SQLException;
    ResultSet getUserRole(String UserId) throws SQLException;

    ResultSet getAllUserDetails() throws SQLException;

    int updateUser(UserDetailsDTO updatedUser) throws SQLException;

    int deleteUser(String nic) throws SQLException;

    int updateStaff(UserDetailsDTO updatedUser) throws SQLException;

    ResultSet getAllStaffDetails() throws SQLException;

    int deleteSStaff(String nic) throws SQLException;
}
