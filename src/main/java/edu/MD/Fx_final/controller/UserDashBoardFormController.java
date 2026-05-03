package edu.MD.Fx_final.controller;

import edu.MD.Fx_final.Starter;
import edu.MD.Fx_final.model.dto.BookCardDTO;
import edu.MD.Fx_final.model.dto.BookOrderDTO;
import edu.MD.Fx_final.service.OrderRegistry;
import edu.MD.Fx_final.service.BookService;
import edu.MD.Fx_final.service.impl.BookServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class UserDashBoardFormController {
    private static final String CARD_STYLE_NORMAL =
            "-fx-background-color: rgba(255,255,255,0.92); -fx-background-radius: 14; -fx-padding: 12; -fx-cursor: hand; -fx-border-color: rgba(28,86,148,0.15); -fx-border-radius: 14;";
    private static final String CARD_STYLE_HOVER =
            "-fx-background-color: rgba(255,255,255,1); -fx-background-radius: 14; -fx-padding: 12; -fx-cursor: hand; -fx-border-color: rgba(28,86,148,0.35); -fx-border-radius: 14;";
    private static final String DEFAULT_BOOK_IMAGE = "/image/library.png";
    private static final DateTimeFormatter ORDER_TIME_FORMAT = DateTimeFormatter.ofPattern("dd MMM HH:mm");

    private final BookService bookService = new BookServiceImpl();
    private final List<BookCardDTO> allBooks = new ArrayList<>();
    private final ObservableList<BookOrderDTO> userOrderItems = FXCollections.observableArrayList();
    private String loggedInUserNic = "UNKNOWN";
    private String loggedInUserName = "User";

    @FXML
    private FlowPane bookContainer;
    @FXML
    private Label totalTitlesLabel;
    @FXML
    private Label availableCopiesLabel;
    @FXML
    private ListView<BookOrderDTO> orderHistoryList;
    @FXML
    private TextField txtSearch;

    @FXML
    public void initialize() {
        orderHistoryList.setItems(userOrderItems);
        orderHistoryList.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(BookOrderDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getOrderedAt() + " | " + item.getOrderId() + " | " + item.getBookTitle());
                }
            }
        });
        loadBooksFromDatabase();
    }

    public void setLoggedInUser(String userNic, String userName) {
        if (userNic != null && !userNic.isBlank()) {
            this.loggedInUserNic = userNic;
        }
        if (userName != null && !userName.isBlank()) {
            this.loggedInUserName = userName;
        }
        refreshUserOrderHistory();
    }

    @FXML
    private void handleSearchBooks(ActionEvent actionEvent) {
        String query = normalize(txtSearch.getText());
        if (query.isEmpty()) {
            renderBooks(allBooks);
            return;
        }

        List<BookCardDTO> filteredBooks = new ArrayList<>();
        for (BookCardDTO book : allBooks) {
            if (contains(book.getTitle(), query)
                    || contains(book.getAuthor(), query)
                    || contains(book.getCategory(), query)
                    || contains(book.getPublisher(), query)) {
                filteredBooks.add(book);
            }
        }
        renderBooks(filteredBooks);
    }

    @FXML
    private void handleClearSearch(ActionEvent actionEvent) {
        txtSearch.clear();
        renderBooks(allBooks);
    }

    private void loadBooksFromDatabase() {
        allBooks.clear();
        try {
            allBooks.addAll(bookService.getAllBookDetails());
            applyPersistedOrderReservations();
            renderBooks(allBooks);
            refreshSummary();
        } catch (SQLException e) {
            showMessage("Unable to load books right now.");
        }
    }

    private void renderBooks(List<BookCardDTO> books) {
        bookContainer.getChildren().clear();
        if (books.isEmpty()) {
            showMessage("No books match your search.");
            return;
        }

        for (BookCardDTO book : books) {
            bookContainer.getChildren().add(createBookCard(book));
        }
    }

    private VBox createBookCard(BookCardDTO book) {
        ImageView bookImage = new ImageView(resolveBookImage(book.getImageLink()));
        bookImage.setFitWidth(130);
        bookImage.setFitHeight(165);
        bookImage.setPreserveRatio(true);

        Label titleLabel = new Label(book.getTitle());
        titleLabel.setFont(Font.font(15));
        titleLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #1f3f63;");
        titleLabel.setWrapText(true);

        Label authorLabel = new Label("Author: " + book.getAuthor());
        Label publisherLabel = new Label("Publisher: " + book.getPublisher());
        Label yearLabel = new Label("Year: " + book.getPublished_year());
        Label categoryLabel = new Label("Category: " + book.getCategory());
        Label copiesLabel = new Label("Available: " + book.getAvailable_copies());
        copiesLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #225a8f;");

        VBox infoBox = new VBox(4, titleLabel, authorLabel, publisherLabel, yearLabel, categoryLabel, copiesLabel);
        infoBox.setAlignment(Pos.CENTER_LEFT);

        VBox box = new VBox(10, bookImage, infoBox);
        box.setAlignment(Pos.TOP_CENTER);
        box.setPrefWidth(220);
        box.setStyle(CARD_STYLE_NORMAL);
        box.setOnMouseEntered(event -> box.setStyle(CARD_STYLE_HOVER));
        box.setOnMouseExited(event -> box.setStyle(CARD_STYLE_NORMAL));
        box.setOnMouseClicked(event -> handleBookOrder(book));

        return box;
    }

    private void handleBookOrder(BookCardDTO selectedBook) {
        if (selectedBook.getAvailable_copies() <= 0) {
            showAlert(Alert.AlertType.WARNING, "Book Unavailable",
                    "Sorry, \"" + selectedBook.getTitle() + "\" is currently out of stock.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Order Book");
        confirmAlert.setHeaderText("Order confirmation");
        confirmAlert.setContentText("Do you want to order \"" + selectedBook.getTitle() + "\" now?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        selectedBook.setAvailable_copies(selectedBook.getAvailable_copies() - 1);
        String orderId = "ORD-" + ThreadLocalRandom.current().nextInt(10000, 99999);
        long nowMillis = System.currentTimeMillis();
        String orderedAt = ORDER_TIME_FORMAT.format(LocalDateTime.now());

        handleSearchBooks(null);
        refreshSummary();

        BookOrderDTO newOrder = new BookOrderDTO(
                orderId,
                loggedInUserNic,
                loggedInUserName,
                selectedBook.getTitle(),
                selectedBook.getAuthor(),
                orderedAt,
                "ORDERED",
                nowMillis
        );
        OrderRegistry.addOrder(newOrder);
        refreshUserOrderHistory();

        showAlert(Alert.AlertType.INFORMATION, "Order Placed",
                "Your order has been placed.\nReference: " + orderId);
    }

    @FXML
    private void handleDeleteSelectedOrder(ActionEvent actionEvent) {
        BookOrderDTO selectedOrder = orderHistoryList.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an order to delete.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Delete Order");
        confirmAlert.setHeaderText("Delete selected order");
        confirmAlert.setContentText("Delete order " + selectedOrder.getOrderId() + " for \"" + selectedOrder.getBookTitle() + "\"?");
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        OrderRegistry.CancelResult cancelResult = OrderRegistry.cancelOrder(loggedInUserNic, selectedOrder.getOrderId());
        if (!cancelResult.success()) {
            showAlert(Alert.AlertType.WARNING, "Delete Failed", cancelResult.message());
            refreshUserOrderHistory();
            return;
        }

        restoreBookCopy(cancelResult.order());
        refreshSummary();
        refreshUserOrderHistory();
        showAlert(Alert.AlertType.INFORMATION, "Order Deleted", "Order deleted successfully within 30 minutes.");
    }

    private void refreshSummary() {
        totalTitlesLabel.setText(String.valueOf(allBooks.size()));
        int availableCopies = 0;
        for (BookCardDTO book : allBooks) {
            availableCopies += book.getAvailable_copies();
        }
        availableCopiesLabel.setText(String.valueOf(availableCopies));
    }

    private Image resolveBookImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            return new Image(DEFAULT_BOOK_IMAGE);
        }
        try {
            return new Image(imageUrl, true);
        } catch (Exception e) {
            return new Image(DEFAULT_BOOK_IMAGE);
        }
    }

    @FXML
    private void onPaymentClick(ActionEvent actionEvent) {
        showAlert(Alert.AlertType.INFORMATION, "Payments",
                "Payment module will be connected in the next update.");
    }

    @FXML
    private void onBorrowClick(ActionEvent actionEvent) {
        showAlert(Alert.AlertType.INFORMATION, "My Orders",
                "Select an order and click \"Delete Selected Order\" to remove it within 30 minutes.");
    }

    @FXML
    private void onLogoutClick(ActionEvent actionEvent) {
        Starter.loginFormReference.show();
        OTPLoginController.userDashBoard.hide();
    }

    private void showMessage(String message) {
        Label label = new Label(message);
        label.setStyle("-fx-font-size: 16px; -fx-text-fill: #4b6b8d;");
        bookContainer.getChildren().setAll(label);
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.trim().toLowerCase(Locale.ROOT);
    }

    private boolean contains(String value, String query) {
        return normalize(value).contains(query);
    }

    private void restoreBookCopy(BookOrderDTO deletedOrder) {
        if (deletedOrder == null) {
            return;
        }
        for (BookCardDTO book : allBooks) {
            if (book.getTitle().equals(deletedOrder.getBookTitle())
                    && book.getAuthor().equals(deletedOrder.getBookAuthor())) {
                book.setAvailable_copies(book.getAvailable_copies() + 1);
                break;
            }
        }
        handleSearchBooks(null);
    }

    private void refreshUserOrderHistory() {
        List<BookOrderDTO> allOrders = OrderRegistry.getAllOrders();
        ObservableList<BookOrderDTO> filteredOrders = FXCollections.observableArrayList();
        for (BookOrderDTO order : allOrders) {
            if (order.getUserNic().equals(loggedInUserNic)) {
                filteredOrders.add(order);
            }
        }
        userOrderItems.setAll(filteredOrders);
    }

    private void applyPersistedOrderReservations() {
        List<BookOrderDTO> persistedOrders = OrderRegistry.getAllOrders();
        for (BookCardDTO book : allBooks) {
            int reservedCount = 0;
            for (BookOrderDTO order : persistedOrders) {
                if ("ORDERED".equalsIgnoreCase(order.getStatus())
                        && book.getTitle().equals(order.getBookTitle())
                        && book.getAuthor().equals(order.getBookAuthor())) {
                    reservedCount++;
                }
            }
            int adjustedCopies = Math.max(0, book.getAvailable_copies() - reservedCount);
            book.setAvailable_copies(adjustedCopies);
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
