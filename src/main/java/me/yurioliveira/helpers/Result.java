package me.yurioliveira.helpers;

public class Result<T> {
    private volatile T value;

    public Result(T value) {
        this.value = value;
    }

    public void set(T value) {
        this.value = value;
    }

    public void setIfNotNull(T value) {
        if (value != null) {
            this.value = value;
        }
    }

    public T get() {
        return this.value;
    }

    public static <T> Result<T> notReady() {
        return new Result<>(null);
    }
}