package com.authenticate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.authenticate.LicenseValidator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

@Component
public class LoginController {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@FXML private TextField usernameField;
	@FXML private PasswordField passwordField;
	@FXML private Label messageLabel;
	@FXML private ImageView qrImageView;
	@FXML private Label qrImageViewLabel;
	@FXML
	public void handleLogin(ActionEvent event) {
		RegisterController registerController = null;
		
		String username = usernameField.getText();
		String password = passwordField.getText();
		String role = "";
		
		logger.info("username:==--->"+username);
		
		boolean isValid = true;
		try {
			Stage stage = (Stage) usernameField.getScene().getWindow();
			String validateLicense = LicenseValidator.validateLicense();
			logger.info("validateLicense:==--->"+validateLicense);
			logger.info("validate       :==--->"+(!validateLicense.trim().isEmpty()));
			
			if(!validateLicense.trim().isEmpty()) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
	            alert.setTitle("License Error");
	            alert.setHeaderText(null);
	            alert.setContentText(validateLicense);
	            alert.showAndWait();
	            
				messageLabel.setText(validateLicense);
				isValid = false;
				
			} else if (username.equals("super admin") && password.equals("admin")) {
				role = "super admin";
				isValid = true;
			} else if (username.equals("a") && password.equals("a")) {
				role = "admin"; // Or determine dynamically from credentials
				isValid = true;
			} else if (username.equals("manager") && password.equals("manager")) {
				role = "manager"; // Or determine dynamically from credentials
				isValid = true;
			} else if (username.equals("user") && password.equals("user")) {
				role = "manager";
				isValid = true;
			} else {
				isValid = false;
				messageLabel.setText("Invalid username or password.");
			}

			logger.info("isValid:--->"+isValid);
			
			if(isValid) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/totp.fxml"));
				Parent root = loader.load();
				TOTPController controller = loader.getController();
				controller.setUser("rameshnavnath@gmail.com");
				stage = new Stage();
				stage.setTitle("2FA Verification");
				stage.setScene(new Scene(root));
				stage.show();
				
				registerController = new RegisterController();
				String otpAuthURL = registerController.initialize();
				if(!otpAuthURL.trim().isEmpty()) {
					showTOTPQRCode(otpAuthURL);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
            logger.error("Error LoginController :-", e);
		}
	}

	@FXML
	public void handleCancel(ActionEvent event) {
		// Get the current window and close it
		Stage stage = (Stage) usernameField.getScene().getWindow();
		stage.close();
	}
	
	public void showTOTPQRCode(String qrUrl) {
	    try {
	       // BufferedImage qrImage  = QRCodeGenerator.generateQRCode(qrUrl, 250, 250);
	        //Image fxImage = SwingFXUtils.toFXImage(qrImage, null);
	    	qrImageView.setVisible(true);
	    	qrImageViewLabel.setVisible(true);
	    	
	    	Image fxImage = new Image(qrUrl);
	        qrImageView.setImage(fxImage);
	    } catch (Exception e) {
	    	logger.error("An error occurred", e);
	    }
	}
}
