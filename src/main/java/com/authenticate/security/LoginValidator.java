package com.authenticate.security;

import com.warrenstrange.googleauth.GoogleAuthenticator;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LoginValidator {

    public static boolean validateCode(String username, int enteredCode) {
        try {
            String path = "user_secrets/" + username + ".key";
            File file = new File(path);
            if (!file.exists()) {
                System.err.println("Secret key file not found for user.");
                return false;
            }

            String secret = new String(Files.readAllBytes(Paths.get(path))).trim();
            GoogleAuthenticator gAuth = new GoogleAuthenticator();
            return gAuth.authorize(secret, enteredCode);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
