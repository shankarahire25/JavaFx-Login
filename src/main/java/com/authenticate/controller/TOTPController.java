package com.authenticate.controller;

import com.authenticate.service.TOTPLoginService;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TOTPController {

	@FXML private TextField codeField;
	@FXML private Label errorLabel;

	private String user;

	public void setUser(String user) {
		this.user = user;
	}

	@FXML
	private void onVerify() {
		try {

			if (codeField.getText().trim().isEmpty()) {
				errorLabel.setText("Please enter the code.");
			} else {
				int code = Integer.parseInt(codeField.getText().trim());
				System.out.println("code    :--->" + code);
				System.out.println("username:--->" + user);

				boolean isValidCode = TOTPLoginService.verifyCode(user, code); 
				if (isValidCode) {
					// Load home.fxml
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/home.fxml"));
					Scene scene = new Scene(loader.load());

					// Get controller and set user details
					HomeController homeController = loader.getController();
					String role = "admin"; // You may dynamically fetch this
					homeController.setUserRole(role);
					homeController.setLoggedInUsername(user);

					// Show in current window (or optionally a new window)
					Stage stage = (Stage) codeField.getScene().getWindow(); // get current stage
					stage.setTitle("Home Page - Role: " + role);
					stage.setScene(scene);
					stage.show();

					System.out.println("TOTP Verified. Proceeding to home...");
				} else {
					errorLabel.setText("Invalid code. Try again.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorLabel.setText("Enter a valid 6-digit code.");
		}
	}

}
