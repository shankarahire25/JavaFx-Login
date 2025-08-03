package com.authenticate.util;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

import java.io.*;
import java.nio.file.*;

public class TOTPRegistration {

    private static final String SECRET_DIR = "totp-secrets";

    public static String registerUser(String username) throws IOException {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        GoogleAuthenticatorKey key = gAuth.createCredentials();

        // Save secret key in a file
        Files.createDirectories(Paths.get(SECRET_DIR));
        Path filePath = Paths.get(SECRET_DIR, username + ".secret");
        Files.write(filePath, key.getKey().getBytes());

        // Generate QR URL for user to scan in Microsoft Authenticator
        String issuer = "MyJavaFXApp";
        String otpAuthURL = GoogleAuthenticatorQRGenerator.getOtpAuthURL(issuer, username, key);

        return otpAuthURL; // show this as a QR or open in browser
    }

    public static boolean isRegistered(String username) {
        return Files.exists(Paths.get(SECRET_DIR, username + ".secret"));
    }
}
