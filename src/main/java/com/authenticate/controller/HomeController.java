package com.authenticate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.stage.Stage;

@Component
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@FXML private Label welcomeLabel;
	@FXML private Menu adminMenu;
	@FXML private Menu managerMenu;
	@FXML private Menu userMenu;
	@FXML private Menu userProfileMenu;

	public void setLoggedInUsername(String username) {
		userProfileMenu.setText(userProfileMenu.getText() + ""+username);
	}

	public void setUserRole(String role) {
		switch (role.toLowerCase()) {
		case "super admin":
			adminMenu.setVisible(true);
			managerMenu.setVisible(true);
			userMenu.setVisible(true);
			break;
		case "admin":
			managerMenu.setVisible(true);
			userMenu.setVisible(true);
			break;
		case "manager":
			userMenu.setVisible(true);
			break;
		default:
			// No extra menus for standard users
			break;
		}
	}

	@FXML
	public void onCreateManager() {
		System.out.println("Create Manager clicked!");
		// You can load a new scene/dialog here
	}

	@FXML
	public void onCreateUser() {
		logger.info("Create User clicked!");
	}

	@FXML
	private void onLogout() {
		logger.info("Logout....");
	}


	@FXML
	private void onMyDetails() {
		Stage stage = (Stage) welcomeLabel.getScene().getWindow();
		stage.close();
	}

	@FXML
	private void onExit() {
		Stage stage = (Stage) welcomeLabel.getScene().getWindow();
		stage.close();
	}
}
