package edu.MD.Fx_final.service;

import edu.MD.Fx_final.model.UserLoginDetails;
import edu.MD.Fx_final.model.UserRegistrationDetails;
import edu.MD.Fx_final.repository.UserDashBoardRepository;
import edu.MD.Fx_final.repository.UserDashBoardRepositoryImpl;
import edu.MD.Fx_final.repository.UserRegistrationRepository;
import edu.MD.Fx_final.repository.UserRegistrationRepositoryImpl;

import java.sql.SQLException;

public class UserRegistrationServiceImpl implements UserRegistrationService{
    UserRegistrationRepository userRegistrationRepository=new UserRegistrationRepositoryImpl();
    @Override
    public boolean UserRegisration(UserRegistrationDetails userRegistrationDetails) throws SQLException {
        return userRegistrationRepository.registerUser(userRegistrationDetails);
    }
}
