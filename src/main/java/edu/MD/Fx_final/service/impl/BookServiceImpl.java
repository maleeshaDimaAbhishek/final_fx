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
}
