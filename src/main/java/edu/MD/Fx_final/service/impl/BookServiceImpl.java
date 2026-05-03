package edu.MD.Fx_final.service.impl;

import edu.MD.Fx_final.model.dto.BookCardDTO;
import edu.MD.Fx_final.repository.BookRepository;
import edu.MD.Fx_final.repository.impl.BookRepositoryImpl;
import edu.MD.Fx_final.service.BookService;

import java.sql.SQLException;
import java.util.List;

public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository = new BookRepositoryImpl();

    @Override
    public List<BookCardDTO> getAllBookDetails() throws SQLException {
        return bookRepository.getAllBookDetails();
    }

    @Override
    public boolean addBook(BookCardDTO bookCardDTO) throws SQLException {
        return bookRepository.addBook(bookCardDTO);
    }

    @Override
    public boolean updateBook(String originalTitle, String originalAuthor, BookCardDTO updatedBook) throws SQLException {
        return bookRepository.updateBook(originalTitle, originalAuthor, updatedBook);
    }
}
