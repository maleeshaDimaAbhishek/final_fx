package edu.MD.Fx_final.service;

import edu.MD.Fx_final.model.dto.BookCardDTO;

import java.sql.SQLException;
import java.util.List;

public interface BookService {
    List<BookCardDTO> getAllBookDetails() throws SQLException;
    boolean addBook(BookCardDTO bookCardDTO) throws SQLException;
    boolean updateBook(String originalTitle, String originalAuthor, BookCardDTO updatedBook) throws SQLException;
}
