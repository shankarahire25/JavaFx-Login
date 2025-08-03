package com.authenticate.controller;

import java.io.File;
import java.io.FileWriter;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class RegisterController {

	private static final String USERNAME = "rameshnavnath@gmail.com";
	private static final String SECRET_FILE = "user_secrets/" + USERNAME + ".key";

	@FXML
	private ImageView qrImageView;

	@FXML
	public String initialize() {
		GoogleAuthenticator gAuth = null;
		String otpAuthURL = "";
		
		try {
			
			gAuth = new GoogleAuthenticator();
			// If key already exists, skip
			File file = new File(SECRET_FILE);
			if (file.exists()) {
				System.out.println("Secret already exists.");
				return "";
			}

			GoogleAuthenticatorKey key = gAuth.createCredentials();
			otpAuthURL = GoogleAuthenticatorQRGenerator.getOtpAuthURL("MyApp", USERNAME, key);

			// Save key to file
			file.getParentFile().mkdirs();
			try (FileWriter writer = new FileWriter(file)) {
				writer.write(key.getKey());
			}
			System.out.println("Secret saved: " + key.getKey());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return otpAuthURL;
	}
}