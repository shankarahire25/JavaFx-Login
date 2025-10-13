package com.authenticate.security;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.authenticate.MainApp;
import com.warrenstrange.googleauth.GoogleAuthenticator;

public class LoginValidator {
    private static final Logger logger = LoggerFactory.getLogger(MainApp.class);
    
    public static boolean validateCode(String username, int enteredCode) {
        try {
            String path = "user_secrets/" + username + ".key";
            File file = new File(path);
            if (!file.exists()) {
                logger.info("Secret key file not found for user.");               
                return false;
            }

            String secret = new String(Files.readAllBytes(Paths.get(path))).trim();
            logger.info("secret     :--->"+secret);
            logger.info("enteredCode:--->"+enteredCode);
            
            GoogleAuthenticator gAuth = new GoogleAuthenticator();
            return gAuth.authorize(secret, enteredCode);
        } catch (Exception e) {
            logger.error("Error : ",e);
            return false;
        }
    }
}
