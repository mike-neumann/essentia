package me.xra1ny.essentia.schedule.exception;

import lombok.NonNull;

import java.lang.reflect.Method;

public class ScheduledMethodNotStaticException extends RuntimeException {
    public ScheduledMethodNotStaticException(@NonNull Method method) {
        super("method %s must be static"
                .formatted(method.getName()));
    }
}
