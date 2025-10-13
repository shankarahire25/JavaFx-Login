package com.authenticate.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.warrenstrange.googleauth.GoogleAuthenticator;

public class TOTPLoginService {
	
	private static final Logger logger = LoggerFactory.getLogger(TOTPLoginService.class);
    public static boolean verifyCode(String username, int code) throws IOException {
        Path filePath = Paths.get("user_secrets", username + ".key");
        logger.info("filePath:-->"+filePath.toString());
        
        if (!Files.exists(filePath)) {
            throw new IllegalStateException("User not registered for TOTP");
        }

        
        String secret = new String(Files.readAllBytes(filePath));
        logger.info("secret  :--->"+secret);
        logger.info("code  :--->"+code);
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        return gAuth.authorize(secret, code);
    }
}

