
package edu.MD.Fx_final.controller;

import edu.MD.Fx_final.Starter;
import edu.MD.Fx_final.model.dto.UserLoginDTO;
import edu.MD.Fx_final.service.UserService;
import edu.MD.Fx_final.service.impl.UserServiceImpl;
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
import java.io.IOException;
import java.sql.SQLException;

public class LoginController {
    UserService userService = new UserServiceImpl();
    OTPLoginController otpLoginController=new OTPLoginController();
    public static  Stage otpLogin ;
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
        UserLoginDTO userLoginDetails;
        boolean result = false;
        OTP = otpLoginController.generateOTP();
        boolean isCorrectOTP = false;
        try {
            userLoginDetails = userService.checkUserRole(txtUsername.getText());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (userLoginDetails != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/OTPLogin.fxml"));
            Parent root = loader.load();
            otpLoginController = loader.getController();
            otpLoginController.setOTP(OTP);
            otpLoginController.setRoleId(userLoginDetails.getRoleId());
            otpLogin = new Stage();
            otpLogin.setScene(new Scene(root));
            otpLogin.show();
            Starter.loginFormReference.hide();
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
        /*return EmailUtil.sendEmail(
                Email,
                "Welcome to Book Borrowing System 📚",
                "Hello "+name+",\n\nYour OTP is "+OTP+".\n\nThanks,\nTeam Library"
        );*/
        return true;
    }

    public void onRegisterLabelClick(MouseEvent mouseEvent) throws IOException {
        userRegistration.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/UserRegistration.fxml"))));
        userRegistration.show();
        Starter.loginFormReference.hide();
    }
}
