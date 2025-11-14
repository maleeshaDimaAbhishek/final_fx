package edu.MD.Fx_final.repository;

import edu.MD.Fx_final.model.UserRegistrationDetails;

import java.sql.SQLException;

public interface UserRegistrationRepository {
    boolean registerUser(UserRegistrationDetails userRegistrationDetails) throws SQLException;
}
