package edu.MD.Fx_final.controller;

import edu.MD.Fx_final.Starter;
import edu.MD.Fx_final.model.dto.BookCardDTO;
import edu.MD.Fx_final.model.dto.BookOrderDTO;
import edu.MD.Fx_final.model.dto.UserDetailsDTO;
import edu.MD.Fx_final.service.BookService;
import edu.MD.Fx_final.service.OrderRegistry;
import edu.MD.Fx_final.service.UserService;
import edu.MD.Fx_final.service.impl.BookServiceImpl;
import edu.MD.Fx_final.service.impl.UserServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class StaffDashboardController {
    private static final String MODULE_NOT_AVAILABLE = "This function is planned and not available yet.";

    private final UserService userService = new UserServiceImpl();
    private final BookService bookService = new BookServiceImpl();

    private final ObservableList<String> activityFeed = FXCollections.observableArrayList();
    private final ObservableList<UserDetailsDTO> allUsers = FXCollections.observableArrayList();
    private final ObservableList<UserDetailsDTO> visibleUsers = FXCollections.observableArrayList();
    private final ObservableList<BookCardDTO> allBooks = FXCollections.observableArrayList();
    private final ObservableList<BookCardDTO> visibleBooks = FXCollections.observableArrayList();
    private final ObservableList<BookOrderDTO> allOrders = FXCollections.observableArrayList();
    private final ObservableList<BookOrderDTO> visibleOrders = FXCollections.observableArrayList();

    private Runnable modulePrimaryAction;

    @FXML
    private Label dashboardDateLabel;
    @FXML
    private Label totalUsersLabel;
    @FXML
    private Label totalBooksLabel;
    @FXML
    private Label availableBooksLabel;
    @FXML
    private Label activeBorrowingsLabel;
    @FXML
    private ListView<String> recentActivityList;

    @FXML
    private VBox overviewPane;
    @FXML
    private VBox usersPane;
    @FXML
    private VBox booksPane;
    @FXML
    private VBox ordersPane;
    @FXML
    private VBox modulePane;

    @FXML
    private TextField searchUsersField;
    @FXML
    private TextField searchBooksField;
    @FXML
    private TextField searchOrdersField;

    @FXML
    private TableView<UserDetailsDTO> usersTable;
    @FXML
    private TableColumn<UserDetailsDTO, String> userNicCol;
    @FXML
    private TableColumn<UserDetailsDTO, String> userNameCol;
    @FXML
    private TableColumn<UserDetailsDTO, String> userEmailCol;
    @FXML
    private TableColumn<UserDetailsDTO, String> userPhoneCol;
    @FXML
    private TableColumn<UserDetailsDTO, String> userAddressCol;
    @FXML
    private TableColumn<UserDetailsDTO, LocalDate> userDobCol;

    @FXML
    private TableView<BookCardDTO> booksTable;
    @FXML
    private TableColumn<BookCardDTO, String> titleCol;
    @FXML
    private TableColumn<BookCardDTO, String> authorCol;
    @FXML
    private TableColumn<BookCardDTO, String> publisherCol;
    @FXML
    private TableColumn<BookCardDTO, String> yearCol;
    @FXML
    private TableColumn<BookCardDTO, String> categoryCol;
    @FXML
    private TableColumn<BookCardDTO, Integer> copiesCol;
    @FXML
    private TextField bookTitleField;
    @FXML
    private TextField bookAuthorField;
    @FXML
    private TextField bookPublisherField;
    @FXML
    private TextField bookYearField;
    @FXML
    private TextField bookCategoryField;
    @FXML
    private TextField bookCopiesField;
    @FXML
    private TextField bookImageField;

    @FXML
    private TableView<BookOrderDTO> ordersTable;
    @FXML
    private TableColumn<BookOrderDTO, String> orderIdCol;
    @FXML
    private TableColumn<BookOrderDTO, String> orderUserNicCol;
    @FXML
    private TableColumn<BookOrderDTO, String> orderUserNameCol;
    @FXML
    private TableColumn<BookOrderDTO, String> orderBookTitleCol;
    @FXML
    private TableColumn<BookOrderDTO, String> orderBookAuthorCol;
    @FXML
    private TableColumn<BookOrderDTO, String> orderDateTimeCol;
    @FXML
    private TableColumn<BookOrderDTO, String> orderStatusCol;

    @FXML
    private Label moduleTitleLabel;
    @FXML
    private Label moduleDescriptionLabel;
    @FXML
    private Button modulePrimaryActionBtn;

    @FXML
    public void initialize() {
        dashboardDateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
        setupUsersTable();
        setupBooksTable();
        setupOrdersTable();
        recentActivityList.setItems(activityFeed);
        showOverview();
        refreshDashboardData();
        addActivity("Staff workspace opened");
    }

    @FXML
    public void handleOverviewAction(ActionEvent actionEvent) {
        showOverview();
        addActivity("Switched to overview");
    }

    @FXML
    public void handleRefreshDashboardAction(ActionEvent actionEvent) {
        refreshDashboardData();
        addActivity("Dashboard data refreshed");
    }

    @FXML
    public void handleViewUserOnAction(ActionEvent actionEvent) {
        loadUsers();
        visibleUsers.setAll(allUsers);
        showPane(usersPane);
        addActivity("Viewed users directory");
    }

    @FXML
    public void handleViewBooksOnAction(ActionEvent actionEvent) {
        loadBooks();
        visibleBooks.setAll(allBooks);
        showPane(booksPane);
        addActivity("Viewed books catalog");
    }

    @FXML
    public void handleViewBorrowingsOnAction(ActionEvent actionEvent) {
        loadOrders();
        visibleOrders.setAll(allOrders);
        showPane(ordersPane);
        addActivity("Viewed user book orders");
    }

    @FXML
    public void handleViewPaymentsOnAction(ActionEvent actionEvent) {
        showModule(
                "Payments Module",
                "Review user fines, payment history, and pending settlements.",
                "Open Payments",
                () -> showInfo("Payments", MODULE_NOT_AVAILABLE)
        );
        addActivity("Opened payments panel");
    }

    @FXML
    public void handleRegisterUserOnAction(ActionEvent actionEvent) {
        showModule(
                "Register User",
                "Create and verify new user registrations through the dedicated registration workflow.",
                "Start Registration",
                () -> showInfo("Register User", MODULE_NOT_AVAILABLE)
        );
        addActivity("Opened register user panel");
    }

    @FXML
    public void handleManageBooksOnAction(ActionEvent actionEvent) {
        handleViewBooksOnAction(actionEvent);
    }

    @FXML
    public void handleAddBookAction(ActionEvent actionEvent) {
        BookCardDTO bookToAdd = buildBookFromForm();
        if (bookToAdd == null) {
            return;
        }

        try {
            boolean added = bookService.addBook(bookToAdd);
            if (added) {
                showInfo("Books", "Book added successfully.");
                clearBookForm();
                loadBooks();
                visibleBooks.setAll(allBooks);
                updateStats();
                addActivity("Added book: " + bookToAdd.getTitle());
            } else {
                showInfo("Books", "Book was not added.");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            showInfo("Books Error", "Add blocked: a book with same title and author already exists.");
        } catch (SQLException e) {
            showInfo("Books Error", "Unable to add book right now.\n" + e.getMessage());
        }
    }

    @FXML
    public void handleUpdateBookAction(ActionEvent actionEvent) {
        BookCardDTO selectedBook = booksTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showInfo("Books", "Select a book from the table to update.");
            return;
        }

        BookCardDTO updatedBook = buildBookFromForm();
        if (updatedBook == null) {
            return;
        }

        try {
            boolean updated = bookService.updateBook(selectedBook.getTitle(), selectedBook.getAuthor(), updatedBook);
            if (updated) {
                showInfo("Books", "Book updated successfully.");
                clearBookForm();
                loadBooks();
                visibleBooks.setAll(allBooks);
                updateStats();
                addActivity("Updated book: " + selectedBook.getTitle());
            } else {
                showInfo("Books", "No matching book found to update.");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            showInfo("Books Error", "Update blocked: another book already uses this title and author.");
        } catch (SQLException e) {
            showInfo("Books Error", "Unable to update book right now.\n" + e.getMessage());
        }
    }

    @FXML
    public void handleClearBookFormAction(ActionEvent actionEvent) {
        clearBookForm();
    }

    @FXML
    public void handleSearchUsersAction(ActionEvent actionEvent) {
        String query = normalize(searchUsersField.getText());
        if (query.isEmpty()) {
            visibleUsers.setAll(allUsers);
            return;
        }

        ObservableList<UserDetailsDTO> filtered = FXCollections.observableArrayList();
        for (UserDetailsDTO user : allUsers) {
            if (contains(user.getNIC(), query)
                    || contains(user.getName(), query)
                    || contains(user.getEmail(), query)
                    || contains(user.getPhoneNumber(), query)) {
                filtered.add(user);
            }
        }
        visibleUsers.setAll(filtered);
        addActivity("Searched users: " + query);
    }

    @FXML
    public void handleSearchBooksAction(ActionEvent actionEvent) {
        String query = normalize(searchBooksField.getText());
        if (query.isEmpty()) {
            visibleBooks.setAll(allBooks);
            return;
        }

        ObservableList<BookCardDTO> filtered = FXCollections.observableArrayList();
        for (BookCardDTO book : allBooks) {
            if (contains(book.getTitle(), query)
                    || contains(book.getAuthor(), query)
                    || contains(book.getCategory(), query)
                    || contains(book.getPublisher(), query)) {
                filtered.add(book);
            }
        }
        visibleBooks.setAll(filtered);
        addActivity("Searched books: " + query);
    }

    @FXML
    public void handleSearchOrdersAction(ActionEvent actionEvent) {
        String query = normalize(searchOrdersField.getText());
        if (query.isEmpty()) {
            visibleOrders.setAll(allOrders);
            return;
        }

        ObservableList<BookOrderDTO> filtered = FXCollections.observableArrayList();
        for (BookOrderDTO order : allOrders) {
            if (contains(order.getOrderId(), query)
                    || contains(order.getUserNic(), query)
                    || contains(order.getUserName(), query)
                    || contains(order.getBookTitle(), query)
                    || contains(order.getBookAuthor(), query)) {
                filtered.add(order);
            }
        }
        visibleOrders.setAll(filtered);
        addActivity("Searched orders: " + query);
    }

    @FXML
    public void handleModulePrimaryAction(ActionEvent actionEvent) {
        if (modulePrimaryAction != null) {
            modulePrimaryAction.run();
        }
    }

    @FXML
    public void handleLogoutAction(ActionEvent actionEvent) {
        OTPLoginController.staffDashBoard.close();
        Starter.loginFormReference.show();
    }

    private void setupUsersTable() {
        userNicCol.setCellValueFactory(new PropertyValueFactory<>("NIC"));
        userNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        userEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        userPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        userAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        userDobCol.setCellValueFactory(new PropertyValueFactory<>("dob"));
        usersTable.setItems(visibleUsers);
    }

    private void setupBooksTable() {
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        yearCol.setCellValueFactory(new PropertyValueFactory<>("published_year"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        copiesCol.setCellValueFactory(new PropertyValueFactory<>("available_copies"));
        booksTable.setItems(visibleBooks);
        booksTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> populateBookForm(newValue));
    }

    private void setupOrdersTable() {
        orderIdCol.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        orderUserNicCol.setCellValueFactory(new PropertyValueFactory<>("userNic"));
        orderUserNameCol.setCellValueFactory(new PropertyValueFactory<>("userName"));
        orderBookTitleCol.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        orderBookAuthorCol.setCellValueFactory(new PropertyValueFactory<>("bookAuthor"));
        orderDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("orderedAt"));
        orderStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        ordersTable.setItems(visibleOrders);
    }

    private void refreshDashboardData() {
        loadUsers();
        loadBooks();
        loadOrders();
        updateStats();
    }

    private void loadUsers() {
        try {
            List<UserDetailsDTO> users = userService.getAllUserDetails();
            allUsers.setAll(users);
        } catch (SQLException e) {
            showInfo("Users Error", "Unable to load users.");
        }
    }

    private void loadBooks() {
        try {
            List<BookCardDTO> books = bookService.getAllBookDetails();
            allBooks.setAll(books);
            applyPersistedOrderReservations();
        } catch (SQLException e) {
            showInfo("Books Error", "Unable to load books.");
        }
    }

    private void loadOrders() {
        List<BookOrderDTO> orders = OrderRegistry.getAllOrders();
        allOrders.setAll(orders);
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

    private void updateStats() {
        totalUsersLabel.setText(String.valueOf(allUsers.size()));
        totalBooksLabel.setText(String.valueOf(allBooks.size()));

        int copies = 0;
        for (BookCardDTO book : allBooks) {
            copies += book.getAvailable_copies();
        }
        availableBooksLabel.setText(String.valueOf(copies));
        activeBorrowingsLabel.setText(String.valueOf(allOrders.size()));
    }

    private void populateBookForm(BookCardDTO book) {
        if (book == null) {
            return;
        }
        bookTitleField.setText(book.getTitle());
        bookAuthorField.setText(book.getAuthor());
        bookPublisherField.setText(book.getPublisher());
        bookYearField.setText(book.getPublished_year());
        bookCategoryField.setText(book.getCategory());
        bookCopiesField.setText(String.valueOf(book.getAvailable_copies()));
        bookImageField.setText(book.getImageLink());
    }

    private BookCardDTO buildBookFromForm() {
        String title = normalizeInput(bookTitleField.getText());
        String author = normalizeInput(bookAuthorField.getText());
        String publisher = normalizeInput(bookPublisherField.getText());
        String year = normalizeInput(bookYearField.getText());
        String category = normalizeInput(bookCategoryField.getText());
        String copiesRaw = normalizeInput(bookCopiesField.getText());
        String imageLink = normalizeInput(bookImageField.getText());

        if (title.isEmpty() || author.isEmpty()) {
            showInfo("Validation", "Title and author are required.");
            return null;
        }

        if (year.isEmpty()) {
            showInfo("Validation", "Published year is required.");
            return null;
        }
        if (!year.matches("\\d{4}")) {
            showInfo("Validation", "Published year must be a 4-digit value.");
            return null;
        }

        int copies;
        try {
            copies = Integer.parseInt(copiesRaw);
            if (copies < 0) {
                showInfo("Validation", "Available copies cannot be negative.");
                return null;
            }
        } catch (NumberFormatException e) {
            showInfo("Validation", "Available copies must be a number.");
            return null;
        }

        return new BookCardDTO(title, author, publisher, year, category, copies, imageLink);
    }

    private void clearBookForm() {
        bookTitleField.clear();
        bookAuthorField.clear();
        bookPublisherField.clear();
        bookYearField.clear();
        bookCategoryField.clear();
        bookCopiesField.clear();
        bookImageField.clear();
        booksTable.getSelectionModel().clearSelection();
    }

    private void showOverview() {
        showPane(overviewPane);
    }

    private void showModule(String title, String description, String buttonText, Runnable action) {
        moduleTitleLabel.setText(title);
        moduleDescriptionLabel.setText(description);
        modulePrimaryActionBtn.setText(buttonText);
        modulePrimaryAction = action;
        showPane(modulePane);
    }

    private void showPane(VBox paneToShow) {
        overviewPane.setVisible(false);
        overviewPane.setManaged(false);
        usersPane.setVisible(false);
        usersPane.setManaged(false);
        booksPane.setVisible(false);
        booksPane.setManaged(false);
        ordersPane.setVisible(false);
        ordersPane.setManaged(false);
        modulePane.setVisible(false);
        modulePane.setManaged(false);

        paneToShow.setManaged(true);
        paneToShow.setVisible(true);
    }

    private void addActivity(String message) {
        activityFeed.add(0, LocalDate.now() + "  •  " + message);
        if (activityFeed.size() > 20) {
            activityFeed.remove(activityFeed.size() - 1);
        }
    }

    private boolean contains(String raw, String query) {
        return normalize(raw).contains(query);
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.trim().toLowerCase(Locale.ROOT);
    }

    private String normalizeInput(String value) {
        if (value == null) {
            return "";
        }
        return value.trim();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
