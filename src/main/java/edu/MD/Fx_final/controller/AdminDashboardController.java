package edu.MD.Fx_final.controller;

import edu.MD.Fx_final.Starter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminDashboardController {
    AdminUserDetailsController userDetailsController=new AdminUserDetailsController();
    public static Stage cUserstage;

    public void handleLogoutAction(ActionEvent actionEvent) {
        OTPLoginController.adminDashBoard.hide();
        Starter.loginFormReference.show();
    }

    @FXML
    public void handleViewUserOnAction(ActionEvent event) {
        try {
            // 1. Load the FXML file (This links the FXML to the Controller)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UserDetails.fxml"));
            Parent root = loader.load();

            // 2. Create the cUserstage and show it
            cUserstage = new Stage();
            cUserstage.setScene(new Scene(root));
            cUserstage.setTitle("User Details");
            cUserstage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleViewStaffAction(ActionEvent actionEvent) {
        try {
            // 1. Load the FXML file (This links the FXML to the Controller)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/StaffDetails.fxml"));
            Parent root = loader.load();

            // 2. Create the cUserstage and show it
            cUserstage = new Stage();
            cUserstage.setScene(new Scene(root));
            cUserstage.setTitle("User Details");
            cUserstage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleViewPaymentOnAction(ActionEvent actionEvent) {
    }

    public void handleViewBorrowingsOnAction(ActionEvent actionEvent) {
    }

    public void handleViewBooksOnAction(ActionEvent actionEvent) {
    }

    public void handleRegisterStaffOnAction(ActionEvent actionEvent) {
    }

    public void handleManageUserOnAction(ActionEvent actionEvent) {
    }

    public void handleManageStaffOnAction(ActionEvent actionEvent) {
    }

    public void handleManageBooksOnAction(ActionEvent actionEvent) {
    }
}
