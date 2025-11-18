package edu.MD.Fx_final.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface BookRepository {
    ResultSet getAllBookDetails() throws SQLException;
}
