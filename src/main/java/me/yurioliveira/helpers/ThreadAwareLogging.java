package me.yurioliveira.helpers;

public class ThreadAwareLogging {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static void log(String logStatement, Object... args) {
        System.out.printf("%s[%s]%s ", ANSI_CYAN, Thread.currentThread().getName(), ANSI_RESET);
        System.out.printf(logStatement, args);
    }

    public static void log(String logStatement, String color, Object... args) {
        System.out.printf("%s[%s]%s ", ANSI_CYAN, Thread.currentThread().getName(), ANSI_RESET);
        System.out.print(color);
        System.out.printf(logStatement, args);
        System.out.print(ANSI_RESET);
        System.out.println();
    }
}