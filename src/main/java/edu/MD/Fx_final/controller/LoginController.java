package edu.MD.Fx_final.controller;

import edu.MD.Fx_final.Starter;
import edu.MD.Fx_final.Utill.EmailUtil;
import edu.MD.Fx_final.model.dto.UserLoginDTO;
import edu.MD.Fx_final.service.UserService;
import edu.MD.Fx_final.service.impl.UserServiceImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ThreadLocalRandom;

public class LoginController {
    private final UserService userService = new UserServiceImpl();

    public static Stage otpLogin;
    public static final Stage userRegistration = new Stage();

    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private ImageView libraryImage;
    @FXML
    private ImageView bookImage;

    @FXML
    public void initialize() {
        bookImage.setImage(new Image("/image/library.png"));
        libraryImage.setImage(new Image("/image/openbook.jpeg"));
    }

    @FXML
    public void handleLoginAction(ActionEvent actionEvent) throws IOException {
        String username = txtUsername.getText() == null ? "" : txtUsername.getText().trim();
        if (username.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter your user ID.");
            return;
        }

        UserLoginDTO userLoginDetails;
        try {
            userLoginDetails = userService.checkUserRole(username);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Unable to validate user ID right now.");
            return;
        }

        if (userLoginDetails == null) {
            txtUsername.clear();
            txtPassword.clear();
            showAlert(Alert.AlertType.ERROR, "Error", "Incorrect User ID. Try again.");
            return;
        }

        int otp = generateOtp();
        boolean emailSent = sendMail(userLoginDetails.getMail(), otp, userLoginDetails.getName());
        if (!emailSent) {
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to send OTP. Please check your connection and try again.");
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/OTPLogin.fxml"));
        Parent root = loader.load();
        OTPLoginController otpLoginController = loader.getController();
        otpLoginController.setOTP(otp);
        otpLoginController.setRoleId(userLoginDetails.getRoleId());
        otpLoginController.setLoggedInUser(username, userLoginDetails.getName());

        otpLogin = new Stage();
        otpLogin.setScene(new Scene(root));
        otpLogin.show();
        Starter.loginFormReference.hide();
    }

    public boolean sendMail(String email, int otp, String name) {
        // return EmailUtil.sendEmail(
        //         email,
        //         "Welcome to Book Borrowing System",
        //         "Hello " + name + ",\n\nYour OTP is " + otp + ".\n\nThanks,\nTeam Library"
        // );
        System.out.println("[TEST OTP][LOGIN] Email: " + email + " | OTP: " + otp);
        return true;
    }

    @FXML
    public void onRegisterLabelClick(MouseEvent mouseEvent) throws IOException {
        userRegistration.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/UserRegistration.fxml"))));
        userRegistration.show();
        Starter.loginFormReference.hide();
    }

    private int generateOtp() {
        return ThreadLocalRandom.current().nextInt(100000, 1000000);
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
