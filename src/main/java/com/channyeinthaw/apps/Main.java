package com.channyeinthaw.apps;

import java.lang.reflect.Constructor;

public class Main {
    public static void main(String[] args) {
        if(args.length == 0) return;

        try {
            Class targetApp = Class.forName(args[0]);
            Constructor targetConstructor = targetApp.getConstructors()[0];

            AppInterface a = (AppInterface) targetConstructor.newInstance();
            a.run(args);
        } catch (Exception e) {
            System.out.println("Invalid App");
        }
    }
}
