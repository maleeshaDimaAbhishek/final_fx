package edu.MD.Fx_final.service;
import edu.MD.Fx_final.model.UserLoginDetails;
import java.sql.SQLException;

public interface UserLoginService {
    UserLoginDetails checkUserRole(String userName) throws SQLException;
}
