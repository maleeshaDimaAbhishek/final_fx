package edu.MD.Fx_final.controller;

import edu.MD.Fx_final.model.dto.UserDetailsDTO;
import edu.MD.Fx_final.service.UserService;
import edu.MD.Fx_final.service.impl.UserServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class AdminStaffDetailsController {

    private final UserService userService = new UserServiceImpl();

    // Table and Columns
    @FXML private TableView<UserDetailsDTO> tblUsers;
    @FXML private TableColumn<UserDetailsDTO, String> colNIC;
    @FXML private TableColumn<UserDetailsDTO, String> colName;
    @FXML private TableColumn<UserDetailsDTO, LocalDate> colDOB;
    @FXML private TableColumn<UserDetailsDTO, String> colEmail;
    @FXML private TableColumn<UserDetailsDTO, String> colPhone;
    @FXML private TableColumn<UserDetailsDTO, String> colAddress;

    // Fields
    @FXML private TextField txtUserId;
    @FXML private TextField txtName;
    @FXML private TextField txtNIC;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhone;
    @FXML private TextArea txtAddress;
    @FXML private DatePicker dpDOB;

    // Search and Buttons
    @FXML private TextField txtSearch;
    @FXML private Button btnSearch;
    @FXML private Button btnRefresh;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnClear;
    @FXML private Button btnBack;

    // Data Lists
    private final ObservableList<UserDetailsDTO> allUsersList = FXCollections.observableArrayList();
    private ObservableList<UserDetailsDTO> filteredUsersList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Setup table columns
        setupTableColumns();

        // Setup table selection listener for auto-fill
        setupTableSelectionListener();

        // Load initial data
        loadTable();

        // Set placeholder for empty table
        tblUsers.setPlaceholder(new Label("No users found. Click Refresh to load data."));

    }
    //set table column
    private void setupTableColumns() {
        colNIC.setCellValueFactory(new PropertyValueFactory<>("NIC"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDOB.setCellValueFactory(new PropertyValueFactory<>("dob"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));

        tblUsers.setItems(filteredUsersList);
    }

    //autofill table
    private void setupTableSelectionListener() {
        tblUsers.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        txtName.setText(newValue.getName());
                        txtNIC.setText(newValue.getNIC());
                        txtEmail.setText(newValue.getEmail());
                        txtPhone.setText(newValue.getPhoneNumber());
                        txtAddress.setText(newValue.getAddress());
                        dpDOB.setValue(newValue.getDob());
                    }
                }
        );
    }

    public void loadTable() {
        allUsersList.clear();
        filteredUsersList.clear();

        try {
            List<UserDetailsDTO> userDetailsDTOS = userService.getAllStaffDetails();
            allUsersList.addAll(userDetailsDTOS);
            filteredUsersList.addAll(userDetailsDTOS);

            if (userDetailsDTOS.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "No Data", "No users found in the database.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error",
                    "Failed to load users from database: " + e.getMessage());
        }
    }

    /**
     * Search users by NIC, Name, or Email
     */
    @FXML
    private void handleSearch() {
        String searchText = txtSearch.getText().trim();

        if (searchText.isEmpty()) {
            filteredUsersList.setAll(allUsersList);
            return;
        }

        String lowerCaseSearch = searchText.toLowerCase();
        ObservableList<UserDetailsDTO> filtered = FXCollections.observableArrayList();

        for (UserDetailsDTO user : allUsersList) {
            if ((user.getNIC() != null && user.getNIC().toLowerCase().contains(lowerCaseSearch)) ||
                    (user.getName() != null && user.getName().toLowerCase().contains(lowerCaseSearch)) ||
                    (user.getEmail() != null && user.getEmail().toLowerCase().contains(lowerCaseSearch))) {
                filtered.add(user);
            }
        }

        filteredUsersList.setAll(filtered);

        if (filtered.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "No Results",
                    "No users found matching: '" + searchText + "'");
        }
    }

    /**
     * Update selected user's information
     */
    @FXML
    private void handleUpdate() {
        UserDetailsDTO selectedUser = tblUsers.getSelectionModel().getSelectedItem();

        if (selectedUser == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "Please select a user from the table to update.");
            return;
        }

        // Validate form fields
        if (!validateFormFields()) {
            return;
        }

        // Confirm update
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Update");
        confirmAlert.setHeaderText("Update User Information");
        confirmAlert.setContentText("Are you sure you want to update the information for: " +
                selectedUser.getName() + "?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            updateUserInService();
        }
    }

    /**
     * Update user using service layer
     */
    private void updateUserInService() {
        try {
            // Create updated DTO with form data
            UserDetailsDTO updatedUser = new UserDetailsDTO();
            updatedUser.setName(txtName.getText().trim());
            updatedUser.setNIC(txtNIC.getText().trim());
            updatedUser.setEmail(txtEmail.getText().trim());
            updatedUser.setPhoneNumber(txtPhone.getText().trim());
            updatedUser.setAddress(txtAddress.getText().trim());
            updatedUser.setDob(dpDOB.getValue());

            // Call service to update
            int success_count = userService.updateStaff(updatedUser);

            if (success_count!=0) {
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "User information updated successfully.");
                loadTable(); // Refresh table
                handleClear(null); // Clear form
            } else {
                showAlert(Alert.AlertType.ERROR, "Error",
                        "Failed to update user information.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error",
                    "An unexpected error occurred: " + e.getMessage());
        }
    }

    /**
     * Delete selected user
     */
    @FXML
    private void handleDelete() {
        UserDetailsDTO selectedUser = tblUsers.getSelectionModel().getSelectedItem();

        if (selectedUser == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "Please select a user from the table to delete.");
            return;
        }

        // Confirm deletion
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Delete User");
        confirmAlert.setContentText("Are you sure you want to delete user: " +
                selectedUser.getName() + "?\n\nThis action cannot be undone.");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            deleteUserFromService(selectedUser);
        }
    }

    /**
     * Delete user using service layer
     */
    private void deleteUserFromService(UserDetailsDTO user) {
        try {
            int rowCount = userService.deleteStaff(user.getNIC());

            if (rowCount!=0) {
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "User deleted successfully.");
                loadTable(); // Refresh table
                handleClear(null); // Clear form
            } else {
                showAlert(Alert.AlertType.ERROR, "Error",
                        "Failed to delete user.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error",
                    "An unexpected error occurred: " + e.getMessage());
        }
    }

    /**
     * Refresh table data
     */
    @FXML
    private void handleRefresh() {
        loadTable();
        txtSearch.clear();
        showAlert(Alert.AlertType.INFORMATION, "Refreshed",
                "User data has been refreshed successfully.");
    }

    //clear all fields
    @FXML
    public void handleClear(ActionEvent actionEvent) {
        txtName.clear();
        txtNIC.clear();
        txtEmail.clear();
        txtPhone.clear();
        txtAddress.clear();
        dpDOB.setValue(null);

        // Clear table selection
        tblUsers.getSelectionModel().clearSelection();

        System.out.println("Form cleared.");
    }

    /**
     * Navigate back to Admin Dashboard
     */
    @FXML
    public void handleBack(ActionEvent actionEvent) {
        try {
            // Load the admin dashboard
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AdminDashBoard.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Admin Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error",
                    "Failed to return to dashboard: " + e.getMessage());
        }
    }

    /**
     * Validate all form fields before update
     */
    private boolean validateFormFields() {
        if (txtName.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Name field cannot be empty.");
            txtName.requestFocus();
            return false;
        }

        if (txtNIC.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "NIC field cannot be empty.");
            txtNIC.requestFocus();
            return false;
        }

        if (txtEmail.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Email field cannot be empty.");
            txtEmail.requestFocus();
            return false;
        }

        // Basic email validation
        if (!txtEmail.getText().trim().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Please enter a valid email address.");
            txtEmail.requestFocus();
            return false;
        }

        if (txtPhone.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Phone number field cannot be empty.");
            txtPhone.requestFocus();
            return false;
        }

        // Basic phone validation (numbers only, 10 digits)
        if (!txtPhone.getText().trim().matches("^[0-9]{10}$")) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Phone number must be 10 digits.");
            txtPhone.requestFocus();
            return false;
        }

        if (dpDOB.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Please select a date of birth.");
            dpDOB.requestFocus();
            return false;
        }

        // Check if DOB is not in the future
        if (dpDOB.getValue().isAfter(LocalDate.now())) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Date of birth cannot be in the future.");
            dpDOB.requestFocus();
            return false;
        }

        return true;
    }


    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}