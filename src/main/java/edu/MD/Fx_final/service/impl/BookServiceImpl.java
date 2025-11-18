package edu.MD.Fx_final.service.impl;

import edu.MD.Fx_final.model.dto.BookCardDTO;
import edu.MD.Fx_final.repository.BookRepository;
import edu.MD.Fx_final.repository.impl.BookRepositoryImpl;
import edu.MD.Fx_final.service.BookService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookServiceImpl implements BookService {
    BookRepository bookRepository=new BookRepositoryImpl();
    @Override
    public List<BookCardDTO> getAllBookDetails() throws SQLException {
        ResultSet resultSet=bookRepository.getAllBookDetails();
        List<BookCardDTO> bookList = new ArrayList<>();
        while (resultSet.next()) {
            bookList.add( new BookCardDTO(
                    resultSet.getString("title"),
                    resultSet.getString("author"),
                    resultSet.getString("publisher"),
                    resultSet.getString("published_year"),
                    resultSet.getString("category"),
                    resultSet.getInt("available_copies"),
                    resultSet.getString("book_image")
            ));
        }
        return bookList;
    }
}
