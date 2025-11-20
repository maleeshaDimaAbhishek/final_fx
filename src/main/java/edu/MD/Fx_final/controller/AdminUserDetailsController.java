package edu.MD.Fx_final.controller;

import edu.MD.Fx_final.model.dto.UserDetailsDTO;
import edu.MD.Fx_final.service.UserService;
import edu.MD.Fx_final.service.impl.UserServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminUserDetailsController {
    UserService userService=new UserServiceImpl();
    @FXML private TextField txtSearch;
    @FXML private TableView<UserDetailsDTO> tblUsers;
    @FXML private TableColumn<?, ?> colNIC;
    @FXML private TableColumn<?, ?> colName;
    @FXML private TableColumn<?, ?> colDOB;
    @FXML private TableColumn<?, ?> colEmail;
    @FXML private TableColumn<?, ?> colPhone;
    @FXML private TableColumn<?, ?> colAddress;
    @FXML private TableColumn<?, ?> colRole;

    @FXML
    public void initialize() {
        loadTable();
        System.out.println("colNIC = " + colNIC);

    }

    public void loadTable() {
        List<UserDetailsDTO> userDetailsDTOS=new ArrayList<>();
        try {
            userDetailsDTOS=userService.getAllUserDetails();
            ObservableList<UserDetailsDTO> observableList = FXCollections.observableArrayList(userDetailsDTOS);
            colNIC.setCellValueFactory(new PropertyValueFactory<>("NIC"));
            colName.setCellValueFactory(new PropertyValueFactory<>("name"));
            colDOB.setCellValueFactory(new PropertyValueFactory<>("dob"));
            colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
            colPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
            colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));

            // the observableList from earlier:
            tblUsers.setItems(observableList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // TODO: load data from DB using service
    }

    @FXML
    private void handleSearch() {
        // TODO: implement searching logic
    }

    @FXML
    private void handleUpdate() {
        // TODO: open update dialog
    }

    @FXML
    private void handleDelete() {
        // TODO: delete selected user
    }

    @FXML
    private void handleRefresh() {
        loadTable();
    }

    public void handleBack(ActionEvent actionEvent) {

    }

    public void handleClear(ActionEvent actionEvent) {

    }
}

