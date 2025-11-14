
package edu.MD.Fx_final.controller;

import java.io.IOException;
import java.sql.SQLException;

import edu.MD.Fx_final.Starter;
import edu.MD.Fx_final.Utill.EmailUtil;
import edu.MD.Fx_final.model.UserLoginDetails;
import edu.MD.Fx_final.service.UserLoginService;
import edu.MD.Fx_final.service.UserLoginServiceImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.swing.*;

public class LoginController {
    UserLoginService userLoginService = new UserLoginServiceImpl();
    Stage otpLogin = new Stage();
    OTPLoginController otpLoginController=new OTPLoginController();
    public static Stage userRegistration=new Stage();
    int OTP;
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
    public void handleLoginAction(ActionEvent actionEvent) throws IOException {
        UserLoginDetails userLoginDetails;
        boolean result = false;
        OTP = otpLoginController.generateOTP();
        boolean isCorrectOTP = false;
        try {
            userLoginDetails = userLoginService.checkUserRole(txtUsername.getText());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (userLoginDetails != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/OTPLogin.fxml"));
            Parent root = loader.load();
            OTPLoginController otpController = loader.getController();
            otpController.setOTP(OTP);
            Stage otpLogin = new Stage();
            otpLogin.setScene(new Scene(root));
            otpLogin.show();
            result = sendMail(userLoginDetails.getMail(), OTP, userLoginDetails.getName());

            if (!result) {
                JOptionPane.showMessageDialog(
                        null,
                        "❌ Connection Error! Please try again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } else {
            txtUsername.setText("");
            JOptionPane.showMessageDialog(
                    null,
                    "❌ Incorrect User ID.Try Again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
    public boolean sendMail(String Email,int OTP,String name){
        return EmailUtil.sendEmail(
                Email,
                "Welcome to Book Borrowing System 📚",
                "Hello "+name+",\n\nYour OTP is "+OTP+".\n\nThanks,\nTeam Library"
        );
    }

    public void onRegisterLabelClick(MouseEvent mouseEvent) throws IOException {
        userRegistration.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/UserRegistration.fxml"))));
        userRegistration.show();
        Starter.loginFormReference.hide();
    }
}
