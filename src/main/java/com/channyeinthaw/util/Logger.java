package com.channyeinthaw.util;

import java.io.PrintStream;

public class Logger {
    private Type type;

    private final String DEBUG = "[DEB]";
    private final String INFO = "[INF]";
    private final String ERROR = "[ERR]";
    private String TAG;

    public Logger(Type t, String tag) {
        type = t;
        TAG = tag;
    }

    public void info(String message) {log(LogType.Info, message);}

    public void debug(String message) {log(LogType.Debug, message);}

    public void error(String message) {log(LogType.Error, message);}

    public void divider() {
        System.out.println();
    }

    private void log(LogType lType, String message) {
        switch (lType) {
            case Info: output(INFO, message); break;
            case Debug: output(DEBUG, message); break;
            case Error: output(ERROR, message); break;
        }
    }

    private void output(String type, String message) {
        PrintStream stream = type.equals(ERROR) ? System.err : System.out;

        if (!TAG.isEmpty())
            stream.println(String.format("%s %s %s", type, TAG, message));
        else
            stream.println(String.format("%s %s", type, message));
    }

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type { Console, File }
    public enum LogType { Info, Debug, Error }
}
