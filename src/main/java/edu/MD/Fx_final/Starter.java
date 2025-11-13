package edu.MD.Fx_final;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Starter extends Application {
    public static void main(String[] args) {
        launch();
    }
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/Login.fxml"))));
        stage.show();
    }
}
