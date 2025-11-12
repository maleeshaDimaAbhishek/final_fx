package edu.MD.Fx_final.repository;

import edu.MD.Fx_final.model.BookCardDetails;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface UserDashBoardRepository {
    ResultSet getAllBookDetails() throws SQLException;
}
