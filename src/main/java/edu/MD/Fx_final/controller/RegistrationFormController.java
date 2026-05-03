package edu.MD.Fx_final.controller;

import edu.MD.Fx_final.Starter;
import edu.MD.Fx_final.Utill.EmailUtil;
import edu.MD.Fx_final.model.dto.UserRegistrationDTO;
import edu.MD.Fx_final.service.UserService;
import edu.MD.Fx_final.service.impl.UserServiceImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ThreadLocalRandom;

public class RegistrationFormController {
    private final UserService userService = new UserServiceImpl();
    private int registrationOtp = 0;
    public static final Stage OTPDialogBox = new Stage();

    @FXML
    private TextField txtNIC;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtPhone;
    @FXML
    private TextField txtAddress;
    @FXML
    private DatePicker dpDOB;

    private String userMail;
    private String userName;

    @FXML
    public void onRegisterClick(ActionEvent actionEvent) throws IOException {
        if (!validateFields()) {
            showAlert(Alert.AlertType.ERROR, "Empty Fields Detected", "Please fill all fields.");
            return;
        }

        userMail = txtEmail.getText().trim();
        userName = txtName.getText().trim();
        if (!isValidEmail(userMail)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Email", "Please enter a valid email address.");
            return;
        }

        registrationOtp = generateOTP();
        if (!sendMail(userMail, registrationOtp, userName)) {
            showAlert(Alert.AlertType.ERROR, "Email Error", "Please check your internet connection or email and try again.");
            txtEmail.clear();
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/OTPDialogBox.fxml"));
        Scene scene = new Scene(loader.load());
        OTPDialogController controller = loader.getController();
        controller.setOtp(registrationOtp);
        controller.setOnSuccess(this::registerUserSafely);

        LoginController.userRegistration.hide();
        OTPDialogBox.setScene(scene);
        OTPDialogBox.show();
    }

    @FXML
    public void onBackClick(ActionEvent actionEvent) {
        Starter.loginFormReference.show();
        LoginController.userRegistration.hide();
    }

    public boolean sendMail(String email, int otp, String name) {
        return EmailUtil.sendEmail(
                email,
                "Welcome to Book Borrowing System",
                "Hello " + name + ",\n\nYour registration OTP is " + otp + ".\n\nThanks,\nTeam Library"
        );
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

    private boolean validateFields() {
        return !txtNIC.getText().isBlank()
                && !txtName.getText().isBlank()
                && !txtEmail.getText().isBlank()
                && !txtPhone.getText().isBlank()
                && !txtAddress.getText().isBlank()
                && dpDOB.getValue() != null;
    }

    public int generateOTP() {
        registrationOtp = ThreadLocalRandom.current().nextInt(100000, 1000000);
        return registrationOtp;
    }

    public void registerUser() throws SQLException {
        boolean isRegistered = userService.UserRegistration(
                new UserRegistrationDTO(
                        txtNIC.getText().trim(),
                        userName,
                        dpDOB.getValue(),
                        userMail,
                        txtPhone.getText().trim(),
                        txtAddress.getText().trim(),
                        10
                )
        );

        if (isRegistered) {
            Starter.loginFormReference.show();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Registration successful.");
            OTPDialogBox.hide();
        } else {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "Unable to register user.");
        }
    }

    private void registerUserSafely() {
        try {
            registerUser();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Unable to complete registration right now.");
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
