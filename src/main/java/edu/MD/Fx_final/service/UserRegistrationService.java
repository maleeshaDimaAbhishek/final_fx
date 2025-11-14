package edu.MD.Fx_final.service;

import edu.MD.Fx_final.model.UserLoginDetails;
import edu.MD.Fx_final.model.UserRegistrationDetails;

import java.sql.SQLException;

public interface UserRegistrationService {
    boolean UserRegisration(UserRegistrationDetails userRegistrationDetails) throws SQLException;
}
