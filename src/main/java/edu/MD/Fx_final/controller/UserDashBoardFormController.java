package edu.MD.Fx_final.controller;

import edu.MD.Fx_final.Starter;
import edu.MD.Fx_final.model.dto.BookCardDTO;
import edu.MD.Fx_final.service.BookService;
import edu.MD.Fx_final.service.UserService;
import edu.MD.Fx_final.service.impl.BookServiceImpl;
import edu.MD.Fx_final.service.impl.UserServiceImpl;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.geometry.Pos;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDashBoardFormController {
    BookService bookService=new BookServiceImpl();
    List<BookCardDTO> bookCardDetailsList=new ArrayList<>();
    @FXML
    private FlowPane bookContainer;

    @FXML
    public void initialize() {
        loadBooksFromDatabase();
    }

    private void loadBooksFromDatabase() {
        try {
           bookCardDetailsList= bookService.getAllBookDetails();
           for(BookCardDTO bookCardDetails1:bookCardDetailsList) {
               VBox bookCard = createBookCard(bookCardDetails1.getTitle(),
                       bookCardDetails1.getAuthor(),
                       bookCardDetails1.getPublisher(),
                       bookCardDetails1.getPublished_year(),
                       bookCardDetails1.getCategory(),
                       bookCardDetails1.getAvailable_copies(),
                       bookCardDetails1.getImageLink());
               bookContainer.getChildren().add(bookCard);
           }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private VBox createBookCard(String title, String author, String publisher, String year,
                                String category, int copies, String imageUrl) {
        ImageView bookImage = new ImageView();
        try {
            Image image = new Image(imageUrl, 120, 150, true, true);
            bookImage.setImage(image);
        } catch (Exception e) {
            System.out.println("Invalid image URL: " + imageUrl);
        }

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font(15));
        titleLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c2c2c;");

        Label authorLabel = new Label("Author: " + author);
        Label publisherLabel = new Label("Publisher: " + publisher);
        Label yearLabel = new Label("Year: " + year);
        Label categoryLabel = new Label("Category: " + category);
        Label copiesLabel = new Label("Available: " + copies);

        VBox infoBox = new VBox(3, titleLabel, authorLabel, publisherLabel, yearLabel, categoryLabel, copiesLabel);
        infoBox.setAlignment(Pos.CENTER_LEFT);
        infoBox.setStyle("-fx-padding: 5;");

        VBox box = new VBox(10, bookImage, infoBox);
        box.setAlignment(Pos.CENTER);
        box.setPrefSize(200, 280);
        box.setStyle("-fx-background-color: rgba(255,255,255,0.85); -fx-background-radius: 15; -fx-padding: 10; -fx-cursor: hand;");

        // Hover effects
        box.setOnMouseEntered(e -> box.setStyle("-fx-background-color: rgba(255,255,255,1); -fx-background-radius: 15; -fx-padding: 10;"));
        box.setOnMouseExited(e -> box.setStyle("-fx-background-color: rgba(255,255,255,0.85); -fx-background-radius: 15; -fx-padding: 10;"));

        // Clickable behavior
        box.setOnMouseClicked(e -> handleBookClick(title));

        return box;
    }

    private void handleBookClick(String title) {
        System.out.println("Clicked book: " + title);
        // You can open a book details window here
    }

    // 🔹 Top navigation events
    @FXML private void onPaymentClick() { System.out.println("Payment Clicked"); }
    @FXML private void onReturnClick() {

    }
    @FXML private void onBorrowClick() { System.out.println("Borrow Book Clicked"); }
    @FXML private void onLogoutClick() {
        Starter.loginFormReference.show();
        OTPLoginController.userDashBoard.hide();
    }
}
