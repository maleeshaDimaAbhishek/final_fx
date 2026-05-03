package edu.MD.Fx_final.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import lombok.Setter;

public class OTPDialogController {

    @FXML
    private TextField txtOTP;

    private int registrationOtp;
    @Setter
    private Runnable onSuccess;

    public void setOtp(int otp) {
        this.registrationOtp = otp;
    }
    @FXML
    public void onVerifyClick(ActionEvent event) {
        String value = txtOTP.getText() == null ? "" : txtOTP.getText().trim();
        if (value.isEmpty()) {
            showInvalidOtpAlert();
            return;
        }

        try {
            if (registrationOtp == Integer.parseInt(value)) {
                if (onSuccess != null) {
                    onSuccess.run();
                }
            } else {
                showInvalidOtpAlert();
            }
        } catch (NumberFormatException e) {
            showInvalidOtpAlert();
        }
    }

    private void showInvalidOtpAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText("Invalid OTP!");
        alert.show();
    }

    public void onCancelClick(ActionEvent actionEvent) {
        RegistrationFormController.OTPDialogBox.hide();
        LoginController.userRegistration.show();
    }
}
