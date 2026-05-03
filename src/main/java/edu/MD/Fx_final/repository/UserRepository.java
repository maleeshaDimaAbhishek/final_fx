package edu.MD.Fx_final.repository;

import edu.MD.Fx_final.model.dto.UserDetailsDTO;
import edu.MD.Fx_final.model.dto.UserLoginDTO;
import edu.MD.Fx_final.model.dto.UserRegistrationDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    boolean registerUser(UserRegistrationDTO userRegistrationDetails) throws SQLException;
    Optional<UserLoginDTO> getUserRole(String userId) throws SQLException;

    List<UserDetailsDTO> getAllUserDetails() throws SQLException;

    int updateUser(UserDetailsDTO updatedUser) throws SQLException;

    int deleteUser(String nic) throws SQLException;

    int updateStaff(UserDetailsDTO updatedUser) throws SQLException;

    List<UserDetailsDTO> getAllStaffDetails() throws SQLException;

    int deleteSStaff(String nic) throws SQLException;
}
