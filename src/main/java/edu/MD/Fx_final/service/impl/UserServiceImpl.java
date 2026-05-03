package edu.MD.Fx_final.service.impl;

import edu.MD.Fx_final.model.dto.UserDetailsDTO;
import edu.MD.Fx_final.model.dto.UserLoginDTO;
import edu.MD.Fx_final.model.dto.UserRegistrationDTO;
import edu.MD.Fx_final.repository.UserRepository;
import edu.MD.Fx_final.repository.impl.UserRepositoryImpl;
import edu.MD.Fx_final.service.UserService;

import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository = new UserRepositoryImpl();

    @Override
    public boolean UserRegistration(UserRegistrationDTO userRegistrationDetails) throws SQLException {
        return userRepository.registerUser(userRegistrationDetails);
    }

    @Override
    public UserLoginDTO checkUserRole(String userName) throws SQLException {
        return userRepository.getUserRole(userName).orElse(null);
    }

    @Override
    public List<UserDetailsDTO> getAllUserDetails() throws SQLException {
        return userRepository.getAllUserDetails();
    }

    @Override
    public int updateUser(UserDetailsDTO updatedUser) throws SQLException {
        return userRepository.updateUser(updatedUser);
    }

    @Override
    public int deleteUser(String nic) throws SQLException {
        return userRepository.deleteUser(nic);
    }

    @Override
    public int updateStaff(UserDetailsDTO updatedUser) throws SQLException {
        return userRepository.updateStaff(updatedUser);
    }

    @Override
    public List<UserDetailsDTO> getAllStaffDetails() throws SQLException {
        return userRepository.getAllStaffDetails();
    }

    @Override
    public int deleteStaff(String nic) throws SQLException {
        return userRepository.deleteSStaff(nic);
    }
}
