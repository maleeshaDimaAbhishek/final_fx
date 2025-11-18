package edu.MD.Fx_final.service;

import edu.MD.Fx_final.model.dto.BookCardDTO;
import edu.MD.Fx_final.model.dto.UserLoginDTO;
import edu.MD.Fx_final.model.dto.UserRegistrationDTO;

import java.sql.SQLException;
import java.util.List;

public interface UserService {
    boolean UserRegistration(UserRegistrationDTO userRegistrationDetails) throws SQLException;
    UserLoginDTO checkUserRole(String userName) throws SQLException;
}
