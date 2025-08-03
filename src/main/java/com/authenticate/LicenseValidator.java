package com.authenticate;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class LicenseValidator {

    private static final String SECRET_KEY = "MySuperSecretKey123";

    public static String validateLicense() throws Exception {
    	String errorMessage = "";
    	
        File licenseFile = new File("license.key");
        if (!licenseFile.exists()) {
        	errorMessage ="The license has expired or is invalid.";
        }

        String encrypted = new String(Files.readAllBytes(licenseFile.toPath()), StandardCharsets.UTF_8);
        String decrypted = decrypt(encrypted);

        String[] parts = decrypted.split("=");
        if (parts.length != 2) throw new RuntimeException("Invalid license format");

        String macInLicense = parts[0].trim();
        String expiryDateStr = parts[1].trim();

        String currentMac = MacUtils.getSystemMacAddress();
        if (!macInLicense.equalsIgnoreCase(currentMac)) {
            throw new RuntimeException("MAC address does not match.");
        }
        System.out.println("expiryDateStr:-->"+expiryDateStr);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date expiryDate = sdf.parse(expiryDateStr);
        if (expiryDate.before(new Date())) {
        	errorMessage = "License has expired.";
        	 System.out.println("errorMessage:-->"+errorMessage);
        }
        return errorMessage;
    }

    private static String decrypt(String encrypted) throws Exception {
        byte[] key = SECRET_KEY.getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        key = Arrays.copyOf(sha.digest(key), 16);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"));
        byte[] decoded = Base64.getDecoder().decode(encrypted);
        return new String(cipher.doFinal(decoded));
    }
}
