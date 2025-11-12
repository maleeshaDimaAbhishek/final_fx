package edu.MD.Fx_final.service;

import edu.MD.Fx_final.model.BookCardDetails;
import edu.MD.Fx_final.repository.UserDashBoardRepository;
import edu.MD.Fx_final.repository.UserDashBoardRepositoryImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDashBoardServiceImpl implements UserDashBoardService {
    UserDashBoardRepository userDashBoardRepository=new UserDashBoardRepositoryImpl();
    @Override
    public List<BookCardDetails> getAllBookDetails() throws SQLException {
        ResultSet resultSet=userDashBoardRepository.getAllBookDetails();
        List<BookCardDetails> bookList = new ArrayList<>();
        while (resultSet.next()) {
            bookList.add( new BookCardDetails(
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
