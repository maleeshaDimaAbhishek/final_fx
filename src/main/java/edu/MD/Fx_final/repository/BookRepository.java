package edu.MD.Fx_final.repository;

import edu.MD.Fx_final.model.dto.BookCardDTO;

import java.sql.SQLException;
import java.util.List;

public interface BookRepository {
    List<BookCardDTO> getAllBookDetails() throws SQLException;
}
