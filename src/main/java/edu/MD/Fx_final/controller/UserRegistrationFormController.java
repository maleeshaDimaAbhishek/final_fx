package edu.MD.Fx_final.controller;

import edu.MD.Fx_final.Starter;
import edu.MD.Fx_final.Utill.EmailUtil;
import edu.MD.Fx_final.model.UserRegistrationDetails;
import edu.MD.Fx_final.service.UserRegistrationService;
import edu.MD.Fx_final.service.UserRegistrationServiceImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.util.Random;

public class UserRegistrationFormController {
    UserRegistrationService userRegistrationService=new UserRegistrationServiceImpl();
    int registrationOtp=0;
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

    public void onRegisterClick(ActionEvent actionEvent) {
        String userMail=txtEmail.getText();
        String userName=txtName.getText();
        if(validateFields()){
            if (isValidEmail(txtEmail.getText())){
                if(sendMail(userMail,registrationOtp,userName)){
                    try {
                        boolean b=userRegistrationService.UserRegisration(
                                new UserRegistrationDetails(
                                        txtNIC.getText(),
                                        userName,
                                        dpDOB.getValue(),
                                        userMail,
                                        txtPhone.getText(),
                                        txtAddress.getText()
                                )
                        );
                        System.out.println("registe");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
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

}
