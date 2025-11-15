package edu.MD.Fx_final.controller;

import edu.MD.Fx_final.Starter;
import edu.MD.Fx_final.Utill.EmailUtil;
import edu.MD.Fx_final.model.UserRegistrationDetails;
import edu.MD.Fx_final.service.UserRegistrationService;
import edu.MD.Fx_final.service.UserRegistrationServiceImpl;
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
import java.util.Random;

public class UserRegistrationFormController {
    UserRegistrationService userRegistrationService=new UserRegistrationServiceImpl();
    int registrationOtp=0;
    public static Stage OTPDialogBox=new Stage();

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

    String userMail;
    String userName;
    private ActionEvent actionEvent1;

    public void onRegisterClick(ActionEvent actionEvent) throws IOException {
        if(validateFields()){
            userMail=txtEmail.getText();
            userName=txtName.getText();
            if (isValidEmail(userMail)){
                registrationOtp=generateOTP();
                if(sendMail(userMail,registrationOtp,userName)){
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/OTPDialogBox.fxml"));
                    Scene scene = new Scene(loader.load());
                    OTPDialogController controller = loader.getController();
                    controller.setOtp(registrationOtp);
                    controller.setOnSuccess(() -> {
                        try {
                            registerUser();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    LoginController.userRegistration.hide();
                    OTPDialogBox.setScene(scene);
                    OTPDialogBox.show();

                }else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Email or Connection Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Please Check Your Internet Connection or Mail.");
                    alert.show();
                    txtEmail.setText("");
                }
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Email");
                alert.setHeaderText(null);
                alert.setContentText("Please Enter Valid Email");
                alert.show();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Empty Filed Detected");
            alert.setHeaderText(null);
            alert.setContentText("Please Fill All The Field");
            alert.show();
        }
//        Starter.loginFormReference.show();
//        LoginController.userRegistration.hide();
    }

    public void onBackClick(ActionEvent actionEvent) {
        Starter.loginFormReference.show();
        LoginController.userRegistration.hide();
    }
    //send mail to customer
    public boolean sendMail(String Email,int OTP,String name){
        return EmailUtil.sendEmail(
                Email,
                "Welcome to Book Borrowing System 📚",
                "Hello "+name+",\n\nYour Registration 0OTP is "+OTP+".\n\nThanks,\nTeam Library"
        );

    }
    //Email validation
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }
    //Validate all fields are fill or not
    private boolean validateFields() {
        return  !txtNIC.getText().isEmpty() &&
                !txtName.getText().isEmpty() &&
                !txtEmail.getText().isEmpty() &&
                !txtPhone.getText().isEmpty() &&
                !txtAddress.getText().isEmpty() &&
                dpDOB.getValue() != null;
    }
    //OTP generator
    public  int generateOTP() {
        Random random = new Random();
        registrationOtp = 100000 + random.nextInt(900000);
        return registrationOtp;
    }

    public void registerUser() throws SQLException {
        if (userRegistrationService.UserRegisration(
                new UserRegistrationDetails(
                        txtNIC.getText(),
                        userName,
                        dpDOB.getValue(),
                        userMail,
                        txtPhone.getText(),
                        txtAddress.getText()
                )
        )) {
            Starter.loginFormReference.show();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Registration Successful!");
            alert.show();
            OTPDialogBox.hide();
        }

    }

}
