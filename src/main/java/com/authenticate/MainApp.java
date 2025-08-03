package com.authenticate;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class MainApp extends Application {

    private AnnotationConfigApplicationContext context;
    
    @Override
    public void init() {
        context = new AnnotationConfigApplicationContext(AppConfig.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
        	LicenseKeyGenerator.generatorKey();
        	
        } catch (Exception e) {
            System.err.println("License Error: " + e.getMessage());
            Platform.exit();
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        loader.setControllerFactory(context::getBean);
        primaryStage.setScene(new Scene(loader.load()));
        primaryStage.setTitle("Spring Core Login");
        primaryStage.show();
        
        String validateLicense = LicenseValidator.validateLicense();
        
        if(!validateLicense.trim().isEmpty()) {
        	Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("License Error");
            alert.setHeaderText(null);
            alert.setContentText(validateLicense);
            alert.showAndWait();
        }
    }

    @Override
    public void stop() {
        context.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
