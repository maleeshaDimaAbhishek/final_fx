package edu.MD.Fx_final.service.impl;

import edu.MD.Fx_final.model.dto.UserDetailsDTO;
import edu.MD.Fx_final.model.dto.UserLoginDTO;
import edu.MD.Fx_final.model.dto.UserRegistrationDTO;
import edu.MD.Fx_final.repository.UserRepository;
import edu.MD.Fx_final.repository.impl.UserRepositoryImpl;
import edu.MD.Fx_final.service.UserService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {
    UserRepository  userRepository= new UserRepositoryImpl();
    @Override
    public boolean UserRegistration(UserRegistrationDTO userRegistrationDetails) throws SQLException {
        return userRepository.registerUser(userRegistrationDetails);
    }
    @Override
    public UserLoginDTO checkUserRole(String userName) throws SQLException {
        ResultSet resultSet = userRepository.getUserRole(userName);
        if(resultSet.next()){
            return new UserLoginDTO(
                    resultSet.getString("email"),
                    resultSet.getInt("role_id"),
                    resultSet.getString("name"));
        }
        return null;
    }

    @Override
    public List<UserDetailsDTO> getAllUserDetails() throws SQLException {
        ResultSet  resultSet=userRepository.getAllUserDetails();
        List<UserDetailsDTO> userDetailsDTOS=new ArrayList<>();
        UserDetailsDTO userDetailsDTO;
        while (resultSet.next()) {
            userDetailsDTOS.add(userDetailsDTO=new UserDetailsDTO(
                    resultSet.getString("NIC"),
                    resultSet.getString("name"),
                    resultSet.getDate("dob").toLocalDate(),
                    resultSet.getString("email"),
                    resultSet.getString("phone_number"),
                    resultSet.getString("address")
            ));
            System.out.println(userDetailsDTO);
        }    return userDetailsDTOS;
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
        ResultSet  resultSet=userRepository.getAllStaffDetails();
        List<UserDetailsDTO> userDetailsDTOS=new ArrayList<>();
        UserDetailsDTO userDetailsDTO;
        while (resultSet.next()) {
            userDetailsDTOS.add(userDetailsDTO=new UserDetailsDTO(
                    resultSet.getString("NIC"),
                    resultSet.getString("name"),
                    resultSet.getDate("dob").toLocalDate(),
                    resultSet.getString("email"),
                    resultSet.getString("phone_number"),
                    resultSet.getString("address")
            ));
            System.out.println(userDetailsDTO);
        }    return userDetailsDTOS;
    }

    @Override
    public int deleteStaff(String nic) throws SQLException {
        return userRepository.deleteSStaff(nic);
    }
}
