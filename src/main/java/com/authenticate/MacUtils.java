package com.authenticate;
import java.net.NetworkInterface;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MacUtils {
    private static final Logger logger = LoggerFactory.getLogger(MacUtils.class);

    public static String getSystemMacAddress() {
        try {
            for (NetworkInterface ni : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (!ni.isLoopback() && ni.getHardwareAddress() != null) {
                    byte[] mac = ni.getHardwareAddress();
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < mac.length; i++) {
                        sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                    }
                    return sb.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("An error occurred", e);
        }
        return "UNKNOWN";
    }
}
