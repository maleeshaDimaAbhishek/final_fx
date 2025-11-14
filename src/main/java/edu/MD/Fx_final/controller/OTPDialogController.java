package edu.MD.Fx_final.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class OTPDialogController {

    @FXML
    private TextField txtOTP;

    private int registrationOtp;
    private Runnable onSuccess;

    public void setOtp(int otp) {
        this.registrationOtp = otp;
    }

    public void setOnSuccess(Runnable r) {
        this.onSuccess = r;
    }

    @FXML
    public void onVerifyClick(ActionEvent event) {
        if (txtOTP.getText() == null) return;

        if (registrationOtp == Integer.parseInt(txtOTP.getText())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("OTP Verified!");
            alert.show();

            if (onSuccess != null) onSuccess.run();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Invalid OTP!");
            alert.show();
        }
    }

    public void onCancelClick(ActionEvent actionEvent) {
        UserRegistrationFormController.OTPDialogBox.hide();
        LoginController.userRegistration.show();
    }
}

