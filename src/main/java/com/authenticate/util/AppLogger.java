package com.authenticate.util;

import java.util.logging.*;

public class AppLogger {
    private static final Logger logger = Logger.getLogger(AppLogger.class.getName());

    static {
        try {
            LogManager.getLogManager().reset();
            logger.setLevel(Level.ALL);

            // Log file handler
            FileHandler fileHandler = new FileHandler("application.log", true);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);

            // Console handler
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.INFO);
            logger.addHandler(consoleHandler);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Logger getLogger() {
        return logger;
    }
}

