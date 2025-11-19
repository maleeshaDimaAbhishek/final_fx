package edu.MD.Fx_final;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Starter extends Application {
    public static Stage loginFormReference;
    public static void main(String[] args) {
        launch();
    }
    public void start(Stage stage) throws Exception {
        loginFormReference=stage;
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/AdminDashBoard.fxml"))));
        stage.show();
    }
}
