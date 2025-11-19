package edu.MD.Fx_final.controller;

import edu.MD.Fx_final.Starter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.util.Random;


public class OTPLoginController {
    int OTP=0;
    Stage userDashBoard=new Stage();
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

    public void initialize() {
        bookImage.setImage(new Image("/image/library.png"));
        libraryImage.setImage(new Image("/image/openbook.jpeg"));
    }
    public  int generateOTP() {
        Random random = new Random();
         OTP = 100000 + random.nextInt(900000);
        return OTP;
    }

    public void setOTP(int otp) {
        OTP = otp;
        System.out.println("Received OTP: " + otp);
    }

    public void btnGetOTP(ActionEvent Event) throws IOException {
        String userOtp=txtOtp.getText();
        try {
            if(Integer.parseInt(txtOtp.getText())==OTP){
                JOptionPane.showMessageDialog(
                        null,
                        "✅ OTP Verified Successfully!",
                        "OTP Status",
                        JOptionPane.INFORMATION_MESSAGE
                );
                LoginController.otpLogin.hide();
                userDashBoard.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/UserDashBoardForm.fxml"))));
                userDashBoard.show();

            }else {
                txtOtp.setText("");
                JOptionPane.showMessageDialog(
                        null,
                        "❌ Incorrect OTP! Please try again.",
                        "OTP Status",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "❌ Incorrect OTP! Please Enter Valid One.",
                    "OTP Status",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public void onActionDidnotgetOTP(MouseEvent mouseEvent) {
        JOptionPane.showMessageDialog(
                null,
                "✅ Check Your Mail !",
                "OTP Status",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public void onActionBack(ActionEvent actionEvent) {
        Starter.loginFormReference.show();
    }
}
