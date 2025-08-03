package com.authenticate.service;

import com.warrenstrange.googleauth.GoogleAuthenticator;

import java.io.IOException;
import java.nio.file.*;

public class TOTPLoginService {

    public static boolean verifyCode(String username, int code) throws IOException {
        Path filePath = Paths.get("user_secrets", username + ".key");
        System.out.println("filePath:-->"+filePath.toString());
        
        if (!Files.exists(filePath)) {
            throw new IllegalStateException("User not registered for TOTP");
        }

        
        String secret = new String(Files.readAllBytes(filePath));
        System.out.println("secret  :--->"+secret);
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        return gAuth.authorize(secret, code);
    }
}

