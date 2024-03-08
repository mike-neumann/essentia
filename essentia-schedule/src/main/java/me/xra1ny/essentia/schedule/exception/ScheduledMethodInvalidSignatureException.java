package me.xra1ny.essentia.schedule.exception;

import lombok.NonNull;

import java.lang.reflect.Method;
import java.util.Arrays;

public class ScheduledMethodInvalidSignatureException extends RuntimeException {
    public ScheduledMethodInvalidSignatureException(@NonNull Method method) {
        super("invalid signature %s for method %s"
                .formatted(Arrays.toString(method.getParameterTypes()), method.getName()));
    }
}
