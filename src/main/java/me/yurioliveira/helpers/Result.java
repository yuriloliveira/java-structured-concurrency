package me.yurioliveira.helpers;

import java.util.stream.Stream;

import static me.yurioliveira.helpers.ThreadAwareLogging.*;

public class Result<T> {
    private volatile T value;

    public Result(T value) {
        this.value = value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public void setIfNotNull(T value) {
        if (value != null) {
            this.value = value;
        }
    }

    public T getValue() {
        return this.value;
    }

    public static <T> Result<T> notReady() {
        return new Result<>(null);
    }

    public static void logAll(Result... results) {
        boolean allComplete = Stream
            .of(results)
            .allMatch(result -> result.getValue() != null);

        if (allComplete) {
            log("All results were complete!", ANSI_GREEN);
        } else {
            log("Some or all results are incomplete...", ANSI_RED);
        }
    }
}