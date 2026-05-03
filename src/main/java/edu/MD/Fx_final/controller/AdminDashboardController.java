package edu.MD.Fx_final.controller;

import edu.MD.Fx_final.Starter;
import edu.MD.Fx_final.model.dto.BookCardDTO;
import edu.MD.Fx_final.model.dto.UserDetailsDTO;
import edu.MD.Fx_final.service.BookService;
import edu.MD.Fx_final.service.UserService;
import edu.MD.Fx_final.service.impl.BookServiceImpl;
import edu.MD.Fx_final.service.impl.UserServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AdminDashboardController {
    public static final Stage cUserstage = new Stage();
    private static final String MODULE_NOT_AVAILABLE = "This module is not available in this version yet.";

    private final UserService userService = new UserServiceImpl();
    private final BookService bookService = new BookServiceImpl();

    private final ObservableList<String> activityFeed = FXCollections.observableArrayList();
    private final ObservableList<BookCardDTO> books = FXCollections.observableArrayList();

    private Runnable modulePrimaryAction;

    @FXML
    private Label dashboardDateLabel;
    @FXML
    private Label totalUsersLabel;
    @FXML
    private Label totalStaffLabel;
    @FXML
    private Label totalBooksLabel;
    @FXML
    private Label activeBorrowingsLabel;
    @FXML
    private ListView<String> recentActivityList;

    @FXML
    private VBox overviewPane;
    @FXML
    private VBox booksPane;
    @FXML
    private VBox modulePane;

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
    private Label moduleTitleLabel;
    @FXML
    private Label moduleDescriptionLabel;
    @FXML
    private Button modulePrimaryActionBtn;

    @FXML
    public void initialize() {
        dashboardDateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
        setupBooksTable();
        recentActivityList.setItems(activityFeed);
        showOverview();
        refreshDashboardStats();
        addActivity("Dashboard initialized");
    }

    @FXML
    public void handleOverviewAction(ActionEvent actionEvent) {
        showOverview();
        addActivity("Switched to overview");
    }

    @FXML
    public void handleRefreshDashboardStats(ActionEvent actionEvent) {
        refreshDashboardStats();
        addActivity("Stats refreshed");
    }

    @FXML
    public void handleLogoutAction(ActionEvent actionEvent) {
        OTPLoginController.adminDashBoard.close();
        Starter.loginFormReference.show();
    }

    @FXML
    public void handleViewUserOnAction(ActionEvent event) {
        addActivity("Opened user records");
        openDetailsView("/view/UserDetails.fxml", "User Details");
    }

    @FXML
    public void handleViewStaffAction(ActionEvent actionEvent) {
        addActivity("Opened staff records");
        openDetailsView("/view/StaffDetails.fxml", "Staff Details");
    }

    @FXML
    public void handleViewPaymentOnAction(ActionEvent actionEvent) {
        showModule(
                "Payments Module",
                "Monitor payment transactions, overdue fines, and collection trends from one place.",
                "Open Payments",
                () -> showInfo("Payments", MODULE_NOT_AVAILABLE)
        );
        addActivity("Opened payments panel");
    }

    @FXML
    public void handleViewBorrowingsOnAction(ActionEvent actionEvent) {
        showModule(
                "Borrowings Module",
                "Track active borrowings, due dates, and pending returns with real-time status updates.",
                "Open Borrowings",
                () -> showInfo("Borrowings", MODULE_NOT_AVAILABLE)
        );
        addActivity("Opened borrowings panel");
    }

    @FXML
    public void handleViewBooksOnAction(ActionEvent actionEvent) {
        loadBooks();
        showPane(booksPane);
        addActivity("Viewed books catalog");
    }

    @FXML
    public void handleRegisterStaffOnAction(ActionEvent actionEvent) {
        showModule(
                "Register Staff",
                "Use this action to onboard new staff accounts with role-based access.",
                "Start Registration",
                () -> showInfo("Register Staff", "Staff registration workflow will be available in the next update.")
        );
        addActivity("Opened staff registration panel");
    }

    @FXML
    public void handleManageUserOnAction(ActionEvent actionEvent) {
        handleViewUserOnAction(actionEvent);
    }

    @FXML
    public void handleManageStaffOnAction(ActionEvent actionEvent) {
        handleViewStaffAction(actionEvent);
    }

    @FXML
    public void handleManageBooksOnAction(ActionEvent actionEvent) {
        handleViewBooksOnAction(actionEvent);
    }

    @FXML
    public void handleModulePrimaryAction(ActionEvent actionEvent) {
        if (modulePrimaryAction != null) {
            modulePrimaryAction.run();
        }
    }

    private void setupBooksTable() {
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        yearCol.setCellValueFactory(new PropertyValueFactory<>("published_year"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        copiesCol.setCellValueFactory(new PropertyValueFactory<>("available_copies"));
        booksTable.setItems(books);
    }

    private void refreshDashboardStats() {
        try {
            List<UserDetailsDTO> users = userService.getAllUserDetails();
            List<UserDetailsDTO> staff = userService.getAllStaffDetails();
            List<BookCardDTO> bookCards = bookService.getAllBookDetails();

            totalUsersLabel.setText(String.valueOf(users.size()));
            totalStaffLabel.setText(String.valueOf(staff.size()));
            totalBooksLabel.setText(String.valueOf(bookCards.size()));
            activeBorrowingsLabel.setText("N/A");
        } catch (SQLException e) {
            showInfo("Stats Error", "Unable to load dashboard stats right now.");
        }
    }

    private void loadBooks() {
        try {
            books.setAll(bookService.getAllBookDetails());
            if (books.isEmpty()) {
                showInfo("Books", "No books found in the catalog.");
            }
        } catch (SQLException e) {
            showInfo("Books Error", "Unable to load books right now.");
        }
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
        booksPane.setVisible(false);
        booksPane.setManaged(false);
        modulePane.setVisible(false);
        modulePane.setManaged(false);

        paneToShow.setManaged(true);
        paneToShow.setVisible(true);
    }

    private void openDetailsView(String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            OTPLoginController.adminDashBoard.hide();
            cUserstage.setScene(new Scene(root));
            cUserstage.setTitle(title);
            cUserstage.show();
        } catch (IOException e) {
            showInfo("Navigation Error", "Unable to open " + title + ".");
        }
    }

    private void addActivity(String message) {
        activityFeed.add(0, LocalDate.now() + "  •  " + message);
        if (activityFeed.size() > 20) {
            activityFeed.remove(activityFeed.size() - 1);
        }
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
