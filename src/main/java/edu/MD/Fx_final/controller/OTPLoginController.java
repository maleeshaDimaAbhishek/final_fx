package edu.MD.Fx_final.controller;

import javax.swing.*;
import java.util.Random;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;


public class OTPLoginController {
    int OTP=0;
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

    public void btnGetOTP(ActionEvent Event){
        if(Integer.parseInt(txtOtp.getText())==OTP){
            JOptionPane.showMessageDialog(
                    null,
                    "✅ OTP Verified Successfully!",
                    "OTP Status",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }else {
            txtOtp.setText("");
            JOptionPane.showMessageDialog(
                    null,
                    "❌ Incorrect OTP! Please try again.",
                    "OTP Status",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        System.out.println("Button clicked");
    }

}
