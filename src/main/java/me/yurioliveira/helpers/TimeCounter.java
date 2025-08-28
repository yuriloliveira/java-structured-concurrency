package me.yurioliveira.helpers;

import java.time.Duration;

public class TimeCounter {
    public final Long startTime = System.currentTimeMillis();

    public TimeCounter() { }

    public Duration elapsed() {
        return Duration.ofMillis(System.currentTimeMillis() - startTime);
    }

    public static TimeCounter start() {
        return new TimeCounter();
    }
}