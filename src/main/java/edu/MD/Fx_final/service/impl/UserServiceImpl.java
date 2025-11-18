package edu.MD.Fx_final.service.impl;

import edu.MD.Fx_final.model.dto.BookCardDTO;
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
}
