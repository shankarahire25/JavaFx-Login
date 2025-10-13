package com.authenticate.util;

import java.io.*;
import java.time.LocalDateTime;

import com.authenticate.MainApp;

public class Launcher {
    public static void main(String[] args) {
        String logDirPath = System.getenv("APPDATA") + "\\JavaFx-Login-1.0";
        File logDir = new File(logDirPath);
        if (!logDir.exists()) logDir.mkdirs();
        File logFile = new File(logDir, "log.txt");
        
        try {
            MainApp.main(args);
        } catch (Throwable t) {
            try (PrintWriter out = new PrintWriter(new FileWriter(logFile, true))) {
                out.println("Startup error at " + LocalDateTime.now());
                t.printStackTrace(out);
            } catch (IOException ignored) {}
            t.printStackTrace();
        }
    }
}
