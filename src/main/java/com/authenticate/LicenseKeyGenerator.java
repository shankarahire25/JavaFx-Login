package com.authenticate;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class LicenseKeyGenerator {

    private static final String SECRET_KEY = "MySuperSecretKey123";

    public static void generatorKey() {
        try {
            // Input values
            String macAddress = MacUtils.getSystemMacAddress();
            String expiryDate = "2025-06-02";

            String data = macAddress + "=" + expiryDate;
            
            System.out.println("data:--->"+data);
            
            // Generate AES key
            byte[] key = SECRET_KEY.getBytes("UTF-8");
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = java.util.Arrays.copyOf(key, 16); // AES needs 16-byte key
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");

            // Encrypt with PKCS5 padding
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encrypted = cipher.doFinal(data.getBytes("UTF-8"));

            // Encode as Base64
            String encryptedString = Base64.getEncoder().encodeToString(encrypted);

         // Write only if file is missing or empty
            File licenseFile = new File("license.key");
            if (!licenseFile.exists() || Files.readString(licenseFile.toPath()).trim().isEmpty()) {
                try (FileWriter writer = new FileWriter(licenseFile)) {
                    writer.write(encryptedString);
                    System.out.println("Encrypted license written to license.key");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("license.key already exists and is not empty. Skipping write.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
