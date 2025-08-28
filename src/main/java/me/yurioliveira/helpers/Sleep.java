package me.yurioliveira.helpers;

public class Sleep {
    public static void sleep(long timeInMillis) {
        try {
            Thread.sleep(timeInMillis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
