package edu.MD.Fx_final.service;

import edu.MD.Fx_final.model.BookCardDetails;

import java.sql.SQLException;
import java.util.List;

public interface UserDashBoardService {
    List<BookCardDetails> getAllBookDetails() throws SQLException;
}
