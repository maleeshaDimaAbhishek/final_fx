package edu.MD.Fx_final.controller;

import edu.MD.Fx_final.service.UserService;
import edu.MD.Fx_final.service.impl.UserServiceImpl;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class UserDetailsController {
    UserService userService=new UserServiceImpl();
    @FXML private TextField txtSearch;
    @FXML private TableView<?> tblUsers;
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
    }

    private void loadTable() {
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
}

