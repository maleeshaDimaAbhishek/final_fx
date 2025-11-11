package edu.MD.Fx_final.service;

import java.sql.ResultSet;
import java.sql.SQLException;

import edu.MD.Fx_final.model.UserLoginDetails;
import edu.MD.Fx_final.repository.UserLoginRepository;
import edu.MD.Fx_final.repository.UserLoginRepositoryImpl;


public class UserLoginServiceImpl implements UserLoginService {
    UserLoginRepository userLoginRepository = new UserLoginRepositoryImpl();

    public UserLoginDetails checkUserRole(String userName) throws SQLException {
        ResultSet resultSet = userLoginRepository.getUserRole(userName);
        if(resultSet.next()){
            return new UserLoginDetails(
                    resultSet.getString("email"),
                    resultSet.getInt("role_id"),
                    resultSet.getString("name"));
        }
        return null;
    }
}

