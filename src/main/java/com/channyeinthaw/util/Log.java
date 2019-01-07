package com.channyeinthaw.util;

public class Log {
    private static Logger logger;

    public static void setLogger(Logger l) {
        logger = l;
    }

    public static void setTag(String tag) {
        logger.setTAG(tag);
    }

    public static void setType(Logger.Type type) {
        logger.setType(type);
    }

    public static void info(String message) {logger.info(message);}
    public static void debug(String message) {logger.debug(message);}
    public static void error(String message) {logger.error(message);}
    public static void divider() {logger.divider();}
}
