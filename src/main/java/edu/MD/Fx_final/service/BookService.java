package edu.MD.Fx_final.service;

import edu.MD.Fx_final.model.dto.BookCardDTO;

import java.sql.SQLException;
import java.util.List;

public interface BookService {
    List<BookCardDTO> getAllBookDetails() throws SQLException;
}
