package com.authenticate.util;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

public class TOTPUtils {
    private static final GoogleAuthenticator gAuth = new GoogleAuthenticator();

    public static GoogleAuthenticatorKey generateSecretKey(String username) {
        return gAuth.createCredentials(username);
    }

    public static boolean verifyCode(String secretKey, int code) {
        return gAuth.authorize(secretKey, code);
    }

    public static String getQRBarcodeURL(String user, String host, GoogleAuthenticatorKey key) {
        return GoogleAuthenticatorQRGenerator.getOtpAuthURL(host, user, key);
    }
}