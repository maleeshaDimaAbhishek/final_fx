package edu.MD.Fx_final.controller;

import edu.MD.Fx_final.Starter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.IOException;

public class OTPLoginController {
    private int otp = 0;
    private String loggedInUserNic = "UNKNOWN";
    private String loggedInUserName = "User";

    @Setter
    private int roleId;

    public static final Stage userDashBoard = new Stage();
    public static final Stage adminDashBoard = new Stage();
    public static final Stage staffDashBoard = new Stage();

    @FXML
    private ImageView bookImage;
    @FXML
    private Button btnLogin;
    @FXML
    private StackPane leftPane;
    @FXML
    private ImageView libraryImage;
    @FXML
    private StackPane rightPane;
    @FXML
    private TextField txtOtp;

    @FXML
    public void initialize() {
        bookImage.setImage(new Image("/image/library.png"));
        libraryImage.setImage(new Image("/image/openbook.jpeg"));
    }

    public void setOTP(int otp) {
        this.otp = otp;
    }

    public void setLoggedInUser(String userNic, String userName) {
        if (userNic != null && !userNic.isBlank()) {
            this.loggedInUserNic = userNic;
        }
        if (userName != null && !userName.isBlank()) {
            this.loggedInUserName = userName;
        }
    }

    @FXML
    public void btnGetOTP(ActionEvent event) throws IOException {
        String rawOtp = txtOtp.getText() == null ? "" : txtOtp.getText().trim();
        if (rawOtp.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "OTP Status", "Please enter the OTP.");
            return;
        }

        int userOtp;
        try {
            userOtp = Integer.parseInt(rawOtp);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "OTP Status", "Incorrect OTP! Please enter a valid OTP.");
            txtOtp.clear();
            return;
        }

        if (userOtp != otp) {
            txtOtp.clear();
            showAlert(Alert.AlertType.ERROR, "OTP Status", "Incorrect OTP! Please try again.");
            return;
        }

        LoginController.otpLogin.hide();
        switch (roleId) {
            case 10:
                FXMLLoader userLoader = new FXMLLoader(getClass().getResource("/view/UserDashBoardForm.fxml"));
                Scene userScene = new Scene(userLoader.load());
                UserDashBoardFormController userController = userLoader.getController();
                userController.setLoggedInUser(loggedInUserNic, loggedInUserName);
                userDashBoard.setScene(userScene);
                userDashBoard.show();
                break;
            case 11:
                adminDashBoard.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/AdminDashBoard.fxml"))));
                adminDashBoard.show();
                break;
            default:
                staffDashBoard.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/StaffDashBoard.fxml"))));
                staffDashBoard.show();
                break;
        }
    }

    @FXML
    public void onActionDidnotgetOTP(MouseEvent mouseEvent) {
        showAlert(Alert.AlertType.INFORMATION, "OTP Status", "Check your mail for the OTP.");
    }

    @FXML
    public void onActionBack(ActionEvent actionEvent) {
        LoginController.otpLogin.hide();
        Starter.loginFormReference.show();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
