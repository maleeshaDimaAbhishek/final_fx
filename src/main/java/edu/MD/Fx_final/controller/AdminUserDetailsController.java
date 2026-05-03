package edu.MD.Fx_final.controller;

import edu.MD.Fx_final.model.dto.UserDetailsDTO;
import edu.MD.Fx_final.service.UserService;
import edu.MD.Fx_final.service.impl.UserServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class AdminUserDetailsController {

    private final UserService userService = new UserServiceImpl();

    @FXML
    private TableView<UserDetailsDTO> tblUsers;
    @FXML
    private TableColumn<UserDetailsDTO, String> colNIC;
    @FXML
    private TableColumn<UserDetailsDTO, String> colName;
    @FXML
    private TableColumn<UserDetailsDTO, LocalDate> colDOB;
    @FXML
    private TableColumn<UserDetailsDTO, String> colEmail;
    @FXML
    private TableColumn<UserDetailsDTO, String> colPhone;
    @FXML
    private TableColumn<UserDetailsDTO, String> colAddress;

    @FXML
    private TextField txtUserId;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtNIC;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtPhone;
    @FXML
    private TextArea txtAddress;
    @FXML
    private DatePicker dpDOB;

    @FXML
    private TextField txtSearch;
    @FXML
    private Button btnSearch;
    @FXML
    private Button btnRefresh;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnBack;

    private final ObservableList<UserDetailsDTO> allUsersList = FXCollections.observableArrayList();
    private final ObservableList<UserDetailsDTO> filteredUsersList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTableColumns();
        setupTableSelectionListener();
        loadTable();
        tblUsers.setPlaceholder(new Label("No users found."));
    }

    private void setupTableColumns() {
        colNIC.setCellValueFactory(new PropertyValueFactory<>("NIC"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDOB.setCellValueFactory(new PropertyValueFactory<>("dob"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        tblUsers.setItems(filteredUsersList);
    }

    private void setupTableSelectionListener() {
        tblUsers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selectedUser) -> {
            if (selectedUser == null) {
                return;
            }
            txtName.setText(selectedUser.getName());
            txtNIC.setText(selectedUser.getNIC());
            txtEmail.setText(selectedUser.getEmail());
            txtPhone.setText(selectedUser.getPhoneNumber());
            txtAddress.setText(selectedUser.getAddress());
            dpDOB.setValue(selectedUser.getDob());
        });
    }

    public void loadTable() {
        allUsersList.clear();
        filteredUsersList.clear();
        try {
            List<UserDetailsDTO> userDetails = userService.getAllUserDetails();
            allUsersList.addAll(userDetails);
            filteredUsersList.addAll(userDetails);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load users from database.");
        }
    }

    @FXML
    private void handleSearch() {
        String searchText = txtSearch.getText().trim();
        if (searchText.isEmpty()) {
            filteredUsersList.setAll(allUsersList);
            return;
        }

        String query = searchText.toLowerCase();
        ObservableList<UserDetailsDTO> filtered = FXCollections.observableArrayList();
        for (UserDetailsDTO user : allUsersList) {
            if ((user.getNIC() != null && user.getNIC().toLowerCase().contains(query))
                    || (user.getName() != null && user.getName().toLowerCase().contains(query))
                    || (user.getEmail() != null && user.getEmail().toLowerCase().contains(query))) {
                filtered.add(user);
            }
        }
        filteredUsersList.setAll(filtered);
    }

    @FXML
    private void handleUpdate() {
        UserDetailsDTO selectedUser = tblUsers.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a user from the table to update.");
            return;
        }
        if (!validateFormFields()) {
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Update");
        confirmAlert.setHeaderText("Update User Information");
        confirmAlert.setContentText("Are you sure you want to update: " + selectedUser.getName() + "?");
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            updateUserInService();
        }
    }

    private void updateUserInService() {
        try {
            UserDetailsDTO updatedUser = new UserDetailsDTO(
                    txtNIC.getText().trim(),
                    txtName.getText().trim(),
                    dpDOB.getValue(),
                    txtEmail.getText().trim(),
                    txtPhone.getText().trim(),
                    txtAddress.getText().trim()
            );
            int updatedRows = userService.updateUser(updatedUser);
            if (updatedRows > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "User information updated successfully.");
                loadTable();
                handleClear(null);
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update user information.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred while updating user.");
        }
    }

    @FXML
    private void handleDelete() {
        UserDetailsDTO selectedUser = tblUsers.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a user from the table to delete.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Delete User");
        confirmAlert.setContentText("Are you sure you want to delete: " + selectedUser.getName() + "?");
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            deleteUserFromService(selectedUser);
        }
    }

    private void deleteUserFromService(UserDetailsDTO user) {
        try {
            int rowCount = userService.deleteUser(user.getNIC());
            if (rowCount > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "User deleted successfully.");
                loadTable();
                handleClear(null);
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete user.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred while deleting user.");
        }
    }

    @FXML
    private void handleRefresh() {
        txtSearch.clear();
        loadTable();
    }

    @FXML
    public void handleClear(ActionEvent actionEvent) {
        txtUserId.clear();
        txtName.clear();
        txtNIC.clear();
        txtEmail.clear();
        txtPhone.clear();
        txtAddress.clear();
        dpDOB.setValue(null);
        tblUsers.getSelectionModel().clearSelection();
    }

    @FXML
    public void handleBack(ActionEvent actionEvent) {
        AdminDashboardController.cUserstage.hide();
        OTPLoginController.adminDashBoard.show();
    }

    private boolean validateFormFields() {
        if (txtName.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Name field cannot be empty.");
            txtName.requestFocus();
            return false;
        }
        if (txtNIC.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "NIC field cannot be empty.");
            txtNIC.requestFocus();
            return false;
        }
        if (txtEmail.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Email field cannot be empty.");
            txtEmail.requestFocus();
            return false;
        }
        if (!txtEmail.getText().trim().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please enter a valid email address.");
            txtEmail.requestFocus();
            return false;
        }
        if (txtPhone.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Phone number field cannot be empty.");
            txtPhone.requestFocus();
            return false;
        }
        if (!txtPhone.getText().trim().matches("^[0-9]{10}$")) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Phone number must be 10 digits.");
            txtPhone.requestFocus();
            return false;
        }
        if (dpDOB.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please select a date of birth.");
            dpDOB.requestFocus();
            return false;
        }
        if (dpDOB.getValue().isAfter(LocalDate.now())) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Date of birth cannot be in the future.");
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
