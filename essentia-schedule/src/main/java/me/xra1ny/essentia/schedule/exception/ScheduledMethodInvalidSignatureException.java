package me.xra1ny.essentia.schedule.exception;

import lombok.NonNull;

import java.lang.reflect.Method;
import java.util.Arrays;

public class ScheduledMethodInvalidSignatureException extends RuntimeException {
    public ScheduledMethodInvalidSignatureException(@NonNull Method method) {
        super("invalid signature " + Arrays.toString(method.getParameterTypes()) + " for method " + method.getName());
    }
}
